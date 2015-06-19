package elec332.craftingtableiv.handler;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * Created by Elec332 on 18-6-2015.
 */
public class InventoryWrapper<I extends IInventory> {

    public InventoryWrapper(I i){
        this(i.getSizeInventory());
        for (int j = 0; j < i.getSizeInventory(); j++) {
            ItemStack stack = i.getStackInSlot(j);
            if (stack == null)
                inventoryContent[j] = null;
            else inventoryContent[j] = new StackComparator(stack.copy());
            /*if (stack != null)
                contents.add(new InventoryStackComparator(stack.copy(), j));
            else freeSlots.add(j);
        }
        this.invSize = i.getSizeInventory();*/
        }
    }

    private InventoryWrapper(int i){
        inventoryContent = new StackComparator[i];
        //contents = Lists.newArrayList();
        //freeSlots = Lists.newArrayList();
    }

    private StackComparator[] inventoryContent;

    public void backToInventory(I i){
        for (int j = 0; j < inventoryContent.length; j++) {
            i.setInventorySlotContents(j, inventoryContent[j].getStack());
        }
    }

    public void copyOverFrom(InventoryWrapper<I> inventoryWrapper){
        this.inventoryContent = inventoryWrapper.inventoryContent;
    }

    public InventoryWrapper<I> getCopy(){
        InventoryWrapper<I> ret = new InventoryWrapper<I>(inventoryContent.length);
        for (int i = 0; i < inventoryContent.length; i++) {
            if (inventoryContent[i] == null)
                ret.inventoryContent[i] = null;
            else ret.inventoryContent[i] = inventoryContent[i].getCopy();
        }
        return ret;
    }

    public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
        if(this.inventoryContent[p_70298_1_] != null) {
            ItemStack itemstack;
            if(this.inventoryContent[p_70298_1_].getStack().stackSize <= p_70298_2_) {
                itemstack = this.inventoryContent[p_70298_1_].getStack();
                this.inventoryContent[p_70298_1_] = null;
                return itemstack;
            } else {
                itemstack = this.inventoryContent[p_70298_1_].getStack().splitStack(p_70298_2_);
                if(this.inventoryContent[p_70298_1_].getStack().stackSize == 0) {
                    this.inventoryContent[p_70298_1_] = null;
                }
                return itemstack;
            }
        } else {
            return null;
        }
    }

    public int getFirstSlotWithItemStack(ItemStack stack){
        for (int i = 0; i < inventoryContent.length; i++) {
            if (inventoryContent[i] != null && inventoryContent[i].test(stack))
                return i;
        }
        return -1;
    }

    public boolean addItemStackToInventory(ItemStack aStack) {
        int index = findStack(aStack);
        while (index > -1) {
            ItemStack tarStack = inventoryContent[index].getStack();
            if (tarStack.isStackable())
                if (tarStack.getMaxStackSize() - tarStack.stackSize >= aStack.stackSize) {
                    tarStack.stackSize += aStack.stackSize;
                    aStack.stackSize = 0;
                    inventoryContent[index] = new StackComparator(tarStack);//setInventorySlotContents(index, tarStack);
                } else {
                    aStack.stackSize -= (tarStack.getMaxStackSize() - tarStack.stackSize);
                    tarStack.stackSize = tarStack.getMaxStackSize();
                    inventoryContent[index] = new StackComparator(tarStack);//setInventorySlotContents(index, tarStack);
                }
            if (aStack.stackSize <= 0)
                return true;
            index = findStack(aStack);
        }
        if (aStack.stackSize > 0) {
            index = getFirstEmptyStack();
            if (index > -1) {
                inventoryContent[index] = new StackComparator(aStack);//setInventorySlotContents(index, aStack);
            } else {
                //System.out.println("Cannot add item to TE");
                return false;
            }
        }
        return true;
    }

    public int findStack(ItemStack aStack) {
        for (int i = 0; i < this.inventoryContent.length; i++) {
            if (inventoryContent[i] != null)
                if (inventoryContent[i].getStack().getItem() == aStack.getItem())
                    if (inventoryContent[i].getStack().getHasSubtypes()) {
                        if (inventoryContent[i].getStack().getItemDamage() == aStack.getItemDamage())
                            if (inventoryContent[i].getStack().getMaxStackSize() > inventoryContent[i].getStack().stackSize)
                                return i;
                    } else {
                        if (inventoryContent[i].getStack().getMaxStackSize() > inventoryContent[i].getStack().stackSize)
                            return i;
                    }
        }
        return -1;
    }

    private int getFirstEmptyStack(){
        for (int i = 0; i < inventoryContent.length; i++) {
            if (inventoryContent[i] == null)
                return i;
        }
        return -1;
    }
    /*private List<InventoryStackComparator> contents;
    private List<Integer> freeSlots;
    private int invSize;

    public int getFirstSlotWithItemStack(ItemStack stack){
        for (InventoryStackComparator inventoryStackComparator : contents)
            if (inventoryStackComparator.equals(new StackComparator(stack)))
                return inventoryStackComparator.getIndex();
        return -1;
    }

    public ItemStack decrStackSize(int index, int amount) {
        InventoryStackComparator stackInSlot = null;
        for (InventoryStackComparator inventoryStackComparator : contents)
            if (inventoryStackComparator.getIndex() == index)
                stackInSlot = inventoryStackComparator;
        if(stackInSlot != null) {
            ItemStack itemstack;
            if(stackInSlot.stack.stackSize <= amount) {
                itemstack = stackInSlot.stack;
                stackInSlot.stack = null;
                remap();
                return itemstack;
            } else {
                itemstack = stackInSlot.stack.splitStack(amount);
                if(stackInSlot.stack.stackSize == 0) {
                    stackInSlot.stack = null;
                    remap();
                }
                return itemstack;
            }
        } else {
            return null;
        }
    }

    private InventoryStackComparator getFirstStackWithItemStack(ItemStack stack){
        for (InventoryStackComparator inventoryStackComparator : contents)
            if (inventoryStackComparator.equals(new StackComparator(stack)))
                return inventoryStackComparator;
        return null;
    }

    public boolean addItemStackToInventory(ItemStack aStack) {
        InventoryStackComparator isc = getFirstStackWithItemStack(aStack);
        while (isc != null) {
            ItemStack tarStack = isc.getStack();
            if (tarStack.isStackable())
                if (tarStack.getMaxStackSize() - tarStack.stackSize >= aStack.stackSize) {
                    tarStack.stackSize += aStack.stackSize;
                    aStack.stackSize = 0;
                    isc.stack = tarStack;//setInventorySlotContents(isc, tarStack);
                } else {
                    aStack.stackSize -= (tarStack.getMaxStackSize() - tarStack.stackSize);
                    tarStack.stackSize = tarStack.getMaxStackSize();
                    isc.stack = tarStack;//setInventorySlotContents(isc, tarStack);
                }
            if (aStack.stackSize <= 0)
                return true;
            isc = getFirstStackWithItemStack(aStack);
        }
        if (aStack.stackSize > 0) {
            int index = getFreeSlot();
            if (index > -1) {
                freeSlots.remove(0);
                contents.add(new InventoryStackComparator(aStack, index));//setInventorySlotContents(isc, aStack);
            } else {
                //System.out.println("Cannot add item to TE");
                return false;
            }
        }
        return true;
    }

    public int getFreeSlot(){
        /*List<Integer> sigh = Lists.newArrayList();
        for (int i = 0; i < invSize; i++) {
            sigh.add(i);
        }
        for (InventoryStackComparator isc : contents)
            sigh.remove(isc.getIndex());
        if (sigh.size() > 0)
            return sigh.get(0);
        return -1;*/
        /*
        if (freeSlots.size() > 0)
            return freeSlots.get(0);
        return -1;
    }

    private void remap(){
        List<InventoryStackComparator> toReplace = Lists.newArrayList();
        for (InventoryStackComparator isc : contents)
            if (isc.stack != null)
                toReplace.add(isc);
        contents = toReplace;
    }

    public void backToInventory(I i){
        for (InventoryStackComparator inventoryStackComparator : contents){
            i.setInventorySlotContents(inventoryStackComparator.getIndex(), inventoryStackComparator.getStack());
        }
    }

    public void copyOverFrom(InventoryWrapper<I> inventoryWrapper){
        this.contents.clear();
        this.contents = inventoryWrapper.contents;
    }

    public InventoryWrapper<I> getCopy(){
        InventoryWrapper<I> ret = new InventoryWrapper<I>();
        for (InventoryStackComparator isc : contents)
            ret.contents.add(isc.getCopy());
        for (int i : freeSlots)
            ret.freeSlots.add(i);
        ret.invSize = invSize;
        return ret;
    }

    private class InventoryStackComparator extends StackComparator{
        private InventoryStackComparator(ItemStack stack, int i) {
            super(stack);
            this.i = i;
        }

        private int i;

        public int getIndex() {
            return i;
        }

        @Override
        public InventoryStackComparator getCopy() {
            return new InventoryStackComparator(stack.copy(), i);
        }
    }*/
}

package elec332.craftingtableiv.abstraction.handler;

import net.minecraft.item.ItemStack;

/**
 * Created by Elec332 on 7-6-2015.
 */
public class ItemComparator {

    public ItemComparator(ItemStack stack){
        this.stack = stack;
        if (stack == null || stack.getItem() == null)
            throw new IllegalArgumentException("Invalid ItemStack!");
    }

    protected final ItemStack stack;

    public ItemStack getStack() {
        return stack.copy();
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ItemStack ? stacksEqual((ItemStack) obj) : ((obj instanceof ItemComparator) && stacksEqual(((ItemComparator) obj).getStack()));
    }

    public boolean stacksEqual(ItemStack s1){
        return s1.getItem() == stack.getItem();
    }
}

package elec332.craftingtableiv.handler;

import elec332.core.helper.OredictHelper;
import net.minecraft.item.ItemStack;

/**
 * Created by Elec332 on 16-6-2015.
 */
public class RecipeStackComparator extends StackComparator {

    public RecipeStackComparator(ItemStack stack) {
        this(stack, OredictHelper.getOreName(stack), true);
    }

    private RecipeStackComparator(ItemStack stack, String s, boolean b){
        super(stack);
        this.string = s;
        this.b = b;
    }

    public RecipeStackComparator setCompareOre(boolean b) {
        this.b = b;
        return this;
    }

    private String string;
    private boolean b;

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof RecipeStackComparator && ((b && ((RecipeStackComparator) obj).b) && string.equals(((RecipeStackComparator) obj).string))) || super.equals(obj);
    }

    @Override
    public RecipeStackComparator getCopy() {
        return new RecipeStackComparator(stack.copy(), string, b);
    }
}

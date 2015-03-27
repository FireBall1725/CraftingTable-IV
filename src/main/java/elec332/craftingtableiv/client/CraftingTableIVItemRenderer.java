package elec332.craftingtableiv.client;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

/**
 * Created by Elec332 on 24-3-2015.
 */
public class CraftingTableIVItemRenderer implements IItemRenderer {
    private ModelCraftingTableIV modelCraftingTableIV;

    public CraftingTableIVItemRenderer() {
        modelCraftingTableIV = new ModelCraftingTableIV();
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        switch (type)
        {
            case ENTITY:
                return true;
            case EQUIPPED:
                return true;
            case EQUIPPED_FIRST_PERSON:
                return true;
            case INVENTORY:
                return true;
            default:
                return false;
        }
    }

    private void render(float x, float y, float z) {
        GL11.glPushMatrix();
        GL11.glTranslatef(x + 0.5f, y, z + 0.5f);
        ResourceLocation test = new ResourceLocation("craftingtableiv", "blocktextures/ctiv.png");
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(test);
        modelCraftingTableIV.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }

    @Override
    public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data){
        switch (type) {
            case ENTITY:
                render(0, 0, 0);
                break;
            case EQUIPPED:
                render(0, 0, 0.5f);
                break;
            case EQUIPPED_FIRST_PERSON:
                render(+0.5f, 0.5f, +0.5f);
                break;
            case INVENTORY:
                render(-0.5f, -0.5f, -0.5f);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper) {
        return true;
    }
}
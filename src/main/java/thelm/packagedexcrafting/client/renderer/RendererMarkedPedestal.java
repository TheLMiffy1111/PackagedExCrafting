package thelm.packagedexcrafting.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import thelm.packagedauto.client.RenderTimer;
import thelm.packagedexcrafting.tile.TileMarkedPedestal;

public class RendererMarkedPedestal extends TileEntitySpecialRenderer<TileMarkedPedestal> {

	@Override
	public void render(TileMarkedPedestal te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		ItemStack stack = te.getInventory().getStackInSlot(0);
		if(!stack.isEmpty()) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(x+0.5, y+1.4, z+0.5);
			GlStateManager.scale(0.5, 0.5, 0.5);
			double tick = (RenderTimer.INSTANCE.getTicks()+partialTicks)/16D;
			GlStateManager.translate(0, Math.sin(tick % (Math.PI*2))*0.065, 0);
			GlStateManager.rotate((float)tick*40 % 360, 0F, 1F, 0F);
			GlStateManager.disableLighting();
			GlStateManager.pushAttrib();
			RenderHelper.enableStandardItemLighting();
			GlStateManager.color(1, 1, 1, 1);
			Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
			RenderHelper.disableStandardItemLighting();
			GlStateManager.popAttrib();
			GlStateManager.enableLighting();
			GlStateManager.popMatrix();
		}
	}
}

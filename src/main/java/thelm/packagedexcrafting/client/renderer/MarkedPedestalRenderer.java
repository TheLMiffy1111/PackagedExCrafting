package thelm.packagedexcrafting.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import thelm.packagedexcrafting.tile.MarkedPedestalTile;

public class MarkedPedestalRenderer extends TileEntityRenderer<MarkedPedestalTile> {

	public MarkedPedestalRenderer(TileEntityRendererDispatcher rendererDispatcher) {
		super(rendererDispatcher);
	}

	@Override
	public void render(MarkedPedestalTile tile, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
		Minecraft minecraft = Minecraft.getInstance();
		ItemStack stack = tile.getItemHandler().getStackInSlot(0);
		if(!stack.isEmpty()) {
			matrixStack.push();
			matrixStack.translate(0.5, 1.2, 0.5);
			float scale = stack.getItem() instanceof BlockItem ? 0.9F : 0.65F;
			matrixStack.scale(scale, scale, scale);
			double tick = System.currentTimeMillis() / 800D;
			matrixStack.translate(0, Math.sin(tick % (Math.PI*2)) * 0.065, 0);
			matrixStack.rotate(Vector3f.YP.rotationDegrees((float)(tick*40 % 360)));
			minecraft.getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.GROUND, combinedLight, combinedOverlay, matrixStack, buffer);
			matrixStack.pop();
		}
	}
}

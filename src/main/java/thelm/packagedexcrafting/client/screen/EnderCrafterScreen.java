package thelm.packagedexcrafting.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import thelm.packagedauto.client.screen.BaseScreen;
import thelm.packagedexcrafting.container.EnderCrafterContainer;

public class EnderCrafterScreen extends BaseScreen<EnderCrafterContainer> {

	public static final ResourceLocation BACKGROUND = new ResourceLocation("packagedexcrafting:textures/gui/ender_crafter.png");

	public EnderCrafterScreen(EnderCrafterContainer container, PlayerInventory playerInventory, ITextComponent title) {
		super(container, playerInventory, title);
	}

	@Override
	protected ResourceLocation getBackgroundTexture() {
		return BACKGROUND;
	}

	@Override
	protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
		super.renderBg(matrixStack, partialTicks, mouseX, mouseY);
		blit(matrixStack, leftPos+102, topPos+35, 176, 0, menu.tile.getScaledProgress(22), 16);
		int scaledEnergy = menu.tile.getScaledEnergy(40);
		blit(matrixStack, leftPos+10, topPos+10+40-scaledEnergy, 176, 16+40-scaledEnergy, 12, scaledEnergy);
	}

	@Override
	protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
		String s = menu.tile.getDisplayName().getString();
		font.draw(matrixStack, s, imageWidth/2 - font.width(s)/2, 6, 0x404040);
		font.draw(matrixStack, menu.playerInventory.getDisplayName().getString(), menu.getPlayerInvX(), menu.getPlayerInvY()-11, 0x404040);
		if(mouseX-leftPos >= 10 && mouseY-topPos >= 10 && mouseX-leftPos <= 21 && mouseY-topPos <= 49) {
			renderTooltip(matrixStack, new StringTextComponent(menu.tile.getEnergyStorage().getEnergyStored()+" / "+menu.tile.getEnergyStorage().getMaxEnergyStored()+" FE"), mouseX-leftPos, mouseY-topPos);
		}
	}
}

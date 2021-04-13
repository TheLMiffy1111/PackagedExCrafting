package thelm.packagedexcrafting.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import thelm.packagedauto.client.screen.BaseScreen;
import thelm.packagedexcrafting.container.CombinationCrafterContainer;

public class CombinationCrafterScreen extends BaseScreen<CombinationCrafterContainer> {

	public static final ResourceLocation BACKGROUND = new ResourceLocation("packagedexcrafting:textures/gui/combination_crafter.png");

	public CombinationCrafterScreen(CombinationCrafterContainer container, PlayerInventory playerInventory, ITextComponent title) {
		super(container, playerInventory, title);
	}

	@Override
	protected ResourceLocation getBackgroundTexture() {
		return BACKGROUND;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(matrixStack, partialTicks, mouseX, mouseY);
		blit(matrixStack, guiLeft+75, guiTop+35, 176, 0, container.tile.getScaledProgress(22), 16);
		int scaledEnergy = container.tile.getScaledEnergy(40);
		blit(matrixStack, guiLeft+10, guiTop+10+40-scaledEnergy, 176, 16+40-scaledEnergy, 12, scaledEnergy);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
		String s = container.tile.getDisplayName().getString();
		font.drawString(matrixStack, s, Math.max(25, xSize/2 - font.getStringWidth(s)/2), 6, 0x404040);
		font.drawString(matrixStack, container.playerInventory.getDisplayName().getString(), container.getPlayerInvX(), container.getPlayerInvY()-11, 0x404040);
		if(mouseX-guiLeft >= 10 && mouseY-guiTop >= 10 && mouseX-guiLeft <= 21 && mouseY-guiTop <= 49) {
			renderTooltip(matrixStack, new StringTextComponent(container.tile.getEnergyStorage().getEnergyStored()+" / "+container.tile.getEnergyStorage().getMaxEnergyStored()+" FE"), mouseX-guiLeft, mouseY-guiTop);
		}
	}
}

package thelm.packagedexcrafting.client.gui;

import net.minecraft.util.ResourceLocation;
import thelm.packagedauto.client.gui.GuiContainerTileBase;
import thelm.packagedexcrafting.container.ContainerUltimateCrafter;

public class GuiUltimateCrafter extends GuiContainerTileBase<ContainerUltimateCrafter> {

	public static final ResourceLocation BACKGROUND = new ResourceLocation("packagedexcrafting:textures/gui/ultimate_crafter.png");

	public GuiUltimateCrafter(ContainerUltimateCrafter container) {
		super(container);
		xSize = 270;
		ySize = 274;
	}

	@Override
	protected ResourceLocation getBackgroundTexture() {
		return BACKGROUND;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		drawModalRectWithCustomSizedTexture(guiLeft+210, guiTop+89, 270, 0, container.tile.getScaledProgress(22), 16, 512, 512);
		int scaledEnergy = container.tile.getScaledEnergy(40);
		drawModalRectWithCustomSizedTexture(guiLeft+10, guiTop+64+40-scaledEnergy, 270, 16+40-scaledEnergy, 12, scaledEnergy, 512, 512);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		String s = container.inventory.getDisplayName().getUnformattedText();
		fontRenderer.drawString(s, xSize/2 - fontRenderer.getStringWidth(s)/2, 6, 0x404040);
		fontRenderer.drawString(container.playerInventory.getDisplayName().getUnformattedText(), container.getPlayerInvX(), container.getPlayerInvY()-11, 0x404040);
		if(mouseX-guiLeft >= 10 && mouseY-guiTop >= 64 && mouseX-guiLeft <= 21 && mouseY-guiTop <= 103) {
			drawHoveringText(container.tile.getEnergyStorage().getEnergyStored()+" / "+container.tile.getEnergyStorage().getMaxEnergyStored()+" FE", mouseX-guiLeft, mouseY-guiTop);
		}
	}
}

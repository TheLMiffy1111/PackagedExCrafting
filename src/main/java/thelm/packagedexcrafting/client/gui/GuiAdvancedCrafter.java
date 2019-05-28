package thelm.packagedexcrafting.client.gui;

import net.minecraft.util.ResourceLocation;
import thelm.packagedauto.client.gui.GuiContainerTileBase;
import thelm.packagedexcrafting.container.ContainerAdvancedCrafter;

public class GuiAdvancedCrafter extends GuiContainerTileBase<ContainerAdvancedCrafter> {

	public static final ResourceLocation BACKGROUND = new ResourceLocation("packagedexcrafting:textures/gui/advanced_crafter.png");

	public GuiAdvancedCrafter(ContainerAdvancedCrafter container) {
		super(container);
		xSize = 198;
		ySize = 202;
	}

	@Override
	protected ResourceLocation getBackgroundTexture() {
		return BACKGROUND;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		drawTexturedModalRect(guiLeft+138, guiTop+53, 198, 0, container.tile.getScaledProgress(22), 16);
		int scaledEnergy = container.tile.getScaledEnergy(40);
		drawTexturedModalRect(guiLeft+10, guiTop+28+40-scaledEnergy, 198, 16+40-scaledEnergy, 12, scaledEnergy);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		String s = container.inventory.getDisplayName().getUnformattedText();
		fontRenderer.drawString(s, xSize/2 - fontRenderer.getStringWidth(s)/2, 6, 0x404040);
		fontRenderer.drawString(container.playerInventory.getDisplayName().getUnformattedText(), container.getPlayerInvX(), container.getPlayerInvY()-11, 0x404040);
		if(mouseX-guiLeft >= 10 && mouseY-guiTop >= 28 && mouseX-guiLeft <= 21 && mouseY-guiTop <= 67) {
			drawHoveringText(container.tile.getEnergyStorage().getEnergyStored()+" / "+container.tile.getEnergyStorage().getMaxEnergyStored()+" FE", mouseX-guiLeft, mouseY-guiTop);
		}
	}
}

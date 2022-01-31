package thelm.packagedexcrafting.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import thelm.packagedauto.client.screen.BaseScreen;
import thelm.packagedexcrafting.menu.UltimateCrafterMenu;

public class UltimateCrafterScreen extends BaseScreen<UltimateCrafterMenu> {

	public static final ResourceLocation BACKGROUND = new ResourceLocation("packagedexcrafting:textures/gui/ultimate_crafter.png");

	public UltimateCrafterScreen(UltimateCrafterMenu menu, Inventory inventory, Component title) {
		super(menu, inventory, title);
		imageWidth = 270;
		imageHeight = 274;
	}

	@Override
	protected ResourceLocation getBackgroundTexture() {
		return BACKGROUND;
	}

	@Override
	protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
		super.renderBg(poseStack, partialTicks, mouseX, mouseY);
		blit(poseStack, leftPos+210, topPos+89, 270, 0, menu.blockEntity.getScaledProgress(22), 16, 512, 512);
		int scaledEnergy = menu.blockEntity.getScaledEnergy(40);
		blit(poseStack, leftPos+10, topPos+64+40-scaledEnergy, 270, 16+40-scaledEnergy, 12, scaledEnergy, 512, 512);
	}

	@Override
	protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
		String s = menu.blockEntity.getDisplayName().getString();
		font.draw(poseStack, s, imageWidth/2 - font.width(s)/2, 6, 0x404040);
		font.draw(poseStack, menu.inventory.getDisplayName().getString(), menu.getPlayerInvX(), menu.getPlayerInvY()-11, 0x404040);
		if(mouseX-leftPos >= 10 && mouseY-topPos >= 64 && mouseX-leftPos <= 21 && mouseY-topPos <= 103) {
			renderTooltip(poseStack, new TextComponent(menu.blockEntity.getEnergyStorage().getEnergyStored()+" / "+menu.blockEntity.getEnergyStorage().getMaxEnergyStored()+" FE"), mouseX-leftPos, mouseY-topPos);
		}
	}
}

package thelm.packagedexcrafting.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import thelm.packagedauto.client.screen.BaseScreen;
import thelm.packagedexcrafting.menu.AdvancedCrafterMenu;

public class AdvancedCrafterScreen extends BaseScreen<AdvancedCrafterMenu> {

	public static final ResourceLocation BACKGROUND = new ResourceLocation("packagedexcrafting:textures/gui/advanced_crafter.png");

	public AdvancedCrafterScreen(AdvancedCrafterMenu menu, Inventory inventory, Component title) {
		super(menu, inventory, title);
		imageWidth = 198;
		imageHeight = 202;
	}

	@Override
	protected ResourceLocation getBackgroundTexture() {
		return BACKGROUND;
	}

	@Override
	protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
		super.renderBg(poseStack, partialTicks, mouseX, mouseY);
		blit(poseStack, leftPos+138, topPos+53, 198, 0, menu.blockEntity.getScaledProgress(22), 16);
		int scaledEnergy = menu.blockEntity.getScaledEnergy(40);
		blit(poseStack, leftPos+10, topPos+28+40-scaledEnergy, 198, 16+40-scaledEnergy, 12, scaledEnergy);
	}

	@Override
	protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
		String s = menu.blockEntity.getDisplayName().getString();
		font.draw(poseStack, s, imageWidth/2 - font.width(s)/2, 6, 0x404040);
		font.draw(poseStack, menu.inventory.getDisplayName().getString(), menu.getPlayerInvX(), menu.getPlayerInvY()-11, 0x404040);
		if(mouseX-leftPos >= 10 && mouseY-topPos >= 28 && mouseX-leftPos <= 21 && mouseY-topPos <= 67) {
			renderTooltip(poseStack, Component.literal(menu.blockEntity.getEnergyStorage().getEnergyStored()+" / "+menu.blockEntity.getEnergyStorage().getMaxEnergyStored()+" FE"), mouseX-leftPos, mouseY-topPos);
		}
	}
}

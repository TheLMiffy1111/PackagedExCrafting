package thelm.packagedexcrafting.client.event;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import thelm.packagedexcrafting.block.entity.CombinationCrafterBlockEntity;
import thelm.packagedexcrafting.block.entity.MarkedPedestalBlockEntity;
import thelm.packagedexcrafting.client.renderer.CombinationCrafterRenderer;
import thelm.packagedexcrafting.client.renderer.MarkedPedestalRenderer;
import thelm.packagedexcrafting.client.screen.AdvancedCrafterScreen;
import thelm.packagedexcrafting.client.screen.BasicCrafterScreen;
import thelm.packagedexcrafting.client.screen.CombinationCrafterScreen;
import thelm.packagedexcrafting.client.screen.EliteCrafterScreen;
import thelm.packagedexcrafting.client.screen.EnderCrafterScreen;
import thelm.packagedexcrafting.client.screen.FluxCrafterScreen;
import thelm.packagedexcrafting.client.screen.UltimateCrafterScreen;
import thelm.packagedexcrafting.menu.AdvancedCrafterMenu;
import thelm.packagedexcrafting.menu.BasicCrafterMenu;
import thelm.packagedexcrafting.menu.CombinationCrafterMenu;
import thelm.packagedexcrafting.menu.EliteCrafterMenu;
import thelm.packagedexcrafting.menu.EnderCrafterMenu;
import thelm.packagedexcrafting.menu.FluxCrafterMenu;
import thelm.packagedexcrafting.menu.UltimateCrafterMenu;

public class ClientEventHandler {

	public static final ClientEventHandler INSTANCE = new ClientEventHandler();

	public static ClientEventHandler getInstance() {
		return INSTANCE;
	}

	public void onConstruct() {
		FMLJavaModLoadingContext.get().getModEventBus().register(this);
	}

	@SubscribeEvent
	public void onClientSetup(FMLClientSetupEvent event) {
		MenuScreens.register(BasicCrafterMenu.TYPE_INSTANCE, BasicCrafterScreen::new);
		MenuScreens.register(AdvancedCrafterMenu.TYPE_INSTANCE, AdvancedCrafterScreen::new);
		MenuScreens.register(EliteCrafterMenu.TYPE_INSTANCE, EliteCrafterScreen::new);
		MenuScreens.register(UltimateCrafterMenu.TYPE_INSTANCE, UltimateCrafterScreen::new);
		MenuScreens.register(EnderCrafterMenu.TYPE_INSTANCE, EnderCrafterScreen::new);
		MenuScreens.register(FluxCrafterMenu.TYPE_INSTANCE, FluxCrafterScreen::new);
		MenuScreens.register(CombinationCrafterMenu.TYPE_INSTANCE, CombinationCrafterScreen::new);

		BlockEntityRenderers.register(CombinationCrafterBlockEntity.TYPE_INSTANCE, CombinationCrafterRenderer::new);
		BlockEntityRenderers.register(MarkedPedestalBlockEntity.TYPE_INSTANCE, MarkedPedestalRenderer::new);
	}
}

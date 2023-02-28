package thelm.packagedexcrafting.client.event;

import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import thelm.packagedexcrafting.client.renderer.CombinationCrafterRenderer;
import thelm.packagedexcrafting.client.renderer.MarkedPedestalRenderer;
import thelm.packagedexcrafting.client.screen.AdvancedCrafterScreen;
import thelm.packagedexcrafting.client.screen.BasicCrafterScreen;
import thelm.packagedexcrafting.client.screen.CombinationCrafterScreen;
import thelm.packagedexcrafting.client.screen.EliteCrafterScreen;
import thelm.packagedexcrafting.client.screen.EnderCrafterScreen;
import thelm.packagedexcrafting.client.screen.UltimateCrafterScreen;
import thelm.packagedexcrafting.container.AdvancedCrafterContainer;
import thelm.packagedexcrafting.container.BasicCrafterContainer;
import thelm.packagedexcrafting.container.CombinationCrafterContainer;
import thelm.packagedexcrafting.container.EliteCrafterContainer;
import thelm.packagedexcrafting.container.EnderCrafterContainer;
import thelm.packagedexcrafting.container.UltimateCrafterContainer;
import thelm.packagedexcrafting.tile.CombinationCrafterTile;
import thelm.packagedexcrafting.tile.MarkedPedestalTile;

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
		ScreenManager.register(BasicCrafterContainer.TYPE_INSTANCE, BasicCrafterScreen::new);
		ScreenManager.register(AdvancedCrafterContainer.TYPE_INSTANCE, AdvancedCrafterScreen::new);
		ScreenManager.register(EliteCrafterContainer.TYPE_INSTANCE, EliteCrafterScreen::new);
		ScreenManager.register(UltimateCrafterContainer.TYPE_INSTANCE, UltimateCrafterScreen::new);
		ScreenManager.register(EnderCrafterContainer.TYPE_INSTANCE, EnderCrafterScreen::new);
		ScreenManager.register(CombinationCrafterContainer.TYPE_INSTANCE, CombinationCrafterScreen::new);

		ClientRegistry.bindTileEntityRenderer(CombinationCrafterTile.TYPE_INSTANCE, CombinationCrafterRenderer::new);
		ClientRegistry.bindTileEntityRenderer(MarkedPedestalTile.TYPE_INSTANCE, MarkedPedestalRenderer::new);
	}
}

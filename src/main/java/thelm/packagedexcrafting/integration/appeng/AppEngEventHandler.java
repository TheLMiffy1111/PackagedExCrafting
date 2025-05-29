package thelm.packagedexcrafting.integration.appeng;

import appeng.api.AECapabilities;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import thelm.packagedauto.integration.appeng.AppEngUtil;
import thelm.packagedexcrafting.block.entity.PackagedExCraftingBlockEntities;

public class AppEngEventHandler {

	public static final AppEngEventHandler INSTANCE = new AppEngEventHandler();

	public static AppEngEventHandler getInstance() {
		return INSTANCE;
	}

	@SubscribeEvent
	public void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(AECapabilities.IN_WORLD_GRID_NODE_HOST, PackagedExCraftingBlockEntities.BASIC_CRAFTER.get(), (be, v)->AppEngUtil.getAsInWorldGridNodeHost(be));
		event.registerBlockEntity(AECapabilities.IN_WORLD_GRID_NODE_HOST, PackagedExCraftingBlockEntities.ADVANCED_CRAFTER.get(), (be, v)->AppEngUtil.getAsInWorldGridNodeHost(be));
		event.registerBlockEntity(AECapabilities.IN_WORLD_GRID_NODE_HOST, PackagedExCraftingBlockEntities.ELITE_CRAFTER.get(), (be, v)->AppEngUtil.getAsInWorldGridNodeHost(be));
		event.registerBlockEntity(AECapabilities.IN_WORLD_GRID_NODE_HOST, PackagedExCraftingBlockEntities.ULTIMATE_CRAFTER.get(), (be, v)->AppEngUtil.getAsInWorldGridNodeHost(be));
		event.registerBlockEntity(AECapabilities.IN_WORLD_GRID_NODE_HOST, PackagedExCraftingBlockEntities.ENDER_CRAFTER.get(), (be, v)->AppEngUtil.getAsInWorldGridNodeHost(be));
		event.registerBlockEntity(AECapabilities.IN_WORLD_GRID_NODE_HOST, PackagedExCraftingBlockEntities.FLUX_CRAFTER.get(), (be, v)->AppEngUtil.getAsInWorldGridNodeHost(be));
		event.registerBlockEntity(AECapabilities.IN_WORLD_GRID_NODE_HOST, PackagedExCraftingBlockEntities.COMBINATION_CRAFTER.get(), (be, v)->AppEngUtil.getAsInWorldGridNodeHost(be));
		event.registerBlockEntity(AECapabilities.IN_WORLD_GRID_NODE_HOST, PackagedExCraftingBlockEntities.MARKED_PEDESTAL.get(), (be, v)->AppEngUtil.getAsInWorldGridNodeHost(be));
	}
}

package thelm.packagedexcrafting.event;

import appeng.api.AECapabilities;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import thelm.packagedauto.block.entity.BaseBlockEntity;
import thelm.packagedauto.integration.appeng.AppEngUtil;
import thelm.packagedauto.util.ApiImpl;
import thelm.packagedauto.util.MiscHelper;
import thelm.packagedexcrafting.block.PackagedExCraftingBlocks;
import thelm.packagedexcrafting.block.entity.PackagedExCraftingBlockEntities;
import thelm.packagedexcrafting.config.PackagedExCraftingConfig;
import thelm.packagedexcrafting.creativetab.PackagedExCraftingCreativeTabs;
import thelm.packagedexcrafting.item.PackagedExCraftingItems;
import thelm.packagedexcrafting.menu.PackagedExCraftingMenus;
import thelm.packagedexcrafting.recipe.AdvancedPackageRecipeType;
import thelm.packagedexcrafting.recipe.BasicPackageRecipeType;
import thelm.packagedexcrafting.recipe.CombinationPackageRecipeType;
import thelm.packagedexcrafting.recipe.ElitePackageRecipeType;
import thelm.packagedexcrafting.recipe.EnderPackageRecipeType;
import thelm.packagedexcrafting.recipe.FluxPackageRecipeType;
import thelm.packagedexcrafting.recipe.UltimatePackageRecipeType;

public class CommonEventHandler {

	public static final CommonEventHandler INSTANCE = new CommonEventHandler();

	public static CommonEventHandler getInstance() {
		return INSTANCE;
	}

	public void onConstruct(IEventBus modEventBus, ModContainer modContainer) {
		modEventBus.register(this);
		PackagedExCraftingConfig.registerConfig(modContainer);

		PackagedExCraftingBlocks.BLOCKS.register(modEventBus);
		PackagedExCraftingItems.ITEMS.register(modEventBus);
		PackagedExCraftingBlockEntities.BLOCK_ENTITIES.register(modEventBus);
		PackagedExCraftingMenus.MENUS.register(modEventBus);
		PackagedExCraftingCreativeTabs.CREATIVE_TABS.register(modEventBus);
	}

	@SubscribeEvent
	public void onCommonSetup(FMLCommonSetupEvent event) {
		ApiImpl.INSTANCE.registerRecipeType(BasicPackageRecipeType.INSTANCE);
		ApiImpl.INSTANCE.registerRecipeType(AdvancedPackageRecipeType.INSTANCE);
		ApiImpl.INSTANCE.registerRecipeType(ElitePackageRecipeType.INSTANCE);
		ApiImpl.INSTANCE.registerRecipeType(UltimatePackageRecipeType.INSTANCE);
		ApiImpl.INSTANCE.registerRecipeType(EnderPackageRecipeType.INSTANCE);
		ApiImpl.INSTANCE.registerRecipeType(FluxPackageRecipeType.INSTANCE);
		ApiImpl.INSTANCE.registerRecipeType(CombinationPackageRecipeType.INSTANCE);
	}

	@SubscribeEvent
	public void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, PackagedExCraftingBlockEntities.BASIC_CRAFTER.get(), BaseBlockEntity::getItemHandler);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, PackagedExCraftingBlockEntities.ADVANCED_CRAFTER.get(), BaseBlockEntity::getItemHandler);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, PackagedExCraftingBlockEntities.ELITE_CRAFTER.get(), BaseBlockEntity::getItemHandler);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, PackagedExCraftingBlockEntities.ULTIMATE_CRAFTER.get(), BaseBlockEntity::getItemHandler);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, PackagedExCraftingBlockEntities.ENDER_CRAFTER.get(), BaseBlockEntity::getItemHandler);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, PackagedExCraftingBlockEntities.FLUX_CRAFTER.get(), BaseBlockEntity::getItemHandler);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, PackagedExCraftingBlockEntities.COMBINATION_CRAFTER.get(), BaseBlockEntity::getItemHandler);

		event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, PackagedExCraftingBlockEntities.BASIC_CRAFTER.get(), BaseBlockEntity::getEnergyStorage);
		event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, PackagedExCraftingBlockEntities.ADVANCED_CRAFTER.get(), BaseBlockEntity::getEnergyStorage);
		event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, PackagedExCraftingBlockEntities.ELITE_CRAFTER.get(), BaseBlockEntity::getEnergyStorage);
		event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, PackagedExCraftingBlockEntities.ULTIMATE_CRAFTER.get(), BaseBlockEntity::getEnergyStorage);
		event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, PackagedExCraftingBlockEntities.ENDER_CRAFTER.get(), BaseBlockEntity::getEnergyStorage);
		event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, PackagedExCraftingBlockEntities.FLUX_CRAFTER.get(), BaseBlockEntity::getEnergyStorage);
		event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, PackagedExCraftingBlockEntities.COMBINATION_CRAFTER.get(), BaseBlockEntity::getEnergyStorage);

		MiscHelper.INSTANCE.conditionalRunnable(()->ModList.get().isLoaded("ae2"), ()->()->{
			event.registerBlockEntity(AECapabilities.IN_WORLD_GRID_NODE_HOST, PackagedExCraftingBlockEntities.BASIC_CRAFTER.get(), (be, v)->AppEngUtil.getAsInWorldGridNodeHost(be));
			event.registerBlockEntity(AECapabilities.IN_WORLD_GRID_NODE_HOST, PackagedExCraftingBlockEntities.ADVANCED_CRAFTER.get(), (be, v)->AppEngUtil.getAsInWorldGridNodeHost(be));
			event.registerBlockEntity(AECapabilities.IN_WORLD_GRID_NODE_HOST, PackagedExCraftingBlockEntities.ELITE_CRAFTER.get(), (be, v)->AppEngUtil.getAsInWorldGridNodeHost(be));
			event.registerBlockEntity(AECapabilities.IN_WORLD_GRID_NODE_HOST, PackagedExCraftingBlockEntities.ULTIMATE_CRAFTER.get(), (be, v)->AppEngUtil.getAsInWorldGridNodeHost(be));
			event.registerBlockEntity(AECapabilities.IN_WORLD_GRID_NODE_HOST, PackagedExCraftingBlockEntities.ENDER_CRAFTER.get(), (be, v)->AppEngUtil.getAsInWorldGridNodeHost(be));
			event.registerBlockEntity(AECapabilities.IN_WORLD_GRID_NODE_HOST, PackagedExCraftingBlockEntities.FLUX_CRAFTER.get(), (be, v)->AppEngUtil.getAsInWorldGridNodeHost(be));
			event.registerBlockEntity(AECapabilities.IN_WORLD_GRID_NODE_HOST, PackagedExCraftingBlockEntities.COMBINATION_CRAFTER.get(), (be, v)->AppEngUtil.getAsInWorldGridNodeHost(be));
			event.registerBlockEntity(AECapabilities.IN_WORLD_GRID_NODE_HOST, PackagedExCraftingBlockEntities.MARKED_PEDESTAL.get(), (be, v)->AppEngUtil.getAsInWorldGridNodeHost(be));
		}, ()->()->{}).run();
	}

	@SubscribeEvent
	public void onModConfigLoading(ModConfigEvent.Loading event) {
		switch(event.getConfig().getType()) {
		case SERVER -> PackagedExCraftingConfig.reloadServerConfig();
		default -> {}
		}
	}

	@SubscribeEvent
	public void onModConfigReloading(ModConfigEvent.Reloading event) {
		switch(event.getConfig().getType()) {
		case SERVER -> PackagedExCraftingConfig.reloadServerConfig();
		default -> {}
		}
	}
}

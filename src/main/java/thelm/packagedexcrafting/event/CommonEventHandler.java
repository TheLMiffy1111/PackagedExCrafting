package thelm.packagedexcrafting.event;

import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import thelm.packagedauto.util.ApiImpl;
import thelm.packagedexcrafting.block.AdvancedCrafterBlock;
import thelm.packagedexcrafting.block.BasicCrafterBlock;
import thelm.packagedexcrafting.block.CombinationCrafterBlock;
import thelm.packagedexcrafting.block.EliteCrafterBlock;
import thelm.packagedexcrafting.block.EnderCrafterBlock;
import thelm.packagedexcrafting.block.MarkedPedestalBlock;
import thelm.packagedexcrafting.block.UltimateCrafterBlock;
import thelm.packagedexcrafting.config.PackagedExCraftingConfig;
import thelm.packagedexcrafting.container.AdvancedCrafterContainer;
import thelm.packagedexcrafting.container.BasicCrafterContainer;
import thelm.packagedexcrafting.container.CombinationCrafterContainer;
import thelm.packagedexcrafting.container.EliteCrafterContainer;
import thelm.packagedexcrafting.container.EnderCrafterContainer;
import thelm.packagedexcrafting.container.UltimateCrafterContainer;
import thelm.packagedexcrafting.recipe.AdvancedPackageRecipeType;
import thelm.packagedexcrafting.recipe.BasicPackageRecipeType;
import thelm.packagedexcrafting.recipe.CombinationPackageRecipeType;
import thelm.packagedexcrafting.recipe.ElitePackageRecipeType;
import thelm.packagedexcrafting.recipe.EnderPackageRecipeType;
import thelm.packagedexcrafting.recipe.UltimatePackageRecipeType;
import thelm.packagedexcrafting.tile.AdvancedCrafterTile;
import thelm.packagedexcrafting.tile.BasicCrafterTile;
import thelm.packagedexcrafting.tile.CombinationCrafterTile;
import thelm.packagedexcrafting.tile.EliteCrafterTile;
import thelm.packagedexcrafting.tile.EnderCrafterTile;
import thelm.packagedexcrafting.tile.MarkedPedestalTile;
import thelm.packagedexcrafting.tile.UltimateCrafterTile;

public class CommonEventHandler {

	public static final CommonEventHandler INSTANCE = new CommonEventHandler();

	public static CommonEventHandler getInstance() {
		return INSTANCE;
	}

	public void onConstruct() {
		FMLJavaModLoadingContext.get().getModEventBus().register(this);
		PackagedExCraftingConfig.registerConfig();
	}

	@SubscribeEvent
	public void onBlockRegister(RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();
		registry.register(BasicCrafterBlock.INSTANCE);
		registry.register(AdvancedCrafterBlock.INSTANCE);
		registry.register(EliteCrafterBlock.INSTANCE);
		registry.register(UltimateCrafterBlock.INSTANCE);
		registry.register(EnderCrafterBlock.INSTANCE);
		registry.register(CombinationCrafterBlock.INSTANCE);
		registry.register(MarkedPedestalBlock.INSTANCE);
	}

	@SubscribeEvent
	public void onItemRegister(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();
		registry.register(BasicCrafterBlock.ITEM_INSTANCE);
		registry.register(AdvancedCrafterBlock.ITEM_INSTANCE);
		registry.register(EliteCrafterBlock.ITEM_INSTANCE);
		registry.register(UltimateCrafterBlock.ITEM_INSTANCE);
		registry.register(EnderCrafterBlock.ITEM_INSTANCE);
		registry.register(CombinationCrafterBlock.ITEM_INSTANCE);
		registry.register(MarkedPedestalBlock.ITEM_INSTANCE);
	}

	@SubscribeEvent
	public void onTileRegister(RegistryEvent.Register<TileEntityType<?>> event) {
		IForgeRegistry<TileEntityType<?>> registry = event.getRegistry();
		registry.register(BasicCrafterTile.TYPE_INSTANCE);
		registry.register(AdvancedCrafterTile.TYPE_INSTANCE);
		registry.register(EliteCrafterTile.TYPE_INSTANCE);
		registry.register(UltimateCrafterTile.TYPE_INSTANCE);
		registry.register(EnderCrafterTile.TYPE_INSTANCE);
		registry.register(CombinationCrafterTile.TYPE_INSTANCE);
		registry.register(MarkedPedestalTile.TYPE_INSTANCE);
	}

	@SubscribeEvent
	public void onContainerRegister(RegistryEvent.Register<ContainerType<?>> event) {
		IForgeRegistry<ContainerType<?>> registry = event.getRegistry();
		registry.register(BasicCrafterContainer.TYPE_INSTANCE);
		registry.register(AdvancedCrafterContainer.TYPE_INSTANCE);
		registry.register(EliteCrafterContainer.TYPE_INSTANCE);
		registry.register(UltimateCrafterContainer.TYPE_INSTANCE);
		registry.register(EnderCrafterContainer.TYPE_INSTANCE);
		registry.register(CombinationCrafterContainer.TYPE_INSTANCE);
	}

	@SubscribeEvent
	public void onCommonSetup(FMLCommonSetupEvent event) {
		ApiImpl.INSTANCE.registerRecipeType(BasicPackageRecipeType.INSTANCE);
		ApiImpl.INSTANCE.registerRecipeType(AdvancedPackageRecipeType.INSTANCE);
		ApiImpl.INSTANCE.registerRecipeType(ElitePackageRecipeType.INSTANCE);
		ApiImpl.INSTANCE.registerRecipeType(UltimatePackageRecipeType.INSTANCE);
		ApiImpl.INSTANCE.registerRecipeType(EnderPackageRecipeType.INSTANCE);
		ApiImpl.INSTANCE.registerRecipeType(CombinationPackageRecipeType.INSTANCE);
	}

	@SubscribeEvent
	public void onModConfig(ModConfig.ModConfigEvent event) {
		switch(event.getConfig().getType()) {
		case SERVER:
			PackagedExCraftingConfig.reloadServerConfig();
			break;
		default:
			break;
		}
	}
}

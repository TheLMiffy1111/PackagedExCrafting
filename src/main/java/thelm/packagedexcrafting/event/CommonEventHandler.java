package thelm.packagedexcrafting.event;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;
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
import thelm.packagedexcrafting.block.entity.AdvancedCrafterBlockEntity;
import thelm.packagedexcrafting.block.entity.BasicCrafterBlockEntity;
import thelm.packagedexcrafting.block.entity.CombinationCrafterBlockEntity;
import thelm.packagedexcrafting.block.entity.EliteCrafterBlockEntity;
import thelm.packagedexcrafting.block.entity.EnderCrafterBlockEntity;
import thelm.packagedexcrafting.block.entity.MarkedPedestalBlockEntity;
import thelm.packagedexcrafting.block.entity.UltimateCrafterBlockEntity;
import thelm.packagedexcrafting.config.PackagedExCraftingConfig;
import thelm.packagedexcrafting.menu.AdvancedCrafterMenu;
import thelm.packagedexcrafting.menu.BasicCrafterMenu;
import thelm.packagedexcrafting.menu.CombinationCrafterMenu;
import thelm.packagedexcrafting.menu.EliteCrafterMenu;
import thelm.packagedexcrafting.menu.EnderCrafterMenu;
import thelm.packagedexcrafting.menu.UltimateCrafterMenu;
import thelm.packagedexcrafting.recipe.AdvancedPackageRecipeType;
import thelm.packagedexcrafting.recipe.BasicPackageRecipeType;
import thelm.packagedexcrafting.recipe.CombinationPackageRecipeType;
import thelm.packagedexcrafting.recipe.ElitePackageRecipeType;
import thelm.packagedexcrafting.recipe.EnderPackageRecipeType;
import thelm.packagedexcrafting.recipe.UltimatePackageRecipeType;

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
	public void onBlockEntityRegister(RegistryEvent.Register<BlockEntityType<?>> event) {
		IForgeRegistry<BlockEntityType<?>> registry = event.getRegistry();
		registry.register(BasicCrafterBlockEntity.TYPE_INSTANCE);
		registry.register(AdvancedCrafterBlockEntity.TYPE_INSTANCE);
		registry.register(EliteCrafterBlockEntity.TYPE_INSTANCE);
		registry.register(UltimateCrafterBlockEntity.TYPE_INSTANCE);
		registry.register(EnderCrafterBlockEntity.TYPE_INSTANCE);
		registry.register(CombinationCrafterBlockEntity.TYPE_INSTANCE);
		registry.register(MarkedPedestalBlockEntity.TYPE_INSTANCE);
	}

	@SubscribeEvent
	public void onMenuRegister(RegistryEvent.Register<MenuType<?>> event) {
		IForgeRegistry<MenuType<?>> registry = event.getRegistry();
		registry.register(BasicCrafterMenu.TYPE_INSTANCE);
		registry.register(AdvancedCrafterMenu.TYPE_INSTANCE);
		registry.register(EliteCrafterMenu.TYPE_INSTANCE);
		registry.register(UltimateCrafterMenu.TYPE_INSTANCE);
		registry.register(EnderCrafterMenu.TYPE_INSTANCE);
		registry.register(CombinationCrafterMenu.TYPE_INSTANCE);
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
	public void onModConfig(ModConfigEvent event) {
		switch(event.getConfig().getType()) {
		case SERVER -> PackagedExCraftingConfig.reloadServerConfig();
		default -> {}
		}
	}
}

package thelm.packagedexcrafting.event;

import net.minecraft.core.Registry;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
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
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		modEventBus.register(this);
		PackagedExCraftingConfig.registerConfig();

		DeferredRegister<Block> blockRegister = DeferredRegister.create(Registry.BLOCK_REGISTRY, "packagedexcrafting");
		blockRegister.register(modEventBus);
		blockRegister.register("basic_crafter", ()->BasicCrafterBlock.INSTANCE);
		blockRegister.register("advanced_crafter", ()->AdvancedCrafterBlock.INSTANCE);
		blockRegister.register("elite_crafter", ()->EliteCrafterBlock.INSTANCE);
		blockRegister.register("ultimate_crafter", ()->UltimateCrafterBlock.INSTANCE);
		blockRegister.register("ender_crafter", ()->EnderCrafterBlock.INSTANCE);
		blockRegister.register("combination_crafter", ()->CombinationCrafterBlock.INSTANCE);
		blockRegister.register("marked_pedestal", ()->MarkedPedestalBlock.INSTANCE);

		DeferredRegister<Item> itemRegister = DeferredRegister.create(Registry.ITEM_REGISTRY, "packagedexcrafting");
		itemRegister.register(modEventBus);
		itemRegister.register("basic_crafter", ()->BasicCrafterBlock.ITEM_INSTANCE);
		itemRegister.register("advanced_crafter", ()->AdvancedCrafterBlock.ITEM_INSTANCE);
		itemRegister.register("elite_crafter", ()->EliteCrafterBlock.ITEM_INSTANCE);
		itemRegister.register("ultimate_crafter", ()->UltimateCrafterBlock.ITEM_INSTANCE);
		itemRegister.register("ender_crafter", ()->EnderCrafterBlock.ITEM_INSTANCE);
		itemRegister.register("combination_crafter", ()->CombinationCrafterBlock.ITEM_INSTANCE);
		itemRegister.register("marked_pedestal", ()->MarkedPedestalBlock.ITEM_INSTANCE);

		DeferredRegister<BlockEntityType<?>> blockEntityRegister = DeferredRegister.create(Registry.BLOCK_ENTITY_TYPE_REGISTRY, "packagedexcrafting");
		blockEntityRegister.register(modEventBus);
		blockEntityRegister.register("basic_crafter", ()->BasicCrafterBlockEntity.TYPE_INSTANCE);
		blockEntityRegister.register("advanced_crafter", ()->AdvancedCrafterBlockEntity.TYPE_INSTANCE);
		blockEntityRegister.register("elite_crafter", ()->EliteCrafterBlockEntity.TYPE_INSTANCE);
		blockEntityRegister.register("ultimate_crafter", ()->UltimateCrafterBlockEntity.TYPE_INSTANCE);
		blockEntityRegister.register("ender_crafter", ()->EnderCrafterBlockEntity.TYPE_INSTANCE);
		blockEntityRegister.register("combination_crafter", ()->CombinationCrafterBlockEntity.TYPE_INSTANCE);
		blockEntityRegister.register("marked_pedestal", ()->MarkedPedestalBlockEntity.TYPE_INSTANCE);

		DeferredRegister<MenuType<?>> menuRegister = DeferredRegister.create(Registry.MENU_REGISTRY, "packagedexcrafting");
		menuRegister.register(modEventBus);
		menuRegister.register("basic_crafter", ()->BasicCrafterMenu.TYPE_INSTANCE);
		menuRegister.register("advanced_crafter", ()->AdvancedCrafterMenu.TYPE_INSTANCE);
		menuRegister.register("elite_crafter", ()->EliteCrafterMenu.TYPE_INSTANCE);
		menuRegister.register("ultimate_crafter", ()->UltimateCrafterMenu.TYPE_INSTANCE);
		menuRegister.register("ender_crafter", ()->EnderCrafterMenu.TYPE_INSTANCE);
		menuRegister.register("combination_crafter", ()->CombinationCrafterMenu.TYPE_INSTANCE);
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
		case SERVER:
			PackagedExCraftingConfig.reloadServerConfig();
			break;
		default:
			break;
		}
	}
}

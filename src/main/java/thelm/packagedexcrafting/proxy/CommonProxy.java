package thelm.packagedexcrafting.proxy;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import thelm.packagedauto.api.RecipeTypeRegistry;
import thelm.packagedexcrafting.block.BlockAdvancedCrafter;
import thelm.packagedexcrafting.block.BlockBasicCrafter;
import thelm.packagedexcrafting.block.BlockEliteCrafter;
import thelm.packagedexcrafting.block.BlockUltimateCrafter;
import thelm.packagedexcrafting.config.PackagedExCraftingConfig;
import thelm.packagedexcrafting.recipe.RecipeTypeAdvanced;
import thelm.packagedexcrafting.recipe.RecipeTypeBasic;
import thelm.packagedexcrafting.recipe.RecipeTypeElite;
import thelm.packagedexcrafting.recipe.RecipeTypeUltimate;
import thelm.packagedexcrafting.tile.TileAdvancedCrafter;
import thelm.packagedexcrafting.tile.TileBasicCrafter;
import thelm.packagedexcrafting.tile.TileEliteCrafter;
import thelm.packagedexcrafting.tile.TileUltimateCrafter;

public class CommonProxy {

	public void registerBlock(Block block) {
		ForgeRegistries.BLOCKS.register(block);
	}

	public void registerItem(Item item) {
		ForgeRegistries.ITEMS.register(item);
	}

	public void register(FMLPreInitializationEvent event) {
		registerConfig(event);
		registerBlocks();
		registerItems();
		registerModels();
		registerTileEntities();
		registerRecipeTypes();
	}

	protected void registerConfig(FMLPreInitializationEvent event) {
		PackagedExCraftingConfig.init(event.getSuggestedConfigurationFile());
	}

	protected void registerBlocks() {
		if(TileBasicCrafter.enabled) {
			registerBlock(BlockBasicCrafter.INSTANCE);
		}
		if(TileAdvancedCrafter.enabled) {
			registerBlock(BlockAdvancedCrafter.INSTANCE);
		}
		if(TileEliteCrafter.enabled) {
			registerBlock(BlockEliteCrafter.INSTANCE);
		}
		registerBlock(BlockUltimateCrafter.INSTANCE);
	}

	protected void registerItems() {
		if(TileBasicCrafter.enabled) {
			registerItem(BlockBasicCrafter.ITEM_INSTANCE);
		}
		if(TileAdvancedCrafter.enabled) {
			registerItem(BlockAdvancedCrafter.ITEM_INSTANCE);
		}
		if(TileEliteCrafter.enabled) {
			registerItem(BlockEliteCrafter.ITEM_INSTANCE);
		}
		if(TileUltimateCrafter.enabled) {
			registerItem(BlockUltimateCrafter.ITEM_INSTANCE);
		}
	}

	protected void registerModels() {}

	protected void registerTileEntities() {
		if(TileBasicCrafter.enabled) {
			GameRegistry.registerTileEntity(TileBasicCrafter.class, new ResourceLocation("packagedexcrafting:basic_crafter"));
		}
		if(TileAdvancedCrafter.enabled) {
			GameRegistry.registerTileEntity(TileAdvancedCrafter.class, new ResourceLocation("packagedexcrafting:advanced_crafter"));
		}
		if(TileEliteCrafter.enabled) {
			GameRegistry.registerTileEntity(TileEliteCrafter.class, new ResourceLocation("packagedexcrafting:elite_crafter"));
		}
		if(TileUltimateCrafter.enabled) {
			GameRegistry.registerTileEntity(TileUltimateCrafter.class, new ResourceLocation("packagedexcrafting:ultimate_crafter"));
		}
	}

	protected void registerRecipeTypes() {
		if(TileBasicCrafter.enabled) {
			RecipeTypeRegistry.registerRecipeType(RecipeTypeBasic.INSTANCE);
		}
		if(TileAdvancedCrafter.enabled) {
			RecipeTypeRegistry.registerRecipeType(RecipeTypeAdvanced.INSTANCE);
		}
		if(TileEliteCrafter.enabled) {
			RecipeTypeRegistry.registerRecipeType(RecipeTypeElite.INSTANCE);
		}
		if(TileUltimateCrafter.enabled) {
			RecipeTypeRegistry.registerRecipeType(RecipeTypeUltimate.INSTANCE);
		}
	}
}

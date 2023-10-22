package thelm.packagedexcrafting.recipe;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import com.blakebr0.extendedcrafting.init.ModBlocks;
import com.google.common.collect.ImmutableList;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntRBTreeSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import thelm.packagedauto.api.IGuiIngredientWrapper;
import thelm.packagedauto.api.IPackageRecipeInfo;
import thelm.packagedauto.api.IPackageRecipeType;
import thelm.packagedauto.api.IRecipeLayoutWrapper;

public class UltimatePackageRecipeType implements IPackageRecipeType {

	public static final UltimatePackageRecipeType INSTANCE = new UltimatePackageRecipeType();
	public static final ResourceLocation NAME = new ResourceLocation("packagedexcrafting:ultimate");
	public static final IntSet SLOTS;
	public static final List<ResourceLocation> CATEGORIES = ImmutableList.of(
			new ResourceLocation("extendedcrafting:ultimate_crafting"),
			new ResourceLocation("extendedcrafting:elite_crafting"),
			new ResourceLocation("extendedcrafting:advanced_crafting"),
			new ResourceLocation("extendedcrafting:basic_crafting"));
	public static final Vector3i COLOR = new Vector3i(139, 139, 139);
	public static final Vector3i COLOR_DISABLED = new Vector3i(64, 64, 64);

	static {
		SLOTS = new IntRBTreeSet();
		IntStream.range(0, 81).forEachOrdered(SLOTS::add);
	}

	protected UltimatePackageRecipeType() {}

	@Override
	public ResourceLocation getName() {
		return NAME;
	}

	@Override
	public IFormattableTextComponent getDisplayName() {
		return new TranslationTextComponent("recipe.packagedexcrafting.ultimate");
	}

	@Override
	public IFormattableTextComponent getShortDisplayName() {
		return new TranslationTextComponent("recipe.packagedexcrafting.ultimate.short");
	}

	@Override
	public IPackageRecipeInfo getNewRecipeInfo() {
		return new UltimatePackageRecipeInfo();
	}

	@Override
	public IntSet getEnabledSlots() {
		return SLOTS;
	}

	@Override
	public boolean canSetOutput() {
		return false;
	}

	@Override
	public boolean hasMachine() {
		return true;
	}

	@Override
	public List<ResourceLocation> getJEICategories() {
		return CATEGORIES;
	}

	@Override
	public Int2ObjectMap<ItemStack> getRecipeTransferMap(IRecipeLayoutWrapper recipeLayoutWrapper) {
		if(recipeLayoutWrapper.getCategoryUid().equals(CATEGORIES.get(0))) {
			Int2ObjectMap<ItemStack> map = new Int2ObjectOpenHashMap<>();
			Map<Integer, IGuiIngredientWrapper<ItemStack>> ingredients = recipeLayoutWrapper.getItemStackIngredients();
			int index = 0;
			for(Map.Entry<Integer, IGuiIngredientWrapper<ItemStack>> entry : ingredients.entrySet()) {
				IGuiIngredientWrapper<ItemStack> ingredient = entry.getValue();
				if(ingredient.isInput()) {
					ItemStack displayed = entry.getValue().getDisplayedIngredient();
					if(displayed != null && !displayed.isEmpty()) {
						map.put(index, displayed);
					}
					++index;
				}
				if(index >= 81) {
					break;
				}
			}
			return map;
		}
		return ElitePackageRecipeType.INSTANCE.getRecipeTransferMap(recipeLayoutWrapper);
	}

	@Override
	public Object getRepresentation() {
		return new ItemStack(ModBlocks.ULTIMATE_TABLE.get());
	}

	@Override
	public Vector3i getSlotColor(int slot) {
		if(slot >= 81 && slot != 85 && slot < 90) {
			return COLOR_DISABLED;
		}
		return COLOR;
	}
}

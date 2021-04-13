package thelm.packagedexcrafting.recipe;

import java.util.List;
import java.util.Map;

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

public class ElitePackageRecipeType implements IPackageRecipeType {

	public static final ElitePackageRecipeType INSTANCE = new ElitePackageRecipeType();
	public static final ResourceLocation NAME = new ResourceLocation("packagedexcrafting:elite");
	public static final IntSet SLOTS;
	public static final List<ResourceLocation> CATEGORIES = ImmutableList.of(
			new ResourceLocation("extendedcrafting:elite_crafting"),
			new ResourceLocation("extendedcrafting:advanced_crafting"),
			new ResourceLocation("extendedcrafting:basic_crafting"));
	public static final Vector3i COLOR = new Vector3i(139, 139, 139);
	public static final Vector3i COLOR_DISABLED = new Vector3i(64, 64, 64);

	static {
		SLOTS = new IntRBTreeSet();
		for(int i = 1; i < 8; ++i) {
			for(int j = 1; j < 8; ++j) {
				SLOTS.add(9*i+j);
			}
		}
	}

	protected ElitePackageRecipeType() {};

	@Override
	public ResourceLocation getName() {
		return NAME;
	}

	@Override
	public IFormattableTextComponent getDisplayName() {
		return new TranslationTextComponent("recipe.packagedexcrafting.elite");
	}

	@Override
	public IFormattableTextComponent getShortDisplayName() {
		return new TranslationTextComponent("recipe.packagedexcrafting.elite.short");
	}

	@Override
	public IPackageRecipeInfo getNewRecipeInfo() {
		return new ElitePackageRecipeInfo();
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
			int[] slotArray = SLOTS.toIntArray();
			for(Map.Entry<Integer, IGuiIngredientWrapper<ItemStack>> entry : ingredients.entrySet()) {
				IGuiIngredientWrapper<ItemStack> ingredient = entry.getValue();
				if(ingredient.isInput()) {
					ItemStack displayed = entry.getValue().getDisplayedIngredient();
					if(displayed != null && !displayed.isEmpty()) {
						map.put(slotArray[index], displayed);
					}
					++index;
				}
				if(index >= 49) {
					break;
				}
			}
			return map;
		}
		return AdvancedPackageRecipeType.INSTANCE.getRecipeTransferMap(recipeLayoutWrapper);
	}

	@Override
	public Object getRepresentation() {
		return new ItemStack(ModBlocks.ELITE_TABLE.get());
	}

	@Override
	public Vector3i getSlotColor(int slot) {
		if(!SLOTS.contains(slot) && slot != 85) {
			return COLOR_DISABLED;
		}
		return COLOR;
	}
}

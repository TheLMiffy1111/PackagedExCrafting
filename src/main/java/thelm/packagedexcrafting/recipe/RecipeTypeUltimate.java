package thelm.packagedexcrafting.recipe;

import java.awt.Color;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import com.blakebr0.extendedcrafting.block.ModBlocks;
import com.google.common.collect.ImmutableList;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntRBTreeSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import mezz.jei.api.gui.IGuiIngredient;
import mezz.jei.api.gui.IRecipeLayout;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thelm.packagedauto.api.IRecipeInfo;
import thelm.packagedauto.api.IRecipeType;

public class RecipeTypeUltimate implements IRecipeType {

	public static final RecipeTypeUltimate INSTANCE = new RecipeTypeUltimate();
	public static final ResourceLocation NAME = new ResourceLocation("packagedexcrafting:ultimate");
	public static final IntSet SLOTS;
	public static final List<String> CATEGORIES = ImmutableList.of(
			"extendedcrafting:table_crafting_9x9",
			"extendedcrafting:table_crafting_7x7",
			"extendedcrafting:table_crafting_5x5",
			"extendedcrafting:table_crafting_3x3");
	public static final Color COLOR = new Color(139, 139, 139);
	public static final Color COLOR_DISABLED = new Color(64, 64, 64);

	static {
		SLOTS = new IntRBTreeSet();
		IntStream.range(0, 81).forEachOrdered(SLOTS::add);
	}

	protected RecipeTypeUltimate() {};

	@Override
	public ResourceLocation getName() {
		return NAME;
	}

	@Override
	public String getLocalizedName() {
		return I18n.translateToLocal("recipe.packagedexcrafting.ultimate");
	}

	@Override
	public String getLocalizedNameShort() {
		return I18n.translateToLocal("recipe.packagedexcrafting.ultimate.short");
	}

	@Override
	public IRecipeInfo getNewRecipeInfo() {
		return new RecipeInfoUltimate();
	}

	@Override
	public IntSet getEnabledSlots() {
		return SLOTS;
	}

	@Override
	public List<String> getJEICategories() {
		return CATEGORIES;
	}

	@Optional.Method(modid="jei")
	@Override
	public Int2ObjectMap<ItemStack> getRecipeTransferMap(IRecipeLayout recipeLayout, String category) {
		if(category.equals(CATEGORIES.get(0))) {
			Int2ObjectMap<ItemStack> map = new Int2ObjectOpenHashMap<>();
			Map<Integer, ? extends IGuiIngredient<ItemStack>> ingredients = recipeLayout.getItemStacks().getGuiIngredients();
			int index = 0;
			for(Map.Entry<Integer, ? extends IGuiIngredient<ItemStack>> entry : ingredients.entrySet()) {
				IGuiIngredient<ItemStack> ingredient = entry.getValue();
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
		return RecipeTypeElite.INSTANCE.getRecipeTransferMap(recipeLayout, category);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Object getRepresentation() {
		return new ItemStack(ModBlocks.blockUltimateTable);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Color getSlotColor(int slot) {
		if(slot >= 81 && slot != 85 && slot < 90) {
			return COLOR_DISABLED;
		}
		return COLOR;
	}
}

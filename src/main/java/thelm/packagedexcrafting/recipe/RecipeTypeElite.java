package thelm.packagedexcrafting.recipe;

import java.awt.Color;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.blakebr0.extendedcrafting.block.ModBlocks;

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

public class RecipeTypeElite implements IRecipeType {

	public static final RecipeTypeElite INSTANCE = new RecipeTypeElite();
	public static final ResourceLocation NAME = new ResourceLocation("packagedexcrafting:elite");
	public static final IntSet SLOTS;
	public static final List<String> CATEGORIES = Collections.singletonList("extendedcrafting:table_crafting_7x7");
	public static final Color COLOR = new Color(139, 139, 139);
	public static final Color COLOR_DISABLED = new Color(64, 64, 64);

	static {
		SLOTS = new IntRBTreeSet();
		for(int i = 1; i < 8; ++i) {
			for(int j = 1; j < 8; ++j) {
				SLOTS.add(9*i+j);
			}
		}
	}

	protected RecipeTypeElite() {};

	@Override
	public ResourceLocation getName() {
		return NAME;
	}

	@Override
	public String getLocalizedName() {
		return I18n.translateToLocal("recipe.packagedexcrafting.elite");
	}

	@Override
	public String getLocalizedNameShort() {
		return I18n.translateToLocal("recipe.packagedexcrafting.elite.short");
	}

	@Override
	public IRecipeInfo getNewRecipeInfo() {
		return new RecipeInfoElite();
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
		Int2ObjectMap<ItemStack> map = new Int2ObjectOpenHashMap<>();
		Map<Integer, ? extends IGuiIngredient<ItemStack>> ingredients = recipeLayout.getItemStacks().getGuiIngredients();
		int index = 0;
		int[] slotArray = SLOTS.toIntArray();
		for(Map.Entry<Integer, ? extends IGuiIngredient<ItemStack>> entry : ingredients.entrySet()) {
			IGuiIngredient<ItemStack> ingredient = entry.getValue();
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

	@SideOnly(Side.CLIENT)
	@Override
	public Object getRepresentation() {
		return new ItemStack(ModBlocks.blockEliteTable);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Color getSlotColor(int slot) {
		if(!SLOTS.contains(slot) && slot != 85) {
			return COLOR_DISABLED;
		}
		return COLOR;
	}
}

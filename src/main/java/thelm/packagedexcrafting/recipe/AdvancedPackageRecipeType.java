package thelm.packagedexcrafting.recipe;

import java.util.List;
import java.util.Map;

import com.blakebr0.extendedcrafting.init.ModBlocks;
import com.google.common.collect.ImmutableList;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntRBTreeSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import thelm.packagedauto.api.IGuiIngredientWrapper;
import thelm.packagedauto.api.IPackageRecipeInfo;
import thelm.packagedauto.api.IPackageRecipeType;
import thelm.packagedauto.api.IRecipeLayoutWrapper;

public class AdvancedPackageRecipeType implements IPackageRecipeType {

	public static final AdvancedPackageRecipeType INSTANCE = new AdvancedPackageRecipeType();
	public static final ResourceLocation NAME = new ResourceLocation("packagedexcrafting:advanced");
	public static final IntSet SLOTS;
	public static final List<ResourceLocation> CATEGORIES = ImmutableList.of(
			new ResourceLocation("extendedcrafting:advanced_crafting"),
			new ResourceLocation("extendedcrafting:basic_crafting"));
	public static final Vec3i COLOR = new Vec3i(139, 139, 139);
	public static final Vec3i COLOR_DISABLED = new Vec3i(64, 64, 64);

	static {
		SLOTS = new IntRBTreeSet();
		for(int i = 2; i < 7; ++i) {
			for(int j = 2; j < 7; ++j) {
				SLOTS.add(9*i+j);
			}
		}
	}

	protected AdvancedPackageRecipeType() {};

	@Override
	public ResourceLocation getName() {
		return NAME;
	}

	@Override
	public MutableComponent getDisplayName() {
		return new TranslatableComponent("recipe.packagedexcrafting.advanced");
	}

	@Override
	public MutableComponent getShortDisplayName() {
		return new TranslatableComponent("recipe.packagedexcrafting.advanced.short");
	}

	@Override
	public IPackageRecipeInfo getNewRecipeInfo() {
		return new AdvancedPackageRecipeInfo();
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
	public boolean hasContainerItem() {
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
				if(index >= 25) {
					break;
				}
			}
			return map;
		}
		return BasicPackageRecipeType.INSTANCE.getRecipeTransferMap(recipeLayoutWrapper);
	}

	@Override
	public Object getRepresentation() {
		return new ItemStack(ModBlocks.ADVANCED_TABLE.get());
	}

	@Override
	public Vec3i getSlotColor(int slot) {
		if(!SLOTS.contains(slot) && slot != 81) {
			return COLOR_DISABLED;
		}
		return COLOR;
	}
}

package thelm.packagedexcrafting.recipe;

import java.util.List;
import java.util.stream.IntStream;

import com.blakebr0.extendedcrafting.crafting.recipe.ShapelessTableRecipe;
import com.blakebr0.extendedcrafting.init.ModBlocks;
import com.google.common.collect.ImmutableList;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntRBTreeSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import thelm.packagedauto.api.IPackageRecipeInfo;
import thelm.packagedauto.api.IPackageRecipeType;
import thelm.packagedauto.api.IRecipeSlotViewWrapper;
import thelm.packagedauto.api.IRecipeSlotsViewWrapper;

public class UltimatePackageRecipeType implements IPackageRecipeType {

	public static final UltimatePackageRecipeType INSTANCE = new UltimatePackageRecipeType();
	public static final ResourceLocation NAME = new ResourceLocation("packagedexcrafting:ultimate");
	public static final IntSet SLOTS;
	public static final List<ResourceLocation> CATEGORIES = ImmutableList.of(
			new ResourceLocation("extendedcrafting:ultimate_crafting"),
			new ResourceLocation("extendedcrafting:elite_crafting"),
			new ResourceLocation("extendedcrafting:advanced_crafting"),
			new ResourceLocation("extendedcrafting:basic_crafting"));
	public static final Vec3i COLOR = new Vec3i(139, 139, 139);
	public static final Vec3i COLOR_DISABLED = new Vec3i(64, 64, 64);

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
	public MutableComponent getDisplayName() {
		return Component.translatable("recipe.packagedexcrafting.ultimate");
	}

	@Override
	public MutableComponent getShortDisplayName() {
		return Component.translatable("recipe.packagedexcrafting.ultimate.short");
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
	public List<ResourceLocation> getJEICategories() {
		return CATEGORIES;
	}

	@Override
	public Int2ObjectMap<ItemStack> getRecipeTransferMap(IRecipeSlotsViewWrapper recipeLayoutWrapper) {
		Int2ObjectMap<ItemStack> map = new Int2ObjectOpenHashMap<>();
		List<IRecipeSlotViewWrapper> slotViews = recipeLayoutWrapper.getRecipeSlotViews();
		int tier = recipeLayoutWrapper.getRecipe() instanceof ShapelessTableRecipe shapelessRecipe ? shapelessRecipe.getTier() : 0;
		if(tier == 0 && slotViews.size() == 82 || tier == 4) {
			int index = 0;
			int[] slotArray = SLOTS.toIntArray();
			for(IRecipeSlotViewWrapper slotView : slotViews) {
				if(slotView.isInput()) {
					Object displayed = slotView.getDisplayedIngredient().orElse(null);
					if(displayed instanceof ItemStack stack && !stack.isEmpty()) {
						map.put(slotArray[index], stack);
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
	public Vec3i getSlotColor(int slot) {
		if(slot >= 81 && slot != 81 && slot < 90) {
			return COLOR_DISABLED;
		}
		return COLOR;
	}
}

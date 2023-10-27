package thelm.packagedexcrafting.recipe;

import java.util.Collections;
import java.util.List;

import com.blakebr0.extendedcrafting.api.crafting.ITableRecipe;
import com.blakebr0.extendedcrafting.init.ModBlocks;

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

public class BasicPackageRecipeType implements IPackageRecipeType {

	public static final BasicPackageRecipeType INSTANCE = new BasicPackageRecipeType();
	public static final ResourceLocation NAME = new ResourceLocation("packagedexcrafting:basic");
	public static final IntSet SLOTS;
	public static final List<ResourceLocation> CATEGORIES = Collections.singletonList(new ResourceLocation("extendedcrafting:basic_crafting"));
	public static final Vec3i COLOR = new Vec3i(139, 139, 139);
	public static final Vec3i COLOR_DISABLED = new Vec3i(64, 64, 64);

	static {
		SLOTS = new IntRBTreeSet();
		for(int i = 3; i < 6; ++i) {
			for(int j = 3; j < 6; ++j) {
				SLOTS.add(9*i+j);
			}
		}
	}

	protected BasicPackageRecipeType() {}

	@Override
	public ResourceLocation getName() {
		return NAME;
	}

	@Override
	public MutableComponent getDisplayName() {
		return Component.translatable("recipe.packagedexcrafting.basic");
	}

	@Override
	public MutableComponent getShortDisplayName() {
		return Component.translatable("recipe.packagedexcrafting.basic.short");
	}

	@Override
	public IPackageRecipeInfo getNewRecipeInfo() {
		return new BasicPackageRecipeInfo();
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
		if(recipeLayoutWrapper.getRecipe() instanceof ITableRecipe recipe && recipe.getTier() == 1) {
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
				if(index >= 9) {
					break;
				}
			}
			return map;
		}
		return IPackageRecipeType.super.getRecipeTransferMap(recipeLayoutWrapper);
	}

	@Override
	public Object getRepresentation() {
		return new ItemStack(ModBlocks.BASIC_TABLE.get());
	}

	@Override
	public Vec3i getSlotColor(int slot) {
		if(!SLOTS.contains(slot) && slot != 81) {
			return COLOR_DISABLED;
		}
		return COLOR;
	}
}

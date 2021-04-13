package thelm.packagedexcrafting.recipe;

import java.util.Collections;
import java.util.List;

import com.blakebr0.extendedcrafting.api.crafting.ITableRecipe;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import thelm.packagedauto.api.IPackageRecipeInfo;

public interface ITablePackageRecipeInfo extends IPackageRecipeInfo {

	int getTier();

	ItemStack getOutput();

	ITableRecipe getRecipe();

	IInventory getMatrix();

	List<ItemStack> getRemainingItems();

	@Override
	default List<ItemStack> getOutputs() {
		return Collections.singletonList(getOutput());
	}
}

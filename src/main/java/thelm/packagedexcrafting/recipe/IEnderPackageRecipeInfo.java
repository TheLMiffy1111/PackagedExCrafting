package thelm.packagedexcrafting.recipe;

import java.util.Collections;
import java.util.List;

import com.blakebr0.extendedcrafting.api.crafting.IEnderCrafterRecipe;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import thelm.packagedauto.api.IPackageRecipeInfo;

public interface IEnderPackageRecipeInfo extends IPackageRecipeInfo {

	ItemStack getOutput();

	IEnderCrafterRecipe getRecipe();

	IInventory getMatrix();

	int getTimeRequired();

	@Override
	default List<ItemStack> getOutputs() {
		ItemStack output = getOutput();
		return output.isEmpty() ? Collections.emptyList() : Collections.singletonList(output);
	}
}

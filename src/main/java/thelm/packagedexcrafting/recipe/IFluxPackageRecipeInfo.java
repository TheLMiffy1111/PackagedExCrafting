package thelm.packagedexcrafting.recipe;

import java.util.List;

import com.blakebr0.extendedcrafting.api.crafting.IFluxCrafterRecipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import thelm.packagedauto.api.IPackageRecipeInfo;

public interface IFluxPackageRecipeInfo extends IPackageRecipeInfo {

	ItemStack getOutput();

	IFluxCrafterRecipe getRecipe();

	CraftingInput getMatrix();

	int getEnergyRequired();

	int getEnergyUsage();

	@Override
	default List<ItemStack> getOutputs() {
		ItemStack output = getOutput();
		return output.isEmpty() ? List.of() : List.of(output);
	}
}

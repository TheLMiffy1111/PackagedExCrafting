package thelm.packagedexcrafting.recipe;

import java.util.Collections;
import java.util.List;

import com.blakebr0.extendedcrafting.api.crafting.IFluxCrafterRecipe;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import thelm.packagedauto.api.IPackageRecipeInfo;

public interface IFluxPackageRecipeInfo extends IPackageRecipeInfo {

	ItemStack getOutput();

	IFluxCrafterRecipe getRecipe();

	Container getMatrix();

	int getEnergyRequired();

	int getEnergyUsage();

	List<ItemStack> getRemainingItems();

	@Override
	default List<ItemStack> getOutputs() {
		return Collections.singletonList(getOutput());
	}
}

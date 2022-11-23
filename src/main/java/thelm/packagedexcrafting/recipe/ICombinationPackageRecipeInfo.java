package thelm.packagedexcrafting.recipe;

import java.util.Collections;
import java.util.List;

import com.blakebr0.extendedcrafting.api.crafting.ICombinationRecipe;

import net.minecraft.world.item.ItemStack;
import thelm.packagedauto.api.IPackageRecipeInfo;

public interface ICombinationPackageRecipeInfo extends IPackageRecipeInfo {

	ItemStack getCoreInput();

	List<ItemStack> getPedestalInputs();

	ItemStack getOutput();

	long getEnergyRequired();

	int getEnergyUsage();

	ICombinationRecipe getRecipe();

	@Override
	default List<ItemStack> getOutputs() {
		return List.of(getOutput());
	}
}

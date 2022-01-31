package thelm.packagedexcrafting.recipe;

import java.util.Collections;
import java.util.List;

import com.blakebr0.extendedcrafting.api.crafting.IEnderCrafterRecipe;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import thelm.packagedauto.api.IPackageRecipeInfo;

public interface IEnderPackageRecipeInfo extends IPackageRecipeInfo {

	ItemStack getOutput();

	IEnderCrafterRecipe getRecipe();

	Container getMatrix();

	int getTimeRequired();

	List<ItemStack> getRemainingItems();

	@Override
	default List<ItemStack> getOutputs() {
		return Collections.singletonList(getOutput());
	}
}

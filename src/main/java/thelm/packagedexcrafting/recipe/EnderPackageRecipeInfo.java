package thelm.packagedexcrafting.recipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.blakebr0.extendedcrafting.api.crafting.IEnderCrafterRecipe;
import com.blakebr0.extendedcrafting.init.ModRecipeTypes;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import thelm.packagedauto.api.IPackagePattern;
import thelm.packagedauto.api.IPackageRecipeType;
import thelm.packagedauto.util.MiscHelper;
import thelm.packagedauto.util.PackagePattern;

public class EnderPackageRecipeInfo implements IEnderPackageRecipeInfo {

	IEnderCrafterRecipe recipe;
	List<ItemStack> input = new ArrayList<>();
	Container matrix = new SimpleContainer(9);
	ItemStack output;
	List<IPackagePattern> patterns = new ArrayList<>();

	@Override
	public void load(CompoundTag nbt) {
		input.clear();
		output = ItemStack.EMPTY;
		patterns.clear();
		Recipe<?> recipe = MiscHelper.INSTANCE.getRecipeManager().byKey(new ResourceLocation(nbt.getString("Recipe"))).orElse(null);
		List<ItemStack> matrixList = new ArrayList<>();
		MiscHelper.INSTANCE.loadAllItems(nbt.getList("Matrix", 10), matrixList);
		for(int i = 0; i < 9 && i < matrixList.size(); ++i) {
			matrix.setItem(i, matrixList.get(i));
		}
		if(recipe instanceof IEnderCrafterRecipe enderRecipe) {
			this.recipe = enderRecipe;
			input.addAll(MiscHelper.INSTANCE.condenseStacks(matrix));
			output = this.recipe.assemble(matrix, MiscHelper.INSTANCE.getRegistryAccess()).copy();
			for(int i = 0; i*9 < input.size(); ++i) {
				patterns.add(new PackagePattern(this, i));
			}
		}
	}

	@Override
	public void save(CompoundTag nbt) {
		if(recipe != null) {
			nbt.putString("Recipe", recipe.getId().toString());
		}
		List<ItemStack> matrixList = new ArrayList<>();
		for(int i = 0; i < 9; ++i) {
			matrixList.add(matrix.getItem(i));
		}
		ListTag matrixTag = MiscHelper.INSTANCE.saveAllItems(new ListTag(), matrixList);
		nbt.put("Matrix", matrixTag);
	}

	@Override
	public IPackageRecipeType getRecipeType() {
		return EnderPackageRecipeType.INSTANCE;
	}

	@Override
	public boolean isValid() {
		return recipe != null;
	}

	@Override
	public List<IPackagePattern> getPatterns() {
		return Collections.unmodifiableList(patterns);
	}

	@Override
	public List<ItemStack> getInputs() {
		return Collections.unmodifiableList(input);
	}

	@Override
	public ItemStack getOutput() {
		return output.copy();
	}

	@Override
	public IEnderCrafterRecipe getRecipe() {
		return recipe;
	}

	@Override
	public Container getMatrix() {
		return matrix;
	}

	@Override
	public int getTimeRequired() {
		return recipe.getCraftingTime();
	}

	@Override
	public List<ItemStack> getRemainingItems() {
		return recipe.getRemainingItems(matrix);
	}

	@Override
	public void generateFromStacks(List<ItemStack> input, List<ItemStack> output, Level level) {
		recipe = null;
		this.input.clear();
		patterns.clear();
		int[] slotArray = BasicPackageRecipeType.SLOTS.toIntArray();
		for(int i = 0; i < 9; ++i) {
			ItemStack toSet = input.get(slotArray[i]);
			toSet.setCount(1);
			matrix.setItem(i, toSet.copy());
		}
		IEnderCrafterRecipe recipe = MiscHelper.INSTANCE.getRecipeManager().getRecipeFor(ModRecipeTypes.ENDER_CRAFTER.get(), matrix, level).orElse(null);
		if(recipe != null) {
			this.recipe = recipe;
			this.input.addAll(MiscHelper.INSTANCE.condenseStacks(matrix));
			this.output = recipe.assemble(matrix, level.registryAccess()).copy();
			for(int i = 0; i*9 < this.input.size(); ++i) {
				patterns.add(new PackagePattern(this, i));
			}
			return;
		}
		matrix.clearContent();
	}

	@Override
	public Int2ObjectMap<ItemStack> getEncoderStacks() {
		Int2ObjectMap<ItemStack> map = new Int2ObjectOpenHashMap<>();
		int[] slotArray = EnderPackageRecipeType.SLOTS.toIntArray();
		for(int i = 0; i < 9; ++i) {
			map.put(slotArray[i], matrix.getItem(i));
		}
		return map;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof EnderPackageRecipeInfo other) {
			return MiscHelper.INSTANCE.recipeEquals(this, recipe, other, other.recipe);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return MiscHelper.INSTANCE.recipeHashCode(this, recipe);
	}
}

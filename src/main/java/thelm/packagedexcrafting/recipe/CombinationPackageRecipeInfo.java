package thelm.packagedexcrafting.recipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.blakebr0.extendedcrafting.api.crafting.ICombinationRecipe;
import com.blakebr0.extendedcrafting.api.crafting.RecipeTypes;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import thelm.packagedauto.api.IPackagePattern;
import thelm.packagedauto.api.IPackageRecipeType;
import thelm.packagedauto.util.MiscHelper;
import thelm.packagedauto.util.PackagePattern;

public class CombinationPackageRecipeInfo implements ICombinationPackageRecipeInfo {

	ICombinationRecipe recipe;
	ItemStack inputCore = ItemStack.EMPTY;
	List<ItemStack> inputPedestal = new ArrayList<>();
	List<ItemStack> input = new ArrayList<>();
	ItemStack output;
	List<IPackagePattern> patterns = new ArrayList<>();

	@Override
	public void read(CompoundNBT nbt) {
		inputPedestal.clear();
		input.clear();
		output = ItemStack.EMPTY;
		inputCore = ItemStack.of(nbt.getCompound("InputCore"));
		MiscHelper.INSTANCE.loadAllItems(nbt.getList("InputPedestal", 10), inputPedestal);
		patterns.clear();
		IRecipe<?> recipe = MiscHelper.INSTANCE.getRecipeManager().byKey(new ResourceLocation(nbt.getString("Recipe"))).orElse(null);
		if(inputPedestal.isEmpty()) {
			return;
		}
		if(recipe instanceof ICombinationRecipe) {
			this.recipe = (ICombinationRecipe)recipe;
			List<ItemStack> toCondense = new ArrayList<>(inputPedestal);
			toCondense.add(inputCore);
			input.addAll(MiscHelper.INSTANCE.condenseStacks(toCondense));
			Inventory matrix = new Inventory(inputPedestal.size()+1);
			matrix.setItem(0, inputCore);
			for(int i = 0; i < inputPedestal.size(); ++i) {
				matrix.setItem(i+1, inputPedestal.get(i));
			}
			output = this.recipe.assemble(matrix).copy();
			for(int i = 0; i*9 < input.size(); ++i) {
				patterns.add(new PackagePattern(this, i));
			}
		}
	}

	@Override
	public CompoundNBT write(CompoundNBT nbt) {
		if(recipe != null) {
			nbt.putString("Recipe", recipe.getId().toString());
		}
		CompoundNBT inputCoreTag = inputCore.save(new CompoundNBT());
		ListNBT inputPedestalTag = MiscHelper.INSTANCE.saveAllItems(new ListNBT(), inputPedestal);
		nbt.put("InputCore", inputCoreTag);
		nbt.put("InputPedestal", inputPedestalTag);
		return nbt;
	}

	@Override
	public IPackageRecipeType getRecipeType() {
		return CombinationPackageRecipeType.INSTANCE;
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
	public ItemStack getCoreInput() {
		return inputCore.copy();
	}

	@Override
	public List<ItemStack> getPedestalInputs() {
		return Collections.unmodifiableList(inputPedestal);
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
	public long getEnergyRequired() {
		return recipe.getPowerCost();
	}

	@Override
	public int getEnergyUsage() {
		return recipe.getPowerRate();
	}

	@Override
	public ICombinationRecipe getRecipe() {
		return recipe;
	}

	@Override
	public void generateFromStacks(List<ItemStack> input, List<ItemStack> output, World world) {
		recipe = null;
		inputCore = ItemStack.EMPTY;
		inputPedestal.clear();
		this.input.clear();
		patterns.clear();
		int[] slotArray = CombinationPackageRecipeType.SLOTS.toIntArray();
		ArrayUtils.shift(slotArray, 0, 25, 1);
		for(int i = 0; i < 49; ++i) {
			ItemStack toSet = input.get(slotArray[i]);
			if(!toSet.isEmpty()) {
				toSet.setCount(1);
				if(i == 0) {
					inputCore = toSet.copy();
				}
				else {
					inputPedestal.add(toSet.copy());
				}
			}
			else if(i == 0) {
				return;
			}
		}
		Inventory matrix = new Inventory(inputPedestal.size()+1);
		matrix.setItem(0, inputCore);
		for(int i = 0; i < inputPedestal.size(); ++i) {
			matrix.setItem(i+1, inputPedestal.get(i));
		}
		ICombinationRecipe recipe = MiscHelper.INSTANCE.getRecipeManager().getRecipeFor(RecipeTypes.COMBINATION, matrix, world).orElse(null);
		if(recipe != null) {
			this.recipe = recipe;
			List<ItemStack> toCondense = new ArrayList<>(inputPedestal);
			toCondense.add(inputCore);
			this.input.addAll(MiscHelper.INSTANCE.condenseStacks(toCondense));
			this.output = recipe.assemble(matrix).copy();
			for(int i = 0; i*9 < this.input.size(); ++i) {
				patterns.add(new PackagePattern(this, i));
			}
			return;
		}
	}

	@Override
	public Int2ObjectMap<ItemStack> getEncoderStacks() {
		Int2ObjectMap<ItemStack> map = new Int2ObjectOpenHashMap<>();
		int[] slotArray = CombinationPackageRecipeType.SLOTS.toIntArray();
		ArrayUtils.remove(slotArray, 24);
		map.put(40, inputCore);
		for(int i = 0; i < inputPedestal.size(); ++i) {
			map.put(slotArray[i], inputPedestal.get(i));
		}
		return map;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CombinationPackageRecipeInfo) {
			CombinationPackageRecipeInfo other = (CombinationPackageRecipeInfo)obj;
			return MiscHelper.INSTANCE.recipeEquals(this, recipe, other, other.recipe);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return MiscHelper.INSTANCE.recipeHashCode(this, recipe);
	}
}

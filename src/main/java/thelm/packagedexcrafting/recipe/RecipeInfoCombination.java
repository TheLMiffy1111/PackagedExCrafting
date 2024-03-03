package thelm.packagedexcrafting.recipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.blakebr0.extendedcrafting.crafting.CombinationRecipe;
import com.blakebr0.extendedcrafting.crafting.CombinationRecipeManager;
import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IngredientNBT;
import net.minecraftforge.common.util.RecipeMatcher;
import thelm.packagedauto.api.IPackagePattern;
import thelm.packagedauto.api.IRecipeType;
import thelm.packagedauto.api.MiscUtil;
import thelm.packagedauto.util.PatternHelper;

public class RecipeInfoCombination implements IRecipeInfoCombination {

	CombinationRecipe recipe;
	ItemStack inputCore = ItemStack.EMPTY;
	List<ItemStack> inputPedestal = new ArrayList<>();
	List<ItemStack> input = new ArrayList<>();
	ItemStack output;
	List<IPackagePattern> patterns = new ArrayList<>();

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		inputPedestal.clear();
		input.clear();
		output = ItemStack.EMPTY;
		inputCore = new ItemStack(nbt.getCompoundTag("InputCore"));
		MiscUtil.loadAllItems(nbt.getTagList("InputPedestal", 10), inputPedestal);
		patterns.clear();
		if(inputPedestal.isEmpty()) {
			return;
		}
		for(CombinationRecipe recipe : CombinationRecipeManager.getInstance().getRecipes()) {
			ItemStack inputStack = recipe.getInput();
			if(!inputStack.isEmpty() && inputStack.isItemEqual(inputCore) && recipe.getPedestalItems().size() == inputPedestal.size()) {
				List<Ingredient> matchers = Lists.transform(recipe.getPedestalItems(), RecipeInfoCombination::getIngredient);
				if(RecipeMatcher.findMatches(inputPedestal, matchers) != null) {
					this.recipe = recipe;
					break;
				}
			}
		}
		if(recipe != null) {
			List<ItemStack> toCondense = new ArrayList<>(inputPedestal);
			toCondense.add(inputCore);
			input.addAll(MiscUtil.condenseStacks(toCondense));
			output = recipe.getOutput().copy();
			for(int i = 0; i*9 < input.size(); ++i) {
				patterns.add(new PatternHelper(this, i));
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		NBTTagCompound inputCoreTag = inputCore.writeToNBT(new NBTTagCompound());
		NBTTagList inputPedestalTag = MiscUtil.saveAllItems(new NBTTagList(), inputPedestal);
		nbt.setTag("InputCore", inputCoreTag);
		nbt.setTag("InputPedestal", inputPedestalTag);
		return nbt;
	}

	@Override
	public IRecipeType getRecipeType() {
		return RecipeTypeCombination.INSTANCE;
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
		return recipe.getCost();
	}

	@Override
	public int getEnergyUsage() {
		return recipe.getPerTick();
	}

	@Override
	public CombinationRecipe getRecipe() {
		return recipe;
	}

	@Override
	public void generateFromStacks(List<ItemStack> input, List<ItemStack> output, World world) {
		recipe = null;
		inputCore = ItemStack.EMPTY;
		inputPedestal.clear();
		this.input.clear();
		patterns.clear();
		int[] slotArray = RecipeTypeCombination.SLOTS.toIntArray();
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
		for(CombinationRecipe recipe : CombinationRecipeManager.getInstance().getRecipes()) {
			ItemStack inputStack = recipe.getInput();
			if(!inputStack.isEmpty() && inputStack.isItemEqual(inputCore) && recipe.getPedestalItems().size() == inputPedestal.size()) {
				List<Ingredient> matchers = Lists.transform(recipe.getPedestalItems(), RecipeInfoCombination::getIngredient);
				if(RecipeMatcher.findMatches(inputPedestal, matchers) != null) {
					this.recipe = recipe;
					List<ItemStack> toCondense = new ArrayList<>(inputPedestal);
					toCondense.add(inputCore);
					this.input.addAll(MiscUtil.condenseStacks(toCondense));
					this.output = recipe.getOutput().copy();
					for(int i = 0; i*9 < this.input.size(); ++i) {
						patterns.add(new PatternHelper(this, i));
					}
					return;
				}
			}
		}
	}

	protected static Ingredient getIngredient(Object obj) {
		if(obj instanceof ItemStack) {
			ItemStack stack = (ItemStack)obj;
			return stack.hasTagCompound() ? new IngredientNBT(stack) {} : Ingredient.fromStacks(stack);
		}
		else if(obj instanceof List<?>) {
			return Ingredient.fromStacks(((List<?>)obj).stream().toArray(i->new ItemStack[i]));
		}
		else {
			return CraftingHelper.getIngredient(obj);
		}
	}

	@Override
	public Int2ObjectMap<ItemStack> getEncoderStacks() {
		Int2ObjectMap<ItemStack> map = new Int2ObjectOpenHashMap<>();
		int[] slotArray = RecipeTypeCombination.SLOTS.toIntArray();
		ArrayUtils.remove(slotArray, 24);
		map.put(40, inputCore);
		for(int i = 0; i < inputPedestal.size(); ++i) {
			map.put(slotArray[i], inputPedestal.get(i));
		}
		return map;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof RecipeInfoCombination) {
			RecipeInfoCombination other = (RecipeInfoCombination)obj;
			return MiscUtil.recipeEquals(this, recipe, other, other.recipe);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return MiscUtil.recipeHashCode(this, recipe);
	}
}

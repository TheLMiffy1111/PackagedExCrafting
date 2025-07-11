package thelm.packagedexcrafting.recipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.blakebr0.cucumber.crafting.ShapelessCraftingInput;
import com.blakebr0.extendedcrafting.api.crafting.ICombinationRecipe;
import com.blakebr0.extendedcrafting.init.ModRecipeTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import thelm.packagedauto.api.IPackagePattern;
import thelm.packagedauto.api.IPackageRecipeType;
import thelm.packagedauto.util.MiscHelper;
import thelm.packagedauto.util.PackagePattern;

public class CombinationPackageRecipeInfo implements ICombinationPackageRecipeInfo {

	public static final MapCodec<CombinationPackageRecipeInfo> MAP_CODEC = RecordCodecBuilder.mapCodec(instance->instance.group(
			ResourceLocation.CODEC.fieldOf("id").forGetter(CombinationPackageRecipeInfo::getRecipeId),
			ItemStack.OPTIONAL_CODEC.orElse(ItemStack.EMPTY).fieldOf("core").forGetter(CombinationPackageRecipeInfo::getCoreInput),
			ItemStack.OPTIONAL_CODEC.orElse(ItemStack.EMPTY).sizeLimitedListOf(48).fieldOf("pedestal").forGetter(CombinationPackageRecipeInfo::getPedestalInputs)).
			apply(instance, CombinationPackageRecipeInfo::new));
	public static final Codec<CombinationPackageRecipeInfo> CODEC = MAP_CODEC.codec();
	public static final StreamCodec<RegistryFriendlyByteBuf, CombinationPackageRecipeInfo> STREAM_CODEC = StreamCodec.composite(
			ResourceLocation.STREAM_CODEC, CombinationPackageRecipeInfo::getRecipeId,
			ItemStack.OPTIONAL_STREAM_CODEC, CombinationPackageRecipeInfo::getCoreInput,
			ItemStack.OPTIONAL_LIST_STREAM_CODEC, CombinationPackageRecipeInfo::getPedestalInputs,
			CombinationPackageRecipeInfo::new);

	private final ResourceLocation id;
	private final ICombinationRecipe recipe;
	private final ItemStack inputCore;
	private final List<ItemStack> inputPedestal;
	private final List<ItemStack> input;
	private final ItemStack output;
	private final List<IPackagePattern> patterns = new ArrayList<>();

	public CombinationPackageRecipeInfo(ResourceLocation id, ItemStack inputCore, List<ItemStack> inputPedestal) {
		this.id = id;
		this.inputCore = inputCore;
		this.inputPedestal = inputPedestal;
		List<ItemStack> matrixList = new ArrayList<>(inputPedestal.size()+1);
		matrixList.add(inputCore);
		matrixList.addAll(inputPedestal);
		CraftingInput matrix = new ShapelessCraftingInput(matrixList);
		input = MiscHelper.INSTANCE.condenseStacks(matrixList);
		for(int i = 0; i*9 < input.size(); ++i) {
			patterns.add(new PackagePattern(this, i));
		}
		Recipe<?> recipeSer = MiscHelper.INSTANCE.getRecipeManager().byKey(id).map(RecipeHolder::value).orElse(null);
		if(recipeSer instanceof ICombinationRecipe combinationRecipe) {
			recipe = combinationRecipe;
			output = recipe.assemble(matrix, MiscHelper.INSTANCE.getRegistryAccess()).copy();
		}
		else {
			recipe = null;
			output = ItemStack.EMPTY;
		}
	}

	public CombinationPackageRecipeInfo(List<ItemStack> inputs, Level level) {
		ItemStack inputCore = ItemStack.EMPTY;
		inputPedestal = new ArrayList<>();
		int[] slotArray = CombinationPackageRecipeType.SLOTS.toIntArray();
		ArrayUtils.shift(slotArray, 0, 25, 1);
		for(int i = 0; i < 49; ++i) {
			ItemStack toSet = inputs.get(slotArray[i]);
			if(!toSet.isEmpty()) {
				toSet.setCount(1);
				if(i == 0) {
					inputCore = toSet.copy();
				}
				else {
					inputPedestal.add(toSet.copy());
				}
			}
		}
		this.inputCore = inputCore;
		List<ItemStack> matrixList = new ArrayList<>(inputPedestal.size()+1);
		matrixList.add(inputCore);
		matrixList.addAll(inputPedestal);
		CraftingInput matrix = new ShapelessCraftingInput(matrixList);
		RecipeHolder<ICombinationRecipe> recipeHolder = MiscHelper.INSTANCE.getRecipeManager().getRecipeFor(ModRecipeTypes.COMBINATION.get(), matrix, level).orElse(null);
		if(recipeHolder != null) {
			id = recipeHolder.id();
			recipe = recipeHolder.value();
			output = recipe.assemble(matrix, level.registryAccess()).copy();
		}
		else {
			id = null;
			recipe = null;
			output = null;
		}
		input = MiscHelper.INSTANCE.condenseStacks(matrixList);
		for(int i = 0; i*9 < input.size(); ++i) {
			this.patterns.add(new PackagePattern(this, i));
		}
	}

	@Override
	public IPackageRecipeType getRecipeType() {
		return CombinationPackageRecipeType.INSTANCE;
	}

	@Override
	public boolean isValid() {
		return id != null && recipe != null;
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

	public ResourceLocation getRecipeId() {
		return id;
	}

	@Override
	public Int2ObjectMap<ItemStack> getEncoderStacks() {
		Int2ObjectMap<ItemStack> map = new Int2ObjectOpenHashMap<>();
		int[] slotArray = CombinationPackageRecipeType.SLOTS.toIntArray();
		slotArray = ArrayUtils.remove(slotArray, 24);
		map.put(40, inputCore);
		for(int i = 0; i < inputPedestal.size(); ++i) {
			map.put(slotArray[i], inputPedestal.get(i));
		}
		return map;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CombinationPackageRecipeInfo other) {
			return MiscHelper.INSTANCE.recipeEquals(this, recipe, other, other.recipe);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return MiscHelper.INSTANCE.recipeHashCode(this, recipe);
	}
}

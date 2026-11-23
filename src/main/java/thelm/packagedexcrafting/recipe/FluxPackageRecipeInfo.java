package thelm.packagedexcrafting.recipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.blakebr0.extendedcrafting.api.crafting.IFluxCrafterRecipe;
import com.blakebr0.extendedcrafting.init.ModRecipeTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
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

public class FluxPackageRecipeInfo implements IFluxPackageRecipeInfo {

	public static final MapCodec<FluxPackageRecipeInfo> MAP_CODEC = RecordCodecBuilder.mapCodec(instance->instance.group(
			ResourceLocation.CODEC.fieldOf("id").forGetter(FluxPackageRecipeInfo::getRecipeId),
			Codec.INT.fieldOf("width").forGetter(FluxPackageRecipeInfo::getMatrixWidth),
			Codec.INT.fieldOf("height").forGetter(FluxPackageRecipeInfo::getMatrixHeight),
			ItemStack.OPTIONAL_CODEC.orElse(ItemStack.EMPTY).sizeLimitedListOf(9).fieldOf("input").forGetter(FluxPackageRecipeInfo::getMatrixAsList)).
			apply(instance, FluxPackageRecipeInfo::new));
	public static final Codec<FluxPackageRecipeInfo> CODEC = MAP_CODEC.codec();
	public static final StreamCodec<RegistryFriendlyByteBuf, FluxPackageRecipeInfo> STREAM_CODEC = StreamCodec.composite(
			ResourceLocation.STREAM_CODEC, FluxPackageRecipeInfo::getRecipeId,
			ByteBufCodecs.INT, FluxPackageRecipeInfo::getMatrixWidth,
			ByteBufCodecs.INT, FluxPackageRecipeInfo::getMatrixHeight,
			ItemStack.OPTIONAL_LIST_STREAM_CODEC, FluxPackageRecipeInfo::getMatrixAsList,
			FluxPackageRecipeInfo::new);

	private final ResourceLocation id;
	private final IFluxCrafterRecipe recipe;
	private final List<ItemStack> input;
	private final CraftingInput matrix;
	private final ItemStack output;
	private final List<IPackagePattern> patterns = new ArrayList<>();

	public FluxPackageRecipeInfo(ResourceLocation id, int width, int height, List<ItemStack> matrixSer) {
		this.id = id;
		matrix = CraftingInput.of(width, height, matrixSer);
		input = MiscHelper.INSTANCE.condenseStacks(matrix.items());
		for(int i = 0; i*9 < input.size(); ++i) {
			patterns.add(new PackagePattern(this, i));
		}
		Recipe<?> recipeSer = MiscHelper.INSTANCE.getRecipeManager().byKey(id).map(RecipeHolder::value).orElse(null);
		if(recipeSer instanceof IFluxCrafterRecipe fluxRecipe) {
			recipe = fluxRecipe;
			output = recipe.assemble(matrix, MiscHelper.INSTANCE.getRegistryAccess()).copy();
		}
		else {
			recipe = null;
			output = ItemStack.EMPTY;
		}
	}

	public FluxPackageRecipeInfo(List<ItemStack> inputs, Level level) {
		NonNullList<ItemStack> matrixList = NonNullList.withSize(9, ItemStack.EMPTY);
		int[] slotArray = FluxPackageRecipeType.SLOTS.toIntArray();
		for(int i = 0; i < 9; ++i) {
			ItemStack toSet = inputs.get(slotArray[i]);
			toSet.setCount(1);
			matrixList.set(i, toSet.copy());
		}
		matrix = CraftingInput.of(3, 3, matrixList);
		RecipeHolder<IFluxCrafterRecipe> recipeHolder = MiscHelper.INSTANCE.getRecipeManager().getRecipeFor(ModRecipeTypes.FLUX_CRAFTER.get(), matrix, level).orElse(null);
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
		input = MiscHelper.INSTANCE.condenseStacks(matrix.items());
		for(int i = 0; i*9 < input.size(); ++i) {
			this.patterns.add(new PackagePattern(this, i));
		}
	}

	@Override
	public IPackageRecipeType getRecipeType() {
		return FluxPackageRecipeType.INSTANCE;
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
	public List<ItemStack> getInputs() {
		return Collections.unmodifiableList(input);
	}

	@Override
	public ItemStack getOutput() {
		return output.copy();
	}

	@Override
	public IFluxCrafterRecipe getRecipe() {
		return recipe;
	}

	public ResourceLocation getRecipeId() {
		return id;
	}

	@Override
	public CraftingInput getMatrix() {
		return matrix;
	}

	public List<ItemStack> getMatrixAsList() {
		return Collections.unmodifiableList(matrix.items());
	}

	public int getMatrixWidth() {
		return matrix.width();
	}

	public int getMatrixHeight() {
		return matrix.height();
	}

	@Override
	public int getEnergyRequired() {
		return recipe.getPowerRequired();
	}

	@Override
	public int getEnergyUsage() {
		return recipe.getPowerRate();
	}

	@Override
	public Int2ObjectMap<ItemStack> getEncoderStacks() {
		Int2ObjectMap<ItemStack> map = new Int2ObjectOpenHashMap<>();
		int[] slotArray = FluxPackageRecipeType.SLOTS.toIntArray();
		for(int i = 0; i < matrix.height(); ++i) {
			for(int j = 0; j < matrix.width(); ++j) {
				map.put(slotArray[i*3+j], matrix.getItem(i*matrix.width()+j));
			}
		}
		return map;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof FluxPackageRecipeInfo other) {
			return MiscHelper.INSTANCE.recipeEquals(this, recipe, other, other.recipe);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return MiscHelper.INSTANCE.recipeHashCode(this, recipe);
	}
}

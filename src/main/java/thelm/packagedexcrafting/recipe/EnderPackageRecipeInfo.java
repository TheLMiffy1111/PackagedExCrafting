package thelm.packagedexcrafting.recipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.blakebr0.extendedcrafting.api.crafting.IEnderCrafterRecipe;
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

public class EnderPackageRecipeInfo implements IEnderPackageRecipeInfo {

	public static final MapCodec<EnderPackageRecipeInfo> MAP_CODEC = RecordCodecBuilder.mapCodec(instance->instance.group(
			ResourceLocation.CODEC.fieldOf("id").forGetter(EnderPackageRecipeInfo::getRecipeId),
			Codec.INT.fieldOf("width").forGetter(EnderPackageRecipeInfo::getMatrixWidth),
			Codec.INT.fieldOf("height").forGetter(EnderPackageRecipeInfo::getMatrixHeight),
			ItemStack.OPTIONAL_CODEC.orElse(ItemStack.EMPTY).sizeLimitedListOf(9).fieldOf("input").forGetter(EnderPackageRecipeInfo::getMatrixAsList)).
			apply(instance, EnderPackageRecipeInfo::new));
	public static final Codec<EnderPackageRecipeInfo> CODEC = MAP_CODEC.codec();
	public static final StreamCodec<RegistryFriendlyByteBuf, EnderPackageRecipeInfo> STREAM_CODEC = StreamCodec.composite(
			ResourceLocation.STREAM_CODEC, EnderPackageRecipeInfo::getRecipeId,
			ByteBufCodecs.INT, EnderPackageRecipeInfo::getMatrixWidth,
			ByteBufCodecs.INT, EnderPackageRecipeInfo::getMatrixHeight,
			ItemStack.OPTIONAL_LIST_STREAM_CODEC, EnderPackageRecipeInfo::getMatrixAsList,
			EnderPackageRecipeInfo::new);

	private final ResourceLocation id;
	private final IEnderCrafterRecipe recipe;
	private final List<ItemStack> input;
	private final CraftingInput matrix;
	private final ItemStack output;
	private final List<IPackagePattern> patterns = new ArrayList<>();

	public EnderPackageRecipeInfo(ResourceLocation id, int width, int height, List<ItemStack> matrixSer) {
		this.id = id;
		matrix = CraftingInput.of(width, height, matrixSer);
		input = MiscHelper.INSTANCE.condenseStacks(matrix.items());
		for(int i = 0; i*9 < input.size(); ++i) {
			patterns.add(new PackagePattern(this, i));
		}
		Recipe<?> recipeSer = MiscHelper.INSTANCE.getRecipeManager().byKey(id).map(RecipeHolder::value).orElse(null);
		if(recipeSer instanceof IEnderCrafterRecipe enderRecipe) {
			recipe = enderRecipe;
			output = recipe.assemble(matrix, MiscHelper.INSTANCE.getRegistryAccess()).copy();
		}
		else {
			recipe = null;
			output = ItemStack.EMPTY;
		}
	}

	public EnderPackageRecipeInfo(List<ItemStack> inputs, Level level) {
		NonNullList<ItemStack> matrixList = NonNullList.withSize(9, ItemStack.EMPTY);
		int[] slotArray = EnderPackageRecipeType.SLOTS.toIntArray();
		for(int i = 0; i < 9; ++i) {
			ItemStack toSet = inputs.get(slotArray[i]);
			toSet.setCount(1);
			matrixList.set(i, toSet.copy());
		}
		matrix = CraftingInput.of(3, 3, matrixList);
		RecipeHolder<IEnderCrafterRecipe> recipeHolder = MiscHelper.INSTANCE.getRecipeManager().getRecipeFor(ModRecipeTypes.ENDER_CRAFTER.get(), matrix, level).orElse(null);
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
		return EnderPackageRecipeType.INSTANCE;
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
	public IEnderCrafterRecipe getRecipe() {
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
	public int getTimeRequired() {
		return recipe.getCraftingTime();
	}

	@Override
	public Int2ObjectMap<ItemStack> getEncoderStacks() {
		Int2ObjectMap<ItemStack> map = new Int2ObjectOpenHashMap<>();
		int[] slotArray = EnderPackageRecipeType.SLOTS.toIntArray();
		for(int i = 0; i < matrix.height(); ++i) {
			for(int j = 0; j < matrix.width(); ++j) {
				map.put(slotArray[i*3+j], matrix.getItem(i*matrix.width()+j));
			}
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

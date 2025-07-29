package thelm.packagedexcrafting.block.entity;

import java.util.List;

import com.blakebr0.extendedcrafting.tileentity.FluxAlternatorTileEntity;
import com.google.common.base.Predicates;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import thelm.packagedauto.api.IPackageCraftingMachine;
import thelm.packagedauto.api.IPackageRecipeInfo;
import thelm.packagedauto.block.PackagedAutoBlocks;
import thelm.packagedauto.block.entity.BaseBlockEntity;
import thelm.packagedauto.energy.EnergyStorage;
import thelm.packagedauto.util.MiscHelper;
import thelm.packagedexcrafting.inventory.FluxCrafterItemHandler;
import thelm.packagedexcrafting.menu.FluxCrafterMenu;
import thelm.packagedexcrafting.recipe.IFluxPackageRecipeInfo;

public class FluxCrafterBlockEntity extends BaseBlockEntity implements IPackageCraftingMachine {

	public static int energyCapacity = 5000;
	public static int energyReq = 500;
	public static int energyUsage = 100;
	public static boolean drawMEEnergy = true;

	public boolean isWorking = false;
	public int progressReq = 0;
	public int progress = 0;
	public int alternatorUsage = 0;
	public int remainingProgress = 0;
	public IFluxPackageRecipeInfo currentRecipe;

	public FluxCrafterBlockEntity(BlockPos pos, BlockState state) {
		super(PackagedExCraftingBlockEntities.FLUX_CRAFTER.get(), pos, state);
		setItemHandler(new FluxCrafterItemHandler(this));
		setEnergyStorage(new EnergyStorage(this, energyCapacity));
	}

	@Override
	protected Component getDefaultName() {
		return Component.translatable("block.packagedexcrafting.flux_crafter");
	}

	@Override
	public void tick() {
		if(!level.isClientSide) {
			if(isWorking) {
				tickProcess();
				if(remainingProgress <= 0) {
					finishProcess();
					ejectItems();
				}
			}
			chargeEnergy();
			if(level.getGameTime() % 8 == 0) {
				ejectItems();
			}
		}
	}

	@Override
	public boolean acceptPackage(IPackageRecipeInfo recipeInfo, List<ItemStack> stacks, Direction direction) {
		if(!isBusy() && recipeInfo.isValid() && recipeInfo instanceof IFluxPackageRecipeInfo recipe) {
			ItemStack slotStack = itemHandler.getStackInSlot(9);
			ItemStack outputStack = recipe.getOutput();
			if(slotStack.isEmpty() || ItemStack.isSameItemSameComponents(slotStack, outputStack) && slotStack.getCount()+outputStack.getCount() <= outputStack.getMaxStackSize()) {
				currentRecipe = recipe;
				isWorking = true;
				progressReq = recipe.getEnergyRequired();
				alternatorUsage = recipe.getEnergyUsage();
				remainingProgress = energyReq;
				CraftingInput matrix = recipe.getMatrix();
				for(int i = 0; i < matrix.height(); ++i) {
					for(int j = 0; j < matrix.width(); ++j) {
						itemHandler.setStackInSlot(i*3+j, matrix.getItem(i*matrix.width()+j).copy());
					}
				}
				setChanged();
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isBusy() {
		return isWorking || !itemHandler.getStacks().subList(0, 9).stream().allMatch(ItemStack::isEmpty);
	}

	protected void tickProcess() {
		if(progress < progressReq) {
			List<FluxAlternatorTileEntity> alternators = getAlternators();
			int alternatorCount = alternators.size();
			progress += alternatorCount*alternatorUsage;
			for(FluxAlternatorTileEntity alternator : alternators) {
				alternator.getEnergy().extractEnergy(alternatorUsage, false);
				BlockPos alternatorPos = alternator.getBlockPos();
				if(level.isEmptyBlock(alternatorPos.above())) {
					spawnParticles(DustParticleOptions.REDSTONE, alternatorPos, 1, 1);
				}
			}
		}
		if(progress >= progressReq) {
			progress = progressReq;
			int energy = energyStorage.extractEnergy(Math.min(energyUsage, remainingProgress), false);
			remainingProgress -= energy;
		}
	}

	protected void finishProcess() {
		if(currentRecipe == null) {
			endProcess();
			return;
		}
		if(itemHandler.getStackInSlot(9).isEmpty()) {
			itemHandler.setStackInSlot(9, currentRecipe.getOutput());
		}
		else {
			itemHandler.getStackInSlot(9).grow(currentRecipe.getOutput().getCount());
		}
		List<ItemStack> remainingItems = currentRecipe.getRemainingItems();
		CraftingInput matrix = currentRecipe.getMatrix();
		for(int i = 0; i < matrix.height(); ++i) {
			for(int j = 0; j < matrix.width(); ++j) {
				itemHandler.setStackInSlot(i*3+j, remainingItems.get(i*matrix.width()+j));
			}
		}
		endProcess();
	}

	public void endProcess() {
		progressReq = 0;
		progress = 0;
		alternatorUsage = 0;
		remainingProgress = 0;
		isWorking = false;
		currentRecipe = null;
		setChanged();
	}

	protected List<FluxAlternatorTileEntity> getAlternators() {
		return BlockPos.betweenClosedStream(worldPosition.offset(-3, -3, -3), worldPosition.offset(3, 3, 3)).map(pos->{
			if(level.getBlockEntity(pos) instanceof FluxAlternatorTileEntity alternator && alternator.getEnergy().getEnergyStored() >= alternatorUsage) {
				return alternator;
			}
			return null;
		}).filter(Predicates.notNull()).toList();
	}

	protected void ejectItems() {
		int endIndex = isWorking ? 9 : 0;
		for(Direction direction : Direction.values()) {
			BlockPos offsetPos = worldPosition.relative(direction);
			Block block = level.getBlockState(offsetPos).getBlock();
			IItemHandler itemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, offsetPos, direction.getOpposite());
			if(block != PackagedAutoBlocks.UNPACKAGER.get() && itemHandler != null) {
				for(int i = 9; i >= endIndex; --i) {
					ItemStack stack = this.itemHandler.getStackInSlot(i);
					if(stack.isEmpty()) {
						continue;
					}
					ItemStack stackRem = ItemHandlerHelper.insertItem(itemHandler, stack, false);
					this.itemHandler.setStackInSlot(i, stackRem);
				}
			}
		}
	}

	protected void chargeEnergy() {
		ItemStack energyStack = itemHandler.getStackInSlot(10);
		IEnergyStorage itemEnergyStorage = energyStack.getCapability(Capabilities.EnergyStorage.ITEM);
		if(itemEnergyStorage != null) {
			int energyRequest = Math.min(energyStorage.getMaxReceive(), energyStorage.getMaxEnergyStored() - energyStorage.getEnergyStored());
			energyStorage.receiveEnergy(itemEnergyStorage.extractEnergy(energyRequest, false), false);
			if(energyStack.getCount() <= 0) {
				itemHandler.setStackInSlot(10, ItemStack.EMPTY);
			}
		}
	}

	protected <T extends ParticleOptions> void spawnParticles(T particle, BlockPos pos, double yOffset, int count) {
		if(level == null || level.isClientSide()) {
			return;
		}
		ServerLevel level = (ServerLevel)this.level;
		double x = pos.getX()+0.5;
		double y = pos.getY()+yOffset;
		double z = pos.getZ()+0.5;
		level.sendParticles(particle, x, y, z, count, 0, 0, 0, 0.1);
	}

	@Override
	public int getComparatorSignal() {
		if(isWorking) {
			return 1;
		}
		if(!itemHandler.getStacks().subList(0, 10).stream().allMatch(ItemStack::isEmpty)) {
			return 15;
		}
		return 0;
	}

	@Override
	public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
		super.loadAdditional(nbt, registries);
		isWorking = nbt.getBoolean("working");
		progressReq = nbt.getInt("progress_req");
		progress = nbt.getInt("progress");
		alternatorUsage = nbt.getInt("alternator_usage");
		remainingProgress = nbt.getInt("energy_progress");
		currentRecipe = null;
		if(nbt.contains("recipe")) {
			CompoundTag tag = nbt.getCompound("recipe");
			IPackageRecipeInfo recipe = MiscHelper.INSTANCE.loadRecipe(tag, registries);
			if(recipe instanceof IFluxPackageRecipeInfo fluxRecipe) {
				currentRecipe = fluxRecipe;
			}
		}
	}

	@Override
	public void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
		super.saveAdditional(nbt, registries);
		nbt.putBoolean("working", isWorking);
		nbt.putInt("progress_req", progressReq);
		nbt.putInt("progress", progress);
		nbt.putInt("alternator_usage", alternatorUsage);
		nbt.putInt("energy_progress", remainingProgress);
		if(currentRecipe != null) {
			CompoundTag tag = MiscHelper.INSTANCE.saveRecipe(new CompoundTag(), currentRecipe, registries);
			nbt.put("Recipe", tag);
		}
	}

	public int getScaledEnergy(int scale) {
		if(energyStorage.getMaxEnergyStored() <= 0) {
			return 0;
		}
		return Math.min(scale * energyStorage.getEnergyStored() / energyStorage.getMaxEnergyStored(), scale);
	}

	public int getScaledProgress(int scale) {
		if(progress <= 0 || progressReq <= 0) {
			return 0;
		}
		return scale * progress / progressReq;
	}

	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player) {
		sync(false);
		return new FluxCrafterMenu(windowId, inventory, this);
	}
}

package thelm.packagedexcrafting.block.entity;

import java.util.List;

import com.blakebr0.extendedcrafting.block.EnderAlternatorBlock;
import com.google.common.base.Predicates;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
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
import thelm.packagedexcrafting.inventory.EnderCrafterItemHandler;
import thelm.packagedexcrafting.menu.EnderCrafterMenu;
import thelm.packagedexcrafting.recipe.IEnderPackageRecipeInfo;

public class EnderCrafterBlockEntity extends BaseBlockEntity implements IPackageCraftingMachine {

	public static int energyCapacity = 5000;
	public static double alternatorEff = 0.02;
	public static int energyReq = 500;
	public static int energyUsage = 100;
	public static boolean drawMEEnergy = true;

	public boolean isWorking = false;
	public int progressReq = 0;
	public int progress = 0;
	public int actualProgressReq = 0;
	public int remainingProgress = 0;
	public IEnderPackageRecipeInfo currentRecipe;

	public EnderCrafterBlockEntity(BlockPos pos, BlockState state) {
		super(PackagedExCraftingBlockEntities.ENDER_CRAFTER.get(), pos, state);
		setItemHandler(new EnderCrafterItemHandler(this));
		setEnergyStorage(new EnergyStorage(this, energyCapacity));
	}

	@Override
	protected Component getDefaultName() {
		return Component.translatable("block.packagedexcrafting.ender_crafter");
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
		if(!isBusy() && recipeInfo.isValid() && recipeInfo instanceof IEnderPackageRecipeInfo recipe) {
			ItemStack slotStack = itemHandler.getStackInSlot(9);
			ItemStack outputStack = recipe.getOutput();
			if(slotStack.isEmpty() || ItemStack.isSameItemSameComponents(slotStack, outputStack) && slotStack.getCount()+outputStack.getCount() <= outputStack.getMaxStackSize()) {
				currentRecipe = recipe;
				isWorking = true;
				progressReq = recipe.getTimeRequired()*20;
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
		List<BlockPos> alternators = getAlternators();
		int alternatorCount = alternators.size();
		if(alternatorCount > 0) {
			progress++;
			actualProgressReq = (int)Math.max(progressReq*(1-alternatorEff*alternatorCount), 0);
			for(BlockPos alternatorPos : alternators) {
				BlockState state = level.getBlockState(alternatorPos);
				Direction direction = state.getOptionalValue(BlockStateProperties.FACING).orElse(Direction.UP);
				if(level.isEmptyBlock(alternatorPos.relative(direction))) {
					sendAlternatorParticles(alternatorPos, direction);
				}
			}
		}
		else {
			actualProgressReq = progressReq;
		}
		if(progress >= actualProgressReq) {
			progress = actualProgressReq;
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
		CraftingInput matrix = currentRecipe.getMatrix();
		for(int i = 0; i < matrix.height(); ++i) {
			for(int j = 0; j < matrix.width(); ++j) {
				itemHandler.setStackInSlot(i*3+j, ItemStack.EMPTY);
			}
		}
		endProcess();
	}

	public void endProcess() {
		progressReq = 0;
		progress = 0;
		remainingProgress = 0;
		isWorking = false;
		currentRecipe = null;
		setChanged();
	}

	protected List<BlockPos> getAlternators() {
		return BlockPos.betweenClosedStream(worldPosition.offset(-3, -3, -3), worldPosition.offset(3, 3, 3)).map(pos->{
			if(level.getBlockState(pos).getBlock() instanceof EnderAlternatorBlock) {
				return pos.immutable();
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

	protected void sendAlternatorParticles(BlockPos pos, Direction direction) {
		if(level == null || level.isClientSide()) {
			return;
		}
		ServerLevel level = (ServerLevel)this.level;
		double x = pos.getX()+direction.getStepX()*0.5+0.5;
		double y = pos.getY()+direction.getStepY()*0.625+0.375;
		double z = pos.getZ()+direction.getStepZ()*0.5+0.5;
		level.sendParticles(ParticleTypes.PORTAL, x, y, z, 1, 0, 0, 0, 0.1);
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
		remainingProgress = nbt.getInt("energy_progress");
		currentRecipe = null;
		if(nbt.contains("recipe")) {
			CompoundTag tag = nbt.getCompound("recipe");
			IPackageRecipeInfo recipe = MiscHelper.INSTANCE.loadRecipe(tag, registries);
			if(recipe instanceof IEnderPackageRecipeInfo enderRecipe) {
				currentRecipe = enderRecipe;
			}
		}
	}

	@Override
	public void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
		super.saveAdditional(nbt, registries);
		nbt.putBoolean("working", isWorking);
		nbt.putInt("progress_req", progressReq);
		nbt.putInt("progress", progress);
		nbt.putInt("energy_progress", remainingProgress);
		if(currentRecipe != null) {
			CompoundTag tag = MiscHelper.INSTANCE.saveRecipe(new CompoundTag(), currentRecipe, registries);
			nbt.put("recipe", tag);
		}
	}

	public int getScaledEnergy(int scale) {
		if(energyStorage.getMaxEnergyStored() <= 0) {
			return 0;
		}
		return Math.min(scale * energyStorage.getEnergyStored() / energyStorage.getMaxEnergyStored(), scale);
	}

	public int getScaledProgress(int scale) {
		if(progress <= 0 || actualProgressReq <= 0) {
			return 0;
		}
		return scale * progress / actualProgressReq;
	}

	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player) {
		sync(false);
		return new EnderCrafterMenu(windowId, inventory, this);
	}
}

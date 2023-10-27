package thelm.packagedexcrafting.block.entity;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import thelm.packagedauto.api.IPackageCraftingMachine;
import thelm.packagedauto.api.IPackageRecipeInfo;
import thelm.packagedauto.block.entity.BaseBlockEntity;
import thelm.packagedauto.block.entity.UnpackagerBlockEntity;
import thelm.packagedauto.energy.EnergyStorage;
import thelm.packagedauto.util.MiscHelper;
import thelm.packagedexcrafting.block.BasicCrafterBlock;
import thelm.packagedexcrafting.integration.appeng.blockentity.AEBasicCrafterBlockEntity;
import thelm.packagedexcrafting.inventory.BasicCrafterItemHandler;
import thelm.packagedexcrafting.menu.BasicCrafterMenu;
import thelm.packagedexcrafting.recipe.ITablePackageRecipeInfo;

public class BasicCrafterBlockEntity extends BaseBlockEntity implements IPackageCraftingMachine {

	public static final BlockEntityType<BasicCrafterBlockEntity> TYPE_INSTANCE = BlockEntityType.Builder.
			of(MiscHelper.INSTANCE.<BlockEntityType.BlockEntitySupplier<BasicCrafterBlockEntity>>conditionalSupplier(
					()->ModList.get().isLoaded("ae2"),
					()->()->AEBasicCrafterBlockEntity::new, ()->()->BasicCrafterBlockEntity::new).get(),
					BasicCrafterBlock.INSTANCE).build(null);

	public static int energyCapacity = 5000;
	public static int energyReq = 500;
	public static int energyUsage = 100;
	public static boolean drawMEEnergy = true;

	public boolean isWorking = false;
	public int remainingProgress = 0;
	public ITablePackageRecipeInfo currentRecipe;

	public BasicCrafterBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE_INSTANCE, pos, state);
		setItemHandler(new BasicCrafterItemHandler(this));
		setEnergyStorage(new EnergyStorage(this, energyCapacity));
	}

	@Override
	protected Component getDefaultName() {
		return Component.translatable("block.packagedexcrafting.basic_crafter");
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
		if(!isBusy() && recipeInfo instanceof ITablePackageRecipeInfo recipe) {
			if(recipe.getTier() == 1) {
				ItemStack slotStack = itemHandler.getStackInSlot(9);
				ItemStack outputStack = recipe.getOutput();
				if(slotStack.isEmpty() || ItemStack.isSameItemSameTags(slotStack, outputStack) && slotStack.getCount()+outputStack.getCount() <= outputStack.getMaxStackSize()) {
					currentRecipe = recipe;
					isWorking = true;
					remainingProgress = energyReq;
					for(int i = 0; i < 9; ++i) {
						itemHandler.setStackInSlot(i, recipe.getMatrix().getItem(i).copy());
					}
					setChanged();
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean isBusy() {
		return isWorking || !itemHandler.getStacks().subList(0, 9).stream().allMatch(ItemStack::isEmpty);
	}

	protected void tickProcess() {
		int energy = energyStorage.extractEnergy(Math.min(energyUsage, remainingProgress), false);
		remainingProgress -= energy;
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
		for(int i = 0; i < 9; ++i) {
			itemHandler.setStackInSlot(i, remainingItems.get(i));
		}
		endProcess();
	}

	public void endProcess() {
		remainingProgress = 0;
		isWorking = false;
		currentRecipe = null;
		setChanged();
	}

	protected void ejectItems() {
		int endIndex = isWorking ? 9 : 0;
		for(Direction direction : Direction.values()) {
			BlockEntity blockEntity = level.getBlockEntity(worldPosition.relative(direction));
			if(blockEntity != null && !(blockEntity instanceof UnpackagerBlockEntity) && blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, direction.getOpposite()).isPresent()) {
				IItemHandler itemHandler = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, direction.getOpposite()).resolve().get();
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
		if(energyStack.getCapability(ForgeCapabilities.ENERGY, null).isPresent()) {
			int energyRequest = Math.min(energyStorage.getMaxReceive(), energyStorage.getMaxEnergyStored() - energyStorage.getEnergyStored());
			energyStorage.receiveEnergy(energyStack.getCapability(ForgeCapabilities.ENERGY).resolve().get().extractEnergy(energyRequest, false), false);
			if(energyStack.getCount() <= 0) {
				itemHandler.setStackInSlot(10, ItemStack.EMPTY);
			}
		}
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
	public void load(CompoundTag nbt) {
		super.load(nbt);
		isWorking = nbt.getBoolean("Working");
		remainingProgress = nbt.getInt("Progress");
		currentRecipe = null;
		if(nbt.contains("Recipe")) {
			CompoundTag tag = nbt.getCompound("Recipe");
			IPackageRecipeInfo recipe = MiscHelper.INSTANCE.loadRecipe(tag);
			if(recipe instanceof ITablePackageRecipeInfo tableRecipe && tableRecipe.getTier() == 1) {
				currentRecipe = tableRecipe;
			}
		}
	}

	@Override
	public void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);
		nbt.putBoolean("Working", isWorking);
		nbt.putInt("Progress", remainingProgress);
		if(currentRecipe != null) {
			CompoundTag tag = MiscHelper.INSTANCE.saveRecipe(new CompoundTag(), currentRecipe);
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
		if(remainingProgress <= 0 || energyReq <= 0) {
			return 0;
		}
		return scale * (energyReq-remainingProgress) / energyReq;
	}

	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player) {
		sync(false);
		return new BasicCrafterMenu(windowId, inventory, this);
	}
}

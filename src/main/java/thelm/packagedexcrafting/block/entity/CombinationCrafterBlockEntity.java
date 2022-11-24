package thelm.packagedexcrafting.block.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Predicates;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
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
import thelm.packagedauto.api.IPackageCraftingMachine;
import thelm.packagedauto.api.IPackageRecipeInfo;
import thelm.packagedauto.block.entity.BaseBlockEntity;
import thelm.packagedauto.block.entity.UnpackagerBlockEntity;
import thelm.packagedauto.energy.EnergyStorage;
import thelm.packagedauto.util.MiscHelper;
import thelm.packagedexcrafting.block.CombinationCrafterBlock;
import thelm.packagedexcrafting.integration.appeng.blockentity.AECombinationCrafterBlockEntity;
import thelm.packagedexcrafting.inventory.CombinationCrafterItemHandler;
import thelm.packagedexcrafting.menu.CombinationCrafterMenu;
import thelm.packagedexcrafting.recipe.ICombinationPackageRecipeInfo;

public class CombinationCrafterBlockEntity extends BaseBlockEntity implements IPackageCraftingMachine {

	public static final BlockEntityType<CombinationCrafterBlockEntity> TYPE_INSTANCE = BlockEntityType.Builder.
			of(MiscHelper.INSTANCE.<BlockEntityType.BlockEntitySupplier<CombinationCrafterBlockEntity>>conditionalSupplier(
					()->ModList.get().isLoaded("ae2"),
					()->()->AECombinationCrafterBlockEntity::new, ()->()->CombinationCrafterBlockEntity::new).get(),
					CombinationCrafterBlock.INSTANCE).build(null);

	public static int energyCapacity = 5000000;
	public static boolean drawMEEnergy = false;

	public boolean isWorking = false;
	public long energyReq = 0;
	public long remainingProgress = 0;
	public int energyUsage = 0;
	public ICombinationPackageRecipeInfo currentRecipe;
	public List<BlockPos> pedestals = new ArrayList<>();

	public CombinationCrafterBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE_INSTANCE, pos, state);
		setItemHandler(new CombinationCrafterItemHandler(this));
		setEnergyStorage(new EnergyStorage(this, energyCapacity));
	}

	@Override
	protected Component getDefaultName() {
		return Component.translatable("block.packagedexcrafting.combination_crafter");
	}

	@Override
	public void tick() {
		if(!level.isClientSide) {
			if(isWorking) {
				tickProcess();
				if(remainingProgress <= 0) {
					energyStorage.receiveEnergy((int)Math.abs(remainingProgress), false);
					finishProcess();
					ejectItems();
				}
			}
			chargeEnergy();
			if(level.getGameTime() % 8 == 0) {
				ejectItems();
			}
			energyStorage.updateIfChanged();
		}
	}

	@Override
	public boolean acceptPackage(IPackageRecipeInfo recipeInfo, List<ItemStack> stacks, Direction direction) {
		if(!isBusy() && recipeInfo instanceof ICombinationPackageRecipeInfo recipe) {
			List<ItemStack> pedestalInputs = recipe.getPedestalInputs();
			List<BlockPos> emptyPedestals = getEmptyPedestals();
			if(emptyPedestals.size() >= pedestalInputs.size()) {
				pedestals.clear();
				pedestals.addAll(emptyPedestals.subList(0, pedestalInputs.size()));
				currentRecipe = recipe;
				isWorking = true;
				energyReq = remainingProgress = recipe.getEnergyRequired();
				energyUsage = recipe.getEnergyUsage();
				itemHandler.setStackInSlot(0, recipe.getCoreInput());
				for(int i = 0; i < pedestals.size(); ++i) {
					((MarkedPedestalBlockEntity)level.getBlockEntity(pedestals.get(i))).getItemHandler().
					setStackInSlot(0, pedestalInputs.get(i));
				}
				sync(false);
				setChanged();
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isBusy() {
		return isWorking || !itemHandler.getStacks().subList(0, 2).stream().allMatch(ItemStack::isEmpty);
	}

	protected void tickProcess() {
		if(pedestals.stream().map(level::getBlockEntity).anyMatch(be->!(be instanceof MarkedPedestalBlockEntity) || be.isRemoved())) {
			endProcess();
		}
		else {
			int energy = energyStorage.extractEnergy(energyUsage, false);
			remainingProgress -= energy;
			if(!level.isClientSide) {
				spawnParticles(ParticleTypes.ENTITY_EFFECT, worldPosition, 1.15, 2);
				if(shouldSpawnItemParticles()) {
					for(BlockPos pedestalPos : pedestals) {
						ItemStack stack = ((MarkedPedestalBlockEntity)level.getBlockEntity(pedestalPos)).getItemHandler().getStackInSlot(0);
						spawnItemParticles(pedestalPos, stack);
					}
				}
			}
		}
	}

	protected void finishProcess() {
		if(currentRecipe == null) {
			endProcess();
			return;
		}
		if(pedestals.stream().map(level::getBlockEntity).anyMatch(be->!(be instanceof MarkedPedestalBlockEntity) || be.isRemoved())) {
			endProcess();
			return;
		}
		for(BlockPos pedestalPos : pedestals) {
			((MarkedPedestalBlockEntity)level.getBlockEntity(pedestalPos)).getItemHandler().setStackInSlot(0, ItemStack.EMPTY);
			spawnParticles(ParticleTypes.SMOKE, pedestalPos, 1.1, 20);
		}
		itemHandler.setStackInSlot(0, ItemStack.EMPTY);
		spawnParticles(ParticleTypes.END_ROD, worldPosition, 1.1, 50);
		itemHandler.setStackInSlot(1, currentRecipe.getOutput());
		endProcess();
	}

	public void endProcess() {
		energyReq = 0;
		remainingProgress = 0;
		energyUsage = 0;
		pedestals.stream().map(level::getBlockEntity).
		filter(be->be instanceof MarkedPedestalBlockEntity && !be.isRemoved()).
		forEach(be->((MarkedPedestalBlockEntity)be).spawnItem());
		pedestals.clear();
		isWorking = false;
		currentRecipe = null;
		sync(false);
		setChanged();
	}

	protected List<BlockPos> getEmptyPedestals() {
		return BlockPos.betweenClosedStream(worldPosition.offset(-3, 0, -3), worldPosition.offset(3, 0, 3)).map(pos->{
			if(level.getBlockEntity(pos) instanceof MarkedPedestalBlockEntity pedestal && pedestal.getItemHandler().getStackInSlot(0).isEmpty()) {
				return pos.immutable();
			}
			return null;
		}).filter(Predicates.notNull()).collect(Collectors.toList());
	}

	protected void ejectItems() {
		int endIndex = isWorking ? 1 : 0;
		for(Direction direction : Direction.values()) {
			BlockEntity blockEntity = level.getBlockEntity(worldPosition.relative(direction));
			if(blockEntity != null && !(blockEntity instanceof UnpackagerBlockEntity) && blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, direction.getOpposite()).isPresent()) {
				IItemHandler itemHandler = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, direction.getOpposite()).resolve().get();
				boolean flag = true;
				for(int i = 1; i >= endIndex; --i) {
					ItemStack stack = this.itemHandler.getStackInSlot(i);
					if(stack.isEmpty()) {
						continue;
					}
					for(int slot = 0; slot < itemHandler.getSlots(); ++slot) {
						ItemStack stackRem = itemHandler.insertItem(slot, stack, false);
						if(stackRem.getCount() < stack.getCount()) {
							stack = stackRem;
							flag = false;
						}
						if(stack.isEmpty()) {
							break;
						}
					}
					this.itemHandler.setStackInSlot(i, stack);
					if(flag) {
						break;
					}
				}
			}
		}
	}

	protected void chargeEnergy() {
		int prevStored = energyStorage.getEnergyStored();
		ItemStack energyStack = itemHandler.getStackInSlot(2);
		if(energyStack.getCapability(ForgeCapabilities.ENERGY, null).isPresent()) {
			int energyRequest = Math.min(energyStorage.getMaxReceive(), energyStorage.getMaxEnergyStored() - energyStorage.getEnergyStored());
			energyStorage.receiveEnergy(energyStack.getCapability(ForgeCapabilities.ENERGY).resolve().get().extractEnergy(energyRequest, false), false);
			if(energyStack.getCount() <= 0) {
				itemHandler.setStackInSlot(2, ItemStack.EMPTY);
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

	protected void spawnItemParticles(BlockPos pedestalPos, ItemStack stack) {
		if(level == null || level.isClientSide()) {
			return;
		}
		ServerLevel level = (ServerLevel)this.level;
		double x = pedestalPos.getX() + level.getRandom().nextDouble()*0.2 + 0.4;
		double y = pedestalPos.getY() + level.getRandom().nextDouble()*0.2 + 1.4;
		double z = pedestalPos.getZ() + level.getRandom().nextDouble()*0.2 + 0.4;
		double velX = worldPosition.getX() - pedestalPos.getX();
		double velY = 0.25;
		double velZ = worldPosition.getZ() - pedestalPos.getZ();
		level.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, stack), x, y, z, 0, velX, velY, velZ, 0.18);
	}

	protected boolean shouldSpawnItemParticles() {
		return remainingProgress < energyUsage*40;
	}

	@Override
	public void setRemoved() {
		super.setRemoved();
		endProcess();
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		currentRecipe = null;
		if(nbt.contains("Recipe")) {
			CompoundTag tag = nbt.getCompound("Recipe");
			IPackageRecipeInfo recipe = MiscHelper.INSTANCE.loadRecipe(tag);
			if(recipe instanceof ICombinationPackageRecipeInfo combinationRecipe) {
				currentRecipe = combinationRecipe;
			}
			pedestals.clear();
			ListTag pedestalsTag = nbt.getList("Pedestals", 11);
			for(int i = 0; i < pedestalsTag.size(); ++i) {
				int[] posArray = pedestalsTag.getIntArray(i);
				BlockPos pos = new BlockPos(posArray[0], posArray[1], posArray[2]);
				pedestals.add(pos);
			}
		}
	}

	@Override
	public void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);
		if(currentRecipe != null) {
			CompoundTag tag = MiscHelper.INSTANCE.saveRecipe(new CompoundTag(), currentRecipe);
			nbt.put("Recipe", tag);
			ListTag pedestalsTag = new ListTag();
			pedestals.stream().map(pos->new int[] {pos.getX(), pos.getY(), pos.getZ()}).
			forEach(arr->pedestalsTag.add(new IntArrayTag(arr)));
			nbt.put("Pedestals", pedestalsTag);
		}
	}

	@Override
	public void loadSync(CompoundTag nbt) {
		super.loadSync(nbt);
		isWorking = nbt.getBoolean("Working");
		remainingProgress = nbt.getLong("Progress");
		energyReq = nbt.getLong("EnergyReq");
		energyUsage = nbt.getInt("EnergyUsage");
		itemHandler.load(nbt);
	}

	@Override
	public CompoundTag saveSync(CompoundTag nbt) {
		super.saveSync(nbt);
		nbt.putBoolean("Working", isWorking);
		nbt.putLong("Progress", remainingProgress);
		nbt.putLong("EnergyReq", energyReq);
		nbt.putInt("EnergyUsage", energyUsage);
		itemHandler.save(nbt);
		return nbt;
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
		return (int)(scale * (energyReq-remainingProgress) / energyReq);
	}

	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player) {
		sync(false);
		return new CombinationCrafterMenu(windowId, inventory, this);
	}
}
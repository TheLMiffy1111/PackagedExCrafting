package thelm.packagedexcrafting.tile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Predicates;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import thelm.packagedauto.api.IPackageCraftingMachine;
import thelm.packagedauto.api.IPackageRecipeInfo;
import thelm.packagedauto.energy.EnergyStorage;
import thelm.packagedauto.tile.BaseTile;
import thelm.packagedauto.tile.UnpackagerTile;
import thelm.packagedauto.util.MiscHelper;
import thelm.packagedexcrafting.block.CombinationCrafterBlock;
import thelm.packagedexcrafting.container.CombinationCrafterContainer;
import thelm.packagedexcrafting.integration.appeng.tile.AECombinationCrafterTile;
import thelm.packagedexcrafting.inventory.CombinationCrafterItemHandler;
import thelm.packagedexcrafting.recipe.ICombinationPackageRecipeInfo;

public class CombinationCrafterTile extends BaseTile implements ITickableTileEntity, IPackageCraftingMachine {

	public static final TileEntityType<CombinationCrafterTile> TYPE_INSTANCE = (TileEntityType<CombinationCrafterTile>)TileEntityType.Builder.
			of(MiscHelper.INSTANCE.conditionalSupplier(()->ModList.get().isLoaded("appliedenergistics2"),
					()->AECombinationCrafterTile::new, ()->CombinationCrafterTile::new), CombinationCrafterBlock.INSTANCE).
			build(null).setRegistryName("packagedexcrafting:combination_crafter");

	public static int energyCapacity = 5000000;
	public static boolean drawMEEnergy = false;

	public boolean isWorking = false;
	public long energyReq = 0;
	public long remainingProgress = 0;
	public int energyUsage = 0;
	public ICombinationPackageRecipeInfo currentRecipe;
	public List<BlockPos> pedestals = new ArrayList<>();

	public CombinationCrafterTile() {
		super(TYPE_INSTANCE);
		setItemHandler(new CombinationCrafterItemHandler(this));
		setEnergyStorage(new EnergyStorage(this, energyCapacity));
	}

	@Override
	protected ITextComponent getDefaultName() {
		return new TranslationTextComponent("block.packagedexcrafting.combination_crafter");
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
			energyStorage.updateIfChanged();
		}
	}

	@Override
	public boolean acceptPackage(IPackageRecipeInfo recipeInfo, List<ItemStack> stacks, Direction direction) {
		if(!isBusy() && recipeInfo instanceof ICombinationPackageRecipeInfo) {
			ICombinationPackageRecipeInfo recipe = (ICombinationPackageRecipeInfo)recipeInfo;
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
					((MarkedPedestalTile)level.getBlockEntity(pedestals.get(i))).getItemHandler().
					setStackInSlot(0, pedestalInputs.get(i).copy());
				}
				syncTile(false);
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
		if(pedestals.stream().map(level::getBlockEntity).anyMatch(tile->!(tile instanceof MarkedPedestalTile) || tile.isRemoved())) {
			endProcess();
		}
		else {
			int energy = energyStorage.extractEnergy((int)Math.min(energyUsage, remainingProgress), false);
			remainingProgress -= energy;
			if(!level.isClientSide) {
				spawnParticles(ParticleTypes.ENTITY_EFFECT, worldPosition, 1.15, 2);
				if(shouldSpawnItemParticles()) {
					for(BlockPos pedestalPos : pedestals) {
						ItemStack stack = ((MarkedPedestalTile)level.getBlockEntity(pedestalPos)).getItemHandler().getStackInSlot(0);
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
		if(pedestals.stream().map(level::getBlockEntity).anyMatch(tile->!(tile instanceof MarkedPedestalTile) || tile.isRemoved())) {
			endProcess();
			return;
		}
		for(BlockPos pedestalPos : pedestals) {
			ItemStackHandler pedestalInv = ((MarkedPedestalTile)level.getBlockEntity(pedestalPos)).getItemHandler();
			pedestalInv.setStackInSlot(0, MiscHelper.INSTANCE.getContainerItem(pedestalInv.getStackInSlot(0)));
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
		filter(tile->tile instanceof MarkedPedestalTile && !tile.isRemoved()).
		forEach(tile->((MarkedPedestalTile)tile).spawnItem());
		pedestals.clear();
		isWorking = false;
		currentRecipe = null;
		syncTile(false);
		setChanged();
	}

	protected List<BlockPos> getEmptyPedestals() {
		return BlockPos.betweenClosedStream(worldPosition.offset(-3, 0, -3), worldPosition.offset(3, 0, 3)).map(pos->{
			TileEntity tile = level.getBlockEntity(pos);
			if(tile instanceof MarkedPedestalTile) {
				if(((MarkedPedestalTile)tile).getItemHandler().getStackInSlot(0).isEmpty()) {
					return pos.immutable();
				}
			}
			return null;
		}).filter(Predicates.notNull()).collect(Collectors.toList());
	}

	protected void ejectItems() {
		int endIndex = isWorking ? 1 : 0;
		for(Direction direction : Direction.values()) {
			TileEntity tile = level.getBlockEntity(worldPosition.relative(direction));
			if(tile != null && !(tile instanceof UnpackagerTile) && tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, direction.getOpposite()).isPresent()) {
				IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, direction.getOpposite()).resolve().get();
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
		if(energyStack.getCapability(CapabilityEnergy.ENERGY, null).isPresent()) {
			int energyRequest = Math.min(energyStorage.getMaxReceive(), energyStorage.getMaxEnergyStored() - energyStorage.getEnergyStored());
			energyStorage.receiveEnergy(energyStack.getCapability(CapabilityEnergy.ENERGY).resolve().get().extractEnergy(energyRequest, false), false);
			if(energyStack.getCount() <= 0) {
				itemHandler.setStackInSlot(2, ItemStack.EMPTY);
			}
		}
	}

	protected <T extends IParticleData> void spawnParticles(T particle, BlockPos pos, double yOffset, int count) {
		if(level == null || level.isClientSide) {
			return;
		}
		ServerWorld world = (ServerWorld)level;
		double x = pos.getX()+0.5;
		double y = pos.getY()+yOffset;
		double z = pos.getZ()+0.5;
		world.sendParticles(particle, x, y, z, count, 0, 0, 0, 0.1);
	}

	protected void spawnItemParticles(BlockPos pedestalPos, ItemStack stack) {
		if(level == null || level.isClientSide) {
			return;
		}
		ServerWorld world = (ServerWorld)level;
		double x = pedestalPos.getX() + world.getRandom().nextDouble()*0.2 + 0.4;
		double y = pedestalPos.getY() + world.getRandom().nextDouble()*0.2 + 1.4;
		double z = pedestalPos.getZ() + world.getRandom().nextDouble()*0.2 + 0.4;
		double velX = worldPosition.getX() - pedestalPos.getX();
		double velY = 0.25;
		double velZ = worldPosition.getZ() - pedestalPos.getZ();
		world.sendParticles(new ItemParticleData(ParticleTypes.ITEM, stack), x, y, z, 0, velX, velY, velZ, 0.18);
	}

	protected boolean shouldSpawnItemParticles() {
		return remainingProgress < energyUsage*40;
	}

	@Override
	public void load(BlockState blockState, CompoundNBT nbt) {
		super.load(blockState, nbt);
		currentRecipe = null;
		if(nbt.contains("Recipe")) {
			CompoundNBT tag = nbt.getCompound("Recipe");
			IPackageRecipeInfo recipe = MiscHelper.INSTANCE.readRecipe(tag);
			if(recipe instanceof ICombinationPackageRecipeInfo) {
				currentRecipe = (ICombinationPackageRecipeInfo)recipe;
			}
			pedestals.clear();
			ListNBT pedestalsTag = nbt.getList("Pedestals", 11);
			for(int i = 0; i < pedestalsTag.size(); ++i) {
				int[] posArray = pedestalsTag.getIntArray(i);
				BlockPos pos = new BlockPos(posArray[0], posArray[1], posArray[2]);
				pedestals.add(pos);
			}
		}
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		super.save(nbt);
		if(currentRecipe != null) {
			CompoundNBT tag = MiscHelper.INSTANCE.writeRecipe(new CompoundNBT(), currentRecipe);
			nbt.put("Recipe", tag);
			ListNBT pedestalsTag = new ListNBT();
			pedestals.stream().map(pos->new int[] {pos.getX(), pos.getY(), pos.getZ()}).
			forEach(arr->pedestalsTag.add(new IntArrayNBT(arr)));
			nbt.put("Pedestals", pedestalsTag);
		}
		return nbt;
	}

	@Override
	public void readSync(CompoundNBT nbt) {
		super.readSync(nbt);
		isWorking = nbt.getBoolean("Working");
		remainingProgress = nbt.getLong("Progress");
		energyReq = nbt.getLong("EnergyReq");
		energyUsage = nbt.getInt("EnergyUsage");
		itemHandler.read(nbt);
	}

	@Override
	public CompoundNBT writeSync(CompoundNBT nbt) {
		super.writeSync(nbt);
		nbt.putBoolean("Working", isWorking);
		nbt.putLong("Progress", remainingProgress);
		nbt.putLong("EnergyReq", energyReq);
		nbt.putInt("EnergyUsage", energyUsage);
		itemHandler.write(nbt);
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
	public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player) {
		syncTile(false);
		return new CombinationCrafterContainer(windowId, playerInventory, this);
	}
}
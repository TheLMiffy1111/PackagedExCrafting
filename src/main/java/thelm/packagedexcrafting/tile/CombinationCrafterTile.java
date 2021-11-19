package thelm.packagedexcrafting.tile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import thelm.packagedexcrafting.inventory.MarkedPedestalItemHandler;
import thelm.packagedexcrafting.recipe.ICombinationPackageRecipeInfo;

public class CombinationCrafterTile extends BaseTile implements ITickableTileEntity, IPackageCraftingMachine {

	public static final TileEntityType<CombinationCrafterTile> TYPE_INSTANCE = (TileEntityType<CombinationCrafterTile>)TileEntityType.Builder.
			create(MiscHelper.INSTANCE.conditionalSupplier(()->ModList.get().isLoaded("appliedenergistics2"),
					()->AECombinationCrafterTile::new, ()->CombinationCrafterTile::new), CombinationCrafterBlock.INSTANCE).
			build(null).setRegistryName("packagedexcrafting:combination_crafter");

	public static int energyCapacity = 5000000;
	public static boolean drawMEEnergy = false;

	public boolean isWorking = false;
	public long energyReq = 0;
	public long remainingProgress = 0;
	public int energyUsage = 0;
	public ICombinationPackageRecipeInfo currentRecipe;
	public Map<BlockPos, MarkedPedestalTile> pedestals = new HashMap<>();

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
		if(!world.isRemote) {
			if(isWorking) {
				tickProcess();
				if(remainingProgress <= 0) {
					energyStorage.receiveEnergy((int)Math.abs(remainingProgress), false);
					finishProcess();
					ejectItems();
				}
			}
			chargeEnergy();
			if(world.getGameTime() % 8 == 0) {
				ejectItems();
			}
			energyStorage.updateIfChanged();
		}
	}

	@Override
	public boolean acceptPackage(IPackageRecipeInfo recipeInfo, List<ItemStack> stacks, Direction direction) {
		// TODO: validate stacks against recipe?
		if(!isBusy() && recipeInfo instanceof ICombinationPackageRecipeInfo) {
			ICombinationPackageRecipeInfo recipe = (ICombinationPackageRecipeInfo)recipeInfo;
			List<ItemStack> pedestalInputs = recipe.getPedestalInputs();
			Map<BlockPos, MarkedPedestalTile> emptyPedestals = getEmptyPedestals();

			if(emptyPedestals.size() >= pedestalInputs.size()) {
				pedestals.clear();
				emptyPedestals.entrySet().stream()
		        	.limit(pedestalInputs.size())
		        	.forEach(pair -> {
		        		if (pedestals.containsKey(pair.getKey())) {
		        			System.err.println("would replace tile " + pedestals.get(pair.getKey()) + " with " +
		        					pair.getValue() + " in the pedestals map!");
		        		} else {
		        			pedestals.put(pair.getKey(), pair.getValue());
		        		}
		        	});
				if (pedestals.size() != pedestalInputs.size()) {
					System.err.println("pedestals.size() is " + pedestals.size() + " but should be " + pedestalInputs.size());
					return false;
				}

				currentRecipe = recipe;
				isWorking = true;
				energyReq = remainingProgress = recipe.getEnergyRequired();
				energyUsage = recipe.getEnergyUsage();

				int i = 0;
				for(MarkedPedestalTile pedestalTile : pedestals.values()) {
					MarkedPedestalItemHandler pedestalHandler = (MarkedPedestalItemHandler) pedestalTile.getItemHandler();
					if (!pedestalHandler.getStackInSlot(0).isEmpty()) {
						System.err.println("pedestal already has item!");
						return false;
					}
					ItemStack itemStack = pedestalInputs.get(i);
					pedestalHandler.setStackInSlot(0, itemStack);
					i++;
				}

				itemHandler.setStackInSlot(0, recipe.getCoreInput());
				syncTile(false);
				markDirty();
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
		if (pedestals.values().stream().anyMatch(tile -> tile.isRemoved())) {
			endProcess();
		}
		else {
			int energy = energyStorage.extractEnergy(energyUsage, false);
			remainingProgress -= energy;
			if(!world.isRemote) {
				spawnParticles(ParticleTypes.ENTITY_EFFECT, pos, 1.15, 2);
				if(shouldSpawnItemParticles()) {
					for(Entry<BlockPos, MarkedPedestalTile> pair : pedestals.entrySet()) {
						ItemStack stack = pair.getValue().getItemHandler().getStackInSlot(0);
						spawnItemParticles(pair.getKey(), stack);
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
		if(pedestals.values().stream().anyMatch(tile->tile.isRemoved())) {
			endProcess();
			return;
		}
		for(Entry<BlockPos, MarkedPedestalTile> pair : pedestals.entrySet()) {
			pair.getValue().getItemHandler().setStackInSlot(0, ItemStack.EMPTY);
			spawnParticles(ParticleTypes.SMOKE, pair.getKey(), 1.1, 20);
		}
		itemHandler.setStackInSlot(0, ItemStack.EMPTY);
		spawnParticles(ParticleTypes.END_ROD, pos, 1.1D, 50);
		itemHandler.setStackInSlot(1, currentRecipe.getOutput());
		endProcess();
	}

	public void endProcess() {
		energyReq = 0;
		remainingProgress = 0;
		energyUsage = 0;
		pedestals.values().stream().
			filter(tile -> !tile.isRemoved()).
			forEach(MarkedPedestalTile::spawnItem);
		pedestals.clear();
		isWorking = false;
		currentRecipe = null;
		syncTile(false);
		markDirty();
	}

	protected Map<BlockPos, MarkedPedestalTile> getEmptyPedestals() {
		// TODO: populate the pedestals variable directly? Accept a number of pedestals to look for and only find that many?
		Map<BlockPos, MarkedPedestalTile> output = new HashMap<>();
		BlockPos.getAllInBox(pos.add(-3, 0, -3), pos.add(3, 0, 3)).forEach(p->{
			TileEntity tile = world.getTileEntity(p);
			if(tile instanceof MarkedPedestalTile) {
				MarkedPedestalTile mpTile = (MarkedPedestalTile)tile;
				if(mpTile.getItemHandler().getStackInSlot(0).isEmpty()) {
					output.put(new BlockPos(p), mpTile);
				}
			}
		});
		return output;
	}

	protected void ejectItems() {
		int endIndex = isWorking ? 1 : 0;
		for(Direction direction : Direction.values()) {
			TileEntity tile = world.getTileEntity(pos.offset(direction));
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
		if(world == null || world.isRemote()) {
			return;
		}
		ServerWorld world = (ServerWorld)this.world;
		double x = pos.getX()+0.5;
		double y = pos.getY()+yOffset;
		double z = pos.getZ()+0.5;
		world.spawnParticle(particle, x, y, z, count, 0, 0, 0, 0.1);
	}

	protected void spawnItemParticles(BlockPos pedestalPos, ItemStack stack) {
		if(world == null || world.isRemote()) {
			return;
		}
		ServerWorld world = (ServerWorld)this.world;
		double x = pedestalPos.getX() + world.getRandom().nextDouble()*0.2 + 0.4;
		double y = pedestalPos.getY() + world.getRandom().nextDouble()*0.2 + 1.4;
		double z = pedestalPos.getZ() + world.getRandom().nextDouble()*0.2 + 0.4;
		double velX = pos.getX() - pedestalPos.getX();
		double velY = 0.25;
		double velZ = pos.getZ() - pedestalPos.getZ();
		world.spawnParticle(new ItemParticleData(ParticleTypes.ITEM, stack), x, y, z, 0, velX, velY, velZ, 0.18);
	}

	protected boolean shouldSpawnItemParticles() {
		return remainingProgress < energyUsage*40;
	}

	@Override
	public void remove() {
		super.remove();
		endProcess();
	}

	@Override
	public void read(BlockState blockState, CompoundNBT nbt) {
		super.read(blockState, nbt);
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
				// TODO: is it dangerous to get tile entities here?
				TileEntity tile = world.getTileEntity(pos);
				if (tile instanceof MarkedPedestalTile) {
					pedestals.put(pos, (MarkedPedestalTile)tile);
				} else {
					if (tile != null) {
						System.err.println("pedestal tile entity " + i + " is " + tile.getClass().getCanonicalName() + "!");
					} else {
						System.err.println("Marked Pedestal tile entity " + i + " is null!");
					}
				}
			}
		}
	}

	@Override
	public CompoundNBT write(CompoundNBT nbt) {
		super.write(nbt);
		if(currentRecipe != null) {
			CompoundNBT tag = MiscHelper.INSTANCE.writeRecipe(new CompoundNBT(), currentRecipe);
			nbt.put("Recipe", tag);
			ListNBT pedestalsTag = new ListNBT();
			pedestals.keySet().stream().map(pos->new int[] {pos.getX(), pos.getY(), pos.getZ()}).
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
		if(remainingProgress <= 0 || energyReq == 0) {
			return 0;
		}
		return scale * (int)((energyReq-remainingProgress) / energyReq);
	}

	@Override
	public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player) {
		syncTile(false);
		return new CombinationCrafterContainer(windowId, playerInventory, this);
	}
}

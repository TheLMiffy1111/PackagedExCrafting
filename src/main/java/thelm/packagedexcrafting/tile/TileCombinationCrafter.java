package thelm.packagedexcrafting.tile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Streams;

import appeng.api.networking.IGridHost;
import appeng.api.networking.IGridNode;
import appeng.api.networking.security.IActionHost;
import appeng.api.util.AECableType;
import appeng.api.util.AEPartLocation;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.WorldServer;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import thelm.packagedauto.api.IPackageCraftingMachine;
import thelm.packagedauto.api.IRecipeInfo;
import thelm.packagedauto.api.MiscUtil;
import thelm.packagedauto.energy.EnergyStorage;
import thelm.packagedauto.tile.TileBase;
import thelm.packagedauto.tile.TileUnpackager;
import thelm.packagedexcrafting.client.gui.GuiCombinationCrafter;
import thelm.packagedexcrafting.container.ContainerCombinationCrafter;
import thelm.packagedexcrafting.integration.appeng.networking.HostHelperTileCombinationCrafter;
import thelm.packagedexcrafting.inventory.InventoryCombinationCrafter;
import thelm.packagedexcrafting.recipe.IRecipeInfoCombination;

@Optional.InterfaceList({
	@Optional.Interface(iface="appeng.api.networking.IGridHost", modid="appliedenergistics2"),
	@Optional.Interface(iface="appeng.api.networking.security.IActionHost", modid="appliedenergistics2"),
})
public class TileCombinationCrafter extends TileBase implements ITickable, IPackageCraftingMachine, IGridHost, IActionHost {

	public static boolean enabled = true;

	public static int energyCapacity = 5000000;
	public static boolean drawMEEnergy = false;

	public int requiredPedestals = 0;
	public boolean isWorking = false;
	public long energyReq = 0;
	public long remainingProgress = 0;
	public int energyUsage = 0;
	public IRecipeInfoCombination currentRecipe;
	public List<BlockPos> pedestals = new ArrayList<>();

	public TileCombinationCrafter() {
		setInventory(new InventoryCombinationCrafter(this));
		setEnergyStorage(new EnergyStorage(this, energyCapacity));
		if(Loader.isModLoaded("appliedenergistics2")) {
			hostHelper = new HostHelperTileCombinationCrafter(this);
		}
	}

	@Override
	protected String getLocalizedName() {
		return I18n.translateToLocal("tile.packagedexcrafting.combination_crafter.name");
	}

	public ITextComponent getMessage() {
		if(isWorking) {
			return null;
		}
		int usablePedestals = getEmptyPedestals().size();
		ITextComponent message = new TextComponentTranslation("tile.packagedexcrafting.combination_crafter.pedestals.usable", usablePedestals);
		if(requiredPedestals > 0) {
			message.appendText("\n");
			message.appendSibling(new TextComponentTranslation("tile.packagedexcrafting.combination_crafter.pedestals.required", requiredPedestals));
		}
		return message;
	}

	@Override
	public void update() {
		if(!world.isRemote) {
			if(isWorking) {
				tickProcess();
				if(remainingProgress <= 0) {
					finishProcess();
					if(hostHelper != null && hostHelper.isActive()) {
						hostHelper.ejectItem();
					}
					else {
						ejectItems();
					}
				}
			}
			chargeEnergy();
			if(world.getTotalWorldTime() % 8 == 0) {
				if(hostHelper != null && hostHelper.isActive()) {
					hostHelper.ejectItem();
					if(drawMEEnergy) {
						hostHelper.chargeEnergy();
					}
				}
				else {
					ejectItems();
				}
			}
		}
	}

	@Override
	public boolean acceptPackage(IRecipeInfo recipeInfo, List<ItemStack> stacks, EnumFacing facing) {
		if(!isBusy() && recipeInfo instanceof IRecipeInfoCombination) {
			IRecipeInfoCombination recipe = (IRecipeInfoCombination)recipeInfo;
			List<ItemStack> pedestalInputs = recipe.getPedestalInputs();
			List<BlockPos> emptyPedestals = getEmptyPedestals();
			requiredPedestals = Math.max(requiredPedestals, pedestalInputs.size());
			if(emptyPedestals.size() >= pedestalInputs.size()) {
				pedestals.clear();
				pedestals.addAll(emptyPedestals.subList(0, pedestalInputs.size()));
				currentRecipe = recipe;
				isWorking = true;
				energyReq = remainingProgress = recipe.getEnergyRequired();
				energyUsage = recipe.getEnergyUsage();
				inventory.setInventorySlotContents(0, recipe.getCoreInput());
				for(int i = 0; i < pedestals.size(); ++i) {
					((TileMarkedPedestal)world.getTileEntity(pedestals.get(i))).getInventory().
					setInventorySlotContents(0, pedestalInputs.get(i).copy());
				}
				syncTile(false);
				markDirty();
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isBusy() {
		return isWorking || !inventory.stacks.subList(0, 2).stream().allMatch(ItemStack::isEmpty);
	}

	protected void tickProcess() {
		if(pedestals.stream().map(world::getTileEntity).
				anyMatch(tile->!(tile instanceof TileMarkedPedestal) || tile.isInvalid())) {
			endProcess();
		}
		else {
			int energy = energyStorage.extractEnergy((int)Math.min(energyUsage, remainingProgress), false);
			remainingProgress -= energy;
			if(!world.isRemote) {
				((WorldServer)world).spawnParticle(EnumParticleTypes.SPELL, false,
						pos.getX()+0.5, pos.getY()+1.1, pos.getZ()+0.5, 2, 0, 0, 0, 0.1);
			}
		}
	}

	protected void finishProcess() {
		if(currentRecipe == null) {
			endProcess();
			return;
		}
		if(pedestals.stream().map(world::getTileEntity).
				anyMatch(tile->!(tile instanceof TileMarkedPedestal) || tile.isInvalid())) {
			endProcess();
			return;
		}
		for(BlockPos pedestalPos : pedestals) {
			IInventory pedestalInv = ((TileMarkedPedestal)world.getTileEntity(pedestalPos)).getInventory();
			pedestalInv.setInventorySlotContents(0, MiscUtil.getContainerItem(pedestalInv.getStackInSlot(0)));
			((WorldServer)world).spawnParticle(EnumParticleTypes.SMOKE_NORMAL, false,
					pedestalPos.getX()+0.5, pedestalPos.getY()+1.1, pedestalPos.getZ()+0.5, 20, 0, 0, 0, 0.1);
		}
		inventory.setInventorySlotContents(0, ItemStack.EMPTY);
		((WorldServer)world).spawnParticle(EnumParticleTypes.END_ROD, false,
				pos.getX()+0.5, pos.getY()+1.1, pos.getZ()+0.5, 50, 0, 0, 0, 0.1);
		inventory.setInventorySlotContents(1, currentRecipe.getOutput());
		endProcess();
	}

	public void endProcess() {
		energyReq = 0;
		remainingProgress = 0;
		energyUsage = 0;
		pedestals.stream().map(world::getTileEntity).
		filter(tile->tile instanceof TileMarkedPedestal && !tile.isInvalid()).
		forEach(tile->((TileMarkedPedestal)tile).spawnItem());
		pedestals.clear();
		isWorking = false;
		currentRecipe = null;
		syncTile(false);
		markDirty();
	}

	protected List<BlockPos> getEmptyPedestals() {
		return Streams.stream(BlockPos.getAllInBox(pos.add(-3, 0, -3), pos.add(3, 0, 3))).filter(pos->{
			TileEntity tile = world.getTileEntity(pos);
			if(tile instanceof TileMarkedPedestal) {
				return ((TileMarkedPedestal)tile).getInventory().isEmpty();
			}
			return false;
		}).collect(Collectors.toList());
	}

	protected void ejectItems() {
		int endIndex = isWorking ? 1 : 0;
		for(EnumFacing facing : EnumFacing.VALUES) {
			TileEntity tile = world.getTileEntity(pos.offset(facing));
			if(tile != null && !(tile instanceof TileUnpackager) && tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite())) {
				IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite());
				boolean flag = true;
				for(int i = 1; i >= endIndex; --i) {
					ItemStack stack = inventory.getStackInSlot(i);
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
					inventory.setInventorySlotContents(i, stack);
					if(flag) {
						break;
					}
				}
			}
		}
	}

	protected void chargeEnergy() {
		int prevStored = energyStorage.getEnergyStored();
		ItemStack energyStack = inventory.getStackInSlot(2);
		if(energyStack.hasCapability(CapabilityEnergy.ENERGY, null)) {
			int energyRequest = Math.min(energyStorage.getMaxReceive(), energyStorage.getMaxEnergyStored() - energyStorage.getEnergyStored());
			energyStorage.receiveEnergy(energyStack.getCapability(CapabilityEnergy.ENERGY, null).extractEnergy(energyRequest, false), false);
			if(energyStack.getCount() <= 0) {
				inventory.setInventorySlotContents(2, ItemStack.EMPTY);
			}
		}
	}

	@Override
	public int getComparatorSignal() {
		if(isWorking) {
			return 1;
		}
		if(!inventory.stacks.subList(0, 2).stream().allMatch(ItemStack::isEmpty)) {
			return 15;
		}
		return 0;
	}

	public HostHelperTileCombinationCrafter hostHelper;

	@Override
	public void invalidate() {
		super.invalidate();
		if(hostHelper != null) {
			hostHelper.invalidate();
		}
	}

	@Override
	public void onChunkUnload() {
		super.onChunkUnload();
		if(hostHelper != null) {
			hostHelper.invalidate();
		}
	}

	@Optional.Method(modid="appliedenergistics2")
	@Override
	public IGridNode getGridNode(AEPartLocation dir) {
		return getActionableNode();
	}

	@Optional.Method(modid="appliedenergistics2")
	@Override
	public AECableType getCableConnectionType(AEPartLocation dir) {
		return AECableType.SMART;
	}

	@Optional.Method(modid="appliedenergistics2")
	@Override
	public void securityBreak() {
		world.destroyBlock(pos, true);
	}

	@Optional.Method(modid="appliedenergistics2")
	@Override
	public IGridNode getActionableNode() {
		return hostHelper.getNode();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		currentRecipe = null;
		if(nbt.hasKey("Recipe")) {
			NBTTagCompound tag = nbt.getCompoundTag("Recipe");
			IRecipeInfo recipe = MiscUtil.readRecipeFromNBT(tag);
			if(recipe instanceof IRecipeInfoCombination) {
				currentRecipe = (IRecipeInfoCombination)recipe;
			}
			pedestals.clear();
			NBTTagList pedestalsTag = nbt.getTagList("Pedestals", 11);
			for(int i = 0; i < pedestalsTag.tagCount(); ++i) {
				int[] posArray = pedestalsTag.getIntArrayAt(i);
				BlockPos pos = new BlockPos(posArray[0], posArray[1], posArray[2]);
				pedestals.add(pos);
			}
		}
		if(hostHelper != null) {
			hostHelper.readFromNBT(nbt);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		if(currentRecipe != null) {
			NBTTagCompound tag = MiscUtil.writeRecipeToNBT(new NBTTagCompound(), currentRecipe);
			nbt.setTag("Recipe", tag);
			NBTTagList pedestalsTag = new NBTTagList();
			pedestals.stream().map(pos->new int[] {pos.getX(), pos.getY(), pos.getZ()}).
			forEach(arr->pedestalsTag.appendTag(new NBTTagIntArray(arr)));
			nbt.setTag("Pedestals", pedestalsTag);
		}
		if(hostHelper != null) {
			hostHelper.writeToNBT(nbt);
		}
		return nbt;
	}

	@Override
	public void readSyncNBT(NBTTagCompound nbt) {
		super.readSyncNBT(nbt);
		isWorking = nbt.getBoolean("Working");
		remainingProgress = nbt.getLong("Progress");
		energyReq = nbt.getLong("EnergyReq");
		energyUsage = nbt.getInteger("EnergyUsage");
		inventory.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeSyncNBT(NBTTagCompound nbt) {
		super.writeSyncNBT(nbt);
		nbt.setBoolean("Working", isWorking);
		nbt.setLong("Progress", remainingProgress);
		nbt.setLong("EnergyReq", energyReq);
		nbt.setInteger("EnergyUsage", energyUsage);
		inventory.writeToNBT(nbt);
		return nbt;
	}

	public int getScaledEnergy(int scale) {
		if(energyStorage.getMaxEnergyStored() <= 0) {
			return 0;
		}
		return scale * energyStorage.getEnergyStored() / energyStorage.getMaxEnergyStored();
	}

	public int getScaledProgress(int scale) {
		if(remainingProgress <= 0 || energyReq == 0) {
			return 0;
		}
		return (int)(scale * (energyReq-remainingProgress) / energyReq);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public GuiContainer getClientGuiElement(EntityPlayer player, Object... args) {
		return new GuiCombinationCrafter(new ContainerCombinationCrafter(player.inventory, this));
	}

	@Override
	public Container getServerGuiElement(EntityPlayer player, Object... args) {
		return new ContainerCombinationCrafter(player.inventory, this);
	}
}

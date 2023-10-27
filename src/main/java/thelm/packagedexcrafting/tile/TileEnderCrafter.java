package thelm.packagedexcrafting.tile;

import java.util.List;

import com.blakebr0.extendedcrafting.block.BlockEnderAlternator;
import com.google.common.collect.Streams;

import appeng.api.networking.IGridHost;
import appeng.api.networking.IGridNode;
import appeng.api.networking.security.IActionHost;
import appeng.api.util.AECableType;
import appeng.api.util.AEPartLocation;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import thelm.packagedauto.api.IPackageCraftingMachine;
import thelm.packagedauto.api.IRecipeInfo;
import thelm.packagedauto.api.MiscUtil;
import thelm.packagedauto.energy.EnergyStorage;
import thelm.packagedauto.tile.TileBase;
import thelm.packagedauto.tile.TileUnpackager;
import thelm.packagedexcrafting.client.gui.GuiEnderCrafter;
import thelm.packagedexcrafting.container.ContainerEnderCrafter;
import thelm.packagedexcrafting.integration.appeng.networking.HostHelperTileEnderCrafter;
import thelm.packagedexcrafting.inventory.InventoryEnderCrafter;
import thelm.packagedexcrafting.recipe.IRecipeInfoEnder;

@Optional.InterfaceList({
	@Optional.Interface(iface="appeng.api.networking.IGridHost", modid="appliedenergistics2"),
	@Optional.Interface(iface="appeng.api.networking.security.IActionHost", modid="appliedenergistics2"),
})
public class TileEnderCrafter extends TileBase implements ITickable, IPackageCraftingMachine, IGridHost, IActionHost {

	public static boolean enabled = true;

	public static int energyCapacity = 5000;
	public static int progressReq = 600;
	public static double alternatorEff = 0.02;
	public static int energyReq = 500;
	public static int energyUsage = 100;
	public static boolean drawMEEnergy = true;

	public boolean isWorking = false;
	public int progress = 0;
	public int actualProgressReq = 0;
	public int remainingProgress = 0;
	public IRecipeInfoEnder currentRecipe;

	public TileEnderCrafter() {
		setInventory(new InventoryEnderCrafter(this));
		setEnergyStorage(new EnergyStorage(this, energyCapacity));
		if(Loader.isModLoaded("appliedenergistics2")) {
			hostHelper = new HostHelperTileEnderCrafter(this);
		}
	}

	@Override
	protected String getLocalizedName() {
		return I18n.translateToLocal("tile.packagedexcrafting.ender_crafter.name");
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
		if(!isBusy() && recipeInfo instanceof IRecipeInfoEnder) {
			IRecipeInfoEnder recipe = (IRecipeInfoEnder)recipeInfo;
			ItemStack slotStack = inventory.getStackInSlot(9);
			ItemStack outputStack = recipe.getOutput();
			if(slotStack.isEmpty() || slotStack.getItem() == outputStack.getItem() && slotStack.getItemDamage() == outputStack.getItemDamage() && ItemStack.areItemStackShareTagsEqual(slotStack, outputStack) && slotStack.getCount()+outputStack.getCount() <= outputStack.getMaxStackSize()) {
				currentRecipe = recipe;
				isWorking = true;
				remainingProgress = energyReq;
				for(int i = 0; i < 9; ++i) {
					inventory.setInventorySlotContents(i, recipe.getMatrix().getStackInSlot(i).copy());
				}
				markDirty();
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isBusy() {
		return isWorking || !inventory.stacks.subList(0, 9).stream().allMatch(ItemStack::isEmpty);
	}

	protected void tickProcess() {
		int alternatorCount = getAlternatorCount();
		if(alternatorCount > 0) {
			progress++;
			actualProgressReq = (int)Math.max(progressReq*(1-alternatorEff*alternatorCount), 0);
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
		if(inventory.getStackInSlot(9).isEmpty()) {
			inventory.setInventorySlotContents(9, currentRecipe.getOutput());
		}
		else {
			inventory.getStackInSlot(9).grow(currentRecipe.getOutput().getCount());
		}
		for(int i = 0; i < 9; ++i) {
			inventory.setInventorySlotContents(i, MiscUtil.getContainerItem(inventory.getStackInSlot(i)));
		}
		endProcess();
	}

	public void endProcess() {
		progress = 0;
		remainingProgress = 0;
		isWorking = false;
		currentRecipe = null;
		markDirty();
	}

	protected int getAlternatorCount() {
		return (int)Streams.stream(BlockPos.getAllInBox(pos.add(-3, -3, -3), pos.add(3, 3, 3))).
				map(pos->world.getBlockState(pos).getBlock()).
				filter(block->block instanceof BlockEnderAlternator).count();
	}

	protected void ejectItems() {
		int endIndex = isWorking ? 9 : 0;
		for(EnumFacing facing : EnumFacing.VALUES) {
			TileEntity tile = world.getTileEntity(pos.offset(facing));
			if(tile != null && !(tile instanceof TileUnpackager) && tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite())) {
				IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite());
				for(int i = 9; i >= endIndex; --i) {
					ItemStack stack = inventory.getStackInSlot(i);
					if(stack.isEmpty()) {
						continue;
					}
					ItemStack stackRem = ItemHandlerHelper.insertItem(itemHandler, stack, false);
					inventory.setInventorySlotContents(i, stackRem);
				}
			}
		}
	}

	protected void chargeEnergy() {
		ItemStack energyStack = inventory.getStackInSlot(10);
		if(energyStack.hasCapability(CapabilityEnergy.ENERGY, null)) {
			int energyRequest = Math.min(energyStorage.getMaxReceive(), energyStorage.getMaxEnergyStored() - energyStorage.getEnergyStored());
			energyStorage.receiveEnergy(energyStack.getCapability(CapabilityEnergy.ENERGY, null).extractEnergy(energyRequest, false), false);
			if(energyStack.getCount() <= 0) {
				inventory.setInventorySlotContents(10, ItemStack.EMPTY);
			}
		}
	}

	@Override
	public int getComparatorSignal() {
		if(isWorking) {
			return 1;
		}
		if(!inventory.stacks.subList(0, 10).stream().allMatch(ItemStack::isEmpty)) {
			return 15;
		}
		return 0;
	}

	public HostHelperTileEnderCrafter hostHelper;

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
		isWorking = nbt.getBoolean("Working");
		progress = nbt.getInteger("Progress");
		remainingProgress = nbt.getInteger("EnergyProgress");
		currentRecipe = null;
		if(nbt.hasKey("Recipe")) {
			NBTTagCompound tag = nbt.getCompoundTag("Recipe");
			IRecipeInfo recipe = MiscUtil.readRecipeFromNBT(tag);
			if(recipe instanceof IRecipeInfoEnder) {
				currentRecipe = (IRecipeInfoEnder)recipe;
			}
		}
		if(hostHelper != null) {
			hostHelper.readFromNBT(nbt);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setBoolean("Working", isWorking);
		nbt.setInteger("Progress", progress);
		nbt.setInteger("EnergyProgress", remainingProgress);
		if(currentRecipe != null) {
			NBTTagCompound tag = MiscUtil.writeRecipeToNBT(new NBTTagCompound(), currentRecipe);
			nbt.setTag("Recipe", tag);
		}
		if(hostHelper != null) {
			hostHelper.writeToNBT(nbt);
		}
		return nbt;
	}

	public int getScaledEnergy(int scale) {
		if(energyStorage.getMaxEnergyStored() <= 0) {
			return 0;
		}
		return scale * energyStorage.getEnergyStored() / energyStorage.getMaxEnergyStored();
	}

	public int getScaledProgress(int scale) {
		if(progress <= 0) {
			return 0;
		}
		return scale * progress / actualProgressReq;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public GuiContainer getClientGuiElement(EntityPlayer player, Object... args) {
		return new GuiEnderCrafter(new ContainerEnderCrafter(player.inventory, this));
	}

	@Override
	public Container getServerGuiElement(EntityPlayer player, Object... args) {
		return new ContainerEnderCrafter(player.inventory, this);
	}
}

package thelm.packagedexcrafting.integration.appeng.networking;

import appeng.api.AEApi;
import appeng.api.config.Actionable;
import appeng.api.config.PowerMultiplier;
import appeng.api.config.PowerUnits;
import appeng.api.networking.GridFlags;
import appeng.api.networking.IGrid;
import appeng.api.networking.energy.IEnergyGrid;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.channels.IItemStorageChannel;
import appeng.api.storage.data.IAEItemStack;
import net.minecraft.item.ItemStack;
import thelm.packagedauto.integration.appeng.networking.HostHelperTile;
import thelm.packagedexcrafting.tile.TileAdvancedCrafter;

public class HostHelperTileAdvancedCrafter extends HostHelperTile<TileAdvancedCrafter> {

	public HostHelperTileAdvancedCrafter(TileAdvancedCrafter tile) {
		super(tile);
		gridBlock.flags.remove(GridFlags.REQUIRE_CHANNEL);
	}

	public void ejectItem() {
		if(isActive()) {
			IGrid grid = getNode().getGrid();
			IStorageGrid storageGrid = grid.getCache(IStorageGrid.class);
			IItemStorageChannel storageChannel = AEApi.instance().storage().getStorageChannel(IItemStorageChannel.class);
			IMEMonitor<IAEItemStack> inventory = storageGrid.getInventory(storageChannel);
			int endIndex = tile.isWorking ? 25 : 0;
			for(int i = 25; i >= endIndex; --i) {
				ItemStack is = tile.getInventory().getStackInSlot(i);
				if(is.isEmpty()) {
					continue;
				}
				IAEItemStack stack = storageChannel.createStack(is);
				IAEItemStack rem = inventory.injectItems(stack, Actionable.SIMULATE, source);
				if(rem == null || rem.getStackSize() == 0) {
					inventory.injectItems(stack, Actionable.MODULATE, source);
					tile.getInventory().setInventorySlotContents(i, ItemStack.EMPTY);
				}
				else if(rem.getStackSize() < stack.getStackSize()) {
					tile.getInventory().setInventorySlotContents(i, inventory.injectItems(stack, Actionable.MODULATE, source).createItemStack());
				}
			}
		}
	}

	public void chargeEnergy() {
		if(isActive()) {
			IGrid grid = getNode().getGrid();
			IEnergyGrid energyGrid = grid.getCache(IEnergyGrid.class);
			double conversion = PowerUnits.RF.convertTo(PowerUnits.AE, 1);
			int request = Math.min(tile.getEnergyStorage().getMaxReceive(), tile.getEnergyStorage().getMaxEnergyStored()-tile.getEnergyStorage().getEnergyStored());
			double available = energyGrid.extractAEPower((request+0.5)*conversion, Actionable.SIMULATE, PowerMultiplier.CONFIG);
			int extract = (int)(available/conversion);
			energyGrid.extractAEPower(extract*conversion, Actionable.MODULATE, PowerMultiplier.CONFIG);
			tile.getEnergyStorage().receiveEnergy(extract, false);
		}
	}
}

package thelm.packagedexcrafting.integration.appeng.networking;

import appeng.api.AEApi;
import appeng.api.config.Actionable;
import appeng.api.networking.GridFlags;
import appeng.api.networking.IGrid;
import appeng.api.networking.energy.IEnergyGrid;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.channels.IItemStorageChannel;
import appeng.api.storage.data.IAEItemStack;
import net.minecraft.item.ItemStack;
import thelm.packagedauto.integration.appeng.networking.HostHelperTile;
import thelm.packagedexcrafting.tile.TileMarkedPedestal;

public class HostHelperTileMarkedPedestal extends HostHelperTile<TileMarkedPedestal> {

	public HostHelperTileMarkedPedestal(TileMarkedPedestal tile) {
		super(tile);
		gridBlock.flags.remove(GridFlags.REQUIRE_CHANNEL);
	}

	public void ejectItem() {
		if(isActive()) {
			IGrid grid = getNode().getGrid();
			IStorageGrid storageGrid = grid.getCache(IStorageGrid.class);
			IEnergyGrid energyGrid = grid.getCache(IEnergyGrid.class);
			IItemStorageChannel storageChannel = AEApi.instance().storage().getStorageChannel(IItemStorageChannel.class);
			IMEMonitor<IAEItemStack> inventory = storageGrid.getInventory(storageChannel);
			ItemStack is = tile.getInventory().getStackInSlot(0);
			if(!is.isEmpty()) {
				IAEItemStack stack = storageChannel.createStack(is);
				IAEItemStack rem = AEApi.instance().storage().poweredInsert(energyGrid, inventory, stack, source, Actionable.MODULATE);
				if(rem == null || rem.getStackSize() == 0) {
					tile.getInventory().setInventorySlotContents(0, ItemStack.EMPTY);
				}
				else if(rem.getStackSize() < stack.getStackSize()) {
					tile.getInventory().setInventorySlotContents(0, rem.createItemStack());
				}
			}
		}
	}
}

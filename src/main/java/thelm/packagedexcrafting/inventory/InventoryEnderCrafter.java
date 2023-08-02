package thelm.packagedexcrafting.inventory;

import java.util.stream.IntStream;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.CapabilityEnergy;
import thelm.packagedauto.inventory.InventoryTileBase;
import thelm.packagedexcrafting.tile.TileEnderCrafter;

public class InventoryEnderCrafter extends InventoryTileBase {

	public final TileEnderCrafter tile;

	public InventoryEnderCrafter(TileEnderCrafter tile) {
		super(tile, 11);
		this.tile = tile;
		slots = IntStream.rangeClosed(0, 9).toArray();
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if(index == 10) {
			return stack.hasCapability(CapabilityEnergy.ENERGY, null);
		}
		return false;
	}

	@Override
	public int getField(int id) {
		switch(id) {
		case 0: return tile.progress;
		case 1: return tile.actualProgressReq;
		case 2: return tile.isWorking ? 1 : 0;
		case 3: return tile.getEnergyStorage().getEnergyStored();
		default: return 0;
		}
	}

	@Override
	public void setField(int id, int value) {
		switch(id) {
		case 0:
			tile.progress = value;
			break;
		case 1:
			tile.actualProgressReq = value;
			break;
		case 2:
			tile.isWorking = value != 0;
			break;
		case 3:
			tile.getEnergyStorage().setEnergyStored(value);
			break;
		}
	}

	@Override
	public int getFieldCount() {
		return 4;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return tile.isWorking ? index == 9 : index != 10;
	}
}

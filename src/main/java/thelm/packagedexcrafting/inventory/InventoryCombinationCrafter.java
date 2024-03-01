package thelm.packagedexcrafting.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.CapabilityEnergy;
import thelm.packagedauto.inventory.InventoryTileBase;
import thelm.packagedexcrafting.tile.TileCombinationCrafter;

public class InventoryCombinationCrafter extends InventoryTileBase {

	public final TileCombinationCrafter tile;

	public InventoryCombinationCrafter(TileCombinationCrafter tile) {
		super(tile, 3);
		this.tile = tile;
		slots = new int[] {0, 1};
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if(index == 2) {
			return stack.hasCapability(CapabilityEnergy.ENERGY, null);
		}
		return false;
	}

	@Override
	public int getField(int id) {
		switch(id) {
		case 0: return (int)(tile.energyReq & 0xFFFFFFFFL);
		case 1: return (int)(tile.remainingProgress & 0xFFFFFFFFL);
		case 2: return (int)(tile.energyReq >>> 32);
		case 3: return (int)(tile.remainingProgress >>> 32);
		case 4: return tile.isWorking ? 1 : 0;
		case 5: return tile.getEnergyStorage().getEnergyStored();
		default: return 0;
		}
	}

	@Override
	public void setField(int id, int value) {
		switch(id) {
		case 0:
			tile.energyReq = (tile.energyReq | 0xFFFFFFFFL) & value;
			break;
		case 1:
			tile.remainingProgress = (tile.remainingProgress | 0xFFFFFFFFL) & value;
			break;
		case 2:
			tile.energyReq = (tile.energyReq | 0xFFFFFFFF00000000L) & ((long)value << 32 | 0xFFFFFFFFL);
			break;
		case 3:
			tile.remainingProgress = (tile.remainingProgress | 0xFFFFFFFF00000000L) & ((long)value << 32 | 0xFFFFFFFFL);
			break;
		case 4:
			tile.isWorking = value != 0;
			break;
		case 5:
			tile.getEnergyStorage().setEnergyStored(value);
			break;
		}
	}

	@Override
	public int getFieldCount() {
		return 6;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return tile.isWorking ? index == 1 : index != 2;
	}
}

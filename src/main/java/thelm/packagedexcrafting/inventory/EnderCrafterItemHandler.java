package thelm.packagedexcrafting.inventory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;
import thelm.packagedauto.inventory.BaseItemHandler;
import thelm.packagedexcrafting.tile.EnderCrafterTile;

public class EnderCrafterItemHandler extends BaseItemHandler<EnderCrafterTile> {

	public EnderCrafterItemHandler(EnderCrafterTile tile) {
		super(tile, 11);
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		if(slot == 10) {
			return stack.getCapability(CapabilityEnergy.ENERGY).isPresent();
		}
		return false;
	}

	@Override
	public int get(int id) {
		switch(id) {
		case 0: return tile.progress;
		case 1: return tile.actualProgressReq;
		case 2: return tile.isWorking ? 1 : 0;
		default: return 0;
		}
	}

	@Override
	public void set(int id, int value) {
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
		}
	}

	@Override
	public int size() {
		return 3;
	}
}

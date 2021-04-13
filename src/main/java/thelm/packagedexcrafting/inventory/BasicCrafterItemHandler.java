package thelm.packagedexcrafting.inventory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;
import thelm.packagedauto.inventory.BaseItemHandler;
import thelm.packagedexcrafting.tile.BasicCrafterTile;

public class BasicCrafterItemHandler extends BaseItemHandler<BasicCrafterTile> {

	public BasicCrafterItemHandler(BasicCrafterTile tile) {
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
		case 0: return tile.remainingProgress;
		case 1: return tile.isWorking ? 1 : 0;
		default: return 0;
		}
	}

	@Override
	public void set(int id, int value) {
		switch(id) {
		case 0:
			tile.remainingProgress = value;
			break;
		case 1:
			tile.isWorking = value != 0;
			break;
		}
	}

	@Override
	public int size() {
		return 2;
	}
}

package thelm.packagedexcrafting.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.IItemHandlerModifiable;
import thelm.packagedauto.inventory.BaseItemHandler;
import thelm.packagedexcrafting.tile.AdvancedCrafterTile;

public class AdvancedCrafterItemHandler extends BaseItemHandler<AdvancedCrafterTile> {

	public AdvancedCrafterItemHandler(AdvancedCrafterTile tile) {
		super(tile, 27);
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		if(slot == 26) {
			return stack.getCapability(CapabilityEnergy.ENERGY).isPresent();
		}
		return false;
	}

	@Override
	public IItemHandlerModifiable getWrapperForDirection(Direction side) {
		return wrapperMap.computeIfAbsent(side, s->new AdvancedCrafterItemHandlerWrapper(this, s));
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

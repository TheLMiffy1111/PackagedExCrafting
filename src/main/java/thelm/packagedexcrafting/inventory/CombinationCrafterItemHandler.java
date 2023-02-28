package thelm.packagedexcrafting.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.IItemHandlerModifiable;
import thelm.packagedauto.inventory.BaseItemHandler;
import thelm.packagedexcrafting.tile.CombinationCrafterTile;

public class CombinationCrafterItemHandler extends BaseItemHandler<CombinationCrafterTile> {

	public CombinationCrafterItemHandler(CombinationCrafterTile tile) {
		super(tile, 3);
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		if(slot == 2) {
			return stack.getCapability(CapabilityEnergy.ENERGY).isPresent();
		}
		return false;
	}

	@Override
	public IItemHandlerModifiable getWrapperForDirection(Direction side) {
		return wrapperMap.computeIfAbsent(side, s->new CombinationCrafterItemHandlerWrapper(this, s));
	}

	@Override
	public int get(int id) {
		switch(id) {
		case 0: return (int)(tile.energyReq & 0xFFFFFFFF);
		case 1: return (int)(tile.remainingProgress & 0xFFFFFFFF);
		case 2: return (int)(tile.energyReq >>> 32);
		case 3: return (int)(tile.remainingProgress >>> 32);
		case 4: return tile.isWorking ? 1 : 0;
		default: return 0;
		}
	}

	@Override
	public void set(int id, int value) {
		switch(id) {
		case 0:
			tile.energyReq = tile.energyReq | 0xFFFFFFFF & value;
			break;
		case 1:
			tile.remainingProgress = tile.remainingProgress | 0xFFFFFFFF & value;
			break;
		case 2:
			tile.energyReq = (tile.energyReq | 0xFFFFFFFF00000000L) & (((long)value << 32) | 0xFFFFFFFF);
			break;
		case 3:
			tile.remainingProgress = (tile.remainingProgress | 0xFFFFFFFF00000000L) & (((long)value << 32) | 0xFFFFFFFF);
			break;
		case 4:
			tile.isWorking = value != 0;
			break;
		}
	}

	@Override
	public int getCount() {
		return 5;
	}
}

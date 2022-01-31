package thelm.packagedexcrafting.inventory;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.IItemHandlerModifiable;
import thelm.packagedauto.inventory.BaseItemHandler;
import thelm.packagedexcrafting.block.entity.CombinationCrafterBlockEntity;

public class CombinationCrafterItemHandler extends BaseItemHandler<CombinationCrafterBlockEntity> {

	public CombinationCrafterItemHandler(CombinationCrafterBlockEntity blockEntity) {
		super(blockEntity, 3);
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
		case 0: return (int)(blockEntity.energyReq & 0xFFFFFFFF);
		case 1: return (int)(blockEntity.remainingProgress & 0xFFFFFFFF);
		case 2: return (int)(blockEntity.energyReq >>> 32);
		case 3: return (int)(blockEntity.remainingProgress >>> 32);
		case 4: return blockEntity.isWorking ? 1 : 0;
		default: return 0;
		}
	}

	@Override
	public void set(int id, int value) {
		switch(id) {
		case 0:
			blockEntity.energyReq = blockEntity.energyReq | 0xFFFFFFFF & value;
			break;
		case 1:
			blockEntity.remainingProgress = blockEntity.remainingProgress | 0xFFFFFFFF & value;
			break;
		case 2:
			blockEntity.energyReq = (blockEntity.energyReq | 0xFFFFFFFF00000000L) & (((long)value << 32) | 0xFFFFFFFF);
			break;
		case 3:
			blockEntity.remainingProgress = (blockEntity.remainingProgress | 0xFFFFFFFF00000000L) & (((long)value << 32) | 0xFFFFFFFF);
			break;
		case 4:
			blockEntity.isWorking = value != 0;
			break;
		}
	}

	@Override
	public int getCount() {
		return 5;
	}
}

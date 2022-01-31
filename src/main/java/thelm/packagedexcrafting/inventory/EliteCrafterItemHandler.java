package thelm.packagedexcrafting.inventory;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.IItemHandlerModifiable;
import thelm.packagedauto.inventory.BaseItemHandler;
import thelm.packagedexcrafting.block.entity.EliteCrafterBlockEntity;

public class EliteCrafterItemHandler extends BaseItemHandler<EliteCrafterBlockEntity> {

	public EliteCrafterItemHandler(EliteCrafterBlockEntity blockEntity) {
		super(blockEntity, 51);
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		if(slot == 50) {
			return stack.getCapability(CapabilityEnergy.ENERGY).isPresent();
		}
		return false;
	}

	@Override
	public IItemHandlerModifiable getWrapperForDirection(Direction side) {
		return wrapperMap.computeIfAbsent(side, s->new EliteCrafterItemHandlerWrapper(this, s));
	}

	@Override
	public int get(int id) {
		switch(id) {
		case 0: return blockEntity.remainingProgress;
		case 1: return blockEntity.isWorking ? 1 : 0;
		default: return 0;
		}
	}

	@Override
	public void set(int id, int value) {
		switch(id) {
		case 0:
			blockEntity.remainingProgress = value;
			break;
		case 1:
			blockEntity.isWorking = value != 0;
			break;
		}
	}

	@Override
	public int getCount() {
		return 2;
	}
}

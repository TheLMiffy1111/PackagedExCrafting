package thelm.packagedexcrafting.inventory;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.IItemHandlerModifiable;
import thelm.packagedauto.inventory.BaseItemHandler;
import thelm.packagedexcrafting.block.entity.EnderCrafterBlockEntity;

public class EnderCrafterItemHandler extends BaseItemHandler<EnderCrafterBlockEntity> {

	public EnderCrafterItemHandler(EnderCrafterBlockEntity blockEntity) {
		super(blockEntity, 11);
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		if(slot == 10) {
			return stack.getCapability(CapabilityEnergy.ENERGY).isPresent();
		}
		return false;
	}

	@Override
	public IItemHandlerModifiable getWrapperForDirection(Direction side) {
		return wrapperMap.computeIfAbsent(side, s->new EnderCrafterItemHandlerWrapper(this, s));
	}

	@Override
	public int get(int id) {
		return switch(id) {
		case 0 -> blockEntity.progress;
		case 1 -> blockEntity.actualProgressReq;
		case 2 -> blockEntity.isWorking ? 1 : 0;
		default -> 0;
		};
	}

	@Override
	public void set(int id, int value) {
		switch(id) {
		case 0 -> blockEntity.progress = value;
		case 1 -> blockEntity.actualProgressReq = value;
		case 2 -> blockEntity.isWorking = value != 0;
		}
	}

	@Override
	public int getCount() {
		return 3;
	}
}

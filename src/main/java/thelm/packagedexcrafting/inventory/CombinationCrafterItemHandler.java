package thelm.packagedexcrafting.inventory;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
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
			return stack.getCapability(ForgeCapabilities.ENERGY).isPresent();
		}
		return false;
	}

	@Override
	public IItemHandlerModifiable getWrapperForDirection(Direction side) {
		return wrapperMap.computeIfAbsent(side, s->new CombinationCrafterItemHandlerWrapper(this, s));
	}

	@Override
	public int get(int id) {
		return switch(id) {
		case 0 -> (int)(blockEntity.energyReq & 0xFFFFFFFF);
		case 1 -> (int)(blockEntity.remainingProgress & 0xFFFFFFFF);
		case 2 -> (int)(blockEntity.energyReq >>> 32);
		case 3 -> (int)(blockEntity.remainingProgress >>> 32);
		case 4 -> blockEntity.isWorking ? 1 : 0;
		case 5 -> blockEntity.getEnergyStorage().getEnergyStored();
		default -> 0;
		};
	}

	@Override
	public void set(int id, int value) {
		switch(id) {
		case 0 -> blockEntity.energyReq = blockEntity.energyReq | 0xFFFFFFFF & value;
		case 1 -> blockEntity.remainingProgress = blockEntity.remainingProgress | 0xFFFFFFFF & value;
		case 2 -> blockEntity.energyReq = (blockEntity.energyReq | 0xFFFFFFFF00000000L) & (((long)value << 32) | 0xFFFFFFFF);
		case 3 -> blockEntity.remainingProgress = (blockEntity.remainingProgress | 0xFFFFFFFF00000000L) & (((long)value << 32) | 0xFFFFFFFF);
		case 4 -> blockEntity.isWorking = value != 0;
		case 5 -> blockEntity.getEnergyStorage().setEnergyStored(value);
		}
	}

	@Override
	public int getCount() {
		return 6;
	}
}

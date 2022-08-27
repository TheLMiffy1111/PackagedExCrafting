package thelm.packagedexcrafting.inventory;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import thelm.packagedauto.inventory.BaseItemHandler;
import thelm.packagedexcrafting.block.entity.EnderCrafterBlockEntity;

public class EnderCrafterItemHandler extends BaseItemHandler<EnderCrafterBlockEntity> {

	public EnderCrafterItemHandler(EnderCrafterBlockEntity blockEntity) {
		super(blockEntity, 11);
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		if(slot == 10) {
			return stack.getCapability(ForgeCapabilities.ENERGY).isPresent();
		}
		return false;
	}

	@Override
	public int get(int id) {
		switch(id) {
		case 0: return blockEntity.progress;
		case 1: return blockEntity.actualProgressReq;
		case 2: return blockEntity.isWorking ? 1 : 0;
		default: return 0;
		}
	}

	@Override
	public void set(int id, int value) {
		switch(id) {
		case 0:
			blockEntity.progress = value;
			break;
		case 1:
			blockEntity.actualProgressReq = value;
			break;
		case 2:
			blockEntity.isWorking = value != 0;
			break;
		}
	}

	@Override
	public int getCount() {
		return 3;
	}
}

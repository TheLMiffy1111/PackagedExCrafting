package thelm.packagedexcrafting.inventory;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import thelm.packagedauto.inventory.BaseItemHandler;
import thelm.packagedexcrafting.block.entity.BasicCrafterBlockEntity;

public class BasicCrafterItemHandler extends BaseItemHandler<BasicCrafterBlockEntity> {

	public BasicCrafterItemHandler(BasicCrafterBlockEntity blockEntity) {
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

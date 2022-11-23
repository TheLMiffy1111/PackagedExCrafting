package thelm.packagedexcrafting.inventory;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import thelm.packagedauto.inventory.SidedItemHandlerWrapper;

public class FluxCrafterItemHandlerWrapper extends SidedItemHandlerWrapper<EnderCrafterItemHandler> {

	public static final int[] SLOTS = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

	public FluxCrafterItemHandlerWrapper(EnderCrafterItemHandler itemHandler, Direction direction) {
		super(itemHandler, direction);
	}

	@Override
	public int[] getSlotsForDirection(Direction direction) {
		return SLOTS;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack stack, Direction direction) {
		return false;
	}

	@Override
	public boolean canExtractItem(int index, Direction direction) {
		return itemHandler.blockEntity.isWorking ? index == 9 : true;
	}
}

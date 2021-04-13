package thelm.packagedexcrafting.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import thelm.packagedauto.inventory.SidedItemHandlerWrapper;

public class EnderCrafterItemHandlerWrapper extends SidedItemHandlerWrapper<EnderCrafterItemHandler> {

	public static final int[] SLOTS = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

	public EnderCrafterItemHandlerWrapper(EnderCrafterItemHandler itemHandler, Direction direction) {
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
		return itemHandler.tile.isWorking ? index == 9 : true;
	}
}

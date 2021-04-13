package thelm.packagedexcrafting.inventory;

import java.util.stream.IntStream;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import thelm.packagedauto.inventory.SidedItemHandlerWrapper;

public class UltimateCrafterItemHandlerWrapper extends SidedItemHandlerWrapper<UltimateCrafterItemHandler> {

	public static final int[] SLOTS = IntStream.rangeClosed(0, 81).toArray();

	public UltimateCrafterItemHandlerWrapper(UltimateCrafterItemHandler itemHandler, Direction direction) {
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
		return itemHandler.tile.isWorking ? index == 81 : true;
	}
}

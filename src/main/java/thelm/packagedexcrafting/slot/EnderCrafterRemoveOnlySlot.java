package thelm.packagedexcrafting.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import thelm.packagedexcrafting.tile.EnderCrafterTile;

//Code from CoFHCore
public class EnderCrafterRemoveOnlySlot extends SlotItemHandler {

	public final EnderCrafterTile tile;

	public EnderCrafterRemoveOnlySlot(EnderCrafterTile tile, int index, int x, int y) {
		super(tile.getItemHandler(), index, x, y);
		this.tile = tile;
	}

	@Override
	public boolean canTakeStack(PlayerEntity playerIn) {
		return !tile.isWorking;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return false;
	}
}

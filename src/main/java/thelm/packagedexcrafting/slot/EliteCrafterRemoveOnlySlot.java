package thelm.packagedexcrafting.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import thelm.packagedexcrafting.tile.EliteCrafterTile;

//Code from CoFHCore
public class EliteCrafterRemoveOnlySlot extends SlotItemHandler {

	public final EliteCrafterTile tile;

	public EliteCrafterRemoveOnlySlot(EliteCrafterTile tile, int index, int x, int y) {
		super(tile.getItemHandler(), index, x, y);
		this.tile = tile;
	}

	@Override
	public boolean mayPickup(PlayerEntity playerIn) {
		return !tile.isWorking;
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return false;
	}
}

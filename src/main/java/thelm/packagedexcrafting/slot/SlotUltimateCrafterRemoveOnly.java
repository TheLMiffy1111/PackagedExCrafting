package thelm.packagedexcrafting.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import thelm.packagedauto.slot.SlotBase;
import thelm.packagedexcrafting.tile.TileUltimateCrafter;

//Code from CoFHCore
public class SlotUltimateCrafterRemoveOnly extends SlotBase {

	public final TileUltimateCrafter tile;

	public SlotUltimateCrafterRemoveOnly(TileUltimateCrafter tile, int index, int x, int y) {
		super(tile.getInventory(), index, x, y);
		this.tile = tile;
	}

	@Override
	public boolean canTakeStack(EntityPlayer playerIn) {
		return !tile.isWorking;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return false;
	}
}

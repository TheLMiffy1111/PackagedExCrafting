package thelm.packagedexcrafting.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import thelm.packagedauto.slot.SlotBase;
import thelm.packagedexcrafting.tile.TileBasicCrafter;

//Code from CoFHCore
public class SlotBasicCrafterRemoveOnly extends SlotBase {

	public final TileBasicCrafter tile;

	public SlotBasicCrafterRemoveOnly(TileBasicCrafter tile, int index, int x, int y) {
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

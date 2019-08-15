package thelm.packagedexcrafting.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import thelm.packagedauto.slot.SlotBase;
import thelm.packagedexcrafting.tile.TileCombinationCrafter;

//Code from CoFHCore
public class SlotCombinationCrafterRemoveOnly extends SlotBase {

	public final TileCombinationCrafter tile;

	public SlotCombinationCrafterRemoveOnly(TileCombinationCrafter tile, int index, int x, int y) {
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

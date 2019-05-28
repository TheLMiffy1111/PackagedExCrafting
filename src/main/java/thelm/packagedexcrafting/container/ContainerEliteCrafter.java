package thelm.packagedexcrafting.container;

import net.minecraft.entity.player.InventoryPlayer;
import thelm.packagedauto.container.ContainerTileBase;
import thelm.packagedauto.slot.SlotBase;
import thelm.packagedauto.slot.SlotRemoveOnly;
import thelm.packagedexcrafting.slot.SlotEliteCrafterRemoveOnly;
import thelm.packagedexcrafting.tile.TileEliteCrafter;

public class ContainerEliteCrafter extends ContainerTileBase<TileEliteCrafter> {

	public ContainerEliteCrafter(InventoryPlayer playerInventory, TileEliteCrafter tile) {
		super(playerInventory, tile);
		addSlotToContainer(new SlotBase(inventory, 50, 8, 89));
		for(int i = 0; i < 7; ++i) {
			for(int j = 0; j < 7; ++j) {
				addSlotToContainer(new SlotEliteCrafterRemoveOnly(tile, i*7+j, 44+j*18, 17+i*18));
			}
		}
		addSlotToContainer(new SlotRemoveOnly(inventory, 49, 206, 71));
		setupPlayerInventory();
	}

	@Override
	public int getPlayerInvX() {
		return 37;
	}

	@Override
	public int getPlayerInvY() {
		return 156;
	}
}

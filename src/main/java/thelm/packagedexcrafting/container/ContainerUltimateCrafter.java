package thelm.packagedexcrafting.container;

import net.minecraft.entity.player.InventoryPlayer;
import thelm.packagedauto.container.ContainerTileBase;
import thelm.packagedauto.slot.SlotBase;
import thelm.packagedauto.slot.SlotRemoveOnly;
import thelm.packagedexcrafting.slot.SlotUltimateCrafterRemoveOnly;
import thelm.packagedexcrafting.tile.TileUltimateCrafter;

public class ContainerUltimateCrafter extends ContainerTileBase<TileUltimateCrafter> {

	public ContainerUltimateCrafter(InventoryPlayer playerInventory, TileUltimateCrafter tile) {
		super(playerInventory, tile);
		addSlotToContainer(new SlotBase(inventory, 82, 8, 107));
		for(int i = 0; i < 9; ++i) {
			for(int j = 0; j < 9; ++j) {
				addSlotToContainer(new SlotUltimateCrafterRemoveOnly(tile, i*9+j, 44+j*18, 17+i*18));
			}
		}
		addSlotToContainer(new SlotRemoveOnly(inventory, 81, 242, 89));
		setupPlayerInventory();
	}

	@Override
	public int getPlayerInvX() {
		return 55;
	}

	@Override
	public int getPlayerInvY() {
		return 192;
	}
}

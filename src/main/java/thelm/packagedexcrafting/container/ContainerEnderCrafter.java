package thelm.packagedexcrafting.container;

import net.minecraft.entity.player.InventoryPlayer;
import thelm.packagedauto.container.ContainerTileBase;
import thelm.packagedauto.slot.SlotBase;
import thelm.packagedauto.slot.SlotRemoveOnly;
import thelm.packagedexcrafting.slot.SlotEnderCrafterRemoveOnly;
import thelm.packagedexcrafting.tile.TileEnderCrafter;

public class ContainerEnderCrafter extends ContainerTileBase<TileEnderCrafter> {

	public ContainerEnderCrafter(InventoryPlayer playerInventory, TileEnderCrafter tile) {
		super(playerInventory, tile);
		addSlotToContainer(new SlotBase(inventory, 10, 8, 53));
		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 3; ++j) {
				addSlotToContainer(new SlotEnderCrafterRemoveOnly(tile, i*3+j, 44+j*18, 17+i*18));
			}
		}
		addSlotToContainer(new SlotRemoveOnly(inventory, 9, 134, 35));
		setupPlayerInventory();
	}
}

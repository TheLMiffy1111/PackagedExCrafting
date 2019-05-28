package thelm.packagedexcrafting.container;

import net.minecraft.entity.player.InventoryPlayer;
import thelm.packagedauto.container.ContainerTileBase;
import thelm.packagedauto.slot.SlotBase;
import thelm.packagedauto.slot.SlotRemoveOnly;
import thelm.packagedexcrafting.slot.SlotBasicCrafterRemoveOnly;
import thelm.packagedexcrafting.tile.TileBasicCrafter;

public class ContainerBasicCrafter extends ContainerTileBase<TileBasicCrafter> {

	public ContainerBasicCrafter(InventoryPlayer playerInventory, TileBasicCrafter tile) {
		super(playerInventory, tile);
		addSlotToContainer(new SlotBase(inventory, 10, 8, 53));
		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 3; ++j) {
				addSlotToContainer(new SlotBasicCrafterRemoveOnly(tile, i*3+j, 44+j*18, 17+i*18));
			}
		}
		addSlotToContainer(new SlotRemoveOnly(inventory, 9, 134, 35));
		setupPlayerInventory();
	}
}

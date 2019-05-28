package thelm.packagedexcrafting.container;

import net.minecraft.entity.player.InventoryPlayer;
import thelm.packagedauto.container.ContainerTileBase;
import thelm.packagedauto.slot.SlotBase;
import thelm.packagedauto.slot.SlotRemoveOnly;
import thelm.packagedexcrafting.slot.SlotAdvancedCrafterRemoveOnly;
import thelm.packagedexcrafting.tile.TileAdvancedCrafter;

public class ContainerAdvancedCrafter extends ContainerTileBase<TileAdvancedCrafter> {

	public ContainerAdvancedCrafter(InventoryPlayer playerInventory, TileAdvancedCrafter tile) {
		super(playerInventory, tile);
		addSlotToContainer(new SlotBase(inventory, 50, 8, 71));
		for(int i = 0; i < 5; ++i) {
			for(int j = 0; j < 5; ++j) {
				addSlotToContainer(new SlotAdvancedCrafterRemoveOnly(tile, i*5+j, 44+j*18, 17+i*18));
			}
		}
		addSlotToContainer(new SlotRemoveOnly(inventory, 49, 170, 53));
		setupPlayerInventory();
	}

	@Override
	public int getPlayerInvX() {
		return 19;
	}

	@Override
	public int getPlayerInvY() {
		return 120;
	}
}

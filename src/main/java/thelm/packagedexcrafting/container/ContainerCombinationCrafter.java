package thelm.packagedexcrafting.container;

import net.minecraft.entity.player.InventoryPlayer;
import thelm.packagedauto.container.ContainerTileBase;
import thelm.packagedauto.slot.SlotBase;
import thelm.packagedauto.slot.SlotRemoveOnly;
import thelm.packagedexcrafting.slot.SlotCombinationCrafterRemoveOnly;
import thelm.packagedexcrafting.tile.TileCombinationCrafter;

public class ContainerCombinationCrafter extends ContainerTileBase<TileCombinationCrafter> {

	public ContainerCombinationCrafter(InventoryPlayer playerInventory, TileCombinationCrafter tile) {
		super(playerInventory, tile);
		addSlotToContainer(new SlotBase(inventory, 2, 8, 53));
		addSlotToContainer(new SlotCombinationCrafterRemoveOnly(tile, 0, 53, 35));
		addSlotToContainer(new SlotRemoveOnly(inventory, 1, 107, 35));
		setupPlayerInventory();
	}
}

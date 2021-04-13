package thelm.packagedexcrafting.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.items.SlotItemHandler;
import thelm.packagedauto.container.BaseContainer;
import thelm.packagedauto.container.factory.PositionalTileContainerFactory;
import thelm.packagedauto.slot.RemoveOnlySlot;
import thelm.packagedexcrafting.slot.CombinationCrafterRemoveOnlySlot;
import thelm.packagedexcrafting.tile.CombinationCrafterTile;

public class CombinationCrafterContainer extends BaseContainer<CombinationCrafterTile> {

	public static final ContainerType<CombinationCrafterContainer> TYPE_INSTANCE = (ContainerType<CombinationCrafterContainer>)IForgeContainerType.
			create(new PositionalTileContainerFactory<>(CombinationCrafterContainer::new)).
			setRegistryName("packagedexcrafting:combination_crafter");

	public CombinationCrafterContainer(int windowId, PlayerInventory playerInventory, CombinationCrafterTile tile) {
		super(TYPE_INSTANCE, windowId, playerInventory, tile);
		addSlot(new SlotItemHandler(itemHandler, 2, 8, 53));
		addSlot(new CombinationCrafterRemoveOnlySlot(tile, 0, 53, 35));
		addSlot(new RemoveOnlySlot(itemHandler, 1, 107, 35));
		setupPlayerInventory();
	}
}

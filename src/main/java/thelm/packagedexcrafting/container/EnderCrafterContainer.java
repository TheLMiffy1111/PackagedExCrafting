package thelm.packagedexcrafting.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.items.SlotItemHandler;
import thelm.packagedauto.container.BaseContainer;
import thelm.packagedauto.container.factory.PositionalTileContainerFactory;
import thelm.packagedauto.slot.RemoveOnlySlot;
import thelm.packagedexcrafting.slot.EnderCrafterRemoveOnlySlot;
import thelm.packagedexcrafting.tile.EnderCrafterTile;

public class EnderCrafterContainer extends BaseContainer<EnderCrafterTile> {

	public static final ContainerType<EnderCrafterContainer> TYPE_INSTANCE = (ContainerType<EnderCrafterContainer>)IForgeContainerType.
			create(new PositionalTileContainerFactory<>(EnderCrafterContainer::new)).
			setRegistryName("packagedexcrafting:ender_crafter");

	public EnderCrafterContainer(int windowId, PlayerInventory playerInventory, EnderCrafterTile tile) {
		super(TYPE_INSTANCE, windowId, playerInventory, tile);
		addSlot(new SlotItemHandler(itemHandler, 10, 8, 53));
		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 3; ++j) {
				addSlot(new EnderCrafterRemoveOnlySlot(tile, i*3+j, 44+j*18, 17+i*18));
			}
		}
		addSlot(new RemoveOnlySlot(itemHandler, 9, 134, 35));
		setupPlayerInventory();
	}
}

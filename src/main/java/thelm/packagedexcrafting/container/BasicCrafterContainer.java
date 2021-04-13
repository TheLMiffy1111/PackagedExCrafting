package thelm.packagedexcrafting.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.items.SlotItemHandler;
import thelm.packagedauto.container.BaseContainer;
import thelm.packagedauto.container.factory.PositionalTileContainerFactory;
import thelm.packagedauto.slot.RemoveOnlySlot;
import thelm.packagedexcrafting.slot.BasicCrafterRemoveOnlySlot;
import thelm.packagedexcrafting.tile.BasicCrafterTile;

public class BasicCrafterContainer extends BaseContainer<BasicCrafterTile> {

	public static final ContainerType<BasicCrafterContainer> TYPE_INSTANCE = (ContainerType<BasicCrafterContainer>)IForgeContainerType.
			create(new PositionalTileContainerFactory<>(BasicCrafterContainer::new)).
			setRegistryName("packagedexcrafting:basic_crafter");

	public BasicCrafterContainer(int windowId, PlayerInventory playerInventory, BasicCrafterTile tile) {
		super(TYPE_INSTANCE, windowId, playerInventory, tile);
		addSlot(new SlotItemHandler(itemHandler, 10, 8, 53));
		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 3; ++j) {
				addSlot(new BasicCrafterRemoveOnlySlot(tile, i*3+j, 44+j*18, 17+i*18));
			}
		}
		addSlot(new RemoveOnlySlot(itemHandler, 9, 134, 35));
		setupPlayerInventory();
	}
}

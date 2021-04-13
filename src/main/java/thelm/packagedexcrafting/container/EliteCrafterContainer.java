package thelm.packagedexcrafting.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.items.SlotItemHandler;
import thelm.packagedauto.container.BaseContainer;
import thelm.packagedauto.container.factory.PositionalTileContainerFactory;
import thelm.packagedauto.slot.RemoveOnlySlot;
import thelm.packagedexcrafting.slot.EliteCrafterRemoveOnlySlot;
import thelm.packagedexcrafting.tile.EliteCrafterTile;

public class EliteCrafterContainer extends BaseContainer<EliteCrafterTile> {

	public static final ContainerType<EliteCrafterContainer> TYPE_INSTANCE = (ContainerType<EliteCrafterContainer>)IForgeContainerType.
			create(new PositionalTileContainerFactory<>(EliteCrafterContainer::new)).
			setRegistryName("packagedexcrafting:elite_crafter");

	public EliteCrafterContainer(int windowId, PlayerInventory playerInventory, EliteCrafterTile tile) {
		super(TYPE_INSTANCE, windowId, playerInventory, tile);
		addSlot(new SlotItemHandler(itemHandler, 50, 8, 89));
		for(int i = 0; i < 7; ++i) {
			for(int j = 0; j < 7; ++j) {
				addSlot(new EliteCrafterRemoveOnlySlot(tile, i*7+j, 44+j*18, 17+i*18));
			}
		}
		addSlot(new RemoveOnlySlot(itemHandler, 49, 206, 71));
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

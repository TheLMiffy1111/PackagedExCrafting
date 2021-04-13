package thelm.packagedexcrafting.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.items.SlotItemHandler;
import thelm.packagedauto.container.BaseContainer;
import thelm.packagedauto.container.factory.PositionalTileContainerFactory;
import thelm.packagedauto.slot.RemoveOnlySlot;
import thelm.packagedexcrafting.slot.UltimateCrafterRemoveOnlySlot;
import thelm.packagedexcrafting.tile.UltimateCrafterTile;

public class UltimateCrafterContainer extends BaseContainer<UltimateCrafterTile> {

	public static final ContainerType<UltimateCrafterContainer> TYPE_INSTANCE = (ContainerType<UltimateCrafterContainer>)IForgeContainerType.
			create(new PositionalTileContainerFactory<>(UltimateCrafterContainer::new)).
			setRegistryName("packagedexcrafting:ultimate_crafter");

	public UltimateCrafterContainer(int windowId, PlayerInventory playerInventory, UltimateCrafterTile tile) {
		super(TYPE_INSTANCE, windowId, playerInventory, tile);
		addSlot(new SlotItemHandler(itemHandler, 82, 8, 107));
		for(int i = 0; i < 9; ++i) {
			for(int j = 0; j < 9; ++j) {
				addSlot(new UltimateCrafterRemoveOnlySlot(tile, i*9+j, 44+j*18, 17+i*18));
			}
		}
		addSlot(new RemoveOnlySlot(itemHandler, 81, 242, 89));
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

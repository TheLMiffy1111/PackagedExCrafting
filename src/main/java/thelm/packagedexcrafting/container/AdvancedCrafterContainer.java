package thelm.packagedexcrafting.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.items.SlotItemHandler;
import thelm.packagedauto.container.BaseContainer;
import thelm.packagedauto.container.factory.PositionalTileContainerFactory;
import thelm.packagedauto.slot.RemoveOnlySlot;
import thelm.packagedexcrafting.slot.AdvancedCrafterRemoveOnlySlot;
import thelm.packagedexcrafting.tile.AdvancedCrafterTile;

public class AdvancedCrafterContainer extends BaseContainer<AdvancedCrafterTile> {

	public static final ContainerType<AdvancedCrafterContainer> TYPE_INSTANCE = (ContainerType<AdvancedCrafterContainer>)IForgeContainerType.
			create(new PositionalTileContainerFactory<>(AdvancedCrafterContainer::new)).
			setRegistryName("packagedexcrafting:advanced_crafter");

	public AdvancedCrafterContainer(int windowId, PlayerInventory playerInventory, AdvancedCrafterTile tile) {
		super(TYPE_INSTANCE, windowId, playerInventory, tile);
		addSlot(new SlotItemHandler(itemHandler, 50, 8, 71));
		for(int i = 0; i < 5; ++i) {
			for(int j = 0; j < 5; ++j) {
				addSlot(new AdvancedCrafterRemoveOnlySlot(tile, i*5+j, 44+j*18, 17+i*18));
			}
		}
		addSlot(new RemoveOnlySlot(itemHandler, 49, 170, 53));
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

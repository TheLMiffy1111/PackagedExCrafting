package thelm.packagedexcrafting.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.items.SlotItemHandler;
import thelm.packagedauto.menu.BaseMenu;
import thelm.packagedauto.menu.factory.PositionalBlockEntityMenuFactory;
import thelm.packagedauto.slot.RemoveOnlySlot;
import thelm.packagedexcrafting.block.entity.FluxCrafterBlockEntity;
import thelm.packagedexcrafting.slot.FluxCrafterRemoveOnlySlot;

public class FluxCrafterMenu extends BaseMenu<FluxCrafterBlockEntity> {

	public static final MenuType<FluxCrafterMenu> TYPE_INSTANCE = (MenuType<FluxCrafterMenu>)IForgeMenuType.
			create(new PositionalBlockEntityMenuFactory<>(FluxCrafterMenu::new));

	public FluxCrafterMenu(int windowId, Inventory inventory, FluxCrafterBlockEntity blockEntity) {
		super(TYPE_INSTANCE, windowId, inventory, blockEntity);
		addSlot(new SlotItemHandler(itemHandler, 10, 8, 53));
		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 3; ++j) {
				addSlot(new FluxCrafterRemoveOnlySlot(blockEntity, i*3+j, 44+j*18, 17+i*18));
			}
		}
		addSlot(new RemoveOnlySlot(itemHandler, 9, 134, 35));
		setupPlayerInventory();
	}
}

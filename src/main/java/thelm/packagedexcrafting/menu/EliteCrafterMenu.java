package thelm.packagedexcrafting.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.items.SlotItemHandler;
import thelm.packagedauto.menu.BaseMenu;
import thelm.packagedauto.menu.factory.PositionalBlockEntityMenuFactory;
import thelm.packagedauto.slot.RemoveOnlySlot;
import thelm.packagedexcrafting.block.entity.EliteCrafterBlockEntity;
import thelm.packagedexcrafting.slot.EliteCrafterRemoveOnlySlot;

public class EliteCrafterMenu extends BaseMenu<EliteCrafterBlockEntity> {

	public static final MenuType<EliteCrafterMenu> TYPE_INSTANCE = (MenuType<EliteCrafterMenu>)IForgeMenuType.
			create(new PositionalBlockEntityMenuFactory<>(EliteCrafterMenu::new)).
			setRegistryName("packagedexcrafting:elite_crafter");

	public EliteCrafterMenu(int windowId, Inventory inventory, EliteCrafterBlockEntity blockEntity) {
		super(TYPE_INSTANCE, windowId, inventory, blockEntity);
		addSlot(new SlotItemHandler(itemHandler, 50, 8, 89));
		for(int i = 0; i < 7; ++i) {
			for(int j = 0; j < 7; ++j) {
				addSlot(new EliteCrafterRemoveOnlySlot(blockEntity, i*7+j, 44+j*18, 17+i*18));
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

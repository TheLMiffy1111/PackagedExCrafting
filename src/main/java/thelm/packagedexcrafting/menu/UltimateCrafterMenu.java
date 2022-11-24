package thelm.packagedexcrafting.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.items.SlotItemHandler;
import thelm.packagedauto.menu.BaseMenu;
import thelm.packagedauto.menu.factory.PositionalBlockEntityMenuFactory;
import thelm.packagedauto.slot.RemoveOnlySlot;
import thelm.packagedexcrafting.block.entity.UltimateCrafterBlockEntity;
import thelm.packagedexcrafting.slot.UltimateCrafterRemoveOnlySlot;

public class UltimateCrafterMenu extends BaseMenu<UltimateCrafterBlockEntity> {

	public static final MenuType<UltimateCrafterMenu> TYPE_INSTANCE = IForgeMenuType.create(new PositionalBlockEntityMenuFactory<>(UltimateCrafterMenu::new));

	public UltimateCrafterMenu(int windowId, Inventory inventory, UltimateCrafterBlockEntity blockEntity) {
		super(TYPE_INSTANCE, windowId, inventory, blockEntity);
		addSlot(new SlotItemHandler(itemHandler, 82, 8, 107));
		for(int i = 0; i < 9; ++i) {
			for(int j = 0; j < 9; ++j) {
				addSlot(new UltimateCrafterRemoveOnlySlot(blockEntity, i*9+j, 44+j*18, 17+i*18));
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

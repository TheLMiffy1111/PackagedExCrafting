package thelm.packagedexcrafting.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.items.SlotItemHandler;
import thelm.packagedauto.menu.BaseMenu;
import thelm.packagedauto.menu.factory.PositionalBlockEntityMenuFactory;
import thelm.packagedauto.slot.RemoveOnlySlot;
import thelm.packagedexcrafting.block.entity.BasicCrafterBlockEntity;
import thelm.packagedexcrafting.slot.BasicCrafterRemoveOnlySlot;

public class BasicCrafterMenu extends BaseMenu<BasicCrafterBlockEntity> {

	public static final MenuType<BasicCrafterMenu> TYPE_INSTANCE = IForgeMenuType.create(new PositionalBlockEntityMenuFactory<>(BasicCrafterMenu::new));

	public BasicCrafterMenu(int windowId, Inventory inventory, BasicCrafterBlockEntity blockEntity) {
		super(TYPE_INSTANCE, windowId, inventory, blockEntity);
		addSlot(new SlotItemHandler(itemHandler, 10, 8, 53));
		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 3; ++j) {
				addSlot(new BasicCrafterRemoveOnlySlot(blockEntity, i*3+j, 44+j*18, 17+i*18));
			}
		}
		addSlot(new RemoveOnlySlot(itemHandler, 9, 134, 35));
		setupPlayerInventory();
	}
}

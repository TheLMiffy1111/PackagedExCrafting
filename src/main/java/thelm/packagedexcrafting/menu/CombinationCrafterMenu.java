package thelm.packagedexcrafting.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.items.SlotItemHandler;
import thelm.packagedauto.menu.BaseMenu;
import thelm.packagedauto.menu.factory.PositionalBlockEntityMenuFactory;
import thelm.packagedauto.slot.RemoveOnlySlot;
import thelm.packagedexcrafting.block.entity.CombinationCrafterBlockEntity;
import thelm.packagedexcrafting.slot.CombinationCrafterRemoveOnlySlot;

public class CombinationCrafterMenu extends BaseMenu<CombinationCrafterBlockEntity> {

	public static final MenuType<CombinationCrafterMenu> TYPE_INSTANCE = (MenuType<CombinationCrafterMenu>)IForgeMenuType.
			create(new PositionalBlockEntityMenuFactory<>(CombinationCrafterMenu::new)).
			setRegistryName("packagedexcrafting:combination_crafter");

	public CombinationCrafterMenu(int windowId, Inventory inventory, CombinationCrafterBlockEntity blockEntity) {
		super(TYPE_INSTANCE, windowId, inventory, blockEntity);
		addSlot(new SlotItemHandler(itemHandler, 2, 8, 53));
		addSlot(new CombinationCrafterRemoveOnlySlot(blockEntity, 0, 53, 35));
		addSlot(new RemoveOnlySlot(itemHandler, 1, 107, 35));
		setupPlayerInventory();
	}
}

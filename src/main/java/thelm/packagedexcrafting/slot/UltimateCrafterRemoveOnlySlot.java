package thelm.packagedexcrafting.slot;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import thelm.packagedexcrafting.block.entity.UltimateCrafterBlockEntity;

//Code from CoFHCore
public class UltimateCrafterRemoveOnlySlot extends SlotItemHandler {

	public final UltimateCrafterBlockEntity blockEntity;

	public UltimateCrafterRemoveOnlySlot(UltimateCrafterBlockEntity blockEntity, int index, int x, int y) {
		super(blockEntity.getItemHandler(), index, x, y);
		this.blockEntity = blockEntity;
	}

	@Override
	public boolean mayPickup(Player player) {
		return !blockEntity.isWorking;
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return false;
	}
}

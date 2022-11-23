package thelm.packagedexcrafting.slot;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import thelm.packagedexcrafting.block.entity.FluxCrafterBlockEntity;

//Code from CoFHCore
public class FluxCrafterRemoveOnlySlot extends SlotItemHandler {

	public final FluxCrafterBlockEntity blockEntity;

	public FluxCrafterRemoveOnlySlot(FluxCrafterBlockEntity blockEntity, int index, int x, int y) {
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

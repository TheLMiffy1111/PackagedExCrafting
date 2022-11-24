package thelm.packagedexcrafting.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thelm.packagedauto.block.entity.BaseBlockEntity;
import thelm.packagedexcrafting.block.MarkedPedestalBlock;
import thelm.packagedexcrafting.inventory.MarkedPedestalItemHandler;

public class MarkedPedestalBlockEntity extends BaseBlockEntity {

	public static final BlockEntityType<MarkedPedestalBlockEntity> TYPE_INSTANCE = BlockEntityType.Builder.
			of(MarkedPedestalBlockEntity::new, MarkedPedestalBlock.INSTANCE).build(null);

	public MarkedPedestalBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE_INSTANCE, pos, state);
		setItemHandler(new MarkedPedestalItemHandler(this));
	}

	@Override
	protected Component getDefaultName() {
		return Component.translatable("block.packagedexcrafting.marked_pedestal");
	}

	public void spawnItem() {
		ItemStack stack = itemHandler.getStackInSlot(0);
		itemHandler.setStackInSlot(0, ItemStack.EMPTY);
		if(!level.isClientSide && !stack.isEmpty()) {
			double dx = level.random.nextFloat()/2+0.25;
			double dy = level.random.nextFloat()/2+0.25;
			double dz = level.random.nextFloat()/2+0.25;
			ItemEntity itemEntity = new ItemEntity(level, worldPosition.getX()+dx, worldPosition.getY()+dy, worldPosition.getZ()+dz, stack);
			itemEntity.setDefaultPickUpDelay();
			level.addFreshEntity(itemEntity);
		}
	}

	@Override
	public void loadSync(CompoundTag nbt) {
		super.loadSync(nbt);
		itemHandler.load(nbt);
	}

	@Override
	public CompoundTag saveSync(CompoundTag nbt) {
		super.saveSync(nbt);
		itemHandler.save(nbt);
		return nbt;
	}

	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player) {
		return null;
	}
}

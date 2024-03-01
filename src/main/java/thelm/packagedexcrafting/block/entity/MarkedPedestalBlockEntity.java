package thelm.packagedexcrafting.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.ModList;
import thelm.packagedauto.block.entity.BaseBlockEntity;
import thelm.packagedauto.util.MiscHelper;
import thelm.packagedexcrafting.block.MarkedPedestalBlock;
import thelm.packagedexcrafting.integration.appeng.blockentity.AEMarkedPedestalBlockEntity;
import thelm.packagedexcrafting.inventory.MarkedPedestalItemHandler;

public class MarkedPedestalBlockEntity extends BaseBlockEntity {

	public static final BlockEntityType<MarkedPedestalBlockEntity> TYPE_INSTANCE = (BlockEntityType<MarkedPedestalBlockEntity>)BlockEntityType.Builder.
			of(MiscHelper.INSTANCE.<BlockEntityType.BlockEntitySupplier<MarkedPedestalBlockEntity>>conditionalSupplier(
					()->ModList.get().isLoaded("ae2"),
					()->()->AEMarkedPedestalBlockEntity::new, ()->()->MarkedPedestalBlockEntity::new).get(),
					MarkedPedestalBlock.INSTANCE).
			build(null).setRegistryName("packagedexcrafting:marked_pedestal");

	public MarkedPedestalBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE_INSTANCE, pos, state);
		setItemHandler(new MarkedPedestalItemHandler(this));
	}

	@Override
	protected Component getDefaultName() {
		return new TranslatableComponent("block.packagedexcrafting.marked_pedestal");
	}

	public void ejectItem() {
		ItemStack stack = itemHandler.getStackInSlot(0);
		itemHandler.setStackInSlot(0, ItemStack.EMPTY);
		if(!stack.isEmpty()) {
			double dx = level.random.nextFloat()/2+0.25;
			double dy = level.random.nextFloat()/2+0.75;
			double dz = level.random.nextFloat()/2+0.25;
			ItemEntity itemEntity = new ItemEntity(level, worldPosition.getX()+dx, worldPosition.getY()+dy, worldPosition.getZ()+dz, stack);
			itemEntity.setDefaultPickUpDelay();
			level.addFreshEntity(itemEntity);
		}
	}

	@Override
	public int getComparatorSignal() {
		return itemHandler.getStackInSlot(0).isEmpty() ? 0 : 15;
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

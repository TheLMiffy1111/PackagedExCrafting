package thelm.packagedexcrafting.tile;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import thelm.packagedauto.tile.BaseTile;
import thelm.packagedexcrafting.block.MarkedPedestalBlock;
import thelm.packagedexcrafting.inventory.MarkedPedestalItemHandler;

public class MarkedPedestalTile extends BaseTile {

	public static final TileEntityType<MarkedPedestalTile> TYPE_INSTANCE = (TileEntityType<MarkedPedestalTile>)TileEntityType.Builder.
			create(MarkedPedestalTile::new, MarkedPedestalBlock.INSTANCE).
			build(null).setRegistryName("packagedexcrafting:marked_pedestal");

	public MarkedPedestalTile() {
		super(TYPE_INSTANCE);
		setItemHandler(new MarkedPedestalItemHandler(this));
	}

	@Override
	protected ITextComponent getDefaultName() {
		return new TranslationTextComponent("block.packagedexcrafting.marked_pedestal");
	}

	public void spawnItem() {
		ItemStack stack = itemHandler.getStackInSlot(0);
		itemHandler.setStackInSlot(0, ItemStack.EMPTY);
		if(!world.isRemote && !stack.isEmpty()) {
			double dx = world.rand.nextFloat()/2+0.25;
			double dy = world.rand.nextFloat()/2+0.25;
			double dz = world.rand.nextFloat()/2+0.25;
			ItemEntity itemEntity = new ItemEntity(world, pos.getX()+dx, pos.getY()+dy, pos.getZ()+dz, stack);
			itemEntity.setDefaultPickupDelay();
			world.addEntity(itemEntity);
		}
	}
	
	@Override
	public void readSync(CompoundNBT nbt) {
		super.readSync(nbt);
		itemHandler.read(nbt);
	}

	@Override
	public CompoundNBT writeSync(CompoundNBT nbt) {
		super.writeSync(nbt);
		itemHandler.write(nbt);
		return nbt;
	}

	@Override
	public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player) {
		return null;
	}
}

package thelm.packagedexcrafting.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import thelm.packagedauto.block.BaseBlock;
import thelm.packagedauto.block.entity.BaseBlockEntity;
import thelm.packagedexcrafting.block.entity.EnderCrafterBlockEntity;

public class EnderCrafterBlock extends BaseBlock {

	public static final EnderCrafterBlock INSTANCE = new EnderCrafterBlock();
	public static final Item ITEM_INSTANCE = new BlockItem(INSTANCE, new Item.Properties());

	public EnderCrafterBlock() {
		super(BlockBehaviour.Properties.of(Material.METAL).strength(15F, 25F).sound(SoundType.METAL));
	}

	@Override
	public EnderCrafterBlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return EnderCrafterBlockEntity.TYPE_INSTANCE.create(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		return BaseBlockEntity::tick;
	}
}

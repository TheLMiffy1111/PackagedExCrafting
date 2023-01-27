package thelm.packagedexcrafting.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import thelm.packagedauto.block.BaseBlock;
import thelm.packagedauto.block.entity.BaseBlockEntity;
import thelm.packagedexcrafting.block.entity.CombinationCrafterBlockEntity;

public class CombinationCrafterBlock extends BaseBlock {

	public static final CombinationCrafterBlock INSTANCE = new CombinationCrafterBlock();
	public static final Item ITEM_INSTANCE = new BlockItem(INSTANCE, new Item.Properties());
	public static final VoxelShape SHAPE = Shapes.or(box(0, 12, 0, 16, 16, 16), box(0, 5, 0, 16, 11, 16), box(0, 0, 0, 16, 4, 16), box(1, 4, 1, 15, 12, 15));

	public CombinationCrafterBlock() {
		super(BlockBehaviour.Properties.of(Material.METAL).strength(15F, 25F).noOcclusion().sound(SoundType.METAL));
	}

	@Override
	public CombinationCrafterBlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return CombinationCrafterBlockEntity.TYPE_INSTANCE.create(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		return BaseBlockEntity::tick;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
}

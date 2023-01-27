package thelm.packagedexcrafting.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import thelm.packagedauto.block.BaseBlock;
import thelm.packagedauto.block.entity.BaseBlockEntity;
import thelm.packagedexcrafting.block.entity.MarkedPedestalBlockEntity;

public class MarkedPedestalBlock extends BaseBlock {

	public static final MarkedPedestalBlock INSTANCE = new MarkedPedestalBlock();
	public static final Item ITEM_INSTANCE = new BlockItem(INSTANCE, new Item.Properties());
	public static final VoxelShape SHAPE = Shapes.or(box(1, 0, 1, 15, 2, 15), box(3, 2, 3, 13, 14, 13), box(2, 14, 2, 14, 16, 14));

	public MarkedPedestalBlock() {
		super(BlockBehaviour.Properties.of(Material.METAL).strength(15F, 25F).noOcclusion().sound(SoundType.METAL));
	}

	@Override
	public MarkedPedestalBlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return MarkedPedestalBlockEntity.TYPE_INSTANCE.create(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		return BaseBlockEntity::tick;
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		return InteractionResult.PASS;
	}
}

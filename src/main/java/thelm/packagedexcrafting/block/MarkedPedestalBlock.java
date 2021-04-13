package thelm.packagedexcrafting.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import thelm.packagedauto.block.BaseBlock;
import thelm.packagedexcrafting.PackagedExCrafting;
import thelm.packagedexcrafting.tile.MarkedPedestalTile;

public class MarkedPedestalBlock extends BaseBlock {

	public static final MarkedPedestalBlock INSTANCE = new MarkedPedestalBlock();
	public static final Item ITEM_INSTANCE = new BlockItem(INSTANCE, new Item.Properties().group(PackagedExCrafting.ITEM_GROUP)).setRegistryName("packagedexcrafting:marked_pedestal");
	public static final VoxelShape SHAPE = VoxelShapes.or(makeCuboidShape(1, 0, 1, 15, 2, 15), makeCuboidShape(3, 2, 3, 13, 14, 13), makeCuboidShape(2, 14, 2, 14, 16, 14));

	public MarkedPedestalBlock() {
		super(AbstractBlock.Properties.create(Material.IRON).hardnessAndResistance(15F, 25F).notSolid().sound(SoundType.METAL));
		setRegistryName("packagedexcrafting:marked_pedestal");
	}

	@Override
	public MarkedPedestalTile createTileEntity(BlockState state, IBlockReader worldIn) {
		return MarkedPedestalTile.TYPE_INSTANCE.create();
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult rayTraceResult) {
		return ActionResultType.PASS;
	}
}

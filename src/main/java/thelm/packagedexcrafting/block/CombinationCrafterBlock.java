package thelm.packagedexcrafting.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import thelm.packagedauto.block.BaseBlock;
import thelm.packagedexcrafting.PackagedExCrafting;
import thelm.packagedexcrafting.tile.CombinationCrafterTile;

public class CombinationCrafterBlock extends BaseBlock {

	public static final CombinationCrafterBlock INSTANCE = new CombinationCrafterBlock();
	public static final Item ITEM_INSTANCE = new BlockItem(INSTANCE, new Item.Properties().tab(PackagedExCrafting.ITEM_GROUP)).setRegistryName("packagedexcrafting:combination_crafter");
	public static final VoxelShape SHAPE = VoxelShapes.or(box(0, 12, 0, 16, 16, 16), box(0, 5, 0, 16, 11, 16), box(0, 0, 0, 16, 4, 16), box(1, 4, 1, 15, 12, 15));

	public CombinationCrafterBlock() {
		super(AbstractBlock.Properties.of(Material.METAL).strength(15F, 25F).noOcclusion().sound(SoundType.METAL));
		setRegistryName("packagedexcrafting:combination_crafter");
	}

	@Override
	public CombinationCrafterTile createTileEntity(BlockState state, IBlockReader worldIn) {
		return CombinationCrafterTile.TYPE_INSTANCE.create();
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}
}

package thelm.packagedexcrafting.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.world.IBlockReader;
import thelm.packagedauto.block.BaseBlock;
import thelm.packagedexcrafting.PackagedExCrafting;
import thelm.packagedexcrafting.tile.BasicCrafterTile;

public class BasicCrafterBlock extends BaseBlock {

	public static final BasicCrafterBlock INSTANCE = new BasicCrafterBlock();
	public static final Item ITEM_INSTANCE = new BlockItem(INSTANCE, new Item.Properties().tab(PackagedExCrafting.ITEM_GROUP)).setRegistryName("packagedexcrafting:basic_crafter");

	public BasicCrafterBlock() {
		super(AbstractBlock.Properties.of(Material.METAL).strength(15F, 25F).sound(SoundType.METAL));
		setRegistryName("packagedexcrafting:basic_crafter");
	}

	@Override
	public BasicCrafterTile createTileEntity(BlockState state, IBlockReader worldIn) {
		return BasicCrafterTile.TYPE_INSTANCE.create();
	}
}

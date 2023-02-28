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
import thelm.packagedexcrafting.tile.EliteCrafterTile;

public class EliteCrafterBlock extends BaseBlock {

	public static final EliteCrafterBlock INSTANCE = new EliteCrafterBlock();
	public static final Item ITEM_INSTANCE = new BlockItem(INSTANCE, new Item.Properties().tab(PackagedExCrafting.ITEM_GROUP)).setRegistryName("packagedexcrafting:elite_crafter");

	public EliteCrafterBlock() {
		super(AbstractBlock.Properties.of(Material.METAL).strength(15F, 25F).sound(SoundType.METAL));
		setRegistryName("packagedexcrafting:elite_crafter");
	}

	@Override
	public EliteCrafterTile createTileEntity(BlockState state, IBlockReader worldIn) {
		return EliteCrafterTile.TYPE_INSTANCE.create();
	}
}

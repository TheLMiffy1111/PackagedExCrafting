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
import thelm.packagedexcrafting.tile.AdvancedCrafterTile;

public class AdvancedCrafterBlock extends BaseBlock {

	public static final AdvancedCrafterBlock INSTANCE = new AdvancedCrafterBlock();
	public static final Item ITEM_INSTANCE = new BlockItem(INSTANCE, new Item.Properties().tab(PackagedExCrafting.ITEM_GROUP)).setRegistryName("packagedexcrafting:advanced_crafter");

	public AdvancedCrafterBlock() {
		super(AbstractBlock.Properties.of(Material.METAL).strength(15F, 25F).sound(SoundType.METAL));
		setRegistryName("packagedexcrafting:advanced_crafter");
	}

	@Override
	public AdvancedCrafterTile createTileEntity(BlockState state, IBlockReader worldIn) {
		return AdvancedCrafterTile.TYPE_INSTANCE.create();
	}
}

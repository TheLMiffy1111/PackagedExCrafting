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
import thelm.packagedexcrafting.tile.UltimateCrafterTile;

public class UltimateCrafterBlock extends BaseBlock {

	public static final UltimateCrafterBlock INSTANCE = new UltimateCrafterBlock();
	public static final Item ITEM_INSTANCE = new BlockItem(INSTANCE, new Item.Properties().group(PackagedExCrafting.ITEM_GROUP)).setRegistryName("packagedexcrafting:ultimate_crafter");

	public UltimateCrafterBlock() {
		super(AbstractBlock.Properties.create(Material.IRON).hardnessAndResistance(15F, 25F).sound(SoundType.METAL));
		setRegistryName("packagedexcrafting:ultimate_crafter");
	}

	@Override
	public UltimateCrafterTile createTileEntity(BlockState state, IBlockReader worldIn) {
		return UltimateCrafterTile.TYPE_INSTANCE.create();
	}
}

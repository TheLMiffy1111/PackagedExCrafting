package thelm.packagedexcrafting.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thelm.packagedauto.block.BlockBase;
import thelm.packagedauto.tile.TileBase;
import thelm.packagedexcrafting.PackagedExCrafting;
import thelm.packagedexcrafting.tile.TileBasicCrafter;

public class BlockBasicCrafter extends BlockBase {

	public static final BlockBasicCrafter INSTANCE = new BlockBasicCrafter();
	public static final Item ITEM_INSTANCE = new ItemBlock(INSTANCE).setRegistryName("packagedexcrafting:basic_crafter");
	public static final ModelResourceLocation MODEL_LOCATION = new ModelResourceLocation("packagedexcrafting:basic_crafter#normal");

	public BlockBasicCrafter() {
		super(Material.IRON);
		setHardness(15F);
		setResistance(25F);
		setSoundType(SoundType.METAL);
		setTranslationKey("packagedexcrafting.basic_crafter");
		setRegistryName("packagedexcrafting:basic_crafter");
		setCreativeTab(PackagedExCrafting.CREATIVE_TAB);
	}

	@Override
	public TileBase createNewTileEntity(World worldIn, int meta) {
		return new TileBasicCrafter();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(ITEM_INSTANCE, 0, MODEL_LOCATION);
	}
}

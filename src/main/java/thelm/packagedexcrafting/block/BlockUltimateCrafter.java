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
import thelm.packagedexcrafting.tile.TileUltimateCrafter;

public class BlockUltimateCrafter extends BlockBase {

	public static final BlockUltimateCrafter INSTANCE = new BlockUltimateCrafter();
	public static final Item ITEM_INSTANCE = new ItemBlock(INSTANCE).setRegistryName("packagedexcrafting:ultimate_crafter");
	public static final ModelResourceLocation MODEL_LOCATION = new ModelResourceLocation("packagedexcrafting:ultimate_crafter#normal");

	public BlockUltimateCrafter() {
		super(Material.IRON);
		setHardness(15F);
		setResistance(25F);
		setSoundType(SoundType.METAL);
		setTranslationKey("packagedexcrafting.ultimate_crafter");
		setRegistryName("packagedexcrafting:ultimate_crafter");
		setCreativeTab(PackagedExCrafting.CREATIVE_TAB);
	}

	@Override
	public TileBase createNewTileEntity(World worldIn, int meta) {
		return new TileUltimateCrafter();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(ITEM_INSTANCE, 0, MODEL_LOCATION);
	}
}

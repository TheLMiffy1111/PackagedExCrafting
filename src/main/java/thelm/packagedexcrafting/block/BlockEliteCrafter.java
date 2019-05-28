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
import thelm.packagedexcrafting.tile.TileEliteCrafter;

public class BlockEliteCrafter extends BlockBase {

	public static final BlockEliteCrafter INSTANCE = new BlockEliteCrafter();
	public static final Item ITEM_INSTANCE = new ItemBlock(INSTANCE).setRegistryName("packagedexcrafting:elite_crafter");
	public static final ModelResourceLocation MODEL_LOCATION = new ModelResourceLocation("packagedexcrafting:elite_crafter#normal");

	public BlockEliteCrafter() {
		super(Material.IRON);
		setHardness(15F);
		setResistance(25F);
		setSoundType(SoundType.METAL);
		setTranslationKey("packagedexcrafting.elite_crafter");
		setRegistryName("packagedexcrafting:elite_crafter");
		setCreativeTab(PackagedExCrafting.CREATIVE_TAB);
	}

	@Override
	public TileBase createNewTileEntity(World worldIn, int meta) {
		return new TileEliteCrafter();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(ITEM_INSTANCE, 0, MODEL_LOCATION);
	}
}

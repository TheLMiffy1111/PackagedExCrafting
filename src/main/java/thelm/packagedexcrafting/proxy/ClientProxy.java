package thelm.packagedexcrafting.proxy;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import thelm.packagedauto.client.IModelRegister;
import thelm.packagedexcrafting.client.renderer.RendererCombinationCrafter;
import thelm.packagedexcrafting.client.renderer.RendererMarkedPedestal;
import thelm.packagedexcrafting.tile.TileCombinationCrafter;
import thelm.packagedexcrafting.tile.TileMarkedPedestal;

public class ClientProxy extends CommonProxy {

	private static List<IModelRegister> modelRegisterList = new ArrayList<>();

	@Override
	public void registerBlock(Block block) {
		super.registerBlock(block);
		if(block instanceof IModelRegister) {
			modelRegisterList.add((IModelRegister)block);
		}
	}

	@Override
	public void registerItem(Item item) {
		super.registerItem(item);
		if(item instanceof IModelRegister) {
			modelRegisterList.add((IModelRegister)item);
		}
	}

	@Override
	protected void registerModels() {
		for(IModelRegister model : modelRegisterList) {
			model.registerModels();
		}
	}

	@Override
	protected void registerTileEntities() {
		super.registerTileEntities();
		if(TileCombinationCrafter.enabled) {
			ClientRegistry.bindTileEntitySpecialRenderer(TileCombinationCrafter.class, new RendererCombinationCrafter());
			ClientRegistry.bindTileEntitySpecialRenderer(TileMarkedPedestal.class, new RendererMarkedPedestal());
		}
	}
}

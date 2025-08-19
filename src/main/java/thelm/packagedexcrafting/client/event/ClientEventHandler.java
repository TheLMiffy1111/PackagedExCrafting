package thelm.packagedexcrafting.client.event;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import thelm.packagedauto.client.IModelRegister;
import thelm.packagedexcrafting.client.renderer.RendererCombinationCrafter;
import thelm.packagedexcrafting.client.renderer.RendererMarkedPedestal;
import thelm.packagedexcrafting.event.CommonEventHandler;
import thelm.packagedexcrafting.tile.TileCombinationCrafter;
import thelm.packagedexcrafting.tile.TileMarkedPedestal;

public class ClientEventHandler extends CommonEventHandler {

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
	public void onPreInit(FMLPreInitializationEvent event) {
		super.onPreInit(event);
		registerModels();
	}

	@Override
	protected void registerTileEntities() {
		super.registerTileEntities();
		if(TileCombinationCrafter.enabled) {
			ClientRegistry.bindTileEntitySpecialRenderer(TileCombinationCrafter.class, new RendererCombinationCrafter());
			ClientRegistry.bindTileEntitySpecialRenderer(TileMarkedPedestal.class, new RendererMarkedPedestal());
		}
	}

	protected void registerModels() {
		for(IModelRegister model : modelRegisterList) {
			model.registerModels();
		}
	}
}

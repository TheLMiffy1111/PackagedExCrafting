package thelm.packagedexcrafting.integration.patchouli;

import thelm.packagedexcrafting.tile.TileAdvancedCrafter;
import thelm.packagedexcrafting.tile.TileBasicCrafter;
import thelm.packagedexcrafting.tile.TileCombinationCrafter;
import thelm.packagedexcrafting.tile.TileEliteCrafter;
import thelm.packagedexcrafting.tile.TileEnderCrafter;
import thelm.packagedexcrafting.tile.TileUltimateCrafter;
import vazkii.patchouli.api.PatchouliAPI;

public class PackagedExCraftingPatchouliHandler {

	public static void init() {
		PatchouliAPI.instance.setConfigFlag("packagedexcrafting:basic", TileBasicCrafter.enabled);
		PatchouliAPI.instance.setConfigFlag("packagedexcrafting:advanced", TileAdvancedCrafter.enabled);
		PatchouliAPI.instance.setConfigFlag("packagedexcrafting:elite", TileEliteCrafter.enabled);
		PatchouliAPI.instance.setConfigFlag("packagedexcrafting:ultimate", TileUltimateCrafter.enabled);
		PatchouliAPI.instance.setConfigFlag("packagedexcrafting:ender", TileEnderCrafter.enabled);
		PatchouliAPI.instance.setConfigFlag("packagedexcrafting:combination", TileCombinationCrafter.enabled);
	}
}

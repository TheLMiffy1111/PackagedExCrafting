package thelm.packagedexcrafting.config;

import java.io.File;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thelm.packagedexcrafting.tile.TileAdvancedCrafter;
import thelm.packagedexcrafting.tile.TileBasicCrafter;
import thelm.packagedexcrafting.tile.TileCombinationCrafter;
import thelm.packagedexcrafting.tile.TileEliteCrafter;
import thelm.packagedexcrafting.tile.TileEnderCrafter;
import thelm.packagedexcrafting.tile.TileUltimateCrafter;

public class PackagedExCraftingConfig {

	private PackagedExCraftingConfig() {}

	public static Configuration config;

	public static void init(File file) {
		MinecraftForge.EVENT_BUS.register(PackagedExCraftingConfig.class);
		config = new Configuration(file);
		config.load();
		init();
	}

	public static void init() {
		String category;
		category = "blocks.basic_crafter";
		TileBasicCrafter.enabled = config.get(category, "enabled", TileBasicCrafter.enabled, "Should the Basic Package Crafter be enabled.").setRequiresMcRestart(true).getBoolean();
		TileBasicCrafter.energyCapacity = config.get(category, "energy_capacity", TileBasicCrafter.energyCapacity, "How much FE the Basic Package Crafter should hold.", 0, Integer.MAX_VALUE).getInt();
		TileBasicCrafter.energyReq = config.get(category, "energy_req", TileBasicCrafter.energyReq, "How much FE the Basic Package Crafter should use.", 0, Integer.MAX_VALUE).getInt();
		TileBasicCrafter.energyUsage = config.get(category, "energy_usage", TileBasicCrafter.energyUsage, "How much FE/t maximum the Basic Package Crafter should use.", 0, Integer.MAX_VALUE).getInt();
		TileBasicCrafter.drawMEEnergy = config.get(category, "draw_me_energy", TileBasicCrafter.drawMEEnergy, "Should the Basic Packager Crafter draw energy from ME systems.").getBoolean();
		category = "blocks.advanced_crafter";
		TileAdvancedCrafter.enabled = config.get(category, "enabled", TileAdvancedCrafter.enabled, "Should the Advanced Package Crafter be enabled.").setRequiresMcRestart(true).getBoolean();
		TileAdvancedCrafter.energyCapacity = config.get(category, "energy_capacity", TileAdvancedCrafter.energyCapacity, "How much FE the Advanced Package Crafter should hold.", 0, Integer.MAX_VALUE).getInt();
		TileAdvancedCrafter.energyReq = config.get(category, "energy_req", TileAdvancedCrafter.energyReq, "How much FE the Advanced Package Crafter should use.", 0, Integer.MAX_VALUE).getInt();
		TileAdvancedCrafter.energyUsage = config.get(category, "energy_usage", TileAdvancedCrafter.energyUsage, "How much FE/t maximum the Advanced Package Crafter should use.", 0, Integer.MAX_VALUE).getInt();
		TileAdvancedCrafter.drawMEEnergy = config.get(category, "draw_me_energy", TileAdvancedCrafter.drawMEEnergy, "Should the Advanced Packager Crafter draw energy from ME systems.").getBoolean();
		category = "blocks.elite_crafter";
		TileEliteCrafter.enabled = config.get(category, "enabled", TileEliteCrafter.enabled, "Should the Elite Package Crafter be enabled.").setRequiresMcRestart(true).getBoolean();
		TileEliteCrafter.energyCapacity = config.get(category, "energy_capacity", TileEliteCrafter.energyCapacity, "How much FE the Elite Package Crafter should hold.", 0, Integer.MAX_VALUE).getInt();
		TileEliteCrafter.energyReq = config.get(category, "energy_req", TileEliteCrafter.energyReq, "How much FE the Elite Package Crafter should use.", 0, Integer.MAX_VALUE).getInt();
		TileEliteCrafter.energyUsage = config.get(category, "energy_usage", TileEliteCrafter.energyUsage, "How much FE/t maximum the Elite Package Crafter should use.", 0, Integer.MAX_VALUE).getInt();
		TileEliteCrafter.drawMEEnergy = config.get(category, "draw_me_energy", TileEliteCrafter.drawMEEnergy, "Should the Elite Packager Crafter draw energy from ME systems.").getBoolean();
		category = "blocks.ultimate_crafter";
		TileUltimateCrafter.enabled = config.get(category, "enabled", TileUltimateCrafter.enabled, "Should the Ultimate Package Crafter be enabled.").setRequiresMcRestart(true).getBoolean();
		TileUltimateCrafter.energyCapacity = config.get(category, "energy_capacity", TileUltimateCrafter.energyCapacity, "How much FE the Ultimate Package Crafter should hold.", 0, Integer.MAX_VALUE).getInt();
		TileUltimateCrafter.energyReq = config.get(category, "energy_req", TileUltimateCrafter.energyReq, "How much FE the Ultimate Package Crafter should use.", 0, Integer.MAX_VALUE).getInt();
		TileUltimateCrafter.energyUsage = config.get(category, "energy_usage", TileUltimateCrafter.energyUsage, "How much FE/t maximum the Ultimate Package Crafter should use.", 0, Integer.MAX_VALUE).getInt();
		TileUltimateCrafter.drawMEEnergy = config.get(category, "draw_me_energy", TileUltimateCrafter.drawMEEnergy, "Should the Ultimate Packager Crafter draw energy from ME systems.").getBoolean();
		category = "blocks.ender_crafter";
		TileEnderCrafter.enabled = config.get(category, "enabled", TileEnderCrafter.enabled, "Should the Ender Package Crafter be enabled.").setRequiresMcRestart(true).getBoolean();
		TileEnderCrafter.energyCapacity = config.get(category, "energy_capacity", TileEnderCrafter.energyCapacity, "How much FE the Ender Package Crafter should hold.", 0, Integer.MAX_VALUE).getInt();
		TileEnderCrafter.progressReq = config.get(category, "progress_req", TileEnderCrafter.progressReq, "How many ticks should the Ender Package Crafter wait before using energy.", 0, Integer.MAX_VALUE).getInt();
		TileEnderCrafter.alternatorEff = config.get(category, "alternator_eff", TileEnderCrafter.alternatorEff, "How much each alternator should reduce the waiting time of the Ender Package Crafter.", 0, Double.MAX_VALUE).getDouble();
		TileEnderCrafter.energyReq = config.get(category, "energy_req", TileEnderCrafter.energyReq, "How much FE the Ender Package Crafter should use.", 0, Integer.MAX_VALUE).getInt();
		TileEnderCrafter.energyUsage = config.get(category, "energy_usage", TileEnderCrafter.energyUsage, "How much FE/t maximum the Ender Package Crafter should use.", 0, Integer.MAX_VALUE).getInt();
		TileEnderCrafter.drawMEEnergy = config.get(category, "draw_me_energy", TileEnderCrafter.drawMEEnergy, "Should the Ender Packager Crafter draw energy from ME systems.").getBoolean();
		category = "blocks.combination_crafter";
		TileCombinationCrafter.enabled = config.get(category, "enabled", TileCombinationCrafter.enabled, "Should the Combination Package Crafter be enabled.").setRequiresMcRestart(true).getBoolean();
		TileCombinationCrafter.energyCapacity = config.get(category, "energy_capacity", TileCombinationCrafter.energyCapacity, "How much FE the Combination Package Crafter should hold.", 0, Integer.MAX_VALUE).getInt();
		TileCombinationCrafter.drawMEEnergy = config.get(category, "draw_me_energy", TileCombinationCrafter.drawMEEnergy, "Should the Combination Packager Crafter draw energy from ME systems.").getBoolean();
		if(config.hasChanged()) {
			config.save();
		}
	}

	@SubscribeEvent
	public void onConfigChanged(OnConfigChangedEvent event) {
		if(event.getModID().equals("packagedexcrafting")) {
			init();
		}
	}
}

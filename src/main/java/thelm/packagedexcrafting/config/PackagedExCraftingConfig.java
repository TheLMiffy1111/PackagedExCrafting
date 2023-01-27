package thelm.packagedexcrafting.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import thelm.packagedexcrafting.tile.AdvancedCrafterTile;
import thelm.packagedexcrafting.tile.BasicCrafterTile;
import thelm.packagedexcrafting.tile.CombinationCrafterTile;
import thelm.packagedexcrafting.tile.EliteCrafterTile;
import thelm.packagedexcrafting.tile.EnderCrafterTile;
import thelm.packagedexcrafting.tile.UltimateCrafterTile;

public class PackagedExCraftingConfig {

	private PackagedExCraftingConfig() {}

	private static ForgeConfigSpec serverSpec;

	public static ForgeConfigSpec.IntValue basicCrafterEnergyCapacity;
	public static ForgeConfigSpec.IntValue basicCrafterEnergyReq;
	public static ForgeConfigSpec.IntValue basicCrafterEnergyUsage;
	public static ForgeConfigSpec.BooleanValue basicCrafterDrawMEEnergy;

	public static ForgeConfigSpec.IntValue advancedCrafterEnergyCapacity;
	public static ForgeConfigSpec.IntValue advancedCrafterEnergyReq;
	public static ForgeConfigSpec.IntValue advancedCrafterEnergyUsage;
	public static ForgeConfigSpec.BooleanValue advancedCrafterDrawMEEnergy;

	public static ForgeConfigSpec.IntValue eliteCrafterEnergyCapacity;
	public static ForgeConfigSpec.IntValue eliteCrafterEnergyReq;
	public static ForgeConfigSpec.IntValue eliteCrafterEnergyUsage;
	public static ForgeConfigSpec.BooleanValue eliteCrafterDrawMEEnergy;

	public static ForgeConfigSpec.IntValue ultimateCrafterEnergyCapacity;
	public static ForgeConfigSpec.IntValue ultimateCrafterEnergyReq;
	public static ForgeConfigSpec.IntValue ultimateCrafterEnergyUsage;
	public static ForgeConfigSpec.BooleanValue ultimateCrafterDrawMEEnergy;

	public static ForgeConfigSpec.IntValue enderCrafterEnergyCapacity;
	public static ForgeConfigSpec.DoubleValue enderCrafterAlternatorEff;
	public static ForgeConfigSpec.IntValue enderCrafterEnergyReq;
	public static ForgeConfigSpec.IntValue enderCrafterEnergyUsage;
	public static ForgeConfigSpec.BooleanValue enderCrafterDrawMEEnergy;

	public static ForgeConfigSpec.IntValue combinationCrafterEnergyCapacity;
	public static ForgeConfigSpec.BooleanValue combinationCrafterDrawMEEnergy;

	public static void registerConfig() {
		buildConfig();
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, serverSpec);
	}

	private static void buildConfig() {
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

		builder.push("basic_crafter");
		builder.comment("How much FE the Basic Package Crafter should hold.");
		basicCrafterEnergyCapacity = builder.defineInRange("energy_capacity", 5000, 0, Integer.MAX_VALUE);
		builder.comment("How much total FE the Basic Package Crafter should use per operation.");
		basicCrafterEnergyReq = builder.defineInRange("energy_req", 500, 0, Integer.MAX_VALUE);
		builder.comment("How much FE/t maximum the Basic Package Crafter can use.");
		basicCrafterEnergyUsage = builder.defineInRange("energy_usage", 100, 0, Integer.MAX_VALUE);
		builder.comment("Should the Basic Package Crafter draw energy from ME systems.");
		basicCrafterDrawMEEnergy = builder.define("draw_me_energy", true);
		builder.pop();

		builder.push("advanced_crafter");
		builder.comment("How much FE the Advanced Package Crafter should hold.");
		advancedCrafterEnergyCapacity = builder.defineInRange("energy_capacity", 5000, 0, Integer.MAX_VALUE);
		builder.comment("How much total FE the Advanced Package Crafter should use per operation.");
		advancedCrafterEnergyReq = builder.defineInRange("energy_req", 1000, 0, Integer.MAX_VALUE);
		builder.comment("How much FE/t maximum the Advanced Package Crafter can use.");
		advancedCrafterEnergyUsage = builder.defineInRange("energy_usage", 125, 0, Integer.MAX_VALUE);
		builder.comment("Should the Advanced Package Crafter draw energy from ME systems.");
		advancedCrafterDrawMEEnergy = builder.define("draw_me_energy", true);
		builder.pop();

		builder.push("elite_crafter");
		builder.comment("How much FE the Elite Package Crafter should hold.");
		eliteCrafterEnergyCapacity = builder.defineInRange("energy_capacity", 5000, 0, Integer.MAX_VALUE);
		builder.comment("How much total FE the Elite Package Crafter should use per operation.");
		eliteCrafterEnergyReq = builder.defineInRange("energy_req", 2500, 0, Integer.MAX_VALUE);
		builder.comment("How much FE/t maximum the Elite Package Crafter can use.");
		eliteCrafterEnergyUsage = builder.defineInRange("energy_usage", 250, 0, Integer.MAX_VALUE);
		builder.comment("Should the Elite Package Crafter draw energy from ME systems.");
		eliteCrafterDrawMEEnergy = builder.define("draw_me_energy", true);
		builder.pop();

		builder.push("ultimate_crafter");
		builder.comment("How much FE the Ultimate Package Crafter should hold.");
		ultimateCrafterEnergyCapacity = builder.defineInRange("energy_capacity", 5000, 0, Integer.MAX_VALUE);
		builder.comment("How much total FE the Ultimate Package Crafter should use per operation.");
		ultimateCrafterEnergyReq = builder.defineInRange("energy_req", 5000, 0, Integer.MAX_VALUE);
		builder.comment("How much FE/t maximum the Ultimate Package Crafter can use.");
		ultimateCrafterEnergyUsage = builder.defineInRange("energy_usage", 500, 0, Integer.MAX_VALUE);
		builder.comment("Should the Ultimate Package Crafter draw energy from ME systems.");
		ultimateCrafterDrawMEEnergy = builder.define("draw_me_energy", true);
		builder.pop();

		builder.push("ender_crafter");
		builder.comment("How much FE the Ender Package Crafter should hold.");
		enderCrafterEnergyCapacity = builder.defineInRange("energy_capacity", 5000, 0, Integer.MAX_VALUE);
		builder.comment("How much each alternator should reduce the waiting time of the Ender Package Crafter. This is a percentage of the time required.");
		enderCrafterAlternatorEff = builder.defineInRange("alternator_eff", 0.02, 0, Double.MAX_VALUE);
		builder.comment("How much total FE the Ender Package Crafter should use per operation.");
		enderCrafterEnergyReq = builder.defineInRange("energy_req", 500, 0, Integer.MAX_VALUE);
		builder.comment("How much FE/t maximum the Ender Package Crafter can use.");
		enderCrafterEnergyUsage = builder.defineInRange("energy_usage", 100, 0, Integer.MAX_VALUE);
		builder.comment("Should the Ender Package Crafter draw energy from ME systems.");
		enderCrafterDrawMEEnergy = builder.define("draw_me_energy", true);
		builder.pop();

		builder.push("combination_crafter");
		builder.comment("How much FE the Combination Package Crafter should hold.");
		combinationCrafterEnergyCapacity = builder.defineInRange("energy_capacity", 5000000, 0, Integer.MAX_VALUE);
		builder.comment("Should the Combination Package Crafter draw energy from ME systems.");
		combinationCrafterDrawMEEnergy = builder.define("draw_me_energy", false);
		builder.pop();

		serverSpec = builder.build();
	}

	public static void reloadServerConfig() {
		BasicCrafterTile.energyCapacity = basicCrafterEnergyCapacity.get();
		BasicCrafterTile.energyReq = basicCrafterEnergyReq.get();
		BasicCrafterTile.energyUsage = basicCrafterEnergyUsage.get();
		BasicCrafterTile.drawMEEnergy = basicCrafterDrawMEEnergy.get();

		AdvancedCrafterTile.energyCapacity = advancedCrafterEnergyCapacity.get();
		AdvancedCrafterTile.energyReq = advancedCrafterEnergyReq.get();
		AdvancedCrafterTile.energyUsage = advancedCrafterEnergyUsage.get();
		AdvancedCrafterTile.drawMEEnergy = advancedCrafterDrawMEEnergy.get();

		EliteCrafterTile.energyCapacity = eliteCrafterEnergyCapacity.get();
		EliteCrafterTile.energyReq = eliteCrafterEnergyReq.get();
		EliteCrafterTile.energyUsage = eliteCrafterEnergyUsage.get();
		EliteCrafterTile.drawMEEnergy = eliteCrafterDrawMEEnergy.get();

		UltimateCrafterTile.energyCapacity = ultimateCrafterEnergyCapacity.get();
		UltimateCrafterTile.energyReq = ultimateCrafterEnergyReq.get();
		UltimateCrafterTile.energyUsage = ultimateCrafterEnergyUsage.get();
		UltimateCrafterTile.drawMEEnergy = ultimateCrafterDrawMEEnergy.get();

		EnderCrafterTile.energyCapacity = enderCrafterEnergyCapacity.get();
		EnderCrafterTile.alternatorEff = enderCrafterAlternatorEff.get();
		EnderCrafterTile.energyReq = enderCrafterEnergyReq.get();
		EnderCrafterTile.energyUsage = enderCrafterEnergyUsage.get();
		EnderCrafterTile.drawMEEnergy = enderCrafterDrawMEEnergy.get();

		CombinationCrafterTile.energyCapacity = combinationCrafterEnergyCapacity.get();
		CombinationCrafterTile.drawMEEnergy = combinationCrafterDrawMEEnergy.get();
	}
}

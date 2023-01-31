package thelm.packagedexcrafting.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import thelm.packagedexcrafting.block.entity.AdvancedCrafterBlockEntity;
import thelm.packagedexcrafting.block.entity.BasicCrafterBlockEntity;
import thelm.packagedexcrafting.block.entity.CombinationCrafterBlockEntity;
import thelm.packagedexcrafting.block.entity.EliteCrafterBlockEntity;
import thelm.packagedexcrafting.block.entity.EnderCrafterBlockEntity;
import thelm.packagedexcrafting.block.entity.FluxCrafterBlockEntity;
import thelm.packagedexcrafting.block.entity.UltimateCrafterBlockEntity;

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

	public static ForgeConfigSpec.IntValue fluxCrafterEnergyCapacity;
	public static ForgeConfigSpec.IntValue fluxCrafterEnergyReq;
	public static ForgeConfigSpec.IntValue fluxCrafterEnergyUsage;
	public static ForgeConfigSpec.BooleanValue fluxCrafterDrawMEEnergy;

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

		builder.push("flux_crafter");
		builder.comment("How much FE the Flux Package Crafter should hold.");
		fluxCrafterEnergyCapacity = builder.defineInRange("energy_capacity", 5000, 0, Integer.MAX_VALUE);
		builder.comment("How much total FE the Flux Package Crafter should use per operation.");
		fluxCrafterEnergyReq = builder.defineInRange("energy_req", 500, 0, Integer.MAX_VALUE);
		builder.comment("How much FE/t maximum the Flux Package Crafter can use.");
		fluxCrafterEnergyUsage = builder.defineInRange("energy_usage", 100, 0, Integer.MAX_VALUE);
		builder.comment("Should the Flux Package Crafter draw energy from ME systems.");
		fluxCrafterDrawMEEnergy = builder.define("draw_me_energy", false);

		builder.push("combination_crafter");
		builder.comment("How much FE the Combination Package Crafter should hold.");
		combinationCrafterEnergyCapacity = builder.defineInRange("energy_capacity", 5000000, 0, Integer.MAX_VALUE);
		builder.comment("Should the Combination Package Crafter draw energy from ME systems.");
		combinationCrafterDrawMEEnergy = builder.define("draw_me_energy", false);
		builder.pop();

		serverSpec = builder.build();
	}

	public static void reloadServerConfig() {
		BasicCrafterBlockEntity.energyCapacity = basicCrafterEnergyCapacity.get();
		BasicCrafterBlockEntity.energyReq = basicCrafterEnergyReq.get();
		BasicCrafterBlockEntity.energyUsage = basicCrafterEnergyUsage.get();
		BasicCrafterBlockEntity.drawMEEnergy = basicCrafterDrawMEEnergy.get();

		AdvancedCrafterBlockEntity.energyCapacity = advancedCrafterEnergyCapacity.get();
		AdvancedCrafterBlockEntity.energyReq = advancedCrafterEnergyReq.get();
		AdvancedCrafterBlockEntity.energyUsage = advancedCrafterEnergyUsage.get();
		AdvancedCrafterBlockEntity.drawMEEnergy = advancedCrafterDrawMEEnergy.get();

		EliteCrafterBlockEntity.energyCapacity = eliteCrafterEnergyCapacity.get();
		EliteCrafterBlockEntity.energyReq = eliteCrafterEnergyReq.get();
		EliteCrafterBlockEntity.energyUsage = eliteCrafterEnergyUsage.get();
		EliteCrafterBlockEntity.drawMEEnergy = eliteCrafterDrawMEEnergy.get();

		UltimateCrafterBlockEntity.energyCapacity = ultimateCrafterEnergyCapacity.get();
		UltimateCrafterBlockEntity.energyReq = ultimateCrafterEnergyReq.get();
		UltimateCrafterBlockEntity.energyUsage = ultimateCrafterEnergyUsage.get();
		UltimateCrafterBlockEntity.drawMEEnergy = ultimateCrafterDrawMEEnergy.get();

		EnderCrafterBlockEntity.energyCapacity = enderCrafterEnergyCapacity.get();
		EnderCrafterBlockEntity.alternatorEff = enderCrafterAlternatorEff.get();
		EnderCrafterBlockEntity.energyReq = enderCrafterEnergyReq.get();
		EnderCrafterBlockEntity.energyUsage = enderCrafterEnergyUsage.get();
		EnderCrafterBlockEntity.drawMEEnergy = enderCrafterDrawMEEnergy.get();

		FluxCrafterBlockEntity.energyCapacity = fluxCrafterEnergyCapacity.get();
		FluxCrafterBlockEntity.energyReq = fluxCrafterEnergyReq.get();
		FluxCrafterBlockEntity.energyUsage = fluxCrafterEnergyUsage.get();
		FluxCrafterBlockEntity.drawMEEnergy = fluxCrafterDrawMEEnergy.get();

		CombinationCrafterBlockEntity.energyCapacity = combinationCrafterEnergyCapacity.get();
		CombinationCrafterBlockEntity.drawMEEnergy = combinationCrafterDrawMEEnergy.get();
	}
}

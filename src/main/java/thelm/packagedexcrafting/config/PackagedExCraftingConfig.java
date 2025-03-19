package thelm.packagedexcrafting.config;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import thelm.packagedexcrafting.block.entity.AdvancedCrafterBlockEntity;
import thelm.packagedexcrafting.block.entity.BasicCrafterBlockEntity;
import thelm.packagedexcrafting.block.entity.CombinationCrafterBlockEntity;
import thelm.packagedexcrafting.block.entity.EliteCrafterBlockEntity;
import thelm.packagedexcrafting.block.entity.EnderCrafterBlockEntity;
import thelm.packagedexcrafting.block.entity.FluxCrafterBlockEntity;
import thelm.packagedexcrafting.block.entity.UltimateCrafterBlockEntity;

public class PackagedExCraftingConfig {

	private PackagedExCraftingConfig() {}

	private static ModConfigSpec serverSpec;

	public static ModConfigSpec.IntValue basicCrafterEnergyCapacity;
	public static ModConfigSpec.IntValue basicCrafterEnergyReq;
	public static ModConfigSpec.IntValue basicCrafterEnergyUsage;
	public static ModConfigSpec.BooleanValue basicCrafterDrawMEEnergy;

	public static ModConfigSpec.IntValue advancedCrafterEnergyCapacity;
	public static ModConfigSpec.IntValue advancedCrafterEnergyReq;
	public static ModConfigSpec.IntValue advancedCrafterEnergyUsage;
	public static ModConfigSpec.BooleanValue advancedCrafterDrawMEEnergy;

	public static ModConfigSpec.IntValue eliteCrafterEnergyCapacity;
	public static ModConfigSpec.IntValue eliteCrafterEnergyReq;
	public static ModConfigSpec.IntValue eliteCrafterEnergyUsage;
	public static ModConfigSpec.BooleanValue eliteCrafterDrawMEEnergy;

	public static ModConfigSpec.IntValue ultimateCrafterEnergyCapacity;
	public static ModConfigSpec.IntValue ultimateCrafterEnergyReq;
	public static ModConfigSpec.IntValue ultimateCrafterEnergyUsage;
	public static ModConfigSpec.BooleanValue ultimateCrafterDrawMEEnergy;

	public static ModConfigSpec.IntValue enderCrafterEnergyCapacity;
	public static ModConfigSpec.DoubleValue enderCrafterAlternatorEff;
	public static ModConfigSpec.IntValue enderCrafterEnergyReq;
	public static ModConfigSpec.IntValue enderCrafterEnergyUsage;
	public static ModConfigSpec.BooleanValue enderCrafterDrawMEEnergy;

	public static ModConfigSpec.IntValue fluxCrafterEnergyCapacity;
	public static ModConfigSpec.IntValue fluxCrafterEnergyReq;
	public static ModConfigSpec.IntValue fluxCrafterEnergyUsage;
	public static ModConfigSpec.BooleanValue fluxCrafterDrawMEEnergy;

	public static ModConfigSpec.IntValue combinationCrafterEnergyCapacity;
	public static ModConfigSpec.BooleanValue combinationCrafterDrawMEEnergy;

	public static void registerConfig(ModContainer modContainer) {
		buildConfig();
		modContainer.registerConfig(ModConfig.Type.SERVER, serverSpec);
	}

	private static void buildConfig() {
		ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

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

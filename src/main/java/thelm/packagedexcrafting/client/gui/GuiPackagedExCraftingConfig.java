package thelm.packagedexcrafting.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import thelm.packagedexcrafting.config.PackagedExCraftingConfig;

public class GuiPackagedExCraftingConfig extends GuiConfig {

	public GuiPackagedExCraftingConfig(GuiScreen parent) {
		super(parent, getConfigElements(), "packagedexcrafting", false, false, getAbridgedConfigPath(PackagedExCraftingConfig.config.toString()));
	}

	private static List<IConfigElement> getConfigElements() {
		ArrayList<IConfigElement> list = new ArrayList<>();
		for(String category : PackagedExCraftingConfig.config.getCategoryNames()) {
			list.add(new ConfigElement(PackagedExCraftingConfig.config.getCategory(category)));
		}
		return list;
	}
}

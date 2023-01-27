package thelm.packagedexcrafting;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import thelm.packagedexcrafting.client.event.ClientEventHandler;
import thelm.packagedexcrafting.event.CommonEventHandler;

@Mod(PackagedExCrafting.MOD_ID)
public class PackagedExCrafting {

	public static final String MOD_ID = "packagedexcrafting";
	public static PackagedExCrafting core;

	public PackagedExCrafting() {
		core = this;
		CommonEventHandler.getInstance().onConstruct();
		DistExecutor.runWhenOn(Dist.CLIENT, ()->()->{
			ClientEventHandler.getInstance().onConstruct();
		});
	}
}

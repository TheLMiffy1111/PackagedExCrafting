package thelm.packagedexcrafting;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import thelm.packagedexcrafting.block.UltimateCrafterBlock;
import thelm.packagedexcrafting.client.event.ClientEventHandler;
import thelm.packagedexcrafting.event.CommonEventHandler;

@Mod(PackagedExCrafting.MOD_ID)
public class PackagedExCrafting {

	public static final String MOD_ID = "packagedexcrafting";
	public static final CreativeModeTab CREATIVE_TAB = new CreativeModeTab("packagedexcrafting") {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(UltimateCrafterBlock.INSTANCE);
		}
	};

	public PackagedExCrafting() {
		CommonEventHandler.getInstance().onConstruct();
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, ()->()->{
			ClientEventHandler.getInstance().onConstruct();
		});
	}
}

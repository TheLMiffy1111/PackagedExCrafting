package thelm.packagedexcrafting.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import com.blakebr0.extendedcrafting.compat.jei.combinationcrafting.CombinationCraftingCategory;
import com.blakebr0.extendedcrafting.compat.jei.endercrafter.EnderCrafterCategory;
import com.blakebr0.extendedcrafting.compat.jei.tablecrafting.AdvancedTableCategory;
import com.blakebr0.extendedcrafting.compat.jei.tablecrafting.BasicTableCategory;
import com.blakebr0.extendedcrafting.compat.jei.tablecrafting.EliteTableCategory;
import com.blakebr0.extendedcrafting.compat.jei.tablecrafting.UltimateTableCategory;
import net.minecraft.item.ItemStack;
import thelm.packagedexcrafting.block.BlockAdvancedCrafter;
import thelm.packagedexcrafting.block.BlockBasicCrafter;
import thelm.packagedexcrafting.block.BlockCombinationCrafter;
import thelm.packagedexcrafting.block.BlockEliteCrafter;
import thelm.packagedexcrafting.block.BlockEnderCrafter;
import thelm.packagedexcrafting.block.BlockMarkedPedestal;
import thelm.packagedexcrafting.block.BlockUltimateCrafter;
import thelm.packagedexcrafting.tile.TileAdvancedCrafter;
import thelm.packagedexcrafting.tile.TileBasicCrafter;
import thelm.packagedexcrafting.tile.TileCombinationCrafter;
import thelm.packagedexcrafting.tile.TileEliteCrafter;
import thelm.packagedexcrafting.tile.TileEnderCrafter;
import thelm.packagedexcrafting.tile.TileUltimateCrafter;

@JEIPlugin
public final class CompatJEI implements IModPlugin {
    @Override
    public void register(IModRegistry registry) {
        if(TileBasicCrafter.enabled) {
            registry.addRecipeCatalyst(new ItemStack(BlockBasicCrafter.INSTANCE), BasicTableCategory.UID);
        }
        if(TileAdvancedCrafter.enabled) {
            registry.addRecipeCatalyst(new ItemStack(BlockAdvancedCrafter.INSTANCE), AdvancedTableCategory.UID);
        }
        if(TileEliteCrafter.enabled) {
            registry.addRecipeCatalyst(new ItemStack(BlockEliteCrafter.INSTANCE), EliteTableCategory.UID);
        }
        if(TileUltimateCrafter.enabled) {
            registry.addRecipeCatalyst(new ItemStack(BlockUltimateCrafter.INSTANCE), UltimateTableCategory.UID);
        }
        if(TileEnderCrafter.enabled) {
            registry.addRecipeCatalyst(new ItemStack(BlockEnderCrafter.INSTANCE), EnderCrafterCategory.UID);
        }
        if(TileCombinationCrafter.enabled) {
            registry.addRecipeCatalyst(new ItemStack(BlockCombinationCrafter.INSTANCE), CombinationCraftingCategory.UID);
            registry.addRecipeCatalyst(new ItemStack(BlockMarkedPedestal.INSTANCE), CombinationCraftingCategory.UID);
        }
    }
}

package thelm.packagedexcrafting.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import thelm.packagedauto.block.BaseBlock;
import thelm.packagedauto.block.entity.BaseBlockEntity;
import thelm.packagedexcrafting.block.entity.EnderCrafterBlockEntity;
import thelm.packagedexcrafting.block.entity.PackagedExCraftingBlockEntities;

public class EnderCrafterBlock extends BaseBlock {

	protected EnderCrafterBlock() {
		super(BlockBehaviour.Properties.of().strength(10F, 15F).mapColor(MapColor.METAL).sound(SoundType.METAL));
	}

	@Override
	public EnderCrafterBlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return PackagedExCraftingBlockEntities.ENDER_CRAFTER.get().create(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		return BaseBlockEntity::tick;
	}
}

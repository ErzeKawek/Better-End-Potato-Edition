package mod.beethoven92.betterendforge.mixin.common;

import mod.beethoven92.betterendforge.common.init.ModBlocks;
import mod.beethoven92.betterendforge.common.init.ModTags;
import mod.beethoven92.betterendforge.common.util.BlockHelper;
import mod.beethoven92.betterendforge.common.world.generator.GeneratorOptions;
import net.minecraft.block.*;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nonnull;
import java.util.Random;

@Mixin(value = {ChorusFlowerBlock.class}, priority = 100)
public abstract class ChorusFlowerBlockMixin extends Block {
	@Shadow
	@Final
	private ChorusPlantBlock plantBlock;

	@Unique
	private static final VoxelShape SHAPE_FULL = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);

	@Unique
	private static final VoxelShape SHAPE_HALF = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D);

	@Shadow
	protected abstract void placeGrownFlower(World paramWorld, BlockPos paramBlockPos, int paramInt);

	public ChorusFlowerBlockMixin(AbstractBlock.Properties properties) {
		super(properties);
	}

	@Inject(method = {"isValidPosition"}, at = {@At("HEAD")}, cancellable = true)
	private void onCheckPos(BlockState state, IWorldReader world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		if (world.getBlockState(pos.down()).isIn(ModBlocks.CHORUS_NYLIUM.get())) {
			cir.setReturnValue(Boolean.TRUE);
		}
	}

	@Inject(method = {"randomTick"}, at = {@At("HEAD")}, cancellable = true)
	private void onTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
		if (world.getBlockState(pos.down()).isIn(ModTags.END_GROUND)) {
			BlockPos up = pos.up();
			if (world.isAirBlock(up) && up.getY() < 256) {
				int i = state.get(ChorusFlowerBlock.AGE);
				if (i < 5) {
					this.placeGrownFlower(world, up, i + 1);
					if (GeneratorOptions.changeChorusPlant()) {
						BlockHelper.setWithoutUpdate(world, pos, this.plantBlock.getDefaultState().with(ChorusPlantBlock.UP, Boolean.TRUE).with(ChorusPlantBlock.DOWN, Boolean.TRUE).with(BlockHelper.ROOTS, Boolean.TRUE));
					} else {
						BlockHelper.setWithoutUpdate(world, pos, this.plantBlock.getDefaultState().with(ChorusPlantBlock.UP, Boolean.TRUE).with(ChorusPlantBlock.DOWN, Boolean.TRUE));
					}
					ci.cancel();
				}
			}
		}
	}

	@Inject(method = {"generatePlant"}, at = {@At("RETURN")})
	private static void onGen(IWorld world, BlockPos pos, Random random, int size, CallbackInfo ci) {
		BlockState state = world.getBlockState(pos);
		if (GeneratorOptions.changeChorusPlant() && state.isIn(Blocks.CHORUS_PLANT))
			BlockHelper.setWithoutUpdate(world, pos, state.with(BlockHelper.ROOTS, Boolean.TRUE));
	}

	@Nonnull
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos, @Nonnull ISelectionContext context) {
		if (GeneratorOptions.changeChorusPlant()) return (state.get(ChorusFlowerBlock.AGE) == 5) ? SHAPE_HALF : SHAPE_FULL;
		return super.getShape(state, world, pos, context);
	}

	@Inject(method = {"placeDeadFlower"}, at = {@At("HEAD")}, cancellable = true)
	private void onDead(World world, BlockPos pos, CallbackInfo ci) {
		BlockState down = world.getBlockState(pos.down());
		if (down.isIn(Blocks.CHORUS_PLANT) || down.isIn(ModTags.GEN_TERRAIN)) {
			world.setBlockState(pos, getDefaultState().with(BlockStateProperties.AGE_0_5, 5), 2);
			world.playEvent(1034, pos, 0);
		}
		ci.cancel();
	}
}

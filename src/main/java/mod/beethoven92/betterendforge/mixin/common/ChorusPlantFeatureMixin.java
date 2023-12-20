package mod.beethoven92.betterendforge.mixin.common;

import mod.beethoven92.betterendforge.common.init.ModBlocks;
import mod.beethoven92.betterendforge.common.util.BlockHelper;
import mod.beethoven92.betterendforge.common.util.ModMathHelper;
import mod.beethoven92.betterendforge.common.world.generator.GeneratorOptions;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChorusFlowerBlock;
import net.minecraft.block.SixWayBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.ChorusPlantFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin({ChorusPlantFeature.class})
public abstract class ChorusPlantFeatureMixin {
	@Inject(method = {"generate(Lnet/minecraft/world/ISeedReader;Lnet/minecraft/world/gen/ChunkGenerator;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/gen/feature/NoFeatureConfig;)Z"}, at = {@At("HEAD")}, cancellable = true)
	private void be_generate(ISeedReader worldIn, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, NoFeatureConfig config, CallbackInfoReturnable<Boolean> info) {
		if (worldIn.isAirBlock(blockPos) && worldIn.getBlockState(blockPos.down()).isIn(ModBlocks.CHORUS_NYLIUM.get())) {
			ChorusFlowerBlock.generatePlant(worldIn, blockPos, random, ModMathHelper.randRange(8, 16, random));
			BlockState bottom = worldIn.getBlockState(blockPos);
			if (bottom.isIn(Blocks.CHORUS_PLANT))
				if (GeneratorOptions.changeChorusPlant()) {
					BlockHelper.setWithoutUpdate(worldIn, blockPos, bottom.with(BlockHelper.ROOTS, Boolean.TRUE).with(SixWayBlock.DOWN, Boolean.TRUE));
				} else {
					BlockHelper.setWithoutUpdate(worldIn, blockPos, bottom.with(SixWayBlock.DOWN, Boolean.TRUE));
				}
			info.setReturnValue(Boolean.TRUE);
		}
	}
}


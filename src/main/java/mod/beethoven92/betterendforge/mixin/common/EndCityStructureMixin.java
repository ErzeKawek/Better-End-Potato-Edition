package mod.beethoven92.betterendforge.mixin.common;

import mod.beethoven92.betterendforge.common.world.generator.GeneratorOptions;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.EndCityStructure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndCityStructure.class)
public abstract class EndCityStructureMixin 
{
	@Inject(method = "func_230363_a_(Lnet/minecraft/world/gen/ChunkGenerator;Lnet/minecraft/world/biome/provider/BiomeProvider;JLnet/minecraft/util/SharedSeedRandom;IILnet/minecraft/world/biome/Biome;Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/world/gen/feature/NoFeatureConfig;)Z", at = @At("HEAD"), cancellable = true)
	private void be_shouldStartAt(ChunkGenerator chunkGenerator, BiomeProvider biomeSource, long l, 
			SharedSeedRandom chunkRandom, int i, int j, Biome biome, ChunkPos chunkPos,
			NoFeatureConfig config, CallbackInfoReturnable<Boolean> info) 
	{
		if (GeneratorOptions.useNewGenerator())
		{
			int chance = GeneratorOptions.getEndCityFailChance();
			if (chance == 0) 
			{
				info.setReturnValue(getYPosForStructure(i, j, chunkGenerator) >= 60);
				info.cancel();
			}
			else if (chunkRandom.nextInt(chance) == 0)
			{
				info.setReturnValue(getYPosForStructure(i, j, chunkGenerator) >= 60);
				info.cancel();
			}
			else 
			{
				info.setReturnValue(false);
				info.cancel();
			}
		}
	}
	
	@Shadow
	private static int getYPosForStructure(int chunkX, int chunkY, ChunkGenerator generatorIn)
	{
		return 0;
	}
}

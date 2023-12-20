package mod.beethoven92.betterendforge.mixin.client;

import mod.beethoven92.betterendforge.client.ClientOptions;
import mod.beethoven92.betterendforge.common.init.ModBlocks;
import mod.beethoven92.betterendforge.common.util.ModMathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;

@Mixin(BiomeColors.class)
public class BiomeColorsMixin {
	@Unique
    private static final int POISON_COLOR = ModMathHelper.color(92, 160, 78);
	@Unique
    private static final int STREAM_COLOR = ModMathHelper.color(105, 213, 244);
	@Unique
    private static final Point[] OFFSETS;
	@Unique
    private static final boolean HAS_MAGNESIUM;

	@Inject(method = "getWaterColor", at = @At("RETURN"), cancellable = true)
	private static void be_getWaterColor(IBlockDisplayReader world, BlockPos blockPos, CallbackInfoReturnable<Integer> info) {
		if (ClientOptions.useSulfurWaterColor()) {
			IBlockDisplayReader view = HAS_MAGNESIUM ? Minecraft.getInstance().world : world;
			Mutable mut = new Mutable();
			mut.setY(blockPos.getY());
			for (int i = 0; i < OFFSETS.length; i++) {
				mut.setX(blockPos.getX() + OFFSETS[i].x);
				mut.setZ(blockPos.getZ() + OFFSETS[i].y);
				if ((view.getBlockState(mut).isIn(ModBlocks.BRIMSTONE.get()))) {
					info.setReturnValue(i < 4 ? POISON_COLOR : STREAM_COLOR);
					return;
				}
			}
		}

	}

	//Magnesium Compat
	static {
		HAS_MAGNESIUM = ModList.get().isLoaded("magnesium");

		int index = 0;
		OFFSETS = new Point[20];
		for (int x = -2; x < 3; x++) {
			for (int z = -2; z < 3; z++) {
				if ((x != 0 || z != 0) && (Math.abs(x) != 2 || Math.abs(z) != 2)) {
					OFFSETS[index++] = new Point(x, z);
				}
			}
		}
		Arrays.sort(OFFSETS, Comparator.comparingInt(pos -> ModMathHelper.sqr(pos.x) + ModMathHelper.sqr(pos.y)));
	}

}

package mod.beethoven92.betterendforge.mixin.client;

import mod.beethoven92.betterendforge.BetterEnd;
import mod.beethoven92.betterendforge.common.world.generator.GeneratorOptions;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ModelBakery.class})
public abstract class ModelBakeryMixin {
	@Redirect(method = {"loadBlockstate"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ResourceLocation;getPath()Ljava/lang/String;"))
	private String be_switchModel(ResourceLocation id) {
		String originalPath = id.getPath();
		if (GeneratorOptions.changeChorusPlant() && be_shouldChangeModel(id)) return BetterEnd.makeID(originalPath.replace("chorus", "custom_chorus")).getPath();
		return originalPath;
	}

	@Unique
	private boolean be_shouldChangeModel(ResourceLocation id) {
		return (id.getNamespace().equals("minecraft") && id.getPath().startsWith("blockstates/") && id.getPath().contains("chorus") && !id.getPath().contains("custom_"));
	}
}

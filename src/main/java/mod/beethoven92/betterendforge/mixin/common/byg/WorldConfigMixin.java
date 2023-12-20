package mod.beethoven92.betterendforge.mixin.common.byg;

import corgiaoc.byg.config.WorldConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = {WorldConfig.class}, remap = false)
public abstract class WorldConfigMixin {
    @ModifyArg(method = {"<init>"}, at = @At(value = "INVOKE", ordinal = 1, target = "Lcorgiaoc/byg/config/AbstractCommentedConfigHelper;add(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;"))
    private Object modifyControlEnd(Object obj) {
        return Boolean.FALSE;
    }
}
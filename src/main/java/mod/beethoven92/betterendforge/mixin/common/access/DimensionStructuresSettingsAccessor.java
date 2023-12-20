package mod.beethoven92.betterendforge.mixin.common.access;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(DimensionStructuresSettings.class)
public interface DimensionStructuresSettingsAccessor {
    @Mutable
    @Accessor("field_236193_d_")
    void field_236193_d_(Map<Structure<?>, StructureSeparationSettings> map);

    @Mutable
    @Accessor("field_236191_b_")
    static void field_236191_b_(ImmutableMap<Structure<?>, StructureSeparationSettings> map) {
        throw new RuntimeException();
    }
}

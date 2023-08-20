package io.github.eggohito.advancement_macros.mixin.impl;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.criterion.Criterion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Criterion.ConditionsContainer.class)
public interface ConditionsContainerAccessor {

    @Accessor
    String getId();

    @Accessor
    Advancement getAdvancement();

}

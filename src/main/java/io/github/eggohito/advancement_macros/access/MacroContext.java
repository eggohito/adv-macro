package io.github.eggohito.advancement_macros.access;

import io.github.eggohito.advancement_macros.data.TriggerContext;
import net.minecraft.advancement.criterion.Criterion;

import java.util.function.Consumer;

public interface MacroContext {
    void advancement_macros$setContext(Criterion<?> criterion, Consumer<TriggerContext> contextConsumer);
}

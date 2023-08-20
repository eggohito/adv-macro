package io.github.eggohito.advancement_macros.event;

import io.github.eggohito.advancement_macros.AdvancementMacros;
import io.github.eggohito.advancement_macros.util.TriggerContext;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.nbt.NbtCompound;

/**
 *      <p>An event callback called <b>before</b> the function reward of an advancement is granted. This is for writing
 *      NBT data of custom criterion triggers (also known as advancement triggers )that will be passed
 *      to the function reward of an advancement.</p>
 *
 *      <p>See also: {@link TriggerContext}</p>
 */
public interface CriteriaTriggerCallback {

    Event<CriteriaTriggerCallback> EVENT = EventFactory.createWithPhases(
        CriteriaTriggerCallback.class,
        listeners -> (context, nbtToWriteTo) -> {
            for (CriteriaTriggerCallback listener : listeners) {
                listener.writeToNbt(context, nbtToWriteTo);
            }
        },
        AdvancementMacros.VANILLA_PHASE,
        Event.DEFAULT_PHASE
    );

    void writeToNbt(TriggerContext context, NbtCompound nbtToWriteTo);

}

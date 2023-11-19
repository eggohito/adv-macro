package io.github.eggohito.advancement_macros.data;

import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 *      A class for storing the context (e.g: objects) of criterion triggers when triggered. This is used to store the
 *      objects at a later time to serialize to NBT via a {@link io.github.eggohito.advancement_macros.api.Macro}
 */
@SuppressWarnings({"unchecked", "unused"})
public class TriggerContext {

    private final Map<String, Object> mappedData;
    private final Identifier id;

    public TriggerContext(Identifier id) {
        this.id = id;
        this.mappedData = new HashMap<>();
    }

    /**
     *      @return The {@linkplain Identifier ID} of the criterion trigger this trigger context is associated with.
     */
    public Identifier getId() {
        return id;
    }

    /**
     *      <p>Add an {@linkplain Object object} mapped to a {@linkplain String string} to this trigger context. This
     *      is useful for distinguishing between objects of the same type.</p>
     *
     *      @param name     The name for the {@linkplain Object object} to add to this trigger context.
     *      @param data     The {@linkplain Object object} to add to this trigger context.
     */
    public TriggerContext add(String name, Object data) {

        if (data != null) {
            this.mappedData.put(name, data);
        }

        return this;

    }

    /**
     *      @return     The {@linkplain Map map} of {@linkplain String string} and {@linkplain Object objects}
     *                  that are added to this trigger context.
     */
    public Map<String, Object> getAll() {
        return mappedData;
    }

    /**
     *      @return     {@code true} if this trigger context does not contain any mappings.
     */
    public boolean isEmpty() {
        return mappedData.isEmpty();
    }

    /**
     *      <p>Get the {@link Object object} from the specified {@linkplain String string} from this trigger
     *      context.</p>
     *
     *      @param name                 The key to get the object from.
     *      @param <T>                  The type of the object.
     *      @return                     The object from the specified name.
     *      @throws RuntimeException    if this trigger context does not contain the specified key.
     */
    public <T> T get(String name) throws RuntimeException {

        if (!mappedData.containsKey(name)) {
            throw new RuntimeException("Tried getting field \"" + name + "\" from trigger context of criterion trigger \"" + id +"\", which didn't exist!");
        }

        return (T) mappedData.get(name);

    }

    /**
     *      <p>Get the {@link Object object} from the specified {@linkplain String string} from this trigger context and wrap it
     *      in an {@link Optional}.</p>
     *
     *      @param name     The key to get the object from.
     *      @param <T>      The type of the object.
     *      @return         The object from the specified name or {@link Optional#empty()} if there is no mapping for the specified key.
     */
    public <T> Optional<T> getOptional(String name) {
        return Optional.ofNullable((T) mappedData.get(name));
    }

    /**
     *      <p>Check if the specified {@linkplain String string} has an associated value in this trigger context.</p>
     *
     *      @param name         The name to check if it has an associated value.
     *      @return             {@code true} if the specified name has an associated value.
     */
    public boolean isPresent(String name) {
        return mappedData.containsKey(name);
    }

    /**
     *      <p>If the specified {@linkplain String string} has an associated {@linkplain Object} in this trigger
     *      context, perform the specified {@linkplain Consumer action} to the associated
     *      {@linkplain Object object} of the specified {@linkplain String string}</p>
     *
     *      @param name         The name to check and process the associated object of.
     *      @param action       The process to perform on the associated object.
     */
    public <T> void ifPresent(String name, Consumer<T> action) {
        if (isPresent(name)) {
            action.accept(get(name));
        }
    }

}

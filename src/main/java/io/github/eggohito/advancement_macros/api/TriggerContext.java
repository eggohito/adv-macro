package io.github.eggohito.advancement_macros.api;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *      A class for storing the context (e.g: objects) of criterion triggers when triggered. This is used to store the
 *      objects at a later time to serialize to NBT (serialization to NBT is done separately)
 */
public class TriggerContext {

    private final List<Object> data;
    private final Map<String, Object> mappedData;

    private final Identifier id;

    public static TriggerContext create(Identifier id) {
        return new TriggerContext(id);
    }

    private TriggerContext(Identifier id) {
        this.id = id;
        this.data = new LinkedList<>();
        this.mappedData = new HashMap<>();
    }

    /**
     *      @return The {@linkplain Identifier ID} of the criterion trigger this trigger context associates with.
     */
    public Identifier getId() {
        return id;
    }

    /**
     *      <p>Add an {@linkplain Object object} to the trigger context. If multiple objects of the same type
     *      needs to be added to the trigger context, use {@link #addMappedData(String, Object)} instead.</p>
     *
     *      @param data     The {@linkplain Object object} to add to the trigger context.
     */
    public TriggerContext addData(Object data) {
        this.data.add(data);
        return this;
    }

    /**
     *      <p>Same as {@link #addData(Object)}, except the object is mapped to a {@linkplain String string}. This
     *      is useful for distinguishing between objects of the same type.</p>
     *
     *      @param name     The name for the {@linkplain Object object} to add to the trigger context.
     *      @param data     The {@linkplain Object object} to add to the trigger context.
     */
    public TriggerContext addMappedData(String name, Object data) {
        this.mappedData.put(name, data);
        return this;
    }

    /**
     *      @return     The {@linkplain LinkedList list} of {@linkplain Object objects} that are added
     *                  to this trigger context.
     */
    public List<Object> getData() {
        return data;
    }

    /**
     *      @return     The {@linkplain HashMap map} of {@linkplain String string} and {@linkplain Object objects}
     *                  that are added to this trigger context.
     */
    public Map<String, Object> getMappedData() {
        return mappedData;
    }

    /**
     *      @return     {@code true} if the trigger context does not contain any objects (or mapped objects)
     */
    public boolean isEmpty() {
        return data.isEmpty() && mappedData.isEmpty();
    }

    /**
     *      <p>Serializes the objects (or mapped objects) of the trigger context into the passed
     *      {@linkplain NbtCompound NBT} using the passed {@linkplain Macro macro}.</p>
     *
     *      @param nbt      The {@linkplain NbtCompound NBT} to serialize the objects (or mapped objects) to.
     *      @param macro    The {@linkplain Macro macro} to use for serializing objects.
     */
    public void process(NbtCompound nbt, Macro macro) {
        data.removeIf(obj -> {
            macro.writeToNbt(nbt, obj);
            return true;
        });
        mappedData.entrySet().removeIf(entry -> {
            macro.writeToNbt(nbt, entry.getKey(), entry.getValue());
            return true;
        });
    }

}

package io.github.eggohito.advancement_macros.util;

import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *      <p>A class for containing the criterion trigger (also known as an advancement trigger) and its data to be
 *      written to NBT and passed to a {@linkplain net.minecraft.server.function.CommandFunction function}. The data
 *      are stored as {@linkplain Object objects}, which can be added using {@link #addData(Object data)}. It can also
 *      be mapped with a key using {@link #addMappedData(String key, Object data)}.</p>
 */
public final class TriggerContext {

    private final Criterion<?> criterion;

    private final List<Object> data;
    private final Map<String, Object> mappedData;

    public TriggerContext(Criterion<?> criterion) {
        this.criterion = criterion;
        this.data = new LinkedList<>();
        this.mappedData = new HashMap<>();
    }

    public Criterion<?> getCriterion() {
        return criterion;
    }

    public Identifier getId() {
        return criterion.getId();
    }

    /**
     *      Add data to the context.
     *      @param data     The data to be added to the trigger context.
     */
    public TriggerContext addData(Object data) {
        this.data.add(data);
        return this;
    }

    /**
     *      Add data mapped to a certain string key.
     *      @param key      The key to map the data to.
     *      @param data     The data to be added to the trigger context.
     */
    public TriggerContext addMappedData(String key, Object data) {
        this.mappedData.put(key, data);
        return this;
    }

    public List<Object> getData() {
        return data;
    }

    public Map<String, Object> getMappedData() {
        return mappedData;
    }

}

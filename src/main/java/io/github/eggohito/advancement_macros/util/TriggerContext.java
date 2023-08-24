package io.github.eggohito.advancement_macros.util;

import io.github.eggohito.advancement_macros.macro.Macro;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

    public Identifier getId() {
        return id;
    }

    public TriggerContext addData(Object data) {
        this.data.add(data);
        return this;
    }

    public TriggerContext addMappedData(String name, Object data) {
        this.mappedData.put(name, data);
        return this;
    }

    public List<Object> getData() {
        return data;
    }

    public Map<String, Object> getMappedData() {
        return mappedData;
    }

    public boolean isEmpty() {
        return data.isEmpty() && mappedData.isEmpty();
    }

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

package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import io.github.eggohito.advancement_macros.util.NbtUtil;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.function.BiFunction;

public class ThrownItemPickedUpByEntityCriterionMacro extends Macro {

    public static final String THROWN_ITEM_KEY_FIELD = "thrown_item_key";
    public static final String ENTITY_KEY_FIELD = "entity_key";

    public static final Codec<ThrownItemPickedUpByEntityCriterionMacro> CODEC = getCodec(ThrownItemPickedUpByEntityCriterionMacro::new);

    private final String thrownItemKey;
    private final String entityKey;

    public ThrownItemPickedUpByEntityCriterionMacro(Identifier id, String thrownItemKey, String entityKey) {
        super(id);
        this.thrownItemKey = thrownItemKey;
        this.entityKey = entityKey;
    }

    public ThrownItemPickedUpByEntityCriterionMacro(String thrownItemKey, String entityKey) {
        this(Criteria.THROWN_ITEM_PICKED_UP_BY_ENTITY.getId(), thrownItemKey, entityKey);
    }

    protected static Codec<ThrownItemPickedUpByEntityCriterionMacro> getCodec(BiFunction<String, String, ThrownItemPickedUpByEntityCriterionMacro> macroFunction) {
        return RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.optionalFieldOf(THROWN_ITEM_KEY_FIELD, "thrown_item").forGetter(ThrownItemPickedUpByEntityCriterionMacro::getThrownItemKey),
            Codec.STRING.optionalFieldOf(ENTITY_KEY_FIELD, "entity").forGetter(ThrownItemPickedUpByEntityCriterionMacro::getEntityKey)
        ).apply(instance, macroFunction));
    }

    public String getThrownItemKey() {
        return thrownItemKey;
    }

    public String getEntityKey() {
        return entityKey;
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {

        context.<ItemStack>ifPresent(THROWN_ITEM_KEY_FIELD, thrownItemStack ->
            NbtUtil.writeItemStackToNbt(rootNbt, thrownItemKey, thrownItemStack)
        );

        context.<Entity>ifPresent(ENTITY_KEY_FIELD, entity ->
            rootNbt.putString(entityKey, entity.getUuidAsString())
        );

    }

    public static Pair<Identifier, Type> getFactory() {
        return new Pair<>(Criteria.THROWN_ITEM_PICKED_UP_BY_ENTITY.getId(), () -> CODEC);
    }

}

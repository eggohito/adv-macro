package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import io.github.eggohito.advancement_macros.util.NbtUtil;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Pair;

import java.util.function.BiFunction;

public class ThrownItemPickedUpByEntityCriterionMacro extends Macro {

    public static final String ITEM_KEY = "item";
    public static final String ENTITY_KEY = "entity";

    public static final Codec<ThrownItemPickedUpByEntityCriterionMacro> CODEC = createCodec(ThrownItemPickedUpByEntityCriterionMacro::new);

    private final String itemKey;
    private final String entityKey;

    public ThrownItemPickedUpByEntityCriterionMacro(Criterion<?> baseCriterion, String itemKey, String entityKey) {
        super(baseCriterion);
        this.itemKey = itemKey;
        this.entityKey = entityKey;
    }

    public ThrownItemPickedUpByEntityCriterionMacro(String itemKey, String entityKey) {
        this(Criteria.THROWN_ITEM_PICKED_UP_BY_ENTITY, itemKey, entityKey);
    }

    protected static <T extends ThrownItemPickedUpByEntityCriterionMacro> Codec<T> createCodec(BiFunction<String, String, T> macroFunction) {
        return RecordCodecBuilder.create(instance -> instance.group(
            strictOptionalField(ITEM_KEY, ITEM_KEY).forGetter(ThrownItemPickedUpByEntityCriterionMacro::getItemKey),
            strictOptionalField(ENTITY_KEY, ENTITY_KEY).forGetter(ThrownItemPickedUpByEntityCriterionMacro::getEntityKey)
        ).apply(instance, macroFunction));
    }

    public String getItemKey() {
        return itemKey;
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

        context.<ItemStack>ifPresent(ITEM_KEY, thrownItemStack ->
            NbtUtil.writeItemStackToNbt(rootNbt, itemKey, thrownItemStack)
        );

        context.<Entity>ifPresent(ENTITY_KEY, entity ->
            rootNbt.putString(entityKey, entity.getUuidAsString())
        );

    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.THROWN_ITEM_PICKED_UP_BY_ENTITY, () -> CODEC);
    }

}

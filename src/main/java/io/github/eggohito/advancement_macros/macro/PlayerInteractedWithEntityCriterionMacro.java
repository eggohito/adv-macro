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
import net.minecraft.util.Pair;

public class PlayerInteractedWithEntityCriterionMacro extends Macro {

    public static final String USED_ITEM_KEY_FIELD = "used_item_key";
    public static final String INTERACTED_ENTITY_KEY_FIELD = "interacted_entity_key";

    public static Codec<PlayerInteractedWithEntityCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        strictOptionalField(USED_ITEM_KEY_FIELD, "used_item").forGetter(PlayerInteractedWithEntityCriterionMacro::getUsedItemKey),
        strictOptionalField(INTERACTED_ENTITY_KEY_FIELD, "interacted_entity").forGetter(PlayerInteractedWithEntityCriterionMacro::getInteractedEntityKey)
    ).apply(instance, PlayerInteractedWithEntityCriterionMacro::new));

    private final String usedItemKey;
    private final String interactedEntityKey;

    public PlayerInteractedWithEntityCriterionMacro(String usedItemKey, String interactedEntityKey) {
        super(Criteria.PLAYER_INTERACTED_WITH_ENTITY);
        this.usedItemKey = usedItemKey;
        this.interactedEntityKey = interactedEntityKey;
    }

    public String getUsedItemKey() {
        return usedItemKey;
    }

    public String getInteractedEntityKey() {
        return interactedEntityKey;
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {

        context.<ItemStack>ifPresent(USED_ITEM_KEY_FIELD, usedItemStack ->
            NbtUtil.writeItemStackToNbt(rootNbt, usedItemKey, usedItemStack)
        );

        context.<Entity>ifPresent(INTERACTED_ENTITY_KEY_FIELD, interactedEntity ->
            rootNbt.putString(interactedEntityKey, interactedEntity.getUuidAsString())
        );

    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.PLAYER_INTERACTED_WITH_ENTITY, () -> CODEC);
    }

}

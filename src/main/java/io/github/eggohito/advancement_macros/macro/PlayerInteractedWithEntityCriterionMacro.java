package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.util.NbtUtil;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

public class PlayerInteractedWithEntityCriterionMacro extends Macro {

    public static Codec<PlayerInteractedWithEntityCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf("item_key", "item").forGetter(PlayerInteractedWithEntityCriterionMacro::getItemKey),
        Codec.STRING.optionalFieldOf("interacted_entity_key", "interacted_entity").forGetter(PlayerInteractedWithEntityCriterionMacro::getInteractedEntityKey)
    ).apply(instance, PlayerInteractedWithEntityCriterionMacro::new));

    private final String itemKey;
    private final String interactedEntityKey;

    public PlayerInteractedWithEntityCriterionMacro(String itemKey, String interactedEntityKey) {
        super(Criteria.PLAYER_INTERACTED_WITH_ENTITY.getId());
        this.itemKey = itemKey;
        this.interactedEntityKey = interactedEntityKey;
    }

    public String getItemKey() {
        return itemKey;
    }

    public String getInteractedEntityKey() {
        return interactedEntityKey;
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, Object object) {

        if (object instanceof ItemStack stack) {
            NbtUtil.writeItemStackToNbt(rootNbt, itemKey, stack);
        }

        if (object instanceof Entity interactedEntity) {
            rootNbt.putString(interactedEntityKey, interactedEntity.getUuidAsString());
        }

    }

    public static Pair<Identifier, Type> getFactory() {
        return new Pair<>(Criteria.PLAYER_INTERACTED_WITH_ENTITY.getId(), () -> CODEC);
    }

}

package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

public class PlayerGeneratesContainerLootCriterionMacro extends Macro {

    public static final String LOOT_TABLE_KEY = "loot_table";

    public static final Codec<PlayerGeneratesContainerLootCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        strictOptionalField(LOOT_TABLE_KEY, LOOT_TABLE_KEY).forGetter(PlayerGeneratesContainerLootCriterionMacro::getLootTableKey)
    ).apply(instance, PlayerGeneratesContainerLootCriterionMacro::new));

    private final String lootTableKey;

    public PlayerGeneratesContainerLootCriterionMacro(String lootTableIdKey) {
        this.lootTableKey = lootTableIdKey;
    }

    public String getLootTableKey() {
        return lootTableKey;
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {
        context.<Identifier>ifPresent(LOOT_TABLE_KEY, lootTableId ->
            rootNbt.putString(lootTableKey, lootTableId.toString())
        );
    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.PLAYER_GENERATES_CONTAINER_LOOT, () -> CODEC);
    }

}

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

    public static final String LOOT_TABLE_ID_KEY_FIELD = "loot_table_id_key";

    public static final Codec<PlayerGeneratesContainerLootCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf(LOOT_TABLE_ID_KEY_FIELD, "loot_table_id").forGetter(PlayerGeneratesContainerLootCriterionMacro::getLootTableIdKey)
    ).apply(instance, PlayerGeneratesContainerLootCriterionMacro::new));

    private final String lootTableIdKey;

    public PlayerGeneratesContainerLootCriterionMacro(String lootTableIdKey) {
        super(Criteria.PLAYER_GENERATES_CONTAINER_LOOT);
        this.lootTableIdKey = lootTableIdKey;
    }

    public String getLootTableIdKey() {
        return lootTableIdKey;
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {
        context.<Identifier>ifPresent(LOOT_TABLE_ID_KEY_FIELD, lootTableId ->
            rootNbt.putString(lootTableIdKey, lootTableId.toString())
        );
    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.PLAYER_GENERATES_CONTAINER_LOOT, () -> CODEC);
    }

}

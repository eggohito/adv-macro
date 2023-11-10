package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Pair;

public class CuredZombieVillagerCriterionMacro extends Macro {

    public static final String ZOMBIE_KEY_FIELD = "zombie_key";
    public static final String VILLAGER_KEY_FIELD = "villager_key";

    public static final Codec<CuredZombieVillagerCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf(ZOMBIE_KEY_FIELD, "zombie").forGetter(CuredZombieVillagerCriterionMacro::getZombieKey),
        Codec.STRING.optionalFieldOf(VILLAGER_KEY_FIELD, "villager").forGetter(CuredZombieVillagerCriterionMacro::getVillagerKey)
    ).apply(instance, CuredZombieVillagerCriterionMacro::new));

    private final String zombieKey;
    private final String villagerKey;

    public CuredZombieVillagerCriterionMacro(String zombieKey, String villagerKey) {
        super(Criteria.CURED_ZOMBIE_VILLAGER);
        this.zombieKey = zombieKey;
        this.villagerKey = villagerKey;
    }

    public String getZombieKey() {
        return zombieKey;
    }

    public String getVillagerKey() {
        return villagerKey;
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {

        context.<ZombieEntity>ifPresent(ZOMBIE_KEY_FIELD, zombieEntity ->
            rootNbt.putString(zombieKey, zombieEntity.getUuidAsString())
        );

        context.<VillagerEntity>ifPresent(VILLAGER_KEY_FIELD, villagerEntity ->
            rootNbt.putString(villagerKey, villagerEntity.getUuidAsString())
        );

    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.CURED_ZOMBIE_VILLAGER, () -> CODEC);
    }

}

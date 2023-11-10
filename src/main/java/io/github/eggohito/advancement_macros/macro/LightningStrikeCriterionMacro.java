package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Pair;

import java.util.List;

public class LightningStrikeCriterionMacro extends Macro {

    public static final String LIGHTNING_KEY_FIELD = "lightning_key";
    public static final String BYSTANDERS_KEY_FIELD = "bystanders_key";

    public static final Codec<LightningStrikeCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf(LIGHTNING_KEY_FIELD, "lightning").forGetter(LightningStrikeCriterionMacro::getLightningKey),
        Codec.STRING.optionalFieldOf(BYSTANDERS_KEY_FIELD, "bystanders").forGetter(LightningStrikeCriterionMacro::getBystandersKey)
    ).apply(instance, LightningStrikeCriterionMacro::new));

    private final String lightningKey;
    private final String bystandersKey;

    public LightningStrikeCriterionMacro(String lightningKey, String bystandersKey) {
        super(Criteria.LIGHTNING_STRIKE);
        this.lightningKey = lightningKey;
        this.bystandersKey = bystandersKey;
    }

    public String getLightningKey() {
        return lightningKey;
    }

    public String getBystandersKey() {
        return bystandersKey;
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {

        context.<LightningEntity>ifPresent(LIGHTNING_KEY_FIELD, lightningEntity ->
            rootNbt.putString(lightningKey, lightningEntity.getUuidAsString())
        );

        context.<List<Entity>>ifPresent(BYSTANDERS_KEY_FIELD, bystanders -> {

            NbtList bystandersNbt = new NbtList();
            for (Entity bystander : bystanders) {
                bystandersNbt.add(NbtString.of(bystander.getUuidAsString()));
            }

            rootNbt.put(bystandersKey, bystandersNbt);

        });

    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.LIGHTNING_STRIKE, () -> CODEC);
    }

}

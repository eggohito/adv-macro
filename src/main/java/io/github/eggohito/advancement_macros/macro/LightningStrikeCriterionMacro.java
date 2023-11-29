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

    public static final String LIGHTNING_KEY = "lightning";
    public static final String BYSTANDERS_KEY = "bystanders";

    public static final Codec<LightningStrikeCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        strictOptionalField(LIGHTNING_KEY, LIGHTNING_KEY).forGetter(LightningStrikeCriterionMacro::getLightningKey),
        strictOptionalField(BYSTANDERS_KEY, BYSTANDERS_KEY).forGetter(LightningStrikeCriterionMacro::getBystandersKey)
    ).apply(instance, LightningStrikeCriterionMacro::new));

    private final String lightningKey;
    private final String bystandersKey;

    public LightningStrikeCriterionMacro(String lightningKey, String bystandersKey) {
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

        context.<LightningEntity>ifPresent(LIGHTNING_KEY, lightningEntity ->
            rootNbt.putString(lightningKey, lightningEntity.getUuidAsString())
        );

        context.<List<Entity>>ifPresent(BYSTANDERS_KEY, bystanders -> {

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

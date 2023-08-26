package io.github.eggohito.advancement_macros.mixin.impl;

import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import io.github.eggohito.advancement_macros.AdvancementMacros;
import io.github.eggohito.advancement_macros.access.MacroStorage;
import io.github.eggohito.advancement_macros.macro.Macro;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.util.JsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@SuppressWarnings("unused")
@Mixin(AdvancementCriterion.class)
public abstract class AdvancementCriterionMixin implements MacroStorage {

    @Unique
    private Macro advancement_macros$macro;

    @Override
    public Macro advancement_macros$getMacro() {
        return advancement_macros$macro;
    }

    @Override
    public void advancement_macros$setMacro(Macro newMacro) {
        advancement_macros$macro = newMacro;
    }

    @ModifyReturnValue(method = "fromJson", at = @At("RETURN"))
    private static AdvancementCriterion advancement_macros$getMappings(AdvancementCriterion original, JsonObject jsonObject, AdvancementEntityPredicateDeserializer predicateDeserializer, @Local CriterionConditions criterionConditions) {

        //  Get the JSON object from the `advancement-macros:mapping` field of the JSON object
        JsonObject mappingObj = JsonHelper.getObject(jsonObject, AdvancementMacros.of("mapping").toString(), new JsonObject());
        mappingObj.add("trigger", jsonObject.get("trigger"));

        //  Get the codec for the macro registry
        Codec<Macro> macroCodec = AdvancementMacros.REGISTRY
            .getCodec()
            .dispatch("trigger", Macro::getType, Macro.Type::getCodec);

        //  Deserialize the macro from the JSON object from the `advancement-macros:mapping` field and attach it to the
        //  advancement criterion
        return macroCodec.decode(JsonOps.INSTANCE, mappingObj)
            .result()
            .map(pair -> {

                AdvancementCriterion criterion = new AdvancementCriterion(criterionConditions);
                ((MacroStorage) criterion).advancement_macros$setMacro(pair.getFirst());

                return criterion;

            })
            .orElse(original);

    }

}

package io.github.eggohito.advancement_macros.mixin.impl;

import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import io.github.eggohito.advancement_macros.AdvancementMacros;
import io.github.eggohito.advancement_macros.access.MacroStorage;
import io.github.eggohito.advancement_macros.api.Macro;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.util.JsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

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

    @ModifyReturnValue(method = "fromJson(Lcom/google/gson/JsonObject;Lnet/minecraft/predicate/entity/AdvancementEntityPredicateDeserializer;Lnet/minecraft/advancement/criterion/Criterion;)Lnet/minecraft/advancement/AdvancementCriterion;", at = @At("RETURN"))
    private static <T extends CriterionConditions> AdvancementCriterion<T> advancement_macros$attachMappings(AdvancementCriterion<T> original, JsonObject jsonObject, AdvancementEntityPredicateDeserializer predicateDeserializer, Criterion<T> criterionTrigger) {

        String criterionTriggerIdString = JsonHelper.getString(jsonObject, "trigger");

        //  Get the JSON object from the `advancement-macros:mapping` field of the JSON object
        JsonObject mappingJsonObject = JsonHelper.getObject(jsonObject, AdvancementMacros.MAPPING_KEY, new JsonObject());
        mappingJsonObject.addProperty(AdvancementMacros.CODEC_TYPE_KEY, criterionTriggerIdString);

        //  Dispatch the proper codec for the specified criterion trigger from the macro registry
        Codec<Macro> macroCodec = AdvancementMacros.REGISTRY
            .getCodec()
            .dispatch(AdvancementMacros.CODEC_TYPE_KEY, Macro::getType, Macro.Type::getCodec);

        //  Deserialize the macro from the mapping JSON object and attach it to the criterion
        var macro = macroCodec
            .decode(JsonOps.INSTANCE, mappingJsonObject)
            .resultOrPartial(AdvancementMacros::logErrorOnce);

        macro.ifPresent(macroAndJsonElement -> ((MacroStorage) (Object) original).advancement_macros$setMacro(macroAndJsonElement.getFirst()));
        return original;

    }

}

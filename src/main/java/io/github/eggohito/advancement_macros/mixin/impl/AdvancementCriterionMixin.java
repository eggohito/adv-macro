package io.github.eggohito.advancement_macros.mixin.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.JsonOps;
import io.github.eggohito.advancement_macros.AdvancementMacros;
import io.github.eggohito.advancement_macros.access.MacroStorage;
import io.github.eggohito.advancement_macros.api.Macro;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

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

    @WrapOperation(method = "criteriaFromJson", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/AdvancementCriterion;fromJson(Lcom/google/gson/JsonObject;Lnet/minecraft/predicate/entity/AdvancementEntityPredicateDeserializer;)Lnet/minecraft/advancement/AdvancementCriterion;"))
    private static AdvancementCriterion<?> advancement_macros$serializeMacro(JsonObject jsonObject, AdvancementEntityPredicateDeserializer predicateDeserializer, Operation<AdvancementCriterion<?>> original, @Local Map.Entry<String, JsonElement> criterionEntry) {

        //  Get the criterion and its name
        String criterionName = criterionEntry.getKey();
        AdvancementCriterion<?> advancementCriterion = original.call(jsonObject, predicateDeserializer);

        //  Get the ID of the criterion's trigger. If it's null (it shouldn't be), skip the method handler by this point
        Identifier criterionTriggerId = Criteria.getId(advancementCriterion.trigger());
        if (criterionTriggerId == null) {
            return advancementCriterion;
        }

        //  Get the JSON object from the `advancement-macros:mapping` field of the criterion's JSON object
        JsonObject macroJsonObject = JsonHelper.getObject(jsonObject, AdvancementMacros.MAPPING_KEY, new JsonObject());
        macroJsonObject.addProperty(AdvancementMacros.CODEC_TYPE_KEY, criterionTriggerId.toString());

        //  Dispatch the codec for the specified criterion trigger and serialize the JSOn object from the
        //  `advancement-macros:mapping` field of the criterion's JSON object into a macro handler
        AdvancementMacros.REGISTRY
            .getCodec()
            .dispatch(AdvancementMacros.CODEC_TYPE_KEY, Macro::getType, Macro.Type::getCodec)
            .decode(JsonOps.INSTANCE, macroJsonObject)
            .resultOrPartial(s -> AdvancementMacros.LOGGER.error("Error trying to serialize macro handler for criterion \"{}\" of advancement \"{}\": {}", criterionName, predicateDeserializer.getAdvancementId(), s))
            .ifPresent(macroAndJsonElement -> ((MacroStorage) (Object) advancementCriterion).advancement_macros$setMacro(macroAndJsonElement.getFirst()));

        return advancementCriterion;

    }

}

package io.github.eggohito.advancement_macros.mixin.impl;

import com.google.gson.JsonObject;
import io.github.eggohito.advancement_macros.AdvancementMacros;
import io.github.eggohito.advancement_macros.access.AdvancementData;
import io.github.eggohito.advancement_macros.util.PassOrder;
import net.minecraft.advancement.Advancement;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.util.JsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("DataFlowIssue")
@Mixin(Advancement.class)
public abstract class AdvancementMixin implements AdvancementData {

    @Unique
    private PassOrder advancement_macros$passOrder = PassOrder.NONE;

    @Override
    public void advancement_macros$setPassOrder(PassOrder passOrder) {
        advancement_macros$passOrder = passOrder;
    }

    @Override
    public PassOrder advancement_macros$getPassOrder() {
        return advancement_macros$passOrder;
    }

    @Inject(method = "fromJson", at = @At("RETURN"))
    private static void advancement_macros$serializePassOrder(JsonObject jsonObject, AdvancementEntityPredicateDeserializer predicateDeserializer, CallbackInfoReturnable<Advancement> cir) {

        //  Skip this method handler if the advancement doesn't have a `rewards` JSON object specified
        if (!(jsonObject.get("rewards") instanceof JsonObject rewardsJsonObject)) {
            return;
        }

        //  Try serializing the `advancement-macros:pass_order` field from the `rewards` JSON object of
        //  the advancement and use it as the pass order for the advancement
        try {

            String passOrderName = JsonHelper.getString(rewardsJsonObject, AdvancementMacros.PASS_ORDER_KEY, PassOrder.LAST.name());
            PassOrder passOrder = PassOrder.fromName(passOrderName);

            ((AdvancementData) (Object) cir.getReturnValue()).advancement_macros$setPassOrder(passOrder);

        } catch (Exception e) {
            AdvancementMacros.LOGGER.error("Error trying to serialize \"{}\" field from the \"rewards\" JSON object field of advancement \"{}\": {}", AdvancementMacros.PASS_ORDER_KEY, predicateDeserializer.getAdvancementId(), e.getMessage());
        }

    }

}

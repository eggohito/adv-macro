package io.github.eggohito.advancement_macros.macro;

import net.minecraft.util.Identifier;

public abstract class OnKilledCriterionMacro extends Macro {

    private final String killerKey;
    private final String victimKey;
    private final String killingBlowKey;

    public OnKilledCriterionMacro(Identifier id, String killerKey, String victimKey, String killingBlowKey) {
        super(id);
        this.killerKey = killerKey;
        this.victimKey = victimKey;
        this.killingBlowKey = killingBlowKey;
    }

    public String getKillerKey() {
        return killerKey;
    }

    public String getVictimKey() {
        return victimKey;
    }

    public String getKillingBlowKey() {
        return killingBlowKey;
    }

}

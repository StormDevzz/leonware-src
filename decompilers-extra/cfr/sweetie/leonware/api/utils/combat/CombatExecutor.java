/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1309
 */
package sweetie.leonware.api.utils.combat;

import java.util.List;
import lombok.Generated;
import net.minecraft.class_1309;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.utils.combat.CombatManager;
import sweetie.leonware.api.utils.rotation.manager.Rotation;

public class CombatExecutor {
    private final CombatManager combatManager = new CombatManager();
    private final MultiBooleanSetting options = new MultiBooleanSetting("Options").value(new BooleanSetting[]{new BooleanSetting("Only crits").value(true), new BooleanSetting("Smart crits").value(true).setVisible(() -> this.options().isEnabled("Only crits")), new BooleanSetting("Raytrace").value(true), new BooleanSetting("Shield break").value(true), new BooleanSetting("Unpress shield").value(false), new BooleanSetting("Ignore walls").value(false), new BooleanSetting("Through walls RW").value(false).setVisible(() -> this.options().isEnabled("Ignore walls")), new BooleanSetting("Ignore entity").value(false), new BooleanSetting("No attack if eat").value(false)});

    public void performAttack() {
        this.combatManager.handleAttack();
    }

    @Generated
    public CombatManager combatManager() {
        return this.combatManager;
    }

    @Generated
    public MultiBooleanSetting options() {
        return this.options;
    }

    public static class CombatConfigurable {
        public final class_1309 target;
        public final Rotation rotation;
        public final float distance;
        public final boolean onlyCrits;
        public final boolean smartCrits;
        public final boolean raytrace;
        public final boolean shieldBreak;
        public final boolean unpressShield;
        public final boolean ignoreWalls;
        public final boolean throughWallsRW;
        public final boolean ignoreEntity;
        public final boolean noAttackIfEat;
        public boolean autoMace = false;
        public int maceDelay = 0;
        public float minFallDistance = 3.0f;
        public boolean maceOnlyPlayers = true;
        public boolean swapElytraForMace = false;

        public CombatConfigurable(class_1309 target, Rotation rotation, float distance, boolean onlyCrits, boolean smartCrits, boolean raytrace, boolean shieldBreak, boolean unpressShield, boolean ignoreWalls, boolean throughWallsRW, boolean ignoreEntity, boolean noAttackIfEat) {
            this.target = target;
            this.rotation = rotation;
            this.distance = distance;
            this.onlyCrits = onlyCrits;
            this.smartCrits = smartCrits;
            this.raytrace = raytrace;
            this.shieldBreak = shieldBreak;
            this.unpressShield = unpressShield;
            this.ignoreWalls = ignoreWalls;
            this.throughWallsRW = throughWallsRW;
            this.ignoreEntity = ignoreEntity;
            this.noAttackIfEat = noAttackIfEat;
        }

        public CombatConfigurable(class_1309 target, Rotation rotation, float distance, List<String> options) {
            this(target, rotation, distance, options.contains("Only crits"), options.contains("Smart crits"), options.contains("Raytrace"), options.contains("Shield break"), options.contains("Unpress shield"), options.contains("Ignore walls"), options.contains("Through walls RW"), options.contains("Ignore entity"), options.contains("No attack if eat"));
        }

        public CombatConfigurable withMace(boolean enabled, int delay, float fallDist, boolean onlyPlayers, boolean swapElytra) {
            this.autoMace = enabled;
            this.maceDelay = delay;
            this.minFallDistance = fallDist;
            this.maceOnlyPlayers = onlyPlayers;
            this.swapElytraForMace = swapElytra;
            return this;
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1268
 *  net.minecraft.class_1294
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_1657
 *  net.minecraft.class_1713
 *  net.minecraft.class_1743
 *  net.minecraft.class_1792
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_1829
 *  net.minecraft.class_1839
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_239$class_240
 *  net.minecraft.class_243
 *  net.minecraft.class_2596
 *  net.minecraft.class_2846
 *  net.minecraft.class_2846$class_2847
 *  net.minecraft.class_2868
 *  net.minecraft.class_3959
 *  net.minecraft.class_3959$class_242
 *  net.minecraft.class_3959$class_3960
 *  net.minecraft.class_3965
 *  net.minecraft.class_3966
 *  net.minecraft.class_9362
 */
package sweetie.leonware.api.utils.combat;

import java.util.ArrayDeque;
import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1713;
import net.minecraft.class_1743;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1829;
import net.minecraft.class_1839;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2846;
import net.minecraft.class_2868;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_3966;
import net.minecraft.class_9362;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.move.SprintEvent;
import sweetie.leonware.api.interfaces.IBacktrackable;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.combat.ClickScheduler;
import sweetie.leonware.api.utils.combat.ClickScheduler18;
import sweetie.leonware.api.utils.combat.CombatExecutor;
import sweetie.leonware.api.utils.combat.SprintManager;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.api.utils.other.SlownessManager;
import sweetie.leonware.api.utils.player.InventoryUtil;
import sweetie.leonware.api.utils.player.PlayerUtil;
import sweetie.leonware.api.utils.rotation.RaytracingUtil;
import sweetie.leonware.api.utils.rotation.rotations.FunTimeRotation;
import sweetie.leonware.client.features.modules.combat.AuraModule;
import sweetie.leonware.client.features.modules.combat.BacktrackModule;
import sweetie.leonware.client.features.modules.combat.CriticalsModule;
import sweetie.leonware.client.features.modules.movement.InventoryMoveModule;
import sweetie.leonware.client.features.modules.movement.SprintModule;

public class CombatManager
implements QuickImports {
    private final ClickScheduler clickScheduler = new ClickScheduler();
    private final ClickScheduler18 clickScheduler18 = new ClickScheduler18();
    private final SprintManager sprintManager = new SprintManager(SprintManager.SprintType.LEGIT);
    private boolean pendingMaceAttack = false;
    private class_1309 pendingMaceTarget = null;
    private CombatExecutor.CombatConfigurable configurable;
    private boolean pendingShieldBreak = false;
    private class_1657 pendingShieldTarget = null;

    private void advanceSchedulers(long delay) {
        this.clickScheduler.recalculate(delay);
        if (AuraModule.getInstance().pvpStyle.is("1.8.x")) {
            ClickScheduler18.ClickPattern pattern = switch ((String)AuraModule.getInstance().clickPattern.getValue()) {
                case "Static" -> ClickScheduler18.ClickPattern.STATIC;
                case "Jitter" -> ClickScheduler18.ClickPattern.JITTER;
                case "Interpolation" -> ClickScheduler18.ClickPattern.INTERPOLATION;
                case "Gaussian" -> ClickScheduler18.ClickPattern.GAUSSIAN;
                case "Legit" -> ClickScheduler18.ClickPattern.LEGIT;
                default -> ClickScheduler18.ClickPattern.RANDOM;
            };
            this.clickScheduler18.advance((int)((Float)AuraModule.getInstance().minCps.getValue()).floatValue(), (int)((Float)AuraModule.getInstance().maxCps.getValue()).floatValue(), pattern);
        }
    }

    public CombatManager() {
        SprintEvent.getInstance().subscribe(new Listener<SprintEvent.SprintEventData>(0, event -> {
            boolean waterSkip = AuraModule.getInstance().isDontDropSprintInWater() && (CombatManager.mc.field_1724.method_5799() || CombatManager.mc.field_1724.method_5681());
            boolean cancelCrit = this.shouldCancelCrit();
            float velocityY = (float)CombatManager.mc.field_1724.method_18798().field_1351;
            float velocityThreshold = CombatManager.mc.field_1724.field_6235 > 0 ? 0.25f : 0.16477329f;
            boolean oneTickNow = this.clickScheduler.isOneTickBeforeAttack() || CombatManager.mc.field_1724.method_7261(0.5f) >= 1.0f;
            boolean legitRule = this.configurable != null && this.configurable.target != null && this.configurable.onlyCrits && !CombatManager.mc.field_1724.method_24828() && !cancelCrit && oneTickNow && velocityY <= velocityThreshold;
            boolean fastRule = this.configurable != null && this.configurable.target != null && this.configurable.onlyCrits && !CombatManager.mc.field_1724.method_24828() && !cancelCrit && oneTickNow && velocityY <= 0.0f;
            boolean isLegit = AuraModule.getInstance().isLegitCrit();
            this.sprintManager.legitSprint((SprintEvent.SprintEventData)event, isLegit ? legitRule : fastRule, waterSkip);
        }));
    }

    public void handleAttack() {
        boolean maceConditions;
        if (this.pendingMaceAttack && this.pendingMaceTarget != null) {
            this.pendingMaceAttack = false;
            this.performMaceAttack(this.pendingMaceTarget);
            this.advanceSchedulers(700L);
            this.pendingMaceTarget = null;
            return;
        }
        switch ((String)SprintModule.getInstance().mode.getValue()) {
            case "Packet": {
                SprintManager.SprintType sprintType = SprintManager.SprintType.PACKET;
                break;
            }
            case "Legit": {
                SprintManager.SprintType sprintType = SprintManager.SprintType.LEGIT;
                break;
            }
            default: {
                SprintManager.SprintType sprintType = this.sprintManager.sprintType = SprintManager.SprintType.NONE;
            }
        }
        if (this.pendingShieldBreak && this.pendingShieldTarget != null) {
            if (this.configurable == null || !this.canAttack()) {
                return;
            }
            class_1657 shieldTarget = this.pendingShieldTarget;
            this.pendingShieldBreak = false;
            this.pendingShieldTarget = null;
            this.breakShield(shieldTarget);
            return;
        }
        FunTimeRotation.updateAttackState(this.canAttack());
        if (!this.canAttack()) {
            return;
        }
        class_1309 target = this.configurable.target;
        boolean bl = maceConditions = this.configurable.autoMace && !CombatManager.mc.field_1724.method_24828() && CombatManager.mc.field_1724.field_6017 >= this.configurable.minFallDistance && (!this.configurable.maceOnlyPlayers || target instanceof class_1657) && InventoryUtil.findMaceBetter() != -1;
        if (this.configurable.throughWallsRW) {
            this.tryGhostDigThrough(this.configurable.target);
        } else if (this.isRaytraceFailed(this.getRaytraceEntity())) {
            return;
        }
        this.sprintManager.packetSprint(false);
        if (this.configurable.shieldBreak && target instanceof class_1657) {
            class_1657 targetPlayer = (class_1657)target;
            if (InventoryUtil.findAxeBetter() != -1) {
                if (maceConditions) {
                    if (this.breakShield(targetPlayer)) {
                        this.sprintManager.packetSprint(true);
                        this.pendingMaceAttack = true;
                        this.pendingMaceTarget = targetPlayer;
                        return;
                    }
                } else if (targetPlayer.method_6115() && !targetPlayer.method_6030().method_7960() && targetPlayer.method_6030().method_7909().method_7853(targetPlayer.method_6030()) == class_1839.field_8949) {
                    this.pendingShieldBreak = true;
                    this.pendingShieldTarget = targetPlayer;
                    this.sprintManager.packetSprint(true);
                    return;
                }
            }
        }
        if (maceConditions) {
            this.performMaceAttack(target);
            this.sprintManager.packetSprint(true);
            this.advanceSchedulers(700L);
            ++FunTimeRotation.attackCount;
            return;
        }
        CriticalsModule.getInstance().triggerCriticals((class_1297)target);
        this.attackTarget((class_1297)target);
        this.sprintManager.packetSprint(true);
        long extraDelay = AuraModule.getInstance().isLegitHits() ? (long)MathUtil.randomInRange((int)AuraModule.getInstance().getLegitDelayMin(), (int)AuraModule.getInstance().getLegitDelayMax()) : 0L;
        this.advanceSchedulers(500L + extraDelay);
        ++FunTimeRotation.attackCount;
    }

    private void attackTarget(class_1297 target) {
        if (this.configurable != null && this.configurable.unpressShield && CombatManager.mc.field_1724.method_6115() && (CombatManager.mc.field_1724.method_6079().method_7909() == class_1802.field_8255 || CombatManager.mc.field_1724.method_6047().method_7909() == class_1802.field_8255)) {
            CombatManager.mc.field_1724.method_6075();
            CombatManager.mc.field_1724.field_3944.method_52787((class_2596)new class_2846(class_2846.class_2847.field_12974, class_2338.field_10980, class_2350.field_11033));
        }
        CombatManager.mc.field_1761.method_2918((class_1657)CombatManager.mc.field_1724, target);
        CombatManager.mc.field_1724.method_6104(class_1268.field_5808);
    }

    public void reset() {
        this.pendingMaceAttack = false;
        this.pendingMaceTarget = null;
        this.pendingShieldBreak = false;
        this.pendingShieldTarget = null;
        this.configurable = null;
        this.clickScheduler.recalculate(0L);
    }

    private boolean breakShield(class_1657 target) {
        Runnable doAttack;
        if (!target.method_6115()) {
            return false;
        }
        class_1799 activeItem = target.method_6030();
        if (activeItem.method_7960()) {
            return false;
        }
        if (activeItem.method_7909().method_7853(activeItem) != class_1839.field_8949) {
            return false;
        }
        if (!this.clickScheduler.isCooldownComplete()) {
            return false;
        }
        int axe = InventoryUtil.findAxeBetter();
        if (axe == -1) {
            return false;
        }
        int prevSlot = CombatManager.mc.field_1724.method_31548().field_7545;
        int syncId = CombatManager.mc.field_1724.field_7498.field_7763;
        if (axe < 9) {
            doAttack = () -> {
                CombatManager.mc.field_1724.field_3944.method_52787((class_2596)new class_2868(axe));
                this.attackTarget((class_1297)target);
                CombatManager.mc.field_1724.field_3944.method_52787((class_2596)new class_2868(prevSlot));
                this.advanceSchedulers(500L);
                ++FunTimeRotation.attackCount;
            };
        } else {
            int targetSlot = this.findEmptyHotbarSlot();
            if (targetSlot == -1) {
                targetSlot = this.findNonWeaponHotbarSlot();
            }
            if (targetSlot == -1) {
                return false;
            }
            int swapSlot = targetSlot;
            doAttack = () -> {
                CombatManager.mc.field_1761.method_2906(syncId, axe, 0, class_1713.field_7790, (class_1657)CombatManager.mc.field_1724);
                CombatManager.mc.field_1761.method_2906(syncId, swapSlot + 36, 0, class_1713.field_7790, (class_1657)CombatManager.mc.field_1724);
                CombatManager.mc.field_1724.field_3944.method_52787((class_2596)new class_2868(swapSlot));
                this.attackTarget((class_1297)target);
                CombatManager.mc.field_1724.field_3944.method_52787((class_2596)new class_2868(prevSlot));
                CombatManager.mc.field_1761.method_2906(syncId, swapSlot + 36, 0, class_1713.field_7790, (class_1657)CombatManager.mc.field_1724);
                CombatManager.mc.field_1761.method_2906(syncId, axe, 0, class_1713.field_7790, (class_1657)CombatManager.mc.field_1724);
                this.advanceSchedulers(500L);
                ++FunTimeRotation.attackCount;
            };
        }
        if (!InventoryMoveModule.getInstance().isBasic() && SlownessManager.isEnabled()) {
            SlownessManager.applySlowness(110L, 1L, doAttack);
        } else {
            doAttack.run();
        }
        return true;
    }

    private int findEmptyHotbarSlot() {
        for (int i = 0; i < 9; ++i) {
            if (!CombatManager.mc.field_1724.method_31548().method_5438(i).method_7960()) continue;
            return i;
        }
        return -1;
    }

    private int findNonWeaponHotbarSlot() {
        int prevSlot = CombatManager.mc.field_1724.method_31548().field_7545;
        for (int i = 0; i < 9; ++i) {
            class_1792 item;
            if (i == prevSlot || (item = CombatManager.mc.field_1724.method_31548().method_5438(i).method_7909()) instanceof class_1829 || item instanceof class_1743 || item instanceof class_9362) continue;
            return i;
        }
        return -1;
    }

    private void performMaceAttack(class_1309 target) {
        int maceSlot = InventoryUtil.findMaceBetter();
        if (maceSlot == -1) {
            return;
        }
        int prevSlot = CombatManager.mc.field_1724.method_31548().field_7545;
        if (maceSlot < 9) {
            CombatManager.mc.field_1724.field_3944.method_52787((class_2596)new class_2868(maceSlot));
            this.attackTarget((class_1297)target);
            CombatManager.mc.field_1724.field_3944.method_52787((class_2596)new class_2868(prevSlot));
        } else {
            int syncId = CombatManager.mc.field_1724.field_7512.field_7763;
            Runnable swapAction = () -> {
                CombatManager.mc.field_1761.method_2906(syncId, maceSlot, prevSlot, class_1713.field_7791, (class_1657)CombatManager.mc.field_1724);
                this.attackTarget((class_1297)target);
                CombatManager.mc.field_1761.method_2906(syncId, maceSlot, prevSlot, class_1713.field_7791, (class_1657)CombatManager.mc.field_1724);
            };
            if (!InventoryMoveModule.getInstance().isBasic() && SlownessManager.isEnabled()) {
                SlownessManager.applySlowness(100L, 50L, swapAction);
            } else {
                swapAction.run();
            }
        }
    }

    private class_243 getAimPointForRotation(class_1309 targetEntity) {
        if (targetEntity == null) {
            return null;
        }
        class_243 base = targetEntity.method_19538().method_1031(0.0, (double)(targetEntity.method_17682() / 1.2f), 0.0);
        base = base.method_1031(targetEntity.method_23317() - targetEntity.field_6014, targetEntity.method_23318() - targetEntity.field_6036, targetEntity.method_23321() - targetEntity.field_5969);
        return base;
    }

    private void tryGhostDigThrough(class_1309 target) {
        if (target == null || CombatManager.mc.field_1724 == null || CombatManager.mc.field_1687 == null) {
            return;
        }
        try {
            class_3965 hit;
            class_243 eye = CombatManager.mc.field_1724.method_33571();
            float yawRad = (float)Math.toRadians(this.configurable.rotation.getYaw());
            float pitchRad = (float)Math.toRadians(this.configurable.rotation.getPitch());
            double dirX = -Math.sin(yawRad) * Math.cos(pitchRad);
            double dirY = -Math.sin(pitchRad);
            double dirZ = Math.cos(yawRad) * Math.cos(pitchRad);
            class_243 rotationDir = new class_243(dirX, dirY, dirZ).method_1029();
            class_243 aimPoint = this.getAimPointForRotation(target);
            class_243 rayEnd = aimPoint != null ? aimPoint : eye.method_1019(rotationDir.method_1021(4.0));
            class_243 dirVec = aimPoint != null ? aimPoint.method_1020(eye).method_1029() : rotationDir;
            dirX = dirVec.field_1352;
            dirY = dirVec.field_1351;
            dirZ = dirVec.field_1350;
            class_243 currentStart = eye;
            int maxSteps = 2;
            for (int i = 0; i < maxSteps && (hit = CombatManager.mc.field_1687.method_17742(new class_3959(currentStart, rayEnd, class_3959.class_3960.field_17558, class_3959.class_242.field_1348, (class_1297)CombatManager.mc.field_1724))) != null && hit.method_17783() == class_239.class_240.field_1332; ++i) {
                CombatManager.mc.field_1724.field_3944.method_52787((class_2596)new class_2846(class_2846.class_2847.field_12968, hit.method_17777(), hit.method_17780()));
                CombatManager.mc.field_1724.field_3944.method_52787((class_2596)new class_2846(class_2846.class_2847.field_12973, hit.method_17777(), hit.method_17780()));
                class_243 hitPos = hit.method_17784();
                currentStart = new class_243(hitPos.field_1352 + dirX * 0.05, hitPos.field_1351 + dirY * 0.05, hitPos.field_1350 + dirZ * 0.05);
                if (!(currentStart.method_1025(rayEnd) < 0.0025)) {
                    continue;
                }
                break;
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public boolean canAttack() {
        if (this.configurable == null) {
            return false;
        }
        boolean is18 = AuraModule.getInstance().pvpStyle.is("1.8.x");
        if (is18 ? !this.clickScheduler18.canClick() : this.isCooldownNotComplete()) {
            return false;
        }
        if (this.configurable.noAttackIfEat && PlayerUtil.isEating()) {
            return false;
        }
        if (is18) {
            return true;
        }
        if (!CombatManager.mc.field_1690.field_1903.method_1434() && CombatManager.mc.field_1724.method_24828() && this.configurable.onlyCrits && this.configurable.smartCrits) {
            return true;
        }
        if (!CombatManager.mc.field_1690.field_1903.method_1434() && PlayerUtil.isAboveWater()) {
            return true;
        }
        return this.shouldDoHit() || !this.configurable.onlyCrits;
    }

    private boolean isRaytraceFailed(class_1297 targetEntity) {
        if (!this.configurable.raytrace) {
            return false;
        }
        if (CombatManager.mc.field_1724.method_6128()) {
            return false;
        }
        if (targetEntity == null) {
            return true;
        }
        return targetEntity != this.configurable.target;
    }

    private boolean isCooldownNotComplete() {
        return !this.clickScheduler.isCooldownComplete();
    }

    private boolean shouldCancelCrit() {
        return CombatManager.mc.field_1724.method_6059(class_1294.field_5919) || CombatManager.mc.field_1724.method_6059(class_1294.field_5902) || CombatManager.mc.field_1724.method_6059(class_1294.field_5906) || PlayerUtil.isInWeb() || CombatManager.mc.field_1724.method_5771() || CombatManager.mc.field_1724.method_6101() || CombatManager.mc.field_1724.method_3144() || CombatManager.mc.field_1724.method_5765() || CombatManager.mc.field_1724.method_5869() || CombatManager.mc.field_1724.method_5740() || CombatManager.mc.field_1724.method_31549().field_7479;
    }

    private boolean shouldDoHit() {
        return !CombatManager.mc.field_1724.method_24828() && CombatManager.mc.field_1724.field_6017 > 0.0f || this.shouldCancelCrit();
    }

    private class_1297 getRaytraceEntity() {
        BacktrackModule.Position backtrackPos;
        IBacktrackable backtrackable;
        ArrayDeque<BacktrackModule.Position> tracks;
        class_1309 class_13092;
        if (AuraModule.getInstance().isHitInBacktrack() && BacktrackModule.getInstance().isEnabled() && (class_13092 = this.configurable.target) instanceof IBacktrackable && !(tracks = (backtrackable = (IBacktrackable)class_13092).leonware$getBackTracks()).isEmpty() && (backtrackPos = tracks.peekFirst()) != null) {
            class_243 btPos = backtrackPos.pos().method_1031(0.0, (double)(this.configurable.target.method_17682() / 2.0f), 0.0);
            class_3966 result = RaytracingUtil.raytraceEntityBacktrack(this.configurable.distance, this.configurable.rotation, this.configurable.ignoreWalls, btPos, this.configurable.target);
            return result != null ? result.method_17782() : null;
        }
        class_3966 entityHitResult = RaytracingUtil.raytraceEntity(this.configurable.distance, this.configurable.rotation, this.configurable.ignoreWalls, entity -> !this.configurable.ignoreEntity || entity == this.configurable.target, 0.0f);
        return entityHitResult != null ? entityHitResult.method_17782() : null;
    }

    @Generated
    public ClickScheduler clickScheduler() {
        return this.clickScheduler;
    }

    @Generated
    public ClickScheduler18 clickScheduler18() {
        return this.clickScheduler18;
    }

    @Generated
    public SprintManager sprintManager() {
        return this.sprintManager;
    }

    @Generated
    public boolean pendingMaceAttack() {
        return this.pendingMaceAttack;
    }

    @Generated
    public class_1309 pendingMaceTarget() {
        return this.pendingMaceTarget;
    }

    @Generated
    public boolean pendingShieldBreak() {
        return this.pendingShieldBreak;
    }

    @Generated
    public class_1657 pendingShieldTarget() {
        return this.pendingShieldTarget;
    }

    @Generated
    public CombatExecutor.CombatConfigurable configurable() {
        return this.configurable;
    }

    @Generated
    public CombatManager configurable(CombatExecutor.CombatConfigurable configurable) {
        this.configurable = configurable;
        return this;
    }
}


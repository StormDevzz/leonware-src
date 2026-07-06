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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/combat/CombatManager.class */
public class CombatManager implements QuickImports {
    private CombatExecutor.CombatConfigurable configurable;
    private final ClickScheduler clickScheduler = new ClickScheduler();
    private final ClickScheduler18 clickScheduler18 = new ClickScheduler18();
    private final SprintManager sprintManager = new SprintManager(SprintManager.SprintType.LEGIT);
    private boolean pendingMaceAttack = false;
    private class_1309 pendingMaceTarget = null;
    private boolean pendingShieldBreak = false;
    private class_1657 pendingShieldTarget = null;

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

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    private void advanceSchedulers(long delay) throws MatchException {
        ClickScheduler18.ClickPattern clickPattern;
        this.clickScheduler.recalculate(delay);
        if (AuraModule.getInstance().pvpStyle.is("1.8.x")) {
            switch (AuraModule.getInstance().clickPattern.getValue()) {
                case "Static":
                    clickPattern = ClickScheduler18.ClickPattern.STATIC;
                    break;
                case "Jitter":
                    clickPattern = ClickScheduler18.ClickPattern.JITTER;
                    break;
                case "Interpolation":
                    clickPattern = ClickScheduler18.ClickPattern.INTERPOLATION;
                    break;
                case "Gaussian":
                    clickPattern = ClickScheduler18.ClickPattern.GAUSSIAN;
                    break;
                case "Legit":
                    clickPattern = ClickScheduler18.ClickPattern.LEGIT;
                    break;
                default:
                    clickPattern = ClickScheduler18.ClickPattern.RANDOM;
                    break;
            }
            ClickScheduler18.ClickPattern pattern = clickPattern;
            this.clickScheduler18.advance((int) AuraModule.getInstance().minCps.getValue().floatValue(), (int) AuraModule.getInstance().maxCps.getValue().floatValue(), pattern);
        }
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
    public CombatExecutor.CombatConfigurable configurable() {
        return this.configurable;
    }

    @Generated
    public CombatManager configurable(CombatExecutor.CombatConfigurable configurable) {
        this.configurable = configurable;
        return this;
    }

    public CombatManager() {
        SprintEvent.getInstance().subscribe(new Listener(0, event -> {
            float f;
            boolean waterSkip = AuraModule.getInstance().isDontDropSprintInWater() && (mc.field_1724.method_5799() || mc.field_1724.method_5681());
            boolean cancelCrit = shouldCancelCrit();
            float velocityY = (float) mc.field_1724.method_18798().field_1351;
            if (mc.field_1724.field_6235 > 0) {
                f = 0.25f;
            } else {
                f = 0.16477329f;
            }
            float velocityThreshold = f;
            boolean oneTickNow = this.clickScheduler.isOneTickBeforeAttack() || mc.field_1724.method_7261(0.5f) >= 1.0f;
            boolean legitRule = (this.configurable == null || this.configurable.target == null || !this.configurable.onlyCrits || mc.field_1724.method_24828() || cancelCrit || !oneTickNow || velocityY > velocityThreshold) ? false : true;
            boolean fastRule = (this.configurable == null || this.configurable.target == null || !this.configurable.onlyCrits || mc.field_1724.method_24828() || cancelCrit || !oneTickNow || velocityY > 0.0f) ? false : true;
            boolean isLegit = AuraModule.getInstance().isLegitCrit();
            this.sprintManager.legitSprint(event, isLegit ? legitRule : fastRule, waterSkip);
        }));
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    public void handleAttack() throws MatchException {
        SprintManager.SprintType sprintType;
        long jRandomInRange;
        if (this.pendingMaceAttack && this.pendingMaceTarget != null) {
            this.pendingMaceAttack = false;
            performMaceAttack(this.pendingMaceTarget);
            advanceSchedulers(700L);
            this.pendingMaceTarget = null;
            return;
        }
        SprintManager sprintManager = this.sprintManager;
        switch (SprintModule.getInstance().mode.getValue()) {
            case "Packet":
                sprintType = SprintManager.SprintType.PACKET;
                break;
            case "Legit":
                sprintType = SprintManager.SprintType.LEGIT;
                break;
            default:
                sprintType = SprintManager.SprintType.NONE;
                break;
        }
        sprintManager.sprintType = sprintType;
        if (this.pendingShieldBreak && this.pendingShieldTarget != null) {
            if (this.configurable == null || !canAttack()) {
                return;
            }
            class_1657 shieldTarget = this.pendingShieldTarget;
            this.pendingShieldBreak = false;
            this.pendingShieldTarget = null;
            breakShield(shieldTarget);
            return;
        }
        FunTimeRotation.updateAttackState(canAttack());
        if (canAttack()) {
            class_1297 target = this.configurable.target;
            boolean maceConditions = this.configurable.autoMace && !mc.field_1724.method_24828() && mc.field_1724.field_6017 >= this.configurable.minFallDistance && (!this.configurable.maceOnlyPlayers || (target instanceof class_1657)) && InventoryUtil.findMaceBetter() != -1;
            if (this.configurable.throughWallsRW) {
                tryGhostDigThrough(this.configurable.target);
            } else if (isRaytraceFailed(getRaytraceEntity())) {
                return;
            }
            this.sprintManager.packetSprint(false);
            if (this.configurable.shieldBreak && (target instanceof class_1657)) {
                class_1657 targetPlayer = (class_1657) target;
                if (InventoryUtil.findAxeBetter() != -1) {
                    if (maceConditions) {
                        if (breakShield(targetPlayer)) {
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
                performMaceAttack((class_1309) target);
                this.sprintManager.packetSprint(true);
                advanceSchedulers(700L);
                FunTimeRotation.attackCount++;
                return;
            }
            CriticalsModule.getInstance().triggerCriticals(target);
            attackTarget(target);
            this.sprintManager.packetSprint(true);
            if (AuraModule.getInstance().isLegitHits()) {
                jRandomInRange = MathUtil.randomInRange((int) AuraModule.getInstance().getLegitDelayMin(), (int) AuraModule.getInstance().getLegitDelayMax());
            } else {
                jRandomInRange = 0;
            }
            long extraDelay = jRandomInRange;
            advanceSchedulers(500 + extraDelay);
            FunTimeRotation.attackCount++;
        }
    }

    private void attackTarget(class_1297 target) {
        if (this.configurable != null && this.configurable.unpressShield && mc.field_1724.method_6115() && (mc.field_1724.method_6079().method_7909() == class_1802.field_8255 || mc.field_1724.method_6047().method_7909() == class_1802.field_8255)) {
            mc.field_1724.method_6075();
            mc.field_1724.field_3944.method_52787(new class_2846(class_2846.class_2847.field_12974, class_2338.field_10980, class_2350.field_11033));
        }
        mc.field_1761.method_2918(mc.field_1724, target);
        mc.field_1724.method_6104(class_1268.field_5808);
    }

    @Generated
    public boolean pendingShieldBreak() {
        return this.pendingShieldBreak;
    }

    @Generated
    public class_1657 pendingShieldTarget() {
        return this.pendingShieldTarget;
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
        int axe;
        Runnable doAttack;
        if (!target.method_6115()) {
            return false;
        }
        class_1799 activeItem = target.method_6030();
        if (activeItem.method_7960() || activeItem.method_7909().method_7853(activeItem) != class_1839.field_8949 || !this.clickScheduler.isCooldownComplete() || (axe = InventoryUtil.findAxeBetter()) == -1) {
            return false;
        }
        int prevSlot = mc.field_1724.method_31548().field_7545;
        int syncId = mc.field_1724.field_7498.field_7763;
        if (axe < 9) {
            doAttack = () -> {
                mc.field_1724.field_3944.method_52787(new class_2868(axe));
                attackTarget(target);
                mc.field_1724.field_3944.method_52787(new class_2868(prevSlot));
                advanceSchedulers(500L);
                FunTimeRotation.attackCount++;
            };
        } else {
            int targetSlot = findEmptyHotbarSlot();
            if (targetSlot == -1) {
                targetSlot = findNonWeaponHotbarSlot();
            }
            if (targetSlot == -1) {
                return false;
            }
            int swapSlot = targetSlot;
            doAttack = () -> {
                mc.field_1761.method_2906(syncId, axe, 0, class_1713.field_7790, mc.field_1724);
                mc.field_1761.method_2906(syncId, swapSlot + 36, 0, class_1713.field_7790, mc.field_1724);
                mc.field_1724.field_3944.method_52787(new class_2868(swapSlot));
                attackTarget(target);
                mc.field_1724.field_3944.method_52787(new class_2868(prevSlot));
                mc.field_1761.method_2906(syncId, swapSlot + 36, 0, class_1713.field_7790, mc.field_1724);
                mc.field_1761.method_2906(syncId, axe, 0, class_1713.field_7790, mc.field_1724);
                advanceSchedulers(500L);
                FunTimeRotation.attackCount++;
            };
        }
        if (!InventoryMoveModule.getInstance().isBasic() && SlownessManager.isEnabled()) {
            SlownessManager.applySlowness(110L, 1L, doAttack);
            return true;
        }
        doAttack.run();
        return true;
    }

    private int findEmptyHotbarSlot() {
        for (int i = 0; i < 9; i++) {
            if (mc.field_1724.method_31548().method_5438(i).method_7960()) {
                return i;
            }
        }
        return -1;
    }

    private int findNonWeaponHotbarSlot() {
        int prevSlot = mc.field_1724.method_31548().field_7545;
        for (int i = 0; i < 9; i++) {
            if (i != prevSlot) {
                class_1792 item = mc.field_1724.method_31548().method_5438(i).method_7909();
                if (!(item instanceof class_1829) && !(item instanceof class_1743) && !(item instanceof class_9362)) {
                    return i;
                }
            }
        }
        return -1;
    }

    private void performMaceAttack(class_1309 target) {
        int maceSlot = InventoryUtil.findMaceBetter();
        if (maceSlot == -1) {
            return;
        }
        int prevSlot = mc.field_1724.method_31548().field_7545;
        if (maceSlot < 9) {
            mc.field_1724.field_3944.method_52787(new class_2868(maceSlot));
            attackTarget(target);
            mc.field_1724.field_3944.method_52787(new class_2868(prevSlot));
            return;
        }
        int syncId = mc.field_1724.field_7512.field_7763;
        Runnable swapAction = () -> {
            mc.field_1761.method_2906(syncId, maceSlot, prevSlot, class_1713.field_7791, mc.field_1724);
            attackTarget(target);
            mc.field_1761.method_2906(syncId, maceSlot, prevSlot, class_1713.field_7791, mc.field_1724);
        };
        if (!InventoryMoveModule.getInstance().isBasic() && SlownessManager.isEnabled()) {
            SlownessManager.applySlowness(100L, 50L, swapAction);
        } else {
            swapAction.run();
        }
    }

    private class_243 getAimPointForRotation(class_1309 targetEntity) {
        if (targetEntity == null) {
            return null;
        }
        class_243 base = targetEntity.method_19538().method_1031(0.0d, targetEntity.method_17682() / 1.2f, 0.0d);
        return base.method_1031(targetEntity.method_23317() - targetEntity.field_6014, targetEntity.method_23318() - targetEntity.field_6036, targetEntity.method_23321() - targetEntity.field_5969);
    }

    private void tryGhostDigThrough(class_1309 target) {
        if (target == null || mc.field_1724 == null || mc.field_1687 == null) {
            return;
        }
        try {
            class_243 eye = mc.field_1724.method_33571();
            float yawRad = (float) Math.toRadians(this.configurable.rotation.getYaw());
            float pitchRad = (float) Math.toRadians(this.configurable.rotation.getPitch());
            double dirX = (-Math.sin(yawRad)) * Math.cos(pitchRad);
            double dirY = -Math.sin(pitchRad);
            double dirZ = Math.cos(yawRad) * Math.cos(pitchRad);
            class_243 rotationDir = new class_243(dirX, dirY, dirZ).method_1029();
            class_243 aimPoint = getAimPointForRotation(target);
            class_243 rayEnd = aimPoint != null ? aimPoint : eye.method_1019(rotationDir.method_1021(4.0d));
            class_243 dirVec = aimPoint != null ? aimPoint.method_1020(eye).method_1029() : rotationDir;
            double dirX2 = dirVec.field_1352;
            double dirY2 = dirVec.field_1351;
            double dirZ2 = dirVec.field_1350;
            class_243 currentStart = eye;
            for (int i = 0; i < 2; i++) {
                class_3965 hit = mc.field_1687.method_17742(new class_3959(currentStart, rayEnd, class_3959.class_3960.field_17558, class_3959.class_242.field_1348, mc.field_1724));
                if (hit == null || hit.method_17783() != class_239.class_240.field_1332) {
                    break;
                }
                mc.field_1724.field_3944.method_52787(new class_2846(class_2846.class_2847.field_12968, hit.method_17777(), hit.method_17780()));
                mc.field_1724.field_3944.method_52787(new class_2846(class_2846.class_2847.field_12973, hit.method_17777(), hit.method_17780()));
                class_243 hitPos = hit.method_17784();
                currentStart = new class_243(hitPos.field_1352 + (dirX2 * 0.05d), hitPos.field_1351 + (dirY2 * 0.05d), hitPos.field_1350 + (dirZ2 * 0.05d));
                if (currentStart.method_1025(rayEnd) < 0.0025d) {
                    break;
                }
            }
        } catch (Throwable th) {
        }
    }

    public boolean canAttack() {
        if (this.configurable == null) {
            return false;
        }
        boolean is18 = AuraModule.getInstance().pvpStyle.is("1.8.x");
        if (is18) {
            if (!this.clickScheduler18.canClick()) {
                return false;
            }
        } else if (isCooldownNotComplete()) {
            return false;
        }
        if (this.configurable.noAttackIfEat && PlayerUtil.isEating()) {
            return false;
        }
        if (is18) {
            return true;
        }
        if (!mc.field_1690.field_1903.method_1434() && mc.field_1724.method_24828() && this.configurable.onlyCrits && this.configurable.smartCrits) {
            return true;
        }
        return (!mc.field_1690.field_1903.method_1434() && PlayerUtil.isAboveWater()) || shouldDoHit() || !this.configurable.onlyCrits;
    }

    private boolean isRaytraceFailed(class_1297 targetEntity) {
        if (this.configurable.raytrace && !mc.field_1724.method_6128()) {
            return targetEntity == null || targetEntity != this.configurable.target;
        }
        return false;
    }

    private boolean isCooldownNotComplete() {
        return !this.clickScheduler.isCooldownComplete();
    }

    private boolean shouldCancelCrit() {
        return mc.field_1724.method_6059(class_1294.field_5919) || mc.field_1724.method_6059(class_1294.field_5902) || mc.field_1724.method_6059(class_1294.field_5906) || PlayerUtil.isInWeb() || mc.field_1724.method_5771() || mc.field_1724.method_6101() || mc.field_1724.method_3144() || mc.field_1724.method_5765() || mc.field_1724.method_5869() || mc.field_1724.method_5740() || mc.field_1724.method_31549().field_7479;
    }

    private boolean shouldDoHit() {
        return (!mc.field_1724.method_24828() && mc.field_1724.field_6017 > 0.0f) || shouldCancelCrit();
    }

    private class_1297 getRaytraceEntity() {
        BacktrackModule.Position backtrackPos;
        if (AuraModule.getInstance().isHitInBacktrack() && BacktrackModule.getInstance().isEnabled()) {
            IBacktrackable iBacktrackable = this.configurable.target;
            if (iBacktrackable instanceof IBacktrackable) {
                IBacktrackable backtrackable = iBacktrackable;
                ArrayDeque<BacktrackModule.Position> tracks = backtrackable.leonware$getBackTracks();
                if (!tracks.isEmpty() && (backtrackPos = tracks.peekFirst()) != null) {
                    class_243 btPos = backtrackPos.pos().method_1031(0.0d, this.configurable.target.method_17682() / 2.0f, 0.0d);
                    class_3966 result = RaytracingUtil.raytraceEntityBacktrack(this.configurable.distance, this.configurable.rotation, this.configurable.ignoreWalls, btPos, this.configurable.target);
                    if (result != null) {
                        return result.method_17782();
                    }
                    return null;
                }
            }
        }
        class_3966 entityHitResult = RaytracingUtil.raytraceEntity(this.configurable.distance, this.configurable.rotation, this.configurable.ignoreWalls, entity -> {
            return !this.configurable.ignoreEntity || entity == this.configurable.target;
        }, 0.0f);
        if (entityHitResult != null) {
            return entityHitResult.method_17782();
        }
        return null;
    }
}

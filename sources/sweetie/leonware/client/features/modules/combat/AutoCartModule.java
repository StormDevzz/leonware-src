package sweetie.leonware.client.features.modules.combat;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.HashSet;
import java.util.Set;
import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1269;
import net.minecraft.class_1297;
import net.minecraft.class_1665;
import net.minecraft.class_1685;
import net.minecraft.class_1701;
import net.minecraft.class_1713;
import net.minecraft.class_1802;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_3481;
import net.minecraft.class_3486;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BindSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.system.backend.KeyStorage;
import sweetie.leonware.api.utils.player.InventoryUtil;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/AutoCartModule.class */
@ModuleRegister(name = "Auto Cart", category = Category.COMBAT)
public class AutoCartModule extends Module {
    private static final AutoCartModule instance = new AutoCartModule();
    private static final int MAX_PREDICTION_TICKS = 80;
    private static final int BOW_CHARGE_TICKS = 10;
    private boolean bowShotQueued;
    private boolean bowCharging;
    private int bowChargeTime;
    private final SliderSetting range = new SliderSetting("Range").value(Float.valueOf(5.0f)).range(1.0f, 8.0f).step(0.1f);
    private final BindSetting bowShootKey = new BindSetting("Bow Shoot Key").value((Integer) (-999));
    private final Set<Integer> knownArrows = new HashSet();
    private final Set<Integer> processedArrows = new HashSet();
    private int bowPrevSlot = -1;

    @Generated
    public static AutoCartModule getInstance() {
        return instance;
    }

    public AutoCartModule() {
        addSettings(this.range, this.bowShootKey);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onEnable() {
        this.knownArrows.clear();
        this.processedArrows.clear();
        resetBowState();
        cacheExistingArrows();
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        this.knownArrows.clear();
        this.processedArrows.clear();
        if (mc.field_1690 != null) {
            mc.field_1690.field_1904.method_23481(false);
        }
        resetBowState();
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener(event -> {
            onTick();
        }));
        addEvents(tickEvent);
    }

    private void onTick() {
        LandingPrediction prediction;
        if (mc.field_1724 == null || mc.field_1687 == null || mc.field_1761 == null) {
            return;
        }
        if (this.bowShootKey.getValue().intValue() != -999 && KeyStorage.isPressed(this.bowShootKey.getValue().intValue()) && !this.bowShotQueued) {
            this.bowShotQueued = true;
        }
        handleBowShoot();
        Set<Integer> aliveArrows = new HashSet<>();
        for (class_1297 entity : mc.field_1687.method_18112()) {
            if (entity instanceof class_1665) {
                class_1665 projectile = (class_1665) entity;
                if (!(projectile instanceof class_1685) && projectile.method_24921() == mc.field_1724) {
                    int id = projectile.method_5628();
                    aliveArrows.add(Integer.valueOf(id));
                    this.knownArrows.add(Integer.valueOf(id));
                    if (!this.processedArrows.contains(Integer.valueOf(id)) && hasRequiredItemsInHotbar() && shouldProcessArrow(projectile) && (prediction = predictLanding(projectile)) != null) {
                        double rangeSq = this.range.getValue().floatValue() * this.range.getValue().floatValue();
                        if (mc.field_1724.method_5707(class_243.method_24953(prediction.placePos())) > rangeSq) {
                            this.processedArrows.add(Integer.valueOf(id));
                        } else if (placeCartAt(prediction.placePos()) || projectile.method_24828()) {
                            this.processedArrows.add(Integer.valueOf(id));
                        }
                    }
                }
            }
        }
        this.knownArrows.retainAll(aliveArrows);
        this.processedArrows.retainAll(aliveArrows);
    }

    private void handleBowShoot() {
        if (mc.field_1724 == null || mc.field_1761 == null || mc.field_1690 == null) {
            return;
        }
        if (this.bowCharging) {
            if (!mc.field_1724.method_6047().method_31574(class_1802.field_8102)) {
                finishBowShot(false);
                return;
            }
            this.bowChargeTime++;
            if (this.bowChargeTime >= 10) {
                finishBowShot(true);
                return;
            }
            return;
        }
        if (this.bowShotQueued) {
            this.bowShotQueued = false;
            int bowSlot = InventoryUtil.findItem(class_1802.field_8102, true);
            if (bowSlot == -1) {
                return;
            }
            this.bowPrevSlot = mc.field_1724.method_31548().field_7545;
            if (this.bowPrevSlot != bowSlot) {
                mc.field_1724.method_31548().field_7545 = bowSlot;
                mc.field_1761.leonware$syncSelectedSlot();
            }
            mc.field_1690.field_1904.method_23481(true);
            this.bowCharging = true;
            this.bowChargeTime = 0;
        }
    }

    private void finishBowShot(boolean releaseShot) {
        if (mc.field_1724 == null || mc.field_1761 == null || mc.field_1690 == null) {
            resetBowState();
            return;
        }
        if (releaseShot) {
            mc.field_1690.field_1904.method_23481(false);
            if (mc.field_1724.method_6115()) {
                mc.field_1761.method_2897(mc.field_1724);
            }
        }
        if (this.bowPrevSlot >= 0 && this.bowPrevSlot <= 8 && mc.field_1724.method_31548().field_7545 != this.bowPrevSlot) {
            mc.field_1724.method_31548().field_7545 = this.bowPrevSlot;
            mc.field_1761.leonware$syncSelectedSlot();
        }
        resetBowState();
    }

    private void resetBowState() {
        this.bowShotQueued = false;
        this.bowCharging = false;
        this.bowChargeTime = 0;
        this.bowPrevSlot = -1;
    }

    private void cacheExistingArrows() {
        if (mc.field_1724 == null || mc.field_1687 == null) {
            return;
        }
        for (class_1665 class_1665Var : mc.field_1687.method_18112()) {
            if (class_1665Var instanceof class_1665) {
                class_1665 projectile = class_1665Var;
                if (!(projectile instanceof class_1685) && projectile.method_24921() == mc.field_1724) {
                    this.knownArrows.add(Integer.valueOf(projectile.method_5628()));
                }
            }
        }
    }

    private boolean hasRequiredItemsInHotbar() {
        return (InventoryUtil.findItem(class_1802.field_8129, true) == -1 || InventoryUtil.findItem(class_1802.field_8069, true) == -1) ? false : true;
    }

    private boolean shouldProcessArrow(class_1665 projectile) {
        return !projectile.method_24828() && projectile.method_18798().method_1027() > 1.0E-4d;
    }

    private LandingPrediction predictLanding(class_1665 arrow) {
        if (mc.field_1687 == null) {
            return null;
        }
        class_243 pos = arrow.method_19538();
        class_243 motion = arrow.method_18798();
        for (int tick = 1; tick <= MAX_PREDICTION_TICKS; tick++) {
            class_243 prevPos = pos;
            pos = pos.method_1019(motion);
            motion = updateMotion(arrow, prevPos, motion);
            class_3965 hit = mc.field_1687.method_17742(new class_3959(prevPos, pos, class_3959.class_3960.field_17558, class_3959.class_242.field_1348, arrow));
            if (hit.method_17783() != class_239.class_240.field_1333) {
                class_2338 placePos = resolvePlacePos(hit);
                if (placePos != null) {
                    return new LandingPrediction(placePos, tick);
                }
                return null;
            }
            if (pos.field_1351 < mc.field_1687.method_31607() - 2) {
                return null;
            }
        }
        return null;
    }

    private class_243 updateMotion(class_1665 arrow, class_243 pos, class_243 motion) {
        if (mc.field_1687 == null) {
            return motion;
        }
        boolean inWater = mc.field_1687.method_8320(class_2338.method_49638(pos)).method_26227().method_15767(class_3486.field_15517);
        double drag = inWater ? 0.6d : 0.99d;
        return motion.method_1021(drag).method_1031(0.0d, -arrow.method_56989(), 0.0d);
    }

    private class_2338 resolvePlacePos(class_3965 hit) {
        if (mc.field_1687 == null) {
            return null;
        }
        class_2338 basePos = hit.method_17777();
        if (isRail(basePos)) {
            return basePos;
        }
        class_2338 preferred = hit.method_17780() == class_2350.field_11036 ? basePos.method_10084() : basePos;
        if (canPlaceRailAt(preferred)) {
            return preferred;
        }
        class_2338 up = basePos.method_10084();
        if (canPlaceRailAt(up)) {
            return up;
        }
        class_2338 down = basePos.method_10074();
        if (canPlaceRailAt(down)) {
            return down;
        }
        return null;
    }

    /* JADX WARN: Finally extract failed */
    private boolean placeCartAt(class_2338 placePos) {
        if (mc.field_1724 == null || mc.field_1687 == null || mc.field_1761 == null) {
            return false;
        }
        int railSlot = InventoryUtil.findItem(class_1802.field_8129, true);
        int cartSlot = InventoryUtil.findItem(class_1802.field_8069, true);
        if (railSlot == -1 || cartSlot == -1) {
            return false;
        }
        int prevSlot = mc.field_1724.method_31548().field_7545;
        try {
            if (!isRail(placePos) && !placeRail(placePos, railSlot)) {
                mc.field_1724.method_31548().field_7545 = prevSlot;
                mc.field_1761.leonware$syncSelectedSlot();
                return false;
            }
            if (hasTntMinecart(placePos)) {
                mc.field_1724.method_31548().field_7545 = prevSlot;
                mc.field_1761.leonware$syncSelectedSlot();
                return true;
            }
            placeTntMinecart(placePos, cartSlot);
            restockMinecartSlot(cartSlot);
            boolean placed = hasTntMinecart(placePos);
            mc.field_1724.method_31548().field_7545 = prevSlot;
            mc.field_1761.leonware$syncSelectedSlot();
            return placed;
        } catch (Throwable th) {
            mc.field_1724.method_31548().field_7545 = prevSlot;
            mc.field_1761.leonware$syncSelectedSlot();
            throw th;
        }
    }

    private boolean isRail(class_2338 pos) {
        return mc.field_1687 != null && mc.field_1687.method_8320(pos).method_26164(class_3481.field_15463);
    }

    private boolean canPlaceRailAt(class_2338 pos) {
        if (mc.field_1687 == null) {
            return false;
        }
        if (isRail(pos)) {
            return true;
        }
        if (!mc.field_1687.method_8320(pos).method_45474()) {
            return false;
        }
        class_2338 support = pos.method_10074();
        return mc.field_1687.method_8320(support).method_26206(mc.field_1687, support, class_2350.field_11036);
    }

    private boolean placeRail(class_2338 placePos, int railSlot) {
        if (mc.field_1724 == null || mc.field_1761 == null) {
            return false;
        }
        mc.field_1724.method_31548().field_7545 = railSlot;
        mc.field_1761.leonware$syncSelectedSlot();
        class_2338 support = placePos.method_10074();
        class_3965 hitResult = new class_3965(new class_243(((double) placePos.method_10263()) + 0.5d, placePos.method_10264(), ((double) placePos.method_10260()) + 0.5d), class_2350.field_11036, support, false);
        class_1269 result = mc.field_1761.method_2896(mc.field_1724, class_1268.field_5808, hitResult);
        if (result.method_23665()) {
            mc.field_1724.method_6104(class_1268.field_5808);
            return true;
        }
        return isRail(placePos);
    }

    private void placeTntMinecart(class_2338 railPos, int cartSlot) {
        if (mc.field_1724 == null || mc.field_1761 == null) {
            return;
        }
        mc.field_1724.method_31548().field_7545 = cartSlot;
        mc.field_1761.leonware$syncSelectedSlot();
        class_3965 hitResult = new class_3965(new class_243(((double) railPos.method_10263()) + 0.5d, ((double) railPos.method_10264()) + 0.15d, ((double) railPos.method_10260()) + 0.5d), class_2350.field_11036, railPos, false);
        class_1269 result = mc.field_1761.method_2896(mc.field_1724, class_1268.field_5808, hitResult);
        if (result.method_23665()) {
            mc.field_1724.method_6104(class_1268.field_5808);
        }
    }

    private void restockMinecartSlot(int hotbarSlot) {
        int invSlot;
        if (mc.field_1724 == null || mc.field_1761 == null || hotbarSlot < 0 || hotbarSlot > 8 || mc.field_1724.method_31548().method_5438(hotbarSlot).method_31574(class_1802.field_8069) || (invSlot = InventoryUtil.findItem(class_1802.field_8069, false)) == -1) {
            return;
        }
        mc.field_1761.method_2906(0, invSlot, hotbarSlot, class_1713.field_7791, mc.field_1724);
    }

    private boolean hasTntMinecart(class_2338 railPos) {
        if (mc.field_1687 == null) {
            return false;
        }
        class_238 checkBox = new class_238(railPos).method_1009(0.2d, 1.0d, 0.2d);
        for (class_1297 entity : mc.field_1687.method_8335((class_1297) null, checkBox)) {
            if (entity instanceof class_1701) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/AutoCartModule$LandingPrediction.class */
    private static final class LandingPrediction extends Record {
        private final class_2338 placePos;
        private final int ticksToImpact;

        private LandingPrediction(class_2338 placePos, int ticksToImpact) {
            this.placePos = placePos;
            this.ticksToImpact = ticksToImpact;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, LandingPrediction.class), LandingPrediction.class, "placePos;ticksToImpact", "FIELD:Lsweetie/leonware/client/features/modules/combat/AutoCartModule$LandingPrediction;->placePos:Lnet/minecraft/class_2338;", "FIELD:Lsweetie/leonware/client/features/modules/combat/AutoCartModule$LandingPrediction;->ticksToImpact:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, LandingPrediction.class), LandingPrediction.class, "placePos;ticksToImpact", "FIELD:Lsweetie/leonware/client/features/modules/combat/AutoCartModule$LandingPrediction;->placePos:Lnet/minecraft/class_2338;", "FIELD:Lsweetie/leonware/client/features/modules/combat/AutoCartModule$LandingPrediction;->ticksToImpact:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, LandingPrediction.class, Object.class), LandingPrediction.class, "placePos;ticksToImpact", "FIELD:Lsweetie/leonware/client/features/modules/combat/AutoCartModule$LandingPrediction;->placePos:Lnet/minecraft/class_2338;", "FIELD:Lsweetie/leonware/client/features/modules/combat/AutoCartModule$LandingPrediction;->ticksToImpact:I").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public class_2338 placePos() {
            return this.placePos;
        }

        public int ticksToImpact() {
            return this.ticksToImpact;
        }
    }
}

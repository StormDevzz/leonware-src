// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.combat;

import lombok.Generated;
import net.minecraft.class_1701;
import net.minecraft.class_238;
import net.minecraft.class_1713;
import net.minecraft.class_1269;
import net.minecraft.class_1268;
import net.minecraft.class_1922;
import net.minecraft.class_3481;
import net.minecraft.class_2350;
import net.minecraft.class_3486;
import net.minecraft.class_2374;
import net.minecraft.class_2338;
import net.minecraft.class_3965;
import net.minecraft.class_239;
import net.minecraft.class_3959;
import net.minecraft.class_1657;
import sweetie.leonware.inject.accessors.ClientPlayerInteractionManagerAccessor;
import sweetie.leonware.api.utils.player.InventoryUtil;
import net.minecraft.class_1802;
import java.util.Iterator;
import java.util.Collection;
import net.minecraft.class_2382;
import net.minecraft.class_243;
import net.minecraft.class_1685;
import net.minecraft.class_1665;
import net.minecraft.class_1297;
import sweetie.leonware.api.system.backend.KeyStorage;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.module.setting.Setting;
import java.util.HashSet;
import java.util.Set;
import sweetie.leonware.api.module.setting.BindSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Auto Cart", category = Category.COMBAT)
public class AutoCartModule extends Module
{
    private static final AutoCartModule instance;
    private static final int MAX_PREDICTION_TICKS = 80;
    private static final int BOW_CHARGE_TICKS = 10;
    private final SliderSetting range;
    private final BindSetting bowShootKey;
    private final Set<Integer> knownArrows;
    private final Set<Integer> processedArrows;
    private boolean bowShotQueued;
    private boolean bowCharging;
    private int bowChargeTime;
    private int bowPrevSlot;
    
    public AutoCartModule() {
        this.range = new SliderSetting("Range").value(5.0f).range(1.0f, 8.0f).step(0.1f);
        this.bowShootKey = new BindSetting("Bow Shoot Key").value(-999);
        this.knownArrows = new HashSet<Integer>();
        this.processedArrows = new HashSet<Integer>();
        this.bowPrevSlot = -1;
        this.addSettings(this.range, this.bowShootKey);
    }
    
    @Override
    public void onEnable() {
        this.knownArrows.clear();
        this.processedArrows.clear();
        this.resetBowState();
        this.cacheExistingArrows();
    }
    
    @Override
    public void onDisable() {
        this.knownArrows.clear();
        this.processedArrows.clear();
        if (AutoCartModule.mc.field_1690 != null) {
            AutoCartModule.mc.field_1690.field_1904.method_23481(false);
        }
        this.resetBowState();
    }
    
    @Override
    public void onEvent() {
        final EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener<TickEvent>(event -> this.onTick()));
        this.addEvents(tickEvent);
    }
    
    private void onTick() {
        if (AutoCartModule.mc.field_1724 == null || AutoCartModule.mc.field_1687 == null || AutoCartModule.mc.field_1761 == null) {
            return;
        }
        if (this.bowShootKey.getValue() != -999 && KeyStorage.isPressed(this.bowShootKey.getValue()) && !this.bowShotQueued) {
            this.bowShotQueued = true;
        }
        this.handleBowShoot();
        final Set<Integer> aliveArrows = new HashSet<Integer>();
        for (final class_1297 entity : AutoCartModule.mc.field_1687.method_18112()) {
            if (entity instanceof final class_1665 projectile) {
                if (projectile instanceof class_1685) {
                    continue;
                }
                if (projectile.method_24921() != AutoCartModule.mc.field_1724) {
                    continue;
                }
                final int id = projectile.method_5628();
                aliveArrows.add(id);
                this.knownArrows.add(id);
                if (this.processedArrows.contains(id)) {
                    continue;
                }
                if (!this.hasRequiredItemsInHotbar()) {
                    continue;
                }
                if (!this.shouldProcessArrow(projectile)) {
                    continue;
                }
                final LandingPrediction prediction = this.predictLanding(projectile);
                if (prediction == null) {
                    continue;
                }
                final double rangeSq = this.range.getValue() * this.range.getValue();
                if (AutoCartModule.mc.field_1724.method_5707(class_243.method_24953((class_2382)prediction.placePos())) > rangeSq) {
                    this.processedArrows.add(id);
                }
                else {
                    if (!this.placeCartAt(prediction.placePos()) && !projectile.method_24828()) {
                        continue;
                    }
                    this.processedArrows.add(id);
                }
            }
        }
        this.knownArrows.retainAll(aliveArrows);
        this.processedArrows.retainAll(aliveArrows);
    }
    
    private void handleBowShoot() {
        if (AutoCartModule.mc.field_1724 == null || AutoCartModule.mc.field_1761 == null || AutoCartModule.mc.field_1690 == null) {
            return;
        }
        if (this.bowCharging) {
            if (!AutoCartModule.mc.field_1724.method_6047().method_31574(class_1802.field_8102)) {
                this.finishBowShot(false);
                return;
            }
            ++this.bowChargeTime;
            if (this.bowChargeTime >= 10) {
                this.finishBowShot(true);
            }
        }
        else {
            if (!this.bowShotQueued) {
                return;
            }
            this.bowShotQueued = false;
            final int bowSlot = InventoryUtil.findItem(class_1802.field_8102, true);
            if (bowSlot == -1) {
                return;
            }
            this.bowPrevSlot = AutoCartModule.mc.field_1724.method_31548().field_7545;
            if (this.bowPrevSlot != bowSlot) {
                AutoCartModule.mc.field_1724.method_31548().field_7545 = bowSlot;
                ((ClientPlayerInteractionManagerAccessor)AutoCartModule.mc.field_1761).leonware$syncSelectedSlot();
            }
            AutoCartModule.mc.field_1690.field_1904.method_23481(true);
            this.bowCharging = true;
            this.bowChargeTime = 0;
        }
    }
    
    private void finishBowShot(final boolean releaseShot) {
        if (AutoCartModule.mc.field_1724 == null || AutoCartModule.mc.field_1761 == null || AutoCartModule.mc.field_1690 == null) {
            this.resetBowState();
            return;
        }
        if (releaseShot) {
            AutoCartModule.mc.field_1690.field_1904.method_23481(false);
            if (AutoCartModule.mc.field_1724.method_6115()) {
                AutoCartModule.mc.field_1761.method_2897((class_1657)AutoCartModule.mc.field_1724);
            }
        }
        if (this.bowPrevSlot >= 0 && this.bowPrevSlot <= 8 && AutoCartModule.mc.field_1724.method_31548().field_7545 != this.bowPrevSlot) {
            AutoCartModule.mc.field_1724.method_31548().field_7545 = this.bowPrevSlot;
            ((ClientPlayerInteractionManagerAccessor)AutoCartModule.mc.field_1761).leonware$syncSelectedSlot();
        }
        this.resetBowState();
    }
    
    private void resetBowState() {
        this.bowShotQueued = false;
        this.bowCharging = false;
        this.bowChargeTime = 0;
        this.bowPrevSlot = -1;
    }
    
    private void cacheExistingArrows() {
        if (AutoCartModule.mc.field_1724 == null || AutoCartModule.mc.field_1687 == null) {
            return;
        }
        for (final class_1297 entity : AutoCartModule.mc.field_1687.method_18112()) {
            if (entity instanceof final class_1665 projectile) {
                if (projectile instanceof class_1685) {
                    continue;
                }
                if (projectile.method_24921() != AutoCartModule.mc.field_1724) {
                    continue;
                }
                this.knownArrows.add(projectile.method_5628());
            }
        }
    }
    
    private boolean hasRequiredItemsInHotbar() {
        return InventoryUtil.findItem(class_1802.field_8129, true) != -1 && InventoryUtil.findItem(class_1802.field_8069, true) != -1;
    }
    
    private boolean shouldProcessArrow(final class_1665 projectile) {
        return !projectile.method_24828() && projectile.method_18798().method_1027() > 1.0E-4;
    }
    
    private LandingPrediction predictLanding(final class_1665 arrow) {
        if (AutoCartModule.mc.field_1687 == null) {
            return null;
        }
        class_243 pos = arrow.method_19538();
        class_243 motion = arrow.method_18798();
        int tick = 1;
        while (tick <= 80) {
            final class_243 prevPos = pos;
            pos = pos.method_1019(motion);
            motion = this.updateMotion(arrow, prevPos, motion);
            final class_3965 hit = AutoCartModule.mc.field_1687.method_17742(new class_3959(prevPos, pos, class_3959.class_3960.field_17558, class_3959.class_242.field_1348, (class_1297)arrow));
            if (hit.method_17783() != class_239.class_240.field_1333) {
                final class_2338 placePos = this.resolvePlacePos(hit);
                if (placePos != null) {
                    return new LandingPrediction(placePos, tick);
                }
                return null;
            }
            else {
                if (pos.field_1351 < AutoCartModule.mc.field_1687.method_31607() - 2) {
                    break;
                }
                ++tick;
            }
        }
        return null;
    }
    
    private class_243 updateMotion(final class_1665 arrow, final class_243 pos, final class_243 motion) {
        if (AutoCartModule.mc.field_1687 == null) {
            return motion;
        }
        final boolean inWater = AutoCartModule.mc.field_1687.method_8320(class_2338.method_49638((class_2374)pos)).method_26227().method_15767(class_3486.field_15517);
        final double drag = inWater ? 0.6 : 0.99;
        return motion.method_1021(drag).method_1031(0.0, -arrow.method_56989(), 0.0);
    }
    
    private class_2338 resolvePlacePos(final class_3965 hit) {
        if (AutoCartModule.mc.field_1687 == null) {
            return null;
        }
        final class_2338 basePos = hit.method_17777();
        if (this.isRail(basePos)) {
            return basePos;
        }
        final class_2338 preferred = (hit.method_17780() == class_2350.field_11036) ? basePos.method_10084() : basePos;
        if (this.canPlaceRailAt(preferred)) {
            return preferred;
        }
        final class_2338 up = basePos.method_10084();
        if (this.canPlaceRailAt(up)) {
            return up;
        }
        final class_2338 down = basePos.method_10074();
        if (this.canPlaceRailAt(down)) {
            return down;
        }
        return null;
    }
    
    private boolean placeCartAt(final class_2338 placePos) {
        if (AutoCartModule.mc.field_1724 == null || AutoCartModule.mc.field_1687 == null || AutoCartModule.mc.field_1761 == null) {
            return false;
        }
        final int railSlot = InventoryUtil.findItem(class_1802.field_8129, true);
        final int cartSlot = InventoryUtil.findItem(class_1802.field_8069, true);
        if (railSlot == -1 || cartSlot == -1) {
            return false;
        }
        final int prevSlot = AutoCartModule.mc.field_1724.method_31548().field_7545;
        boolean placed = false;
        try {
            if (!this.isRail(placePos) && !this.placeRail(placePos, railSlot)) {
                return false;
            }
            if (this.hasTntMinecart(placePos)) {
                return true;
            }
            this.placeTntMinecart(placePos, cartSlot);
            this.restockMinecartSlot(cartSlot);
            placed = this.hasTntMinecart(placePos);
        }
        finally {
            AutoCartModule.mc.field_1724.method_31548().field_7545 = prevSlot;
            ((ClientPlayerInteractionManagerAccessor)AutoCartModule.mc.field_1761).leonware$syncSelectedSlot();
        }
        return placed;
    }
    
    private boolean isRail(final class_2338 pos) {
        return AutoCartModule.mc.field_1687 != null && AutoCartModule.mc.field_1687.method_8320(pos).method_26164(class_3481.field_15463);
    }
    
    private boolean canPlaceRailAt(final class_2338 pos) {
        if (AutoCartModule.mc.field_1687 == null) {
            return false;
        }
        if (this.isRail(pos)) {
            return true;
        }
        if (!AutoCartModule.mc.field_1687.method_8320(pos).method_45474()) {
            return false;
        }
        final class_2338 support = pos.method_10074();
        return AutoCartModule.mc.field_1687.method_8320(support).method_26206((class_1922)AutoCartModule.mc.field_1687, support, class_2350.field_11036);
    }
    
    private boolean placeRail(final class_2338 placePos, final int railSlot) {
        if (AutoCartModule.mc.field_1724 == null || AutoCartModule.mc.field_1761 == null) {
            return false;
        }
        AutoCartModule.mc.field_1724.method_31548().field_7545 = railSlot;
        ((ClientPlayerInteractionManagerAccessor)AutoCartModule.mc.field_1761).leonware$syncSelectedSlot();
        final class_2338 support = placePos.method_10074();
        final class_3965 hitResult = new class_3965(new class_243(placePos.method_10263() + 0.5, (double)placePos.method_10264(), placePos.method_10260() + 0.5), class_2350.field_11036, support, false);
        final class_1269 result = AutoCartModule.mc.field_1761.method_2896(AutoCartModule.mc.field_1724, class_1268.field_5808, hitResult);
        if (result.method_23665()) {
            AutoCartModule.mc.field_1724.method_6104(class_1268.field_5808);
            return true;
        }
        return this.isRail(placePos);
    }
    
    private void placeTntMinecart(final class_2338 railPos, final int cartSlot) {
        if (AutoCartModule.mc.field_1724 == null || AutoCartModule.mc.field_1761 == null) {
            return;
        }
        AutoCartModule.mc.field_1724.method_31548().field_7545 = cartSlot;
        ((ClientPlayerInteractionManagerAccessor)AutoCartModule.mc.field_1761).leonware$syncSelectedSlot();
        final class_3965 hitResult = new class_3965(new class_243(railPos.method_10263() + 0.5, railPos.method_10264() + 0.15, railPos.method_10260() + 0.5), class_2350.field_11036, railPos, false);
        final class_1269 result = AutoCartModule.mc.field_1761.method_2896(AutoCartModule.mc.field_1724, class_1268.field_5808, hitResult);
        if (result.method_23665()) {
            AutoCartModule.mc.field_1724.method_6104(class_1268.field_5808);
        }
    }
    
    private void restockMinecartSlot(final int hotbarSlot) {
        if (AutoCartModule.mc.field_1724 == null || AutoCartModule.mc.field_1761 == null) {
            return;
        }
        if (hotbarSlot < 0 || hotbarSlot > 8) {
            return;
        }
        if (AutoCartModule.mc.field_1724.method_31548().method_5438(hotbarSlot).method_31574(class_1802.field_8069)) {
            return;
        }
        final int invSlot = InventoryUtil.findItem(class_1802.field_8069, false);
        if (invSlot == -1) {
            return;
        }
        AutoCartModule.mc.field_1761.method_2906(0, invSlot, hotbarSlot, class_1713.field_7791, (class_1657)AutoCartModule.mc.field_1724);
    }
    
    private boolean hasTntMinecart(final class_2338 railPos) {
        if (AutoCartModule.mc.field_1687 == null) {
            return false;
        }
        final class_238 checkBox = new class_238(railPos).method_1009(0.2, 1.0, 0.2);
        for (final class_1297 entity : AutoCartModule.mc.field_1687.method_8335((class_1297)null, checkBox)) {
            if (entity instanceof class_1701) {
                return true;
            }
        }
        return false;
    }
    
    @Generated
    public static AutoCartModule getInstance() {
        return AutoCartModule.instance;
    }
    
    static {
        instance = new AutoCartModule();
    }
    
    record LandingPrediction(class_2338 placePos, int ticksToImpact) {}
}

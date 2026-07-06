/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1293
 *  net.minecraft.class_1294
 *  net.minecraft.class_1799
 *  net.minecraft.class_2708
 *  net.minecraft.class_5134
 *  net.minecraft.class_9285
 *  net.minecraft.class_9285$class_9287
 *  net.minecraft.class_9334
 */
package sweetie.leonware.client.features.modules.movement.speed.modes;

import java.util.function.Supplier;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1799;
import net.minecraft.class_2708;
import net.minecraft.class_5134;
import net.minecraft.class_9285;
import net.minecraft.class_9334;
import sweetie.leonware.api.event.events.client.KeyEvent;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.module.setting.BindSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.player.MoveUtil;
import sweetie.leonware.client.features.modules.movement.speed.SpeedMode;

public class SpeedMetaHvH
extends SpeedMode {
    private final BooleanSetting damageBoost = new BooleanSetting("\u0411\u0443\u0441\u0442 \u043e\u0442 \u0443\u0440\u043e\u043d\u0430").value(false);
    private final SliderSetting damageBoostStr = new SliderSetting("\u0421\u0438\u043b\u0430 \u0431\u0443\u0441\u0442\u0430 \u043e\u0442 \u0443\u0440\u043e\u043d\u0430").value(Float.valueOf(0.1f)).range(0.01f, 0.3f).step(0.01f);
    private final SliderSetting damageBoostLen = new SliderSetting("\u0414\u043b\u0438\u043d\u0430 \u0431\u0443\u0441\u0442\u0430 \u043e\u0442 \u0443\u0440\u043e\u043d\u0430").value(Float.valueOf(300.0f)).range(100.0f, 1000.0f).step(100.0f);
    private final BooleanSetting keyBoost = new BooleanSetting("\u0411\u0443\u0441\u0442 \u043f\u043e \u043a\u043d\u043e\u043f\u043a\u0435").value(false);
    private final BindSetting keyBoostBind = new BindSetting("\u041a\u043d\u043e\u043f\u043a\u0430 \u0431\u0443\u0441\u0442\u0430").value(-999);
    private final SliderSetting keyBoostStr = new SliderSetting("\u0421\u0438\u043b\u0430 \u0431\u0443\u0441\u0442\u0430 \u043f\u043e \u043a\u043d\u043e\u043f\u043a\u0435").value(Float.valueOf(0.01f)).range(0.01f, 0.1f).step(0.01f);
    private final SliderSetting keyBoostLen = new SliderSetting("\u0414\u043b\u0438\u043d\u0430 \u0431\u0443\u0441\u0442\u0430 \u043f\u043e \u043a\u043d\u043e\u043f\u043a\u0435").value(Float.valueOf(300.0f)).range(100.0f, 1000.0f).step(100.0f);
    private final SliderSetting keyBoostDelay = new SliderSetting("\u0417\u0430\u0434\u0435\u0440\u0436\u043a\u0430 \u043c\u0435\u0436\u0434\u0443 \u0431\u0443\u0441\u0442\u0430\u043c\u0438").value(Float.valueOf(5000.0f)).range(1000.0f, 30000.0f).step(500.0f);
    private long damageBoostEnd = 0L;
    private float currentDamageBoost = 0.0f;
    private long keyBoostEnd = 0L;
    private float currentKeyBoost = 0.0f;
    private long lastKeyBoostEnd = 0L;
    private long lastFlagTime = 0L;
    private boolean wasInWater = false;
    private long waterExitTime = 0L;
    private long waterBlockTime = 0L;
    private boolean prevHurt = false;

    public SpeedMetaHvH(Supplier<Boolean> condition) {
        this.damageBoost.setVisible((Supplier)condition);
        this.damageBoostStr.setVisible(() -> (Boolean)condition.get() != false && (Boolean)this.damageBoost.getValue() != false);
        this.damageBoostLen.setVisible(() -> (Boolean)condition.get() != false && (Boolean)this.damageBoost.getValue() != false);
        this.keyBoost.setVisible((Supplier)condition);
        this.keyBoostBind.setVisible(() -> (Boolean)condition.get() != false && (Boolean)this.keyBoost.getValue() != false);
        this.keyBoostStr.setVisible(() -> (Boolean)condition.get() != false && (Boolean)this.keyBoost.getValue() != false);
        this.keyBoostLen.setVisible(() -> (Boolean)condition.get() != false && (Boolean)this.keyBoost.getValue() != false);
        this.keyBoostDelay.setVisible(() -> (Boolean)condition.get() != false && (Boolean)this.keyBoost.getValue() != false);
        this.addSettings(this.damageBoost, this.damageBoostStr, this.damageBoostLen, this.keyBoost, this.keyBoostBind, this.keyBoostStr, this.keyBoostLen, this.keyBoostDelay);
    }

    @Override
    public String getName() {
        return "MetaHvH";
    }

    @Override
    public void onEnable() {
        this.resetBoosts();
        this.wasInWater = false;
        this.prevHurt = false;
    }

    @Override
    public void onDisable() {
        this.resetBoosts();
    }

    @Override
    public void onUpdate() {
        if (SpeedMetaHvH.mc.field_1724 == null) {
            return;
        }
        if (this.isBlocked()) {
            this.resetBoosts();
            return;
        }
        this.handleWaterState();
        this.handleDamageBoost();
    }

    @Override
    public void onTravel() {
        if (SpeedMetaHvH.mc.field_1724 == null || SpeedMetaHvH.mc.field_1687 == null) {
            return;
        }
        if (this.isBlocked()) {
            return;
        }
        if (System.currentTimeMillis() < this.waterBlockTime) {
            return;
        }
        double speed = this.calculateSpeed();
        MoveUtil.setSpeed(speed);
    }

    public void onKey(KeyEvent.KeyEventData event) {
        if (!((Boolean)this.keyBoost.getValue()).booleanValue()) {
            return;
        }
        if (event.action() != 1) {
            return;
        }
        if (event.key() != ((Integer)this.keyBoostBind.getValue()).intValue()) {
            return;
        }
        long now = System.currentTimeMillis();
        long timeSinceLast = now - this.lastKeyBoostEnd;
        long delay = ((Float)this.keyBoostDelay.getValue()).longValue();
        if (this.keyBoostEnd == 0L || now >= this.keyBoostEnd && timeSinceLast >= delay) {
            this.currentKeyBoost = ((Float)this.keyBoostStr.getValue()).floatValue();
            this.keyBoostEnd = now + ((Float)this.keyBoostLen.getValue()).longValue();
        }
    }

    public void onPacket(PacketEvent.PacketEventData event) {
        if (event.packet() instanceof class_2708) {
            this.lastFlagTime = System.currentTimeMillis();
            this.resetBoosts();
        }
    }

    private boolean isBlocked() {
        if (SpeedMetaHvH.mc.field_1724.method_5715() && SpeedMetaHvH.mc.field_1724.method_18798().field_1351 < -0.005) {
            return true;
        }
        if (SpeedMetaHvH.mc.field_1724.method_31549().field_7479) {
            return true;
        }
        if (SpeedMetaHvH.mc.field_1724.method_6128()) {
            return true;
        }
        return SpeedMetaHvH.mc.field_1724.method_5765();
    }

    private void handleWaterState() {
        boolean inWater = SpeedMetaHvH.mc.field_1724.method_5799() || SpeedMetaHvH.mc.field_1724.method_5771() || SpeedMetaHvH.mc.field_1724.method_5681() || SpeedMetaHvH.mc.field_1724.method_6128();
        long now = System.currentTimeMillis();
        if (inWater) {
            if (!this.wasInWater) {
                this.wasInWater = true;
                this.resetBoosts();
            }
            this.waterExitTime = now;
        } else if (this.wasInWater) {
            this.wasInWater = false;
            this.resetBoosts();
            this.waterBlockTime = now + 600L;
        }
    }

    private void handleDamageBoost() {
        boolean hurt;
        if (!((Boolean)this.damageBoost.getValue()).booleanValue()) {
            return;
        }
        boolean bl = hurt = SpeedMetaHvH.mc.field_1724.field_6235 > 0;
        if (hurt && !this.prevHurt) {
            this.currentDamageBoost = ((Float)this.damageBoostStr.getValue()).floatValue();
            this.damageBoostEnd = System.currentTimeMillis() + ((Float)this.damageBoostLen.getValue()).longValue();
        }
        this.prevHurt = hurt;
    }

    private double calculateSpeed() {
        long now = System.currentTimeMillis();
        float base = 0.39f;
        base *= 1.0f + this.getItemSpeedModifier(SpeedMetaHvH.mc.field_1724.method_6079());
        base += base * this.getEffectSpeedModifier();
        base -= base * this.getEffectSlowModifier();
        if (now < this.damageBoostEnd) {
            base += this.currentDamageBoost;
        } else {
            this.currentDamageBoost = 0.0f;
        }
        if (now < this.keyBoostEnd) {
            base += this.currentKeyBoost;
        } else {
            this.currentKeyBoost = 0.0f;
            if (this.keyBoostEnd != 0L && now >= this.keyBoostEnd) {
                this.lastKeyBoostEnd = this.keyBoostEnd;
                this.keyBoostEnd = 0L;
            }
        }
        float recovery = this.getRecoveryFactor(now);
        base *= recovery;
        base -= base * this.getHelmetSlowModifier() * 1.25f;
        return base;
    }

    private float getItemSpeedModifier(class_1799 stack) {
        if (stack.method_7960()) {
            return 0.0f;
        }
        float modifier = 0.0f;
        class_9285 attrModifiers = (class_9285)stack.method_57824(class_9334.field_49636);
        if (attrModifiers == null) {
            return 0.0f;
        }
        for (class_9285.class_9287 entry : attrModifiers.comp_2393()) {
            if (entry.comp_2395().comp_349() != class_5134.field_23719) continue;
            modifier = (float)((double)modifier + entry.comp_2396().comp_2449());
        }
        return modifier;
    }

    private float getEffectSpeedModifier() {
        class_1293 effect = SpeedMetaHvH.mc.field_1724.method_6112(class_1294.field_5904);
        if (effect != null) {
            return 0.201f * (float)(effect.method_5578() + 1);
        }
        return 0.0f;
    }

    private float getEffectSlowModifier() {
        class_1293 effect = SpeedMetaHvH.mc.field_1724.method_6112(class_1294.field_5909);
        if (effect != null) {
            return 0.16f * (float)(effect.method_5578() + 1);
        }
        return 0.0f;
    }

    private float getHelmetSlowModifier() {
        class_1799 helmet = SpeedMetaHvH.mc.field_1724.method_31548().method_7372(3);
        if (helmet.method_7960()) {
            return 0.0f;
        }
        float modifier = 0.0f;
        class_9285 attrModifiers = (class_9285)helmet.method_57824(class_9334.field_49636);
        if (attrModifiers == null) {
            return 0.0f;
        }
        for (class_9285.class_9287 entry : attrModifiers.comp_2393()) {
            if (entry.comp_2395().comp_349() != class_5134.field_23719 || !(entry.comp_2396().comp_2449() < 0.0)) continue;
            modifier = (float)((double)modifier + Math.abs(entry.comp_2396().comp_2449()));
        }
        return modifier;
    }

    private float getRecoveryFactor(long now) {
        long sinceWater;
        float factor = 1.0f;
        long sinceFlag = now - this.lastFlagTime;
        if (sinceFlag >= 0L && sinceFlag < 1200L) {
            float t = (float)sinceFlag / 1200.0f;
            factor *= 0.35f + 0.65f * t;
        }
        if ((sinceWater = now - this.waterExitTime) >= 0L && sinceWater < 1100L) {
            float t = (float)sinceWater / 1100.0f;
            factor *= 0.4f + 0.6f * t;
        }
        return factor;
    }

    private void resetBoosts() {
        this.damageBoostEnd = 0L;
        this.currentDamageBoost = 0.0f;
        this.keyBoostEnd = 0L;
        this.currentKeyBoost = 0.0f;
    }
}


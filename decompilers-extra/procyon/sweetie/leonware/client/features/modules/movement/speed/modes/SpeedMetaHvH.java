// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement.speed.modes;

import net.minecraft.class_1293;
import net.minecraft.class_1294;
import java.util.Iterator;
import net.minecraft.class_5134;
import net.minecraft.class_9334;
import net.minecraft.class_9285;
import net.minecraft.class_1799;
import net.minecraft.class_2708;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.events.client.KeyEvent;
import sweetie.leonware.api.utils.player.MoveUtil;
import sweetie.leonware.api.module.setting.Setting;
import java.util.function.Supplier;
import sweetie.leonware.api.module.setting.BindSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.client.features.modules.movement.speed.SpeedMode;

public class SpeedMetaHvH extends SpeedMode
{
    private final BooleanSetting damageBoost;
    private final SliderSetting damageBoostStr;
    private final SliderSetting damageBoostLen;
    private final BooleanSetting keyBoost;
    private final BindSetting keyBoostBind;
    private final SliderSetting keyBoostStr;
    private final SliderSetting keyBoostLen;
    private final SliderSetting keyBoostDelay;
    private long damageBoostEnd;
    private float currentDamageBoost;
    private long keyBoostEnd;
    private float currentKeyBoost;
    private long lastKeyBoostEnd;
    private long lastFlagTime;
    private boolean wasInWater;
    private long waterExitTime;
    private long waterBlockTime;
    private boolean prevHurt;
    
    public SpeedMetaHvH(final Supplier<Boolean> condition) {
        this.damageBoost = new BooleanSetting("\u0411\u0443\u0441\u0442 \u043e\u0442 \u0443\u0440\u043e\u043d\u0430").value(false);
        this.damageBoostStr = new SliderSetting("\u0421\u0438\u043b\u0430 \u0431\u0443\u0441\u0442\u0430 \u043e\u0442 \u0443\u0440\u043e\u043d\u0430").value(0.1f).range(0.01f, 0.3f).step(0.01f);
        this.damageBoostLen = new SliderSetting("\u0414\u043b\u0438\u043d\u0430 \u0431\u0443\u0441\u0442\u0430 \u043e\u0442 \u0443\u0440\u043e\u043d\u0430").value(300.0f).range(100.0f, 1000.0f).step(100.0f);
        this.keyBoost = new BooleanSetting("\u0411\u0443\u0441\u0442 \u043f\u043e \u043a\u043d\u043e\u043f\u043a\u0435").value(false);
        this.keyBoostBind = new BindSetting("\u041a\u043d\u043e\u043f\u043a\u0430 \u0431\u0443\u0441\u0442\u0430").value(-999);
        this.keyBoostStr = new SliderSetting("\u0421\u0438\u043b\u0430 \u0431\u0443\u0441\u0442\u0430 \u043f\u043e \u043a\u043d\u043e\u043f\u043a\u0435").value(0.01f).range(0.01f, 0.1f).step(0.01f);
        this.keyBoostLen = new SliderSetting("\u0414\u043b\u0438\u043d\u0430 \u0431\u0443\u0441\u0442\u0430 \u043f\u043e \u043a\u043d\u043e\u043f\u043a\u0435").value(300.0f).range(100.0f, 1000.0f).step(100.0f);
        this.keyBoostDelay = new SliderSetting("\u0417\u0430\u0434\u0435\u0440\u0436\u043a\u0430 \u043c\u0435\u0436\u0434\u0443 \u0431\u0443\u0441\u0442\u0430\u043c\u0438").value(5000.0f).range(1000.0f, 30000.0f).step(500.0f);
        this.damageBoostEnd = 0L;
        this.currentDamageBoost = 0.0f;
        this.keyBoostEnd = 0L;
        this.currentKeyBoost = 0.0f;
        this.lastKeyBoostEnd = 0L;
        this.lastFlagTime = 0L;
        this.wasInWater = false;
        this.waterExitTime = 0L;
        this.waterBlockTime = 0L;
        this.prevHurt = false;
        this.damageBoost.setVisible(condition);
        this.damageBoostStr.setVisible(() -> condition.get() && this.damageBoost.getValue());
        this.damageBoostLen.setVisible(() -> condition.get() && this.damageBoost.getValue());
        this.keyBoost.setVisible(condition);
        this.keyBoostBind.setVisible(() -> condition.get() && this.keyBoost.getValue());
        this.keyBoostStr.setVisible(() -> condition.get() && this.keyBoost.getValue());
        this.keyBoostLen.setVisible(() -> condition.get() && this.keyBoost.getValue());
        this.keyBoostDelay.setVisible(() -> condition.get() && this.keyBoost.getValue());
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
        final double speed = this.calculateSpeed();
        MoveUtil.setSpeed(speed);
    }
    
    public void onKey(final KeyEvent.KeyEventData event) {
        if (!this.keyBoost.getValue()) {
            return;
        }
        if (event.action() != 1) {
            return;
        }
        if (event.key() != this.keyBoostBind.getValue()) {
            return;
        }
        final long now = System.currentTimeMillis();
        final long timeSinceLast = now - this.lastKeyBoostEnd;
        final long delay = this.keyBoostDelay.getValue().longValue();
        if (this.keyBoostEnd == 0L || (now >= this.keyBoostEnd && timeSinceLast >= delay)) {
            this.currentKeyBoost = this.keyBoostStr.getValue();
            this.keyBoostEnd = now + this.keyBoostLen.getValue().longValue();
        }
    }
    
    public void onPacket(final PacketEvent.PacketEventData event) {
        if (event.packet() instanceof class_2708) {
            this.lastFlagTime = System.currentTimeMillis();
            this.resetBoosts();
        }
    }
    
    private boolean isBlocked() {
        return (SpeedMetaHvH.mc.field_1724.method_5715() && SpeedMetaHvH.mc.field_1724.method_18798().field_1351 < -0.005) || SpeedMetaHvH.mc.field_1724.method_31549().field_7479 || SpeedMetaHvH.mc.field_1724.method_6128() || SpeedMetaHvH.mc.field_1724.method_5765();
    }
    
    private void handleWaterState() {
        final boolean inWater = SpeedMetaHvH.mc.field_1724.method_5799() || SpeedMetaHvH.mc.field_1724.method_5771() || SpeedMetaHvH.mc.field_1724.method_5681() || SpeedMetaHvH.mc.field_1724.method_6128();
        final long now = System.currentTimeMillis();
        if (inWater) {
            if (!this.wasInWater) {
                this.wasInWater = true;
                this.resetBoosts();
            }
            this.waterExitTime = now;
        }
        else if (this.wasInWater) {
            this.wasInWater = false;
            this.resetBoosts();
            this.waterBlockTime = now + 600L;
        }
    }
    
    private void handleDamageBoost() {
        if (!this.damageBoost.getValue()) {
            return;
        }
        final boolean hurt = SpeedMetaHvH.mc.field_1724.field_6235 > 0;
        if (hurt && !this.prevHurt) {
            this.currentDamageBoost = this.damageBoostStr.getValue();
            this.damageBoostEnd = System.currentTimeMillis() + this.damageBoostLen.getValue().longValue();
        }
        this.prevHurt = hurt;
    }
    
    private double calculateSpeed() {
        final long now = System.currentTimeMillis();
        float base = 0.39f;
        base *= 1.0f + this.getItemSpeedModifier(SpeedMetaHvH.mc.field_1724.method_6079());
        base += base * this.getEffectSpeedModifier();
        base -= base * this.getEffectSlowModifier();
        if (now < this.damageBoostEnd) {
            base += this.currentDamageBoost;
        }
        else {
            this.currentDamageBoost = 0.0f;
        }
        if (now < this.keyBoostEnd) {
            base += this.currentKeyBoost;
        }
        else {
            this.currentKeyBoost = 0.0f;
            if (this.keyBoostEnd != 0L && now >= this.keyBoostEnd) {
                this.lastKeyBoostEnd = this.keyBoostEnd;
                this.keyBoostEnd = 0L;
            }
        }
        final float recovery = this.getRecoveryFactor(now);
        base *= recovery;
        base -= base * this.getHelmetSlowModifier() * 1.25f;
        return base;
    }
    
    private float getItemSpeedModifier(final class_1799 stack) {
        if (stack.method_7960()) {
            return 0.0f;
        }
        float modifier = 0.0f;
        final class_9285 attrModifiers = (class_9285)stack.method_57824(class_9334.field_49636);
        if (attrModifiers == null) {
            return 0.0f;
        }
        for (final class_9285.class_9287 entry : attrModifiers.comp_2393()) {
            if (entry.comp_2395().comp_349() == class_5134.field_23719) {
                modifier += (float)entry.comp_2396().comp_2449();
            }
        }
        return modifier;
    }
    
    private float getEffectSpeedModifier() {
        final class_1293 effect = SpeedMetaHvH.mc.field_1724.method_6112(class_1294.field_5904);
        if (effect != null) {
            return 0.201f * (effect.method_5578() + 1);
        }
        return 0.0f;
    }
    
    private float getEffectSlowModifier() {
        final class_1293 effect = SpeedMetaHvH.mc.field_1724.method_6112(class_1294.field_5909);
        if (effect != null) {
            return 0.16f * (effect.method_5578() + 1);
        }
        return 0.0f;
    }
    
    private float getHelmetSlowModifier() {
        final class_1799 helmet = SpeedMetaHvH.mc.field_1724.method_31548().method_7372(3);
        if (helmet.method_7960()) {
            return 0.0f;
        }
        float modifier = 0.0f;
        final class_9285 attrModifiers = (class_9285)helmet.method_57824(class_9334.field_49636);
        if (attrModifiers == null) {
            return 0.0f;
        }
        for (final class_9285.class_9287 entry : attrModifiers.comp_2393()) {
            if (entry.comp_2395().comp_349() == class_5134.field_23719 && entry.comp_2396().comp_2449() < 0.0) {
                modifier += (float)Math.abs(entry.comp_2396().comp_2449());
            }
        }
        return modifier;
    }
    
    private float getRecoveryFactor(final long now) {
        float factor = 1.0f;
        final long sinceFlag = now - this.lastFlagTime;
        if (sinceFlag >= 0L && sinceFlag < 1200L) {
            final float t = sinceFlag / 1200.0f;
            factor *= 0.35f + 0.65f * t;
        }
        final long sinceWater = now - this.waterExitTime;
        if (sinceWater >= 0L && sinceWater < 1100L) {
            final float t2 = sinceWater / 1100.0f;
            factor *= 0.4f + 0.6f * t2;
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

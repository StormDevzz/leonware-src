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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/speed/modes/SpeedMetaHvH.class */
public class SpeedMetaHvH extends SpeedMode {
    private final BooleanSetting damageBoost = new BooleanSetting("Буст от урона").value((Boolean) false);
    private final SliderSetting damageBoostStr = new SliderSetting("Сила буста от урона").value(Float.valueOf(0.1f)).range(0.01f, 0.3f).step(0.01f);
    private final SliderSetting damageBoostLen = new SliderSetting("Длина буста от урона").value(Float.valueOf(300.0f)).range(100.0f, 1000.0f).step(100.0f);
    private final BooleanSetting keyBoost = new BooleanSetting("Буст по кнопке").value((Boolean) false);
    private final BindSetting keyBoostBind = new BindSetting("Кнопка буста").value((Integer) (-999));
    private final SliderSetting keyBoostStr = new SliderSetting("Сила буста по кнопке").value(Float.valueOf(0.01f)).range(0.01f, 0.1f).step(0.01f);
    private final SliderSetting keyBoostLen = new SliderSetting("Длина буста по кнопке").value(Float.valueOf(300.0f)).range(100.0f, 1000.0f).step(100.0f);
    private final SliderSetting keyBoostDelay = new SliderSetting("Задержка между бустами").value(Float.valueOf(5000.0f)).range(1000.0f, 30000.0f).step(500.0f);
    private long damageBoostEnd = 0;
    private float currentDamageBoost = 0.0f;
    private long keyBoostEnd = 0;
    private float currentKeyBoost = 0.0f;
    private long lastKeyBoostEnd = 0;
    private long lastFlagTime = 0;
    private boolean wasInWater = false;
    private long waterExitTime = 0;
    private long waterBlockTime = 0;
    private boolean prevHurt = false;

    public SpeedMetaHvH(Supplier<Boolean> condition) {
        this.damageBoost.setVisible(condition);
        this.damageBoostStr.setVisible(() -> {
            return Boolean.valueOf(((Boolean) condition.get()).booleanValue() && this.damageBoost.getValue().booleanValue());
        });
        this.damageBoostLen.setVisible(() -> {
            return Boolean.valueOf(((Boolean) condition.get()).booleanValue() && this.damageBoost.getValue().booleanValue());
        });
        this.keyBoost.setVisible(condition);
        this.keyBoostBind.setVisible(() -> {
            return Boolean.valueOf(((Boolean) condition.get()).booleanValue() && this.keyBoost.getValue().booleanValue());
        });
        this.keyBoostStr.setVisible(() -> {
            return Boolean.valueOf(((Boolean) condition.get()).booleanValue() && this.keyBoost.getValue().booleanValue());
        });
        this.keyBoostLen.setVisible(() -> {
            return Boolean.valueOf(((Boolean) condition.get()).booleanValue() && this.keyBoost.getValue().booleanValue());
        });
        this.keyBoostDelay.setVisible(() -> {
            return Boolean.valueOf(((Boolean) condition.get()).booleanValue() && this.keyBoost.getValue().booleanValue());
        });
        addSettings(this.damageBoost, this.damageBoostStr, this.damageBoostLen, this.keyBoost, this.keyBoostBind, this.keyBoostStr, this.keyBoostLen, this.keyBoostDelay);
    }

    @Override // sweetie.leonware.api.system.backend.Choice, sweetie.leonware.api.module.setting.ModeSetting.NamedChoice
    public String getName() {
        return "MetaHvH";
    }

    @Override // sweetie.leonware.client.features.modules.movement.speed.SpeedMode
    public void onEnable() {
        resetBoosts();
        this.wasInWater = false;
        this.prevHurt = false;
    }

    @Override // sweetie.leonware.client.features.modules.movement.speed.SpeedMode
    public void onDisable() {
        resetBoosts();
    }

    @Override // sweetie.leonware.client.features.modules.movement.speed.SpeedMode
    public void onUpdate() {
        if (mc.field_1724 == null) {
            return;
        }
        if (isBlocked()) {
            resetBoosts();
        } else {
            handleWaterState();
            handleDamageBoost();
        }
    }

    @Override // sweetie.leonware.client.features.modules.movement.speed.SpeedMode
    public void onTravel() {
        if (mc.field_1724 == null || mc.field_1687 == null || isBlocked() || System.currentTimeMillis() < this.waterBlockTime) {
            return;
        }
        double speed = calculateSpeed();
        MoveUtil.setSpeed(speed);
    }

    public void onKey(KeyEvent.KeyEventData event) {
        if (this.keyBoost.getValue().booleanValue() && event.action() == 1 && event.key() == this.keyBoostBind.getValue().intValue()) {
            long now = System.currentTimeMillis();
            long timeSinceLast = now - this.lastKeyBoostEnd;
            long delay = this.keyBoostDelay.getValue().longValue();
            if (this.keyBoostEnd == 0 || (now >= this.keyBoostEnd && timeSinceLast >= delay)) {
                this.currentKeyBoost = this.keyBoostStr.getValue().floatValue();
                this.keyBoostEnd = now + this.keyBoostLen.getValue().longValue();
            }
        }
    }

    public void onPacket(PacketEvent.PacketEventData event) {
        if (event.packet() instanceof class_2708) {
            this.lastFlagTime = System.currentTimeMillis();
            resetBoosts();
        }
    }

    private boolean isBlocked() {
        return (mc.field_1724.method_5715() && mc.field_1724.method_18798().field_1351 < -0.005d) || mc.field_1724.method_31549().field_7479 || mc.field_1724.method_6128() || mc.field_1724.method_5765();
    }

    private void handleWaterState() {
        boolean inWater = mc.field_1724.method_5799() || mc.field_1724.method_5771() || mc.field_1724.method_5681() || mc.field_1724.method_6128();
        long now = System.currentTimeMillis();
        if (inWater) {
            if (!this.wasInWater) {
                this.wasInWater = true;
                resetBoosts();
            }
            this.waterExitTime = now;
            return;
        }
        if (this.wasInWater) {
            this.wasInWater = false;
            resetBoosts();
            this.waterBlockTime = now + 600;
        }
    }

    private void handleDamageBoost() {
        if (this.damageBoost.getValue().booleanValue()) {
            boolean hurt = mc.field_1724.field_6235 > 0;
            if (hurt && !this.prevHurt) {
                this.currentDamageBoost = this.damageBoostStr.getValue().floatValue();
                this.damageBoostEnd = System.currentTimeMillis() + this.damageBoostLen.getValue().longValue();
            }
            this.prevHurt = hurt;
        }
    }

    private double calculateSpeed() {
        long now = System.currentTimeMillis();
        float base = 0.39f * (1.0f + getItemSpeedModifier(mc.field_1724.method_6079()));
        float base2 = base + (base * getEffectSpeedModifier());
        float base3 = base2 - (base2 * getEffectSlowModifier());
        if (now < this.damageBoostEnd) {
            base3 += this.currentDamageBoost;
        } else {
            this.currentDamageBoost = 0.0f;
        }
        if (now < this.keyBoostEnd) {
            base3 += this.currentKeyBoost;
        } else {
            this.currentKeyBoost = 0.0f;
            if (this.keyBoostEnd != 0 && now >= this.keyBoostEnd) {
                this.lastKeyBoostEnd = this.keyBoostEnd;
                this.keyBoostEnd = 0L;
            }
        }
        float recovery = getRecoveryFactor(now);
        float base4 = base3 * recovery;
        return base4 - ((base4 * getHelmetSlowModifier()) * 1.25f);
    }

    private float getItemSpeedModifier(class_1799 stack) {
        if (stack.method_7960()) {
            return 0.0f;
        }
        float modifier = 0.0f;
        class_9285 attrModifiers = (class_9285) stack.method_57824(class_9334.field_49636);
        if (attrModifiers == null) {
            return 0.0f;
        }
        for (class_9285.class_9287 entry : attrModifiers.comp_2393()) {
            if (entry.comp_2395().comp_349() == class_5134.field_23719) {
                modifier = (float) (((double) modifier) + entry.comp_2396().comp_2449());
            }
        }
        return modifier;
    }

    private float getEffectSpeedModifier() {
        class_1293 effect = mc.field_1724.method_6112(class_1294.field_5904);
        if (effect != null) {
            return 0.201f * (effect.method_5578() + 1);
        }
        return 0.0f;
    }

    private float getEffectSlowModifier() {
        class_1293 effect = mc.field_1724.method_6112(class_1294.field_5909);
        if (effect != null) {
            return 0.16f * (effect.method_5578() + 1);
        }
        return 0.0f;
    }

    private float getHelmetSlowModifier() {
        class_1799 helmet = mc.field_1724.method_31548().method_7372(3);
        if (helmet.method_7960()) {
            return 0.0f;
        }
        float modifier = 0.0f;
        class_9285 attrModifiers = (class_9285) helmet.method_57824(class_9334.field_49636);
        if (attrModifiers == null) {
            return 0.0f;
        }
        for (class_9285.class_9287 entry : attrModifiers.comp_2393()) {
            if (entry.comp_2395().comp_349() == class_5134.field_23719 && entry.comp_2396().comp_2449() < 0.0d) {
                modifier = (float) (((double) modifier) + Math.abs(entry.comp_2396().comp_2449()));
            }
        }
        return modifier;
    }

    private float getRecoveryFactor(long now) {
        float factor = 1.0f;
        long sinceFlag = now - this.lastFlagTime;
        if (sinceFlag >= 0 && sinceFlag < 1200) {
            float t = sinceFlag / 1200.0f;
            factor = 1.0f * (0.35f + (0.65f * t));
        }
        long sinceWater = now - this.waterExitTime;
        if (sinceWater >= 0 && sinceWater < 1100) {
            float t2 = sinceWater / 1100.0f;
            factor *= 0.4f + (0.6f * t2);
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

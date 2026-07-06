package sweetie.leonware.client.features.modules.movement;

import java.util.Objects;
import lombok.Generated;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import net.minecraft.class_2627;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.math.TimerUtil;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/ShulkerHighJumpModule.class */
@ModuleRegister(name = "Shulker High Jump", category = Category.MOVEMENT)
public class ShulkerHighJumpModule extends Module {
    private static final ShulkerHighJumpModule instance = new ShulkerHighJumpModule();
    private final SliderSetting cooldown = new SliderSetting("Перезарядка").value(Float.valueOf(100.0f)).range(0.0f, 1000.0f).step(10.0f);
    private final BooleanSetting useYChange = new BooleanSetting("Изменять высоту").value((Boolean) true);
    private final SliderSetting jumpHeight;
    private final BooleanSetting jumpX2;
    private final SliderSetting doubleJumpDelay;
    private final TimerUtil jumpTimer;
    private final TimerUtil doubleJumpTimer;
    private boolean hasJumped;

    @Generated
    public static ShulkerHighJumpModule getInstance() {
        return instance;
    }

    /* JADX WARN: Type inference failed for: r1v10, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v17, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    public ShulkerHighJumpModule() {
        SliderSetting sliderSettingStep = new SliderSetting("Сила прыжка").value(Float.valueOf(2.329f)).range(0.1f, 5.0f).step(0.1f);
        BooleanSetting booleanSetting = this.useYChange;
        Objects.requireNonNull(booleanSetting);
        this.jumpHeight = sliderSettingStep.setVisible(booleanSetting::getValue);
        this.jumpX2 = new BooleanSetting("Второй прыжок").value((Boolean) false);
        SliderSetting sliderSettingStep2 = new SliderSetting("Задержка").value(Float.valueOf(500.0f)).range(30.0f, 2000.0f).step(10.0f);
        BooleanSetting booleanSetting2 = this.jumpX2;
        Objects.requireNonNull(booleanSetting2);
        this.doubleJumpDelay = sliderSettingStep2.setVisible(booleanSetting2::getValue);
        this.jumpTimer = new TimerUtil();
        this.doubleJumpTimer = new TimerUtil();
        this.hasJumped = false;
        addSettings(this.cooldown, this.useYChange, this.jumpHeight, this.jumpX2, this.doubleJumpDelay);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onEnable() {
        this.hasJumped = false;
        this.jumpTimer.reset();
        this.doubleJumpTimer.reset();
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        this.hasJumped = false;
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1724 == null || mc.field_1687 == null || mc.field_1724.method_52535() || mc.field_1724.method_5715() || mc.field_1724.method_31549().field_7479 || mc.field_1724.method_3144() || !this.jumpTimer.finished(this.cooldown.getValue().longValue())) {
                return;
            }
            class_2338 playerPos = mc.field_1724.method_24515();
            for (int x = -3; x <= 3; x++) {
                for (int y = -3; y <= 3; y++) {
                    for (int z = -3; z <= 3; z++) {
                        class_2338 pos = playerPos.method_10069(x, y, z);
                        class_2627 class_2627VarMethod_8321 = mc.field_1687.method_8321(pos);
                        if (class_2627VarMethod_8321 instanceof class_2627) {
                            class_2627 shulkerBox = class_2627VarMethod_8321;
                            double distance = Math.sqrt(Math.pow(mc.field_1724.method_23317() - (((double) pos.method_10263()) + 0.5d), 2.0d) + Math.pow(mc.field_1724.method_23321() - (((double) pos.method_10260()) + 0.5d), 2.0d));
                            double heightDiff = Math.abs(mc.field_1724.method_23318() - (((double) pos.method_10264()) + 0.5d));
                            double currentMaxH = mc.field_1724.method_18798().field_1351 > 1.0d ? 2.0d * 15.0d : 2.0d;
                            if (distance <= 1.0d && heightDiff <= currentMaxH && mc.field_1724.field_6017 == 0.0f) {
                                float progress = shulkerBox.method_11312(1.0f);
                                if (progress > 0.0f && progress != 1.0f) {
                                    performJump();
                                }
                            }
                        }
                    }
                }
            }
            if (this.jumpX2.getValue().booleanValue() && this.hasJumped && this.doubleJumpTimer.finished(this.doubleJumpDelay.getValue().longValue())) {
                performDoubleJump();
                this.hasJumped = false;
            }
            if (this.hasJumped && mc.field_1724.method_24828()) {
                this.hasJumped = false;
            }
        }));
        addEvents(updateEvent);
    }

    private void performJump() {
        if (this.hasJumped) {
            return;
        }
        class_243 velocity = mc.field_1724.method_18798();
        double newY = velocity.field_1351;
        if (this.useYChange.getValue().booleanValue()) {
            newY = this.jumpHeight.getValue().floatValue();
        }
        mc.field_1724.method_18800(velocity.field_1352, newY, velocity.field_1350);
        this.hasJumped = true;
        this.jumpTimer.reset();
        this.doubleJumpTimer.reset();
    }

    private void performDoubleJump() {
        class_243 velocity = mc.field_1724.method_18798();
        double newY = velocity.field_1351;
        if (this.useYChange.getValue().booleanValue()) {
            newY = this.jumpHeight.getValue().floatValue();
        }
        mc.field_1724.method_18800(velocity.field_1352, newY, velocity.field_1350);
        this.doubleJumpTimer.reset();
    }
}

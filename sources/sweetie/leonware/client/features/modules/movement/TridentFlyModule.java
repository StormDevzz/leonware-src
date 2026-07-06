package sweetie.leonware.client.features.modules.movement;

import java.util.Objects;
import lombok.Generated;
import net.minecraft.class_1802;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2846;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/TridentFlyModule.class */
@ModuleRegister(name = "Trident Fly", category = Category.MOVEMENT)
public class TridentFlyModule extends Module {
    private static final TridentFlyModule instance = new TridentFlyModule();
    private final BooleanSetting allowNoWater = new BooleanSetting("Allow No Water").value((Boolean) true);
    private final BooleanSetting instant = new BooleanSetting("Instant").value((Boolean) true);
    private final BooleanSetting spam = new BooleanSetting("Spam").value((Boolean) false);
    private final SliderSetting ticks;
    private boolean wasUsingTrident;

    @Generated
    public static TridentFlyModule getInstance() {
        return instance;
    }

    /* JADX WARN: Type inference failed for: r1v10, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    public TridentFlyModule() {
        SliderSetting sliderSettingStep = new SliderSetting("Ticks").value(Float.valueOf(3.0f)).range(0.0f, 20.0f).step(1.0f);
        BooleanSetting booleanSetting = this.spam;
        Objects.requireNonNull(booleanSetting);
        this.ticks = sliderSettingStep.setVisible(booleanSetting::getValue);
        this.wasUsingTrident = false;
        addSettings(this.allowNoWater, this.instant, this.spam, this.ticks);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener(event -> {
            handleTridentSpam();
        }));
        addEvents(tickEvent);
    }

    private void handleTridentSpam() {
        if (!this.spam.getValue().booleanValue() || mc.field_1724 == null) {
            return;
        }
        boolean isUsingTrident = mc.field_1724.method_6115() && mc.field_1724.method_6047().method_7909() == class_1802.field_8547;
        if (isUsingTrident && !this.wasUsingTrident) {
            this.wasUsingTrident = true;
            return;
        }
        if (this.wasUsingTrident && !isUsingTrident) {
            this.wasUsingTrident = false;
        } else if (isUsingTrident && mc.field_1724.method_6048() >= this.ticks.getValue().floatValue()) {
            sendPacket(new class_2846(class_2846.class_2847.field_12974, class_2338.field_10980, class_2350.field_11033));
            mc.field_1724.method_6075();
            this.wasUsingTrident = false;
        }
    }

    public boolean shouldAllowNoWater() {
        return isEnabled() && this.allowNoWater.getValue().booleanValue();
    }

    public boolean shouldInstantPullback() {
        return isEnabled() && this.instant.getValue().booleanValue();
    }

    private void sendPacket(class_2846 packet) {
        if (mc.method_1562() != null) {
            mc.method_1562().method_52787(packet);
        }
    }
}

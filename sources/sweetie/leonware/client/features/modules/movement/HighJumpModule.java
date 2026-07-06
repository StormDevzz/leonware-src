package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/HighJumpModule.class */
@ModuleRegister(name = "High Jump", category = Category.MOVEMENT)
public class HighJumpModule extends Module {
    private static final HighJumpModule instance = new HighJumpModule();
    private final ModeSetting mode = new ModeSetting("Режим").values("Слайм", "Ванильный", "Поляр", "Слайм Матрикс").value("Слайм");
    private final SliderSetting vanillaHeight = new SliderSetting("Высота прыжка").value(Float.valueOf(0.6f)).range(0.1f, 2.0f).step(0.1f).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("Ванильный"));
    });
    private int jumpCounter = 0;
    private boolean wasOnSlimeBlock = false;

    @Generated
    public static HighJumpModule getInstance() {
        return instance;
    }

    /* JADX WARN: Type inference failed for: r1v7, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    public HighJumpModule() {
        addSettings(this.mode, this.vanillaHeight);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onEnable() {
        super.onEnable();
        this.jumpCounter = 0;
        this.wasOnSlimeBlock = false;
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        super.onDisable();
        this.jumpCounter = 0;
        this.wasOnSlimeBlock = false;
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1724 == null || mc.field_1687 == null || mc.field_1724.method_52535() || mc.field_1724.method_31549().field_7479 || mc.field_1724.method_3144()) {
                return;
            }
            if (this.mode.is("Ванильный")) {
                if (mc.field_1690.field_1903.method_1434() && mc.field_1724.method_24828()) {
                    mc.field_1724.method_18800(mc.field_1724.method_18798().field_1352, this.vanillaHeight.getValue().floatValue(), mc.field_1724.method_18798().field_1350);
                    return;
                }
                return;
            }
            if (!this.mode.is("Слайм")) {
                if (this.mode.is("Поляр")) {
                    if (mc.field_1724.method_5799() && !mc.field_1724.method_5869()) {
                        mc.field_1724.method_5762(0.0d, 0.56d, 0.0d);
                        return;
                    }
                    return;
                }
                if (this.mode.is("Слайм Матрикс")) {
                    class_2338 belowPos = mc.field_1724.method_24515().method_10074();
                    if (mc.field_1724.method_24828() && mc.field_1687.method_8320(belowPos).method_27852(class_2246.field_10030)) {
                        this.wasOnSlimeBlock = true;
                        return;
                    }
                    if (this.wasOnSlimeBlock && !mc.field_1724.method_24828() && mc.field_1724.method_18798().field_1351 > 0.0d) {
                        mc.field_1724.method_5762(0.0d, 1.35d, 0.0d);
                        this.wasOnSlimeBlock = false;
                        return;
                    } else {
                        if (!mc.field_1687.method_8320(belowPos).method_27852(class_2246.field_10030)) {
                            this.wasOnSlimeBlock = false;
                            return;
                        }
                        return;
                    }
                }
                return;
            }
            class_2338 playerPos = mc.field_1724.method_24515();
            class_2338 belowPlayer = playerPos.method_10074();
            if (mc.field_1724.method_24828() && !mc.field_1687.method_8320(belowPlayer).method_27852(class_2246.field_10030)) {
                this.jumpCounter = 0;
            }
            if (mc.field_1724.method_24828() && mc.field_1687.method_8320(belowPlayer).method_27852(class_2246.field_10030)) {
                mc.field_1690.field_1903.method_23481(true);
            }
            if (mc.field_1690.field_1903.method_1434() && mc.field_1724.method_24828() && mc.field_1687.method_8320(belowPlayer).method_27852(class_2246.field_10030)) {
                mc.field_1690.field_1894.method_23481(false);
                mc.field_1690.field_1913.method_23481(false);
                mc.field_1690.field_1849.method_23481(false);
                mc.field_1690.field_1881.method_23481(false);
                float jumpVelocity = 0.3f;
                if (this.jumpCounter >= 1) {
                    jumpVelocity = 0.6f + ((this.jumpCounter - 1) * 0.1f);
                    if (jumpVelocity > 1.1f) {
                        this.jumpCounter = 0;
                        jumpVelocity = 0.6f;
                    }
                }
                mc.field_1724.method_18800(mc.field_1724.method_18798().field_1352, jumpVelocity, mc.field_1724.method_18798().field_1350);
                this.jumpCounter++;
            }
        }));
        addEvents(updateEvent);
    }
}

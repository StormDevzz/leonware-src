package sweetie.leonware.client.features.modules.combat;

import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1657;
import net.minecraft.class_1743;
import net.minecraft.class_1819;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2596;
import net.minecraft.class_2846;
import net.minecraft.class_2886;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.utils.other.NetworkUtil;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/ShieldDesyncModule.class */
@ModuleRegister(name = "Shield Desync", category = Category.COMBAT)
public class ShieldDesyncModule extends Module {
    private static final ShieldDesyncModule instance = new ShieldDesyncModule();
    private final ModeSetting mode = new ModeSetting("Mode").values(Mode.HVH.toString(), Mode.AXE.toString()).value(Mode.HVH.toString());
    private final BooleanSetting onlyWhenBlocking = new BooleanSetting("Only when blocking").value((Boolean) true).setVisible(() -> {
        return Boolean.valueOf(this.mode.is(Mode.HVH.toString()));
    });

    @Generated
    public static ShieldDesyncModule getInstance() {
        return instance;
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/ShieldDesyncModule$Mode.class */
    private enum Mode {
        HVH("HvH"),
        AXE("Axe");

        private final String name;

        Mode(String name) {
            this.name = name;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.name;
        }
    }

    /* JADX WARN: Type inference failed for: r1v5, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    public ShieldDesyncModule() {
        addSettings(this.mode, this.onlyWhenBlocking);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            onUpdate();
        }));
        addEvents(updateEvent);
    }

    private void onUpdate() {
        if (mc.field_1724 == null || mc.field_1687 == null) {
            return;
        }
        boolean hasShield = (mc.field_1724.method_6079().method_7909() instanceof class_1819) || (mc.field_1724.method_6047().method_7909() instanceof class_1819);
        if (hasShield) {
            class_1268 shieldHand = mc.field_1724.method_6079().method_7909() instanceof class_1819 ? class_1268.field_5810 : class_1268.field_5808;
            if (this.mode.is(Mode.HVH.toString())) {
                handleHvHMode(shieldHand);
            } else if (this.mode.is(Mode.AXE.toString())) {
                handleAxeMode();
            }
        }
    }

    private void handleHvHMode(class_1268 shieldHand) {
        if (!this.onlyWhenBlocking.getValue().booleanValue() || mc.field_1724.method_6115()) {
            RotationManager rotationManager = RotationManager.getInstance();
            Rotation rotation = rotationManager.getRotation();
            float yaw = rotation != null ? rotation.getYaw() : mc.field_1724.method_36454();
            float pitch = rotation != null ? rotation.getPitch() : mc.field_1724.method_36455();
            mc.field_1690.field_1904.method_23481(true);
            mc.field_1690.field_1904.method_23481(false);
            mc.field_1690.field_1904.method_23481(true);
            mc.field_1724.method_6075();
            mc.field_1690.field_1904.method_23481(true);
            NetworkUtil.sendPacket((class_2596<?>) new class_2846(class_2846.class_2847.field_12974, class_2338.field_10980, class_2350.field_11033));
            NetworkUtil.sendPacket(s -> {
                return new class_2886(shieldHand, s, yaw, pitch);
            });
        }
    }

    private void handleAxeMode() {
        if (mc.field_1724.method_6115()) {
            boolean enemyHasAxe = mc.field_1687.method_18456().stream().filter(player -> {
                return player != mc.field_1724;
            }).filter(player2 -> {
                return ((double) mc.field_1724.method_5739(player2)) <= 4.0d;
            }).anyMatch((v1) -> {
                return hasAxeInHand(v1);
            });
            if (enemyHasAxe) {
                mc.field_1724.method_6075();
                NetworkUtil.sendPacket((class_2596<?>) new class_2846(class_2846.class_2847.field_12974, class_2338.field_10980, class_2350.field_11033));
            }
        }
    }

    private boolean hasAxeInHand(class_1657 player) {
        return (player.method_6047().method_7909() instanceof class_1743) || (player.method_6079().method_7909() instanceof class_1743);
    }
}

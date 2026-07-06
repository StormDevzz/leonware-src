package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_2248;
import net.minecraft.class_2281;
import net.minecraft.class_2315;
import net.minecraft.class_2325;
import net.minecraft.class_2336;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2377;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_2480;
import net.minecraft.class_2531;
import net.minecraft.class_3708;
import net.minecraft.class_3710;
import net.minecraft.class_3716;
import net.minecraft.class_3865;
import net.minecraft.class_3965;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/player/GhostHandModule.class */
@ModuleRegister(name = "Ghost Hand", category = Category.PLAYER)
public class GhostHandModule extends Module {
    private static final GhostHandModule instance = new GhostHandModule();
    private final SliderSetting range = new SliderSetting("Дальность").value(Float.valueOf(5.0f)).range(1.0f, 10.0f).step(0.5f);
    private final SliderSetting fov = new SliderSetting("FOV прицела").value(Float.valueOf(30.0f)).range(5.0f, 90.0f).step(1.0f);
    private final MultiBooleanSetting containers = new MultiBooleanSetting("Контейнеры").value(new BooleanSetting("Сундук").value((Boolean) true), new BooleanSetting("Шалкер").value((Boolean) true), new BooleanSetting("Бочка").value((Boolean) true), new BooleanSetting("Эндер-сундук").value((Boolean) true), new BooleanSetting("Печь").value((Boolean) false), new BooleanSetting("Воронка").value((Boolean) false), new BooleanSetting("Раздатчик").value((Boolean) false));
    private boolean wasPressed = false;

    @Generated
    public static GhostHandModule getInstance() {
        return instance;
    }

    public GhostHandModule() {
        addSettings(this.range, this.fov, this.containers);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener tick = TickEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1687 == null || mc.field_1724 == null) {
                return;
            }
            boolean pressed = mc.field_1690.field_1904.method_1434();
            if (!pressed || this.wasPressed) {
                this.wasPressed = pressed;
                return;
            }
            this.wasPressed = true;
            if (mc.field_1755 != null) {
                return;
            }
            if (mc.field_1765 != null && mc.field_1765.method_17783() == class_239.class_240.field_1332) {
                class_3965 existing = mc.field_1765;
                if (isContainer(mc.field_1687.method_8320(existing.method_17777()).method_26204())) {
                    return;
                }
            }
            class_2338 target = findTarget();
            if (target == null) {
                return;
            }
            class_2350 face = getClosestFace(target);
            class_243 hitVec = class_243.method_24953(target).method_1019(new class_243(face.method_10148(), face.method_10164(), face.method_10165()).method_1021(0.5d));
            class_3965 hit = new class_3965(hitVec, face, target, false);
            mc.field_1761.method_2896(mc.field_1724, class_1268.field_5808, hit);
            mc.field_1724.method_6104(class_1268.field_5808);
        }));
        addEvents(tick);
    }

    private class_2338 findTarget() {
        class_243 eyes = mc.field_1724.method_33571();
        class_243 look = mc.field_1724.method_5828(1.0f);
        float maxRange = this.range.getValue().floatValue();
        double maxAngle = Math.toRadians(this.fov.getValue().floatValue());
        class_2338 best = null;
        double bestAngle = Double.MAX_VALUE;
        int r = (int) Math.ceil(maxRange);
        class_2338 origin = mc.field_1724.method_24515();
        for (int dx = -r; dx <= r; dx++) {
            for (int dy = -r; dy <= r; dy++) {
                for (int dz = -r; dz <= r; dz++) {
                    class_2338 pos = origin.method_10069(dx, dy, dz);
                    class_243 center = class_243.method_24953(pos);
                    if (eyes.method_1022(center) <= maxRange) {
                        class_2248 block = mc.field_1687.method_8320(pos).method_26204();
                        if (isContainer(block)) {
                            class_243 dir = center.method_1020(eyes).method_1029();
                            double angle = Math.acos(Math.max(-1.0d, Math.min(1.0d, look.method_1026(dir))));
                            if (angle < bestAngle) {
                                bestAngle = angle;
                                best = pos;
                            }
                        }
                    }
                }
            }
        }
        if (bestAngle <= maxAngle) {
            return best;
        }
        return null;
    }

    private boolean isContainer(class_2248 block) {
        if (this.containers.isEnabled("Сундук") && ((block instanceof class_2281) || (block instanceof class_2531))) {
            return true;
        }
        if (this.containers.isEnabled("Шалкер") && (block instanceof class_2480)) {
            return true;
        }
        if (this.containers.isEnabled("Бочка") && (block instanceof class_3708)) {
            return true;
        }
        if (this.containers.isEnabled("Эндер-сундук") && (block instanceof class_2336)) {
            return true;
        }
        if (this.containers.isEnabled("Печь") && ((block instanceof class_3865) || (block instanceof class_3710) || (block instanceof class_3716))) {
            return true;
        }
        if (this.containers.isEnabled("Воронка") && (block instanceof class_2377)) {
            return true;
        }
        if (!this.containers.isEnabled("Раздатчик")) {
            return false;
        }
        if ((block instanceof class_2315) || (block instanceof class_2325)) {
            return true;
        }
        return false;
    }

    private class_2350 getClosestFace(class_2338 pos) {
        class_243 diff = mc.field_1724.method_33571().method_1020(class_243.method_24953(pos));
        class_2350 best = class_2350.field_11036;
        double bestDot = Double.NEGATIVE_INFINITY;
        for (class_2350 dir : class_2350.values()) {
            class_243 dv = new class_243(dir.method_10148(), dir.method_10164(), dir.method_10165());
            double dot = diff.method_1026(dv);
            if (dot > bestDot) {
                bestDot = dot;
                best = dir;
            }
        }
        return best;
    }
}

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1268
 *  net.minecraft.class_2248
 *  net.minecraft.class_2281
 *  net.minecraft.class_2315
 *  net.minecraft.class_2325
 *  net.minecraft.class_2336
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_2377
 *  net.minecraft.class_2382
 *  net.minecraft.class_239$class_240
 *  net.minecraft.class_243
 *  net.minecraft.class_2480
 *  net.minecraft.class_2531
 *  net.minecraft.class_3708
 *  net.minecraft.class_3710
 *  net.minecraft.class_3716
 *  net.minecraft.class_3865
 *  net.minecraft.class_3965
 */
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
import net.minecraft.class_2382;
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

@ModuleRegister(name="Ghost Hand", category=Category.PLAYER)
public class GhostHandModule
extends Module {
    private static final GhostHandModule instance = new GhostHandModule();
    private final SliderSetting range = new SliderSetting("\u0414\u0430\u043b\u044c\u043d\u043e\u0441\u0442\u044c").value(Float.valueOf(5.0f)).range(1.0f, 10.0f).step(0.5f);
    private final SliderSetting fov = new SliderSetting("FOV \u043f\u0440\u0438\u0446\u0435\u043b\u0430").value(Float.valueOf(30.0f)).range(5.0f, 90.0f).step(1.0f);
    private final MultiBooleanSetting containers = new MultiBooleanSetting("\u041a\u043e\u043d\u0442\u0435\u0439\u043d\u0435\u0440\u044b").value(new BooleanSetting("\u0421\u0443\u043d\u0434\u0443\u043a").value(true), new BooleanSetting("\u0428\u0430\u043b\u043a\u0435\u0440").value(true), new BooleanSetting("\u0411\u043e\u0447\u043a\u0430").value(true), new BooleanSetting("\u042d\u043d\u0434\u0435\u0440-\u0441\u0443\u043d\u0434\u0443\u043a").value(true), new BooleanSetting("\u041f\u0435\u0447\u044c").value(false), new BooleanSetting("\u0412\u043e\u0440\u043e\u043d\u043a\u0430").value(false), new BooleanSetting("\u0420\u0430\u0437\u0434\u0430\u0442\u0447\u0438\u043a").value(false));
    private boolean wasPressed = false;

    public GhostHandModule() {
        this.addSettings(this.range, this.fov, this.containers);
    }

    @Override
    public void onEvent() {
        EventListener tick = TickEvent.getInstance().subscribe(new Listener<TickEvent>(event -> {
            class_3965 existing;
            if (GhostHandModule.mc.field_1687 == null || GhostHandModule.mc.field_1724 == null) {
                return;
            }
            boolean pressed = GhostHandModule.mc.field_1690.field_1904.method_1434();
            if (!pressed || this.wasPressed) {
                this.wasPressed = pressed;
                return;
            }
            this.wasPressed = true;
            if (GhostHandModule.mc.field_1755 != null) {
                return;
            }
            if (GhostHandModule.mc.field_1765 != null && GhostHandModule.mc.field_1765.method_17783() == class_239.class_240.field_1332 && this.isContainer(GhostHandModule.mc.field_1687.method_8320((existing = (class_3965)GhostHandModule.mc.field_1765).method_17777()).method_26204())) {
                return;
            }
            class_2338 target = this.findTarget();
            if (target == null) {
                return;
            }
            class_2350 face = this.getClosestFace(target);
            class_243 hitVec = class_243.method_24953((class_2382)target).method_1019(new class_243((double)face.method_10148(), (double)face.method_10164(), (double)face.method_10165()).method_1021(0.5));
            class_3965 hit = new class_3965(hitVec, face, target, false);
            GhostHandModule.mc.field_1761.method_2896(GhostHandModule.mc.field_1724, class_1268.field_5808, hit);
            GhostHandModule.mc.field_1724.method_6104(class_1268.field_5808);
        }));
        this.addEvents(tick);
    }

    private class_2338 findTarget() {
        class_243 eyes = GhostHandModule.mc.field_1724.method_33571();
        class_243 look = GhostHandModule.mc.field_1724.method_5828(1.0f);
        float maxRange = ((Float)this.range.getValue()).floatValue();
        double maxAngle = Math.toRadians(((Float)this.fov.getValue()).floatValue());
        class_2338 best = null;
        double bestAngle = Double.MAX_VALUE;
        int r = (int)Math.ceil(maxRange);
        class_2338 origin = GhostHandModule.mc.field_1724.method_24515();
        for (int dx = -r; dx <= r; ++dx) {
            for (int dy = -r; dy <= r; ++dy) {
                for (int dz = -r; dz <= r; ++dz) {
                    class_243 dir;
                    double angle;
                    class_2248 block;
                    class_2338 pos = origin.method_10069(dx, dy, dz);
                    class_243 center = class_243.method_24953((class_2382)pos);
                    if (eyes.method_1022(center) > (double)maxRange || !this.isContainer(block = GhostHandModule.mc.field_1687.method_8320(pos).method_26204()) || !((angle = Math.acos(Math.max(-1.0, Math.min(1.0, look.method_1026(dir = center.method_1020(eyes).method_1029()))))) < bestAngle)) continue;
                    bestAngle = angle;
                    best = pos;
                }
            }
        }
        return bestAngle <= maxAngle ? best : null;
    }

    private boolean isContainer(class_2248 block) {
        if (this.containers.isEnabled("\u0421\u0443\u043d\u0434\u0443\u043a") && (block instanceof class_2281 || block instanceof class_2531)) {
            return true;
        }
        if (this.containers.isEnabled("\u0428\u0430\u043b\u043a\u0435\u0440") && block instanceof class_2480) {
            return true;
        }
        if (this.containers.isEnabled("\u0411\u043e\u0447\u043a\u0430") && block instanceof class_3708) {
            return true;
        }
        if (this.containers.isEnabled("\u042d\u043d\u0434\u0435\u0440-\u0441\u0443\u043d\u0434\u0443\u043a") && block instanceof class_2336) {
            return true;
        }
        if (this.containers.isEnabled("\u041f\u0435\u0447\u044c") && (block instanceof class_3865 || block instanceof class_3710 || block instanceof class_3716)) {
            return true;
        }
        if (this.containers.isEnabled("\u0412\u043e\u0440\u043e\u043d\u043a\u0430") && block instanceof class_2377) {
            return true;
        }
        return this.containers.isEnabled("\u0420\u0430\u0437\u0434\u0430\u0442\u0447\u0438\u043a") && (block instanceof class_2315 || block instanceof class_2325);
    }

    private class_2350 getClosestFace(class_2338 pos) {
        class_243 diff = GhostHandModule.mc.field_1724.method_33571().method_1020(class_243.method_24953((class_2382)pos));
        class_2350 best = class_2350.field_11036;
        double bestDot = Double.NEGATIVE_INFINITY;
        for (class_2350 dir : class_2350.values()) {
            class_243 dv = new class_243((double)dir.method_10148(), (double)dir.method_10164(), (double)dir.method_10165());
            double dot = diff.method_1026(dv);
            if (!(dot > bestDot)) continue;
            bestDot = dot;
            best = dir;
        }
        return best;
    }

    @Generated
    public static GhostHandModule getInstance() {
        return instance;
    }
}


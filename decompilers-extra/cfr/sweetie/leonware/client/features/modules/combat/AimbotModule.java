/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_1657
 *  net.minecraft.class_243
 *  net.minecraft.class_3532
 */
package sweetie.leonware.client.features.modules.combat;

import java.awt.Color;
import lombok.Generated;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.render.Render2DEvent;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.render.Render2DEngine;

@ModuleRegister(name="Aimbot", category=Category.COMBAT)
public class AimbotModule
extends Module {
    private static final AimbotModule instance = new AimbotModule();
    public final SliderSetting distance = new SliderSetting("\u0414\u0438\u0441\u0442\u0430\u043d\u0446\u0438\u044f").value(Float.valueOf(10.0f)).range(1.0f, 67.0f).step(0.5f);
    public final SliderSetting fov = new SliderSetting("FOV").value(Float.valueOf(90.0f)).range(1.0f, 180.0f).step(1.0f);
    public final SliderSetting yawSpeed = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c Yaw").value(Float.valueOf(5.0f)).range(0.1f, 10.0f).step(0.1f);
    public final SliderSetting pitchSpeed = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c Pitch").value(Float.valueOf(5.0f)).range(0.1f, 10.0f).step(0.1f);
    public final BooleanSetting renderFov = new BooleanSetting("\u0420\u0435\u043d\u0434\u0435\u0440 FOV").value(true);
    public final ModeSetting targetPart = new ModeSetting("\u0427\u0430\u0441\u0442\u044c \u0442\u0435\u043b\u0430").values("\u0413\u043e\u043b\u043e\u0432\u0430", "\u0422\u0435\u043b\u043e", "\u041d\u043e\u0433\u0438").value("\u0422\u0435\u043b\u043e");
    public final BooleanSetting onlyPlayers = new BooleanSetting("\u0422\u043e\u043b\u044c\u043a\u043e \u0438\u0433\u0440\u043e\u043a\u0438").value(true);
    public final BooleanSetting onlyOnGround = new BooleanSetting("\u0422\u043e\u043b\u044c\u043a\u043e \u043d\u0430 \u0437\u0435\u043c\u043b\u0435").value(false);
    public final BooleanSetting onlyVisible = new BooleanSetting("\u0422\u043e\u043b\u044c\u043a\u043e \u0432\u0438\u0434\u0438\u043c\u044b\u0445").value(true);

    public AimbotModule() {
        this.addSettings(this.distance, this.fov, this.yawSpeed, this.pitchSpeed, this.renderFov, this.targetPart, this.onlyPlayers, this.onlyOnGround, this.onlyVisible);
    }

    @Override
    public void onEvent() {
        EventListener render3D = Render3DEvent.getInstance().subscribe(new Listener<Render3DEvent.Render3DEventData>(event -> {
            if (AimbotModule.mc.field_1724 == null || AimbotModule.mc.field_1687 == null) {
                return;
            }
            if (((Boolean)this.onlyOnGround.getValue()).booleanValue() && !AimbotModule.mc.field_1724.method_24828()) {
                return;
            }
            class_1309 best = this.getBestTarget();
            if (best == null) {
                return;
            }
            class_243 aimPos = this.getAimPos(best);
            class_243 eyePos = AimbotModule.mc.field_1724.method_33571();
            class_243 diff = aimPos.method_1020(eyePos);
            float targetYaw = (float)Math.toDegrees(Math.atan2(diff.field_1350, diff.field_1352)) - 90.0f;
            float targetPitch = (float)(-Math.toDegrees(Math.atan2(diff.field_1351, Math.sqrt(diff.field_1352 * diff.field_1352 + diff.field_1350 * diff.field_1350))));
            float currentYaw = AimbotModule.mc.field_1724.method_36454();
            float currentPitch = AimbotModule.mc.field_1724.method_36455();
            float deltaYaw = class_3532.method_15393((float)(targetYaw - currentYaw));
            float deltaPitch = class_3532.method_15393((float)(targetPitch - currentPitch));
            float stepYaw = ((Float)this.yawSpeed.getValue()).floatValue();
            float stepPitch = ((Float)this.pitchSpeed.getValue()).floatValue();
            float newYaw = currentYaw + Math.signum(deltaYaw) * Math.min(Math.abs(deltaYaw), stepYaw);
            float newPitch = currentPitch + Math.signum(deltaPitch) * Math.min(Math.abs(deltaPitch), stepPitch);
            newPitch = class_3532.method_15363((float)newPitch, (float)-90.0f, (float)90.0f);
            AimbotModule.mc.field_1724.method_36456(newYaw);
            AimbotModule.mc.field_1724.method_36457(newPitch);
            AimbotModule.mc.field_1724.field_6241 = newYaw;
        }));
        EventListener render2D = Render2DEvent.getInstance().subscribe(new Listener<Render2DEvent.Render2DEventData>(event -> {
            if (!((Boolean)this.renderFov.getValue()).booleanValue() || AimbotModule.mc.field_1724 == null) {
                return;
            }
            float radius = ((Float)this.fov.getValue()).floatValue() / 180.0f * ((float)mc.method_22683().method_4486() / 2.0f);
            float cx = (float)mc.method_22683().method_4486() / 2.0f;
            float cy = (float)mc.method_22683().method_4502() / 2.0f;
            Render2DEngine.drawCircle(event.matrixStack(), cx, cy, radius, 1.0f, new Color(255, 200, 50, 150).getRGB());
        }));
        this.addEvents(render3D, render2D);
    }

    private class_1309 getBestTarget() {
        class_1309 best = null;
        float bestAngle = ((Float)this.fov.getValue()).floatValue();
        for (class_1297 entity : AimbotModule.mc.field_1687.method_18112()) {
            float dPitch;
            double dist;
            class_1309 living;
            if (!(entity instanceof class_1309) || (living = (class_1309)entity) == AimbotModule.mc.field_1724 || !living.method_5805() || living.method_31481() || living.method_6032() <= 0.0f || ((Boolean)this.onlyPlayers.getValue()).booleanValue() && !(living instanceof class_1657) || (dist = (double)AimbotModule.mc.field_1724.method_5739((class_1297)living)) > (double)((Float)this.distance.getValue()).floatValue() || ((Boolean)this.onlyVisible.getValue()).booleanValue() && !AimbotModule.mc.field_1724.method_6057((class_1297)living)) continue;
            class_243 aimPos = this.getAimPos(living);
            class_243 eyePos = AimbotModule.mc.field_1724.method_33571();
            class_243 diff = aimPos.method_1020(eyePos);
            float targetYaw = (float)Math.toDegrees(Math.atan2(diff.field_1350, diff.field_1352)) - 90.0f;
            float targetPitch = (float)(-Math.toDegrees(Math.atan2(diff.field_1351, Math.sqrt(diff.field_1352 * diff.field_1352 + diff.field_1350 * diff.field_1350))));
            float dYaw = Math.abs(class_3532.method_15393((float)(targetYaw - AimbotModule.mc.field_1724.method_36454())));
            float angle = (float)Math.sqrt(dYaw * dYaw + (dPitch = Math.abs(class_3532.method_15393((float)(targetPitch - AimbotModule.mc.field_1724.method_36455())))) * dPitch);
            if (!(angle < bestAngle)) continue;
            bestAngle = angle;
            best = living;
        }
        return best;
    }

    private class_243 getAimPos(class_1309 entity) {
        double height = entity.method_17682();
        return switch ((String)this.targetPart.getValue()) {
            case "\u0413\u043e\u043b\u043e\u0432\u0430" -> entity.method_19538().method_1031(0.0, height * 0.75, 0.0);
            case "\u041d\u043e\u0433\u0438" -> entity.method_19538().method_1031(0.0, height * 0.1, 0.0);
            default -> entity.method_19538().method_1031(0.0, height * 0.5, 0.0);
        };
    }

    @Generated
    public static AimbotModule getInstance() {
        return instance;
    }
}


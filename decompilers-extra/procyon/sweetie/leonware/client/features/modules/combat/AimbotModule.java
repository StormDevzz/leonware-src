// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.combat;

import lombok.Generated;
import java.util.Iterator;
import net.minecraft.class_1657;
import net.minecraft.class_1297;
import net.minecraft.class_243;
import net.minecraft.class_1309;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.utils.render.Render2DEngine;
import java.awt.Color;
import sweetie.leonware.api.event.events.render.Render2DEvent;
import sweetie.leonware.api.event.Listener;
import net.minecraft.class_3532;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Aimbot", category = Category.COMBAT)
public class AimbotModule extends Module
{
    private static final AimbotModule instance;
    public final SliderSetting distance;
    public final SliderSetting fov;
    public final SliderSetting yawSpeed;
    public final SliderSetting pitchSpeed;
    public final BooleanSetting renderFov;
    public final ModeSetting targetPart;
    public final BooleanSetting onlyPlayers;
    public final BooleanSetting onlyOnGround;
    public final BooleanSetting onlyVisible;
    
    public AimbotModule() {
        this.distance = new SliderSetting("\u0414\u0438\u0441\u0442\u0430\u043d\u0446\u0438\u044f").value(10.0f).range(1.0f, 67.0f).step(0.5f);
        this.fov = new SliderSetting("FOV").value(90.0f).range(1.0f, 180.0f).step(1.0f);
        this.yawSpeed = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c Yaw").value(5.0f).range(0.1f, 10.0f).step(0.1f);
        this.pitchSpeed = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c Pitch").value(5.0f).range(0.1f, 10.0f).step(0.1f);
        this.renderFov = new BooleanSetting("\u0420\u0435\u043d\u0434\u0435\u0440 FOV").value(true);
        this.targetPart = new ModeSetting("\u0427\u0430\u0441\u0442\u044c \u0442\u0435\u043b\u0430").values("\u0413\u043e\u043b\u043e\u0432\u0430", "\u0422\u0435\u043b\u043e", "\u041d\u043e\u0433\u0438").value("\u0422\u0435\u043b\u043e");
        this.onlyPlayers = new BooleanSetting("\u0422\u043e\u043b\u044c\u043a\u043e \u0438\u0433\u0440\u043e\u043a\u0438").value(true);
        this.onlyOnGround = new BooleanSetting("\u0422\u043e\u043b\u044c\u043a\u043e \u043d\u0430 \u0437\u0435\u043c\u043b\u0435").value(false);
        this.onlyVisible = new BooleanSetting("\u0422\u043e\u043b\u044c\u043a\u043e \u0432\u0438\u0434\u0438\u043c\u044b\u0445").value(true);
        this.addSettings(this.distance, this.fov, this.yawSpeed, this.pitchSpeed, this.renderFov, this.targetPart, this.onlyPlayers, this.onlyOnGround, this.onlyVisible);
    }
    
    @Override
    public void onEvent() {
        final EventListener render3D = Render3DEvent.getInstance().subscribe(new Listener<Render3DEvent.Render3DEventData>(event -> {
            if (AimbotModule.mc.field_1724 == null || AimbotModule.mc.field_1687 == null) {
                return;
            }
            else if (this.onlyOnGround.getValue() && !AimbotModule.mc.field_1724.method_24828()) {
                return;
            }
            else {
                final class_1309 best = this.getBestTarget();
                if (best == null) {
                    return;
                }
                else {
                    final class_243 aimPos = this.getAimPos(best);
                    final class_243 eyePos = AimbotModule.mc.field_1724.method_33571();
                    final class_243 diff = aimPos.method_1020(eyePos);
                    final float targetYaw = (float)Math.toDegrees(Math.atan2(diff.field_1350, diff.field_1352)) - 90.0f;
                    final float targetPitch = (float)(-Math.toDegrees(Math.atan2(diff.field_1351, Math.sqrt(diff.field_1352 * diff.field_1352 + diff.field_1350 * diff.field_1350))));
                    final float currentYaw = AimbotModule.mc.field_1724.method_36454();
                    final float currentPitch = AimbotModule.mc.field_1724.method_36455();
                    final float deltaYaw = class_3532.method_15393(targetYaw - currentYaw);
                    final float deltaPitch = class_3532.method_15393(targetPitch - currentPitch);
                    final float stepYaw = this.yawSpeed.getValue();
                    final float stepPitch = this.pitchSpeed.getValue();
                    final float newYaw = currentYaw + Math.signum(deltaYaw) * Math.min(Math.abs(deltaYaw), stepYaw);
                    final float newPitch = currentPitch + Math.signum(deltaPitch) * Math.min(Math.abs(deltaPitch), stepPitch);
                    final float newPitch2 = class_3532.method_15363(newPitch, -90.0f, 90.0f);
                    AimbotModule.mc.field_1724.method_36456(newYaw);
                    AimbotModule.mc.field_1724.method_36457(newPitch2);
                    AimbotModule.mc.field_1724.field_6241 = newYaw;
                    return;
                }
            }
        }));
        final EventListener render2D = Render2DEvent.getInstance().subscribe(new Listener<Render2DEvent.Render2DEventData>(event -> {
            if (!this.renderFov.getValue() || AimbotModule.mc.field_1724 == null) {
                return;
            }
            else {
                final float radius = this.fov.getValue() / 180.0f * (AimbotModule.mc.method_22683().method_4486() / 2.0f);
                final float cx = AimbotModule.mc.method_22683().method_4486() / 2.0f;
                final float cy = AimbotModule.mc.method_22683().method_4502() / 2.0f;
                Render2DEngine.drawCircle(event.matrixStack(), cx, cy, radius, 1.0f, new Color(255, 200, 50, 150).getRGB());
                return;
            }
        }));
        this.addEvents(render3D, render2D);
    }
    
    private class_1309 getBestTarget() {
        class_1309 best = null;
        float bestAngle = this.fov.getValue();
        for (final class_1297 entity : AimbotModule.mc.field_1687.method_18112()) {
            if (entity instanceof final class_1309 living) {
                if (living == AimbotModule.mc.field_1724) {
                    continue;
                }
                if (!living.method_5805()) {
                    continue;
                }
                if (living.method_31481()) {
                    continue;
                }
                if (living.method_6032() <= 0.0f) {
                    continue;
                }
                if (this.onlyPlayers.getValue() && !(living instanceof class_1657)) {
                    continue;
                }
                final double dist = AimbotModule.mc.field_1724.method_5739((class_1297)living);
                if (dist > this.distance.getValue()) {
                    continue;
                }
                if (this.onlyVisible.getValue() && !AimbotModule.mc.field_1724.method_6057((class_1297)living)) {
                    continue;
                }
                final class_243 aimPos = this.getAimPos(living);
                final class_243 eyePos = AimbotModule.mc.field_1724.method_33571();
                final class_243 diff = aimPos.method_1020(eyePos);
                final float targetYaw = (float)Math.toDegrees(Math.atan2(diff.field_1350, diff.field_1352)) - 90.0f;
                final float targetPitch = (float)(-Math.toDegrees(Math.atan2(diff.field_1351, Math.sqrt(diff.field_1352 * diff.field_1352 + diff.field_1350 * diff.field_1350))));
                final float dYaw = Math.abs(class_3532.method_15393(targetYaw - AimbotModule.mc.field_1724.method_36454()));
                final float dPitch = Math.abs(class_3532.method_15393(targetPitch - AimbotModule.mc.field_1724.method_36455()));
                final float angle = (float)Math.sqrt(dYaw * dYaw + dPitch * dPitch);
                if (angle >= bestAngle) {
                    continue;
                }
                bestAngle = angle;
                best = living;
            }
        }
        return best;
    }
    
    private class_243 getAimPos(final class_1309 entity) {
        final double height = entity.method_17682();
        final String s = this.targetPart.getValue();
        return switch (s) {
            case "\u0413\u043e\u043b\u043e\u0432\u0430" -> entity.method_19538().method_1031(0.0, height * 0.75, 0.0);
            case "\u041d\u043e\u0433\u0438" -> entity.method_19538().method_1031(0.0, height * 0.1, 0.0);
            default -> entity.method_19538().method_1031(0.0, height * 0.5, 0.0);
        };
    }
    
    @Generated
    public static AimbotModule getInstance() {
        return AimbotModule.instance;
    }
    
    static {
        instance = new AimbotModule();
    }
}

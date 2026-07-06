package sweetie.leonware.client.features.modules.combat;

import java.awt.Color;
import lombok.Generated;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import net.minecraft.class_746;
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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/AimbotModule.class */
@ModuleRegister(name = "Aimbot", category = Category.COMBAT)
public class AimbotModule extends Module {
    private static final AimbotModule instance = new AimbotModule();
    public final SliderSetting distance = new SliderSetting("Дистанция").value(Float.valueOf(10.0f)).range(1.0f, 67.0f).step(0.5f);
    public final SliderSetting fov = new SliderSetting("FOV").value(Float.valueOf(90.0f)).range(1.0f, 180.0f).step(1.0f);
    public final SliderSetting yawSpeed = new SliderSetting("Скорость Yaw").value(Float.valueOf(5.0f)).range(0.1f, 10.0f).step(0.1f);
    public final SliderSetting pitchSpeed = new SliderSetting("Скорость Pitch").value(Float.valueOf(5.0f)).range(0.1f, 10.0f).step(0.1f);
    public final BooleanSetting renderFov = new BooleanSetting("Рендер FOV").value((Boolean) true);
    public final ModeSetting targetPart = new ModeSetting("Часть тела").values("Голова", "Тело", "Ноги").value("Тело");
    public final BooleanSetting onlyPlayers = new BooleanSetting("Только игроки").value((Boolean) true);
    public final BooleanSetting onlyOnGround = new BooleanSetting("Только на земле").value((Boolean) false);
    public final BooleanSetting onlyVisible = new BooleanSetting("Только видимых").value((Boolean) true);

    @Generated
    public static AimbotModule getInstance() {
        return instance;
    }

    public AimbotModule() {
        addSettings(this.distance, this.fov, this.yawSpeed, this.pitchSpeed, this.renderFov, this.targetPart, this.onlyPlayers, this.onlyOnGround, this.onlyVisible);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener render3D = Render3DEvent.getInstance().subscribe(new Listener(event -> {
            class_1309 best;
            if (mc.field_1724 == null || mc.field_1687 == null) {
                return;
            }
            if ((!this.onlyOnGround.getValue().booleanValue() || mc.field_1724.method_24828()) && (best = getBestTarget()) != null) {
                class_243 aimPos = getAimPos(best);
                class_243 eyePos = mc.field_1724.method_33571();
                class_243 diff = aimPos.method_1020(eyePos);
                float targetYaw = ((float) Math.toDegrees(Math.atan2(diff.field_1350, diff.field_1352))) - 90.0f;
                float targetPitch = (float) (-Math.toDegrees(Math.atan2(diff.field_1351, Math.sqrt((diff.field_1352 * diff.field_1352) + (diff.field_1350 * diff.field_1350)))));
                float currentYaw = mc.field_1724.method_36454();
                float currentPitch = mc.field_1724.method_36455();
                float deltaYaw = class_3532.method_15393(targetYaw - currentYaw);
                float deltaPitch = class_3532.method_15393(targetPitch - currentPitch);
                float stepYaw = this.yawSpeed.getValue().floatValue();
                float stepPitch = this.pitchSpeed.getValue().floatValue();
                float newYaw = currentYaw + (Math.signum(deltaYaw) * Math.min(Math.abs(deltaYaw), stepYaw));
                float newPitch = currentPitch + (Math.signum(deltaPitch) * Math.min(Math.abs(deltaPitch), stepPitch));
                float newPitch2 = class_3532.method_15363(newPitch, -90.0f, 90.0f);
                mc.field_1724.method_36456(newYaw);
                mc.field_1724.method_36457(newPitch2);
                mc.field_1724.field_6241 = newYaw;
            }
        }));
        EventListener render2D = Render2DEvent.getInstance().subscribe(new Listener(event2 -> {
            if (!this.renderFov.getValue().booleanValue() || mc.field_1724 == null) {
                return;
            }
            float radius = (this.fov.getValue().floatValue() / 180.0f) * (mc.method_22683().method_4486() / 2.0f);
            float cx = mc.method_22683().method_4486() / 2.0f;
            float cy = mc.method_22683().method_4502() / 2.0f;
            Render2DEngine.drawCircle(event2.matrixStack(), cx, cy, radius, 1.0f, new Color(255, 200, 50, 150).getRGB());
        }));
        addEvents(render3D, render2D);
    }

    private class_1309 getBestTarget() {
        class_746 class_746Var;
        class_746 class_746Var2 = null;
        float bestAngle = this.fov.getValue().floatValue();
        for (class_746 class_746Var3 : mc.field_1687.method_18112()) {
            if ((class_746Var3 instanceof class_1309) && (class_746Var = (class_1309) class_746Var3) != mc.field_1724 && class_746Var.method_5805() && !class_746Var.method_31481() && class_746Var.method_6032() > 0.0f && (!this.onlyPlayers.getValue().booleanValue() || (class_746Var instanceof class_1657))) {
                double dist = mc.field_1724.method_5739(class_746Var);
                if (dist <= this.distance.getValue().floatValue() && (!this.onlyVisible.getValue().booleanValue() || mc.field_1724.method_6057(class_746Var))) {
                    class_243 aimPos = getAimPos(class_746Var);
                    class_243 eyePos = mc.field_1724.method_33571();
                    class_243 diff = aimPos.method_1020(eyePos);
                    float targetYaw = ((float) Math.toDegrees(Math.atan2(diff.field_1350, diff.field_1352))) - 90.0f;
                    float targetPitch = (float) (-Math.toDegrees(Math.atan2(diff.field_1351, Math.sqrt((diff.field_1352 * diff.field_1352) + (diff.field_1350 * diff.field_1350)))));
                    float dYaw = Math.abs(class_3532.method_15393(targetYaw - mc.field_1724.method_36454()));
                    float dPitch = Math.abs(class_3532.method_15393(targetPitch - mc.field_1724.method_36455()));
                    float angle = (float) Math.sqrt((dYaw * dYaw) + (dPitch * dPitch));
                    if (angle < bestAngle) {
                        bestAngle = angle;
                        class_746Var2 = class_746Var;
                    }
                }
            }
        }
        return class_746Var2;
    }

    private class_243 getAimPos(class_1309 entity) {
        double height;
        height = entity.method_17682();
        switch (this.targetPart.getValue()) {
            case "Голова":
                return entity.method_19538().method_1031(0.0d, height * 0.75d, 0.0d);
            case "Ноги":
                return entity.method_19538().method_1031(0.0d, height * 0.1d, 0.0d);
            default:
                return entity.method_19538().method_1031(0.0d, height * 0.5d, 0.0d);
        }
    }
}

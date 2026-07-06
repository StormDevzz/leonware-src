package sweetie.leonware.client.features.modules.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_10142;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_746;
import net.minecraft.class_7833;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ColorSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/SkeletonESPModule.class */
@ModuleRegister(name = "Skeleton ESP", category = Category.RENDER)
public class SkeletonESPModule extends Module {
    private static final SkeletonESPModule instance = new SkeletonESPModule();
    private final ColorSetting color = new ColorSetting("Цвет").value(new Color(255, 255, 255));
    private final BooleanSetting renderSelf = new BooleanSetting("Рендерить себя").value((Boolean) false);

    @Generated
    public static SkeletonESPModule getInstance() {
        return instance;
    }

    public SkeletonESPModule() {
        addSettings(this.color, this.renderSelf);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener renderEvent = Render3DEvent.getInstance().subscribe(new Listener(event -> {
            class_746 class_746Var;
            class_4587 ms = event.matrixStack();
            class_243 cam = mc.method_1561().field_4686.method_19326();
            ms.method_22903();
            ms.method_22904(-cam.field_1352, -cam.field_1351, -cam.field_1350);
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE_MINUS_SRC_ALPHA, GlStateManager.class_4535.ONE, GlStateManager.class_4534.ZERO);
            RenderSystem.setShader(class_10142.field_53876);
            RenderSystem.lineWidth(2.0f);
            for (class_746 class_746Var2 : mc.field_1687.method_18112()) {
                if ((class_746Var2 instanceof class_1657) && ((class_746Var = (class_1657) class_746Var2) != mc.field_1724 || this.renderSelf.getValue().booleanValue())) {
                    if (!class_746Var.method_5767()) {
                        render(ms, class_746Var, event.partialTicks());
                    }
                }
            }
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();
            ms.method_22909();
        }));
        addEvents(renderEvent);
    }

    private void render(class_4587 ms, class_1657 p, float t) {
        if (p == mc.field_1724 && mc.field_1690.method_31044().method_31034()) {
            return;
        }
        if (p == mc.field_1724 && !this.renderSelf.getValue().booleanValue()) {
            return;
        }
        double x = class_3532.method_16436(t, p.field_6014, p.method_23317());
        double y = class_3532.method_16436(t, p.field_6036, p.method_23318());
        double z = class_3532.method_16436(t, p.field_5969, p.method_23321());
        float bodyYaw = class_3532.method_17821(t, p.field_6220, p.field_6283);
        float headYaw = class_3532.method_17821(t, p.field_6259, p.field_6241);
        float pitch = class_3532.method_16439(t, p.field_6004, p.method_36455());
        float swing = p.field_42108.method_48572(t);
        float swingAmt = p.field_42108.method_48570(t);
        float handSwing = p.method_6055(t);
        boolean elytra = p.method_6128();
        Color c = this.color.getValue();
        for (class_243[] bone : getBones(x, y, z, bodyYaw, headYaw, pitch, swing, swingAmt, handSwing, p.method_17682(), elytra, p.method_5715())) {
            line(ms, bone[0], bone[1], c);
        }
    }

    private List<class_243[]> getBones(double x, double y, double z, float bodyYaw, float headYaw, float pitch, float swing, float swingAmt, float handSwing, float h, boolean elytra, boolean sneak) {
        float lArmRot;
        float rArmRot;
        List<class_243[]> bones = new ArrayList<>();
        class_4587 ms = new class_4587();
        ms.method_22904(x, y, z);
        if (sneak && !elytra) {
            ms.method_22904(0.0d, 0.125d, 0.0d);
        }
        ms.method_22907(class_7833.field_40716.rotationDegrees(-bodyYaw));
        float bodyPitch = 0.0f;
        if (elytra) {
            bodyPitch = 1.57f + (pitch / 57.2958f);
        } else if (sneak) {
            bodyPitch = 0.5f;
        }
        if (elytra || sneak) {
            ms.method_22907(class_7833.field_40714.rotationDegrees(bodyPitch * 57.2958f));
        }
        if (sneak && !elytra) {
            ms.method_22904(0.0d, -0.13d, 0.0d);
        }
        getPos(ms);
        ms.method_22903();
        ms.method_22904(0.0d, ((double) h) * 0.75d, 0.0d);
        class_243 neck = getPos(ms);
        ms.method_22903();
        ms.method_22907(class_7833.field_40716.rotationDegrees(bodyYaw - headYaw));
        if (!elytra) {
            ms.method_22907(class_7833.field_40714.rotationDegrees(pitch));
        }
        ms.method_22904(0.0d, ((double) h) * 0.15d, 0.0d);
        class_243 head = getPos(ms);
        ms.method_22909();
        float swingAmt2 = Math.min(swingAmt, 1.0f) * 0.5f;
        ms.method_22903();
        ms.method_22904(0.4d, 0.0d, 0.0d);
        class_243 lShoulder = getPos(ms);
        if (elytra) {
            lArmRot = -0.2f;
            ms.method_22907(class_7833.field_40718.rotationDegrees(-5.0f));
        } else {
            lArmRot = class_3532.method_15362((swing * 0.6662f) + 3.1415927f) * 0.8f * swingAmt2;
        }
        ms.method_22907(class_7833.field_40714.rotationDegrees(lArmRot * 57.2958f));
        ms.method_22904(0.0d, -0.25d, 0.0d);
        class_243 lElbow = getPos(ms);
        ms.method_22907(class_7833.field_40714.rotationDegrees(Math.max(0.0f, lArmRot * 15.0f)));
        ms.method_22904(0.0d, -0.25d, 0.0d);
        class_243 lHand = getPos(ms);
        ms.method_22909();
        ms.method_22903();
        ms.method_22904(-0.4d, 0.0d, 0.0d);
        class_243 rShoulder = getPos(ms);
        if (elytra) {
            rArmRot = -0.2f;
            ms.method_22907(class_7833.field_40718.rotationDegrees(5.0f));
        } else {
            rArmRot = class_3532.method_15362(swing * 0.6662f) * 0.8f * swingAmt2;
        }
        if (handSwing > 0.0f && !elytra) {
            float swingProgress = 1.0f - handSwing;
            float swingRot = class_3532.method_15374(swingProgress * swingProgress * 3.1415927f);
            float headYawDiff = headYaw - bodyYaw;
            float yawFactor = class_3532.method_15363(headYawDiff / 75.0f, -1.0f, 1.0f);
            ms.method_22907(class_7833.field_40716.rotationDegrees(swingRot * 15.0f * yawFactor));
            rArmRot += (-swingRot) * 0.8f;
        }
        ms.method_22907(class_7833.field_40714.rotationDegrees(rArmRot * 57.2958f));
        ms.method_22904(0.0d, -0.25d, 0.0d);
        class_243 rElbow = getPos(ms);
        ms.method_22907(class_7833.field_40714.rotationDegrees(Math.max(0.0f, rArmRot * 15.0f)));
        ms.method_22904(0.0d, -0.25d, 0.0d);
        class_243 rHand = getPos(ms);
        ms.method_22909();
        ms.method_22909();
        ms.method_22903();
        ms.method_22904(0.0d, ((double) h) * 0.5d, 0.0d);
        class_243 waist = getPos(ms);
        ms.method_22909();
        ms.method_22903();
        ms.method_22904(0.0d, ((double) h) * 0.3d, 0.0d);
        class_243 pelvis = getPos(ms);
        ms.method_22903();
        ms.method_22904(0.125d, 0.0d, 0.0d);
        class_243 lHip = getPos(ms);
        float lLegRot = class_3532.method_15362(swing * 0.6662f) * 0.5f * swingAmt2;
        if (elytra) {
            lLegRot = 0.1f;
        }
        ms.method_22907(class_7833.field_40714.rotationDegrees(lLegRot * 57.2958f));
        ms.method_22904(0.0d, -0.25d, 0.0d);
        class_243 lKnee = getPos(ms);
        ms.method_22907(class_7833.field_40714.rotationDegrees(Math.abs(lLegRot) * 15.0f));
        ms.method_22904(0.0d, -0.25d, 0.0d);
        class_243 lFoot = getPos(ms);
        ms.method_22909();
        ms.method_22903();
        ms.method_22904(-0.125d, 0.0d, 0.0d);
        class_243 rHip = getPos(ms);
        float rLegRot = class_3532.method_15362((swing * 0.6662f) + 3.1415927f) * 0.5f * swingAmt2;
        if (elytra) {
            rLegRot = 0.1f;
        }
        ms.method_22907(class_7833.field_40714.rotationDegrees(rLegRot * 57.2958f));
        ms.method_22904(0.0d, -0.25d, 0.0d);
        class_243 rKnee = getPos(ms);
        ms.method_22907(class_7833.field_40714.rotationDegrees(Math.abs(rLegRot) * 15.0f));
        ms.method_22904(0.0d, -0.25d, 0.0d);
        class_243 rFoot = getPos(ms);
        ms.method_22909();
        ms.method_22909();
        ms.method_22909();
        bones.add(new class_243[]{neck, head});
        bones.add(new class_243[]{neck, waist});
        bones.add(new class_243[]{waist, pelvis});
        bones.add(new class_243[]{neck, lShoulder});
        bones.add(new class_243[]{neck, rShoulder});
        bones.add(new class_243[]{lShoulder, lElbow});
        bones.add(new class_243[]{lElbow, lHand});
        bones.add(new class_243[]{rShoulder, rElbow});
        bones.add(new class_243[]{rElbow, rHand});
        bones.add(new class_243[]{pelvis, lHip});
        bones.add(new class_243[]{pelvis, rHip});
        bones.add(new class_243[]{lHip, lKnee});
        bones.add(new class_243[]{lKnee, lFoot});
        bones.add(new class_243[]{rHip, rKnee});
        bones.add(new class_243[]{rKnee, rFoot});
        return bones;
    }

    private class_243 getPos(class_4587 ms) {
        Vector3f pos = ms.method_23760().method_23761().transformPosition(0.0f, 0.0f, 0.0f, new Vector3f());
        return new class_243(pos.x, pos.y, pos.z);
    }

    private void line(class_4587 ms, class_243 start, class_243 end, Color c) {
        Matrix4f m = ms.method_23760().method_23761();
        class_287 buf = class_289.method_1348().method_60827(class_293.class_5596.field_29344, class_290.field_1576);
        buf.method_22918(m, (float) start.field_1352, (float) start.field_1351, (float) start.field_1350).method_1336(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
        buf.method_22918(m, (float) end.field_1352, (float) end.field_1351, (float) end.field_1350).method_1336(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
        class_286.method_43433(buf.method_60800());
    }
}

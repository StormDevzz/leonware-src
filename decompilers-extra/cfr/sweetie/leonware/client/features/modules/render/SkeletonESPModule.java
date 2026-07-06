/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.GlStateManager$class_4534
 *  com.mojang.blaze3d.platform.GlStateManager$class_4535
 *  com.mojang.blaze3d.systems.RenderSystem
 *  lombok.Generated
 *  net.minecraft.class_10142
 *  net.minecraft.class_10156
 *  net.minecraft.class_1297
 *  net.minecraft.class_1657
 *  net.minecraft.class_243
 *  net.minecraft.class_286
 *  net.minecraft.class_287
 *  net.minecraft.class_289
 *  net.minecraft.class_290
 *  net.minecraft.class_293$class_5596
 *  net.minecraft.class_3532
 *  net.minecraft.class_4587
 *  net.minecraft.class_7833
 *  net.minecraft.class_9801
 *  org.joml.Matrix4f
 *  org.joml.Vector3f
 */
package sweetie.leonware.client.features.modules.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_10142;
import net.minecraft.class_10156;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_7833;
import net.minecraft.class_9801;
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

@ModuleRegister(name="Skeleton ESP", category=Category.RENDER)
public class SkeletonESPModule
extends Module {
    private static final SkeletonESPModule instance = new SkeletonESPModule();
    private final ColorSetting color = new ColorSetting("\u0426\u0432\u0435\u0442").value(new Color(255, 255, 255));
    private final BooleanSetting renderSelf = new BooleanSetting("\u0420\u0435\u043d\u0434\u0435\u0440\u0438\u0442\u044c \u0441\u0435\u0431\u044f").value(false);

    public SkeletonESPModule() {
        this.addSettings(this.color, this.renderSelf);
    }

    @Override
    public void onEvent() {
        EventListener renderEvent = Render3DEvent.getInstance().subscribe(new Listener<Render3DEvent.Render3DEventData>(event -> {
            class_4587 ms = event.matrixStack();
            class_243 cam = SkeletonESPModule.mc.method_1561().field_4686.method_19326();
            ms.method_22903();
            ms.method_22904(-cam.field_1352, -cam.field_1351, -cam.field_1350);
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask((boolean)false);
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate((GlStateManager.class_4535)GlStateManager.class_4535.SRC_ALPHA, (GlStateManager.class_4534)GlStateManager.class_4534.ONE_MINUS_SRC_ALPHA, (GlStateManager.class_4535)GlStateManager.class_4535.ONE, (GlStateManager.class_4534)GlStateManager.class_4534.ZERO);
            RenderSystem.setShader((class_10156)class_10142.field_53876);
            RenderSystem.lineWidth((float)2.0f);
            for (class_1297 e : SkeletonESPModule.mc.field_1687.method_18112()) {
                class_1657 p;
                if (!(e instanceof class_1657) || (p = (class_1657)e) == SkeletonESPModule.mc.field_1724 && !((Boolean)this.renderSelf.getValue()).booleanValue() || p.method_5767()) continue;
                this.render(ms, p, event.partialTicks());
            }
            RenderSystem.depthMask((boolean)true);
            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();
            ms.method_22909();
        }));
        this.addEvents(renderEvent);
    }

    private void render(class_4587 ms, class_1657 p, float t) {
        if (p == SkeletonESPModule.mc.field_1724 && SkeletonESPModule.mc.field_1690.method_31044().method_31034()) {
            return;
        }
        if (p == SkeletonESPModule.mc.field_1724 && !((Boolean)this.renderSelf.getValue()).booleanValue()) {
            return;
        }
        double x = class_3532.method_16436((double)t, (double)p.field_6014, (double)p.method_23317());
        double y = class_3532.method_16436((double)t, (double)p.field_6036, (double)p.method_23318());
        double z = class_3532.method_16436((double)t, (double)p.field_5969, (double)p.method_23321());
        float bodyYaw = class_3532.method_17821((float)t, (float)p.field_6220, (float)p.field_6283);
        float headYaw = class_3532.method_17821((float)t, (float)p.field_6259, (float)p.field_6241);
        float pitch = class_3532.method_16439((float)t, (float)p.field_6004, (float)p.method_36455());
        float swing = p.field_42108.method_48572(t);
        float swingAmt = p.field_42108.method_48570(t);
        float handSwing = p.method_6055(t);
        boolean elytra = p.method_6128();
        Color c = (Color)this.color.getValue();
        for (class_243[] bone : this.getBones(x, y, z, bodyYaw, headYaw, pitch, swing, swingAmt, handSwing, p.method_17682(), elytra, p.method_5715())) {
            this.line(ms, bone[0], bone[1], c);
        }
    }

    private List<class_243[]> getBones(double x, double y, double z, float bodyYaw, float headYaw, float pitch, float swing, float swingAmt, float handSwing, float h, boolean elytra, boolean sneak) {
        float rArmRot;
        float lArmRot;
        ArrayList<class_243[]> bones = new ArrayList<class_243[]>();
        class_4587 ms = new class_4587();
        ms.method_22904(x, y, z);
        if (sneak && !elytra) {
            ms.method_22904(0.0, 0.125, 0.0);
        }
        ms.method_22907(class_7833.field_40716.rotationDegrees(-bodyYaw));
        float bodyPitch = 0.0f;
        if (elytra) {
            bodyPitch = 1.57f + pitch / 57.2958f;
        } else if (sneak) {
            bodyPitch = 0.5f;
        }
        if (elytra || sneak) {
            ms.method_22907(class_7833.field_40714.rotationDegrees(bodyPitch * 57.2958f));
        }
        if (sneak && !elytra) {
            ms.method_22904(0.0, -0.13, 0.0);
        }
        class_243 base = this.getPos(ms);
        ms.method_22903();
        ms.method_22904(0.0, (double)h * 0.75, 0.0);
        class_243 neck = this.getPos(ms);
        ms.method_22903();
        ms.method_22907(class_7833.field_40716.rotationDegrees(bodyYaw - headYaw));
        if (!elytra) {
            ms.method_22907(class_7833.field_40714.rotationDegrees(pitch));
        }
        ms.method_22904(0.0, (double)h * 0.15, 0.0);
        class_243 head = this.getPos(ms);
        ms.method_22909();
        swingAmt = Math.min(swingAmt, 1.0f) * 0.5f;
        ms.method_22903();
        ms.method_22904(0.4, 0.0, 0.0);
        class_243 lShoulder = this.getPos(ms);
        if (elytra) {
            lArmRot = -0.2f;
            ms.method_22907(class_7833.field_40718.rotationDegrees(-5.0f));
        } else {
            lArmRot = class_3532.method_15362((float)(swing * 0.6662f + (float)Math.PI)) * 0.8f * swingAmt;
        }
        ms.method_22907(class_7833.field_40714.rotationDegrees(lArmRot * 57.2958f));
        ms.method_22904(0.0, -0.25, 0.0);
        class_243 lElbow = this.getPos(ms);
        ms.method_22907(class_7833.field_40714.rotationDegrees(Math.max(0.0f, lArmRot * 15.0f)));
        ms.method_22904(0.0, -0.25, 0.0);
        class_243 lHand = this.getPos(ms);
        ms.method_22909();
        ms.method_22903();
        ms.method_22904(-0.4, 0.0, 0.0);
        class_243 rShoulder = this.getPos(ms);
        if (elytra) {
            rArmRot = -0.2f;
            ms.method_22907(class_7833.field_40718.rotationDegrees(5.0f));
        } else {
            rArmRot = class_3532.method_15362((float)(swing * 0.6662f)) * 0.8f * swingAmt;
        }
        if (handSwing > 0.0f && !elytra) {
            float swingProgress = 1.0f - handSwing;
            swingProgress *= swingProgress;
            float swingRot = class_3532.method_15374((float)(swingProgress * (float)Math.PI));
            float headYawDiff = headYaw - bodyYaw;
            float yawFactor = class_3532.method_15363((float)(headYawDiff / 75.0f), (float)-1.0f, (float)1.0f);
            ms.method_22907(class_7833.field_40716.rotationDegrees(swingRot * 15.0f * yawFactor));
            rArmRot += -swingRot * 0.8f;
        }
        ms.method_22907(class_7833.field_40714.rotationDegrees(rArmRot * 57.2958f));
        ms.method_22904(0.0, -0.25, 0.0);
        class_243 rElbow = this.getPos(ms);
        ms.method_22907(class_7833.field_40714.rotationDegrees(Math.max(0.0f, rArmRot * 15.0f)));
        ms.method_22904(0.0, -0.25, 0.0);
        class_243 rHand = this.getPos(ms);
        ms.method_22909();
        ms.method_22909();
        ms.method_22903();
        ms.method_22904(0.0, (double)h * 0.5, 0.0);
        class_243 waist = this.getPos(ms);
        ms.method_22909();
        ms.method_22903();
        ms.method_22904(0.0, (double)h * 0.3, 0.0);
        class_243 pelvis = this.getPos(ms);
        ms.method_22903();
        ms.method_22904(0.125, 0.0, 0.0);
        class_243 lHip = this.getPos(ms);
        float lLegRot = class_3532.method_15362((float)(swing * 0.6662f)) * 0.5f * swingAmt;
        if (elytra) {
            lLegRot = 0.1f;
        }
        ms.method_22907(class_7833.field_40714.rotationDegrees(lLegRot * 57.2958f));
        ms.method_22904(0.0, -0.25, 0.0);
        class_243 lKnee = this.getPos(ms);
        ms.method_22907(class_7833.field_40714.rotationDegrees(Math.abs(lLegRot) * 15.0f));
        ms.method_22904(0.0, -0.25, 0.0);
        class_243 lFoot = this.getPos(ms);
        ms.method_22909();
        ms.method_22903();
        ms.method_22904(-0.125, 0.0, 0.0);
        class_243 rHip = this.getPos(ms);
        float rLegRot = class_3532.method_15362((float)(swing * 0.6662f + (float)Math.PI)) * 0.5f * swingAmt;
        if (elytra) {
            rLegRot = 0.1f;
        }
        ms.method_22907(class_7833.field_40714.rotationDegrees(rLegRot * 57.2958f));
        ms.method_22904(0.0, -0.25, 0.0);
        class_243 rKnee = this.getPos(ms);
        ms.method_22907(class_7833.field_40714.rotationDegrees(Math.abs(rLegRot) * 15.0f));
        ms.method_22904(0.0, -0.25, 0.0);
        class_243 rFoot = this.getPos(ms);
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
        return new class_243((double)pos.x, (double)pos.y, (double)pos.z);
    }

    private void line(class_4587 ms, class_243 start, class_243 end, Color c) {
        Matrix4f m = ms.method_23760().method_23761();
        class_287 buf = class_289.method_1348().method_60827(class_293.class_5596.field_29344, class_290.field_1576);
        buf.method_22918(m, (float)start.field_1352, (float)start.field_1351, (float)start.field_1350).method_1336(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
        buf.method_22918(m, (float)end.field_1352, (float)end.field_1351, (float)end.field_1350).method_1336(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
        class_286.method_43433((class_9801)buf.method_60800());
    }

    @Generated
    public static SkeletonESPModule getInstance() {
        return instance;
    }
}


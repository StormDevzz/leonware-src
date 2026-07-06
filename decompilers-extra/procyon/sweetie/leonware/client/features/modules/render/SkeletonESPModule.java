// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import net.minecraft.class_287;
import org.joml.Matrix4f;
import net.minecraft.class_286;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_289;
import org.joml.Vector3f;
import net.minecraft.class_7833;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_3532;
import java.util.Iterator;
import net.minecraft.class_243;
import net.minecraft.class_4587;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import net.minecraft.class_1657;
import net.minecraft.class_1297;
import net.minecraft.class_10142;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.module.setting.Setting;
import java.awt.Color;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ColorSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Skeleton ESP", category = Category.RENDER)
public class SkeletonESPModule extends Module
{
    private static final SkeletonESPModule instance;
    private final ColorSetting color;
    private final BooleanSetting renderSelf;
    
    public SkeletonESPModule() {
        this.color = new ColorSetting("\u0426\u0432\u0435\u0442").value(new Color(255, 255, 255));
        this.renderSelf = new BooleanSetting("\u0420\u0435\u043d\u0434\u0435\u0440\u0438\u0442\u044c \u0441\u0435\u0431\u044f").value(false);
        this.addSettings(this.color, this.renderSelf);
    }
    
    @Override
    public void onEvent() {
        final EventListener renderEvent = Render3DEvent.getInstance().subscribe(new Listener<Render3DEvent.Render3DEventData>(event -> {
            final class_4587 ms = event.matrixStack();
            final class_243 cam = SkeletonESPModule.mc.method_1561().field_4686.method_19326();
            ms.method_22903();
            ms.method_22904(-cam.field_1352, -cam.field_1351, -cam.field_1350);
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE_MINUS_SRC_ALPHA, GlStateManager.class_4535.ONE, GlStateManager.class_4534.ZERO);
            RenderSystem.setShader(class_10142.field_53876);
            RenderSystem.lineWidth(2.0f);
            SkeletonESPModule.mc.field_1687.method_18112().iterator();
            final Iterator iterator;
            while (iterator.hasNext()) {
                final class_1297 e = iterator.next();
                if (e instanceof final class_1657 p) {
                    if (p == SkeletonESPModule.mc.field_1724 && !this.renderSelf.getValue()) {
                        continue;
                    }
                    else if (p.method_5767()) {
                        continue;
                    }
                    else {
                        this.render(ms, p, event.partialTicks());
                    }
                }
            }
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();
            ms.method_22909();
            return;
        }));
        this.addEvents(renderEvent);
    }
    
    private void render(final class_4587 ms, final class_1657 p, final float t) {
        if (p == SkeletonESPModule.mc.field_1724 && SkeletonESPModule.mc.field_1690.method_31044().method_31034()) {
            return;
        }
        if (p == SkeletonESPModule.mc.field_1724 && !this.renderSelf.getValue()) {
            return;
        }
        final double x = class_3532.method_16436((double)t, p.field_6014, p.method_23317());
        final double y = class_3532.method_16436((double)t, p.field_6036, p.method_23318());
        final double z = class_3532.method_16436((double)t, p.field_5969, p.method_23321());
        final float bodyYaw = class_3532.method_17821(t, p.field_6220, p.field_6283);
        final float headYaw = class_3532.method_17821(t, p.field_6259, p.field_6241);
        final float pitch = class_3532.method_16439(t, p.field_6004, p.method_36455());
        final float swing = p.field_42108.method_48572(t);
        final float swingAmt = p.field_42108.method_48570(t);
        final float handSwing = p.method_6055(t);
        final boolean elytra = p.method_6128();
        final Color c = this.color.getValue();
        for (final class_243[] bone : this.getBones(x, y, z, bodyYaw, headYaw, pitch, swing, swingAmt, handSwing, p.method_17682(), elytra, p.method_5715())) {
            this.line(ms, bone[0], bone[1], c);
        }
    }
    
    private List<class_243[]> getBones(final double x, final double y, final double z, final float bodyYaw, final float headYaw, final float pitch, final float swing, float swingAmt, final float handSwing, final float h, final boolean elytra, final boolean sneak) {
        final List<class_243[]> bones = new ArrayList<class_243[]>();
        final class_4587 ms = new class_4587();
        ms.method_22904(x, y, z);
        if (sneak && !elytra) {
            ms.method_22904(0.0, 0.125, 0.0);
        }
        ms.method_22907(class_7833.field_40716.rotationDegrees(-bodyYaw));
        float bodyPitch = 0.0f;
        if (elytra) {
            bodyPitch = 1.57f + pitch / 57.2958f;
        }
        else if (sneak) {
            bodyPitch = 0.5f;
        }
        if (elytra || sneak) {
            ms.method_22907(class_7833.field_40714.rotationDegrees(bodyPitch * 57.2958f));
        }
        if (sneak && !elytra) {
            ms.method_22904(0.0, -0.13, 0.0);
        }
        final class_243 base = this.getPos(ms);
        ms.method_22903();
        ms.method_22904(0.0, h * 0.75, 0.0);
        final class_243 neck = this.getPos(ms);
        ms.method_22903();
        ms.method_22907(class_7833.field_40716.rotationDegrees(bodyYaw - headYaw));
        if (!elytra) {
            ms.method_22907(class_7833.field_40714.rotationDegrees(pitch));
        }
        ms.method_22904(0.0, h * 0.15, 0.0);
        final class_243 head = this.getPos(ms);
        ms.method_22909();
        swingAmt = Math.min(swingAmt, 1.0f) * 0.5f;
        ms.method_22903();
        ms.method_22904(0.4, 0.0, 0.0);
        final class_243 lShoulder = this.getPos(ms);
        float lArmRot;
        if (elytra) {
            lArmRot = -0.2f;
            ms.method_22907(class_7833.field_40718.rotationDegrees(-5.0f));
        }
        else {
            lArmRot = class_3532.method_15362(swing * 0.6662f + 3.1415927f) * 0.8f * swingAmt;
        }
        ms.method_22907(class_7833.field_40714.rotationDegrees(lArmRot * 57.2958f));
        ms.method_22904(0.0, -0.25, 0.0);
        final class_243 lElbow = this.getPos(ms);
        ms.method_22907(class_7833.field_40714.rotationDegrees(Math.max(0.0f, lArmRot * 15.0f)));
        ms.method_22904(0.0, -0.25, 0.0);
        final class_243 lHand = this.getPos(ms);
        ms.method_22909();
        ms.method_22903();
        ms.method_22904(-0.4, 0.0, 0.0);
        final class_243 rShoulder = this.getPos(ms);
        float rArmRot;
        if (elytra) {
            rArmRot = -0.2f;
            ms.method_22907(class_7833.field_40718.rotationDegrees(5.0f));
        }
        else {
            rArmRot = class_3532.method_15362(swing * 0.6662f) * 0.8f * swingAmt;
        }
        if (handSwing > 0.0f && !elytra) {
            float swingProgress = 1.0f - handSwing;
            swingProgress *= swingProgress;
            final float swingRot = class_3532.method_15374(swingProgress * 3.1415927f);
            final float headYawDiff = headYaw - bodyYaw;
            final float yawFactor = class_3532.method_15363(headYawDiff / 75.0f, -1.0f, 1.0f);
            ms.method_22907(class_7833.field_40716.rotationDegrees(swingRot * 15.0f * yawFactor));
            rArmRot += -swingRot * 0.8f;
        }
        ms.method_22907(class_7833.field_40714.rotationDegrees(rArmRot * 57.2958f));
        ms.method_22904(0.0, -0.25, 0.0);
        final class_243 rElbow = this.getPos(ms);
        ms.method_22907(class_7833.field_40714.rotationDegrees(Math.max(0.0f, rArmRot * 15.0f)));
        ms.method_22904(0.0, -0.25, 0.0);
        final class_243 rHand = this.getPos(ms);
        ms.method_22909();
        ms.method_22909();
        ms.method_22903();
        ms.method_22904(0.0, h * 0.5, 0.0);
        final class_243 waist = this.getPos(ms);
        ms.method_22909();
        ms.method_22903();
        ms.method_22904(0.0, h * 0.3, 0.0);
        final class_243 pelvis = this.getPos(ms);
        ms.method_22903();
        ms.method_22904(0.125, 0.0, 0.0);
        final class_243 lHip = this.getPos(ms);
        float lLegRot = class_3532.method_15362(swing * 0.6662f) * 0.5f * swingAmt;
        if (elytra) {
            lLegRot = 0.1f;
        }
        ms.method_22907(class_7833.field_40714.rotationDegrees(lLegRot * 57.2958f));
        ms.method_22904(0.0, -0.25, 0.0);
        final class_243 lKnee = this.getPos(ms);
        ms.method_22907(class_7833.field_40714.rotationDegrees(Math.abs(lLegRot) * 15.0f));
        ms.method_22904(0.0, -0.25, 0.0);
        final class_243 lFoot = this.getPos(ms);
        ms.method_22909();
        ms.method_22903();
        ms.method_22904(-0.125, 0.0, 0.0);
        final class_243 rHip = this.getPos(ms);
        float rLegRot = class_3532.method_15362(swing * 0.6662f + 3.1415927f) * 0.5f * swingAmt;
        if (elytra) {
            rLegRot = 0.1f;
        }
        ms.method_22907(class_7833.field_40714.rotationDegrees(rLegRot * 57.2958f));
        ms.method_22904(0.0, -0.25, 0.0);
        final class_243 rKnee = this.getPos(ms);
        ms.method_22907(class_7833.field_40714.rotationDegrees(Math.abs(rLegRot) * 15.0f));
        ms.method_22904(0.0, -0.25, 0.0);
        final class_243 rFoot = this.getPos(ms);
        ms.method_22909();
        ms.method_22909();
        ms.method_22909();
        bones.add(new class_243[] { neck, head });
        bones.add(new class_243[] { neck, waist });
        bones.add(new class_243[] { waist, pelvis });
        bones.add(new class_243[] { neck, lShoulder });
        bones.add(new class_243[] { neck, rShoulder });
        bones.add(new class_243[] { lShoulder, lElbow });
        bones.add(new class_243[] { lElbow, lHand });
        bones.add(new class_243[] { rShoulder, rElbow });
        bones.add(new class_243[] { rElbow, rHand });
        bones.add(new class_243[] { pelvis, lHip });
        bones.add(new class_243[] { pelvis, rHip });
        bones.add(new class_243[] { lHip, lKnee });
        bones.add(new class_243[] { lKnee, lFoot });
        bones.add(new class_243[] { rHip, rKnee });
        bones.add(new class_243[] { rKnee, rFoot });
        return bones;
    }
    
    private class_243 getPos(final class_4587 ms) {
        final Vector3f pos = ms.method_23760().method_23761().transformPosition(0.0f, 0.0f, 0.0f, new Vector3f());
        return new class_243((double)pos.x, (double)pos.y, (double)pos.z);
    }
    
    private void line(final class_4587 ms, final class_243 start, final class_243 end, final Color c) {
        final Matrix4f m = ms.method_23760().method_23761();
        final class_287 buf = class_289.method_1348().method_60827(class_293.class_5596.field_29344, class_290.field_1576);
        buf.method_22918(m, (float)start.field_1352, (float)start.field_1351, (float)start.field_1350).method_1336(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
        buf.method_22918(m, (float)end.field_1352, (float)end.field_1351, (float)end.field_1350).method_1336(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
        class_286.method_43433(buf.method_60800());
    }
    
    @Generated
    public static SkeletonESPModule getInstance() {
        return SkeletonESPModule.instance;
    }
    
    static {
        instance = new SkeletonESPModule();
    }
}

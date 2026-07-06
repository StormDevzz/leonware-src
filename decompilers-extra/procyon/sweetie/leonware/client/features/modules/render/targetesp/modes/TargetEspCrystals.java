// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render.targetesp.modes;

import org.joml.Vector3f;
import sweetie.leonware.api.system.files.FileUtil;
import org.joml.Matrix4f;
import net.minecraft.class_287;
import net.minecraft.class_286;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_289;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_10142;
import sweetie.leonware.api.utils.color.ColorUtil;
import net.minecraft.class_7833;
import java.awt.Color;
import java.util.Iterator;
import net.minecraft.class_4587;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import net.minecraft.class_1297;
import sweetie.leonware.client.features.modules.render.targetesp.TargetEspModule;
import net.minecraft.class_1309;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;
import sweetie.leonware.client.features.modules.render.targetesp.TargetEspMode;

public class TargetEspCrystals extends TargetEspMode
{
    private final List<CrystalData> crystals;
    private final Random random;
    private Object lastTarget;
    private float animationTick;
    private float prevAnimationTick;
    
    public TargetEspCrystals() {
        this.crystals = new ArrayList<CrystalData>();
        this.random = new Random();
        this.lastTarget = null;
        this.animationTick = 0.0f;
        this.prevAnimationTick = 0.0f;
    }
    
    @Override
    public void onUpdate() {
        if (this.aura().target != null) {
            TargetEspCrystals.currentTarget = this.aura().target;
        }
        else {
            final class_1297 field_1692 = TargetEspCrystals.mc.field_1692;
            if (field_1692 instanceof final class_1309 class_1309) {
                final class_1309 living = TargetEspCrystals.currentTarget = class_1309;
            }
        }
        this.prevAnimationTick = this.animationTick;
        this.animationTick += TargetEspModule.instance.getCrystalsSpeed();
        if (TargetEspCrystals.currentTarget != this.lastTarget) {
            this.crystals.clear();
            if (TargetEspCrystals.currentTarget != null) {
                this.generateCrystals();
            }
            this.lastTarget = TargetEspCrystals.currentTarget;
        }
    }
    
    @Override
    public void onRender3D(final Render3DEvent.Render3DEventData event) {
        if (TargetEspCrystals.currentTarget == null || !this.canDraw() || this.crystals.isEmpty()) {
            return;
        }
        final class_4587 ms = event.matrixStack();
        final float alphaAnim = (float)TargetEspCrystals.showAnimation.getValue();
        final float tickDelta = TargetEspCrystals.mc.method_61966().method_60637(true);
        final float renderTick = this.prevAnimationTick + (this.animationTick - this.prevAnimationTick) * tickDelta;
        RenderUtil.WORLD.startRender(ms);
        final double tx = getTargetX() - TargetEspCrystals.mc.method_1561().field_4686.method_19326().method_10216();
        final double ty = getTargetY() - TargetEspCrystals.mc.method_1561().field_4686.method_19326().method_10214();
        final double tz = getTargetZ() - TargetEspCrystals.mc.method_1561().field_4686.method_19326().method_10215();
        final float orbitSpeed = 0.02f * TargetEspModule.instance.getCrystalsSpeed();
        for (final CrystalData crystal : this.crystals) {
            final float floatAnim = (float)Math.sin(Math.toRadians(renderTick + crystal.index * 35)) * 0.06f;
            final float orbitAngle = crystal.baseAngle + renderTick * orbitSpeed;
            final double rx = tx + Math.cos(orbitAngle) * crystal.baseRadius;
            final double ry = ty + crystal.pos.y() + floatAnim;
            final double rz = tz + Math.sin(orbitAngle) * crystal.baseRadius;
            final Color finalColor = UIColors.gradient(crystal.index * 30);
            this.renderCrystal(ms, rx, ry, rz, crystal, finalColor, alphaAnim, renderTick);
            this.renderGlow(ms, rx, ry, rz, finalColor, alphaAnim * 0.45f, crystal.scale);
        }
        RenderUtil.WORLD.endRender(ms);
    }
    
    private void renderCrystal(final class_4587 ms, final double x, final double y, final double z, final CrystalData data, final Color color, final float alpha, final float renderTick) {
        ms.method_22903();
        ms.method_22904(x, y, z);
        final float s = data.scale * 0.12f;
        ms.method_22905(s, s, s);
        ms.method_22907(class_7833.field_40714.rotationDegrees(data.rot.x()));
        ms.method_22907(class_7833.field_40716.rotationDegrees(data.rot.y() + renderTick * 1.5f));
        ms.method_22907(class_7833.field_40718.rotationDegrees(data.rot.z() + renderTick * 0.7f));
        final float[] c = ColorUtil.normalize(ColorUtil.setAlpha(color, (int)(220.0f * alpha)));
        RenderSystem.setShader(class_10142.field_53876);
        final class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27379, class_290.field_1576);
        final Matrix4f matrix = ms.method_23760().method_23761();
        final float w = 1.0f;
        final float h = 2.0f;
        this.drawFace(buffer, matrix, 0.0f, h, 0.0f, -w, 0.0f, w, w, 0.0f, w, c);
        this.drawFace(buffer, matrix, 0.0f, h, 0.0f, w, 0.0f, w, w, 0.0f, -w, c);
        this.drawFace(buffer, matrix, 0.0f, h, 0.0f, w, 0.0f, -w, -w, 0.0f, -w, c);
        this.drawFace(buffer, matrix, 0.0f, h, 0.0f, -w, 0.0f, -w, -w, 0.0f, w, c);
        this.drawFace(buffer, matrix, 0.0f, -h, 0.0f, w, 0.0f, w, -w, 0.0f, w, c);
        this.drawFace(buffer, matrix, 0.0f, -h, 0.0f, w, 0.0f, -w, w, 0.0f, w, c);
        this.drawFace(buffer, matrix, 0.0f, -h, 0.0f, -w, 0.0f, -w, w, 0.0f, -w, c);
        this.drawFace(buffer, matrix, 0.0f, -h, 0.0f, -w, 0.0f, w, -w, 0.0f, -w, c);
        class_286.method_43433(buffer.method_60800());
        ms.method_22909();
    }
    
    private void drawFace(final class_287 b, final Matrix4f m, final float x1, final float y1, final float z1, final float x2, final float y2, final float z2, final float x3, final float y3, final float z3, final float[] c) {
        b.method_22918(m, x1, y1, z1).method_22915(c[0], c[1], c[2], c[3]);
        b.method_22918(m, x2, y2, z2).method_22915(c[0], c[1], c[2], c[3]);
        b.method_22918(m, x3, y3, z3).method_22915(c[0], c[1], c[2], c[3]);
    }
    
    private void renderGlow(final class_4587 ms, final double x, final double y, final double z, final Color color, final float alpha, final float scale) {
        ms.method_22903();
        ms.method_22904(x, y, z);
        ms.method_22907(class_7833.field_40716.rotationDegrees(-TargetEspCrystals.mc.field_1773.method_19418().method_19330() + 180.0f));
        ms.method_22907(class_7833.field_40714.rotationDegrees(-TargetEspCrystals.mc.field_1773.method_19418().method_19329() + 180.0f));
        final float size = 0.5f * scale;
        final float[] c = ColorUtil.normalize(ColorUtil.setAlpha(color, (int)(255.0f * alpha)));
        RenderSystem.setShaderTexture(0, FileUtil.getImage("particles/glow"));
        RenderSystem.setShader(class_10142.field_53880);
        final class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
        final Matrix4f matrix = ms.method_23760().method_23761();
        buffer.method_22918(matrix, -size, size, 0.0f).method_22913(0.0f, 1.0f).method_22915(c[0], c[1], c[2], c[3]);
        buffer.method_22918(matrix, size, size, 0.0f).method_22913(1.0f, 1.0f).method_22915(c[0], c[1], c[2], c[3]);
        buffer.method_22918(matrix, size, -size, 0.0f).method_22913(1.0f, 0.0f).method_22915(c[0], c[1], c[2], c[3]);
        buffer.method_22918(matrix, -size, -size, 0.0f).method_22913(0.0f, 0.0f).method_22915(c[0], c[1], c[2], c[3]);
        class_286.method_43433(buffer.method_60800());
        ms.method_22909();
    }
    
    private void generateCrystals() {
        this.crystals.clear();
        final int count = TargetEspModule.instance.getCrystalsCount();
        final float height = TargetEspCrystals.currentTarget.method_17682();
        final float width = TargetEspCrystals.currentTarget.method_17681();
        final float baseRadius = Math.max(0.75f, width * 0.9f + 0.35f);
        final float minY = 0.15f;
        final float maxY = Math.max(minY + 0.2f, height - 0.15f);
        for (int i = 0; i < count; ++i) {
            final float angle = (float)(i / (float)count * 3.141592653589793 * 2.0);
            final float y = minY + (maxY - minY) * (i % 3) / 2.0f;
            this.crystals.add(new CrystalData(new Vector3f(0.0f, y, 0.0f), new Vector3f(20.0f + this.random.nextFloat() * 40.0f, this.random.nextFloat() * 360.0f, this.random.nextFloat() * 360.0f), 0.75f + this.random.nextFloat() * 0.35f, i, angle, baseRadius + (this.random.nextFloat() - 0.5f) * 0.18f));
        }
    }
    
    private static class CrystalData
    {
        private final Vector3f pos;
        private final Vector3f rot;
        private final float scale;
        private final float baseAngle;
        private final float baseRadius;
        private final int index;
        
        public CrystalData(final Vector3f pos, final Vector3f rot, final float scale, final int index, final float baseAngle, final float baseRadius) {
            this.pos = pos;
            this.rot = rot;
            this.scale = scale;
            this.index = index;
            this.baseAngle = baseAngle;
            this.baseRadius = baseRadius;
        }
    }
}

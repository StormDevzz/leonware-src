// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render.targetesp.modes;

import net.minecraft.class_287;
import org.joml.Matrix4f;
import net.minecraft.class_4184;
import net.minecraft.class_286;
import sweetie.leonware.api.utils.color.ColorUtil;
import sweetie.leonware.api.utils.color.UIColors;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_289;
import net.minecraft.class_10142;
import sweetie.leonware.api.system.files.FileUtil;
import net.minecraft.class_7833;
import net.minecraft.class_4587;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Collection;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import java.util.ArrayList;
import java.util.List;
import sweetie.leonware.client.features.modules.render.targetesp.TargetEspMode;

public class TargetEspComets extends TargetEspMode
{
    private final List<double[]> previousPositions;
    private float ghostRotationAngle;
    private float ghostYRotationAngle;
    private float prevGhostRotationAngle;
    private float prevGhostYRotationAngle;
    
    public TargetEspComets() {
        this.previousPositions = new ArrayList<double[]>();
        this.ghostRotationAngle = 0.0f;
        this.ghostYRotationAngle = 0.0f;
        this.prevGhostRotationAngle = 0.0f;
        this.prevGhostYRotationAngle = 0.0f;
    }
    
    @Override
    public void onUpdate() {
        this.updateTarget();
        this.prevGhostRotationAngle = this.ghostRotationAngle;
        this.prevGhostYRotationAngle = this.ghostYRotationAngle;
        this.ghostRotationAngle += 15.0f;
        this.ghostYRotationAngle += 15.0f;
    }
    
    @Override
    public void onRender3D(final Render3DEvent.Render3DEventData event) {
        if (TargetEspComets.currentTarget == null || !this.canDraw()) {
            return;
        }
        final class_4587 matrixStack = event.matrixStack();
        RenderUtil.WORLD.startRender(matrixStack);
        final float size = 0.25f;
        final float dony = 1.15f * MathUtil.interpolate(this.prevSizeAnimation, TargetEspComets.sizeAnimation.getValue());
        final float rem = (float)(0.4 * MathUtil.interpolate(this.prevSizeAnimation, TargetEspComets.sizeAnimation.getValue()));
        final int trailLength = 21;
        final List<double[]> currentGhostPositions = new ArrayList<double[]>();
        final double centerX = TargetEspMode.getTargetX();
        final double centerY = getTargetY() + TargetEspComets.currentTarget.method_17682() / 2.0f;
        final double centerZ = TargetEspMode.getTargetZ();
        for (int i = 0; i < 3; ++i) {
            final float angle = MathUtil.interpolate(this.prevGhostRotationAngle, this.ghostRotationAngle) + i * 180;
            final double radius = TargetEspComets.currentTarget.method_17681() * dony;
            double offsetX = Math.cos(Math.toRadians(angle)) * radius;
            final double offsetZ = Math.sin(Math.toRadians(angle)) * radius;
            double offsetY = Math.cos(Math.toRadians(angle)) / 3.0 * radius;
            final double ghostYI = MathUtil.interpolate(this.prevGhostYRotationAngle, this.ghostYRotationAngle);
            if (i == 0) {
                offsetY = Math.sin(Math.toRadians(ghostYI)) * TargetEspComets.currentTarget.method_17682() * rem;
            }
            else if (i == 2) {
                offsetY = -Math.sin(Math.toRadians(ghostYI)) * TargetEspComets.currentTarget.method_17682() * rem;
                offsetX = -Math.cos(Math.toRadians(angle)) * radius;
            }
            final double ghostX = centerX + offsetX;
            final double ghostY = centerY + offsetY;
            final double ghostZ = centerZ + offsetZ;
            currentGhostPositions.add(new double[] { ghostX, ghostY, ghostZ, angle });
        }
        this.previousPositions.addAll(currentGhostPositions);
        if (this.previousPositions.size() > trailLength * 3) {
            this.previousPositions.subList(0, this.previousPositions.size() - trailLength * 3).clear();
        }
        for (int i = 0; i < 3; ++i) {
            final double[] currentPos = currentGhostPositions.get(i);
            final float angle2 = (float)currentPos[3];
            final double renderX = currentPos[0] - TargetEspComets.mc.method_1561().field_4686.method_19326().method_10216();
            final double renderY = currentPos[1] - TargetEspComets.mc.method_1561().field_4686.method_19326().method_10214();
            final double renderZ = currentPos[2] - TargetEspComets.mc.method_1561().field_4686.method_19326().method_10215();
            this.renderGhost(matrixStack, renderX, renderY, renderZ, angle2, size, 1.0f);
            for (int t = 0; t < this.previousPositions.size() / 3; ++t) {
                final int index = t * 3 + i;
                if (index < this.previousPositions.size()) {
                    final double[] trailPos = this.previousPositions.get(index);
                    final double trailRenderX = trailPos[0] - TargetEspComets.mc.method_1561().field_4686.method_19326().method_10216();
                    final double trailRenderY = trailPos[1] - TargetEspComets.mc.method_1561().field_4686.method_19326().method_10214();
                    final double trailRenderZ = trailPos[2] - TargetEspComets.mc.method_1561().field_4686.method_19326().method_10215();
                    final float trailAngle = (float)trailPos[3];
                    final float trailAlpha = (t + 1) / (this.previousPositions.size() / 3.0f + 1.0f);
                    final float trailSize = Math.max(size * 0.4f, size * trailAlpha);
                    this.renderGhost(matrixStack, trailRenderX, trailRenderY, trailRenderZ, trailAngle, trailSize, trailAlpha);
                }
            }
        }
        RenderSystem.enableCull();
        RenderSystem.blendFunc(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderUtil.WORLD.endRender(matrixStack);
    }
    
    public void renderGhost(final class_4587 stack, final double x, final double y, final double z, final float angle, final float size, final float alphaMultiplier) {
        stack.method_22903();
        stack.method_22904(x, y, z);
        final class_4184 camera = TargetEspComets.mc.field_1773.method_19418();
        stack.method_22907(class_7833.field_40716.rotationDegrees(-camera.method_19330() + 180.0f));
        stack.method_22907(class_7833.field_40714.rotationDegrees(-camera.method_19329() + 180.0f));
        RenderSystem.setShaderTexture(0, FileUtil.getImage("particles/glow"));
        final Matrix4f matrix = stack.method_23760().method_23761();
        RenderSystem.setShader(class_10142.field_53880);
        final class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
        final float[] c = ColorUtil.normalize(ColorUtil.setAlpha(UIColors.gradient((int)angle), (int)(255.0 * TargetEspComets.showAnimation.getValue())));
        final float alpha = c[3] * alphaMultiplier;
        buffer.method_22918(matrix, -size, size, 0.0f).method_22913(0.0f, 1.0f).method_22915(c[0], c[1], c[2], alpha);
        buffer.method_22918(matrix, size, size, 0.0f).method_22913(1.0f, 1.0f).method_22915(c[0], c[1], c[2], alpha);
        buffer.method_22918(matrix, size, -size, 0.0f).method_22913(1.0f, 0.0f).method_22915(c[0], c[1], c[2], alpha);
        buffer.method_22918(matrix, -size, -size, 0.0f).method_22913(0.0f, 0.0f).method_22915(c[0], c[1], c[2], alpha);
        class_286.method_43433(buffer.method_60800());
        stack.method_22909();
    }
}

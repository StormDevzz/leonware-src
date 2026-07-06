package sweetie.leonware.client.features.modules.render.targetesp.modes;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_10142;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_7833;
import org.joml.Matrix4f;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.system.files.FileUtil;
import sweetie.leonware.api.utils.color.ColorUtil;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.client.features.modules.render.targetesp.TargetEspMode;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/targetesp/modes/TargetEspComets.class */
public class TargetEspComets extends TargetEspMode {
    private final List<double[]> previousPositions = new ArrayList();
    private float ghostRotationAngle = 0.0f;
    private float ghostYRotationAngle = 0.0f;
    private float prevGhostRotationAngle = 0.0f;
    private float prevGhostYRotationAngle = 0.0f;

    @Override // sweetie.leonware.client.features.modules.render.targetesp.TargetEspMode
    public void onUpdate() {
        updateTarget();
        this.prevGhostRotationAngle = this.ghostRotationAngle;
        this.prevGhostYRotationAngle = this.ghostYRotationAngle;
        this.ghostRotationAngle += 15.0f;
        this.ghostYRotationAngle += 15.0f;
    }

    @Override // sweetie.leonware.client.features.modules.render.targetesp.TargetEspMode
    public void onRender3D(Render3DEvent.Render3DEventData event) {
        if (currentTarget == null || !canDraw()) {
            return;
        }
        class_4587 matrixStack = event.matrixStack();
        RenderUtil.WORLD.startRender(matrixStack);
        float dony = 1.15f * MathUtil.interpolate(this.prevSizeAnimation, sizeAnimation.getValue());
        float rem = (float) (0.4d * ((double) MathUtil.interpolate(this.prevSizeAnimation, sizeAnimation.getValue())));
        List<double[]> currentGhostPositions = new ArrayList<>();
        double centerX = getTargetX();
        double centerY = getTargetY() + ((double) (currentTarget.method_17682() / 2.0f));
        double centerZ = getTargetZ();
        for (int i = 0; i < 3; i++) {
            float angle = MathUtil.interpolate(this.prevGhostRotationAngle, this.ghostRotationAngle) + (i * 180);
            double radius = currentTarget.method_17681() * dony;
            double offsetX = Math.cos(Math.toRadians(angle)) * radius;
            double offsetZ = Math.sin(Math.toRadians(angle)) * radius;
            double offsetY = (Math.cos(Math.toRadians(angle)) / 3.0d) * radius;
            double ghostYI = MathUtil.interpolate(this.prevGhostYRotationAngle, this.ghostYRotationAngle);
            if (i == 0) {
                offsetY = Math.sin(Math.toRadians(ghostYI)) * ((double) currentTarget.method_17682()) * ((double) rem);
            } else if (i == 2) {
                offsetY = (-Math.sin(Math.toRadians(ghostYI))) * ((double) currentTarget.method_17682()) * ((double) rem);
                offsetX = (-Math.cos(Math.toRadians(angle))) * radius;
            }
            double ghostX = centerX + offsetX;
            double ghostY = centerY + offsetY;
            double ghostZ = centerZ + offsetZ;
            currentGhostPositions.add(new double[]{ghostX, ghostY, ghostZ, angle});
        }
        this.previousPositions.addAll(currentGhostPositions);
        if (this.previousPositions.size() > 21 * 3) {
            this.previousPositions.subList(0, this.previousPositions.size() - (21 * 3)).clear();
        }
        for (int i2 = 0; i2 < 3; i2++) {
            double[] currentPos = currentGhostPositions.get(i2);
            float angle2 = (float) currentPos[3];
            double renderX = currentPos[0] - mc.method_1561().field_4686.method_19326().method_10216();
            double renderY = currentPos[1] - mc.method_1561().field_4686.method_19326().method_10214();
            double renderZ = currentPos[2] - mc.method_1561().field_4686.method_19326().method_10215();
            renderGhost(matrixStack, renderX, renderY, renderZ, angle2, 0.25f, 1.0f);
            for (int t = 0; t < this.previousPositions.size() / 3; t++) {
                int index = (t * 3) + i2;
                if (index < this.previousPositions.size()) {
                    double[] trailPos = this.previousPositions.get(index);
                    double trailRenderX = trailPos[0] - mc.method_1561().field_4686.method_19326().method_10216();
                    double trailRenderY = trailPos[1] - mc.method_1561().field_4686.method_19326().method_10214();
                    double trailRenderZ = trailPos[2] - mc.method_1561().field_4686.method_19326().method_10215();
                    float trailAngle = (float) trailPos[3];
                    float trailAlpha = (t + 1) / ((this.previousPositions.size() / 3.0f) + 1.0f);
                    float trailSize = Math.max(0.25f * 0.4f, 0.25f * trailAlpha);
                    renderGhost(matrixStack, trailRenderX, trailRenderY, trailRenderZ, trailAngle, trailSize, trailAlpha);
                }
            }
        }
        RenderSystem.enableCull();
        RenderSystem.blendFunc(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderUtil.WORLD.endRender(matrixStack);
    }

    public void renderGhost(class_4587 stack, double x, double y, double z, float angle, float size, float alphaMultiplier) {
        stack.method_22903();
        stack.method_22904(x, y, z);
        class_4184 camera = mc.field_1773.method_19418();
        stack.method_22907(class_7833.field_40716.rotationDegrees((-camera.method_19330()) + 180.0f));
        stack.method_22907(class_7833.field_40714.rotationDegrees((-camera.method_19329()) + 180.0f));
        RenderSystem.setShaderTexture(0, FileUtil.getImage("particles/glow"));
        Matrix4f matrix = stack.method_23760().method_23761();
        RenderSystem.setShader(class_10142.field_53880);
        class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
        float[] c = ColorUtil.normalize(ColorUtil.setAlpha(UIColors.gradient((int) angle), (int) (255.0d * showAnimation.getValue())));
        float alpha = c[3] * alphaMultiplier;
        buffer.method_22918(matrix, -size, size, 0.0f).method_22913(0.0f, 1.0f).method_22915(c[0], c[1], c[2], alpha);
        buffer.method_22918(matrix, size, size, 0.0f).method_22913(1.0f, 1.0f).method_22915(c[0], c[1], c[2], alpha);
        buffer.method_22918(matrix, size, -size, 0.0f).method_22913(1.0f, 0.0f).method_22915(c[0], c[1], c[2], alpha);
        buffer.method_22918(matrix, -size, -size, 0.0f).method_22913(0.0f, 0.0f).method_22915(c[0], c[1], c[2], alpha);
        class_286.method_43433(buffer.method_60800());
        stack.method_22909();
    }
}

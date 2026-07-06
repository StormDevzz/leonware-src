// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render.targetesp.modes;

import org.joml.Matrix4f;
import net.minecraft.class_287;
import java.awt.Color;
import net.minecraft.class_4587;
import net.minecraft.class_286;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_289;
import net.minecraft.class_10142;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import sweetie.leonware.api.utils.color.UIColors;
import net.minecraft.class_3532;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.client.features.modules.render.targetesp.TargetEspMode;

public class TargetEspCircle extends TargetEspMode
{
    private float animationProgress;
    private long lastUpdateTime;
    private float smoothTrailHeight;
    private Object lastTarget;
    
    public TargetEspCircle() {
        this.animationProgress = 0.0f;
        this.lastUpdateTime = 0L;
        this.smoothTrailHeight = 0.0f;
        this.lastTarget = null;
    }
    
    @Override
    public void onUpdate() {
        if (TargetEspCircle.currentTarget != this.lastTarget) {
            this.animationProgress = 0.0f;
            this.smoothTrailHeight = 0.0f;
            this.lastTarget = TargetEspCircle.currentTarget;
        }
    }
    
    @Override
    public void onRender3D(final Render3DEvent.Render3DEventData event) {
        if (TargetEspCircle.currentTarget == null || !this.canDraw()) {
            return;
        }
        final float anim = (float)TargetEspCircle.showAnimation.getValue();
        if (anim <= 0.01f) {
            return;
        }
        final long now = System.currentTimeMillis();
        if (this.lastUpdateTime == 0L) {
            this.lastUpdateTime = now;
        }
        final long delta = now - this.lastUpdateTime;
        this.lastUpdateTime = now;
        this.animationProgress += delta * 0.0025f;
        final double x = getTargetX() - TargetEspCircle.mc.field_1773.method_19418().method_19326().field_1352;
        final double y = getTargetY() - TargetEspCircle.mc.field_1773.method_19418().method_19326().field_1351;
        final double z = getTargetZ() - TargetEspCircle.mc.field_1773.method_19418().method_19326().field_1350;
        final float height = TargetEspCircle.currentTarget.method_17682();
        final float sin = class_3532.method_15374(this.animationProgress);
        final float cos = class_3532.method_15362(this.animationProgress);
        final float floatingY = (sin + 1.0f) / 2.0f * height;
        float targetTrailHeight = -cos * 0.5f;
        if (floatingY + targetTrailHeight < 0.0f) {
            targetTrailHeight = -floatingY;
        }
        this.smoothTrailHeight = class_3532.method_16439(delta * 0.006f, this.smoothTrailHeight, targetTrailHeight);
        final class_4587 ms = event.matrixStack();
        final Color color = UIColors.gradient(0);
        final float r = color.getRed() / 255.0f;
        final float g = color.getGreen() / 255.0f;
        final float b = color.getBlue() / 255.0f;
        RenderSystem.enableBlend();
        RenderSystem.disableCull();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.blendFuncSeparate(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE, GlStateManager.class_4535.ZERO, GlStateManager.class_4534.ONE);
        RenderSystem.setShader(class_10142.field_53876);
        ms.method_22903();
        ms.method_22904(x, y + floatingY, z);
        final class_287 builder = class_289.method_1348().method_60827(class_293.class_5596.field_27380, class_290.field_1576);
        final Matrix4f matrix = ms.method_23760().method_23761();
        final float radius = (TargetEspCircle.currentTarget.method_17681() + 0.2f) * 0.75f;
        final int segments = 90;
        for (int i = 0; i <= segments; ++i) {
            final float angle = (float)(i * 3.141592653589793 * 2.0 / segments);
            final float cx = class_3532.method_15362(angle) * radius;
            final float cz = class_3532.method_15374(angle) * radius;
            builder.method_22918(matrix, cx, 0.0f, cz).method_22915(r, g, b, anim * 0.7f);
            builder.method_22918(matrix, cx, this.smoothTrailHeight, cz).method_22915(r, g, b, 0.0f);
        }
        class_286.method_43433(builder.method_60800());
        final class_287 lineBuilder = class_289.method_1348().method_60827(class_293.class_5596.field_29345, class_290.field_1576);
        for (int j = 0; j <= segments; ++j) {
            final float angle2 = (float)(j * 3.141592653589793 * 2.0 / segments);
            final float cx2 = class_3532.method_15362(angle2) * radius;
            final float cz2 = class_3532.method_15374(angle2) * radius;
            lineBuilder.method_22918(matrix, cx2, 0.0f, cz2).method_22915(r, g, b, anim);
        }
        class_286.method_43433(lineBuilder.method_60800());
        ms.method_22909();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        RenderSystem.blendFunc(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE_MINUS_SRC_ALPHA);
    }
}

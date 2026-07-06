package sweetie.leonware.client.features.modules.render.targetesp.modes;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.minecraft.class_10142;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import org.joml.Matrix4f;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.client.features.modules.render.targetesp.TargetEspMode;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/targetesp/modes/TargetEspCircle.class */
public class TargetEspCircle extends TargetEspMode {
    private float animationProgress = 0.0f;
    private long lastUpdateTime = 0;
    private float smoothTrailHeight = 0.0f;
    private Object lastTarget = null;

    @Override // sweetie.leonware.client.features.modules.render.targetesp.TargetEspMode
    public void onUpdate() {
        if (currentTarget != this.lastTarget) {
            this.animationProgress = 0.0f;
            this.smoothTrailHeight = 0.0f;
            this.lastTarget = currentTarget;
        }
    }

    @Override // sweetie.leonware.client.features.modules.render.targetesp.TargetEspMode
    public void onRender3D(Render3DEvent.Render3DEventData event) {
        if (currentTarget == null || !canDraw()) {
            return;
        }
        float anim = (float) showAnimation.getValue();
        if (anim <= 0.01f) {
            return;
        }
        long now = System.currentTimeMillis();
        if (this.lastUpdateTime == 0) {
            this.lastUpdateTime = now;
        }
        long delta = now - this.lastUpdateTime;
        this.lastUpdateTime = now;
        this.animationProgress += delta * 0.0025f;
        double x = getTargetX() - mc.field_1773.method_19418().method_19326().field_1352;
        double y = getTargetY() - mc.field_1773.method_19418().method_19326().field_1351;
        double z = getTargetZ() - mc.field_1773.method_19418().method_19326().field_1350;
        float height = currentTarget.method_17682();
        float sin = class_3532.method_15374(this.animationProgress);
        float cos = class_3532.method_15362(this.animationProgress);
        float floatingY = ((sin + 1.0f) / 2.0f) * height;
        float targetTrailHeight = (-cos) * 0.5f;
        if (floatingY + targetTrailHeight < 0.0f) {
            targetTrailHeight = -floatingY;
        }
        this.smoothTrailHeight = class_3532.method_16439(delta * 0.006f, this.smoothTrailHeight, targetTrailHeight);
        class_4587 ms = event.matrixStack();
        Color color = UIColors.gradient(0);
        float r = color.getRed() / 255.0f;
        float g = color.getGreen() / 255.0f;
        float b = color.getBlue() / 255.0f;
        RenderSystem.enableBlend();
        RenderSystem.disableCull();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.blendFuncSeparate(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE, GlStateManager.class_4535.ZERO, GlStateManager.class_4534.ONE);
        RenderSystem.setShader(class_10142.field_53876);
        ms.method_22903();
        ms.method_22904(x, y + ((double) floatingY), z);
        class_287 builder = class_289.method_1348().method_60827(class_293.class_5596.field_27380, class_290.field_1576);
        Matrix4f matrix = ms.method_23760().method_23761();
        float radius = (currentTarget.method_17681() + 0.2f) * 0.75f;
        for (int i = 0; i <= 90; i++) {
            float angle = (float) (((((double) i) * 3.141592653589793d) * 2.0d) / ((double) 90));
            float cx = class_3532.method_15362(angle) * radius;
            float cz = class_3532.method_15374(angle) * radius;
            builder.method_22918(matrix, cx, 0.0f, cz).method_22915(r, g, b, anim * 0.7f);
            builder.method_22918(matrix, cx, this.smoothTrailHeight, cz).method_22915(r, g, b, 0.0f);
        }
        class_286.method_43433(builder.method_60800());
        class_287 lineBuilder = class_289.method_1348().method_60827(class_293.class_5596.field_29345, class_290.field_1576);
        for (int i2 = 0; i2 <= 90; i2++) {
            float angle2 = (float) (((((double) i2) * 3.141592653589793d) * 2.0d) / ((double) 90));
            lineBuilder.method_22918(matrix, class_3532.method_15362(angle2) * radius, 0.0f, class_3532.method_15374(angle2) * radius).method_22915(r, g, b, anim);
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

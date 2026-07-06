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
import sweetie.leonware.api.system.files.FileUtil;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.client.features.modules.render.targetesp.TargetEspMode;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/targetesp/modes/TargetEspGhosts.class */
public class TargetEspGhosts extends TargetEspMode {
    private float animNurik = 0.0f;
    private long lastRenderTime = 0;

    @Override // sweetie.leonware.client.features.modules.render.targetesp.TargetEspMode
    public void onUpdate() {
    }

    @Override // sweetie.leonware.client.features.modules.render.targetesp.TargetEspMode
    public void onRender3D(Render3DEvent.Render3DEventData event) {
        if (currentTarget == null || !canDraw()) {
            return;
        }
        float anim = (float) showAnimation.getValue();
        long now = System.currentTimeMillis();
        if (this.lastRenderTime == 0) {
            this.lastRenderTime = now;
        }
        long delta = now - this.lastRenderTime;
        this.lastRenderTime = now;
        this.animNurik += (5 * delta) / 600.0f;
        double x = getTargetX() - mc.field_1773.method_19418().method_19326().field_1352;
        double y = getTargetY() - mc.field_1773.method_19418().method_19326().field_1351;
        double z = getTargetZ() - mc.field_1773.method_19418().method_19326().field_1350;
        class_4587 ms = event.matrixStack();
        RenderSystem.setShader(class_10142.field_53880);
        RenderSystem.setShaderTexture(0, FileUtil.getImage("particles/glow"));
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE, GlStateManager.class_4535.ZERO, GlStateManager.class_4534.ONE);
        RenderSystem.disableCull();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        int n4 = 3 * 3;
        ms.method_22903();
        class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < n4) {
                for (int j = 0; j < 12; j++) {
                    Color color = UIColors.gradient(j * 20);
                    int alpha = (int) (anim * 255.0f);
                    int rgb = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha).getRGB();
                    float f2 = this.animNurik + (j * 0.1f);
                    int n5 = (int) Math.pow(i2, 2.0d);
                    ms.method_22903();
                    ms.method_22904(x + ((double) (0.8f * class_3532.method_15374(f2 + n5))), y + ((double) 0.5f) + ((double) (0.3f * class_3532.method_15374(this.animNurik + (j * 0.2f)))) + ((double) (0.2f * i2)), z + ((double) (0.8f * class_3532.method_15362(f2 - n5))));
                    ms.method_22905(anim * (0.005f + (j / 2000.0f)), anim * (0.005f + (j / 2000.0f)), anim * (0.005f + (j / 2000.0f)));
                    ms.method_22907(mc.field_1773.method_19418().method_23767());
                    Matrix4f matrix = ms.method_23760().method_23761();
                    buffer.method_22918(matrix, -25, (-25) + 50, 0.0f).method_22913(0.0f, 1.0f).method_39415(rgb);
                    buffer.method_22918(matrix, (-25) + 50, (-25) + 50, 0.0f).method_22913(1.0f, 1.0f).method_39415(rgb);
                    buffer.method_22918(matrix, (-25) + 50, -25, 0.0f).method_22913(1.0f, 0.0f).method_39415(rgb);
                    buffer.method_22918(matrix, -25, -25, 0.0f).method_22913(0.0f, 0.0f).method_39415(rgb);
                    ms.method_22909();
                }
                i = i2 + 3;
            } else {
                class_286.method_43433(buffer.method_60800());
                ms.method_22909();
                RenderSystem.enableDepthTest();
                RenderSystem.depthMask(true);
                RenderSystem.disableBlend();
                RenderSystem.blendFunc(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE_MINUS_SRC_ALPHA);
                RenderSystem.enableCull();
                return;
            }
        }
    }
}

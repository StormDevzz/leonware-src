// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render.targetesp.modes;

import org.joml.Matrix4f;
import net.minecraft.class_287;
import net.minecraft.class_4587;
import net.minecraft.class_286;
import net.minecraft.class_3532;
import java.awt.Color;
import sweetie.leonware.api.utils.color.UIColors;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_289;
import com.mojang.blaze3d.platform.GlStateManager;
import sweetie.leonware.api.system.files.FileUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_10142;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.client.features.modules.render.targetesp.TargetEspMode;

public class TargetEspGhosts extends TargetEspMode
{
    private float animNurik;
    private long lastRenderTime;
    
    public TargetEspGhosts() {
        this.animNurik = 0.0f;
        this.lastRenderTime = 0L;
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onRender3D(final Render3DEvent.Render3DEventData event) {
        if (TargetEspGhosts.currentTarget == null || !this.canDraw()) {
            return;
        }
        final float anim = (float)TargetEspGhosts.showAnimation.getValue();
        final long now = System.currentTimeMillis();
        if (this.lastRenderTime == 0L) {
            this.lastRenderTime = now;
        }
        final long delta = now - this.lastRenderTime;
        this.lastRenderTime = now;
        this.animNurik += 5L * delta / 600.0f;
        final double x = getTargetX() - TargetEspGhosts.mc.field_1773.method_19418().method_19326().field_1352;
        final double y = getTargetY() - TargetEspGhosts.mc.field_1773.method_19418().method_19326().field_1351;
        final double z = getTargetZ() - TargetEspGhosts.mc.field_1773.method_19418().method_19326().field_1350;
        final class_4587 ms = event.matrixStack();
        RenderSystem.setShader(class_10142.field_53880);
        RenderSystem.setShaderTexture(0, FileUtil.getImage("particles/glow"));
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE, GlStateManager.class_4535.ZERO, GlStateManager.class_4534.ONE);
        RenderSystem.disableCull();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        final int n2 = 3;
        final int n3 = 12;
        final int n4 = 3 * n2;
        ms.method_22903();
        final class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
        for (int i = 0; i < n4; i += n2) {
            for (int j = 0; j < n3; ++j) {
                final Color color = UIColors.gradient(j * 20);
                final int alpha = (int)(anim * 255.0f);
                final int rgb = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha).getRGB();
                final float f2 = this.animNurik + j * 0.1f;
                final float f3 = 0.8f;
                final float f4 = 0.5f;
                final int n5 = (int)Math.pow(i, 2.0);
                ms.method_22903();
                ms.method_22904(x + f3 * class_3532.method_15374(f2 + n5), y + f4 + 0.3f * class_3532.method_15374(this.animNurik + j * 0.2f) + 0.2f * i, z + f3 * class_3532.method_15362(f2 - n5));
                ms.method_22905(anim * (0.005f + j / 2000.0f), anim * (0.005f + j / 2000.0f), anim * (0.005f + j / 2000.0f));
                ms.method_22907(TargetEspGhosts.mc.field_1773.method_19418().method_23767());
                final Matrix4f matrix = ms.method_23760().method_23761();
                final int n6 = -25;
                final int n7 = 50;
                buffer.method_22918(matrix, (float)n6, (float)(n6 + n7), 0.0f).method_22913(0.0f, 1.0f).method_39415(rgb);
                buffer.method_22918(matrix, (float)(n6 + n7), (float)(n6 + n7), 0.0f).method_22913(1.0f, 1.0f).method_39415(rgb);
                buffer.method_22918(matrix, (float)(n6 + n7), (float)n6, 0.0f).method_22913(1.0f, 0.0f).method_39415(rgb);
                buffer.method_22918(matrix, (float)n6, (float)n6, 0.0f).method_22913(0.0f, 0.0f).method_39415(rgb);
                ms.method_22909();
            }
        }
        class_286.method_43433(buffer.method_60800());
        ms.method_22909();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
        RenderSystem.blendFunc(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE_MINUS_SRC_ALPHA);
        RenderSystem.enableCull();
    }
}

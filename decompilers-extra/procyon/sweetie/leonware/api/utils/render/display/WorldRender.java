// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.render.display;

import net.minecraft.class_287;
import net.minecraft.class_286;
import net.minecraft.class_310;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_289;
import java.awt.Color;
import net.minecraft.class_10142;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.class_4184;
import org.joml.Vector4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import net.minecraft.class_243;
import org.joml.Matrix4fc;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_4587;
import org.joml.Matrix4f;
import sweetie.leonware.api.system.interfaces.QuickImports;

public class WorldRender implements QuickImports
{
    public static final Matrix4f lastProjMat;
    public static final Matrix4f lastModMat;
    public static final Matrix4f lastWorldSpaceMatrix;
    
    public static void setTranslation(final class_4587 matrixStack) {
        WorldRender.lastProjMat.set((Matrix4fc)RenderSystem.getProjectionMatrix());
        WorldRender.lastModMat.set((Matrix4fc)RenderSystem.getModelViewMatrix());
        WorldRender.lastWorldSpaceMatrix.set((Matrix4fc)matrixStack.method_23760().method_23761());
    }
    
    public static class_243 worldSpaceToScreenSpace(final class_243 pos) {
        final class_4184 camera = WorldRender.mc.method_1561().field_4686;
        final int displayHeight = WorldRender.mc.method_22683().method_4507();
        final int[] viewport = new int[4];
        GL11.glGetIntegerv(2978, viewport);
        final Vector3f target = new Vector3f();
        final double deltaX = pos.field_1352 - camera.method_19326().field_1352;
        final double deltaY = pos.field_1351 - camera.method_19326().field_1351;
        final double deltaZ = pos.field_1350 - camera.method_19326().field_1350;
        final Vector4f transformed = new Vector4f((float)deltaX, (float)deltaY, (float)deltaZ, 1.0f).mul((Matrix4fc)WorldRender.lastWorldSpaceMatrix);
        final Matrix4f matrixProj = new Matrix4f((Matrix4fc)WorldRender.lastProjMat);
        final Matrix4f matrixModel = new Matrix4f((Matrix4fc)WorldRender.lastModMat);
        matrixProj.mul((Matrix4fc)matrixModel).project(transformed.x(), transformed.y(), transformed.z(), viewport, target);
        return new class_243(target.x / WorldRender.mc.method_22683().method_4495(), (displayHeight - target.y) / WorldRender.mc.method_22683().method_4495(), (double)target.z);
    }
    
    public static class_243 interpolatePos(final float prevX, final float prevY, final float prevZ, final float x, final float y, final float z) {
        final class_243 camPos = WorldRender.mc.method_1561().field_4686.method_19326();
        final float delta = WorldRender.mc.method_61966().method_60637(true);
        return new class_243(prevX + (x - prevX) * delta - camPos.field_1352, prevY + (y - prevY) * delta - camPos.field_1351, prevZ + (z - prevZ) * delta - camPos.field_1350);
    }
    
    public void startRender(final class_4587 matrixStack) {
        matrixStack.method_22903();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE);
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(class_10142.field_53880);
    }
    
    public void endRender(final class_4587 matrixStack) {
        RenderSystem.depthMask(true);
        RenderSystem.disableDepthTest();
        RenderSystem.disableBlend();
        matrixStack.method_22909();
    }
    
    public void drawLine(final class_4587 ms, final class_243 from, final class_243 to, final Color color) {
        final class_289 tessellator = class_289.method_1348();
        final class_287 buffer = tessellator.method_60827(class_293.class_5596.field_27377, class_290.field_29337);
        final class_4587.class_4665 entry = ms.method_23760();
        final Matrix4f model = entry.method_23761();
        final class_243 cam = class_310.method_1551().field_1773.method_19418().method_19326();
        final float x1 = (float)(from.field_1352 - cam.field_1352);
        final float y1 = (float)(from.field_1351 - cam.field_1351);
        final float z1 = (float)(from.field_1350 - cam.field_1350);
        final float x2 = (float)(to.field_1352 - cam.field_1352);
        final float y2 = (float)(to.field_1351 - cam.field_1351);
        final float z2 = (float)(to.field_1350 - cam.field_1350);
        buffer.method_22918(model, x1, y1, z1).method_1336(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        buffer.method_22918(model, x2, y2, z2).method_1336(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        class_286.method_43433(buffer.method_60800());
    }
    
    static {
        lastProjMat = new Matrix4f();
        lastModMat = new Matrix4f();
        lastWorldSpaceMatrix = new Matrix4f();
    }
}

package sweetie.leonware.api.utils.render.display;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.minecraft.class_10142;
import net.minecraft.class_243;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_310;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import sweetie.leonware.api.system.interfaces.QuickImports;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/render/display/WorldRender.class */
public class WorldRender implements QuickImports {
    public static final Matrix4f lastProjMat = new Matrix4f();
    public static final Matrix4f lastModMat = new Matrix4f();
    public static final Matrix4f lastWorldSpaceMatrix = new Matrix4f();

    public static void setTranslation(class_4587 matrixStack) {
        lastProjMat.set(RenderSystem.getProjectionMatrix());
        lastModMat.set(RenderSystem.getModelViewMatrix());
        lastWorldSpaceMatrix.set(matrixStack.method_23760().method_23761());
    }

    public static class_243 worldSpaceToScreenSpace(class_243 pos) {
        class_4184 camera = mc.method_1561().field_4686;
        int displayHeight = mc.method_22683().method_4507();
        int[] viewport = new int[4];
        GL11.glGetIntegerv(2978, viewport);
        Vector3f target = new Vector3f();
        double deltaX = pos.field_1352 - camera.method_19326().field_1352;
        double deltaY = pos.field_1351 - camera.method_19326().field_1351;
        double deltaZ = pos.field_1350 - camera.method_19326().field_1350;
        Vector4f transformed = new Vector4f((float) deltaX, (float) deltaY, (float) deltaZ, 1.0f).mul(lastWorldSpaceMatrix);
        Matrix4f matrixProj = new Matrix4f(lastProjMat);
        Matrix4f matrixModel = new Matrix4f(lastModMat);
        matrixProj.mul(matrixModel).project(transformed.x(), transformed.y(), transformed.z(), viewport, target);
        return new class_243(((double) target.x) / mc.method_22683().method_4495(), ((double) (displayHeight - target.y)) / mc.method_22683().method_4495(), target.z);
    }

    public static class_243 interpolatePos(float prevX, float prevY, float prevZ, float x, float y, float z) {
        class_243 camPos = mc.method_1561().field_4686.method_19326();
        float delta = mc.method_61966().method_60637(true);
        return new class_243(((double) (prevX + ((x - prevX) * delta))) - camPos.field_1352, ((double) (prevY + ((y - prevY) * delta))) - camPos.field_1351, ((double) (prevZ + ((z - prevZ) * delta))) - camPos.field_1350);
    }

    public void startRender(class_4587 matrixStack) {
        matrixStack.method_22903();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE);
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(class_10142.field_53880);
    }

    public void endRender(class_4587 matrixStack) {
        RenderSystem.depthMask(true);
        RenderSystem.disableDepthTest();
        RenderSystem.disableBlend();
        matrixStack.method_22909();
    }

    public void drawLine(class_4587 ms, class_243 from, class_243 to, Color color) {
        class_289 tessellator = class_289.method_1348();
        class_287 buffer = tessellator.method_60827(class_293.class_5596.field_27377, class_290.field_29337);
        class_4587.class_4665 entry = ms.method_23760();
        Matrix4f model = entry.method_23761();
        class_243 cam = class_310.method_1551().field_1773.method_19418().method_19326();
        float x1 = (float) (from.field_1352 - cam.field_1352);
        float y1 = (float) (from.field_1351 - cam.field_1351);
        float z1 = (float) (from.field_1350 - cam.field_1350);
        float x2 = (float) (to.field_1352 - cam.field_1352);
        float y2 = (float) (to.field_1351 - cam.field_1351);
        float z2 = (float) (to.field_1350 - cam.field_1350);
        buffer.method_22918(model, x1, y1, z1).method_1336(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        buffer.method_22918(model, x2, y2, z2).method_1336(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        class_286.method_43433(buffer.method_60800());
    }
}

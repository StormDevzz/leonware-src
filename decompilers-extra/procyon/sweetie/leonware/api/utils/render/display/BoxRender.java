// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.render.display;

import org.joml.Vector3f;
import org.joml.Matrix4f;
import net.minecraft.class_3532;
import net.minecraft.class_4588;
import java.awt.Color;
import net.minecraft.class_287;
import java.util.Iterator;
import net.minecraft.class_286;
import net.minecraft.class_10142;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_289;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_4184;
import net.minecraft.class_4604;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_4587;
import java.util.ArrayList;
import java.util.List;
import sweetie.leonware.api.system.interfaces.QuickImports;

public class BoxRender implements QuickImports
{
    private final List<OutlinedBox> outlinedBoxes;
    private final List<FilledBox> filledBoxes;
    private final List<StripedBox> stripedBoxes;
    
    public BoxRender() {
        this.outlinedBoxes = new ArrayList<OutlinedBox>();
        this.filledBoxes = new ArrayList<FilledBox>();
        this.stripedBoxes = new ArrayList<StripedBox>();
    }
    
    public void setup3DRender(final class_4587 matrixStack) {
        final class_4184 camera = BoxRender.mc.field_1773.method_19418();
        final class_243 cameraPos = camera.method_19326();
        final class_4604 frustum = new class_4604(matrixStack.method_23760().method_23761(), RenderSystem.getProjectionMatrix());
        frustum.method_23088(cameraPos.field_1352, cameraPos.field_1351, cameraPos.field_1350);
        this.renderFilledBoxes(this.filledBoxes, frustum, matrixStack);
        this.renderStripedBoxes(this.stripedBoxes, frustum, matrixStack);
        this.renderOutlinedBoxes(this.outlinedBoxes, frustum, matrixStack);
        this.filledBoxes.clear();
        this.stripedBoxes.clear();
        this.outlinedBoxes.clear();
    }
    
    private void renderFilledBoxes(final List<FilledBox> boxes, final class_4604 frustum, final class_4587 matrixStack) {
        if (boxes.isEmpty()) {
            return;
        }
        for (final FilledBox action : boxes) {
            final class_238 box = new class_238(action.pos, action.pos.method_1019(action.params));
            if (!frustum.method_23093(box)) {
                continue;
            }
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            final class_289 tessellator = class_289.method_1348();
            final class_287 buffer = tessellator.method_60827(class_293.class_5596.field_27382, class_290.field_1576);
            RenderSystem.disableCull();
            RenderSystem.disableDepthTest();
            RenderSystem.setShader(class_10142.field_53876);
            this.renderFilledBox(action.pos, action.params, matrixStack, buffer, action.color);
            class_286.method_43433(buffer.method_60800());
            RenderSystem.enableCull();
            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();
        }
    }
    
    private void renderOutlinedBoxes(final List<OutlinedBox> boxes, final class_4604 frustum, final class_4587 matrixStack) {
        if (boxes.isEmpty()) {
            return;
        }
        for (final OutlinedBox action : boxes) {
            final class_238 box = new class_238(action.pos, action.pos.method_1019(action.params));
            if (!frustum.method_23093(box)) {
                continue;
            }
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            final class_289 tessellator = class_289.method_1348();
            final class_287 buffer = tessellator.method_60827(class_293.class_5596.field_27377, class_290.field_29337);
            RenderSystem.disableCull();
            RenderSystem.disableDepthTest();
            RenderSystem.setShader(class_10142.field_53864);
            RenderSystem.lineWidth(action.lineWidth);
            this.renderOutlinedBox(action.pos, action.params, matrixStack, buffer, action.color);
            class_286.method_43433(buffer.method_60800());
            RenderSystem.enableCull();
            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();
        }
    }
    
    private void renderStripedBoxes(final List<StripedBox> boxes, final class_4604 frustum, final class_4587 matrixStack) {
        if (boxes.isEmpty()) {
            return;
        }
        for (final StripedBox action : boxes) {
            final class_238 box = new class_238(action.pos, action.pos.method_1019(action.params));
            if (!frustum.method_23093(box)) {
                continue;
            }
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            final class_289 tessellator = class_289.method_1348();
            final class_287 buffer = tessellator.method_60827(class_293.class_5596.field_27377, class_290.field_29337);
            RenderSystem.disableCull();
            RenderSystem.disableDepthTest();
            RenderSystem.setShader(class_10142.field_53864);
            RenderSystem.lineWidth(action.lineWidth);
            this.renderDashedOutlinedBox(action.pos, action.params, matrixStack, buffer, action.color, action.gapDistance);
            class_286.method_43433(buffer.method_60800());
            RenderSystem.enableCull();
            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();
        }
    }
    
    private void renderFilledBox(final class_243 pos, final class_243 params, final class_4587 matrices, final class_287 buffer, final Color color) {
        final class_243 camPos = BoxRender.mc.field_1773.method_19418().method_19326();
        final float x1 = (float)(pos.field_1352 - camPos.field_1352);
        final float y1 = (float)(pos.field_1351 - camPos.field_1351);
        final float z1 = (float)(pos.field_1350 - camPos.field_1350);
        final float x2 = x1 + (float)params.field_1352;
        final float y2 = y1 + (float)params.field_1351;
        final float z2 = z1 + (float)params.field_1350;
        this.vertexQuad(matrices, buffer, x1, y1, z1, x2, y1, z1, x2, y2, z1, x1, y2, z1, color);
        this.vertexQuad(matrices, buffer, x1, y1, z2, x2, y1, z2, x2, y2, z2, x1, y2, z2, color);
        this.vertexQuad(matrices, buffer, x1, y1, z1, x1, y1, z2, x1, y2, z2, x1, y2, z1, color);
        this.vertexQuad(matrices, buffer, x2, y1, z1, x2, y1, z2, x2, y2, z2, x2, y2, z1, color);
        this.vertexQuad(matrices, buffer, x1, y1, z1, x1, y2, z1, x2, y2, z1, x2, y1, z1, color);
        this.vertexQuad(matrices, buffer, x1, y1, z2, x1, y2, z2, x2, y2, z2, x2, y1, z2, color);
    }
    
    private void renderOutlinedBox(final class_243 pos, final class_243 params, final class_4587 matrices, final class_287 buffer, final Color color) {
        final class_243 camPos = BoxRender.mc.field_1773.method_19418().method_19326();
        final float x1 = (float)(pos.field_1352 - camPos.field_1352);
        final float y1 = (float)(pos.field_1351 - camPos.field_1351);
        final float z1 = (float)(pos.field_1350 - camPos.field_1350);
        final float x2 = x1 + (float)params.field_1352;
        final float y2 = y1 + (float)params.field_1351;
        final float z2 = z1 + (float)params.field_1350;
        this.vertexLine(matrices, (class_4588)buffer, x1, y1, z1, x2, y1, z1, color);
        this.vertexLine(matrices, (class_4588)buffer, x2, y1, z1, x2, y1, z2, color);
        this.vertexLine(matrices, (class_4588)buffer, x2, y1, z2, x1, y1, z2, color);
        this.vertexLine(matrices, (class_4588)buffer, x1, y1, z2, x1, y1, z1, color);
        this.vertexLine(matrices, (class_4588)buffer, x1, y2, z1, x2, y2, z1, color);
        this.vertexLine(matrices, (class_4588)buffer, x2, y2, z1, x2, y2, z2, color);
        this.vertexLine(matrices, (class_4588)buffer, x2, y2, z2, x1, y2, z2, color);
        this.vertexLine(matrices, (class_4588)buffer, x1, y2, z2, x1, y2, z1, color);
        this.vertexLine(matrices, (class_4588)buffer, x1, y1, z1, x1, y2, z1, color);
        this.vertexLine(matrices, (class_4588)buffer, x2, y1, z1, x2, y2, z1, color);
        this.vertexLine(matrices, (class_4588)buffer, x1, y1, z2, x1, y2, z2, color);
        this.vertexLine(matrices, (class_4588)buffer, x2, y1, z2, x2, y2, z2, color);
    }
    
    private void renderDashedOutlinedBox(final class_243 pos, final class_243 params, final class_4587 matrices, final class_287 buffer, final Color color, final float gapDistance) {
        final class_243 camPos = BoxRender.mc.field_1773.method_19418().method_19326();
        final float x1 = (float)(pos.field_1352 - camPos.field_1352);
        final float y1 = (float)(pos.field_1351 - camPos.field_1351);
        final float z1 = (float)(pos.field_1350 - camPos.field_1350);
        final float x2 = x1 + (float)params.field_1352;
        final float y2 = y1 + (float)params.field_1351;
        final float z2 = z1 + (float)params.field_1350;
        this.renderDashedLine(matrices, buffer, color, x1, y1, z1, x2, y1, z1, gapDistance, gapDistance);
        this.renderDashedLine(matrices, buffer, color, x2, y1, z1, x2, y1, z2, gapDistance, gapDistance);
        this.renderDashedLine(matrices, buffer, color, x2, y1, z2, x1, y1, z2, gapDistance, gapDistance);
        this.renderDashedLine(matrices, buffer, color, x1, y1, z2, x1, y1, z1, gapDistance, gapDistance);
        this.renderDashedLine(matrices, buffer, color, x1, y2, z1, x2, y2, z1, gapDistance, gapDistance);
        this.renderDashedLine(matrices, buffer, color, x2, y2, z1, x2, y2, z2, gapDistance, gapDistance);
        this.renderDashedLine(matrices, buffer, color, x2, y2, z2, x1, y2, z2, gapDistance, gapDistance);
        this.renderDashedLine(matrices, buffer, color, x1, y2, z2, x1, y2, z1, gapDistance, gapDistance);
        this.renderDashedLine(matrices, buffer, color, x1, y1, z1, x1, y2, z1, gapDistance, gapDistance);
        this.renderDashedLine(matrices, buffer, color, x2, y1, z1, x2, y2, z1, gapDistance, gapDistance);
        this.renderDashedLine(matrices, buffer, color, x1, y1, z2, x1, y2, z2, gapDistance, gapDistance);
        this.renderDashedLine(matrices, buffer, color, x2, y1, z2, x2, y2, z2, gapDistance, gapDistance);
    }
    
    private void renderDashedLine(final class_4587 matrices, final class_287 buffer, final Color color, final float x1, final float y1, final float z1, final float x2, final float y2, final float z2, final float dashLength, final float gapDistance) {
        final float dx = x2 - x1;
        final float dy = y2 - y1;
        final float dz = z2 - z1;
        final float totalLength = class_3532.method_15355(dx * dx + dy * dy + dz * dz);
        if (class_3532.method_15347(totalLength, 0.0f)) {
            return;
        }
        float tDashEnd;
        for (float t = 0.0f; t < 1.0f; t = tDashEnd, t = Math.min(t + gapDistance / totalLength, 1.0f)) {
            final float tDashStart = t;
            tDashEnd = Math.min(t + dashLength / totalLength, 1.0f);
            if (tDashEnd > tDashStart) {
                final float dashX1 = x1 + dx * tDashStart;
                final float dashY1 = y1 + dy * tDashStart;
                final float dashZ1 = z1 + dz * tDashStart;
                final float dashX2 = x1 + dx * tDashEnd;
                final float dashY2 = y1 + dy * tDashEnd;
                final float dashZ2 = z1 + dz * tDashEnd;
                this.vertexLine(matrices, (class_4588)buffer, dashX1, dashY1, dashZ1, dashX2, dashY2, dashZ2, color);
            }
        }
    }
    
    private void vertexLine(final class_4587 matrices, final class_4588 buffer, final float x1, final float y1, final float z1, final float x2, final float y2, final float z2, final Color lineColor) {
        final class_4587.class_4665 entry = matrices.method_23760();
        final Matrix4f model = entry.method_23761();
        final Vector3f normalVec = this.getNormalVec(x1, y1, z1, x2, y2, z2);
        buffer.method_22918(model, x1, y1, z1).method_1336(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), lineColor.getAlpha()).method_60831(entry, normalVec.x, normalVec.y, normalVec.z);
        buffer.method_22918(model, x2, y2, z2).method_1336(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), lineColor.getAlpha()).method_60831(entry, normalVec.x, normalVec.y, normalVec.z);
    }
    
    private Vector3f getNormalVec(final float x1, final float y1, final float z1, final float x2, final float y2, final float z2) {
        final float xNormal = x2 - x1;
        final float yNormal = y2 - y1;
        final float zNormal = z2 - z1;
        final float normalSqrt = class_3532.method_15355(xNormal * xNormal + yNormal * yNormal + zNormal * zNormal);
        return new Vector3f(xNormal / normalSqrt, yNormal / normalSqrt, zNormal / normalSqrt);
    }
    
    private void vertexQuad(final class_4587 matrices, final class_287 buffer, final float x1, final float y1, final float z1, final float x2, final float y2, final float z2, final float x3, final float y3, final float z3, final float x4, final float y4, final float z4, final Color color) {
        final class_4587.class_4665 entry = matrices.method_23760();
        final Matrix4f model = entry.method_23761();
        final Vector3f normal = new Vector3f(0.0f, 0.0f, 1.0f);
        buffer.method_22918(model, x1, y1, z1).method_1336(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).method_60831(entry, normal.x, normal.y, normal.z);
        buffer.method_22918(model, x2, y2, z2).method_1336(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).method_60831(entry, normal.x, normal.y, normal.z);
        buffer.method_22918(model, x3, y3, z3).method_1336(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).method_60831(entry, normal.x, normal.y, normal.z);
        buffer.method_22918(model, x4, y4, z4).method_1336(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).method_60831(entry, normal.x, normal.y, normal.z);
    }
    
    public void drawBox(final float x1, final float y1, final float z1, final float x2, final float y2, final float z2, final float lineWidth, final Color color, final Render renderMode, final float gapDistance) {
        final class_243 pos = new class_243((double)x1, (double)y1, (double)z1);
        final class_243 params = new class_243((double)(x2 - x1), (double)(y2 - y1), (double)(z2 - z1));
        switch (renderMode.ordinal()) {
            case 0: {
                this.filledBoxes.add(new FilledBox(pos, params, color));
                break;
            }
            case 1: {
                this.outlinedBoxes.add(new OutlinedBox(pos, params, lineWidth, color));
                break;
            }
            case 2: {
                this.stripedBoxes.add(new StripedBox(pos, params, lineWidth, color, gapDistance));
                break;
            }
        }
    }
    
    public enum Render
    {
        FILL, 
        OUTLINE, 
        STRIPED;
    }
    
    record FilledBox(class_243 pos, class_243 params, Color color) {}
    
    record OutlinedBox(class_243 pos, class_243 params, float lineWidth, Color color) {}
    
    record StripedBox(class_243 pos, class_243 params, float lineWidth, Color color, float gapDistance) {}
}

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.class_10142
 *  net.minecraft.class_10156
 *  net.minecraft.class_238
 *  net.minecraft.class_243
 *  net.minecraft.class_286
 *  net.minecraft.class_287
 *  net.minecraft.class_289
 *  net.minecraft.class_290
 *  net.minecraft.class_293$class_5596
 *  net.minecraft.class_3532
 *  net.minecraft.class_4184
 *  net.minecraft.class_4587
 *  net.minecraft.class_4587$class_4665
 *  net.minecraft.class_4588
 *  net.minecraft.class_4604
 *  net.minecraft.class_9801
 *  org.joml.Matrix4f
 *  org.joml.Vector3f
 */
package sweetie.leonware.api.utils.render.display;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_10142;
import net.minecraft.class_10156;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_3532;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_4604;
import net.minecraft.class_9801;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import sweetie.leonware.api.system.interfaces.QuickImports;

public class BoxRender
implements QuickImports {
    private final List<OutlinedBox> outlinedBoxes = new ArrayList<OutlinedBox>();
    private final List<FilledBox> filledBoxes = new ArrayList<FilledBox>();
    private final List<StripedBox> stripedBoxes = new ArrayList<StripedBox>();

    public void setup3DRender(class_4587 matrixStack) {
        class_4184 camera = BoxRender.mc.field_1773.method_19418();
        class_243 cameraPos = camera.method_19326();
        class_4604 frustum = new class_4604(matrixStack.method_23760().method_23761(), RenderSystem.getProjectionMatrix());
        frustum.method_23088(cameraPos.field_1352, cameraPos.field_1351, cameraPos.field_1350);
        this.renderFilledBoxes(this.filledBoxes, frustum, matrixStack);
        this.renderStripedBoxes(this.stripedBoxes, frustum, matrixStack);
        this.renderOutlinedBoxes(this.outlinedBoxes, frustum, matrixStack);
        this.filledBoxes.clear();
        this.stripedBoxes.clear();
        this.outlinedBoxes.clear();
    }

    private void renderFilledBoxes(List<FilledBox> boxes, class_4604 frustum, class_4587 matrixStack) {
        if (boxes.isEmpty()) {
            return;
        }
        for (FilledBox action : boxes) {
            class_238 box = new class_238(action.pos, action.pos.method_1019(action.params));
            if (!frustum.method_23093(box)) continue;
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            class_289 tessellator = class_289.method_1348();
            class_287 buffer = tessellator.method_60827(class_293.class_5596.field_27382, class_290.field_1576);
            RenderSystem.disableCull();
            RenderSystem.disableDepthTest();
            RenderSystem.setShader((class_10156)class_10142.field_53876);
            this.renderFilledBox(action.pos, action.params, matrixStack, buffer, action.color);
            class_286.method_43433((class_9801)buffer.method_60800());
            RenderSystem.enableCull();
            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();
        }
    }

    private void renderOutlinedBoxes(List<OutlinedBox> boxes, class_4604 frustum, class_4587 matrixStack) {
        if (boxes.isEmpty()) {
            return;
        }
        for (OutlinedBox action : boxes) {
            class_238 box = new class_238(action.pos, action.pos.method_1019(action.params));
            if (!frustum.method_23093(box)) continue;
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            class_289 tessellator = class_289.method_1348();
            class_287 buffer = tessellator.method_60827(class_293.class_5596.field_27377, class_290.field_29337);
            RenderSystem.disableCull();
            RenderSystem.disableDepthTest();
            RenderSystem.setShader((class_10156)class_10142.field_53864);
            RenderSystem.lineWidth((float)action.lineWidth);
            this.renderOutlinedBox(action.pos, action.params, matrixStack, buffer, action.color);
            class_286.method_43433((class_9801)buffer.method_60800());
            RenderSystem.enableCull();
            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();
        }
    }

    private void renderStripedBoxes(List<StripedBox> boxes, class_4604 frustum, class_4587 matrixStack) {
        if (boxes.isEmpty()) {
            return;
        }
        for (StripedBox action : boxes) {
            class_238 box = new class_238(action.pos, action.pos.method_1019(action.params));
            if (!frustum.method_23093(box)) continue;
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            class_289 tessellator = class_289.method_1348();
            class_287 buffer = tessellator.method_60827(class_293.class_5596.field_27377, class_290.field_29337);
            RenderSystem.disableCull();
            RenderSystem.disableDepthTest();
            RenderSystem.setShader((class_10156)class_10142.field_53864);
            RenderSystem.lineWidth((float)action.lineWidth);
            this.renderDashedOutlinedBox(action.pos, action.params, matrixStack, buffer, action.color, action.gapDistance);
            class_286.method_43433((class_9801)buffer.method_60800());
            RenderSystem.enableCull();
            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();
        }
    }

    private void renderFilledBox(class_243 pos, class_243 params, class_4587 matrices, class_287 buffer, Color color) {
        class_243 camPos = BoxRender.mc.field_1773.method_19418().method_19326();
        float x1 = (float)(pos.field_1352 - camPos.field_1352);
        float y1 = (float)(pos.field_1351 - camPos.field_1351);
        float z1 = (float)(pos.field_1350 - camPos.field_1350);
        float x2 = x1 + (float)params.field_1352;
        float y2 = y1 + (float)params.field_1351;
        float z2 = z1 + (float)params.field_1350;
        this.vertexQuad(matrices, buffer, x1, y1, z1, x2, y1, z1, x2, y2, z1, x1, y2, z1, color);
        this.vertexQuad(matrices, buffer, x1, y1, z2, x2, y1, z2, x2, y2, z2, x1, y2, z2, color);
        this.vertexQuad(matrices, buffer, x1, y1, z1, x1, y1, z2, x1, y2, z2, x1, y2, z1, color);
        this.vertexQuad(matrices, buffer, x2, y1, z1, x2, y1, z2, x2, y2, z2, x2, y2, z1, color);
        this.vertexQuad(matrices, buffer, x1, y1, z1, x1, y2, z1, x2, y2, z1, x2, y1, z1, color);
        this.vertexQuad(matrices, buffer, x1, y1, z2, x1, y2, z2, x2, y2, z2, x2, y1, z2, color);
    }

    private void renderOutlinedBox(class_243 pos, class_243 params, class_4587 matrices, class_287 buffer, Color color) {
        class_243 camPos = BoxRender.mc.field_1773.method_19418().method_19326();
        float x1 = (float)(pos.field_1352 - camPos.field_1352);
        float y1 = (float)(pos.field_1351 - camPos.field_1351);
        float z1 = (float)(pos.field_1350 - camPos.field_1350);
        float x2 = x1 + (float)params.field_1352;
        float y2 = y1 + (float)params.field_1351;
        float z2 = z1 + (float)params.field_1350;
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

    private void renderDashedOutlinedBox(class_243 pos, class_243 params, class_4587 matrices, class_287 buffer, Color color, float gapDistance) {
        class_243 camPos = BoxRender.mc.field_1773.method_19418().method_19326();
        float x1 = (float)(pos.field_1352 - camPos.field_1352);
        float y1 = (float)(pos.field_1351 - camPos.field_1351);
        float z1 = (float)(pos.field_1350 - camPos.field_1350);
        float x2 = x1 + (float)params.field_1352;
        float y2 = y1 + (float)params.field_1351;
        float z2 = z1 + (float)params.field_1350;
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

    private void renderDashedLine(class_4587 matrices, class_287 buffer, Color color, float x1, float y1, float z1, float x2, float y2, float z2, float dashLength, float gapDistance) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        float dz = z2 - z1;
        float totalLength = class_3532.method_15355((float)(dx * dx + dy * dy + dz * dz));
        if (class_3532.method_15347((float)totalLength, (float)0.0f)) {
            return;
        }
        float t = 0.0f;
        while (t < 1.0f) {
            float tDashStart = t;
            float tDashEnd = Math.min(t + dashLength / totalLength, 1.0f);
            if (tDashEnd > tDashStart) {
                float dashX1 = x1 + dx * tDashStart;
                float dashY1 = y1 + dy * tDashStart;
                float dashZ1 = z1 + dz * tDashStart;
                float dashX2 = x1 + dx * tDashEnd;
                float dashY2 = y1 + dy * tDashEnd;
                float dashZ2 = z1 + dz * tDashEnd;
                this.vertexLine(matrices, (class_4588)buffer, dashX1, dashY1, dashZ1, dashX2, dashY2, dashZ2, color);
            }
            t = tDashEnd;
            t = Math.min(t + gapDistance / totalLength, 1.0f);
        }
    }

    private void vertexLine(class_4587 matrices, class_4588 buffer, float x1, float y1, float z1, float x2, float y2, float z2, Color lineColor) {
        class_4587.class_4665 entry = matrices.method_23760();
        Matrix4f model = entry.method_23761();
        Vector3f normalVec = this.getNormalVec(x1, y1, z1, x2, y2, z2);
        buffer.method_22918(model, x1, y1, z1).method_1336(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), lineColor.getAlpha()).method_60831(entry, normalVec.x, normalVec.y, normalVec.z);
        buffer.method_22918(model, x2, y2, z2).method_1336(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), lineColor.getAlpha()).method_60831(entry, normalVec.x, normalVec.y, normalVec.z);
    }

    private Vector3f getNormalVec(float x1, float y1, float z1, float x2, float y2, float z2) {
        float xNormal = x2 - x1;
        float yNormal = y2 - y1;
        float zNormal = z2 - z1;
        float normalSqrt = class_3532.method_15355((float)(xNormal * xNormal + yNormal * yNormal + zNormal * zNormal));
        return new Vector3f(xNormal / normalSqrt, yNormal / normalSqrt, zNormal / normalSqrt);
    }

    private void vertexQuad(class_4587 matrices, class_287 buffer, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4, Color color) {
        class_4587.class_4665 entry = matrices.method_23760();
        Matrix4f model = entry.method_23761();
        Vector3f normal = new Vector3f(0.0f, 0.0f, 1.0f);
        buffer.method_22918(model, x1, y1, z1).method_1336(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).method_60831(entry, normal.x, normal.y, normal.z);
        buffer.method_22918(model, x2, y2, z2).method_1336(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).method_60831(entry, normal.x, normal.y, normal.z);
        buffer.method_22918(model, x3, y3, z3).method_1336(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).method_60831(entry, normal.x, normal.y, normal.z);
        buffer.method_22918(model, x4, y4, z4).method_1336(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).method_60831(entry, normal.x, normal.y, normal.z);
    }

    public void drawBox(float x1, float y1, float z1, float x2, float y2, float z2, float lineWidth, Color color, Render renderMode, float gapDistance) {
        class_243 pos = new class_243((double)x1, (double)y1, (double)z1);
        class_243 params = new class_243((double)(x2 - x1), (double)(y2 - y1), (double)(z2 - z1));
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
            }
        }
    }

    public record FilledBox(class_243 pos, class_243 params, Color color) {
    }

    public record OutlinedBox(class_243 pos, class_243 params, float lineWidth, Color color) {
    }

    public record StripedBox(class_243 pos, class_243 params, float lineWidth, Color color, float gapDistance) {
    }

    public static enum Render {
        FILL,
        OUTLINE,
        STRIPED;

    }
}


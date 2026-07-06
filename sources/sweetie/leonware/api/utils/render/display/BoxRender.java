package sweetie.leonware.api.utils.render.display;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_10142;
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
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.newsclub.net.unix.AFVSOCKSocketAddress;
import sweetie.leonware.api.system.interfaces.QuickImports;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/render/display/BoxRender.class */
public class BoxRender implements QuickImports {
    private final List<OutlinedBox> outlinedBoxes = new ArrayList();
    private final List<FilledBox> filledBoxes = new ArrayList();
    private final List<StripedBox> stripedBoxes = new ArrayList();

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/render/display/BoxRender$Render.class */
    public enum Render {
        FILL,
        OUTLINE,
        STRIPED
    }

    public void setup3DRender(class_4587 matrixStack) {
        class_4184 camera = mc.field_1773.method_19418();
        class_243 cameraPos = camera.method_19326();
        class_4604 frustum = new class_4604(matrixStack.method_23760().method_23761(), RenderSystem.getProjectionMatrix());
        frustum.method_23088(cameraPos.field_1352, cameraPos.field_1351, cameraPos.field_1350);
        renderFilledBoxes(this.filledBoxes, frustum, matrixStack);
        renderStripedBoxes(this.stripedBoxes, frustum, matrixStack);
        renderOutlinedBoxes(this.outlinedBoxes, frustum, matrixStack);
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
            if (frustum.method_23093(box)) {
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                class_289 tessellator = class_289.method_1348();
                class_287 buffer = tessellator.method_60827(class_293.class_5596.field_27382, class_290.field_1576);
                RenderSystem.disableCull();
                RenderSystem.disableDepthTest();
                RenderSystem.setShader(class_10142.field_53876);
                renderFilledBox(action.pos, action.params, matrixStack, buffer, action.color);
                class_286.method_43433(buffer.method_60800());
                RenderSystem.enableCull();
                RenderSystem.enableDepthTest();
                RenderSystem.disableBlend();
            }
        }
    }

    private void renderOutlinedBoxes(List<OutlinedBox> boxes, class_4604 frustum, class_4587 matrixStack) {
        if (boxes.isEmpty()) {
            return;
        }
        for (OutlinedBox action : boxes) {
            class_238 box = new class_238(action.pos, action.pos.method_1019(action.params));
            if (frustum.method_23093(box)) {
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                class_289 tessellator = class_289.method_1348();
                class_287 buffer = tessellator.method_60827(class_293.class_5596.field_27377, class_290.field_29337);
                RenderSystem.disableCull();
                RenderSystem.disableDepthTest();
                RenderSystem.setShader(class_10142.field_53864);
                RenderSystem.lineWidth(action.lineWidth);
                renderOutlinedBox(action.pos, action.params, matrixStack, buffer, action.color);
                class_286.method_43433(buffer.method_60800());
                RenderSystem.enableCull();
                RenderSystem.enableDepthTest();
                RenderSystem.disableBlend();
            }
        }
    }

    private void renderStripedBoxes(List<StripedBox> boxes, class_4604 frustum, class_4587 matrixStack) {
        if (boxes.isEmpty()) {
            return;
        }
        for (StripedBox action : boxes) {
            class_238 box = new class_238(action.pos, action.pos.method_1019(action.params));
            if (frustum.method_23093(box)) {
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                class_289 tessellator = class_289.method_1348();
                class_287 buffer = tessellator.method_60827(class_293.class_5596.field_27377, class_290.field_29337);
                RenderSystem.disableCull();
                RenderSystem.disableDepthTest();
                RenderSystem.setShader(class_10142.field_53864);
                RenderSystem.lineWidth(action.lineWidth);
                renderDashedOutlinedBox(action.pos, action.params, matrixStack, buffer, action.color, action.gapDistance);
                class_286.method_43433(buffer.method_60800());
                RenderSystem.enableCull();
                RenderSystem.enableDepthTest();
                RenderSystem.disableBlend();
            }
        }
    }

    private void renderFilledBox(class_243 pos, class_243 params, class_4587 matrices, class_287 buffer, Color color) {
        class_243 camPos = mc.field_1773.method_19418().method_19326();
        float x1 = (float) (pos.field_1352 - camPos.field_1352);
        float y1 = (float) (pos.field_1351 - camPos.field_1351);
        float z1 = (float) (pos.field_1350 - camPos.field_1350);
        float x2 = x1 + ((float) params.field_1352);
        float y2 = y1 + ((float) params.field_1351);
        float z2 = z1 + ((float) params.field_1350);
        vertexQuad(matrices, buffer, x1, y1, z1, x2, y1, z1, x2, y2, z1, x1, y2, z1, color);
        vertexQuad(matrices, buffer, x1, y1, z2, x2, y1, z2, x2, y2, z2, x1, y2, z2, color);
        vertexQuad(matrices, buffer, x1, y1, z1, x1, y1, z2, x1, y2, z2, x1, y2, z1, color);
        vertexQuad(matrices, buffer, x2, y1, z1, x2, y1, z2, x2, y2, z2, x2, y2, z1, color);
        vertexQuad(matrices, buffer, x1, y1, z1, x1, y2, z1, x2, y2, z1, x2, y1, z1, color);
        vertexQuad(matrices, buffer, x1, y1, z2, x1, y2, z2, x2, y2, z2, x2, y1, z2, color);
    }

    private void renderOutlinedBox(class_243 pos, class_243 params, class_4587 matrices, class_287 buffer, Color color) {
        class_243 camPos = mc.field_1773.method_19418().method_19326();
        float x1 = (float) (pos.field_1352 - camPos.field_1352);
        float y1 = (float) (pos.field_1351 - camPos.field_1351);
        float z1 = (float) (pos.field_1350 - camPos.field_1350);
        float x2 = x1 + ((float) params.field_1352);
        float y2 = y1 + ((float) params.field_1351);
        float z2 = z1 + ((float) params.field_1350);
        vertexLine(matrices, buffer, x1, y1, z1, x2, y1, z1, color);
        vertexLine(matrices, buffer, x2, y1, z1, x2, y1, z2, color);
        vertexLine(matrices, buffer, x2, y1, z2, x1, y1, z2, color);
        vertexLine(matrices, buffer, x1, y1, z2, x1, y1, z1, color);
        vertexLine(matrices, buffer, x1, y2, z1, x2, y2, z1, color);
        vertexLine(matrices, buffer, x2, y2, z1, x2, y2, z2, color);
        vertexLine(matrices, buffer, x2, y2, z2, x1, y2, z2, color);
        vertexLine(matrices, buffer, x1, y2, z2, x1, y2, z1, color);
        vertexLine(matrices, buffer, x1, y1, z1, x1, y2, z1, color);
        vertexLine(matrices, buffer, x2, y1, z1, x2, y2, z1, color);
        vertexLine(matrices, buffer, x1, y1, z2, x1, y2, z2, color);
        vertexLine(matrices, buffer, x2, y1, z2, x2, y2, z2, color);
    }

    private void renderDashedOutlinedBox(class_243 pos, class_243 params, class_4587 matrices, class_287 buffer, Color color, float gapDistance) {
        class_243 camPos = mc.field_1773.method_19418().method_19326();
        float x1 = (float) (pos.field_1352 - camPos.field_1352);
        float y1 = (float) (pos.field_1351 - camPos.field_1351);
        float z1 = (float) (pos.field_1350 - camPos.field_1350);
        float x2 = x1 + ((float) params.field_1352);
        float y2 = y1 + ((float) params.field_1351);
        float z2 = z1 + ((float) params.field_1350);
        renderDashedLine(matrices, buffer, color, x1, y1, z1, x2, y1, z1, gapDistance, gapDistance);
        renderDashedLine(matrices, buffer, color, x2, y1, z1, x2, y1, z2, gapDistance, gapDistance);
        renderDashedLine(matrices, buffer, color, x2, y1, z2, x1, y1, z2, gapDistance, gapDistance);
        renderDashedLine(matrices, buffer, color, x1, y1, z2, x1, y1, z1, gapDistance, gapDistance);
        renderDashedLine(matrices, buffer, color, x1, y2, z1, x2, y2, z1, gapDistance, gapDistance);
        renderDashedLine(matrices, buffer, color, x2, y2, z1, x2, y2, z2, gapDistance, gapDistance);
        renderDashedLine(matrices, buffer, color, x2, y2, z2, x1, y2, z2, gapDistance, gapDistance);
        renderDashedLine(matrices, buffer, color, x1, y2, z2, x1, y2, z1, gapDistance, gapDistance);
        renderDashedLine(matrices, buffer, color, x1, y1, z1, x1, y2, z1, gapDistance, gapDistance);
        renderDashedLine(matrices, buffer, color, x2, y1, z1, x2, y2, z1, gapDistance, gapDistance);
        renderDashedLine(matrices, buffer, color, x1, y1, z2, x1, y2, z2, gapDistance, gapDistance);
        renderDashedLine(matrices, buffer, color, x2, y1, z2, x2, y2, z2, gapDistance, gapDistance);
    }

    private void renderDashedLine(class_4587 matrices, class_287 buffer, Color color, float x1, float y1, float z1, float x2, float y2, float z2, float dashLength, float gapDistance) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        float dz = z2 - z1;
        float totalLength = class_3532.method_15355((dx * dx) + (dy * dy) + (dz * dz));
        if (class_3532.method_15347(totalLength, 0.0f)) {
            return;
        }
        float fMin = 0.0f;
        while (true) {
            float t = fMin;
            if (t < 1.0f) {
                float tDashEnd = Math.min(t + (dashLength / totalLength), 1.0f);
                if (tDashEnd > t) {
                    float dashX1 = x1 + (dx * t);
                    float dashY1 = y1 + (dy * t);
                    float dashZ1 = z1 + (dz * t);
                    float dashX2 = x1 + (dx * tDashEnd);
                    float dashY2 = y1 + (dy * tDashEnd);
                    float dashZ2 = z1 + (dz * tDashEnd);
                    vertexLine(matrices, buffer, dashX1, dashY1, dashZ1, dashX2, dashY2, dashZ2, color);
                }
                fMin = Math.min(tDashEnd + (gapDistance / totalLength), 1.0f);
            } else {
                return;
            }
        }
    }

    private void vertexLine(class_4587 matrices, class_4588 buffer, float x1, float y1, float z1, float x2, float y2, float z2, Color lineColor) {
        class_4587.class_4665 entry = matrices.method_23760();
        Matrix4f model = entry.method_23761();
        Vector3f normalVec = getNormalVec(x1, y1, z1, x2, y2, z2);
        buffer.method_22918(model, x1, y1, z1).method_1336(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), lineColor.getAlpha()).method_60831(entry, normalVec.x, normalVec.y, normalVec.z);
        buffer.method_22918(model, x2, y2, z2).method_1336(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), lineColor.getAlpha()).method_60831(entry, normalVec.x, normalVec.y, normalVec.z);
    }

    private Vector3f getNormalVec(float x1, float y1, float z1, float x2, float y2, float z2) {
        float xNormal = x2 - x1;
        float yNormal = y2 - y1;
        float zNormal = z2 - z1;
        float normalSqrt = class_3532.method_15355((xNormal * xNormal) + (yNormal * yNormal) + (zNormal * zNormal));
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
        class_243 pos = new class_243(x1, y1, z1);
        class_243 params = new class_243(x2 - x1, y2 - y1, z2 - z1);
        switch (renderMode.ordinal()) {
            case 0:
                this.filledBoxes.add(new FilledBox(pos, params, color));
                break;
            case 1:
                this.outlinedBoxes.add(new OutlinedBox(pos, params, lineWidth, color));
                break;
            case AFVSOCKSocketAddress.VMADDR_CID_HOST /* 2 */:
                this.stripedBoxes.add(new StripedBox(pos, params, lineWidth, color, gapDistance));
                break;
        }
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/render/display/BoxRender$FilledBox.class */
    public static final class FilledBox extends Record {
        private final class_243 pos;
        private final class_243 params;
        private final Color color;

        public FilledBox(class_243 pos, class_243 params, Color color) {
            this.pos = pos;
            this.params = params;
            this.color = color;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, FilledBox.class), FilledBox.class, "pos;params;color", "FIELD:Lsweetie/leonware/api/utils/render/display/BoxRender$FilledBox;->pos:Lnet/minecraft/class_243;", "FIELD:Lsweetie/leonware/api/utils/render/display/BoxRender$FilledBox;->params:Lnet/minecraft/class_243;", "FIELD:Lsweetie/leonware/api/utils/render/display/BoxRender$FilledBox;->color:Ljava/awt/Color;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, FilledBox.class), FilledBox.class, "pos;params;color", "FIELD:Lsweetie/leonware/api/utils/render/display/BoxRender$FilledBox;->pos:Lnet/minecraft/class_243;", "FIELD:Lsweetie/leonware/api/utils/render/display/BoxRender$FilledBox;->params:Lnet/minecraft/class_243;", "FIELD:Lsweetie/leonware/api/utils/render/display/BoxRender$FilledBox;->color:Ljava/awt/Color;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, FilledBox.class, Object.class), FilledBox.class, "pos;params;color", "FIELD:Lsweetie/leonware/api/utils/render/display/BoxRender$FilledBox;->pos:Lnet/minecraft/class_243;", "FIELD:Lsweetie/leonware/api/utils/render/display/BoxRender$FilledBox;->params:Lnet/minecraft/class_243;", "FIELD:Lsweetie/leonware/api/utils/render/display/BoxRender$FilledBox;->color:Ljava/awt/Color;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public class_243 pos() {
            return this.pos;
        }

        public class_243 params() {
            return this.params;
        }

        public Color color() {
            return this.color;
        }
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/render/display/BoxRender$OutlinedBox.class */
    public static final class OutlinedBox extends Record {
        private final class_243 pos;
        private final class_243 params;
        private final float lineWidth;
        private final Color color;

        public OutlinedBox(class_243 pos, class_243 params, float lineWidth, Color color) {
            this.pos = pos;
            this.params = params;
            this.lineWidth = lineWidth;
            this.color = color;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, OutlinedBox.class), OutlinedBox.class, "pos;params;lineWidth;color", "FIELD:Lsweetie/leonware/api/utils/render/display/BoxRender$OutlinedBox;->pos:Lnet/minecraft/class_243;", "FIELD:Lsweetie/leonware/api/utils/render/display/BoxRender$OutlinedBox;->params:Lnet/minecraft/class_243;", "FIELD:Lsweetie/leonware/api/utils/render/display/BoxRender$OutlinedBox;->lineWidth:F", "FIELD:Lsweetie/leonware/api/utils/render/display/BoxRender$OutlinedBox;->color:Ljava/awt/Color;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, OutlinedBox.class), OutlinedBox.class, "pos;params;lineWidth;color", "FIELD:Lsweetie/leonware/api/utils/render/display/BoxRender$OutlinedBox;->pos:Lnet/minecraft/class_243;", "FIELD:Lsweetie/leonware/api/utils/render/display/BoxRender$OutlinedBox;->params:Lnet/minecraft/class_243;", "FIELD:Lsweetie/leonware/api/utils/render/display/BoxRender$OutlinedBox;->lineWidth:F", "FIELD:Lsweetie/leonware/api/utils/render/display/BoxRender$OutlinedBox;->color:Ljava/awt/Color;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, OutlinedBox.class, Object.class), OutlinedBox.class, "pos;params;lineWidth;color", "FIELD:Lsweetie/leonware/api/utils/render/display/BoxRender$OutlinedBox;->pos:Lnet/minecraft/class_243;", "FIELD:Lsweetie/leonware/api/utils/render/display/BoxRender$OutlinedBox;->params:Lnet/minecraft/class_243;", "FIELD:Lsweetie/leonware/api/utils/render/display/BoxRender$OutlinedBox;->lineWidth:F", "FIELD:Lsweetie/leonware/api/utils/render/display/BoxRender$OutlinedBox;->color:Ljava/awt/Color;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public class_243 pos() {
            return this.pos;
        }

        public class_243 params() {
            return this.params;
        }

        public float lineWidth() {
            return this.lineWidth;
        }

        public Color color() {
            return this.color;
        }
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/render/display/BoxRender$StripedBox.class */
    public static final class StripedBox extends Record {
        private final class_243 pos;
        private final class_243 params;
        private final float lineWidth;
        private final Color color;
        private final float gapDistance;

        public StripedBox(class_243 pos, class_243 params, float lineWidth, Color color, float gapDistance) {
            this.pos = pos;
            this.params = params;
            this.lineWidth = lineWidth;
            this.color = color;
            this.gapDistance = gapDistance;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, StripedBox.class), StripedBox.class, "pos;params;lineWidth;color;gapDistance", "FIELD:Lsweetie/leonware/api/utils/render/display/BoxRender$StripedBox;->pos:Lnet/minecraft/class_243;", "FIELD:Lsweetie/leonware/api/utils/render/display/BoxRender$StripedBox;->params:Lnet/minecraft/class_243;", "FIELD:Lsweetie/leonware/api/utils/render/display/BoxRender$StripedBox;->lineWidth:F", "FIELD:Lsweetie/leonware/api/utils/render/display/BoxRender$StripedBox;->color:Ljava/awt/Color;", "FIELD:Lsweetie/leonware/api/utils/render/display/BoxRender$StripedBox;->gapDistance:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, StripedBox.class), StripedBox.class, "pos;params;lineWidth;color;gapDistance", "FIELD:Lsweetie/leonware/api/utils/render/display/BoxRender$StripedBox;->pos:Lnet/minecraft/class_243;", "FIELD:Lsweetie/leonware/api/utils/render/display/BoxRender$StripedBox;->params:Lnet/minecraft/class_243;", "FIELD:Lsweetie/leonware/api/utils/render/display/BoxRender$StripedBox;->lineWidth:F", "FIELD:Lsweetie/leonware/api/utils/render/display/BoxRender$StripedBox;->color:Ljava/awt/Color;", "FIELD:Lsweetie/leonware/api/utils/render/display/BoxRender$StripedBox;->gapDistance:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, StripedBox.class, Object.class), StripedBox.class, "pos;params;lineWidth;color;gapDistance", "FIELD:Lsweetie/leonware/api/utils/render/display/BoxRender$StripedBox;->pos:Lnet/minecraft/class_243;", "FIELD:Lsweetie/leonware/api/utils/render/display/BoxRender$StripedBox;->params:Lnet/minecraft/class_243;", "FIELD:Lsweetie/leonware/api/utils/render/display/BoxRender$StripedBox;->lineWidth:F", "FIELD:Lsweetie/leonware/api/utils/render/display/BoxRender$StripedBox;->color:Ljava/awt/Color;", "FIELD:Lsweetie/leonware/api/utils/render/display/BoxRender$StripedBox;->gapDistance:F").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public class_243 pos() {
            return this.pos;
        }

        public class_243 params() {
            return this.params;
        }

        public float lineWidth() {
            return this.lineWidth;
        }

        public Color color() {
            return this.color;
        }

        public float gapDistance() {
            return this.gapDistance;
        }
    }
}

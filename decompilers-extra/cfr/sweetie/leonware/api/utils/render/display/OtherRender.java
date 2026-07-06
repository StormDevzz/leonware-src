/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_4587
 */
package sweetie.leonware.api.utils.render.display;

import net.minecraft.class_4587;

public class OtherRender {
    public void scaleStart(class_4587 matrixStack, float x, float y, double scale) {
        matrixStack.method_22903();
        matrixStack.method_46416(x, y, 0.0f);
        matrixStack.method_22905((float)scale, (float)scale, 1.0f);
        matrixStack.method_46416(-x, -y, 0.0f);
    }

    public void squshStart(class_4587 matrixStack, float x, float y, double scale) {
        matrixStack.method_22903();
        matrixStack.method_46416(x, y, 0.0f);
        matrixStack.method_22905(1.0f, (float)scale, 1.0f);
        matrixStack.method_46416(-x, -y, 0.0f);
    }

    public void scaleStop(class_4587 matrixStack) {
        matrixStack.method_22909();
    }
}


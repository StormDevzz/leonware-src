// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.render.display;

import net.minecraft.class_4587;

public class OtherRender
{
    public void scaleStart(final class_4587 matrixStack, final float x, final float y, final double scale) {
        matrixStack.method_22903();
        matrixStack.method_46416(x, y, 0.0f);
        matrixStack.method_22905((float)scale, (float)scale, 1.0f);
        matrixStack.method_46416(-x, -y, 0.0f);
    }
    
    public void squshStart(final class_4587 matrixStack, final float x, final float y, final double scale) {
        matrixStack.method_22903();
        matrixStack.method_46416(x, y, 0.0f);
        matrixStack.method_22905(1.0f, (float)scale, 1.0f);
        matrixStack.method_46416(-x, -y, 0.0f);
    }
    
    public void scaleStop(final class_4587 matrixStack) {
        matrixStack.method_22909();
    }
}

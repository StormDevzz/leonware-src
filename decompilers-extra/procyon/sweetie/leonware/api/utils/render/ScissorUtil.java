// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.render;

import lombok.Generated;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_4587;
import sweetie.leonware.api.system.interfaces.QuickImports;

public final class ScissorUtil implements QuickImports
{
    public static void start(final class_4587 matrixStack, final float x, final float y, final float width, final float height) {
        final float scale = (float)ScissorUtil.mc.method_22683().method_4495();
        final float adjustedY = ScissorUtil.mc.method_22683().method_4502() - y;
        final float scaledX = x * scale;
        final float scaledY = adjustedY * scale;
        final float scaledWidth = width * scale;
        final float scaledHeight = height * scale;
        matrixStack.method_22903();
        RenderSystem.enableScissor((int)scaledX, (int)(scaledY - scaledHeight), (int)scaledWidth, (int)scaledHeight);
    }
    
    public static void stop(final class_4587 matrixStack) {
        RenderSystem.disableScissor();
        matrixStack.method_22909();
    }
    
    @Generated
    private ScissorUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

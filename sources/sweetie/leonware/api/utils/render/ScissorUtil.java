package sweetie.leonware.api.utils.render;

import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Generated;
import net.minecraft.class_4587;
import sweetie.leonware.api.system.interfaces.QuickImports;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/render/ScissorUtil.class */
public final class ScissorUtil implements QuickImports {
    @Generated
    private ScissorUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void start(class_4587 matrixStack, float x, float y, float width, float height) {
        float scale = (float) mc.method_22683().method_4495();
        float adjustedY = mc.method_22683().method_4502() - y;
        float scaledX = x * scale;
        float scaledY = adjustedY * scale;
        float scaledWidth = width * scale;
        float scaledHeight = height * scale;
        matrixStack.method_22903();
        RenderSystem.enableScissor((int) scaledX, (int) (scaledY - scaledHeight), (int) scaledWidth, (int) scaledHeight);
    }

    public static void stop(class_4587 matrixStack) {
        RenderSystem.disableScissor();
        matrixStack.method_22909();
    }
}

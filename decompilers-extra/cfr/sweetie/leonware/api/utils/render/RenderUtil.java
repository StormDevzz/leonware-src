/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.utils.render;

import lombok.Generated;
import sweetie.leonware.api.utils.render.display.BlurRectRender;
import sweetie.leonware.api.utils.render.display.BoxRender;
import sweetie.leonware.api.utils.render.display.GlowRectRender;
import sweetie.leonware.api.utils.render.display.GradientRectRender;
import sweetie.leonware.api.utils.render.display.OtherRender;
import sweetie.leonware.api.utils.render.display.RectRender;
import sweetie.leonware.api.utils.render.display.TextureRectRender;
import sweetie.leonware.api.utils.render.display.WorldRender;

public final class RenderUtil {
    public static RectRender RECT = new RectRender();
    public static BlurRectRender BLUR_RECT = new BlurRectRender();
    public static GradientRectRender GRADIENT_RECT = new GradientRectRender();
    public static TextureRectRender TEXTURE_RECT = new TextureRectRender();
    public static GlowRectRender GLOW_RECT = new GlowRectRender();
    public static OtherRender OTHER = new OtherRender();
    public static WorldRender WORLD = new WorldRender();
    public static BoxRender BOX = new BoxRender();

    @Generated
    private RenderUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}


// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.render;

import lombok.Generated;
import sweetie.leonware.api.utils.render.display.BoxRender;
import sweetie.leonware.api.utils.render.display.WorldRender;
import sweetie.leonware.api.utils.render.display.OtherRender;
import sweetie.leonware.api.utils.render.display.GlowRectRender;
import sweetie.leonware.api.utils.render.display.TextureRectRender;
import sweetie.leonware.api.utils.render.display.GradientRectRender;
import sweetie.leonware.api.utils.render.display.BlurRectRender;
import sweetie.leonware.api.utils.render.display.RectRender;

public final class RenderUtil
{
    public static RectRender RECT;
    public static BlurRectRender BLUR_RECT;
    public static GradientRectRender GRADIENT_RECT;
    public static TextureRectRender TEXTURE_RECT;
    public static GlowRectRender GLOW_RECT;
    public static OtherRender OTHER;
    public static WorldRender WORLD;
    public static BoxRender BOX;
    
    @Generated
    private RenderUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    static {
        RenderUtil.RECT = new RectRender();
        RenderUtil.BLUR_RECT = new BlurRectRender();
        RenderUtil.GRADIENT_RECT = new GradientRectRender();
        RenderUtil.TEXTURE_RECT = new TextureRectRender();
        RenderUtil.GLOW_RECT = new GlowRectRender();
        RenderUtil.OTHER = new OtherRender();
        RenderUtil.WORLD = new WorldRender();
        RenderUtil.BOX = new BoxRender();
    }
}

// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.render.fonts;

import net.minecraft.class_4588;
import org.joml.Matrix4f;

public final class MsdfGlyph
{
    private final int code;
    private final float minU;
    private final float maxU;
    private final float minV;
    private final float maxV;
    private final float advance;
    private final float topPosition;
    private final float width;
    private final float height;
    
    public MsdfGlyph(final FontData.GlyphData data, final float atlasWidth, final float atlasHeight) {
        this.code = data.unicode();
        this.advance = data.advance();
        final FontData.BoundsData atlasBounds = data.atlasBounds();
        if (atlasBounds != null) {
            this.minU = atlasBounds.left() / atlasWidth;
            this.maxU = atlasBounds.right() / atlasWidth;
            this.minV = 1.0f - atlasBounds.top() / atlasHeight;
            this.maxV = 1.0f - atlasBounds.bottom() / atlasHeight;
        }
        else {
            final float n = 0.0f;
            this.maxV = n;
            this.minV = n;
            this.maxU = n;
            this.minU = n;
        }
        final FontData.BoundsData planeBounds = data.planeBounds();
        if (planeBounds != null) {
            this.width = planeBounds.right() - planeBounds.left();
            this.height = planeBounds.top() - planeBounds.bottom();
            this.topPosition = planeBounds.top();
        }
        else {
            final float width = 0.0f;
            this.topPosition = width;
            this.height = width;
            this.width = width;
        }
    }
    
    public float apply(final Matrix4f matrix, final class_4588 consumer, final float size, final float x, float y, final float z, final int color) {
        y -= this.topPosition * size;
        final float width = this.width * size;
        final float height = this.height * size;
        consumer.method_22918(matrix, x, y, z).method_22913(this.minU, this.minV).method_39415(color);
        consumer.method_22918(matrix, x, y + height, z).method_22913(this.minU, this.maxV).method_39415(color);
        consumer.method_22918(matrix, x + width, y + height, z).method_22913(this.maxU, this.maxV).method_39415(color);
        consumer.method_22918(matrix, x + width, y, z).method_22913(this.maxU, this.minV).method_39415(color);
        return this.advance * size;
    }
    
    public float getWidth(final float size) {
        return this.advance * size;
    }
    
    public int getCharCode() {
        return this.code;
    }
    
    record ColoredGlyph(char c, int color) {}
}

package sweetie.leonware.api.utils.render.fonts;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import net.minecraft.class_4588;
import org.joml.Matrix4f;
import sweetie.leonware.api.utils.render.fonts.FontData;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/render/fonts/MsdfGlyph.class */
public final class MsdfGlyph {
    private final int code;
    private final float minU;
    private final float maxU;
    private final float minV;
    private final float maxV;
    private final float advance;
    private final float topPosition;
    private final float width;
    private final float height;

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/render/fonts/MsdfGlyph$ColoredGlyph.class */
    public static final class ColoredGlyph extends Record {
        private final char c;
        private final int color;

        public ColoredGlyph(char c, int color) {
            this.c = c;
            this.color = color;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, ColoredGlyph.class), ColoredGlyph.class, "c;color", "FIELD:Lsweetie/leonware/api/utils/render/fonts/MsdfGlyph$ColoredGlyph;->c:C", "FIELD:Lsweetie/leonware/api/utils/render/fonts/MsdfGlyph$ColoredGlyph;->color:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, ColoredGlyph.class), ColoredGlyph.class, "c;color", "FIELD:Lsweetie/leonware/api/utils/render/fonts/MsdfGlyph$ColoredGlyph;->c:C", "FIELD:Lsweetie/leonware/api/utils/render/fonts/MsdfGlyph$ColoredGlyph;->color:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, ColoredGlyph.class, Object.class), ColoredGlyph.class, "c;color", "FIELD:Lsweetie/leonware/api/utils/render/fonts/MsdfGlyph$ColoredGlyph;->c:C", "FIELD:Lsweetie/leonware/api/utils/render/fonts/MsdfGlyph$ColoredGlyph;->color:I").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public char c() {
            return this.c;
        }

        public int color() {
            return this.color;
        }
    }

    public MsdfGlyph(FontData.GlyphData data, float atlasWidth, float atlasHeight) {
        this.code = data.unicode();
        this.advance = data.advance();
        FontData.BoundsData atlasBounds = data.atlasBounds();
        if (atlasBounds != null) {
            this.minU = atlasBounds.left() / atlasWidth;
            this.maxU = atlasBounds.right() / atlasWidth;
            this.minV = 1.0f - (atlasBounds.top() / atlasHeight);
            this.maxV = 1.0f - (atlasBounds.bottom() / atlasHeight);
        } else {
            this.maxV = 0.0f;
            this.minV = 0.0f;
            this.maxU = 0.0f;
            this.minU = 0.0f;
        }
        FontData.BoundsData planeBounds = data.planeBounds();
        if (planeBounds != null) {
            this.width = planeBounds.right() - planeBounds.left();
            this.height = planeBounds.top() - planeBounds.bottom();
            this.topPosition = planeBounds.top();
        } else {
            this.topPosition = 0.0f;
            this.height = 0.0f;
            this.width = 0.0f;
        }
    }

    public float apply(Matrix4f matrix, class_4588 consumer, float size, float x, float y, float z, int color) {
        float y2 = y - (this.topPosition * size);
        float width = this.width * size;
        float height = this.height * size;
        consumer.method_22918(matrix, x, y2, z).method_22913(this.minU, this.minV).method_39415(color);
        consumer.method_22918(matrix, x, y2 + height, z).method_22913(this.minU, this.maxV).method_39415(color);
        consumer.method_22918(matrix, x + width, y2 + height, z).method_22913(this.maxU, this.maxV).method_39415(color);
        consumer.method_22918(matrix, x + width, y2, z).method_22913(this.maxU, this.minV).method_39415(color);
        return this.advance * size;
    }

    public float getWidth(float size) {
        return this.advance * size;
    }

    public int getCharCode() {
        return this.code;
    }
}

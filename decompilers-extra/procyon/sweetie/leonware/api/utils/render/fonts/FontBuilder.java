// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.render.fonts;

import net.minecraft.class_1044;
import java.util.HashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.Map;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_310;
import sweetie.leonware.api.system.files.FileUtil;
import net.minecraft.class_2960;

public class FontBuilder
{
    private String name;
    private class_2960 dataIdentifier;
    private class_2960 atlasIdentifier;
    
    public FontBuilder find(final String fontName) {
        this.name = fontName;
        this.dataIdentifier = class_2960.method_60655("LeonWare".toLowerCase(), "fonts/" + fontName + ".json");
        this.atlasIdentifier = class_2960.method_60655("LeonWare".toLowerCase(), "fonts/" + fontName + ".png");
        return this;
    }
    
    public Font load() {
        final FontData data = FileUtil.fromJsonToInstance(this.dataIdentifier, FontData.class);
        final class_1044 texture = class_310.method_1551().method_1531().method_4619(this.atlasIdentifier);
        if (data == null) {
            throw new RuntimeException("Failed to read font data file: " + this.dataIdentifier.toString() + "; Are you sure this is json file? Try to check the correctness of its syntax.");
        }
        RenderSystem.recordRenderCall(() -> texture.method_4527(true, false));
        final float aWidth = data.atlas().width();
        final float aHeight = data.atlas().height();
        final Map<Integer, MsdfGlyph> glyphs = data.glyphs().stream().collect(Collectors.toMap((Function<? super Object, ? extends Integer>)FontData.GlyphData::unicode, glyphData -> new MsdfGlyph(glyphData, aWidth, aHeight)));
        final Map<Integer, Map<Integer, Float>> kernings = new HashMap<Integer, Map<Integer, Float>>();
        data.kernings().forEach(kerning -> {
            Map<Integer, Float> map = kernings.get(kerning.leftChar());
            if (map == null) {
                map = new HashMap<Integer, Float>();
                kernings.put(kerning.leftChar(), map);
            }
            map.put(kerning.rightChar(), kerning.advance());
            return;
        });
        return new Font(this.name, texture, data.atlas(), data.metrics(), glyphs, kernings);
    }
}

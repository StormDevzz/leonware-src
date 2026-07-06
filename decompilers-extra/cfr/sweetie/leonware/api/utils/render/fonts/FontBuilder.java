/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.class_1044
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 */
package sweetie.leonware.api.utils.render.fonts;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.class_1044;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import sweetie.leonware.api.system.files.FileUtil;
import sweetie.leonware.api.utils.render.fonts.Font;
import sweetie.leonware.api.utils.render.fonts.FontData;
import sweetie.leonware.api.utils.render.fonts.MsdfGlyph;

public class FontBuilder {
    private String name;
    private class_2960 dataIdentifier;
    private class_2960 atlasIdentifier;

    public FontBuilder find(String fontName) {
        this.name = fontName;
        this.dataIdentifier = class_2960.method_60655((String)"LeonWare".toLowerCase(), (String)("fonts/" + fontName + ".json"));
        this.atlasIdentifier = class_2960.method_60655((String)"LeonWare".toLowerCase(), (String)("fonts/" + fontName + ".png"));
        return this;
    }

    public Font load() {
        FontData data = FileUtil.fromJsonToInstance(this.dataIdentifier, FontData.class);
        class_1044 texture = class_310.method_1551().method_1531().method_4619(this.atlasIdentifier);
        if (data == null) {
            throw new RuntimeException("Failed to read font data file: " + this.dataIdentifier.toString() + "; Are you sure this is json file? Try to check the correctness of its syntax.");
        }
        RenderSystem.recordRenderCall(() -> texture.method_4527(true, false));
        float aWidth = data.atlas().width();
        float aHeight = data.atlas().height();
        Map<Integer, MsdfGlyph> glyphs = data.glyphs().stream().collect(Collectors.toMap(FontData.GlyphData::unicode, glyphData -> new MsdfGlyph((FontData.GlyphData)glyphData, aWidth, aHeight)));
        HashMap<Integer, Map<Integer, Float>> kernings = new HashMap<Integer, Map<Integer, Float>>();
        data.kernings().forEach(kerning -> {
            HashMap<Integer, Float> map = (HashMap<Integer, Float>)kernings.get(kerning.leftChar());
            if (map == null) {
                map = new HashMap<Integer, Float>();
                kernings.put(kerning.leftChar(), map);
            }
            map.put(kerning.rightChar(), Float.valueOf(kerning.advance()));
        });
        return new Font(this.name, texture, data.atlas(), data.metrics(), glyphs, kernings);
    }
}


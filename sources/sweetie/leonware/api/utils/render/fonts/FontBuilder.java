package sweetie.leonware.api.utils.render.fonts;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.class_1044;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import sweetie.leonware.api.system.backend.ClientInfo;
import sweetie.leonware.api.system.files.FileUtil;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/render/fonts/FontBuilder.class */
public class FontBuilder {
    private String name;
    private class_2960 dataIdentifier;
    private class_2960 atlasIdentifier;

    public FontBuilder find(String fontName) {
        this.name = fontName;
        this.dataIdentifier = class_2960.method_60655(ClientInfo.NAME.toLowerCase(), "fonts/" + fontName + ".json");
        this.atlasIdentifier = class_2960.method_60655(ClientInfo.NAME.toLowerCase(), "fonts/" + fontName + ".png");
        return this;
    }

    public Font load() {
        FontData data = (FontData) FileUtil.fromJsonToInstance(this.dataIdentifier, FontData.class);
        class_1044 texture = class_310.method_1551().method_1531().method_4619(this.atlasIdentifier);
        if (data == null) {
            throw new RuntimeException("Failed to read font data file: " + this.dataIdentifier.toString() + "; Are you sure this is json file? Try to check the correctness of its syntax.");
        }
        RenderSystem.recordRenderCall(() -> {
            texture.method_4527(true, false);
        });
        float aWidth = data.atlas().width();
        float aHeight = data.atlas().height();
        Map<Integer, MsdfGlyph> glyphs = (Map) data.glyphs().stream().collect(Collectors.toMap((v0) -> {
            return v0.unicode();
        }, glyphData -> {
            return new MsdfGlyph(glyphData, aWidth, aHeight);
        }));
        Map<Integer, Map<Integer, Float>> kernings = new HashMap<>();
        data.kernings().forEach(kerning -> {
            Map<Integer, Float> map = (Map) kernings.get(Integer.valueOf(kerning.leftChar()));
            if (map == null) {
                map = new HashMap<>();
                kernings.put(Integer.valueOf(kerning.leftChar()), map);
            }
            map.put(Integer.valueOf(kerning.rightChar()), Float.valueOf(kerning.advance()));
        });
        return new Font(this.name, texture, data.atlas(), data.metrics(), glyphs, kernings);
    }
}

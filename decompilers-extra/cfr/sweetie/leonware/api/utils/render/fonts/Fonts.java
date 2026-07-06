/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.utils.render.fonts;

import java.util.HashMap;
import java.util.Map;
import lombok.Generated;
import sweetie.leonware.api.utils.render.fonts.Font;

public final class Fonts {
    public static final String sf = "sf_pro";
    public static final String ps = "product_sans";
    private static final Map<String, Font> cache = new HashMap<String, Font>();
    public static final Font SF_BOLD = Fonts.get("sf_pro/sf_bold");
    public static final Font SF_SEMIBOLD = Fonts.get("sf_pro/sf_semibold");
    public static final Font SF_MEDIUM = Fonts.get("sf_pro/sf_medium");
    public static final Font SF_REGULAR = Fonts.get("sf_pro/sf_regular");
    public static final Font SF_LIGHT = Fonts.get("sf_pro/sf_light");
    public static final Font PS_BLACK = Fonts.get("product_sans/productsans_black");
    public static final Font PS_BOLD = Fonts.get("product_sans/productsans_bold");
    public static final Font PS_MEDIUM = Fonts.get("product_sans/productsans_medium");
    public static final Font PS_REGULAR = Fonts.get("product_sans/productsans_regular");
    public static final Font PS_LIGHT = Fonts.get("product_sans/productsans_light");
    public static final Font PS_THIN = Fonts.get("product_sans/productsans_thin");
    public static final Font ICONS = Fonts.get("other/icons");
    public static final Font NOUGAT = Fonts.get("nougat/nougat");

    public static float getMediumThickness() {
        return 0.07f;
    }

    public static float getBoldThickness() {
        return 0.1f;
    }

    private static Font get(String input) {
        return Font.builder().find(input).load();
    }

    @Generated
    private Fonts() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}


// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.render.fonts;

import java.util.HashMap;
import lombok.Generated;
import java.util.Map;

public final class Fonts
{
    public static final String sf = "sf_pro";
    public static final String ps = "product_sans";
    private static final Map<String, Font> cache;
    public static final Font SF_BOLD;
    public static final Font SF_SEMIBOLD;
    public static final Font SF_MEDIUM;
    public static final Font SF_REGULAR;
    public static final Font SF_LIGHT;
    public static final Font PS_BLACK;
    public static final Font PS_BOLD;
    public static final Font PS_MEDIUM;
    public static final Font PS_REGULAR;
    public static final Font PS_LIGHT;
    public static final Font PS_THIN;
    public static final Font ICONS;
    public static final Font NOUGAT;
    
    public static float getMediumThickness() {
        return 0.07f;
    }
    
    public static float getBoldThickness() {
        return 0.1f;
    }
    
    private static Font get(final String input) {
        return Font.builder().find(input).load();
    }
    
    @Generated
    private Fonts() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    static {
        cache = new HashMap<String, Font>();
        SF_BOLD = get("sf_pro/sf_bold");
        SF_SEMIBOLD = get("sf_pro/sf_semibold");
        SF_MEDIUM = get("sf_pro/sf_medium");
        SF_REGULAR = get("sf_pro/sf_regular");
        SF_LIGHT = get("sf_pro/sf_light");
        PS_BLACK = get("product_sans/productsans_black");
        PS_BOLD = get("product_sans/productsans_bold");
        PS_MEDIUM = get("product_sans/productsans_medium");
        PS_REGULAR = get("product_sans/productsans_regular");
        PS_LIGHT = get("product_sans/productsans_light");
        PS_THIN = get("product_sans/productsans_thin");
        ICONS = get("other/icons");
        NOUGAT = get("nougat/nougat");
    }
}

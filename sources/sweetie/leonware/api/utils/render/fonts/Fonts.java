package sweetie.leonware.api.utils.render.fonts;

import java.util.HashMap;
import java.util.Map;
import lombok.Generated;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/render/fonts/Fonts.class */
public final class Fonts {
    public static final String sf = "sf_pro";
    public static final String ps = "product_sans";
    private static final Map<String, Font> cache = new HashMap();
    public static final Font SF_BOLD = get("sf_pro/sf_bold");
    public static final Font SF_SEMIBOLD = get("sf_pro/sf_semibold");
    public static final Font SF_MEDIUM = get("sf_pro/sf_medium");
    public static final Font SF_REGULAR = get("sf_pro/sf_regular");
    public static final Font SF_LIGHT = get("sf_pro/sf_light");
    public static final Font PS_BLACK = get("product_sans/productsans_black");
    public static final Font PS_BOLD = get("product_sans/productsans_bold");
    public static final Font PS_MEDIUM = get("product_sans/productsans_medium");
    public static final Font PS_REGULAR = get("product_sans/productsans_regular");
    public static final Font PS_LIGHT = get("product_sans/productsans_light");
    public static final Font PS_THIN = get("product_sans/productsans_thin");
    public static final Font ICONS = get("other/icons");
    public static final Font NOUGAT = get("nougat/nougat");

    @Generated
    private Fonts() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static float getMediumThickness() {
        return 0.07f;
    }

    public static float getBoldThickness() {
        return 0.1f;
    }

    private static Font get(String input) {
        return Font.builder().find(input).load();
    }
}

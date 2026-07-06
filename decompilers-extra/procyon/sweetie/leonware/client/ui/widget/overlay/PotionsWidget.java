// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.widget.overlay;

import java.util.HashSet;
import java.util.Iterator;
import java.awt.Color;
import sweetie.leonware.api.utils.other.TextUtil;
import sweetie.leonware.api.utils.color.ColorUtil;
import net.minecraft.class_5321;
import net.minecraft.class_1293;
import net.minecraft.class_2477;
import sweetie.leonware.api.utils.color.UIColors;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.class_2960;
import java.util.Set;
import sweetie.leonware.client.ui.widget.ContainerWidget;

public class PotionsWidget extends ContainerWidget
{
    private static final Set<class_2960> BAD_EFFECTS;
    private static final Set<class_2960> COOL_EFFECTS;
    
    public PotionsWidget() {
        super(3.0f, 120.0f);
    }
    
    @Override
    public String getName() {
        return "Potions";
    }
    
    @Override
    protected Map<String, ContainerElement.ColoredString> getCurrentData() {
        final Map<String, ContainerElement.ColoredString> map = new HashMap<String, ContainerElement.ColoredString>();
        if (PotionsWidget.mc.field_1724 == null) {
            return map;
        }
        final Color textColor = UIColors.textColor();
        final Color positiveColor = UIColors.positiveColor();
        final Color negativeColor = UIColors.negativeColor();
        final class_2477 lang = class_2477.method_10517();
        for (class_1293 effect : PotionsWidget.mc.field_1724.method_6088().values()) {
            final class_2960 id = effect.method_5579().method_40230().get().method_29177();
            final Color miss_you = PotionsWidget.BAD_EFFECTS.contains(id) ? ColorUtil.flashingColor(negativeColor, textColor) : (PotionsWidget.COOL_EFFECTS.contains(id) ? ColorUtil.flashingColor(positiveColor, textColor) : textColor);
            final String level = (effect.method_5578() > 0) ? (" " + (effect.method_5578() + 1)) : "";
            final String name = lang.method_48307(effect.method_5586()) + level;
            final String durationText = TextUtil.getDurationText(effect.method_5584());
            map.put(name, new ContainerElement.ColoredString(durationText, miss_you));
        }
        return map;
    }
    
    static {
        BAD_EFFECTS = new HashSet<class_2960>();
        COOL_EFFECTS = new HashSet<class_2960>();
        PotionsWidget.BAD_EFFECTS.add(class_2960.method_60655("minecraft", "wither"));
        PotionsWidget.BAD_EFFECTS.add(class_2960.method_60655("minecraft", "poison"));
        PotionsWidget.BAD_EFFECTS.add(class_2960.method_60655("minecraft", "slowness"));
        PotionsWidget.BAD_EFFECTS.add(class_2960.method_60655("minecraft", "weakness"));
        PotionsWidget.BAD_EFFECTS.add(class_2960.method_60655("minecraft", "mining_fatigue"));
        PotionsWidget.BAD_EFFECTS.add(class_2960.method_60655("minecraft", "nausea"));
        PotionsWidget.BAD_EFFECTS.add(class_2960.method_60655("minecraft", "blindness"));
        PotionsWidget.BAD_EFFECTS.add(class_2960.method_60655("minecraft", "hunger"));
        PotionsWidget.BAD_EFFECTS.add(class_2960.method_60655("minecraft", "levitation"));
        PotionsWidget.BAD_EFFECTS.add(class_2960.method_60655("minecraft", "unluck"));
        PotionsWidget.COOL_EFFECTS.add(class_2960.method_60655("minecraft", "speed"));
        PotionsWidget.COOL_EFFECTS.add(class_2960.method_60655("minecraft", "strength"));
        PotionsWidget.COOL_EFFECTS.add(class_2960.method_60655("minecraft", "regeneration"));
    }
}

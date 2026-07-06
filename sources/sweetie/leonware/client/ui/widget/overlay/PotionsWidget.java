package sweetie.leonware.client.ui.widget.overlay;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.class_1293;
import net.minecraft.class_2477;
import net.minecraft.class_2960;
import net.minecraft.class_5321;
import sweetie.leonware.api.utils.color.ColorUtil;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.other.TextUtil;
import sweetie.leonware.client.ui.widget.ContainerWidget;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/widget/overlay/PotionsWidget.class */
public class PotionsWidget extends ContainerWidget {
    private static final Set<class_2960> BAD_EFFECTS = new HashSet();
    private static final Set<class_2960> COOL_EFFECTS = new HashSet();

    public PotionsWidget() {
        super(3.0f, 120.0f);
    }

    @Override // sweetie.leonware.client.ui.widget.Widget
    public String getName() {
        return "Potions";
    }

    static {
        BAD_EFFECTS.add(class_2960.method_60655("minecraft", "wither"));
        BAD_EFFECTS.add(class_2960.method_60655("minecraft", "poison"));
        BAD_EFFECTS.add(class_2960.method_60655("minecraft", "slowness"));
        BAD_EFFECTS.add(class_2960.method_60655("minecraft", "weakness"));
        BAD_EFFECTS.add(class_2960.method_60655("minecraft", "mining_fatigue"));
        BAD_EFFECTS.add(class_2960.method_60655("minecraft", "nausea"));
        BAD_EFFECTS.add(class_2960.method_60655("minecraft", "blindness"));
        BAD_EFFECTS.add(class_2960.method_60655("minecraft", "hunger"));
        BAD_EFFECTS.add(class_2960.method_60655("minecraft", "levitation"));
        BAD_EFFECTS.add(class_2960.method_60655("minecraft", "unluck"));
        COOL_EFFECTS.add(class_2960.method_60655("minecraft", "speed"));
        COOL_EFFECTS.add(class_2960.method_60655("minecraft", "strength"));
        COOL_EFFECTS.add(class_2960.method_60655("minecraft", "regeneration"));
    }

    @Override // sweetie.leonware.client.ui.widget.ContainerWidget
    protected Map<String, ContainerWidget.ContainerElement.ColoredString> getCurrentData() {
        Color colorFlashingColor;
        Map<String, ContainerWidget.ContainerElement.ColoredString> map = new HashMap<>();
        if (mc.field_1724 == null) {
            return map;
        }
        Color textColor = UIColors.textColor();
        Color positiveColor = UIColors.positiveColor();
        Color negativeColor = UIColors.negativeColor();
        class_2477 lang = class_2477.method_10517();
        for (class_1293 effect : mc.field_1724.method_6088().values()) {
            class_2960 id = ((class_5321) effect.method_5579().method_40230().get()).method_29177();
            if (BAD_EFFECTS.contains(id)) {
                colorFlashingColor = ColorUtil.flashingColor(negativeColor, textColor);
            } else if (COOL_EFFECTS.contains(id)) {
                colorFlashingColor = ColorUtil.flashingColor(positiveColor, textColor);
            } else {
                colorFlashingColor = textColor;
            }
            Color miss_you = colorFlashingColor;
            String level = effect.method_5578() > 0 ? " " + (effect.method_5578() + 1) : "";
            String name = lang.method_48307(effect.method_5586()) + level;
            String durationText = TextUtil.getDurationText(effect.method_5584());
            map.put(name, new ContainerWidget.ContainerElement.ColoredString(durationText, miss_you));
        }
        return map;
    }
}

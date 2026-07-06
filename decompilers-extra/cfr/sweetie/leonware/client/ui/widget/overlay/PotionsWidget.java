/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1293
 *  net.minecraft.class_2477
 *  net.minecraft.class_2960
 *  net.minecraft.class_5321
 */
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

public class PotionsWidget
extends ContainerWidget {
    private static final Set<class_2960> BAD_EFFECTS = new HashSet<class_2960>();
    private static final Set<class_2960> COOL_EFFECTS = new HashSet<class_2960>();

    public PotionsWidget() {
        super(3.0f, 120.0f);
    }

    @Override
    public String getName() {
        return "Potions";
    }

    @Override
    protected Map<String, ContainerWidget.ContainerElement.ColoredString> getCurrentData() {
        HashMap<String, ContainerWidget.ContainerElement.ColoredString> map = new HashMap<String, ContainerWidget.ContainerElement.ColoredString>();
        if (PotionsWidget.mc.field_1724 == null) {
            return map;
        }
        Color textColor = UIColors.textColor();
        Color positiveColor = UIColors.positiveColor();
        Color negativeColor = UIColors.negativeColor();
        class_2477 lang = class_2477.method_10517();
        for (class_1293 effect : PotionsWidget.mc.field_1724.method_6088().values()) {
            class_2960 id = ((class_5321)effect.method_5579().method_40230().get()).method_29177();
            Color miss_you = BAD_EFFECTS.contains(id) ? ColorUtil.flashingColor(negativeColor, textColor) : (COOL_EFFECTS.contains(id) ? ColorUtil.flashingColor(positiveColor, textColor) : textColor);
            String level = effect.method_5578() > 0 ? " " + (effect.method_5578() + 1) : "";
            String name = lang.method_48307(effect.method_5586()) + level;
            String durationText = TextUtil.getDurationText(effect.method_5584());
            map.put(name, new ContainerWidget.ContainerElement.ColoredString(durationText, miss_you));
        }
        return map;
    }

    static {
        BAD_EFFECTS.add(class_2960.method_60655((String)"minecraft", (String)"wither"));
        BAD_EFFECTS.add(class_2960.method_60655((String)"minecraft", (String)"poison"));
        BAD_EFFECTS.add(class_2960.method_60655((String)"minecraft", (String)"slowness"));
        BAD_EFFECTS.add(class_2960.method_60655((String)"minecraft", (String)"weakness"));
        BAD_EFFECTS.add(class_2960.method_60655((String)"minecraft", (String)"mining_fatigue"));
        BAD_EFFECTS.add(class_2960.method_60655((String)"minecraft", (String)"nausea"));
        BAD_EFFECTS.add(class_2960.method_60655((String)"minecraft", (String)"blindness"));
        BAD_EFFECTS.add(class_2960.method_60655((String)"minecraft", (String)"hunger"));
        BAD_EFFECTS.add(class_2960.method_60655((String)"minecraft", (String)"levitation"));
        BAD_EFFECTS.add(class_2960.method_60655((String)"minecraft", (String)"unluck"));
        COOL_EFFECTS.add(class_2960.method_60655((String)"minecraft", (String)"speed"));
        COOL_EFFECTS.add(class_2960.method_60655((String)"minecraft", (String)"strength"));
        COOL_EFFECTS.add(class_2960.method_60655((String)"minecraft", (String)"regeneration"));
    }
}


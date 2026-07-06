/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1792
 *  net.minecraft.class_1796
 *  net.minecraft.class_1796$class_1797
 *  net.minecraft.class_1799
 *  net.minecraft.class_2960
 */
package sweetie.leonware.client.ui.widget.overlay;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.class_1792;
import net.minecraft.class_1796;
import net.minecraft.class_1799;
import net.minecraft.class_2960;
import sweetie.leonware.api.utils.other.TextUtil;
import sweetie.leonware.client.ui.widget.ContainerWidget;

public class CooldownsWidget
extends ContainerWidget {
    @Override
    public String getName() {
        return "Cooldowns";
    }

    public CooldownsWidget() {
        super(120.0f, 100.0f);
    }

    @Override
    protected Map<String, ContainerWidget.ContainerElement.ColoredString> getCurrentData() {
        HashMap<String, ContainerWidget.ContainerElement.ColoredString> cooldownData = new HashMap<String, ContainerWidget.ContainerElement.ColoredString>();
        if (CooldownsWidget.mc.field_1724 == null) {
            return cooldownData;
        }
        class_1796 manager = CooldownsWidget.mc.field_1724.method_7357();
        float tickDelta = mc.method_61966().method_60637(false);
        for (int i = 0; i < CooldownsWidget.mc.field_1724.method_31548().method_5439(); ++i) {
            int remaining;
            class_1799 stack = CooldownsWidget.mc.field_1724.method_31548().method_5438(i);
            if (stack.method_7960()) continue;
            class_1792 item = stack.method_7909();
            if (!manager.method_7904(stack) || (remaining = this.getRemainingCooldownTicks(stack, tickDelta)) <= 0) continue;
            String name = item.method_63680().getString();
            String time = TextUtil.getDurationText(remaining);
            cooldownData.put(name, new ContainerWidget.ContainerElement.ColoredString(time));
        }
        return cooldownData;
    }

    private int getRemainingCooldownTicks(class_1799 stack, float tickDelta) {
        class_1796 manager = CooldownsWidget.mc.field_1724.method_7357();
        class_2960 groupId = manager.method_62836(stack);
        class_1796.class_1797 entry = (class_1796.class_1797)manager.field_8024.get(groupId);
        if (entry != null) {
            return Math.max(0, entry.comp_3084() - (manager.field_8025 + (int)tickDelta));
        }
        return 0;
    }
}


package sweetie.leonware.client.ui.widget.overlay;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.class_1792;
import net.minecraft.class_1796;
import net.minecraft.class_1799;
import net.minecraft.class_2960;
import sweetie.leonware.api.utils.other.TextUtil;
import sweetie.leonware.client.ui.widget.ContainerWidget;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/widget/overlay/CooldownsWidget.class */
public class CooldownsWidget extends ContainerWidget {
    @Override // sweetie.leonware.client.ui.widget.Widget
    public String getName() {
        return "Cooldowns";
    }

    public CooldownsWidget() {
        super(120.0f, 100.0f);
    }

    @Override // sweetie.leonware.client.ui.widget.ContainerWidget
    protected Map<String, ContainerWidget.ContainerElement.ColoredString> getCurrentData() {
        int remaining;
        Map<String, ContainerWidget.ContainerElement.ColoredString> cooldownData = new HashMap<>();
        if (mc.field_1724 == null) {
            return cooldownData;
        }
        class_1796 manager = mc.field_1724.method_7357();
        float tickDelta = mc.method_61966().method_60637(false);
        for (int i = 0; i < mc.field_1724.method_31548().method_5439(); i++) {
            class_1799 stack = mc.field_1724.method_31548().method_5438(i);
            if (!stack.method_7960()) {
                class_1792 item = stack.method_7909();
                if (manager.method_7904(stack) && (remaining = getRemainingCooldownTicks(stack, tickDelta)) > 0) {
                    String name = item.method_63680().getString();
                    String time = TextUtil.getDurationText(remaining);
                    cooldownData.put(name, new ContainerWidget.ContainerElement.ColoredString(time));
                }
            }
        }
        return cooldownData;
    }

    private int getRemainingCooldownTicks(class_1799 stack, float tickDelta) {
        class_1796 manager = mc.field_1724.method_7357();
        class_2960 groupId = manager.method_62836(stack);
        class_1796.class_1797 entry = (class_1796.class_1797) manager.field_8024.get(groupId);
        if (entry != null) {
            return Math.max(0, entry.comp_3084() - (manager.field_8025 + ((int) tickDelta)));
        }
        return 0;
    }
}

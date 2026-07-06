// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.widget.overlay;

import net.minecraft.class_2960;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1796;
import sweetie.leonware.api.utils.other.TextUtil;
import java.util.HashMap;
import java.util.Map;
import sweetie.leonware.client.ui.widget.ContainerWidget;

public class CooldownsWidget extends ContainerWidget
{
    @Override
    public String getName() {
        return "Cooldowns";
    }
    
    public CooldownsWidget() {
        super(120.0f, 100.0f);
    }
    
    @Override
    protected Map<String, ContainerElement.ColoredString> getCurrentData() {
        final Map<String, ContainerElement.ColoredString> cooldownData = new HashMap<String, ContainerElement.ColoredString>();
        if (CooldownsWidget.mc.field_1724 == null) {
            return cooldownData;
        }
        final class_1796 manager = CooldownsWidget.mc.field_1724.method_7357();
        final float tickDelta = CooldownsWidget.mc.method_61966().method_60637(false);
        for (int i = 0; i < CooldownsWidget.mc.field_1724.method_31548().method_5439(); ++i) {
            final class_1799 stack = CooldownsWidget.mc.field_1724.method_31548().method_5438(i);
            if (!stack.method_7960()) {
                final class_1792 item = stack.method_7909();
                if (manager.method_7904(stack)) {
                    final int remaining = this.getRemainingCooldownTicks(stack, tickDelta);
                    if (remaining > 0) {
                        final String name = item.method_63680().getString();
                        final String time = TextUtil.getDurationText(remaining);
                        cooldownData.put(name, new ContainerElement.ColoredString(time));
                    }
                }
            }
        }
        return cooldownData;
    }
    
    private int getRemainingCooldownTicks(final class_1799 stack, final float tickDelta) {
        final class_1796 manager = CooldownsWidget.mc.field_1724.method_7357();
        final class_2960 groupId = manager.method_62836(stack);
        final class_1796.class_1797 entry = manager.field_8024.get(groupId);
        if (entry != null) {
            return Math.max(0, entry.comp_3084() - (manager.field_8025 + (int)tickDelta));
        }
        return 0;
    }
}

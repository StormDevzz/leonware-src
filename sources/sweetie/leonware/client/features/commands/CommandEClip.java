package sweetie.leonware.client.features.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_1713;
import net.minecraft.class_1802;
import net.minecraft.class_2172;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2828;
import net.minecraft.class_2848;
import sweetie.leonware.api.command.Command;
import sweetie.leonware.api.command.CommandRegister;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/commands/CommandEClip.class */
@CommandRegister(name = "eclip")
public class CommandEClip extends Command {
    @Override // sweetie.leonware.api.command.Command
    public void execute(LiteralArgumentBuilder<class_2172> builder) {
        builder.then(literal("bedrock").executes(context -> {
            execute((-((float) mc.field_1724.method_23318())) - 3.0f);
            return this.SINGLE_SUCCESS;
        }));
        builder.then(literal("down").executes(context2 -> {
            float y = 0.0f;
            int i = 1;
            while (true) {
                if (i >= 255) {
                    break;
                }
                if (mc.field_1687.method_8320(class_2338.method_49638(mc.field_1724.method_19538()).method_10069(0, -i, 0)) == class_2246.field_10124.method_9564()) {
                    y = (-i) - 1;
                    break;
                }
                if (mc.field_1687.method_8320(class_2338.method_49638(mc.field_1724.method_19538()).method_10069(0, -i, 0)) != class_2246.field_9987.method_9564()) {
                    i++;
                } else {
                    print("§cМожно телепортироваться только под бедрок: eclip bedrock");
                    return this.SINGLE_SUCCESS;
                }
            }
        }));
        builder.then(literal("up").executes(context3 -> {
            float y = 0.0f;
            int i = 4;
            while (true) {
                if (i >= 255) {
                    break;
                }
                if (mc.field_1687.method_8320(class_2338.method_49638(mc.field_1724.method_19538()).method_10069(0, i, 0)) != class_2246.field_10124.method_9564()) {
                    i++;
                } else {
                    y = i + 1;
                    break;
                }
            }
            execute(y);
            return this.SINGLE_SUCCESS;
        }));
        builder.executes(context4 -> {
            print("Использование: eclip bedrock | eclip up | eclip down");
            return this.SINGLE_SUCCESS;
        });
    }

    private void execute(float y) {
        int elytraSlot = findElytraSlot();
        if (elytraSlot == -1) {
            print("§cВам нужны элитры в инвентаре");
            return;
        }
        boolean needSwap = elytraSlot != 6;
        if (needSwap) {
            int syncId = mc.field_1724.field_7512.field_7763;
            mc.field_1761.method_2906(syncId, elytraSlot, 1, class_1713.field_7790, mc.field_1724);
            mc.field_1761.method_2906(syncId, 6, 1, class_1713.field_7790, mc.field_1724);
        }
        mc.field_1724.field_3944.method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), false, mc.field_1724.field_5976));
        mc.field_1724.field_3944.method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), false, mc.field_1724.field_5976));
        mc.field_1724.field_3944.method_52787(new class_2848(mc.field_1724, class_2848.class_2849.field_12982));
        mc.field_1724.field_3944.method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + ((double) y), mc.field_1724.method_23321(), false, mc.field_1724.field_5976));
        mc.field_1724.field_3944.method_52787(new class_2848(mc.field_1724, class_2848.class_2849.field_12982));
        if (needSwap) {
            int syncId2 = mc.field_1724.field_7512.field_7763;
            mc.field_1761.method_2906(syncId2, 6, 1, class_1713.field_7790, mc.field_1724);
            mc.field_1761.method_2906(syncId2, elytraSlot, 1, class_1713.field_7790, mc.field_1724);
        }
        mc.field_1724.method_5814(mc.field_1724.method_23317(), mc.field_1724.method_23318() + ((double) y), mc.field_1724.method_23321());
        print("§aElytra clip на " + String.format("%.1f", Float.valueOf(Math.abs(y))) + " блоков.");
    }

    private int findElytraSlot() {
        if (mc.field_1724.method_31548().method_5438(38).method_7909() == class_1802.field_8833) {
            return 6;
        }
        int i = 0;
        while (i < 36) {
            if (mc.field_1724.method_31548().method_5438(i).method_7909() == class_1802.field_8833) {
                return i < 9 ? i + 36 : i;
            }
            i++;
        }
        return -1;
    }
}

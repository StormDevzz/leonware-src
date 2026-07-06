// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.commands;

import net.minecraft.class_2246;
import net.minecraft.class_2374;
import net.minecraft.class_2338;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.class_1802;
import net.minecraft.class_1297;
import net.minecraft.class_2848;
import net.minecraft.class_2596;
import net.minecraft.class_2828;
import net.minecraft.class_1657;
import net.minecraft.class_1713;
import net.minecraft.class_2172;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import sweetie.leonware.api.command.CommandRegister;
import sweetie.leonware.api.command.Command;

@CommandRegister(name = "eclip")
public class CommandEClip extends Command
{
    @Override
    public void execute(final LiteralArgumentBuilder<class_2172> builder) {
        builder.then(Command.literal("bedrock").executes(context -> {
            this.execute(-(float)CommandEClip.mc.field_1724.method_23318() - 3.0f);
            return this.SINGLE_SUCCESS;
        }));
        builder.then(Command.literal("down").executes(context -> {
            float y = 0.0f;
            for (int i = 1; i < 255; ++i) {
                if (CommandEClip.mc.field_1687.method_8320(class_2338.method_49638((class_2374)CommandEClip.mc.field_1724.method_19538()).method_10069(0, -i, 0)) == class_2246.field_10124.method_9564()) {
                    y = (float)(-i - 1);
                    break;
                }
                if (CommandEClip.mc.field_1687.method_8320(class_2338.method_49638((class_2374)CommandEClip.mc.field_1724.method_19538()).method_10069(0, -i, 0)) == class_2246.field_9987.method_9564()) {
                    this.print("§c\u041c\u043e\u0436\u043d\u043e \u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c\u0441\u044f \u0442\u043e\u043b\u044c\u043a\u043e \u043f\u043e\u0434 \u0431\u0435\u0434\u0440\u043e\u043a: eclip bedrock");
                    return this.SINGLE_SUCCESS;
                }
            }
            this.execute(y);
            return this.SINGLE_SUCCESS;
        }));
        builder.then(Command.literal("up").executes(context -> {
            float y = 0.0f;
            for (int i = 4; i < 255; ++i) {
                if (CommandEClip.mc.field_1687.method_8320(class_2338.method_49638((class_2374)CommandEClip.mc.field_1724.method_19538()).method_10069(0, i, 0)) == class_2246.field_10124.method_9564()) {
                    y = (float)(i + 1);
                    break;
                }
            }
            this.execute(y);
            return this.SINGLE_SUCCESS;
        }));
        builder.executes(context -> {
            this.print("\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435: eclip bedrock | eclip up | eclip down");
            return this.SINGLE_SUCCESS;
        });
    }
    
    private void execute(final float y) {
        final int elytraSlot = this.findElytraSlot();
        if (elytraSlot == -1) {
            this.print("§c\u0412\u0430\u043c \u043d\u0443\u0436\u043d\u044b \u044d\u043b\u0438\u0442\u0440\u044b \u0432 \u0438\u043d\u0432\u0435\u043d\u0442\u0430\u0440\u0435");
            return;
        }
        final boolean needSwap = elytraSlot != 6;
        if (needSwap) {
            final int syncId = CommandEClip.mc.field_1724.field_7512.field_7763;
            CommandEClip.mc.field_1761.method_2906(syncId, elytraSlot, 1, class_1713.field_7790, (class_1657)CommandEClip.mc.field_1724);
            CommandEClip.mc.field_1761.method_2906(syncId, 6, 1, class_1713.field_7790, (class_1657)CommandEClip.mc.field_1724);
        }
        CommandEClip.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(CommandEClip.mc.field_1724.method_23317(), CommandEClip.mc.field_1724.method_23318(), CommandEClip.mc.field_1724.method_23321(), false, CommandEClip.mc.field_1724.field_5976));
        CommandEClip.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(CommandEClip.mc.field_1724.method_23317(), CommandEClip.mc.field_1724.method_23318(), CommandEClip.mc.field_1724.method_23321(), false, CommandEClip.mc.field_1724.field_5976));
        CommandEClip.mc.field_1724.field_3944.method_52787((class_2596)new class_2848((class_1297)CommandEClip.mc.field_1724, class_2848.class_2849.field_12982));
        CommandEClip.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(CommandEClip.mc.field_1724.method_23317(), CommandEClip.mc.field_1724.method_23318() + y, CommandEClip.mc.field_1724.method_23321(), false, CommandEClip.mc.field_1724.field_5976));
        CommandEClip.mc.field_1724.field_3944.method_52787((class_2596)new class_2848((class_1297)CommandEClip.mc.field_1724, class_2848.class_2849.field_12982));
        if (needSwap) {
            final int syncId = CommandEClip.mc.field_1724.field_7512.field_7763;
            CommandEClip.mc.field_1761.method_2906(syncId, 6, 1, class_1713.field_7790, (class_1657)CommandEClip.mc.field_1724);
            CommandEClip.mc.field_1761.method_2906(syncId, elytraSlot, 1, class_1713.field_7790, (class_1657)CommandEClip.mc.field_1724);
        }
        CommandEClip.mc.field_1724.method_5814(CommandEClip.mc.field_1724.method_23317(), CommandEClip.mc.field_1724.method_23318() + y, CommandEClip.mc.field_1724.method_23321());
        this.print("§aElytra clip \u043d\u0430 " + String.format("%.1f", Math.abs(y)) + " \u0431\u043b\u043e\u043a\u043e\u0432.");
    }
    
    private int findElytraSlot() {
        if (CommandEClip.mc.field_1724.method_31548().method_5438(38).method_7909() == class_1802.field_8833) {
            return 6;
        }
        for (int i = 0; i < 36; ++i) {
            if (CommandEClip.mc.field_1724.method_31548().method_5438(i).method_7909() == class_1802.field_8833) {
                return (i < 9) ? (i + 36) : i;
            }
        }
        return -1;
    }
}

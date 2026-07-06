// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.commands;

import net.minecraft.class_2596;
import net.minecraft.class_2828;
import net.minecraft.class_3532;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import net.minecraft.class_2172;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import sweetie.leonware.api.command.CommandRegister;
import sweetie.leonware.api.command.Command;

@CommandRegister(name = "hclip")
public class CommandHClip extends Command
{
    @Override
    public void execute(final LiteralArgumentBuilder<class_2172> builder) {
        builder.then(Command.literal("s").executes(context -> {
            final double h = 0.8;
            final float f = CommandHClip.mc.field_1724.method_36454() * 0.017453292f;
            final double x = -(class_3532.method_15374(f) * h);
            final double z = class_3532.method_15362(f) * h;
            for (int de = (int)(Math.abs(h) * 9.953), i = 0; i < Math.min(de, 19); ++i) {
                CommandHClip.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(CommandHClip.mc.field_1724.method_23317(), CommandHClip.mc.field_1724.method_23318(), CommandHClip.mc.field_1724.method_23321(), CommandHClip.mc.field_1724.method_24828(), CommandHClip.mc.field_1724.field_5976));
            }
            CommandHClip.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(CommandHClip.mc.field_1724.method_23317() + x, CommandHClip.mc.field_1724.method_23318(), CommandHClip.mc.field_1724.method_23321() + z, false, CommandHClip.mc.field_1724.field_5976));
            CommandHClip.mc.field_1724.method_5814(CommandHClip.mc.field_1724.method_23317() + x, CommandHClip.mc.field_1724.method_23318(), CommandHClip.mc.field_1724.method_23321() + z);
            this.print("§a\u041a\u043b\u0438\u043f\u043d\u0443\u043b\u0438\u0441\u044c \u043d\u0430 " + String.format("%.1f", h) + " \u0431\u043b\u043e\u043a\u043e\u0432 \u0432\u043f\u0435\u0440\u0451\u0434.");
            return this.SINGLE_SUCCESS;
        }));
        builder.then(Command.argument("count", (com.mojang.brigadier.arguments.ArgumentType<Object>)DoubleArgumentType.doubleArg()).executes(context -> {
            final double h = (double)context.getArgument("count", (Class)Double.class);
            final double clampedH = class_3532.method_15350(h, -200.0, 200.0);
            final float f = CommandHClip.mc.field_1724.method_36454() * 0.017453292f;
            final double x = -(class_3532.method_15374(f) * clampedH);
            final double z = class_3532.method_15362(f) * clampedH;
            for (int de = (int)(Math.abs(clampedH) * 9.953), i = 0; i < Math.min(de, 19); ++i) {
                CommandHClip.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(CommandHClip.mc.field_1724.method_23317(), CommandHClip.mc.field_1724.method_23318(), CommandHClip.mc.field_1724.method_23321(), CommandHClip.mc.field_1724.method_24828(), CommandHClip.mc.field_1724.field_5976));
            }
            CommandHClip.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(CommandHClip.mc.field_1724.method_23317() + x, CommandHClip.mc.field_1724.method_23318(), CommandHClip.mc.field_1724.method_23321() + z, false, CommandHClip.mc.field_1724.field_5976));
            CommandHClip.mc.field_1724.method_5814(CommandHClip.mc.field_1724.method_23317() + x, CommandHClip.mc.field_1724.method_23318(), CommandHClip.mc.field_1724.method_23321() + z);
            this.print("§a\u041a\u043b\u0438\u043f\u043d\u0443\u043b\u0438\u0441\u044c \u043d\u0430 " + String.format("%.1f", Math.abs(clampedH)) + " \u0431\u043b\u043e\u043a\u043e\u0432 " + ((clampedH > 0.0) ? "\u0432\u043f\u0435\u0440\u0451\u0434." : "\u043d\u0430\u0437\u0430\u0434."));
            return this.SINGLE_SUCCESS;
        }));
        builder.executes(context -> {
            this.print("\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435: hclip <\u0447\u0438\u0441\u043b\u043e> | hclip s");
            return this.SINGLE_SUCCESS;
        });
    }
}

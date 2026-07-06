/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.arguments.DoubleArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  net.minecraft.class_2172
 *  net.minecraft.class_2596
 *  net.minecraft.class_2828$class_2829
 *  net.minecraft.class_3532
 */
package sweetie.leonware.client.features.commands;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_2172;
import net.minecraft.class_2596;
import net.minecraft.class_2828;
import net.minecraft.class_3532;
import sweetie.leonware.api.command.Command;
import sweetie.leonware.api.command.CommandRegister;

@CommandRegister(name="hclip")
public class CommandHClip
extends Command {
    @Override
    public void execute(LiteralArgumentBuilder<class_2172> builder) {
        builder.then(CommandHClip.literal("s").executes(context -> {
            double h = 0.8;
            float f = CommandHClip.mc.field_1724.method_36454() * ((float)Math.PI / 180);
            double x = -((double)class_3532.method_15374((float)f) * h);
            double z = (double)class_3532.method_15362((float)f) * h;
            int de = (int)(Math.abs(h) * 9.953);
            for (int i = 0; i < Math.min(de, 19); ++i) {
                CommandHClip.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(CommandHClip.mc.field_1724.method_23317(), CommandHClip.mc.field_1724.method_23318(), CommandHClip.mc.field_1724.method_23321(), CommandHClip.mc.field_1724.method_24828(), CommandHClip.mc.field_1724.field_5976));
            }
            CommandHClip.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(CommandHClip.mc.field_1724.method_23317() + x, CommandHClip.mc.field_1724.method_23318(), CommandHClip.mc.field_1724.method_23321() + z, false, CommandHClip.mc.field_1724.field_5976));
            CommandHClip.mc.field_1724.method_5814(CommandHClip.mc.field_1724.method_23317() + x, CommandHClip.mc.field_1724.method_23318(), CommandHClip.mc.field_1724.method_23321() + z);
            this.print("\u00a7a\u041a\u043b\u0438\u043f\u043d\u0443\u043b\u0438\u0441\u044c \u043d\u0430 " + String.format("%.1f", h) + " \u0431\u043b\u043e\u043a\u043e\u0432 \u0432\u043f\u0435\u0440\u0451\u0434.");
            return this.SINGLE_SUCCESS;
        }));
        builder.then(CommandHClip.argument("count", DoubleArgumentType.doubleArg()).executes(context -> {
            double h = (Double)context.getArgument("count", Double.class);
            double clampedH = class_3532.method_15350((double)h, (double)-200.0, (double)200.0);
            float f = CommandHClip.mc.field_1724.method_36454() * ((float)Math.PI / 180);
            double x = -((double)class_3532.method_15374((float)f) * clampedH);
            double z = (double)class_3532.method_15362((float)f) * clampedH;
            int de = (int)(Math.abs(clampedH) * 9.953);
            for (int i = 0; i < Math.min(de, 19); ++i) {
                CommandHClip.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(CommandHClip.mc.field_1724.method_23317(), CommandHClip.mc.field_1724.method_23318(), CommandHClip.mc.field_1724.method_23321(), CommandHClip.mc.field_1724.method_24828(), CommandHClip.mc.field_1724.field_5976));
            }
            CommandHClip.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(CommandHClip.mc.field_1724.method_23317() + x, CommandHClip.mc.field_1724.method_23318(), CommandHClip.mc.field_1724.method_23321() + z, false, CommandHClip.mc.field_1724.field_5976));
            CommandHClip.mc.field_1724.method_5814(CommandHClip.mc.field_1724.method_23317() + x, CommandHClip.mc.field_1724.method_23318(), CommandHClip.mc.field_1724.method_23321() + z);
            this.print("\u00a7a\u041a\u043b\u0438\u043f\u043d\u0443\u043b\u0438\u0441\u044c \u043d\u0430 " + String.format("%.1f", Math.abs(clampedH)) + " \u0431\u043b\u043e\u043a\u043e\u0432 " + (clampedH > 0.0 ? "\u0432\u043f\u0435\u0440\u0451\u0434." : "\u043d\u0430\u0437\u0430\u0434."));
            return this.SINGLE_SUCCESS;
        }));
        builder.executes(context -> {
            this.print("\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435: hclip <\u0447\u0438\u0441\u043b\u043e> | hclip s");
            return this.SINGLE_SUCCESS;
        });
    }
}


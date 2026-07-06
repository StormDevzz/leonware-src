/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.arguments.DoubleArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  net.minecraft.class_2172
 *  net.minecraft.class_2246
 *  net.minecraft.class_2248
 *  net.minecraft.class_2338
 *  net.minecraft.class_2596
 *  net.minecraft.class_2828$class_2829
 *  net.minecraft.class_3532
 */
package sweetie.leonware.client.features.commands;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_2172;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2596;
import net.minecraft.class_2828;
import net.minecraft.class_3532;
import sweetie.leonware.api.command.Command;
import sweetie.leonware.api.command.CommandRegister;

@CommandRegister(name="vclip")
public class CommandVClip
extends Command {
    @Override
    public void execute(LiteralArgumentBuilder<class_2172> builder) {
        builder.then(CommandVClip.literal("down").executes(context -> {
            double y = 0.0;
            int x = (int)Math.floor(CommandVClip.mc.field_1724.method_23317());
            int z = (int)Math.floor(CommandVClip.mc.field_1724.method_23321());
            for (int yS = 0; yS < (int)CommandVClip.mc.field_1724.method_23318(); ++yS) {
                if (!this.isPassable(x, yS, z) || this.isPassable(x, yS - 1, z) || this.isPassable(x, yS - 2, z)) continue;
                y = (double)(yS - 2) - CommandVClip.mc.field_1724.method_23318() + 0.2;
            }
            if (y < -CommandVClip.mc.field_1724.method_23318()) {
                this.print("\u00a7c\u041d\u0435\u0442 \u0441\u0432\u043e\u0431\u043e\u0434\u043d\u043e\u0433\u043e \u043c\u0435\u0441\u0442\u0430!");
                return this.SINGLE_SUCCESS;
            }
            this.clip(y);
            this.print("\u00a7a\u041a\u043b\u0438\u043f\u043d\u0443\u043b\u0438\u0441\u044c \u043d\u0430 " + String.format("%.1f", Math.abs(y)) + " \u0431\u043b\u043e\u043a\u043e\u0432 \u0432\u043d\u0438\u0437.");
            return this.SINGLE_SUCCESS;
        }));
        builder.then(CommandVClip.literal("up").executes(context -> {
            double y = 0.0;
            int verticalRange = 200;
            for (float i = 0.0f; i < (float)verticalRange; i += 0.005f) {
                class_2338 pos = class_2338.method_49637((double)CommandVClip.mc.field_1724.method_23317(), (double)(CommandVClip.mc.field_1724.method_23318() + (double)i + 1.0), (double)CommandVClip.mc.field_1724.method_23321());
                class_2338 posBelow = class_2338.method_49637((double)CommandVClip.mc.field_1724.method_23317(), (double)(CommandVClip.mc.field_1724.method_23318() + (double)i - 0.002), (double)CommandVClip.mc.field_1724.method_23321());
                if (!this.isPassable(CommandVClip.mc.field_1724.method_23317(), CommandVClip.mc.field_1724.method_23318() + (double)i + 1.0, CommandVClip.mc.field_1724.method_23321()) || !this.isPassable(CommandVClip.mc.field_1724.method_23317(), CommandVClip.mc.field_1724.method_23318() + (double)i + 0.005, CommandVClip.mc.field_1724.method_23321()) || CommandVClip.mc.field_1687.method_8320(posBelow).method_26204() == class_2246.field_10124 || !(i > 2.0f) || CommandVClip.mc.field_1687.method_8320(class_2338.method_49637((double)CommandVClip.mc.field_1724.method_23317(), (double)(CommandVClip.mc.field_1724.method_23318() - 0.1), (double)CommandVClip.mc.field_1724.method_23321())).method_26204() == class_2246.field_10382) continue;
                y = (double)i + 0.01;
                if (CommandVClip.mc.field_1687.method_8320(class_2338.method_49637((double)CommandVClip.mc.field_1724.method_23317(), (double)(CommandVClip.mc.field_1724.method_23318() + y - 0.49), (double)CommandVClip.mc.field_1724.method_23321())).method_26204() != class_2246.field_10382) break;
                y -= 1.0;
                break;
            }
            this.clip(y);
            this.print("\u00a7a\u041a\u043b\u0438\u043f\u043d\u0443\u043b\u0438\u0441\u044c \u043d\u0430 " + String.format("%.1f", y) + " \u0431\u043b\u043e\u043a\u043e\u0432 \u0432\u0432\u0435\u0440\u0445.");
            return this.SINGLE_SUCCESS;
        }));
        builder.then(CommandVClip.argument("count", DoubleArgumentType.doubleArg()).executes(context -> {
            double y = class_3532.method_15350((double)((Double)context.getArgument("count", Double.class)), (double)-200.0, (double)200.0);
            this.clip(y);
            this.print("\u00a7a\u041a\u043b\u0438\u043f\u043d\u0443\u043b\u0438\u0441\u044c \u043d\u0430 " + String.format("%.1f", Math.abs(y)) + " \u0431\u043b\u043e\u043a\u043e\u0432 " + (y > 0.0 ? "\u0432\u0432\u0435\u0440\u0445." : "\u0432\u043d\u0438\u0437."));
            return this.SINGLE_SUCCESS;
        }));
        builder.executes(context -> {
            this.print("\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435: vclip <\u0447\u0438\u0441\u043b\u043e> | vclip up | vclip down");
            return this.SINGLE_SUCCESS;
        });
    }

    private void clip(double y) {
        int de = (int)(Math.abs(y) / 10.0);
        for (int i = 0; i < Math.min(de, 19); ++i) {
            CommandVClip.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(CommandVClip.mc.field_1724.method_23317(), CommandVClip.mc.field_1724.method_23318(), CommandVClip.mc.field_1724.method_23321(), CommandVClip.mc.field_1724.method_24828(), CommandVClip.mc.field_1724.field_5976));
        }
        CommandVClip.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(CommandVClip.mc.field_1724.method_23317(), CommandVClip.mc.field_1724.method_23318() + y, CommandVClip.mc.field_1724.method_23321(), false, CommandVClip.mc.field_1724.field_5976));
        CommandVClip.mc.field_1724.method_5814(CommandVClip.mc.field_1724.method_23317(), CommandVClip.mc.field_1724.method_23318() + y, CommandVClip.mc.field_1724.method_23321());
        if (Math.abs(y) > 100.0) {
            CommandVClip.mc.field_1769.method_3279();
        }
    }

    private boolean isPassable(double x, double y, double z) {
        class_2338 pos = class_2338.method_49637((double)x, (double)y, (double)z);
        class_2248 block = CommandVClip.mc.field_1687.method_8320(pos).method_26204();
        return block == class_2246.field_10124 || block == class_2246.field_10382 || block == class_2246.field_10164 || block == class_2246.field_10343 || block == class_2246.field_10453;
    }

    private boolean isPassable(int x, int y, int z) {
        return this.isPassable((double)x, (double)y, (double)z);
    }
}


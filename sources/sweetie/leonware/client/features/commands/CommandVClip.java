package sweetie.leonware.client.features.commands;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_2172;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2828;
import net.minecraft.class_3532;
import sweetie.leonware.api.command.Command;
import sweetie.leonware.api.command.CommandRegister;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/commands/CommandVClip.class */
@CommandRegister(name = "vclip")
public class CommandVClip extends Command {
    @Override // sweetie.leonware.api.command.Command
    public void execute(LiteralArgumentBuilder<class_2172> builder) {
        builder.then(literal("down").executes(context -> {
            double y = 0.0d;
            int x = (int) Math.floor(mc.field_1724.method_23317());
            int z = (int) Math.floor(mc.field_1724.method_23321());
            for (int yS = 0; yS < ((int) mc.field_1724.method_23318()); yS++) {
                if (isPassable(x, yS, z) && !isPassable(x, yS - 1, z) && !isPassable(x, yS - 2, z)) {
                    y = (((double) (yS - 2)) - mc.field_1724.method_23318()) + 0.2d;
                }
            }
            if (y < (-mc.field_1724.method_23318())) {
                print("§cНет свободного места!");
                return this.SINGLE_SUCCESS;
            }
            clip(y);
            print("§aКлипнулись на " + String.format("%.1f", Double.valueOf(Math.abs(y))) + " блоков вниз.");
            return this.SINGLE_SUCCESS;
        }));
        builder.then(literal("up").executes(context2 -> {
            double y = 0.0d;
            float f = 0.0f;
            while (true) {
                float i = f;
                if (i >= 200) {
                    break;
                }
                class_2338.method_49637(mc.field_1724.method_23317(), mc.field_1724.method_23318() + ((double) i) + 1.0d, mc.field_1724.method_23321());
                class_2338 posBelow = class_2338.method_49637(mc.field_1724.method_23317(), (mc.field_1724.method_23318() + ((double) i)) - 0.002d, mc.field_1724.method_23321());
                if (!isPassable(mc.field_1724.method_23317(), mc.field_1724.method_23318() + ((double) i) + 1.0d, mc.field_1724.method_23321()) || !isPassable(mc.field_1724.method_23317(), mc.field_1724.method_23318() + ((double) i) + 0.005d, mc.field_1724.method_23321()) || mc.field_1687.method_8320(posBelow).method_26204() == class_2246.field_10124 || i <= 2.0f || mc.field_1687.method_8320(class_2338.method_49637(mc.field_1724.method_23317(), mc.field_1724.method_23318() - 0.1d, mc.field_1724.method_23321())).method_26204() == class_2246.field_10382) {
                    f = i + 0.005f;
                } else {
                    y = ((double) i) + 0.01d;
                    if (mc.field_1687.method_8320(class_2338.method_49637(mc.field_1724.method_23317(), (mc.field_1724.method_23318() + y) - 0.49d, mc.field_1724.method_23321())).method_26204() == class_2246.field_10382) {
                        y -= 1.0d;
                    }
                }
            }
            clip(y);
            print("§aКлипнулись на " + String.format("%.1f", Double.valueOf(y)) + " блоков вверх.");
            return this.SINGLE_SUCCESS;
        }));
        builder.then(argument("count", DoubleArgumentType.doubleArg()).executes(context3 -> {
            double y = class_3532.method_15350(((Double) context3.getArgument("count", Double.class)).doubleValue(), -200.0d, 200.0d);
            clip(y);
            print("§aКлипнулись на " + String.format("%.1f", Double.valueOf(Math.abs(y))) + " блоков " + (y > 0.0d ? "вверх." : "вниз."));
            return this.SINGLE_SUCCESS;
        }));
        builder.executes(context4 -> {
            print("Использование: vclip <число> | vclip up | vclip down");
            return this.SINGLE_SUCCESS;
        });
    }

    private void clip(double y) {
        int de = (int) (Math.abs(y) / 10.0d);
        for (int i = 0; i < Math.min(de, 19); i++) {
            mc.field_1724.field_3944.method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), mc.field_1724.method_24828(), mc.field_1724.field_5976));
        }
        mc.field_1724.field_3944.method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + y, mc.field_1724.method_23321(), false, mc.field_1724.field_5976));
        mc.field_1724.method_5814(mc.field_1724.method_23317(), mc.field_1724.method_23318() + y, mc.field_1724.method_23321());
        if (Math.abs(y) > 100.0d) {
            mc.field_1769.method_3279();
        }
    }

    private boolean isPassable(double x, double y, double z) {
        class_2338 pos = class_2338.method_49637(x, y, z);
        class_2248 block = mc.field_1687.method_8320(pos).method_26204();
        return block == class_2246.field_10124 || block == class_2246.field_10382 || block == class_2246.field_10164 || block == class_2246.field_10343 || block == class_2246.field_10453;
    }

    private boolean isPassable(int x, int y, int z) {
        return isPassable(x, y, z);
    }
}

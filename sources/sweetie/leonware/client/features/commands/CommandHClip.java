package sweetie.leonware.client.features.commands;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_2172;
import net.minecraft.class_2828;
import net.minecraft.class_3532;
import sweetie.leonware.api.command.Command;
import sweetie.leonware.api.command.CommandRegister;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/commands/CommandHClip.class */
@CommandRegister(name = "hclip")
public class CommandHClip extends Command {
    @Override // sweetie.leonware.api.command.Command
    public void execute(LiteralArgumentBuilder<class_2172> builder) {
        builder.then(literal("s").executes(context -> {
            float f = mc.field_1724.method_36454() * 0.017453292f;
            double x = -(((double) class_3532.method_15374(f)) * 0.8d);
            double z = ((double) class_3532.method_15362(f)) * 0.8d;
            int de = (int) (Math.abs(0.8d) * 9.953d);
            for (int i = 0; i < Math.min(de, 19); i++) {
                mc.field_1724.field_3944.method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), mc.field_1724.method_24828(), mc.field_1724.field_5976));
            }
            mc.field_1724.field_3944.method_52787(new class_2828.class_2829(mc.field_1724.method_23317() + x, mc.field_1724.method_23318(), mc.field_1724.method_23321() + z, false, mc.field_1724.field_5976));
            mc.field_1724.method_5814(mc.field_1724.method_23317() + x, mc.field_1724.method_23318(), mc.field_1724.method_23321() + z);
            print("§aКлипнулись на " + String.format("%.1f", Double.valueOf(0.8d)) + " блоков вперёд.");
            return this.SINGLE_SUCCESS;
        }));
        builder.then(argument("count", DoubleArgumentType.doubleArg()).executes(context2 -> {
            double h = ((Double) context2.getArgument("count", Double.class)).doubleValue();
            double clampedH = class_3532.method_15350(h, -200.0d, 200.0d);
            float f = mc.field_1724.method_36454() * 0.017453292f;
            double x = -(((double) class_3532.method_15374(f)) * clampedH);
            double z = ((double) class_3532.method_15362(f)) * clampedH;
            int de = (int) (Math.abs(clampedH) * 9.953d);
            for (int i = 0; i < Math.min(de, 19); i++) {
                mc.field_1724.field_3944.method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), mc.field_1724.method_24828(), mc.field_1724.field_5976));
            }
            mc.field_1724.field_3944.method_52787(new class_2828.class_2829(mc.field_1724.method_23317() + x, mc.field_1724.method_23318(), mc.field_1724.method_23321() + z, false, mc.field_1724.field_5976));
            mc.field_1724.method_5814(mc.field_1724.method_23317() + x, mc.field_1724.method_23318(), mc.field_1724.method_23321() + z);
            print("§aКлипнулись на " + String.format("%.1f", Double.valueOf(Math.abs(clampedH))) + " блоков " + (clampedH > 0.0d ? "вперёд." : "назад."));
            return this.SINGLE_SUCCESS;
        }));
        builder.executes(context3 -> {
            print("Использование: hclip <число> | hclip s");
            return this.SINGLE_SUCCESS;
        });
    }
}

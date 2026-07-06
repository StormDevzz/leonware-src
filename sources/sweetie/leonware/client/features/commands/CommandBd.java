package sweetie.leonware.client.features.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_1690;
import net.minecraft.class_2172;
import net.minecraft.class_2828;
import sweetie.leonware.api.command.Command;
import sweetie.leonware.api.command.CommandRegister;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/commands/CommandBd.class */
@CommandRegister(name = "bd")
public class CommandBd extends Command {
    @Override // sweetie.leonware.api.command.Command
    public void execute(LiteralArgumentBuilder<class_2172> builder) {
        builder.executes(context -> {
            if (mc.field_1687 == null || mc.field_1724 == null) {
                print("§cМир или игрок не найден!");
                return this.SINGLE_SUCCESS;
            }
            double y = (((double) Math.round(((float) ((((double) (-mc.field_1724.method_17682())) - mc.field_1724.method_23318()) - 0.009999999776482582d)) * 100.0f)) / 100.0d) + 0.15000000596046448d;
            if (y > 0.0d) {
                print("§cНевозможно телепортироваться под бедрок!");
                return this.SINGLE_SUCCESS;
            }
            int packets = (int) Math.min(Math.abs(y / 10.0d) + 1.0d, 19.0d);
            for (int i = 0; i < packets; i++) {
                mc.field_1724.field_3944.method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), false, mc.field_1724.field_5976));
            }
            mc.field_1724.field_3944.method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + y, mc.field_1724.method_23321(), false, mc.field_1724.field_5976));
            mc.field_1724.method_5814(mc.field_1724.method_23317(), mc.field_1724.method_23318() + y, mc.field_1724.method_23321());
            print("§aТелепортирован на " + String.format("%.1f", Double.valueOf(Math.abs(y))) + " блоков вниз.");
            return this.SINGLE_SUCCESS;
        });
        builder.then(LiteralArgumentBuilder.literal("boat").executes(context2 -> {
            if (mc.field_1687 == null || mc.field_1724 == null) {
                print("§cМир или игрок не найден!");
                return this.SINGLE_SUCCESS;
            }
            if (!mc.field_1724.method_5765() || !(mc.field_1724.method_5854() instanceof class_1690)) {
                print("§cВы должны находиться в лодке!");
                return this.SINGLE_SUCCESS;
            }
            double y = (((double) Math.round(((float) ((((double) (-mc.field_1724.method_17682())) - mc.field_1724.method_23318()) - 0.009999999776482582d)) * 100.0f)) / 100.0d) + 0.15000000596046448d;
            if (y > 0.0d) {
                print("§cНевозможно телепортироваться под бедрок!");
                return this.SINGLE_SUCCESS;
            }
            int packets = (int) Math.min(Math.abs(y / 10.0d) + 1.0d, 19.0d);
            for (int i = 0; i < packets; i++) {
                mc.field_1724.field_3944.method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), false, mc.field_1724.field_5976));
            }
            mc.field_1724.field_3944.method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + y, mc.field_1724.method_23321(), false, mc.field_1724.field_5976));
            mc.field_1724.method_5814(mc.field_1724.method_23317(), mc.field_1724.method_23318() + y, mc.field_1724.method_23321());
            mc.field_1724.method_5848();
            mc.field_1724.method_29239();
            print("§aТелепортирован под бедрок с лодки на " + String.format("%.1f", Double.valueOf(Math.abs(y))) + " блоков вниз.");
            return this.SINGLE_SUCCESS;
        }));
    }
}

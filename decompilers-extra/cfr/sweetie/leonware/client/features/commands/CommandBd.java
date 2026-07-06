/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  net.minecraft.class_1690
 *  net.minecraft.class_2172
 *  net.minecraft.class_2596
 *  net.minecraft.class_2828$class_2829
 */
package sweetie.leonware.client.features.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_1690;
import net.minecraft.class_2172;
import net.minecraft.class_2596;
import net.minecraft.class_2828;
import sweetie.leonware.api.command.Command;
import sweetie.leonware.api.command.CommandRegister;

@CommandRegister(name="bd")
public class CommandBd
extends Command {
    @Override
    public void execute(LiteralArgumentBuilder<class_2172> builder) {
        builder.executes(context -> {
            if (CommandBd.mc.field_1687 == null || CommandBd.mc.field_1724 == null) {
                this.print("\u00a7c\u041c\u0438\u0440 \u0438\u043b\u0438 \u0438\u0433\u0440\u043e\u043a \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d!");
                return this.SINGLE_SUCCESS;
            }
            double y = (double)Math.round((float)((double)(-CommandBd.mc.field_1724.method_17682()) - CommandBd.mc.field_1724.method_23318() - (double)0.01f) * 100.0f) / 100.0;
            if ((y += (double)0.15f) > 0.0) {
                this.print("\u00a7c\u041d\u0435\u0432\u043e\u0437\u043c\u043e\u0436\u043d\u043e \u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c\u0441\u044f \u043f\u043e\u0434 \u0431\u0435\u0434\u0440\u043e\u043a!");
                return this.SINGLE_SUCCESS;
            }
            int packets = (int)Math.min(Math.abs(y / 10.0) + 1.0, 19.0);
            for (int i = 0; i < packets; ++i) {
                CommandBd.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(CommandBd.mc.field_1724.method_23317(), CommandBd.mc.field_1724.method_23318(), CommandBd.mc.field_1724.method_23321(), false, CommandBd.mc.field_1724.field_5976));
            }
            CommandBd.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(CommandBd.mc.field_1724.method_23317(), CommandBd.mc.field_1724.method_23318() + y, CommandBd.mc.field_1724.method_23321(), false, CommandBd.mc.field_1724.field_5976));
            CommandBd.mc.field_1724.method_5814(CommandBd.mc.field_1724.method_23317(), CommandBd.mc.field_1724.method_23318() + y, CommandBd.mc.field_1724.method_23321());
            this.print("\u00a7a\u0422\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u043d \u043d\u0430 " + String.format("%.1f", Math.abs(y)) + " \u0431\u043b\u043e\u043a\u043e\u0432 \u0432\u043d\u0438\u0437.");
            return this.SINGLE_SUCCESS;
        });
        builder.then(LiteralArgumentBuilder.literal((String)"boat").executes(context -> {
            if (CommandBd.mc.field_1687 == null || CommandBd.mc.field_1724 == null) {
                this.print("\u00a7c\u041c\u0438\u0440 \u0438\u043b\u0438 \u0438\u0433\u0440\u043e\u043a \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d!");
                return this.SINGLE_SUCCESS;
            }
            if (!CommandBd.mc.field_1724.method_5765() || !(CommandBd.mc.field_1724.method_5854() instanceof class_1690)) {
                this.print("\u00a7c\u0412\u044b \u0434\u043e\u043b\u0436\u043d\u044b \u043d\u0430\u0445\u043e\u0434\u0438\u0442\u044c\u0441\u044f \u0432 \u043b\u043e\u0434\u043a\u0435!");
                return this.SINGLE_SUCCESS;
            }
            double y = (double)Math.round((float)((double)(-CommandBd.mc.field_1724.method_17682()) - CommandBd.mc.field_1724.method_23318() - (double)0.01f) * 100.0f) / 100.0;
            if ((y += (double)0.15f) > 0.0) {
                this.print("\u00a7c\u041d\u0435\u0432\u043e\u0437\u043c\u043e\u0436\u043d\u043e \u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c\u0441\u044f \u043f\u043e\u0434 \u0431\u0435\u0434\u0440\u043e\u043a!");
                return this.SINGLE_SUCCESS;
            }
            int packets = (int)Math.min(Math.abs(y / 10.0) + 1.0, 19.0);
            for (int i = 0; i < packets; ++i) {
                CommandBd.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(CommandBd.mc.field_1724.method_23317(), CommandBd.mc.field_1724.method_23318(), CommandBd.mc.field_1724.method_23321(), false, CommandBd.mc.field_1724.field_5976));
            }
            CommandBd.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(CommandBd.mc.field_1724.method_23317(), CommandBd.mc.field_1724.method_23318() + y, CommandBd.mc.field_1724.method_23321(), false, CommandBd.mc.field_1724.field_5976));
            CommandBd.mc.field_1724.method_5814(CommandBd.mc.field_1724.method_23317(), CommandBd.mc.field_1724.method_23318() + y, CommandBd.mc.field_1724.method_23321());
            CommandBd.mc.field_1724.method_5848();
            CommandBd.mc.field_1724.method_29239();
            this.print("\u00a7a\u0422\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u043d \u043f\u043e\u0434 \u0431\u0435\u0434\u0440\u043e\u043a \u0441 \u043b\u043e\u0434\u043a\u0438 \u043d\u0430 " + String.format("%.1f", Math.abs(y)) + " \u0431\u043b\u043e\u043a\u043e\u0432 \u0432\u043d\u0438\u0437.");
            return this.SINGLE_SUCCESS;
        }));
    }
}


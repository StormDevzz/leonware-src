/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  net.minecraft.class_2172
 */
package sweetie.leonware.client.features.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_2172;
import sweetie.leonware.api.command.Command;
import sweetie.leonware.api.command.CommandRegister;
import sweetie.leonware.api.system.configs.StaffManager;
import sweetie.leonware.client.features.commands.arguments.AnyNameArgument;
import sweetie.leonware.client.features.commands.arguments.StrictlyStaffArgument;

@CommandRegister(name="staffs")
public class CommandStaffs
extends Command {
    @Override
    public void execute(LiteralArgumentBuilder<class_2172> builder) {
        builder.then(CommandStaffs.literal("clear").executes(context -> {
            if (!StaffManager.getInstance().getData().isEmpty()) {
                StaffManager.getInstance().clear();
                this.print("\u0421\u043f\u0438\u0441\u043e\u043a \u043b\u043e\u0445\u043e\u0432 \u043e\u0447\u0438\u0449\u0435\u043d.");
            } else {
                this.print("\u0421\u043f\u0438\u0441\u043e\u043a \u043b\u043e\u0445\u043e\u0432 \u043f\u0443\u0441\u0442.");
            }
            return this.SINGLE_SUCCESS;
        }));
        builder.then(CommandStaffs.literal("list").executes(context -> {
            if (StaffManager.getInstance().getData().isEmpty()) {
                this.print("\u0421\u043f\u0438\u0441\u043e\u043a \u043b\u043e\u0445\u043e\u0432 \u043f\u0443\u0441\u0442.");
            } else {
                try {
                    String staffs = String.join((CharSequence)", ", StaffManager.getInstance().getData());
                    this.print("\u041b\u043e\u0445\u0438: " + staffs);
                }
                catch (Exception e) {
                    this.print("\u041f\u0440\u043e\u0438\u0437\u043e\u0448\u043b\u0430 \u043e\u0448\u0438\u0431\u043a\u0430 \u043f\u0440\u0438 \u043f\u043e\u043b\u0443\u0447\u0435\u043d\u0438\u0438 \u0441\u043f\u0438\u0441\u043a\u0430 \u043b\u043e\u0445\u043e\u0432!");
                }
            }
            return this.SINGLE_SUCCESS;
        }));
        builder.then(CommandStaffs.literal("add").then(CommandStaffs.argument("player", AnyNameArgument.create()).executes(context -> {
            String nickname = (String)context.getArgument("player", String.class);
            if (StaffManager.getInstance().contains(nickname)) {
                this.print("\u0423\u0436\u0435 \u0435\u0441\u0442\u044c \u0432 \u0441\u043f\u0438\u0441\u043a\u0435 \u043b\u043e\u0445\u043e\u0432!");
            } else {
                StaffManager.getInstance().add(nickname);
                this.print(nickname + " \u0434\u043e\u0431\u0430\u0432\u043b\u0435\u043d \u0432 \u0441\u043f\u0438\u0441\u043e\u043a \u043b\u043e\u0445\u043e\u0432.");
            }
            return this.SINGLE_SUCCESS;
        })));
        builder.then(CommandStaffs.literal("remove").then(CommandStaffs.argument("player", StrictlyStaffArgument.create()).executes(context -> {
            String nickname = (String)context.getArgument("player", String.class);
            if (!StaffManager.getInstance().contains(nickname)) {
                this.print("\u041d\u0435\u0442 \u0442\u0430\u043a\u043e\u0433\u043e " + nickname + "!");
            } else {
                StaffManager.getInstance().remove(nickname);
                this.print(nickname + " \u0443\u0434\u0430\u043b\u0435\u043d \u0438\u0437 \u0441\u043f\u0438\u0441\u043a\u0430 \u043b\u043e\u0445\u043e\u0432.");
            }
            return this.SINGLE_SUCCESS;
        })));
    }
}


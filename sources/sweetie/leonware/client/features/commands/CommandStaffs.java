package sweetie.leonware.client.features.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_2172;
import sweetie.leonware.api.command.Command;
import sweetie.leonware.api.command.CommandRegister;
import sweetie.leonware.api.system.configs.StaffManager;
import sweetie.leonware.client.features.commands.arguments.AnyNameArgument;
import sweetie.leonware.client.features.commands.arguments.StrictlyStaffArgument;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/commands/CommandStaffs.class */
@CommandRegister(name = "staffs")
public class CommandStaffs extends Command {
    @Override // sweetie.leonware.api.command.Command
    public void execute(LiteralArgumentBuilder<class_2172> builder) {
        builder.then(literal("clear").executes(context -> {
            if (!StaffManager.getInstance().getData().isEmpty()) {
                StaffManager.getInstance().clear();
                print("Список лохов очищен.");
            } else {
                print("Список лохов пуст.");
            }
            return this.SINGLE_SUCCESS;
        }));
        builder.then(literal("list").executes(context2 -> {
            if (StaffManager.getInstance().getData().isEmpty()) {
                print("Список лохов пуст.");
            } else {
                try {
                    String staffs = String.join(", ", StaffManager.getInstance().getData());
                    print("Лохи: " + staffs);
                } catch (Exception e) {
                    print("Произошла ошибка при получении списка лохов!");
                }
            }
            return this.SINGLE_SUCCESS;
        }));
        builder.then(literal("add").then(argument("player", AnyNameArgument.create()).executes(context3 -> {
            String nickname = (String) context3.getArgument("player", String.class);
            if (StaffManager.getInstance().contains(nickname)) {
                print("Уже есть в списке лохов!");
            } else {
                StaffManager.getInstance().add(nickname);
                print(nickname + " добавлен в список лохов.");
            }
            return this.SINGLE_SUCCESS;
        })));
        builder.then(literal("remove").then(argument("player", StrictlyStaffArgument.create()).executes(context4 -> {
            String nickname = (String) context4.getArgument("player", String.class);
            if (!StaffManager.getInstance().contains(nickname)) {
                print("Нет такого " + nickname + "!");
            } else {
                StaffManager.getInstance().remove(nickname);
                print(nickname + " удален из списка лохов.");
            }
            return this.SINGLE_SUCCESS;
        })));
    }
}

package sweetie.leonware.client.features.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_2172;
import sweetie.leonware.api.command.Command;
import sweetie.leonware.api.command.CommandRegister;
import sweetie.leonware.api.system.configs.FriendManager;
import sweetie.leonware.client.features.commands.arguments.AnyNameArgument;
import sweetie.leonware.client.features.commands.arguments.StrictlyFriendArgument;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/commands/CommandFriend.class */
@CommandRegister(name = "friend")
public class CommandFriend extends Command {
    @Override // sweetie.leonware.api.command.Command
    public void execute(LiteralArgumentBuilder<class_2172> builder) {
        builder.then(literal("clear").executes(context -> {
            if (!FriendManager.getInstance().getData().isEmpty()) {
                FriendManager.getInstance().clear();
                print("Список друзей очищен.");
            } else {
                print("У тебя нет друзей.");
            }
            return this.SINGLE_SUCCESS;
        }));
        builder.then(literal("list").executes(context2 -> {
            if (FriendManager.getInstance().getData().isEmpty()) {
                print("У тебя нет друзей.");
            } else {
                try {
                    String friends = String.join(", ", FriendManager.getInstance().getData());
                    print("Друзья: " + friends);
                } catch (Exception e) {
                    print("Произошла ошибка при получении списка друзей.");
                }
            }
            return this.SINGLE_SUCCESS;
        }));
        builder.then(literal("add").then(argument("player", AnyNameArgument.create()).executes(context3 -> {
            String nickname = (String) context3.getArgument("player", String.class);
            if (FriendManager.getInstance().contains(nickname)) {
                print(nickname + "уже есть в друзьях.");
            } else {
                FriendManager.getInstance().add(nickname);
                print(nickname + " добавлен в друзья.");
            }
            return this.SINGLE_SUCCESS;
        })));
        builder.then(literal("remove").then(argument("player", StrictlyFriendArgument.create()).executes(context4 -> {
            String nickname = (String) context4.getArgument("player", String.class);
            if (!FriendManager.getInstance().contains(nickname)) {
                print("Вы еще не дружили с " + nickname + ".");
            } else {
                FriendManager.getInstance().remove(nickname);
                print(nickname + " удален из друзей.");
            }
            return this.SINGLE_SUCCESS;
        })));
    }
}

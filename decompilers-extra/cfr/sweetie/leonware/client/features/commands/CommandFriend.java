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
import sweetie.leonware.api.system.configs.FriendManager;
import sweetie.leonware.client.features.commands.arguments.AnyNameArgument;
import sweetie.leonware.client.features.commands.arguments.StrictlyFriendArgument;

@CommandRegister(name="friend")
public class CommandFriend
extends Command {
    @Override
    public void execute(LiteralArgumentBuilder<class_2172> builder) {
        builder.then(CommandFriend.literal("clear").executes(context -> {
            if (!FriendManager.getInstance().getData().isEmpty()) {
                FriendManager.getInstance().clear();
                this.print("\u0421\u043f\u0438\u0441\u043e\u043a \u0434\u0440\u0443\u0437\u0435\u0439 \u043e\u0447\u0438\u0449\u0435\u043d.");
            } else {
                this.print("\u0423 \u0442\u0435\u0431\u044f \u043d\u0435\u0442 \u0434\u0440\u0443\u0437\u0435\u0439.");
            }
            return this.SINGLE_SUCCESS;
        }));
        builder.then(CommandFriend.literal("list").executes(context -> {
            if (FriendManager.getInstance().getData().isEmpty()) {
                this.print("\u0423 \u0442\u0435\u0431\u044f \u043d\u0435\u0442 \u0434\u0440\u0443\u0437\u0435\u0439.");
            } else {
                try {
                    String friends = String.join((CharSequence)", ", FriendManager.getInstance().getData());
                    this.print("\u0414\u0440\u0443\u0437\u044c\u044f: " + friends);
                }
                catch (Exception e) {
                    this.print("\u041f\u0440\u043e\u0438\u0437\u043e\u0448\u043b\u0430 \u043e\u0448\u0438\u0431\u043a\u0430 \u043f\u0440\u0438 \u043f\u043e\u043b\u0443\u0447\u0435\u043d\u0438\u0438 \u0441\u043f\u0438\u0441\u043a\u0430 \u0434\u0440\u0443\u0437\u0435\u0439.");
                }
            }
            return this.SINGLE_SUCCESS;
        }));
        builder.then(CommandFriend.literal("add").then(CommandFriend.argument("player", AnyNameArgument.create()).executes(context -> {
            String nickname = (String)context.getArgument("player", String.class);
            if (FriendManager.getInstance().contains(nickname)) {
                this.print(nickname + "\u0443\u0436\u0435 \u0435\u0441\u0442\u044c \u0432 \u0434\u0440\u0443\u0437\u044c\u044f\u0445.");
            } else {
                FriendManager.getInstance().add(nickname);
                this.print(nickname + " \u0434\u043e\u0431\u0430\u0432\u043b\u0435\u043d \u0432 \u0434\u0440\u0443\u0437\u044c\u044f.");
            }
            return this.SINGLE_SUCCESS;
        })));
        builder.then(CommandFriend.literal("remove").then(CommandFriend.argument("player", StrictlyFriendArgument.create()).executes(context -> {
            String nickname = (String)context.getArgument("player", String.class);
            if (!FriendManager.getInstance().contains(nickname)) {
                this.print("\u0412\u044b \u0435\u0449\u0435 \u043d\u0435 \u0434\u0440\u0443\u0436\u0438\u043b\u0438 \u0441 " + nickname + ".");
            } else {
                FriendManager.getInstance().remove(nickname);
                this.print(nickname + " \u0443\u0434\u0430\u043b\u0435\u043d \u0438\u0437 \u0434\u0440\u0443\u0437\u0435\u0439.");
            }
            return this.SINGLE_SUCCESS;
        })));
    }
}


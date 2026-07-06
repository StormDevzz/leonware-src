// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import sweetie.leonware.api.system.configs.FriendManager;
import com.mojang.brigadier.context.CommandContext;
import sweetie.leonware.client.features.commands.arguments.StrictlyFriendArgument;
import com.mojang.brigadier.arguments.ArgumentType;
import sweetie.leonware.client.features.commands.arguments.AnyNameArgument;
import net.minecraft.class_2172;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import sweetie.leonware.api.command.CommandRegister;
import sweetie.leonware.api.command.Command;

@CommandRegister(name = "friend")
public class CommandFriend extends Command
{
    @Override
    public void execute(final LiteralArgumentBuilder<class_2172> builder) {
        builder.then(Command.literal("clear").executes(context -> {
            if (!FriendManager.getInstance().getData().isEmpty()) {
                FriendManager.getInstance().clear();
                this.print("\u0421\u043f\u0438\u0441\u043e\u043a \u0434\u0440\u0443\u0437\u0435\u0439 \u043e\u0447\u0438\u0449\u0435\u043d.");
            }
            else {
                this.print("\u0423 \u0442\u0435\u0431\u044f \u043d\u0435\u0442 \u0434\u0440\u0443\u0437\u0435\u0439.");
            }
            return this.SINGLE_SUCCESS;
        }));
        builder.then(Command.literal("list").executes(context -> {
            if (FriendManager.getInstance().getData().isEmpty()) {
                this.print("\u0423 \u0442\u0435\u0431\u044f \u043d\u0435\u0442 \u0434\u0440\u0443\u0437\u0435\u0439.");
            }
            else {
                try {
                    final String friends = String.join(", ", FriendManager.getInstance().getData());
                    this.print("\u0414\u0440\u0443\u0437\u044c\u044f: " + friends);
                }
                catch (final Exception e) {
                    this.print("\u041f\u0440\u043e\u0438\u0437\u043e\u0448\u043b\u0430 \u043e\u0448\u0438\u0431\u043a\u0430 \u043f\u0440\u0438 \u043f\u043e\u043b\u0443\u0447\u0435\u043d\u0438\u0438 \u0441\u043f\u0438\u0441\u043a\u0430 \u0434\u0440\u0443\u0437\u0435\u0439.");
                }
            }
            return this.SINGLE_SUCCESS;
        }));
        builder.then(Command.literal("add").then(Command.argument("player", (com.mojang.brigadier.arguments.ArgumentType<Object>)AnyNameArgument.create()).executes(context -> {
            final String nickname = (String)context.getArgument("player", (Class)String.class);
            if (FriendManager.getInstance().contains(nickname)) {
                this.print(nickname + "\u0443\u0436\u0435 \u0435\u0441\u0442\u044c \u0432 \u0434\u0440\u0443\u0437\u044c\u044f\u0445.");
            }
            else {
                FriendManager.getInstance().add(nickname);
                this.print(nickname + " \u0434\u043e\u0431\u0430\u0432\u043b\u0435\u043d \u0432 \u0434\u0440\u0443\u0437\u044c\u044f.");
            }
            return this.SINGLE_SUCCESS;
        })));
        builder.then(Command.literal("remove").then(Command.argument("player", (com.mojang.brigadier.arguments.ArgumentType<Object>)StrictlyFriendArgument.create()).executes(context -> {
            final String nickname = (String)context.getArgument("player", (Class)String.class);
            if (!FriendManager.getInstance().contains(nickname)) {
                this.print("\u0412\u044b \u0435\u0449\u0435 \u043d\u0435 \u0434\u0440\u0443\u0436\u0438\u043b\u0438 \u0441 " + nickname);
            }
            else {
                FriendManager.getInstance().remove(nickname);
                this.print(nickname + " \u0443\u0434\u0430\u043b\u0435\u043d \u0438\u0437 \u0434\u0440\u0443\u0437\u0435\u0439.");
            }
            return this.SINGLE_SUCCESS;
        })));
    }
}

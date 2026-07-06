package sweetie.leonware.client.features.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_2172;
import sweetie.leonware.api.command.Command;
import sweetie.leonware.api.command.CommandRegister;
import sweetie.leonware.client.features.modules.player.SpammerModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/commands/CommandSpammer.class */
@CommandRegister(name = "spammer")
public class CommandSpammer extends Command {
    @Override // sweetie.leonware.api.command.Command
    public void execute(LiteralArgumentBuilder<class_2172> builder) {
        builder.then(argument("message", StringArgumentType.greedyString()).executes(context -> {
            String msg = StringArgumentType.getString(context, "message");
            SpammerModule.getInstance().setSpamText(msg);
            print("Текст спаммера изменён на: " + msg);
            return this.SINGLE_SUCCESS;
        }));
    }
}

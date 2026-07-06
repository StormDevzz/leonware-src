package sweetie.leonware.client.features.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_2172;
import sweetie.leonware.api.command.Command;
import sweetie.leonware.api.command.CommandManager;
import sweetie.leonware.api.command.CommandRegister;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/commands/CommandPrefix.class */
@CommandRegister(name = "prefix")
public class CommandPrefix extends Command {
    @Override // sweetie.leonware.api.command.Command
    public void execute(LiteralArgumentBuilder<class_2172> builder) {
        builder.then(argument("prefix", StringArgumentType.greedyString()).executes(context -> {
            String newPrefix = StringArgumentType.getString(context, "prefix");
            if (newPrefix.isEmpty()) {
                print("Префикс не может быть пустым.");
                return this.SINGLE_SUCCESS;
            }
            CommandManager.getInstance().setPrefix(newPrefix);
            print("Префикс изменён на: " + newPrefix);
            return this.SINGLE_SUCCESS;
        }));
        builder.executes(context2 -> {
            print("Текущий префикс: " + CommandManager.getInstance().getPrefix());
            return this.SINGLE_SUCCESS;
        });
    }
}

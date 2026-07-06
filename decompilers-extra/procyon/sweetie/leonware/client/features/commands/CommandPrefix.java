// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import sweetie.leonware.api.command.CommandManager;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.class_2172;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import sweetie.leonware.api.command.CommandRegister;
import sweetie.leonware.api.command.Command;

@CommandRegister(name = "prefix")
public class CommandPrefix extends Command
{
    @Override
    public void execute(final LiteralArgumentBuilder<class_2172> builder) {
        builder.then(Command.argument("prefix", (com.mojang.brigadier.arguments.ArgumentType<Object>)StringArgumentType.greedyString()).executes(context -> {
            final String newPrefix = StringArgumentType.getString(context, "prefix");
            if (newPrefix.isEmpty()) {
                this.print("\u041f\u0440\u0435\u0444\u0438\u043a\u0441 \u043d\u0435 \u043c\u043e\u0436\u0435\u0442 \u0431\u044b\u0442\u044c \u043f\u0443\u0441\u0442\u044b\u043c.");
                return this.SINGLE_SUCCESS;
            }
            CommandManager.getInstance().setPrefix(newPrefix);
            this.print("\u041f\u0440\u0435\u0444\u0438\u043a\u0441 \u0438\u0437\u043c\u0435\u043d\u0451\u043d \u043d\u0430: " + newPrefix);
            return this.SINGLE_SUCCESS;
        }));
        builder.executes(context -> {
            this.print("\u0422\u0435\u043a\u0443\u0449\u0438\u0439 \u043f\u0440\u0435\u0444\u0438\u043a\u0441: " + CommandManager.getInstance().getPrefix());
            return this.SINGLE_SUCCESS;
        });
    }
}

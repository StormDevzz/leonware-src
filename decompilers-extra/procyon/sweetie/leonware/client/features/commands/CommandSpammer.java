// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import sweetie.leonware.client.features.modules.player.SpammerModule;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.class_2172;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import sweetie.leonware.api.command.CommandRegister;
import sweetie.leonware.api.command.Command;

@CommandRegister(name = "spammer")
public class CommandSpammer extends Command
{
    @Override
    public void execute(final LiteralArgumentBuilder<class_2172> builder) {
        builder.then(Command.argument("message", (com.mojang.brigadier.arguments.ArgumentType<Object>)StringArgumentType.greedyString()).executes(context -> {
            final String msg = StringArgumentType.getString(context, "message");
            SpammerModule.getInstance().setSpamText(msg);
            this.print("\u0422\u0435\u043a\u0441\u0442 \u0441\u043f\u0430\u043c\u043c\u0435\u0440\u0430 \u0438\u0437\u043c\u0435\u043d\u0451\u043d \u043d\u0430: " + msg);
            return this.SINGLE_SUCCESS;
        }));
    }
}

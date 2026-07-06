/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.arguments.StringArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  net.minecraft.class_2172
 */
package sweetie.leonware.client.features.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.class_2172;
import sweetie.leonware.api.command.Command;
import sweetie.leonware.api.command.CommandManager;
import sweetie.leonware.api.command.CommandRegister;

@CommandRegister(name="prefix")
public class CommandPrefix
extends Command {
    @Override
    public void execute(LiteralArgumentBuilder<class_2172> builder) {
        builder.then(CommandPrefix.argument("prefix", StringArgumentType.greedyString()).executes(context -> {
            String newPrefix = StringArgumentType.getString((CommandContext)context, (String)"prefix");
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


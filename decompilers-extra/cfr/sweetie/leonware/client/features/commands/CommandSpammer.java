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
import sweetie.leonware.api.command.CommandRegister;
import sweetie.leonware.client.features.modules.player.SpammerModule;

@CommandRegister(name="spammer")
public class CommandSpammer
extends Command {
    @Override
    public void execute(LiteralArgumentBuilder<class_2172> builder) {
        builder.then(CommandSpammer.argument("message", StringArgumentType.greedyString()).executes(context -> {
            String msg = StringArgumentType.getString((CommandContext)context, (String)"message");
            SpammerModule.getInstance().setSpamText(msg);
            this.print("\u0422\u0435\u043a\u0441\u0442 \u0441\u043f\u0430\u043c\u043c\u0435\u0440\u0430 \u0438\u0437\u043c\u0435\u043d\u0451\u043d \u043d\u0430: " + msg);
            return this.SINGLE_SUCCESS;
        }));
    }
}


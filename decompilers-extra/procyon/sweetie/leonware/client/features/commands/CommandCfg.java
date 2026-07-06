// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.commands;

import java.util.Collection;
import sweetie.leonware.api.system.configs.ConfigManager;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import sweetie.leonware.api.system.backend.SharedClass;
import sweetie.leonware.api.system.backend.ClientInfo;
import com.mojang.brigadier.context.CommandContext;
import sweetie.leonware.client.features.commands.arguments.AnyConfigNameArgument;
import com.mojang.brigadier.arguments.ArgumentType;
import sweetie.leonware.client.features.commands.arguments.StrictlyConfigNameArgument;
import net.minecraft.class_2172;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import sweetie.leonware.api.command.CommandRegister;
import sweetie.leonware.api.command.Command;

@CommandRegister(name = "cfg")
public class CommandCfg extends Command
{
    @Override
    public void execute(final LiteralArgumentBuilder<class_2172> builder) {
        builder.then(Command.literal("list").executes(context -> {
            final Collection<String> configs = ConfigManager.getInstance().getConfigsNames();
            if (configs.isEmpty()) {
                this.print("\u0421\u043f\u0438\u0441\u043e\u043a \u043a\u043e\u043d\u0444\u0438\u0433\u043e\u0432 \u043f\u0443\u0441\u0442.");
            }
            else {
                this.print("\u0421\u043f\u0438\u0441\u043e\u043a \u043a\u043e\u043d\u0444\u0438\u0433\u043e\u0432: " + String.join(", ", configs));
            }
            return this.SINGLE_SUCCESS;
        }));
        builder.then(Command.literal("remove").then(Command.argument("config", (com.mojang.brigadier.arguments.ArgumentType<Object>)StrictlyConfigNameArgument.create()).executes(context -> {
            final String config = StringArgumentType.getString(context, "config");
            if (ConfigManager.getInstance().exists(config)) {
                ConfigManager.getInstance().remove(config);
                this.print("\u041a\u043e\u043d\u0444\u0438\u0433 " + config + " \u0443\u0434\u0430\u043b\u0451\u043d.");
            }
            else {
                this.print("\u042f \u043d\u0435 \u043d\u0430\u0448\u043b\u0430 \u0442\u0430\u043a\u043e\u0433\u043e \u043a\u043e\u043d\u0444\u0438\u0433\u0430.");
            }
            return this.SINGLE_SUCCESS;
        })));
        builder.then(Command.literal("load").then(Command.argument("config", (com.mojang.brigadier.arguments.ArgumentType<Object>)StrictlyConfigNameArgument.create()).executes(context -> {
            final String config = StringArgumentType.getString(context, "config");
            ConfigManager.getInstance().load(config);
            if (ConfigManager.getInstance().exists(config)) {
                this.print("\u0417\u0430\u0433\u0440\u0443\u0436\u0435\u043d \u043a\u043e\u043d\u0444\u0438\u0433: " + config);
            }
            else {
                this.print("\u042f \u043d\u0435 \u043d\u0430\u0448\u043b\u0430 \u0442\u0430\u043a\u043e\u0433\u043e \u043a\u043e\u043d\u0444\u0438\u0433\u0430 T.T");
            }
            return this.SINGLE_SUCCESS;
        })));
        builder.then(Command.literal("save").then(Command.argument("config", (com.mojang.brigadier.arguments.ArgumentType<Object>)AnyConfigNameArgument.create()).executes(context -> {
            final String config = StringArgumentType.getString(context, "config");
            ConfigManager.getInstance().save(config);
            this.print("\u0421\u043e\u0445\u0440\u0430\u043d\u0451\u043d \u043a\u043e\u043d\u0444\u0438\u0433: " + config);
            return this.SINGLE_SUCCESS;
        })));
        builder.then(Command.literal("folder").executes(context -> {
            if (SharedClass.openFolder(ClientInfo.CONFIG_PATH_MAIN)) {
                this.print("\u041e\u0442\u043a\u0440\u044b\u0432\u0430\u044e...");
            }
            else {
                this.print("\u041d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u043e\u0442\u043a\u0440\u044b\u0442\u044c \u043f\u0430\u043f\u043a\u0443 \u043a\u043e\u043d\u0444\u0438\u0433\u043e\u0432.");
            }
            return this.SINGLE_SUCCESS;
        }));
        builder.then(Command.literal("dir").executes(context -> {
            if (SharedClass.openFolder(ClientInfo.CONFIG_PATH_MAIN)) {
                this.print("\u041e\u0442\u043a\u0440\u044b\u0432\u0430\u044e...");
            }
            else {
                this.print("\u041d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u043e\u0442\u043a\u0440\u044b\u0442\u044c \u043f\u0430\u043f\u043a\u0443 \u043a\u043e\u043d\u0444\u0438\u0433\u043e\u0432.");
            }
            return this.SINGLE_SUCCESS;
        }));
    }
}

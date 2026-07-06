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
import java.util.List;
import net.minecraft.class_2172;
import sweetie.leonware.api.command.Command;
import sweetie.leonware.api.command.CommandRegister;
import sweetie.leonware.api.system.backend.ClientInfo;
import sweetie.leonware.api.system.backend.SharedClass;
import sweetie.leonware.api.system.configs.ConfigManager;
import sweetie.leonware.client.features.commands.arguments.AnyConfigNameArgument;
import sweetie.leonware.client.features.commands.arguments.StrictlyConfigNameArgument;

@CommandRegister(name="config")
public class CommandConfig
extends Command {
    @Override
    public void execute(LiteralArgumentBuilder<class_2172> builder) {
        builder.then(CommandConfig.literal("list").executes(context -> {
            List<String> configs = ConfigManager.getInstance().getConfigsNames();
            if (configs.isEmpty()) {
                this.print("\u0421\u043f\u0438\u0441\u043e\u043a \u043a\u043e\u043d\u0444\u0438\u0433\u043e\u0432 \u043f\u0443\u0441\u0442.");
            } else {
                this.print("\u0421\u043f\u0438\u0441\u043e\u043a \u043a\u043e\u043d\u0444\u0438\u0433\u043e\u0432: " + String.join((CharSequence)", ", configs));
            }
            return this.SINGLE_SUCCESS;
        }));
        builder.then(CommandConfig.literal("remove").then(CommandConfig.argument("config", StrictlyConfigNameArgument.create()).executes(context -> {
            String config = StringArgumentType.getString((CommandContext)context, (String)"config");
            if (ConfigManager.getInstance().exists(config)) {
                ConfigManager.getInstance().remove(config);
                this.print("\u041a\u043e\u043d\u0444\u0438\u0433 " + config + " \u0443\u0434\u0430\u043b\u0451\u043d.");
            } else {
                this.print("\u042f \u043d\u0435 \u043d\u0430\u0448\u043b\u0430 \u0442\u0430\u043a\u043e\u0433\u043e \u043a\u043e\u043d\u0444\u0438\u0433\u0430.");
            }
            return this.SINGLE_SUCCESS;
        })));
        builder.then(CommandConfig.literal("load").then(CommandConfig.argument("config", StrictlyConfigNameArgument.create()).executes(context -> {
            String config = StringArgumentType.getString((CommandContext)context, (String)"config");
            ConfigManager.getInstance().load(config);
            if (ConfigManager.getInstance().exists(config)) {
                this.print("\u0417\u0430\u0433\u0440\u0443\u0436\u0435\u043d \u043a\u043e\u043d\u0444\u0438\u0433: " + config);
            } else {
                this.print("\u042f \u043d\u0435 \u043d\u0430\u0448\u043b\u0430 \u0442\u0430\u043a\u043e\u0433\u043e \u043a\u043e\u043d\u0444\u0438\u0433\u0430 T.T");
            }
            return this.SINGLE_SUCCESS;
        })));
        builder.then(CommandConfig.literal("save").then(CommandConfig.argument("config", AnyConfigNameArgument.create()).executes(context -> {
            String config = StringArgumentType.getString((CommandContext)context, (String)"config");
            ConfigManager.getInstance().save(config);
            this.print("\u0421\u043e\u0445\u0440\u0430\u043d\u0451\u043d \u043a\u043e\u043d\u0444\u0438\u0433: " + config);
            return this.SINGLE_SUCCESS;
        })));
        builder.then(CommandConfig.literal("folder").executes(context -> {
            if (SharedClass.openFolder(ClientInfo.CONFIG_PATH_MAIN)) {
                this.print("\u041e\u0442\u043a\u0440\u044b\u0432\u0430\u044e...");
            } else {
                this.print("\u041d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u043e\u0442\u043a\u0440\u044b\u0442\u044c \u043f\u0430\u043f\u043a\u0443 \u043a\u043e\u043d\u0444\u0438\u0433\u043e\u0432.");
            }
            return this.SINGLE_SUCCESS;
        }));
    }
}


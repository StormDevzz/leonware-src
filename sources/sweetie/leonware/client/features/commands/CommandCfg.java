package sweetie.leonware.client.features.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.Collection;
import net.minecraft.class_2172;
import sweetie.leonware.api.command.Command;
import sweetie.leonware.api.command.CommandRegister;
import sweetie.leonware.api.system.backend.ClientInfo;
import sweetie.leonware.api.system.backend.SharedClass;
import sweetie.leonware.api.system.configs.ConfigManager;
import sweetie.leonware.client.features.commands.arguments.AnyConfigNameArgument;
import sweetie.leonware.client.features.commands.arguments.StrictlyConfigNameArgument;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/commands/CommandCfg.class */
@CommandRegister(name = "cfg")
public class CommandCfg extends Command {
    @Override // sweetie.leonware.api.command.Command
    public void execute(LiteralArgumentBuilder<class_2172> builder) {
        builder.then(literal("list").executes(context -> {
            Collection<String> configs = ConfigManager.getInstance().getConfigsNames();
            if (configs.isEmpty()) {
                print("Список конфигов пуст.");
            } else {
                print("Список конфигов: " + String.join(", ", configs));
            }
            return this.SINGLE_SUCCESS;
        }));
        builder.then(literal("remove").then(argument("config", StrictlyConfigNameArgument.create()).executes(context2 -> {
            String config = StringArgumentType.getString(context2, "config");
            if (ConfigManager.getInstance().exists(config)) {
                ConfigManager.getInstance().remove(config);
                print("Конфиг " + config + " удалён.");
            } else {
                print("Я не нашла такого конфига.");
            }
            return this.SINGLE_SUCCESS;
        })));
        builder.then(literal("load").then(argument("config", StrictlyConfigNameArgument.create()).executes(context3 -> {
            String config = StringArgumentType.getString(context3, "config");
            ConfigManager.getInstance().load(config);
            if (ConfigManager.getInstance().exists(config)) {
                print("Загружен конфиг: " + config);
            } else {
                print("Я не нашла такого конфига T.T");
            }
            return this.SINGLE_SUCCESS;
        })));
        builder.then(literal("save").then(argument("config", AnyConfigNameArgument.create()).executes(context4 -> {
            String config = StringArgumentType.getString(context4, "config");
            ConfigManager.getInstance().save(config);
            print("Сохранён конфиг: " + config);
            return this.SINGLE_SUCCESS;
        })));
        builder.then(literal("folder").executes(context5 -> {
            if (SharedClass.openFolder(ClientInfo.CONFIG_PATH_MAIN)) {
                print("Открываю...");
            } else {
                print("Не удалось открыть папку конфигов.");
            }
            return this.SINGLE_SUCCESS;
        }));
        builder.then(literal("dir").executes(context6 -> {
            if (SharedClass.openFolder(ClientInfo.CONFIG_PATH_MAIN)) {
                print("Открываю...");
            } else {
                print("Не удалось открыть папку конфигов.");
            }
            return this.SINGLE_SUCCESS;
        }));
    }
}

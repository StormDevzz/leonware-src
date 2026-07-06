package sweetie.leonware.client.features.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_124;
import net.minecraft.class_2172;
import sweetie.leonware.api.command.Command;
import sweetie.leonware.api.command.CommandRegister;
import sweetie.leonware.api.system.backend.KeyStorage;
import sweetie.leonware.api.system.configs.MacroManager;
import sweetie.leonware.client.features.commands.arguments.AnyStringArgument;
import sweetie.leonware.client.features.commands.arguments.StrictlyKeyArgument;
import sweetie.leonware.client.features.commands.arguments.StrictlyMacroNameArgument;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/commands/CommandMacro.class */
@CommandRegister(name = "macro")
public class CommandMacro extends Command {
    private final MacroManager macroManager = MacroManager.getInstance();

    @Override // sweetie.leonware.api.command.Command
    public void execute(LiteralArgumentBuilder<class_2172> builder) {
        builder.then(literal("add").then(argument("name", new AnyStringArgument()).then(argument("key", new StrictlyKeyArgument()).then(argument("message", StringArgumentType.greedyString()).executes(context -> {
            String name = StringArgumentType.getString(context, "name");
            String keyName = StringArgumentType.getString(context, "key");
            String message = StringArgumentType.getString(context, "message");
            int keyCode = KeyStorage.getBind(keyName);
            if (keyCode == -1) {
                print("Клавиша " + keyName + " не найдена!");
                return 0;
            }
            if (this.macroManager.has(name)) {
                print("Макрос с таким именем уже существует!");
                return 0;
            }
            this.macroManager.add(name, message, keyCode);
            print("Добавлен макрос с названием " + name + " с кнопкой " + keyName + " с командой " + message);
            return this.SINGLE_SUCCESS;
        })))));
        builder.then(literal("remove").then(argument("name", new StrictlyMacroNameArgument()).executes(context2 -> {
            String name = StringArgumentType.getString(context2, "name");
            if (!this.macroManager.has(name)) {
                print("Макрос с таким именем не найден!");
                return 0;
            }
            this.macroManager.remove(name);
            print("Макрос " + name + " был успешно удален!");
            return this.SINGLE_SUCCESS;
        })));
        builder.then(literal("clear").executes(context3 -> {
            this.macroManager.getMacros().clear();
            print("Все макросы были удалены.");
            return this.SINGLE_SUCCESS;
        }));
        builder.then(literal("list").executes(context4 -> {
            if (this.macroManager.getMacros().isEmpty()) {
                print("Список пустой");
                return 0;
            }
            this.macroManager.getMacros().forEach(macro -> {
                print("Название: " + String.valueOf(class_124.field_1080) + macro.getName() + String.valueOf(class_124.field_1070) + ", Команда: " + String.valueOf(class_124.field_1080) + macro.getMessage() + String.valueOf(class_124.field_1070) + ", Кнопка: " + String.valueOf(class_124.field_1080) + KeyStorage.getBind(macro.getKey()));
            });
            return this.SINGLE_SUCCESS;
        }));
    }
}

// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import sweetie.leonware.api.system.backend.KeyStorage;
import net.minecraft.class_124;
import com.mojang.brigadier.context.CommandContext;
import sweetie.leonware.client.features.commands.arguments.StrictlyMacroNameArgument;
import com.mojang.brigadier.arguments.StringArgumentType;
import sweetie.leonware.client.features.commands.arguments.StrictlyKeyArgument;
import com.mojang.brigadier.arguments.ArgumentType;
import sweetie.leonware.client.features.commands.arguments.AnyStringArgument;
import net.minecraft.class_2172;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import sweetie.leonware.api.system.configs.MacroManager;
import sweetie.leonware.api.command.CommandRegister;
import sweetie.leonware.api.command.Command;

@CommandRegister(name = "macro")
public class CommandMacro extends Command
{
    private final MacroManager macroManager;
    
    public CommandMacro() {
        this.macroManager = MacroManager.getInstance();
    }
    
    @Override
    public void execute(final LiteralArgumentBuilder<class_2172> builder) {
        builder.then(Command.literal("add").then(Command.argument("name", (com.mojang.brigadier.arguments.ArgumentType<Object>)new AnyStringArgument()).then(Command.argument("key", (com.mojang.brigadier.arguments.ArgumentType<Object>)new StrictlyKeyArgument()).then(Command.argument("message", (com.mojang.brigadier.arguments.ArgumentType<Object>)StringArgumentType.greedyString()).executes(context -> {
            final String name = StringArgumentType.getString(context, "name");
            final String keyName = StringArgumentType.getString(context, "key");
            final String message = StringArgumentType.getString(context, "message");
            final int keyCode = KeyStorage.getBind(keyName);
            if (keyCode == -1) {
                this.print("\u041a\u043b\u0430\u0432\u0438\u0448\u0430 " + keyName + " \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u0430!");
                return 0;
            }
            if (this.macroManager.has(name)) {
                this.print("\u041c\u0430\u043a\u0440\u043e\u0441 \u0441 \u0442\u0430\u043a\u0438\u043c \u0438\u043c\u0435\u043d\u0435\u043c \u0443\u0436\u0435 \u0441\u0443\u0449\u0435\u0441\u0442\u0432\u0443\u0435\u0442!");
                return 0;
            }
            this.macroManager.add(name, message, keyCode);
            this.print("\u0414\u043e\u0431\u0430\u0432\u043b\u0435\u043d \u043c\u0430\u043a\u0440\u043e\u0441 \u0441 \u043d\u0430\u0437\u0432\u0430\u043d\u0438\u0435\u043c " + name + " \u0441 \u043a\u043d\u043e\u043f\u043a\u043e\u0439 " + keyName + " \u0441 \u043a\u043e\u043c\u0430\u043d\u0434\u043e\u0439 " + message);
            return this.SINGLE_SUCCESS;
        })))));
        builder.then(Command.literal("remove").then(Command.argument("name", (com.mojang.brigadier.arguments.ArgumentType<Object>)new StrictlyMacroNameArgument()).executes(context -> {
            final String name = StringArgumentType.getString(context, "name");
            if (!this.macroManager.has(name)) {
                this.print("\u041c\u0430\u043a\u0440\u043e\u0441 \u0441 \u0442\u0430\u043a\u0438\u043c \u0438\u043c\u0435\u043d\u0435\u043c \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d!");
                return 0;
            }
            this.macroManager.remove(name);
            this.print("\u041c\u0430\u043a\u0440\u043e\u0441 " + name + " \u0431\u044b\u043b \u0443\u0441\u043f\u0435\u0448\u043d\u043e \u0443\u0434\u0430\u043b\u0435\u043d!");
            return this.SINGLE_SUCCESS;
        })));
        builder.then(Command.literal("clear").executes(context -> {
            this.macroManager.getMacros().clear();
            this.print("\u0412\u0441\u0435 \u043c\u0430\u043a\u0440\u043e\u0441\u044b \u0431\u044b\u043b\u0438 \u0443\u0434\u0430\u043b\u0435\u043d\u044b.");
            return this.SINGLE_SUCCESS;
        }));
        builder.then(Command.literal("list").executes(context -> {
            if (this.macroManager.getMacros().isEmpty()) {
                this.print("\u0421\u043f\u0438\u0441\u043e\u043a \u043f\u0443\u0441\u0442\u043e\u0439");
                return 0;
            }
            this.macroManager.getMacros().forEach(macro -> this.print("\u041d\u0430\u0437\u0432\u0430\u043d\u0438\u0435: " + String.valueOf(class_124.field_1080) + macro.getName() + String.valueOf(class_124.field_1070) + ", \u041a\u043e\u043c\u0430\u043d\u0434\u0430: " + String.valueOf(class_124.field_1080) + macro.getMessage() + String.valueOf(class_124.field_1070) + ", \u041a\u043d\u043e\u043f\u043a\u0430: " + String.valueOf(class_124.field_1080) + KeyStorage.getBind(macro.getKey())));
            return this.SINGLE_SUCCESS;
        }));
    }
}

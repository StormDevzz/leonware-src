// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.commands;

import java.util.stream.Stream;
import java.util.function.Consumer;
import java.util.Objects;
import java.util.function.Function;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import sweetie.leonware.client.features.modules.render.ClickGUIModule;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;
import java.lang.reflect.Field;
import org.lwjgl.glfw.GLFW;
import java.util.Iterator;
import sweetie.leonware.api.module.ModuleManager;
import sweetie.leonware.api.module.Module;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.class_2172;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import sweetie.leonware.api.command.CommandRegister;
import sweetie.leonware.api.command.Command;

@CommandRegister(name = "bind")
public class CommandBind extends Command
{
    @Override
    public void execute(final LiteralArgumentBuilder<class_2172> builder) {
        builder.then(Command.literal("add").then(Command.argument("module", (com.mojang.brigadier.arguments.ArgumentType<Object>)StringArgumentType.string()).suggests((context, builder1) -> {
            final String prefix = builder1.getRemaining().toLowerCase().replace(" ", "");
            final Stream<Object> filter = ModuleManager.getInstance().getModules().stream().map((Function<? super Object, ?>)Module::getName).map(name -> name.replace(" ", "")).filter(name -> name.toLowerCase().startsWith(prefix));
            Objects.requireNonNull(builder1);
            filter.forEach((Consumer<? super Object>)builder1::suggest);
            return builder1.buildFuture();
        }).then(Command.argument("key", (com.mojang.brigadier.arguments.ArgumentType<Object>)StringArgumentType.string()).executes(context -> {
            final String moduleName = StringArgumentType.getString(context, "module");
            final String keyName = StringArgumentType.getString(context, "key").toUpperCase();
            final Module module = this.findModule(moduleName);
            if (module == null) {
                this.print("§c\u041c\u043e\u0434\u0443\u043b\u044c \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d: §f" + moduleName);
                return this.SINGLE_SUCCESS;
            }
            final int keyCode = this.getKeyCode(keyName);
            if (keyCode == -1) {
                this.print("§c\u041a\u043b\u0430\u0432\u0438\u0448\u0430 \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u0430: §f" + keyName);
                return this.SINGLE_SUCCESS;
            }
            module.setBind(keyCode);
            this.print("§a\u0411\u0438\u043d\u0434 \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d: §f" + module.getName() + " -> " + keyName);
            return this.SINGLE_SUCCESS;
        }))));
        builder.then(Command.literal("remove").then(Command.argument("module", (com.mojang.brigadier.arguments.ArgumentType<Object>)StringArgumentType.greedyString()).suggests((context, builder1) -> {
            final String prefix = builder1.getRemaining().toLowerCase().replace(" ", "");
            final Stream<Object> filter = ModuleManager.getInstance().getModules().stream().map((Function<? super Object, ?>)Module::getName).filter(name -> {
                final String cleanName = name.toLowerCase().replace(" ", "");
                return cleanName.startsWith(prefix);
            });
            Objects.requireNonNull(builder1);
            filter.forEach((Consumer<? super Object>)builder1::suggest);
            return builder1.buildFuture();
        }).executes(context -> {
            final String moduleName = StringArgumentType.getString(context, "module");
            final Module module = this.findModule(moduleName);
            if (module == null) {
                this.print("§c\u041c\u043e\u0434\u0443\u043b\u044c \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d: §f" + moduleName);
                return this.SINGLE_SUCCESS;
            }
            module.setBind(-999);
            this.print("§a\u0411\u0438\u043d\u0434 \u0443\u0431\u0440\u0430\u043d: §f" + module.getName());
            return this.SINGLE_SUCCESS;
        })));
        builder.then(Command.literal("clear").executes(context -> {
            for (final Module module : ModuleManager.getInstance().getModules()) {
                if (module instanceof ClickGUIModule) {
                    continue;
                }
                module.setBind(-999);
            }
            this.print("§a\u0412\u0441\u0435 \u0431\u0438\u043d\u0434\u044b \u043e\u0447\u0438\u0449\u0435\u043d\u044b (\u043a\u0440\u043e\u043c\u0435 ClickGUI).");
            return this.SINGLE_SUCCESS;
        }));
        builder.then(Command.literal("list").executes(context -> {
            boolean hasBinds = false;
            this.print("§6\u0421\u043f\u0438\u0441\u043e\u043a \u0430\u043a\u0442\u0438\u0432\u043d\u044b\u0445 \u0431\u0438\u043d\u0434\u043e\u0432:");
            for (Module module : ModuleManager.getInstance().getModules()) {
                if (module.getBind() != -999) {
                    this.print("§7- §f" + module.getName() + "§7: §a" + this.getKeyName(module.getBind()));
                    hasBinds = true;
                }
            }
            if (!hasBinds) {
                this.print("§c\u0423 \u0432\u0430\u0441 \u043d\u0435\u0442 \u0430\u043a\u0442\u0438\u0432\u043d\u044b\u0445 \u0431\u0438\u043d\u0434\u043e\u0432.");
            }
            return this.SINGLE_SUCCESS;
        }));
        builder.executes(context -> {
            this.print("§6\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435: §fbind add <\u043c\u043e\u0434\u0443\u043b\u044c> <\u043a\u043b\u0430\u0432\u0438\u0448\u0430> | bind remove <\u043c\u043e\u0434\u0443\u043b\u044c> | bind clear | bind list");
            return this.SINGLE_SUCCESS;
        });
    }
    
    private Module findModule(final String name) {
        final String searchName = name.replace(" ", "").toLowerCase();
        for (final Module module : ModuleManager.getInstance().getModules()) {
            final String moduleCleanName = module.getName().replace(" ", "").toLowerCase();
            if (moduleCleanName.equals(searchName)) {
                return module;
            }
        }
        return null;
    }
    
    private int getKeyCode(final String keyName) {
        if (keyName.equals("NONE")) {
            return -999;
        }
        if (keyName.equals("RSHIFT")) {
            return 344;
        }
        if (keyName.equals("LSHIFT")) {
            return 340;
        }
        if (keyName.equals("RCONTROL")) {
            return 345;
        }
        if (keyName.equals("LCONTROL")) {
            return 341;
        }
        if (keyName.startsWith("MOUSE")) {
            try {
                final String buttonNum = keyName.replace("MOUSE", "");
                final Field field = GLFW.class.getField("GLFW_MOUSE_BUTTON_" + buttonNum);
                return field.getInt(null);
            }
            catch (final Exception e) {
                return -1;
            }
        }
        try {
            final Field field2 = GLFW.class.getField("GLFW_KEY_" + keyName);
            return field2.getInt(null);
        }
        catch (final Exception e) {
            return -1;
        }
    }
    
    private String getKeyName(final int keyCode) {
        if (keyCode == -999) {
            return "NONE";
        }
        if (keyCode == 344) {
            return "RSHIFT";
        }
        if (keyCode == 340) {
            return "LSHIFT";
        }
        if (keyCode == 345) {
            return "RCONTROL";
        }
        if (keyCode == 341) {
            return "LCONTROL";
        }
        for (final Field field : GLFW.class.getFields()) {
            if (field.getName().startsWith("GLFW_KEY_")) {
                try {
                    if (field.getInt(null) == keyCode) {
                        return field.getName().replace("GLFW_KEY_", "");
                    }
                }
                catch (final Exception ex) {}
            }
        }
        for (final Field field : GLFW.class.getFields()) {
            if (field.getName().startsWith("GLFW_MOUSE_BUTTON_")) {
                try {
                    if (field.getInt(null) == keyCode) {
                        return "MOUSE" + field.getName().replace("GLFW_MOUSE_BUTTON_", "");
                    }
                }
                catch (final Exception ex2) {}
            }
        }
        return "UNKNOWN";
    }
}

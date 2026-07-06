/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.arguments.StringArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  com.mojang.brigadier.suggestion.SuggestionsBuilder
 *  net.minecraft.class_2172
 *  org.lwjgl.glfw.GLFW
 */
package sweetie.leonware.client.features.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.lang.reflect.Field;
import net.minecraft.class_2172;
import org.lwjgl.glfw.GLFW;
import sweetie.leonware.api.command.Command;
import sweetie.leonware.api.command.CommandRegister;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleManager;
import sweetie.leonware.client.features.modules.render.ClickGUIModule;

@CommandRegister(name="bind")
public class CommandBind
extends Command {
    @Override
    public void execute(LiteralArgumentBuilder<class_2172> builder) {
        builder.then(CommandBind.literal("add").then(CommandBind.argument("module", StringArgumentType.string()).suggests((context, builder1) -> {
            String prefix = builder1.getRemaining().toLowerCase().replace(" ", "");
            ModuleManager.getInstance().getModules().stream().map(Module::getName).map(name -> name.replace(" ", "")).filter(name -> name.toLowerCase().startsWith(prefix)).forEach(arg_0 -> ((SuggestionsBuilder)builder1).suggest(arg_0));
            return builder1.buildFuture();
        }).then(CommandBind.argument("key", StringArgumentType.string()).executes(context -> {
            String moduleName = StringArgumentType.getString((CommandContext)context, (String)"module");
            String keyName = StringArgumentType.getString((CommandContext)context, (String)"key").toUpperCase();
            Module module = this.findModule(moduleName);
            if (module == null) {
                this.print("\u00a7c\u041c\u043e\u0434\u0443\u043b\u044c \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d: \u00a7f" + moduleName);
                return this.SINGLE_SUCCESS;
            }
            int keyCode = this.getKeyCode(keyName);
            if (keyCode == -1) {
                this.print("\u00a7c\u041a\u043b\u0430\u0432\u0438\u0448\u0430 \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u0430: \u00a7f" + keyName);
                return this.SINGLE_SUCCESS;
            }
            module.setBind(keyCode);
            this.print("\u00a7a\u0411\u0438\u043d\u0434 \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d: \u00a7f" + module.getName() + " -> " + keyName);
            return this.SINGLE_SUCCESS;
        }))));
        builder.then(CommandBind.literal("remove").then(CommandBind.argument("module", StringArgumentType.greedyString()).suggests((context, builder1) -> {
            String prefix = builder1.getRemaining().toLowerCase().replace(" ", "");
            ModuleManager.getInstance().getModules().stream().map(Module::getName).filter(name -> {
                String cleanName = name.toLowerCase().replace(" ", "");
                return cleanName.startsWith(prefix);
            }).forEach(arg_0 -> ((SuggestionsBuilder)builder1).suggest(arg_0));
            return builder1.buildFuture();
        }).executes(context -> {
            String moduleName = StringArgumentType.getString((CommandContext)context, (String)"module");
            Module module = this.findModule(moduleName);
            if (module == null) {
                this.print("\u00a7c\u041c\u043e\u0434\u0443\u043b\u044c \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d: \u00a7f" + moduleName);
                return this.SINGLE_SUCCESS;
            }
            module.setBind(-999);
            this.print("\u00a7a\u0411\u0438\u043d\u0434 \u0443\u0431\u0440\u0430\u043d: \u00a7f" + module.getName());
            return this.SINGLE_SUCCESS;
        })));
        builder.then(CommandBind.literal("clear").executes(context -> {
            for (Module module : ModuleManager.getInstance().getModules()) {
                if (module instanceof ClickGUIModule) continue;
                module.setBind(-999);
            }
            this.print("\u00a7a\u0412\u0441\u0435 \u0431\u0438\u043d\u0434\u044b \u043e\u0447\u0438\u0449\u0435\u043d\u044b (\u043a\u0440\u043e\u043c\u0435 ClickGUI).");
            return this.SINGLE_SUCCESS;
        }));
        builder.then(CommandBind.literal("list").executes(context -> {
            boolean hasBinds = false;
            this.print("\u00a76\u0421\u043f\u0438\u0441\u043e\u043a \u0430\u043a\u0442\u0438\u0432\u043d\u044b\u0445 \u0431\u0438\u043d\u0434\u043e\u0432:");
            for (Module module : ModuleManager.getInstance().getModules()) {
                if (module.getBind() == -999) continue;
                this.print("\u00a77- \u00a7f" + module.getName() + "\u00a77: \u00a7a" + this.getKeyName(module.getBind()));
                hasBinds = true;
            }
            if (!hasBinds) {
                this.print("\u00a7c\u0423 \u0432\u0430\u0441 \u043d\u0435\u0442 \u0430\u043a\u0442\u0438\u0432\u043d\u044b\u0445 \u0431\u0438\u043d\u0434\u043e\u0432.");
            }
            return this.SINGLE_SUCCESS;
        }));
        builder.executes(context -> {
            this.print("\u00a76\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435: \u00a7fbind add <\u043c\u043e\u0434\u0443\u043b\u044c> <\u043a\u043b\u0430\u0432\u0438\u0448\u0430> | bind remove <\u043c\u043e\u0434\u0443\u043b\u044c> | bind clear | bind list");
            return this.SINGLE_SUCCESS;
        });
    }

    private Module findModule(String name) {
        String searchName = name.replace(" ", "").toLowerCase();
        for (Module module : ModuleManager.getInstance().getModules()) {
            String moduleCleanName = module.getName().replace(" ", "").toLowerCase();
            if (!moduleCleanName.equals(searchName)) continue;
            return module;
        }
        return null;
    }

    private int getKeyCode(String keyName) {
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
                String buttonNum = keyName.replace("MOUSE", "");
                Field field = GLFW.class.getField("GLFW_MOUSE_BUTTON_" + buttonNum);
                return field.getInt(null);
            }
            catch (Exception e) {
                return -1;
            }
        }
        try {
            Field field = GLFW.class.getField("GLFW_KEY_" + keyName);
            return field.getInt(null);
        }
        catch (Exception e) {
            return -1;
        }
    }

    private String getKeyName(int keyCode) {
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
        for (Field field : GLFW.class.getFields()) {
            if (!field.getName().startsWith("GLFW_KEY_")) continue;
            try {
                if (field.getInt(null) != keyCode) continue;
                return field.getName().replace("GLFW_KEY_", "");
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        for (Field field : GLFW.class.getFields()) {
            if (!field.getName().startsWith("GLFW_MOUSE_BUTTON_")) continue;
            try {
                if (field.getInt(null) != keyCode) continue;
                return "MOUSE" + field.getName().replace("GLFW_MOUSE_BUTTON_", "");
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return "UNKNOWN";
    }
}


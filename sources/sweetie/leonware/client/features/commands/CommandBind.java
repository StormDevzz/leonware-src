package sweetie.leonware.client.features.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.stream.Stream;
import net.minecraft.class_2172;
import org.lwjgl.glfw.GLFW;
import sweetie.leonware.api.command.Command;
import sweetie.leonware.api.command.CommandRegister;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleManager;
import sweetie.leonware.client.features.modules.render.ClickGUIModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/commands/CommandBind.class */
@CommandRegister(name = "bind")
public class CommandBind extends Command {
    @Override // sweetie.leonware.api.command.Command
    public void execute(LiteralArgumentBuilder<class_2172> builder) {
        builder.then(literal("add").then(argument("module", StringArgumentType.string()).suggests((context, builder1) -> {
            String prefix = builder1.getRemaining().toLowerCase().replace(" ", "");
            Stream streamFilter = ModuleManager.getInstance().getModules().stream().map((v0) -> {
                return v0.getName();
            }).map(name -> {
                return name.replace(" ", "");
            }).filter(name2 -> {
                return name2.toLowerCase().startsWith(prefix);
            });
            Objects.requireNonNull(builder1);
            streamFilter.forEach(builder1::suggest);
            return builder1.buildFuture();
        }).then(argument("key", StringArgumentType.string()).executes(context2 -> {
            String moduleName = StringArgumentType.getString(context2, "module");
            String keyName = StringArgumentType.getString(context2, "key").toUpperCase();
            Module module = findModule(moduleName);
            if (module == null) {
                print("§cМодуль не найден: §f" + moduleName);
                return this.SINGLE_SUCCESS;
            }
            int keyCode = getKeyCode(keyName);
            if (keyCode == -1) {
                print("§cКлавиша не найдена: §f" + keyName);
                return this.SINGLE_SUCCESS;
            }
            module.setBind(keyCode);
            print("§aБинд установлен: §f" + module.getName() + " -> " + keyName);
            return this.SINGLE_SUCCESS;
        }))));
        builder.then(literal("remove").then(argument("module", StringArgumentType.greedyString()).suggests((context3, builder12) -> {
            String prefix = builder12.getRemaining().toLowerCase().replace(" ", "");
            Stream streamFilter = ModuleManager.getInstance().getModules().stream().map((v0) -> {
                return v0.getName();
            }).filter(name -> {
                String cleanName = name.toLowerCase().replace(" ", "");
                return cleanName.startsWith(prefix);
            });
            Objects.requireNonNull(builder12);
            streamFilter.forEach(builder12::suggest);
            return builder12.buildFuture();
        }).executes(context4 -> {
            String moduleName = StringArgumentType.getString(context4, "module");
            Module module = findModule(moduleName);
            if (module == null) {
                print("§cМодуль не найден: §f" + moduleName);
                return this.SINGLE_SUCCESS;
            }
            module.setBind(-999);
            print("§aБинд убран: §f" + module.getName());
            return this.SINGLE_SUCCESS;
        })));
        builder.then(literal("clear").executes(context5 -> {
            for (Module module : ModuleManager.getInstance().getModules()) {
                if (!(module instanceof ClickGUIModule)) {
                    module.setBind(-999);
                }
            }
            print("§aВсе бинды очищены (кроме ClickGUI).");
            return this.SINGLE_SUCCESS;
        }));
        builder.then(literal("list").executes(context6 -> {
            boolean hasBinds = false;
            print("§6Список активных биндов:");
            for (Module module : ModuleManager.getInstance().getModules()) {
                if (module.getBind() != -999) {
                    print("§7- §f" + module.getName() + "§7: §a" + getKeyName(module.getBind()));
                    hasBinds = true;
                }
            }
            if (!hasBinds) {
                print("§cУ вас нет активных биндов.");
            }
            return this.SINGLE_SUCCESS;
        }));
        builder.executes(context7 -> {
            print("§6Использование: §fbind add <модуль> <клавиша> | bind remove <модуль> | bind clear | bind list");
            return this.SINGLE_SUCCESS;
        });
    }

    private Module findModule(String name) {
        String searchName = name.replace(" ", "").toLowerCase();
        for (Module module : ModuleManager.getInstance().getModules()) {
            String moduleCleanName = module.getName().replace(" ", "").toLowerCase();
            if (moduleCleanName.equals(searchName)) {
                return module;
            }
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
            } catch (Exception e) {
                return -1;
            }
        }
        try {
            Field field2 = GLFW.class.getField("GLFW_KEY_" + keyName);
            return field2.getInt(null);
        } catch (Exception e2) {
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
            if (field.getName().startsWith("GLFW_KEY_")) {
                try {
                    if (field.getInt(null) == keyCode) {
                        return field.getName().replace("GLFW_KEY_", "");
                    }
                    continue;
                } catch (Exception e) {
                }
            }
        }
        for (Field field2 : GLFW.class.getFields()) {
            if (field2.getName().startsWith("GLFW_MOUSE_BUTTON_")) {
                try {
                    if (field2.getInt(null) == keyCode) {
                        return "MOUSE" + field2.getName().replace("GLFW_MOUSE_BUTTON_", "");
                    }
                    continue;
                } catch (Exception e2) {
                }
            }
        }
        return "UNKNOWN";
    }
}

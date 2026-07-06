package sweetie.leonware.client.features.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.Objects;
import java.util.stream.Stream;
import net.minecraft.class_2172;
import sweetie.leonware.api.command.Command;
import sweetie.leonware.api.command.CommandRegister;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleManager;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/commands/CommandT.class */
@CommandRegister(name = "t")
public class CommandT extends Command {
    @Override // sweetie.leonware.api.command.Command
    public void execute(LiteralArgumentBuilder<class_2172> builder) {
        builder.then(argument("module", StringArgumentType.greedyString()).suggests((context, builder1) -> {
            String prefix = builder1.getRemaining().toLowerCase();
            Stream streamFilter = ModuleManager.getInstance().getModules().stream().map((v0) -> {
                return v0.getName();
            }).filter(name -> {
                return name.toLowerCase().startsWith(prefix);
            });
            Objects.requireNonNull(builder1);
            streamFilter.forEach(builder1::suggest);
            return builder1.buildFuture();
        }).executes(context2 -> {
            String moduleName = StringArgumentType.getString(context2, "module");
            Module module = findModule(moduleName);
            if (module == null) {
                print("§cМодуль не найден: §f" + moduleName);
                return this.SINGLE_SUCCESS;
            }
            module.toggle();
            print(module.getName() + (module.isEnabled() ? " §aвключен" : " §cвыключен"));
            return this.SINGLE_SUCCESS;
        }));
    }

    private Module findModule(String name) {
        for (Module module : ModuleManager.getInstance().getModules()) {
            if (module.getName().equalsIgnoreCase(name)) {
                return module;
            }
        }
        return null;
    }
}

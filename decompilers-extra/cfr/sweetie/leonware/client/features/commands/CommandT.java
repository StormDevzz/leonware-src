/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.arguments.StringArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  com.mojang.brigadier.suggestion.SuggestionsBuilder
 *  net.minecraft.class_2172
 */
package sweetie.leonware.client.features.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.class_2172;
import sweetie.leonware.api.command.Command;
import sweetie.leonware.api.command.CommandRegister;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleManager;

@CommandRegister(name="t")
public class CommandT
extends Command {
    @Override
    public void execute(LiteralArgumentBuilder<class_2172> builder) {
        builder.then(CommandT.argument("module", StringArgumentType.greedyString()).suggests((context, builder1) -> {
            String prefix = builder1.getRemaining().toLowerCase();
            ModuleManager.getInstance().getModules().stream().map(Module::getName).filter(name -> name.toLowerCase().startsWith(prefix)).forEach(arg_0 -> ((SuggestionsBuilder)builder1).suggest(arg_0));
            return builder1.buildFuture();
        }).executes(context -> {
            String moduleName = StringArgumentType.getString((CommandContext)context, (String)"module");
            Module module = this.findModule(moduleName);
            if (module == null) {
                this.print("\u00a7c\u041c\u043e\u0434\u0443\u043b\u044c \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d: \u00a7f" + moduleName);
                return this.SINGLE_SUCCESS;
            }
            module.toggle();
            this.print(module.getName() + (module.isEnabled() ? " \u00a7a\u0432\u043a\u043b\u044e\u0447\u0435\u043d" : " \u00a7c\u0432\u044b\u043a\u043b\u044e\u0447\u0435\u043d"));
            return this.SINGLE_SUCCESS;
        }));
    }

    private Module findModule(String name) {
        for (Module module : ModuleManager.getInstance().getModules()) {
            if (!module.getName().equalsIgnoreCase(name)) continue;
            return module;
        }
        return null;
    }
}


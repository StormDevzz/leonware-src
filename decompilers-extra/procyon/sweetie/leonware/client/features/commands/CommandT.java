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
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;
import java.util.Iterator;
import sweetie.leonware.api.module.ModuleManager;
import sweetie.leonware.api.module.Module;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.class_2172;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import sweetie.leonware.api.command.CommandRegister;
import sweetie.leonware.api.command.Command;

@CommandRegister(name = "t")
public class CommandT extends Command
{
    @Override
    public void execute(final LiteralArgumentBuilder<class_2172> builder) {
        builder.then(Command.argument("module", (com.mojang.brigadier.arguments.ArgumentType<Object>)StringArgumentType.greedyString()).suggests((context, builder1) -> {
            final String prefix = builder1.getRemaining().toLowerCase();
            final Stream<Object> filter = ModuleManager.getInstance().getModules().stream().map((Function<? super Object, ?>)Module::getName).filter(name -> name.toLowerCase().startsWith(prefix));
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
            module.toggle();
            this.print(module.getName() + (module.isEnabled() ? " §a\u0432\u043a\u043b\u044e\u0447\u0435\u043d" : " §c\u0432\u044b\u043a\u043b\u044e\u0447\u0435\u043d"));
            return this.SINGLE_SUCCESS;
        }));
    }
    
    private Module findModule(final String name) {
        for (final Module module : ModuleManager.getInstance().getModules()) {
            if (module.getName().equalsIgnoreCase(name)) {
                return module;
            }
        }
        return null;
    }
}

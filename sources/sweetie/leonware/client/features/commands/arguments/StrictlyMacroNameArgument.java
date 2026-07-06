package sweetie.leonware.client.features.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import sweetie.leonware.api.system.configs.MacroManager;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/commands/arguments/StrictlyMacroNameArgument.class */
public class StrictlyMacroNameArgument implements ArgumentType<String> {
    /* JADX INFO: renamed from: parse, reason: merged with bridge method [inline-methods] */
    public String m220parse(StringReader reader) throws CommandSyntaxException {
        return StringArgumentType.string().parse(reader);
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        List<String> macroNames = MacroManager.getInstance().getMacros().stream().map((v0) -> {
            return v0.getName();
        }).toList();
        String remaining = builder.getRemaining().toLowerCase();
        Stream<String> streamFilter = macroNames.stream().filter(name -> {
            return name.toLowerCase().startsWith(remaining);
        });
        Objects.requireNonNull(builder);
        streamFilter.forEach(builder::suggest);
        return builder.buildFuture();
    }

    public Collection<String> getExamples() {
        return List.of("home", "login", "tp");
    }
}

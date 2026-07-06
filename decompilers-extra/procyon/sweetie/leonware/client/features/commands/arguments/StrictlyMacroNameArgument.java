// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.commands.arguments;

import java.util.Collection;
import java.util.stream.Stream;
import java.util.List;
import java.util.function.Consumer;
import java.util.Objects;
import java.util.function.Function;
import sweetie.leonware.api.system.configs.MacroManager;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;

public class StrictlyMacroNameArgument implements ArgumentType<String>
{
    public String parse(final StringReader reader) throws CommandSyntaxException {
        return StringArgumentType.string().parse(reader);
    }
    
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        final List<String> macroNames = MacroManager.getInstance().getMacros().stream().map((Function<? super Object, ? extends String>)MacroManager.Macro::getName).toList();
        final String remaining = builder.getRemaining().toLowerCase();
        final Stream<Object> filter = macroNames.stream().filter(name -> name.toLowerCase().startsWith(remaining));
        Objects.requireNonNull(builder);
        filter.forEach((Consumer<? super Object>)builder::suggest);
        return builder.buildFuture();
    }
    
    public Collection<String> getExamples() {
        return List.of("home", "login", "tp");
    }
}

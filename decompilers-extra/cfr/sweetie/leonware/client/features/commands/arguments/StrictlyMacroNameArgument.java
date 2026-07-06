/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.StringReader
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.arguments.StringArgumentType
 *  com.mojang.brigadier.context.CommandContext
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 *  com.mojang.brigadier.suggestion.Suggestions
 *  com.mojang.brigadier.suggestion.SuggestionsBuilder
 */
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
import java.util.concurrent.CompletableFuture;
import sweetie.leonware.api.system.configs.MacroManager;

public class StrictlyMacroNameArgument
implements ArgumentType<String> {
    public String parse(StringReader reader) throws CommandSyntaxException {
        return StringArgumentType.string().parse(reader);
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        List<String> macroNames = MacroManager.getInstance().getMacros().stream().map(MacroManager.Macro::getName).toList();
        String remaining = builder.getRemaining().toLowerCase();
        macroNames.stream().filter(name -> name.toLowerCase().startsWith(remaining)).forEach(arg_0 -> ((SuggestionsBuilder)builder).suggest(arg_0));
        return builder.buildFuture();
    }

    public Collection<String> getExamples() {
        return List.of("home", "login", "tp");
    }
}


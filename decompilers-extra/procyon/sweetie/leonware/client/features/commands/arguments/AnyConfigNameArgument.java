// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.commands.arguments;

import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Collection;
import net.minecraft.class_2172;
import sweetie.leonware.api.system.configs.ConfigManager;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;

public class AnyConfigNameArgument implements ArgumentType<String>
{
    public static AnyConfigNameArgument create() {
        return new AnyConfigNameArgument();
    }
    
    public String parse(final StringReader reader) {
        try {
            return reader.readString();
        }
        catch (final CommandSyntaxException e) {
            throw new RuntimeException((Throwable)e);
        }
    }
    
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        return class_2172.method_9265((Iterable)ConfigManager.getInstance().getConfigsNames(), builder);
    }
    
    public Collection<String> getExamples() {
        final List<String> examples = ConfigManager.getInstance().getConfigsNames().stream().limit(5L).collect((Collector<? super Object, ?, List<String>>)Collectors.toList());
        return examples.isEmpty() ? List.of("eva", "donyka", "sex", "swag", "paris") : examples;
    }
}

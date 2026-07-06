// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.commands.arguments;

import com.mojang.brigadier.Message;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Collection;
import net.minecraft.class_2172;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.class_2561;
import sweetie.leonware.api.system.configs.ConfigManager;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;

public class StrictlyConfigNameArgument implements ArgumentType<String>
{
    public static StrictlyConfigNameArgument create() {
        return new StrictlyConfigNameArgument();
    }
    
    public String parse(final StringReader reader) {
        String configName = null;
        try {
            configName = reader.readString();
        }
        catch (final CommandSyntaxException e) {
            throw new RuntimeException((Throwable)e);
        }
        if (!ConfigManager.getInstance().getConfigsNames().contains(configName)) {
            try {
                throw new DynamicCommandExceptionType(name -> class_2561.method_43470("\u041a\u043e\u043d\u0444\u0438\u0433 \u0441 \u0438\u043c\u0435\u043d\u0435\u043c " + String.valueOf(name) + " \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d")).create((Object)configName);
            }
            catch (final CommandSyntaxException e) {
                throw new RuntimeException((Throwable)e);
            }
        }
        return configName;
    }
    
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        return class_2172.method_9265((Iterable)ConfigManager.getInstance().getConfigsNames(), builder);
    }
    
    public Collection<String> getExamples() {
        final List<String> examples = ConfigManager.getInstance().getConfigsNames().stream().limit(5L).collect((Collector<? super Object, ?, List<String>>)Collectors.toList());
        return examples.isEmpty() ? List.of("eva", "donyka", "sex", "swag", "paris") : examples;
    }
}

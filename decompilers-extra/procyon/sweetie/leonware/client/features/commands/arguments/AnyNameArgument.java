// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.commands.arguments;

import net.minecraft.class_640;
import java.util.Collection;
import java.util.List;
import net.minecraft.class_2172;
import java.util.ArrayList;
import net.minecraft.class_310;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;

public class AnyNameArgument implements ArgumentType<String>
{
    public static AnyNameArgument create() {
        return new AnyNameArgument();
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
        final class_310 client = class_310.method_1551();
        final List<String> playerNames = new ArrayList<String>();
        if (client.method_1562() != null) {
            client.method_1562().method_2880().forEach(entry -> playerNames.add(entry.method_2966().getName()));
        }
        return class_2172.method_9265((Iterable)playerNames, builder);
    }
    
    public Collection<String> getExamples() {
        final class_310 client = class_310.method_1551();
        final List<String> examples = new ArrayList<String>();
        if (client.method_1562() != null) {
            client.method_1562().method_2880().stream().limit(5L).forEach(entry -> examples.add(entry.method_2966().getName()));
        }
        if (examples.isEmpty()) {
            examples.addAll(List.of("Evelina", "Donya"));
        }
        return examples;
    }
}

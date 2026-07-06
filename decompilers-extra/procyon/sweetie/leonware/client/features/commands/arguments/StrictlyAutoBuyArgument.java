// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.commands.arguments;

import java.util.Collection;
import net.minecraft.class_2172;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import sweetie.leonware.client.features.modules.other.autobuy.AutoBuyManager;
import java.util.List;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;

public class StrictlyAutoBuyArgument implements ArgumentType<String>
{
    public static StrictlyAutoBuyArgument create() {
        return new StrictlyAutoBuyArgument();
    }
    
    public String parse(final StringReader reader) throws CommandSyntaxException {
        return reader.readString();
    }
    
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        final List<String> names = AutoBuyManager.getInstance().getEntries().stream().map((Function<? super Object, ?>)AutoBuyManager.BuyEntry::getNameContains).collect((Collector<? super Object, ?, List<String>>)Collectors.toList());
        return class_2172.method_9265((Iterable)names, builder);
    }
    
    public Collection<String> getExamples() {
        final List<String> examples = AutoBuyManager.getInstance().getEntries().stream().map((Function<? super Object, ?>)AutoBuyManager.BuyEntry::getNameContains).limit(5L).collect((Collector<? super Object, ?, List<String>>)Collectors.toList());
        return examples.isEmpty() ? List.of("\u041c\u0435\u0447 \u043c\u0430\u0433\u0438\u0441\u0442\u0440\u0430 III") : examples;
    }
}

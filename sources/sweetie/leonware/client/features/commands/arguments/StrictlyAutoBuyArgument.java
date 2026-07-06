package sweetie.leonware.client.features.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import net.minecraft.class_2172;
import sweetie.leonware.client.features.modules.other.autobuy.AutoBuyManager;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/commands/arguments/StrictlyAutoBuyArgument.class */
public class StrictlyAutoBuyArgument implements ArgumentType<String> {
    public static StrictlyAutoBuyArgument create() {
        return new StrictlyAutoBuyArgument();
    }

    /* JADX INFO: renamed from: parse, reason: merged with bridge method [inline-methods] */
    public String m216parse(StringReader reader) throws CommandSyntaxException {
        return reader.readString();
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        List<String> names = (List) AutoBuyManager.getInstance().getEntries().stream().map((v0) -> {
            return v0.getNameContains();
        }).collect(Collectors.toList());
        return class_2172.method_9265(names, builder);
    }

    public Collection<String> getExamples() {
        List<String> examples = (List) AutoBuyManager.getInstance().getEntries().stream().map((v0) -> {
            return v0.getNameContains();
        }).limit(5L).collect(Collectors.toList());
        return examples.isEmpty() ? List.of("Меч магистра III") : examples;
    }
}

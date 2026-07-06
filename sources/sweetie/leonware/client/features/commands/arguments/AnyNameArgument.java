package sweetie.leonware.client.features.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.class_2172;
import net.minecraft.class_310;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/commands/arguments/AnyNameArgument.class */
public class AnyNameArgument implements ArgumentType<String> {
    public static AnyNameArgument create() {
        return new AnyNameArgument();
    }

    /* JADX INFO: renamed from: parse, reason: merged with bridge method [inline-methods] */
    public String m214parse(StringReader reader) {
        try {
            return reader.readString();
        } catch (CommandSyntaxException e) {
            throw new RuntimeException((Throwable) e);
        }
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        class_310 client = class_310.method_1551();
        List<String> playerNames = new ArrayList<>();
        if (client.method_1562() != null) {
            client.method_1562().method_2880().forEach(entry -> {
                playerNames.add(entry.method_2966().getName());
            });
        }
        return class_2172.method_9265(playerNames, builder);
    }

    public Collection<String> getExamples() {
        class_310 client = class_310.method_1551();
        List<String> examples = new ArrayList<>();
        if (client.method_1562() != null) {
            client.method_1562().method_2880().stream().limit(5L).forEach(entry -> {
                examples.add(entry.method_2966().getName());
            });
        }
        if (examples.isEmpty()) {
            examples.addAll(List.of("Evelina", "Donya"));
        }
        return examples;
    }
}

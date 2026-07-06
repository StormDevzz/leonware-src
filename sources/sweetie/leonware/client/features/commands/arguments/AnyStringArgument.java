package sweetie.leonware.client.features.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.concurrent.CompletableFuture;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/commands/arguments/AnyStringArgument.class */
public class AnyStringArgument implements ArgumentType<String> {
    public static AnyStringArgument create() {
        return new AnyStringArgument();
    }

    /* JADX INFO: renamed from: parse, reason: merged with bridge method [inline-methods] */
    public String m215parse(StringReader r) {
        int start = r.getCursor();
        while (r.canRead() && !Character.isWhitespace(r.peek())) {
            r.skip();
        }
        return r.getString().substring(start, r.getCursor());
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return builder.buildFuture();
    }
}

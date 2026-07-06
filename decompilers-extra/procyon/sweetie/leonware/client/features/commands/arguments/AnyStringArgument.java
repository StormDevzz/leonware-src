// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.commands.arguments;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;

public class AnyStringArgument implements ArgumentType<String>
{
    public static AnyStringArgument create() {
        return new AnyStringArgument();
    }
    
    public String parse(final StringReader r) {
        final int start = r.getCursor();
        while (r.canRead() && !Character.isWhitespace(r.peek())) {
            r.skip();
        }
        return r.getString().substring(start, r.getCursor());
    }
    
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        return builder.buildFuture();
    }
}

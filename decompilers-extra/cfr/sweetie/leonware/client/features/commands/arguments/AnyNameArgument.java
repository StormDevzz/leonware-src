/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.StringReader
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.context.CommandContext
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 *  com.mojang.brigadier.suggestion.Suggestions
 *  com.mojang.brigadier.suggestion.SuggestionsBuilder
 *  net.minecraft.class_2172
 *  net.minecraft.class_310
 */
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

public class AnyNameArgument
implements ArgumentType<String> {
    public static AnyNameArgument create() {
        return new AnyNameArgument();
    }

    public String parse(StringReader reader) {
        try {
            return reader.readString();
        }
        catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        class_310 client = class_310.method_1551();
        ArrayList playerNames = new ArrayList();
        if (client.method_1562() != null) {
            client.method_1562().method_2880().forEach(entry -> playerNames.add(entry.method_2966().getName()));
        }
        return class_2172.method_9265(playerNames, (SuggestionsBuilder)builder);
    }

    public Collection<String> getExamples() {
        class_310 client = class_310.method_1551();
        ArrayList<String> examples = new ArrayList<String>();
        if (client.method_1562() != null) {
            client.method_1562().method_2880().stream().limit(5L).forEach(entry -> examples.add(entry.method_2966().getName()));
        }
        if (examples.isEmpty()) {
            examples.addAll(List.of("Evelina", "Donya"));
        }
        return examples;
    }
}


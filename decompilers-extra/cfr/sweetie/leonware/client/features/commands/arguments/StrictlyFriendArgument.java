/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.StringReader
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.context.CommandContext
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 *  com.mojang.brigadier.exceptions.DynamicCommandExceptionType
 *  com.mojang.brigadier.suggestion.Suggestions
 *  com.mojang.brigadier.suggestion.SuggestionsBuilder
 *  net.minecraft.class_2172
 *  net.minecraft.class_2561
 */
package sweetie.leonware.client.features.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import net.minecraft.class_2172;
import net.minecraft.class_2561;
import sweetie.leonware.api.system.configs.FriendManager;

public class StrictlyFriendArgument
implements ArgumentType<String> {
    public static StrictlyFriendArgument create() {
        return new StrictlyFriendArgument();
    }

    public String parse(StringReader reader) {
        String friend = null;
        try {
            friend = reader.readString();
        }
        catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
        if (!FriendManager.getInstance().contains(friend)) {
            try {
                throw new DynamicCommandExceptionType(name -> class_2561.method_43470((String)("\u0414\u0440\u0443\u0433\u0430 \u0441 \u0438\u043c\u0435\u043d\u0435\u043c " + String.valueOf(name) + " \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u043e"))).create((Object)friend);
            }
            catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        return friend;
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return class_2172.method_9265(FriendManager.getInstance().getData(), (SuggestionsBuilder)builder);
    }

    public Collection<String> getExamples() {
        List<String> examples = FriendManager.getInstance().getData().stream().limit(5L).collect(Collectors.toList());
        return examples.isEmpty() ? List.of("eva", "donyka", "sex", "swag", "paris") : examples;
    }
}


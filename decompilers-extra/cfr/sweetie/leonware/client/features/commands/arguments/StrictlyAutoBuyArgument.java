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
 */
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

public class StrictlyAutoBuyArgument
implements ArgumentType<String> {
    public static StrictlyAutoBuyArgument create() {
        return new StrictlyAutoBuyArgument();
    }

    public String parse(StringReader reader) throws CommandSyntaxException {
        return reader.readString();
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        List names = AutoBuyManager.getInstance().getEntries().stream().map(AutoBuyManager.BuyEntry::getNameContains).collect(Collectors.toList());
        return class_2172.method_9265(names, (SuggestionsBuilder)builder);
    }

    public Collection<String> getExamples() {
        List examples = AutoBuyManager.getInstance().getEntries().stream().map(AutoBuyManager.BuyEntry::getNameContains).limit(5L).collect(Collectors.toList());
        return examples.isEmpty() ? List.of("\u041c\u0435\u0447 \u043c\u0430\u0433\u0438\u0441\u0442\u0440\u0430 III") : examples;
    }
}


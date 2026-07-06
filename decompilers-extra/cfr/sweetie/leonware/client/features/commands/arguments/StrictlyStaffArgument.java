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
import sweetie.leonware.api.system.configs.StaffManager;

public class StrictlyStaffArgument
implements ArgumentType<String> {
    public static StrictlyStaffArgument create() {
        return new StrictlyStaffArgument();
    }

    public String parse(StringReader reader) {
        String staff = null;
        try {
            staff = reader.readString();
        }
        catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
        if (!StaffManager.getInstance().contains(staff)) {
            try {
                throw new DynamicCommandExceptionType(name -> class_2561.method_43470((String)("\u041b\u043e\u0445\u0430 \u0441 \u0438\u043c\u0435\u043d\u0435\u043c " + String.valueOf(name) + " \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u043e"))).create((Object)staff);
            }
            catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        return staff;
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return class_2172.method_9265(StaffManager.getInstance().getData(), (SuggestionsBuilder)builder);
    }

    public Collection<String> getExamples() {
        List<String> examples = StaffManager.getInstance().getData().stream().limit(5L).collect(Collectors.toList());
        return examples.isEmpty() ? List.of("eva", "donyka", "sex", "swag", "paris") : examples;
    }
}


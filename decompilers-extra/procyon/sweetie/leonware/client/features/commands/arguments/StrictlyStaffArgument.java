// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.commands.arguments;

import com.mojang.brigadier.Message;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Collection;
import net.minecraft.class_2172;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.class_2561;
import sweetie.leonware.api.system.configs.StaffManager;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;

public class StrictlyStaffArgument implements ArgumentType<String>
{
    public static StrictlyStaffArgument create() {
        return new StrictlyStaffArgument();
    }
    
    public String parse(final StringReader reader) {
        String staff = null;
        try {
            staff = reader.readString();
        }
        catch (final CommandSyntaxException e) {
            throw new RuntimeException((Throwable)e);
        }
        if (!StaffManager.getInstance().contains(staff)) {
            try {
                throw new DynamicCommandExceptionType(name -> class_2561.method_43470("\u041b\u043e\u0445\u0430 \u0441 \u0438\u043c\u0435\u043d\u0435\u043c " + String.valueOf(name) + " \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u043e")).create((Object)staff);
            }
            catch (final CommandSyntaxException e) {
                throw new RuntimeException((Throwable)e);
            }
        }
        return staff;
    }
    
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        return class_2172.method_9265((Iterable)StaffManager.getInstance().getData(), builder);
    }
    
    public Collection<String> getExamples() {
        final List<String> examples = StaffManager.getInstance().getData().stream().limit(5L).collect((Collector<? super Object, ?, List<String>>)Collectors.toList());
        return examples.isEmpty() ? List.of("eva", "donyka", "sex", "swag", "paris") : examples;
    }
}

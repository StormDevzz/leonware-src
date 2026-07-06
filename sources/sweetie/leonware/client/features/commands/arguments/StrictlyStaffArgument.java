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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/commands/arguments/StrictlyStaffArgument.class */
public class StrictlyStaffArgument implements ArgumentType<String> {
    public static StrictlyStaffArgument create() {
        return new StrictlyStaffArgument();
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.mojang.brigadier.exceptions.CommandSyntaxException */
    /* JADX INFO: renamed from: parse, reason: merged with bridge method [inline-methods] */
    public String m221parse(StringReader reader) {
        try {
            String staff = reader.readString();
            if (!StaffManager.getInstance().contains(staff)) {
                try {
                    throw new DynamicCommandExceptionType(name -> {
                        return class_2561.method_43470("Лоха с именем " + String.valueOf(name) + " не найдено");
                    }).create(staff);
                } catch (CommandSyntaxException e) {
                    throw new RuntimeException((Throwable) e);
                }
            }
            return staff;
        } catch (CommandSyntaxException e2) {
            throw new RuntimeException((Throwable) e2);
        }
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return class_2172.method_9265(StaffManager.getInstance().getData(), builder);
    }

    public Collection<String> getExamples() {
        List<String> examples = (List) StaffManager.getInstance().getData().stream().limit(5L).collect(Collectors.toList());
        return examples.isEmpty() ? List.of("eva", "donyka", "sex", "swag", "paris") : examples;
    }
}

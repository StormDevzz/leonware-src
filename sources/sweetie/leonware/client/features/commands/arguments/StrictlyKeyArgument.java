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
import java.util.stream.Stream;
import net.minecraft.class_2172;
import net.minecraft.class_2561;
import org.lwjgl.glfw.GLFW;
import sweetie.leonware.api.system.backend.KeyStorage;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/commands/arguments/StrictlyKeyArgument.class */
public class StrictlyKeyArgument implements ArgumentType<String> {
    public static StrictlyKeyArgument create() {
        return new StrictlyKeyArgument();
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.mojang.brigadier.exceptions.CommandSyntaxException */
    /* JADX INFO: renamed from: parse, reason: merged with bridge method [inline-methods] */
    public String m219parse(StringReader reader) throws CommandSyntaxException {
        String key = reader.readString();
        if (KeyStorage.getBind(key) == -1) {
            throw new DynamicCommandExceptionType(name -> {
                return class_2561.method_43470("Клавиша " + name.toString() + " не найдена");
            }).create(key);
        }
        return key;
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return class_2172.method_9265((Iterable) Stream.of((Object[]) GLFW.class.getDeclaredFields()).filter(f -> {
            return f.getName().startsWith("GLFW_KEY_");
        }).map(f2 -> {
            return f2.getName().substring("GLFW_KEY_".length());
        }).collect(Collectors.toList()), builder);
    }

    public Collection<String> getExamples() {
        return List.of("A", "B", "C", "D", "E");
    }
}

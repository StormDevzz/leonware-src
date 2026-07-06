// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.commands.arguments;

import com.mojang.brigadier.Message;
import java.lang.reflect.Field;
import java.util.Collection;
import net.minecraft.class_2172;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.lwjgl.glfw.GLFW;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.class_2561;
import sweetie.leonware.api.system.backend.KeyStorage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;

public class StrictlyKeyArgument implements ArgumentType<String>
{
    public static StrictlyKeyArgument create() {
        return new StrictlyKeyArgument();
    }
    
    public String parse(final StringReader reader) throws CommandSyntaxException {
        final String key = reader.readString();
        if (KeyStorage.getBind(key) == -1) {
            throw new DynamicCommandExceptionType(name -> class_2561.method_43470("\u041a\u043b\u0430\u0432\u0438\u0448\u0430 " + name.toString() + " \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u0430")).create((Object)key);
        }
        return key;
    }
    
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        return class_2172.method_9265((Iterable)Stream.of(GLFW.class.getDeclaredFields()).filter(f -> f.getName().startsWith("GLFW_KEY_")).map(f -> f.getName().substring("GLFW_KEY_".length())).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()), builder);
    }
    
    public Collection<String> getExamples() {
        return List.of("A", "B", "C", "D", "E");
    }
}

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
 *  org.lwjgl.glfw.GLFW
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
import java.util.stream.Stream;
import net.minecraft.class_2172;
import net.minecraft.class_2561;
import org.lwjgl.glfw.GLFW;
import sweetie.leonware.api.system.backend.KeyStorage;

public class StrictlyKeyArgument
implements ArgumentType<String> {
    public static StrictlyKeyArgument create() {
        return new StrictlyKeyArgument();
    }

    public String parse(StringReader reader) throws CommandSyntaxException {
        String key = reader.readString();
        if (KeyStorage.getBind(key) == -1) {
            throw new DynamicCommandExceptionType(name -> class_2561.method_43470((String)("\u041a\u043b\u0430\u0432\u0438\u0448\u0430 " + name.toString() + " \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u0430"))).create((Object)key);
        }
        return key;
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return class_2172.method_9265((Iterable)Stream.of(GLFW.class.getDeclaredFields()).filter(f -> f.getName().startsWith("GLFW_KEY_")).map(f -> f.getName().substring("GLFW_KEY_".length())).collect(Collectors.toList()), (SuggestionsBuilder)builder);
    }

    public Collection<String> getExamples() {
        return List.of("A", "B", "C", "D", "E");
    }
}


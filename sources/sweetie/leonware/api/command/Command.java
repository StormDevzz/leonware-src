package sweetie.leonware.api.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import lombok.Generated;
import net.minecraft.class_2172;
import sweetie.leonware.api.system.interfaces.QuickImports;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/command/Command.class */
public abstract class Command implements QuickImports {
    private final String name;
    public int SINGLE_SUCCESS = 1;

    public abstract void execute(LiteralArgumentBuilder<class_2172> literalArgumentBuilder);

    @Generated
    public String getName() {
        return this.name;
    }

    @Generated
    public int getSINGLE_SUCCESS() {
        return this.SINGLE_SUCCESS;
    }

    public Command() {
        CommandRegister metadata = (CommandRegister) getClass().getAnnotation(CommandRegister.class);
        this.name = metadata.name();
    }

    public final void register(CommandDispatcher<class_2172> dispatcher) {
        LiteralArgumentBuilder<class_2172> builder = LiteralArgumentBuilder.literal(this.name);
        execute(builder);
        dispatcher.register(builder);
    }

    public static <T> RequiredArgumentBuilder<class_2172, T> argument(String name, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }

    public static LiteralArgumentBuilder<class_2172> literal(String name) {
        return LiteralArgumentBuilder.literal(name);
    }
}

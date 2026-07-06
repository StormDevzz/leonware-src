/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.builder.RequiredArgumentBuilder
 *  lombok.Generated
 *  net.minecraft.class_2172
 */
package sweetie.leonware.api.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import lombok.Generated;
import net.minecraft.class_2172;
import sweetie.leonware.api.command.CommandRegister;
import sweetie.leonware.api.system.interfaces.QuickImports;

public abstract class Command
implements QuickImports {
    private final String name;
    public int SINGLE_SUCCESS = 1;

    public Command() {
        CommandRegister metadata = this.getClass().getAnnotation(CommandRegister.class);
        this.name = metadata.name();
    }

    public abstract void execute(LiteralArgumentBuilder<class_2172> var1);

    public final void register(CommandDispatcher<class_2172> dispatcher) {
        LiteralArgumentBuilder builder = LiteralArgumentBuilder.literal((String)this.name);
        this.execute((LiteralArgumentBuilder<class_2172>)builder);
        dispatcher.register(builder);
    }

    public static <T> RequiredArgumentBuilder<class_2172, T> argument(String name, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument((String)name, type);
    }

    public static LiteralArgumentBuilder<class_2172> literal(String name) {
        return LiteralArgumentBuilder.literal((String)name);
    }

    @Generated
    public String getName() {
        return this.name;
    }

    @Generated
    public int getSINGLE_SUCCESS() {
        return this.SINGLE_SUCCESS;
    }
}


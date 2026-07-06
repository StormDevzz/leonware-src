// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.command;

import lombok.Generated;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.class_2172;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import sweetie.leonware.api.system.interfaces.QuickImports;

public abstract class Command implements QuickImports
{
    private final String name;
    public int SINGLE_SUCCESS;
    
    public Command() {
        this.SINGLE_SUCCESS = 1;
        final CommandRegister metadata = this.getClass().getAnnotation(CommandRegister.class);
        this.name = metadata.name();
    }
    
    public abstract void execute(final LiteralArgumentBuilder<class_2172> p0);
    
    public final void register(final CommandDispatcher<class_2172> dispatcher) {
        final LiteralArgumentBuilder<class_2172> builder = (LiteralArgumentBuilder<class_2172>)LiteralArgumentBuilder.literal(this.name);
        this.execute(builder);
        dispatcher.register((LiteralArgumentBuilder)builder);
    }
    
    public static <T> RequiredArgumentBuilder<class_2172, T> argument(final String name, final ArgumentType<T> type) {
        return (RequiredArgumentBuilder<class_2172, T>)RequiredArgumentBuilder.argument(name, (ArgumentType)type);
    }
    
    public static LiteralArgumentBuilder<class_2172> literal(final String name) {
        return (LiteralArgumentBuilder<class_2172>)LiteralArgumentBuilder.literal(name);
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

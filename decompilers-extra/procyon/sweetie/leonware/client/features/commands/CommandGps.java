// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import sweetie.leonware.api.system.client.GpsManager;
import org.joml.Vector2i;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.class_2172;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import sweetie.leonware.api.command.CommandRegister;
import sweetie.leonware.api.command.Command;

@CommandRegister(name = "gps")
public class CommandGps extends Command
{
    @Override
    public void execute(final LiteralArgumentBuilder<class_2172> builder) {
        builder.then(Command.literal("off").executes(context -> {
            if (GpsManager.getInstance().getGpsPosition() == null) {
                this.print("\u0410 \u0447\u0451 \u0442\u044b \u0432\u044b\u043a\u043b\u044e\u0447\u0438\u0442\u044c \u0441\u043e\u0431\u0438\u0440\u0430\u0435\u0448\u044c\u0441\u044f?");
            }
            else {
                GpsManager.getInstance().setGpsPosition(null);
                this.print("\u041c\u0430\u0440\u0448\u0440\u0443\u0442 \u0443\u0441\u043f\u0435\u0448\u043d\u043e \u0443\u0434\u0430\u043b\u0435\u043d.");
            }
            return this.SINGLE_SUCCESS;
        }));
        builder.then(Command.literal("add").then(Command.argument("x", (com.mojang.brigadier.arguments.ArgumentType<Object>)IntegerArgumentType.integer()).then(Command.argument("z", (com.mojang.brigadier.arguments.ArgumentType<Object>)IntegerArgumentType.integer()).executes(context -> {
            final int x = IntegerArgumentType.getInteger(context, "x");
            final int z = IntegerArgumentType.getInteger(context, "z");
            if (x == 0 && z == 0) {
                this.print("\u0422\u044b \u043d\u0435 \u043c\u043e\u0436\u0435\u0448\u044c \u0443\u043a\u0430\u0437\u0430\u0442\u044c \u043c\u0430\u0440\u0448\u0440\u0443\u0442 \u043d\u0430 \u043d\u0443\u043b\u0435\u0432\u044b\u0435 \u043a\u043e\u043e\u0440\u0434\u0438\u043d\u0430\u0442\u044b.");
            }
            else {
                final Vector2i newPos = new Vector2i(x, z);
                if (GpsManager.getInstance().getGpsPosition() != null && GpsManager.getInstance().getGpsPosition().equals((Object)newPos)) {
                    this.print("\u0422\u0430\u043a\u0430\u044f \u0442\u043e\u0447\u043a\u0430 \u0443\u0436\u0435 \u0435\u0441\u0442\u044c \u0432 \u043c\u0430\u0440\u0448\u0440\u0443\u0442\u0435.");
                }
                else {
                    GpsManager.getInstance().setGpsPosition(newPos);
                    this.print("\u0423\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d \u043c\u0430\u0440\u0448\u0440\u0443\u0442: " + newPos.x + " " + newPos.y);
                }
            }
            return this.SINGLE_SUCCESS;
        }))));
    }
}

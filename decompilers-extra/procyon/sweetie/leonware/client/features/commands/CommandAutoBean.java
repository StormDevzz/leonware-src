// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.commands;

import net.minecraft.class_2338;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import sweetie.leonware.client.features.modules.player.AutoBeansModule;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.class_2172;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import sweetie.leonware.api.command.CommandRegister;
import sweetie.leonware.api.command.Command;

@CommandRegister(name = "autobean")
public class CommandAutoBean extends Command
{
    @Override
    public void execute(final LiteralArgumentBuilder<class_2172> builder) {
        builder.then(Command.literal("pos1").executes(context -> {
            if (CommandAutoBean.mc.field_1724 == null) {
                return this.SINGLE_SUCCESS;
            }
            final class_2338 pos = CommandAutoBean.mc.field_1724.method_24515();
            AutoBeansModule.getInstance().pos1 = pos;
            this.print("§aAutoBean: \u0422\u043e\u0447\u043a\u0430 1 \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d\u0430 \u043d\u0430 " + pos.method_10263() + ", " + pos.method_10264() + ", " + pos.method_10260());
            return this.SINGLE_SUCCESS;
        }));
        builder.then(Command.literal("pos2").executes(context -> {
            if (CommandAutoBean.mc.field_1724 == null) {
                return this.SINGLE_SUCCESS;
            }
            final class_2338 pos = CommandAutoBean.mc.field_1724.method_24515();
            AutoBeansModule.getInstance().pos2 = pos;
            this.print("§aAutoBean: \u0422\u043e\u0447\u043a\u0430 2 \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d\u0430 \u043d\u0430 " + pos.method_10263() + ", " + pos.method_10264() + ", " + pos.method_10260());
            return this.SINGLE_SUCCESS;
        }));
        builder.then(Command.literal("clear").executes(context -> {
            AutoBeansModule.getInstance().pos1 = null;
            AutoBeansModule.getInstance().pos2 = null;
            this.print("§eAutoBean: \u0422\u043e\u0447\u043a\u0438 \u043e\u0447\u0438\u0449\u0435\u043d\u044b.");
            return this.SINGLE_SUCCESS;
        }));
    }
}

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  net.minecraft.class_2172
 *  net.minecraft.class_2338
 */
package sweetie.leonware.client.features.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_2172;
import net.minecraft.class_2338;
import sweetie.leonware.api.command.Command;
import sweetie.leonware.api.command.CommandRegister;
import sweetie.leonware.client.features.modules.player.AutoBeansModule;

@CommandRegister(name="autobean")
public class CommandAutoBean
extends Command {
    @Override
    public void execute(LiteralArgumentBuilder<class_2172> builder) {
        builder.then(CommandAutoBean.literal("pos1").executes(context -> {
            class_2338 pos;
            if (CommandAutoBean.mc.field_1724 == null) {
                return this.SINGLE_SUCCESS;
            }
            AutoBeansModule.getInstance().pos1 = pos = CommandAutoBean.mc.field_1724.method_24515();
            this.print("\u00a7aAutoBean: \u0422\u043e\u0447\u043a\u0430 1 \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d\u0430 \u043d\u0430 " + pos.method_10263() + ", " + pos.method_10264() + ", " + pos.method_10260());
            return this.SINGLE_SUCCESS;
        }));
        builder.then(CommandAutoBean.literal("pos2").executes(context -> {
            class_2338 pos;
            if (CommandAutoBean.mc.field_1724 == null) {
                return this.SINGLE_SUCCESS;
            }
            AutoBeansModule.getInstance().pos2 = pos = CommandAutoBean.mc.field_1724.method_24515();
            this.print("\u00a7aAutoBean: \u0422\u043e\u0447\u043a\u0430 2 \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d\u0430 \u043d\u0430 " + pos.method_10263() + ", " + pos.method_10264() + ", " + pos.method_10260());
            return this.SINGLE_SUCCESS;
        }));
        builder.then(CommandAutoBean.literal("clear").executes(context -> {
            AutoBeansModule.getInstance().pos1 = null;
            AutoBeansModule.getInstance().pos2 = null;
            this.print("\u00a7eAutoBean: \u0422\u043e\u0447\u043a\u0438 \u043e\u0447\u0438\u0449\u0435\u043d\u044b.");
            return this.SINGLE_SUCCESS;
        }));
    }
}


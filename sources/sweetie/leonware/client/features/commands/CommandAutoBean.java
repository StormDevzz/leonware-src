package sweetie.leonware.client.features.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_2172;
import net.minecraft.class_2338;
import sweetie.leonware.api.command.Command;
import sweetie.leonware.api.command.CommandRegister;
import sweetie.leonware.client.features.modules.player.AutoBeansModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/commands/CommandAutoBean.class */
@CommandRegister(name = "autobean")
public class CommandAutoBean extends Command {
    @Override // sweetie.leonware.api.command.Command
    public void execute(LiteralArgumentBuilder<class_2172> builder) {
        builder.then(literal("pos1").executes(context -> {
            if (mc.field_1724 == null) {
                return this.SINGLE_SUCCESS;
            }
            class_2338 pos = mc.field_1724.method_24515();
            AutoBeansModule.getInstance().pos1 = pos;
            print("§aAutoBean: Точка 1 установлена на " + pos.method_10263() + ", " + pos.method_10264() + ", " + pos.method_10260());
            return this.SINGLE_SUCCESS;
        }));
        builder.then(literal("pos2").executes(context2 -> {
            if (mc.field_1724 == null) {
                return this.SINGLE_SUCCESS;
            }
            class_2338 pos = mc.field_1724.method_24515();
            AutoBeansModule.getInstance().pos2 = pos;
            print("§aAutoBean: Точка 2 установлена на " + pos.method_10263() + ", " + pos.method_10264() + ", " + pos.method_10260());
            return this.SINGLE_SUCCESS;
        }));
        builder.then(literal("clear").executes(context3 -> {
            AutoBeansModule.getInstance().pos1 = null;
            AutoBeansModule.getInstance().pos2 = null;
            print("§eAutoBean: Точки очищены.");
            return this.SINGLE_SUCCESS;
        }));
    }
}

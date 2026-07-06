// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.commands;

import java.util.List;
import sweetie.leonware.client.features.modules.combat.AuraModule;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import sweetie.leonware.api.utils.neuro.Neuro2DataCollector;
import net.minecraft.class_2172;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import sweetie.leonware.api.command.CommandRegister;
import sweetie.leonware.api.command.Command;

@CommandRegister(name = "neuro")
public class CommandNeuro extends Command
{
    @Override
    public void execute(final LiteralArgumentBuilder<class_2172> builder) {
        final Neuro2DataCollector collector = Neuro2DataCollector.getInstance();
        builder.then(Command.literal("record").executes(context -> {
            if (collector.isRecording()) {
                this.print("§c[Neuro2] \u0417\u0430\u043f\u0438\u0441\u044c \u0443\u0436\u0435 \u0438\u0434\u0451\u0442!");
                return this.SINGLE_SUCCESS;
            }
            if (!AuraModule.getInstance().isEnabled()) {
                this.print("§c[Neuro2] \u0412\u043a\u043b\u044e\u0447\u0438 Aura!");
                return this.SINGLE_SUCCESS;
            }
            collector.startRecording();
            this.print("§a[Neuro2] \u0417\u0430\u043f\u0438\u0441\u044c \u043d\u0430\u0447\u0430\u0442\u0430. \u0412\u044b\u0431\u0435\u0440\u0438 \u0440\u0435\u0436\u0438\u043c Neuro2 \u0432 Aura \u0438 \u0431\u0435\u0439 \u0446\u0435\u043b\u044c.");
            this.print("§7\u0420\u043e\u0442\u0430\u0446\u0438\u044f \u043e\u0442\u043a\u043b\u044e\u0447\u0435\u043d\u0430 \u2014 \u0432\u043e\u0434\u0438\u0448\u044c \u043c\u044b\u0448\u044c\u044e \u0441\u0430\u043c.");
            return this.SINGLE_SUCCESS;
        }));
        builder.then(Command.literal("stop").executes(context -> {
            if (!collector.isRecording()) {
                this.print("§c[Neuro2] \u0417\u0430\u043f\u0438\u0441\u044c \u043d\u0435 \u0430\u043a\u0442\u0438\u0432\u043d\u0430!");
                return this.SINGLE_SUCCESS;
            }
            collector.stopRecording();
            final int count = AuraModule.getInstance().getNeuro2Model().getDatasetSize();
            this.print("§a[Neuro2] \u0417\u0430\u043f\u0438\u0441\u044c \u043e\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d\u0430. \u041f\u0430\u0442\u0442\u0435\u0440\u043d\u043e\u0432: §f" + count);
            return this.SINGLE_SUCCESS;
        }));
        builder.then(Command.literal("save").then(Command.argument("name", (com.mojang.brigadier.arguments.ArgumentType<Object>)StringArgumentType.string()).executes(context -> {
            final String name = StringArgumentType.getString(context, "name");
            if (AuraModule.getInstance().getNeuro2Model().getDatasetSize() == 0) {
                this.print("§c[Neuro2] \u041d\u0435\u0442 \u0434\u0430\u043d\u043d\u044b\u0445 \u0434\u043b\u044f \u0441\u043e\u0445\u0440\u0430\u043d\u0435\u043d\u0438\u044f!");
                return this.SINGLE_SUCCESS;
            }
            if (collector.save(name)) {
                this.print("§a[Neuro2] \u0421\u043e\u0445\u0440\u0430\u043d\u0435\u043d\u043e §f" + AuraModule.getInstance().getNeuro2Model().getDatasetSize() + " §a\u043f\u0430\u0442\u0442\u0435\u0440\u043d\u043e\u0432 \u2192 §f" + name + ".neuro2");
            }
            else {
                this.print("§c[Neuro2] \u041e\u0448\u0438\u0431\u043a\u0430 \u0441\u043e\u0445\u0440\u0430\u043d\u0435\u043d\u0438\u044f!");
            }
            return this.SINGLE_SUCCESS;
        })));
        builder.then(Command.literal("load").then(Command.argument("name", (com.mojang.brigadier.arguments.ArgumentType<Object>)StringArgumentType.string()).executes(context -> {
            final String name = StringArgumentType.getString(context, "name");
            if (collector.load(name)) {
                this.print("§a[Neuro2] \u0417\u0430\u0433\u0440\u0443\u0436\u0435\u043d\u043e §f" + AuraModule.getInstance().getNeuro2Model().getDatasetSize() + " §a\u043f\u0430\u0442\u0442\u0435\u0440\u043d\u043e\u0432 \u0438\u0437 §f" + name);
            }
            else {
                this.print("§c[Neuro2] \u0424\u0430\u0439\u043b §f" + name + ".neuro2 §c\u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d!");
            }
            return this.SINGLE_SUCCESS;
        })));
        builder.then(Command.literal("clear").executes(context -> {
            AuraModule.getInstance().getNeuro2Model().clear();
            this.print("§e[Neuro2] \u041f\u0430\u0442\u0442\u0435\u0440\u043d\u044b \u043e\u0447\u0438\u0449\u0435\u043d\u044b.");
            return this.SINGLE_SUCCESS;
        }));
        builder.then(Command.literal("list").executes(context -> {
            final List<String> names = collector.getSavedNames();
            if (names.isEmpty()) {
                this.print("§7[Neuro2] \u041d\u0435\u0442 \u0441\u043e\u0445\u0440\u0430\u043d\u0451\u043d\u043d\u044b\u0445 \u043f\u0430\u0442\u0442\u0435\u0440\u043d\u043e\u0432.");
            }
            else {
                this.print("§6[Neuro2] \u0421\u043e\u0445\u0440\u0430\u043d\u0451\u043d\u043d\u044b\u0435 \u043f\u0430\u0442\u0442\u0435\u0440\u043d\u044b:");
                names.forEach(n -> this.print("§f  - " + n));
            }
            return this.SINGLE_SUCCESS;
        }));
        builder.then(Command.literal("status").executes(context -> {
            final int count = AuraModule.getInstance().getNeuro2Model().getDatasetSize();
            final boolean trained = AuraModule.getInstance().getNeuro2Model().isTrained();
            this.print("§6[Neuro2] \u0421\u0442\u0430\u0442\u0443\u0441:");
            this.print("§f  \u041f\u0430\u0442\u0442\u0435\u0440\u043d\u043e\u0432: §a" + count);
            this.print("§f  \u041c\u043e\u0434\u0435\u043b\u044c: " + (trained ? "§a\u041e\u0431\u0443\u0447\u0435\u043d\u0430" : "§c\u041d\u0435 \u043e\u0431\u0443\u0447\u0435\u043d\u0430"));
            this.print("§f  \u0417\u0430\u043f\u0438\u0441\u044c: " + (collector.isRecording() ? "§a\u0410\u043a\u0442\u0438\u0432\u043d\u0430" : "§7\u041d\u0435\u0430\u043a\u0442\u0438\u0432\u043d\u0430"));
            return this.SINGLE_SUCCESS;
        }));
        builder.executes(context -> {
            this.print("§6[Neuro2] \u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435:");
            this.print("§f  $neuro record §7- \u043d\u0430\u0447\u0430\u0442\u044c \u0437\u0430\u043f\u0438\u0441\u044c (Aura \u0434\u043e\u043b\u0436\u043d\u0430 \u0431\u044b\u0442\u044c \u0432\u043a\u043b\u044e\u0447\u0435\u043d\u0430, \u0440\u0435\u0436\u0438\u043c Neuro2)");
            this.print("§f  $neuro stop §7- \u043e\u0441\u0442\u0430\u043d\u043e\u0432\u0438\u0442\u044c \u0437\u0430\u043f\u0438\u0441\u044c");
            this.print("§f  $neuro save <\u0438\u043c\u044f> §7- \u0441\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c \u043f\u0430\u0442\u0442\u0435\u0440\u043d\u044b");
            this.print("§f  $neuro load <\u0438\u043c\u044f> §7- \u0437\u0430\u0433\u0440\u0443\u0437\u0438\u0442\u044c \u043f\u0430\u0442\u0442\u0435\u0440\u043d\u044b");
            this.print("§f  $neuro clear §7- \u043e\u0447\u0438\u0441\u0442\u0438\u0442\u044c \u043f\u0430\u0442\u0442\u0435\u0440\u043d\u044b");
            this.print("§f  $neuro list §7- \u0441\u043f\u0438\u0441\u043e\u043a \u0441\u043e\u0445\u0440\u0430\u043d\u0435\u043d\u0438\u0439");
            this.print("§f  $neuro status §7- \u0441\u0442\u0430\u0442\u0443\u0441");
            return this.SINGLE_SUCCESS;
        });
    }
}

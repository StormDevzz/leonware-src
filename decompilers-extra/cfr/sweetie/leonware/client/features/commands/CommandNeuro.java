/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.arguments.StringArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  net.minecraft.class_2172
 */
package sweetie.leonware.client.features.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import java.util.List;
import net.minecraft.class_2172;
import sweetie.leonware.api.command.Command;
import sweetie.leonware.api.command.CommandRegister;
import sweetie.leonware.api.utils.neuro.Neuro2DataCollector;
import sweetie.leonware.client.features.modules.combat.AuraModule;

@CommandRegister(name="neuro")
public class CommandNeuro
extends Command {
    @Override
    public void execute(LiteralArgumentBuilder<class_2172> builder) {
        Neuro2DataCollector collector = Neuro2DataCollector.getInstance();
        builder.then(CommandNeuro.literal("record").executes(context -> {
            if (collector.isRecording()) {
                this.print("\u00a7c[Neuro2] \u0417\u0430\u043f\u0438\u0441\u044c \u0443\u0436\u0435 \u0438\u0434\u0451\u0442!");
                return this.SINGLE_SUCCESS;
            }
            if (!AuraModule.getInstance().isEnabled()) {
                this.print("\u00a7c[Neuro2] \u0412\u043a\u043b\u044e\u0447\u0438 Aura!");
                return this.SINGLE_SUCCESS;
            }
            collector.startRecording();
            this.print("\u00a7a[Neuro2] \u0417\u0430\u043f\u0438\u0441\u044c \u043d\u0430\u0447\u0430\u0442\u0430. \u0412\u044b\u0431\u0435\u0440\u0438 \u0440\u0435\u0436\u0438\u043c Neuro2 \u0432 Aura \u0438 \u0431\u0435\u0439 \u0446\u0435\u043b\u044c.");
            this.print("\u00a77\u0420\u043e\u0442\u0430\u0446\u0438\u044f \u043e\u0442\u043a\u043b\u044e\u0447\u0435\u043d\u0430 \u2014 \u0432\u043e\u0434\u0438\u0448\u044c \u043c\u044b\u0448\u044c\u044e \u0441\u0430\u043c.");
            return this.SINGLE_SUCCESS;
        }));
        builder.then(CommandNeuro.literal("stop").executes(context -> {
            if (!collector.isRecording()) {
                this.print("\u00a7c[Neuro2] \u0417\u0430\u043f\u0438\u0441\u044c \u043d\u0435 \u0430\u043a\u0442\u0438\u0432\u043d\u0430!");
                return this.SINGLE_SUCCESS;
            }
            collector.stopRecording();
            int count = AuraModule.getInstance().getNeuro2Model().getDatasetSize();
            this.print("\u00a7a[Neuro2] \u0417\u0430\u043f\u0438\u0441\u044c \u043e\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d\u0430. \u041f\u0430\u0442\u0442\u0435\u0440\u043d\u043e\u0432: \u00a7f" + count);
            return this.SINGLE_SUCCESS;
        }));
        builder.then(CommandNeuro.literal("save").then(CommandNeuro.argument("name", StringArgumentType.string()).executes(context -> {
            String name = StringArgumentType.getString((CommandContext)context, (String)"name");
            if (AuraModule.getInstance().getNeuro2Model().getDatasetSize() == 0) {
                this.print("\u00a7c[Neuro2] \u041d\u0435\u0442 \u0434\u0430\u043d\u043d\u044b\u0445 \u0434\u043b\u044f \u0441\u043e\u0445\u0440\u0430\u043d\u0435\u043d\u0438\u044f!");
                return this.SINGLE_SUCCESS;
            }
            if (collector.save(name)) {
                this.print("\u00a7a[Neuro2] \u0421\u043e\u0445\u0440\u0430\u043d\u0435\u043d\u043e \u00a7f" + AuraModule.getInstance().getNeuro2Model().getDatasetSize() + " \u00a7a\u043f\u0430\u0442\u0442\u0435\u0440\u043d\u043e\u0432 \u2192 \u00a7f" + name + ".neuro2");
            } else {
                this.print("\u00a7c[Neuro2] \u041e\u0448\u0438\u0431\u043a\u0430 \u0441\u043e\u0445\u0440\u0430\u043d\u0435\u043d\u0438\u044f!");
            }
            return this.SINGLE_SUCCESS;
        })));
        builder.then(CommandNeuro.literal("load").then(CommandNeuro.argument("name", StringArgumentType.string()).executes(context -> {
            String name = StringArgumentType.getString((CommandContext)context, (String)"name");
            if (collector.load(name)) {
                this.print("\u00a7a[Neuro2] \u0417\u0430\u0433\u0440\u0443\u0436\u0435\u043d\u043e \u00a7f" + AuraModule.getInstance().getNeuro2Model().getDatasetSize() + " \u00a7a\u043f\u0430\u0442\u0442\u0435\u0440\u043d\u043e\u0432 \u0438\u0437 \u00a7f" + name);
            } else {
                this.print("\u00a7c[Neuro2] \u0424\u0430\u0439\u043b \u00a7f" + name + ".neuro2 \u00a7c\u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d!");
            }
            return this.SINGLE_SUCCESS;
        })));
        builder.then(CommandNeuro.literal("clear").executes(context -> {
            AuraModule.getInstance().getNeuro2Model().clear();
            this.print("\u00a7e[Neuro2] \u041f\u0430\u0442\u0442\u0435\u0440\u043d\u044b \u043e\u0447\u0438\u0449\u0435\u043d\u044b.");
            return this.SINGLE_SUCCESS;
        }));
        builder.then(CommandNeuro.literal("list").executes(context -> {
            List<String> names = collector.getSavedNames();
            if (names.isEmpty()) {
                this.print("\u00a77[Neuro2] \u041d\u0435\u0442 \u0441\u043e\u0445\u0440\u0430\u043d\u0451\u043d\u043d\u044b\u0445 \u043f\u0430\u0442\u0442\u0435\u0440\u043d\u043e\u0432.");
            } else {
                this.print("\u00a76[Neuro2] \u0421\u043e\u0445\u0440\u0430\u043d\u0451\u043d\u043d\u044b\u0435 \u043f\u0430\u0442\u0442\u0435\u0440\u043d\u044b:");
                names.forEach(n -> this.print("\u00a7f  - " + n));
            }
            return this.SINGLE_SUCCESS;
        }));
        builder.then(CommandNeuro.literal("status").executes(context -> {
            int count = AuraModule.getInstance().getNeuro2Model().getDatasetSize();
            boolean trained = AuraModule.getInstance().getNeuro2Model().isTrained();
            this.print("\u00a76[Neuro2] \u0421\u0442\u0430\u0442\u0443\u0441:");
            this.print("\u00a7f  \u041f\u0430\u0442\u0442\u0435\u0440\u043d\u043e\u0432: \u00a7a" + count);
            this.print("\u00a7f  \u041c\u043e\u0434\u0435\u043b\u044c: " + (trained ? "\u00a7a\u041e\u0431\u0443\u0447\u0435\u043d\u0430" : "\u00a7c\u041d\u0435 \u043e\u0431\u0443\u0447\u0435\u043d\u0430"));
            this.print("\u00a7f  \u0417\u0430\u043f\u0438\u0441\u044c: " + (collector.isRecording() ? "\u00a7a\u0410\u043a\u0442\u0438\u0432\u043d\u0430" : "\u00a77\u041d\u0435\u0430\u043a\u0442\u0438\u0432\u043d\u0430"));
            return this.SINGLE_SUCCESS;
        }));
        builder.executes(context -> {
            this.print("\u00a76[Neuro2] \u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435:");
            this.print("\u00a7f  $neuro record \u00a77- \u043d\u0430\u0447\u0430\u0442\u044c \u0437\u0430\u043f\u0438\u0441\u044c (Aura \u0434\u043e\u043b\u0436\u043d\u0430 \u0431\u044b\u0442\u044c \u0432\u043a\u043b\u044e\u0447\u0435\u043d\u0430, \u0440\u0435\u0436\u0438\u043c Neuro2)");
            this.print("\u00a7f  $neuro stop \u00a77- \u043e\u0441\u0442\u0430\u043d\u043e\u0432\u0438\u0442\u044c \u0437\u0430\u043f\u0438\u0441\u044c");
            this.print("\u00a7f  $neuro save <\u0438\u043c\u044f> \u00a77- \u0441\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c \u043f\u0430\u0442\u0442\u0435\u0440\u043d\u044b");
            this.print("\u00a7f  $neuro load <\u0438\u043c\u044f> \u00a77- \u0437\u0430\u0433\u0440\u0443\u0437\u0438\u0442\u044c \u043f\u0430\u0442\u0442\u0435\u0440\u043d\u044b");
            this.print("\u00a7f  $neuro clear \u00a77- \u043e\u0447\u0438\u0441\u0442\u0438\u0442\u044c \u043f\u0430\u0442\u0442\u0435\u0440\u043d\u044b");
            this.print("\u00a7f  $neuro list \u00a77- \u0441\u043f\u0438\u0441\u043e\u043a \u0441\u043e\u0445\u0440\u0430\u043d\u0435\u043d\u0438\u0439");
            this.print("\u00a7f  $neuro status \u00a77- \u0441\u0442\u0430\u0442\u0443\u0441");
            return this.SINGLE_SUCCESS;
        });
    }
}


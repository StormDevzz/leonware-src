/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.arguments.LongArgumentType
 *  com.mojang.brigadier.arguments.StringArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  net.minecraft.class_2172
 */
package sweetie.leonware.client.features.commands;

import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import net.minecraft.class_2172;
import sweetie.leonware.api.command.Command;
import sweetie.leonware.api.command.CommandRegister;
import sweetie.leonware.client.features.commands.arguments.StrictlyAutoBuyArgument;
import sweetie.leonware.client.features.modules.other.autobuy.AutoBuyManager;

@CommandRegister(name="autobuy")
public class CommandAutoBuy
extends Command {
    @Override
    public void execute(LiteralArgumentBuilder<class_2172> builder) {
        builder.then(CommandAutoBuy.literal("on").executes(context -> {
            AutoBuyManager.getInstance().setEnabled(true);
            this.print("\u00a77[AutoBuy] \u00a7a\u0412\u043a\u043b\u044e\u0447\u0451\u043d.");
            return this.SINGLE_SUCCESS;
        }));
        builder.then(CommandAutoBuy.literal("off").executes(context -> {
            AutoBuyManager.getInstance().setEnabled(false);
            this.print("\u00a77[AutoBuy] \u00a7c\u0412\u044b\u043a\u043b\u044e\u0447\u0435\u043d.");
            return this.SINGLE_SUCCESS;
        }));
        builder.then(CommandAutoBuy.literal("add").then(CommandAutoBuy.argument("name", StringArgumentType.string()).then(CommandAutoBuy.argument("price", LongArgumentType.longArg((long)0L)).executes(context -> {
            String name = StringArgumentType.getString((CommandContext)context, (String)"name");
            long price = LongArgumentType.getLong((CommandContext)context, (String)"price");
            AutoBuyManager.getInstance().addEntry(name, price);
            this.print("\u00a77[AutoBuy] \u00a7a\u0414\u043e\u0431\u0430\u0432\u043b\u0435\u043d: \u00a7f\"" + name + "\" \u00a77\u043c\u0430\u043a\u0441 \u0446\u0435\u043d\u0430: \u00a7f" + price);
            return this.SINGLE_SUCCESS;
        }))));
        builder.then(CommandAutoBuy.literal("remove").then(CommandAutoBuy.argument("name", StrictlyAutoBuyArgument.create()).executes(context -> {
            String name = (String)context.getArgument("name", String.class);
            boolean removed = AutoBuyManager.getInstance().removeEntry(name);
            if (removed) {
                this.print("\u00a77[AutoBuy] \u00a7a\u0423\u0434\u0430\u043b\u0451\u043d: \u00a7f" + name);
            } else {
                this.print("\u00a77[AutoBuy] \u00a7c\u041f\u0440\u0435\u0434\u043c\u0435\u0442 \u00a7f" + name + " \u00a7c\u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d \u0432 \u0441\u043f\u0438\u0441\u043a\u0435.");
            }
            return this.SINGLE_SUCCESS;
        })));
        builder.then(CommandAutoBuy.literal("list").executes(context -> {
            List<AutoBuyManager.BuyEntry> entries = AutoBuyManager.getInstance().getEntries();
            if (entries.isEmpty()) {
                this.print("\u00a77[AutoBuy] \u00a7e\u0421\u043f\u0438\u0441\u043e\u043a \u043f\u0443\u0441\u0442.");
            } else {
                this.print("\u00a77[AutoBuy] \u00a7f\u0421\u043f\u0438\u0441\u043e\u043a \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u043e\u0432 \u00a77(" + entries.size() + "):");
                for (int i = 0; i < entries.size(); ++i) {
                    AutoBuyManager.BuyEntry e = entries.get(i);
                    this.print("  \u00a77" + (i + 1) + ". \u00a7f\"" + e.getNameContains() + "\" \u00a77\u2014 \u043c\u0430\u043a\u0441: \u00a7f" + e.getMaxPrice());
                }
            }
            return this.SINGLE_SUCCESS;
        }));
        builder.then(CommandAutoBuy.literal("clear").executes(context -> {
            AutoBuyManager.getInstance().clearEntries();
            this.print("\u00a77[AutoBuy] \u00a7e\u0421\u043f\u0438\u0441\u043e\u043a \u043e\u0447\u0438\u0449\u0435\u043d.");
            return this.SINGLE_SUCCESS;
        }));
        builder.then(CommandAutoBuy.literal("history").executes(context -> {
            List<AutoBuyManager.HistoryEntry> history = AutoBuyManager.getInstance().getHistory();
            if (history.isEmpty()) {
                this.print("\u00a77[AutoBuy] \u00a7e\u0418\u0441\u0442\u043e\u0440\u0438\u044f \u043f\u0443\u0441\u0442\u0430.");
            } else {
                this.print("\u00a77[AutoBuy] \u00a7f\u0418\u0441\u0442\u043e\u0440\u0438\u044f \u043f\u043e\u043a\u0443\u043f\u043e\u043a \u00a77(\u043f\u043e\u0441\u043b\u0435\u0434\u043d\u0438\u0435 " + Math.min(history.size(), 10) + "):");
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                for (int i = 0; i < Math.min(history.size(), 10); ++i) {
                    AutoBuyManager.HistoryEntry h = history.get(i);
                    String time = sdf.format(new Date(h.getTimestamp()));
                    this.print("  \u00a77" + (i + 1) + ". \u00a7f[" + time + "] \u00a7f\"" + h.getName() + "\" \u00a77\u2014 \u00a7f" + h.getPrice());
                }
            }
            return this.SINGLE_SUCCESS;
        }));
        builder.then(CommandAutoBuy.literal("delay").then(CommandAutoBuy.argument("clickMs", LongArgumentType.longArg((long)0L)).then(CommandAutoBuy.argument("confirmMs", LongArgumentType.longArg((long)0L)).executes(context -> {
            long click = LongArgumentType.getLong((CommandContext)context, (String)"clickMs");
            long confirm = LongArgumentType.getLong((CommandContext)context, (String)"confirmMs");
            AutoBuyManager.getInstance().setClickDelay(click);
            AutoBuyManager.getInstance().setConfirmDelay(confirm);
            this.print("\u00a77[AutoBuy] \u00a7f\u0417\u0430\u0434\u0435\u0440\u0436\u043a\u0438: \u00a7a\u043a\u043b\u0438\u043a = \u00a7f" + click + " \u043c\u0441\u00a77, \u00a7a\u043f\u043e\u0434\u0442\u0432\u0435\u0440\u0436\u0434\u0435\u043d\u0438\u0435 = \u00a7f" + confirm + " \u043c\u0441");
            return this.SINGLE_SUCCESS;
        }))));
        builder.then(CommandAutoBuy.literal("debug").executes(context -> {
            boolean current = AutoBuyManager.getInstance().isDebug();
            AutoBuyManager.getInstance().setDebug(!current);
            this.print("\u00a77[AutoBuy] \u00a7f\u0414\u0435\u0431\u0430\u0433-\u0441\u043e\u043e\u0431\u0449\u0435\u043d\u0438\u044f: " + (!current ? "\u00a7a\u0412\u043a\u043b\u044e\u0447\u0435\u043d\u044b" : "\u00a7c\u0412\u044b\u043a\u043b\u044e\u0447\u0435\u043d\u044b"));
            return this.SINGLE_SUCCESS;
        }));
        builder.executes(context -> {
            this.print("\u00a77[AutoBuy] \u00a7f\u041a\u043e\u043c\u0430\u043d\u0434\u044b:");
            this.print("  \u00a7f$autobuy on/off \u00a77\u2014 \u0432\u043a\u043b\u044e\u0447\u0438\u0442\u044c/\u0432\u044b\u043a\u043b\u044e\u0447\u0438\u0442\u044c");
            this.print("  \u00a7f$autobuy add \"<\u043d\u0430\u0437\u0432\u0430\u043d\u0438\u0435>\" <\u0446\u0435\u043d\u0430> \u00a77\u2014 \u0434\u043e\u0431\u0430\u0432\u0438\u0442\u044c \u043f\u0440\u0435\u0434\u043c\u0435\u0442");
            this.print("  \u00a7f$autobuy remove \"<\u043d\u0430\u0437\u0432\u0430\u043d\u0438\u0435>\" \u00a77\u2014 \u0443\u0434\u0430\u043b\u0438\u0442\u044c \u043f\u0440\u0435\u0434\u043c\u0435\u0442 (Tab \u0434\u043b\u044f \u043f\u043e\u0434\u0441\u043a\u0430\u0437\u043e\u043a)");
            this.print("  \u00a7f$autobuy list \u00a77\u2014 \u0441\u043f\u0438\u0441\u043e\u043a \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u043e\u0432");
            this.print("  \u00a7f$autobuy clear \u00a77\u2014 \u043e\u0447\u0438\u0441\u0442\u0438\u0442\u044c \u0441\u043f\u0438\u0441\u043e\u043a");
            this.print("  \u00a7f$autobuy history \u00a77\u2014 \u0438\u0441\u0442\u043e\u0440\u0438\u044f \u043f\u043e\u043a\u0443\u043f\u043e\u043a");
            this.print("  \u00a7f$autobuy delay <\u043a\u043b\u0438\u043a \u043c\u0441> <\u043f\u043e\u0434\u0442\u0432\u0435\u0440\u0436\u0434\u0435\u043d\u0438\u0435 \u043c\u0441> \u00a77\u2014 \u0437\u0430\u0434\u0435\u0440\u0436\u043a\u0438 (\u0434\u0435\u0444\u043e\u043b\u0442 300 300)");
            this.print("  \u00a7f$autobuy debug \u00a77\u2014 \u0432\u043a\u043b/\u0432\u044b\u043a\u043b \u0441\u043f\u0430\u043c \u0432 \u0447\u0430\u0442");
            return this.SINGLE_SUCCESS;
        });
    }
}


// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.commands;

import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import sweetie.leonware.client.features.modules.other.autobuy.AutoBuyManager;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;
import sweetie.leonware.client.features.commands.arguments.StrictlyAutoBuyArgument;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.class_2172;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import sweetie.leonware.api.command.CommandRegister;
import sweetie.leonware.api.command.Command;

@CommandRegister(name = "autobuy")
public class CommandAutoBuy extends Command
{
    @Override
    public void execute(final LiteralArgumentBuilder<class_2172> builder) {
        builder.then(Command.literal("on").executes(context -> {
            AutoBuyManager.getInstance().setEnabled(true);
            this.print("§7[AutoBuy] §a\u0412\u043a\u043b\u044e\u0447\u0451\u043d.");
            return this.SINGLE_SUCCESS;
        }));
        builder.then(Command.literal("off").executes(context -> {
            AutoBuyManager.getInstance().setEnabled(false);
            this.print("§7[AutoBuy] §c\u0412\u044b\u043a\u043b\u044e\u0447\u0435\u043d.");
            return this.SINGLE_SUCCESS;
        }));
        builder.then(Command.literal("add").then(Command.argument("name", (com.mojang.brigadier.arguments.ArgumentType<Object>)StringArgumentType.string()).then(Command.argument("price", (com.mojang.brigadier.arguments.ArgumentType<Object>)LongArgumentType.longArg(0L)).executes(context -> {
            final String name = StringArgumentType.getString(context, "name");
            final long price = LongArgumentType.getLong(context, "price");
            AutoBuyManager.getInstance().addEntry(name, price);
            this.print("§7[AutoBuy] §a\u0414\u043e\u0431\u0430\u0432\u043b\u0435\u043d: §f\"" + name + "\" §7\u043c\u0430\u043a\u0441 \u0446\u0435\u043d\u0430: §f" + price);
            return this.SINGLE_SUCCESS;
        }))));
        builder.then(Command.literal("remove").then(Command.argument("name", (com.mojang.brigadier.arguments.ArgumentType<Object>)StrictlyAutoBuyArgument.create()).executes(context -> {
            final String name = (String)context.getArgument("name", (Class)String.class);
            final boolean removed = AutoBuyManager.getInstance().removeEntry(name);
            if (removed) {
                this.print("§7[AutoBuy] §a\u0423\u0434\u0430\u043b\u0451\u043d: §f" + name);
            }
            else {
                this.print("§7[AutoBuy] §c\u041f\u0440\u0435\u0434\u043c\u0435\u0442 §f" + name + " §c\u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d \u0432 \u0441\u043f\u0438\u0441\u043a\u0435.");
            }
            return this.SINGLE_SUCCESS;
        })));
        builder.then(Command.literal("list").executes(context -> {
            final List<AutoBuyManager.BuyEntry> entries = AutoBuyManager.getInstance().getEntries();
            if (entries.isEmpty()) {
                this.print("§7[AutoBuy] §e\u0421\u043f\u0438\u0441\u043e\u043a \u043f\u0443\u0441\u0442.");
            }
            else {
                this.print("§7[AutoBuy] §f\u0421\u043f\u0438\u0441\u043e\u043a \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u043e\u0432 §7(" + entries.size() + "):");
                for (int i = 0; i < entries.size(); ++i) {
                    final AutoBuyManager.BuyEntry e = entries.get(i);
                    this.print("  §7" + (i + 1) + ". §f\"" + e.getNameContains() + "\" §7\u2014 \u043c\u0430\u043a\u0441: §f" + e.getMaxPrice());
                }
            }
            return this.SINGLE_SUCCESS;
        }));
        builder.then(Command.literal("clear").executes(context -> {
            AutoBuyManager.getInstance().clearEntries();
            this.print("§7[AutoBuy] §e\u0421\u043f\u0438\u0441\u043e\u043a \u043e\u0447\u0438\u0449\u0435\u043d.");
            return this.SINGLE_SUCCESS;
        }));
        builder.then(Command.literal("history").executes(context -> {
            final List<AutoBuyManager.HistoryEntry> history = AutoBuyManager.getInstance().getHistory();
            if (history.isEmpty()) {
                this.print("§7[AutoBuy] §e\u0418\u0441\u0442\u043e\u0440\u0438\u044f \u043f\u0443\u0441\u0442\u0430.");
            }
            else {
                this.print("§7[AutoBuy] §f\u0418\u0441\u0442\u043e\u0440\u0438\u044f \u043f\u043e\u043a\u0443\u043f\u043e\u043a §7(\u043f\u043e\u0441\u043b\u0435\u0434\u043d\u0438\u0435 " + Math.min(history.size(), 10) + "):");
                final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                for (int i = 0; i < Math.min(history.size(), 10); ++i) {
                    final AutoBuyManager.HistoryEntry h = history.get(i);
                    final String time = sdf.format(new Date(h.getTimestamp()));
                    this.print("  §7" + (i + 1) + ". §f[" + time + "] §f\"" + h.getName() + "\" §7\u2014 §f" + h.getPrice());
                }
            }
            return this.SINGLE_SUCCESS;
        }));
        builder.then(Command.literal("delay").then(Command.argument("clickMs", (com.mojang.brigadier.arguments.ArgumentType<Object>)LongArgumentType.longArg(0L)).then(Command.argument("confirmMs", (com.mojang.brigadier.arguments.ArgumentType<Object>)LongArgumentType.longArg(0L)).executes(context -> {
            final long click = LongArgumentType.getLong(context, "clickMs");
            final long confirm = LongArgumentType.getLong(context, "confirmMs");
            AutoBuyManager.getInstance().setClickDelay(click);
            AutoBuyManager.getInstance().setConfirmDelay(confirm);
            this.print("§7[AutoBuy] §f\u0417\u0430\u0434\u0435\u0440\u0436\u043a\u0438: §a\u043a\u043b\u0438\u043a = §f" + click + " \u043c\u0441§7, §a\u043f\u043e\u0434\u0442\u0432\u0435\u0440\u0436\u0434\u0435\u043d\u0438\u0435 = §f" + confirm + " \u043c\u0441");
            return this.SINGLE_SUCCESS;
        }))));
        builder.then(Command.literal("debug").executes(context -> {
            final boolean current = AutoBuyManager.getInstance().isDebug();
            AutoBuyManager.getInstance().setDebug(!current);
            this.print("§7[AutoBuy] §f\u0414\u0435\u0431\u0430\u0433-\u0441\u043e\u043e\u0431\u0449\u0435\u043d\u0438\u044f: " + (current ? "§c\u0412\u044b\u043a\u043b\u044e\u0447\u0435\u043d\u044b" : "§a\u0412\u043a\u043b\u044e\u0447\u0435\u043d\u044b"));
            return this.SINGLE_SUCCESS;
        }));
        builder.executes(context -> {
            this.print("§7[AutoBuy] §f\u041a\u043e\u043c\u0430\u043d\u0434\u044b:");
            this.print("  §f$autobuy on/off §7\u2014 \u0432\u043a\u043b\u044e\u0447\u0438\u0442\u044c/\u0432\u044b\u043a\u043b\u044e\u0447\u0438\u0442\u044c");
            this.print("  §f$autobuy add \"<\u043d\u0430\u0437\u0432\u0430\u043d\u0438\u0435>\" <\u0446\u0435\u043d\u0430> §7\u2014 \u0434\u043e\u0431\u0430\u0432\u0438\u0442\u044c \u043f\u0440\u0435\u0434\u043c\u0435\u0442");
            this.print("  §f$autobuy remove \"<\u043d\u0430\u0437\u0432\u0430\u043d\u0438\u0435>\" §7\u2014 \u0443\u0434\u0430\u043b\u0438\u0442\u044c \u043f\u0440\u0435\u0434\u043c\u0435\u0442 (Tab \u0434\u043b\u044f \u043f\u043e\u0434\u0441\u043a\u0430\u0437\u043e\u043a)");
            this.print("  §f$autobuy list §7\u2014 \u0441\u043f\u0438\u0441\u043e\u043a \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u043e\u0432");
            this.print("  §f$autobuy clear §7\u2014 \u043e\u0447\u0438\u0441\u0442\u0438\u0442\u044c \u0441\u043f\u0438\u0441\u043e\u043a");
            this.print("  §f$autobuy history §7\u2014 \u0438\u0441\u0442\u043e\u0440\u0438\u044f \u043f\u043e\u043a\u0443\u043f\u043e\u043a");
            this.print("  §f$autobuy delay <\u043a\u043b\u0438\u043a \u043c\u0441> <\u043f\u043e\u0434\u0442\u0432\u0435\u0440\u0436\u0434\u0435\u043d\u0438\u0435 \u043c\u0441> §7\u2014 \u0437\u0430\u0434\u0435\u0440\u0436\u043a\u0438 (\u0434\u0435\u0444\u043e\u043b\u0442 300 300)");
            this.print("  §f$autobuy debug §7\u2014 \u0432\u043a\u043b/\u0432\u044b\u043a\u043b \u0441\u043f\u0430\u043c \u0432 \u0447\u0430\u0442");
            return this.SINGLE_SUCCESS;
        });
    }
}

package sweetie.leonware.client.features.commands;

import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import net.minecraft.class_2172;
import sweetie.leonware.api.command.Command;
import sweetie.leonware.api.command.CommandRegister;
import sweetie.leonware.client.features.commands.arguments.StrictlyAutoBuyArgument;
import sweetie.leonware.client.features.modules.other.autobuy.AutoBuyManager;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/commands/CommandAutoBuy.class */
@CommandRegister(name = "autobuy")
public class CommandAutoBuy extends Command {
    @Override // sweetie.leonware.api.command.Command
    public void execute(LiteralArgumentBuilder<class_2172> builder) {
        builder.then(literal("on").executes(context -> {
            AutoBuyManager.getInstance().setEnabled(true);
            print("§7[AutoBuy] §aВключён.");
            return this.SINGLE_SUCCESS;
        }));
        builder.then(literal("off").executes(context2 -> {
            AutoBuyManager.getInstance().setEnabled(false);
            print("§7[AutoBuy] §cВыключен.");
            return this.SINGLE_SUCCESS;
        }));
        builder.then(literal("add").then(argument("name", StringArgumentType.string()).then(argument("price", LongArgumentType.longArg(0L)).executes(context3 -> {
            String name = StringArgumentType.getString(context3, "name");
            long price = LongArgumentType.getLong(context3, "price");
            AutoBuyManager.getInstance().addEntry(name, price);
            print("§7[AutoBuy] §aДобавлен: §f\"" + name + "\" §7макс цена: §f" + price);
            return this.SINGLE_SUCCESS;
        }))));
        builder.then(literal("remove").then(argument("name", StrictlyAutoBuyArgument.create()).executes(context4 -> {
            String name = (String) context4.getArgument("name", String.class);
            boolean removed = AutoBuyManager.getInstance().removeEntry(name);
            if (removed) {
                print("§7[AutoBuy] §aУдалён: §f" + name);
            } else {
                print("§7[AutoBuy] §cПредмет §f" + name + " §cне найден в списке.");
            }
            return this.SINGLE_SUCCESS;
        })));
        builder.then(literal("list").executes(context5 -> {
            List<AutoBuyManager.BuyEntry> entries = AutoBuyManager.getInstance().getEntries();
            if (entries.isEmpty()) {
                print("§7[AutoBuy] §eСписок пуст.");
            } else {
                print("§7[AutoBuy] §fСписок предметов §7(" + entries.size() + "):");
                for (int i = 0; i < entries.size(); i++) {
                    AutoBuyManager.BuyEntry e = entries.get(i);
                    print("  §7" + (i + 1) + ". §f\"" + e.getNameContains() + "\" §7— макс: §f" + e.getMaxPrice());
                }
            }
            return this.SINGLE_SUCCESS;
        }));
        builder.then(literal("clear").executes(context6 -> {
            AutoBuyManager.getInstance().clearEntries();
            print("§7[AutoBuy] §eСписок очищен.");
            return this.SINGLE_SUCCESS;
        }));
        builder.then(literal("history").executes(context7 -> {
            List<AutoBuyManager.HistoryEntry> history = AutoBuyManager.getInstance().getHistory();
            if (history.isEmpty()) {
                print("§7[AutoBuy] §eИстория пуста.");
            } else {
                print("§7[AutoBuy] §fИстория покупок §7(последние " + Math.min(history.size(), 10) + "):");
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                for (int i = 0; i < Math.min(history.size(), 10); i++) {
                    AutoBuyManager.HistoryEntry h = history.get(i);
                    String time = sdf.format(new Date(h.getTimestamp()));
                    print("  §7" + (i + 1) + ". §f[" + time + "] §f\"" + h.getName() + "\" §7— §f" + h.getPrice());
                }
            }
            return this.SINGLE_SUCCESS;
        }));
        builder.then(literal("delay").then(argument("clickMs", LongArgumentType.longArg(0L)).then(argument("confirmMs", LongArgumentType.longArg(0L)).executes(context8 -> {
            long click = LongArgumentType.getLong(context8, "clickMs");
            long confirm = LongArgumentType.getLong(context8, "confirmMs");
            AutoBuyManager.getInstance().setClickDelay(click);
            AutoBuyManager.getInstance().setConfirmDelay(confirm);
            print("§7[AutoBuy] §fЗадержки: §aклик = §f" + click + " мс§7, §aподтверждение = §f" + this + " мс");
            return this.SINGLE_SUCCESS;
        }))));
        builder.then(literal("debug").executes(context9 -> {
            boolean current = AutoBuyManager.getInstance().isDebug();
            AutoBuyManager.getInstance().setDebug(!current);
            print("§7[AutoBuy] §fДебаг-сообщения: " + (!current ? "§aВключены" : "§cВыключены"));
            return this.SINGLE_SUCCESS;
        }));
        builder.executes(context10 -> {
            print("§7[AutoBuy] §fКоманды:");
            print("  §f$autobuy on/off §7— включить/выключить");
            print("  §f$autobuy add \"<название>\" <цена> §7— добавить предмет");
            print("  §f$autobuy remove \"<название>\" §7— удалить предмет (Tab для подсказок)");
            print("  §f$autobuy list §7— список предметов");
            print("  §f$autobuy clear §7— очистить список");
            print("  §f$autobuy history §7— история покупок");
            print("  §f$autobuy delay <клик мс> <подтверждение мс> §7— задержки (дефолт 300 300)");
            print("  §f$autobuy debug §7— вкл/выкл спам в чат");
            return this.SINGLE_SUCCESS;
        });
    }
}

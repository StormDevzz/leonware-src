package sweetie.leonware.client.features.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.List;
import net.minecraft.class_2172;
import sweetie.leonware.api.command.Command;
import sweetie.leonware.api.command.CommandRegister;
import sweetie.leonware.api.utils.neuro.Neuro2DataCollector;
import sweetie.leonware.client.features.modules.combat.AuraModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/commands/CommandNeuro.class */
@CommandRegister(name = "neuro")
public class CommandNeuro extends Command {
    @Override // sweetie.leonware.api.command.Command
    public void execute(LiteralArgumentBuilder<class_2172> builder) {
        Neuro2DataCollector collector = Neuro2DataCollector.getInstance();
        builder.then(literal("record").executes(context -> {
            if (collector.isRecording()) {
                print("§c[Neuro2] Запись уже идёт!");
                return this.SINGLE_SUCCESS;
            }
            if (!AuraModule.getInstance().isEnabled()) {
                print("§c[Neuro2] Включи Aura!");
                return this.SINGLE_SUCCESS;
            }
            collector.startRecording();
            print("§a[Neuro2] Запись начата. Выбери режим Neuro2 в Aura и бей цель.");
            print("§7Ротация отключена — водишь мышью сам.");
            return this.SINGLE_SUCCESS;
        }));
        builder.then(literal("stop").executes(context2 -> {
            if (!collector.isRecording()) {
                print("§c[Neuro2] Запись не активна!");
                return this.SINGLE_SUCCESS;
            }
            collector.stopRecording();
            int count = AuraModule.getInstance().getNeuro2Model().getDatasetSize();
            print("§a[Neuro2] Запись остановлена. Паттернов: §f" + count);
            return this.SINGLE_SUCCESS;
        }));
        builder.then(literal("save").then(argument("name", StringArgumentType.string()).executes(context3 -> {
            String name = StringArgumentType.getString(context3, "name");
            if (AuraModule.getInstance().getNeuro2Model().getDatasetSize() == 0) {
                print("§c[Neuro2] Нет данных для сохранения!");
                return this.SINGLE_SUCCESS;
            }
            if (collector.save(name)) {
                print("§a[Neuro2] Сохранено §f" + AuraModule.getInstance().getNeuro2Model().getDatasetSize() + " §aпаттернов → §f" + name + ".neuro2");
            } else {
                print("§c[Neuro2] Ошибка сохранения!");
            }
            return this.SINGLE_SUCCESS;
        })));
        builder.then(literal("load").then(argument("name", StringArgumentType.string()).executes(context4 -> {
            String name = StringArgumentType.getString(context4, "name");
            if (collector.load(name)) {
                print("§a[Neuro2] Загружено §f" + AuraModule.getInstance().getNeuro2Model().getDatasetSize() + " §aпаттернов из §f" + name);
            } else {
                print("§c[Neuro2] Файл §f" + name + ".neuro2 §cне найден!");
            }
            return this.SINGLE_SUCCESS;
        })));
        builder.then(literal("clear").executes(context5 -> {
            AuraModule.getInstance().getNeuro2Model().clear();
            print("§e[Neuro2] Паттерны очищены.");
            return this.SINGLE_SUCCESS;
        }));
        builder.then(literal("list").executes(context6 -> {
            List<String> names = collector.getSavedNames();
            if (names.isEmpty()) {
                print("§7[Neuro2] Нет сохранённых паттернов.");
            } else {
                print("§6[Neuro2] Сохранённые паттерны:");
                names.forEach(n -> {
                    print("§f  - " + n);
                });
            }
            return this.SINGLE_SUCCESS;
        }));
        builder.then(literal("status").executes(context7 -> {
            int count = AuraModule.getInstance().getNeuro2Model().getDatasetSize();
            boolean trained = AuraModule.getInstance().getNeuro2Model().isTrained();
            print("§6[Neuro2] Статус:");
            print("§f  Паттернов: §a" + count);
            print("§f  Модель: " + (trained ? "§aОбучена" : "§cНе обучена"));
            print("§f  Запись: " + (collector.isRecording() ? "§aАктивна" : "§7Неактивна"));
            return this.SINGLE_SUCCESS;
        }));
        builder.executes(context8 -> {
            print("§6[Neuro2] Использование:");
            print("§f  $neuro record §7- начать запись (Aura должна быть включена, режим Neuro2)");
            print("§f  $neuro stop §7- остановить запись");
            print("§f  $neuro save <имя> §7- сохранить паттерны");
            print("§f  $neuro load <имя> §7- загрузить паттерны");
            print("§f  $neuro clear §7- очистить паттерны");
            print("§f  $neuro list §7- список сохранений");
            print("§f  $neuro status §7- статус");
            return this.SINGLE_SUCCESS;
        });
    }
}

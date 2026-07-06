package sweetie.leonware.client.features.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_2172;
import org.joml.Vector2i;
import sweetie.leonware.api.command.Command;
import sweetie.leonware.api.command.CommandRegister;
import sweetie.leonware.api.system.client.GpsManager;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/commands/CommandGps.class */
@CommandRegister(name = "gps")
public class CommandGps extends Command {
    @Override // sweetie.leonware.api.command.Command
    public void execute(LiteralArgumentBuilder<class_2172> builder) {
        builder.then(literal("off").executes(context -> {
            if (GpsManager.getInstance().getGpsPosition() == null) {
                print("А чё ты выключить собираешься?");
            } else {
                GpsManager.getInstance().setGpsPosition(null);
                print("Маршрут успешно удален.");
            }
            return this.SINGLE_SUCCESS;
        }));
        builder.then(literal("add").then(argument("x", IntegerArgumentType.integer()).then(argument("z", IntegerArgumentType.integer()).executes(context2 -> {
            int x = IntegerArgumentType.getInteger(context2, "x");
            int z = IntegerArgumentType.getInteger(context2, "z");
            if (x == 0 && z == 0) {
                print("Ты не можешь указать маршрут на нулевые координаты.");
            } else {
                Vector2i newPos = new Vector2i(x, z);
                if (GpsManager.getInstance().getGpsPosition() != null && GpsManager.getInstance().getGpsPosition().equals(newPos)) {
                    print("Такая точка уже есть в маршруте.");
                } else {
                    GpsManager.getInstance().setGpsPosition(newPos);
                    print("Установлен маршрут: " + newPos.x + " " + newPos.y);
                }
            }
            return this.SINGLE_SUCCESS;
        }))));
    }
}

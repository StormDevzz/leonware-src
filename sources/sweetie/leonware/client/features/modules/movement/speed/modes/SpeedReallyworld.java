package sweetie.leonware.client.features.modules.movement.speed.modes;

import java.util.function.Supplier;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2708;
import net.minecraft.class_2848;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.events.player.move.OnMovePostEvent;
import sweetie.leonware.api.system.client.TimerManager;
import sweetie.leonware.api.utils.other.NetworkUtil;
import sweetie.leonware.api.utils.player.MoveUtil;
import sweetie.leonware.client.features.modules.movement.speed.SpeedMode;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/speed/modes/SpeedReallyworld.class */
public class SpeedReallyworld extends SpeedMode {
    private int ticks = 0;
    private int groundTicks = 0;

    public SpeedReallyworld(Supplier<Boolean> condition) {
    }

    @Override // sweetie.leonware.api.system.backend.Choice, sweetie.leonware.api.module.setting.ModeSetting.NamedChoice
    public String getName() {
        return "Reallyworld";
    }

    @Override // sweetie.leonware.client.features.modules.movement.speed.SpeedMode
    public void onEnable() {
        this.ticks = 0;
        this.groundTicks = 0;
    }

    @Override // sweetie.leonware.client.features.modules.movement.speed.SpeedMode
    public void onDisable() {
        TimerManager.setTimer(1.0f);
        this.ticks = 0;
        this.groundTicks = 0;
    }

    public void onMovePost(OnMovePostEvent.OnMovePostEventData event) {
        TimerManager.setTimer(1.7f);
        if (this.ticks > 3) {
            double bst = 0.03d;
            if (this.ticks % 2 == 0) {
                mc.field_1724.method_45319(new class_243(0.0d, 0.029999999329447746d, 0.0d));
                if (mc.field_1724.method_24828()) {
                    bst = 0.085d;
                } else {
                    bst = 0.03d;
                }
            }
            double yaw = MoveUtil.getDirection();
            double xt = -Math.sin(yaw);
            double zt = Math.cos(yaw);
            if (!MoveUtil.isMoving()) {
                xt = 0.0d;
                zt = 0.0d;
            }
            mc.field_1724.method_45319(new class_243(xt * bst, 0.0d, zt * bst));
        }
        this.ticks++;
    }

    public void onMoveInput() {
        if (mc.field_1724.field_5992) {
            this.groundTicks++;
        } else {
            this.groundTicks = 0;
        }
        if (this.groundTicks >= 1) {
            mc.field_1724.method_6043();
        }
    }

    public void onPostMotion() {
        if (this.ticks % 2 == 0) {
            TimerManager.setTimer(0.3f);
            NetworkUtil.sendSilentPacket((class_2596<?>) new class_2848(mc.field_1724, class_2848.class_2849.field_12982));
        }
    }

    public void onPacket(PacketEvent.PacketEventData event) {
        if (event.packet() instanceof class_2708) {
            if (this.ticks % 2 == 1) {
                this.ticks++;
            }
            TimerManager.setTimer(1.0f);
        }
    }
}

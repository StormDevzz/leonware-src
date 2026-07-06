// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement.speed.modes;

import net.minecraft.class_2708;
import sweetie.leonware.api.event.events.client.PacketEvent;
import net.minecraft.class_2596;
import sweetie.leonware.api.utils.other.NetworkUtil;
import net.minecraft.class_1297;
import net.minecraft.class_2848;
import sweetie.leonware.api.utils.player.MoveUtil;
import net.minecraft.class_243;
import sweetie.leonware.api.event.events.player.move.OnMovePostEvent;
import sweetie.leonware.api.system.client.TimerManager;
import java.util.function.Supplier;
import sweetie.leonware.client.features.modules.movement.speed.SpeedMode;

public class SpeedReallyworld extends SpeedMode
{
    private int ticks;
    private int groundTicks;
    
    public SpeedReallyworld(final Supplier<Boolean> condition) {
        this.ticks = 0;
        this.groundTicks = 0;
    }
    
    @Override
    public String getName() {
        return "Reallyworld";
    }
    
    @Override
    public void onEnable() {
        this.ticks = 0;
        this.groundTicks = 0;
    }
    
    @Override
    public void onDisable() {
        TimerManager.setTimer(1.0f);
        this.ticks = 0;
        this.groundTicks = 0;
    }
    
    public void onMovePost(final OnMovePostEvent.OnMovePostEventData event) {
        TimerManager.setTimer(1.7f);
        if (this.ticks > 3) {
            double bst = 0.03;
            if (this.ticks % 2 == 0) {
                SpeedReallyworld.mc.field_1724.method_45319(new class_243(0.0, 0.029999999329447746, 0.0));
                if (SpeedReallyworld.mc.field_1724.method_24828()) {
                    bst = 0.085;
                }
                else {
                    bst = 0.03;
                }
            }
            final double yaw = MoveUtil.getDirection();
            double xt = -Math.sin(yaw);
            double zt = Math.cos(yaw);
            if (!MoveUtil.isMoving()) {
                xt = 0.0;
                zt = 0.0;
            }
            SpeedReallyworld.mc.field_1724.method_45319(new class_243(xt * bst, 0.0, zt * bst));
        }
        ++this.ticks;
    }
    
    public void onMoveInput() {
        if (SpeedReallyworld.mc.field_1724.field_5992) {
            ++this.groundTicks;
        }
        else {
            this.groundTicks = 0;
        }
        if (this.groundTicks >= 1) {
            SpeedReallyworld.mc.field_1724.method_6043();
        }
    }
    
    public void onPostMotion() {
        if (this.ticks % 2 == 0) {
            TimerManager.setTimer(0.3f);
            NetworkUtil.sendSilentPacket((class_2596<?>)new class_2848((class_1297)SpeedReallyworld.mc.field_1724, class_2848.class_2849.field_12982));
        }
    }
    
    public void onPacket(final PacketEvent.PacketEventData event) {
        if (event.packet() instanceof class_2708) {
            if (this.ticks % 2 == 1) {
                ++this.ticks;
            }
            TimerManager.setTimer(1.0f);
        }
    }
}

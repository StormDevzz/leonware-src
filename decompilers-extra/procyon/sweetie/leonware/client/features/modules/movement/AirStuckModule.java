// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import net.minecraft.class_1297;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.utils.task.TaskPriority;
import sweetie.leonware.api.utils.rotation.manager.RotationStrategy;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import net.minecraft.class_1309;
import sweetie.leonware.api.event.events.player.world.AttackEvent;
import sweetie.leonware.api.event.events.client.DisconnectEvent;
import net.minecraft.class_2596;
import net.minecraft.class_2828;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.events.player.move.MoveEvent;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.utils.other.TextUtil;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.setting.Setting;
import net.minecraft.class_243;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Air Stuck", category = Category.MOVEMENT)
public class AirStuckModule extends Module
{
    private static final AirStuckModule instance;
    private final ModeSetting mode;
    private final BooleanSetting freezeSetting;
    private final BooleanSetting rotateOnAttack;
    private final BooleanSetting onlyCrits;
    private class_243 freezepoziciya;
    private boolean freezeezoshka;
    private long timemessagge;
    private float savedYaw;
    private float savedPitch;
    
    public AirStuckModule() {
        this.mode = new ModeSetting("\u0420\u0435\u0436\u0438\u043c").values("\u041e\u0431\u044b\u0447\u043d\u044b\u0439", "LonyGrief").value("\u041e\u0431\u044b\u0447\u043d\u044b\u0439");
        this.freezeSetting = new BooleanSetting("\u041e\u0442\u043c\u0435\u043d\u044f\u0442\u044c \u0434\u0432\u0438\u0436\u0435\u043d\u0438\u0435").value(false);
        this.rotateOnAttack = new BooleanSetting("\u0420\u043e\u0442\u0430\u0446\u0438\u044f \u043f\u0440\u0438 \u0443\u0434\u0430\u0440\u0435").value(true);
        this.onlyCrits = new BooleanSetting("\u0422\u043e\u043b\u044c\u043a\u043e \u043a\u0440\u0438\u0442\u044b").value(true);
        this.freezepoziciya = class_243.field_1353;
        this.freezeezoshka = false;
        this.timemessagge = 0L;
        this.addSettings(this.mode, this.freezeSetting, this.rotateOnAttack, this.onlyCrits);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.freezeezoshka = false;
        this.timemessagge = 0L;
        if (AirStuckModule.mc.field_1724 != null) {
            this.savedYaw = AirStuckModule.mc.field_1724.method_36454();
            this.savedPitch = AirStuckModule.mc.field_1724.method_36455();
            if (this.mode.is("\u041e\u0431\u044b\u0447\u043d\u044b\u0439")) {
                this.freezepoziciya = AirStuckModule.mc.field_1724.method_19538();
                this.freezeezoshka = true;
            }
        }
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        this.freezeezoshka = false;
    }
    
    @Override
    public void onEvent() {
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (AirStuckModule.mc.field_1724 == null) {
                return;
            }
            else {
                if (this.mode.is("LonyGrief") && !this.freezeezoshka) {
                    final long currentTime = System.currentTimeMillis();
                    if (currentTime - this.timemessagge >= 10000L) {
                        TextUtil.sendMessage("§c \u0416\u0434\u0451\u043c \u043c\u043e\u043c\u0435\u043d\u0442\u0430 \u043a\u043e\u0433\u0434\u0430 \u043d\u0430\u0447\u043d\u0435\u043c \u043f\u0430\u0434\u0430\u0442\u044c");
                        this.timemessagge = currentTime;
                    }
                    if (AirStuckModule.mc.field_1724.field_6017 > 0.0f && AirStuckModule.mc.field_1724.method_18798().field_1351 < 0.0) {
                        this.freezepoziciya = AirStuckModule.mc.field_1724.method_19538();
                        this.freezeezoshka = true;
                        TextUtil.sendMessage("§a \u041d\u0430\u0447\u0430\u043b\u0438 \u0444\u0440\u0438\u0437\u0438\u0442\u044c\u0441\u044f");
                    }
                }
                if (this.freezeezoshka) {
                    AirStuckModule.mc.field_1724.method_18800(0.0, 0.0, 0.0);
                    if (this.onlyCrits.getValue()) {
                        AirStuckModule.mc.field_1724.method_24830(false);
                    }
                }
                return;
            }
        }));
        final EventListener moveEvent = MoveEvent.getInstance().subscribe(new Listener<MoveEvent.MoveEventData>(event -> {
            if (!this.freezeezoshka) {
                return;
            }
            else {
                event.setX(0.0);
                event.setY(0.0);
                event.setZ(0.0);
                MoveEvent.getInstance().setCancel(true);
                return;
            }
        }));
        final EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(event -> {
            if (!this.freezeezoshka) {
                return;
            }
            else {
                if (event.packet() instanceof class_2828) {
                    if (this.freezeSetting.getValue()) {
                        PacketEvent.getInstance().setCancel(true);
                    }
                    else if (this.onlyCrits.getValue()) {
                        final float yaw = this.rotateOnAttack.getValue() ? AirStuckModule.mc.field_1724.method_36454() : this.savedYaw;
                        final float pitch = this.rotateOnAttack.getValue() ? AirStuckModule.mc.field_1724.method_36455() : this.savedPitch;
                        AirStuckModule.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2830(AirStuckModule.mc.field_1724.method_23317(), AirStuckModule.mc.field_1724.method_23318(), AirStuckModule.mc.field_1724.method_23321(), yaw, pitch, false, AirStuckModule.mc.field_1724.field_5976));
                        PacketEvent.getInstance().setCancel(true);
                    }
                }
                return;
            }
        }));
        final EventListener disconnectEvent = DisconnectEvent.getInstance().subscribe(new Listener<DisconnectEvent.DisconnectData>(event -> {
            if (this.isEnabled()) {
                this.disable();
            }
            return;
        }));
        final EventListener attackEvent = AttackEvent.getInstance().subscribe(new Listener<AttackEvent.AttackEventData>(event -> {
            if (!this.freezeezoshka) {
                return;
            }
            else {
                if (this.onlyCrits.getValue()) {
                    AirStuckModule.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_5911(false, AirStuckModule.mc.field_1724.field_5976));
                }
                if (this.rotateOnAttack.getValue()) {
                    final class_1297 patt0$temp = event.entity();
                    if (patt0$temp instanceof final class_1309 target) {
                        final class_243 dir = target.method_33571().method_1020(AirStuckModule.mc.field_1724.method_33571());
                        final float yaw2 = (float)(Math.toDegrees(Math.atan2(dir.field_1350, dir.field_1352)) - 90.0);
                        final float pitch2 = (float)(-Math.toDegrees(Math.atan2(dir.field_1351, Math.sqrt(dir.field_1352 * dir.field_1352 + dir.field_1350 * dir.field_1350))));
                        RotationManager.getInstance().addRotation(new Rotation(yaw2, pitch2), RotationStrategy.TARGET, TaskPriority.HIGH, AirStuckModule.instance);
                    }
                }
                return;
            }
        }));
        this.addEvents(updateEvent, moveEvent, packetEvent, disconnectEvent, attackEvent);
    }
    
    @Generated
    public static AirStuckModule getInstance() {
        return AirStuckModule.instance;
    }
    
    static {
        instance = new AirStuckModule();
    }
}

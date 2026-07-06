/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_243
 *  net.minecraft.class_2596
 *  net.minecraft.class_2828
 *  net.minecraft.class_2828$class_2830
 *  net.minecraft.class_2828$class_5911
 */
package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2828;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.DisconnectEvent;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.events.player.move.MoveEvent;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.event.events.player.world.AttackEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.utils.other.TextUtil;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.utils.rotation.manager.RotationStrategy;
import sweetie.leonware.api.utils.task.TaskPriority;

@ModuleRegister(name="Air Stuck", category=Category.MOVEMENT)
public class AirStuckModule
extends Module {
    private static final AirStuckModule instance = new AirStuckModule();
    private final ModeSetting mode = new ModeSetting("\u0420\u0435\u0436\u0438\u043c").values("\u041e\u0431\u044b\u0447\u043d\u044b\u0439", "LonyGrief").value("\u041e\u0431\u044b\u0447\u043d\u044b\u0439");
    private final BooleanSetting freezeSetting = new BooleanSetting("\u041e\u0442\u043c\u0435\u043d\u044f\u0442\u044c \u0434\u0432\u0438\u0436\u0435\u043d\u0438\u0435").value(false);
    private final BooleanSetting rotateOnAttack = new BooleanSetting("\u0420\u043e\u0442\u0430\u0446\u0438\u044f \u043f\u0440\u0438 \u0443\u0434\u0430\u0440\u0435").value(true);
    private final BooleanSetting onlyCrits = new BooleanSetting("\u0422\u043e\u043b\u044c\u043a\u043e \u043a\u0440\u0438\u0442\u044b").value(true);
    private class_243 freezepoziciya = class_243.field_1353;
    private boolean freezeezoshka = false;
    private long timemessagge = 0L;
    private float savedYaw;
    private float savedPitch;

    public AirStuckModule() {
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
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (AirStuckModule.mc.field_1724 == null) {
                return;
            }
            if (this.mode.is("LonyGrief") && !this.freezeezoshka) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - this.timemessagge >= 10000L) {
                    TextUtil.sendMessage("\u00a7c \u0416\u0434\u0451\u043c \u043c\u043e\u043c\u0435\u043d\u0442\u0430 \u043a\u043e\u0433\u0434\u0430 \u043d\u0430\u0447\u043d\u0435\u043c \u043f\u0430\u0434\u0430\u0442\u044c");
                    this.timemessagge = currentTime;
                }
                if (AirStuckModule.mc.field_1724.field_6017 > 0.0f && AirStuckModule.mc.field_1724.method_18798().field_1351 < 0.0) {
                    this.freezepoziciya = AirStuckModule.mc.field_1724.method_19538();
                    this.freezeezoshka = true;
                    TextUtil.sendMessage("\u00a7a \u041d\u0430\u0447\u0430\u043b\u0438 \u0444\u0440\u0438\u0437\u0438\u0442\u044c\u0441\u044f");
                }
            }
            if (this.freezeezoshka) {
                AirStuckModule.mc.field_1724.method_18800(0.0, 0.0, 0.0);
                if (((Boolean)this.onlyCrits.getValue()).booleanValue()) {
                    AirStuckModule.mc.field_1724.method_24830(false);
                }
            }
        }));
        EventListener moveEvent = MoveEvent.getInstance().subscribe(new Listener<MoveEvent.MoveEventData>(event -> {
            if (!this.freezeezoshka) {
                return;
            }
            event.setX(0.0);
            event.setY(0.0);
            event.setZ(0.0);
            MoveEvent.getInstance().setCancel(true);
        }));
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(event -> {
            if (!this.freezeezoshka) {
                return;
            }
            if (event.packet() instanceof class_2828) {
                if (((Boolean)this.freezeSetting.getValue()).booleanValue()) {
                    PacketEvent.getInstance().setCancel(true);
                } else if (((Boolean)this.onlyCrits.getValue()).booleanValue()) {
                    float yaw = (Boolean)this.rotateOnAttack.getValue() != false ? AirStuckModule.mc.field_1724.method_36454() : this.savedYaw;
                    float pitch = (Boolean)this.rotateOnAttack.getValue() != false ? AirStuckModule.mc.field_1724.method_36455() : this.savedPitch;
                    AirStuckModule.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2830(AirStuckModule.mc.field_1724.method_23317(), AirStuckModule.mc.field_1724.method_23318(), AirStuckModule.mc.field_1724.method_23321(), yaw, pitch, false, AirStuckModule.mc.field_1724.field_5976));
                    PacketEvent.getInstance().setCancel(true);
                }
            }
        }));
        EventListener disconnectEvent = DisconnectEvent.getInstance().subscribe(new Listener<DisconnectEvent.DisconnectData>(event -> {
            if (this.isEnabled()) {
                this.disable();
            }
        }));
        EventListener attackEvent = AttackEvent.getInstance().subscribe(new Listener<AttackEvent.AttackEventData>(event -> {
            class_1297 patt0$temp;
            if (!this.freezeezoshka) {
                return;
            }
            if (((Boolean)this.onlyCrits.getValue()).booleanValue()) {
                AirStuckModule.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_5911(false, AirStuckModule.mc.field_1724.field_5976));
            }
            if (((Boolean)this.rotateOnAttack.getValue()).booleanValue() && (patt0$temp = event.entity()) instanceof class_1309) {
                class_1309 target = (class_1309)patt0$temp;
                class_243 dir = target.method_33571().method_1020(AirStuckModule.mc.field_1724.method_33571());
                float yaw = (float)(Math.toDegrees(Math.atan2(dir.field_1350, dir.field_1352)) - 90.0);
                float pitch = (float)(-Math.toDegrees(Math.atan2(dir.field_1351, Math.sqrt(dir.field_1352 * dir.field_1352 + dir.field_1350 * dir.field_1350))));
                RotationManager.getInstance().addRotation(new Rotation(yaw, pitch), RotationStrategy.TARGET, TaskPriority.HIGH, instance);
            }
        }));
        this.addEvents(updateEvent, moveEvent, packetEvent, disconnectEvent, attackEvent);
    }

    @Generated
    public static AirStuckModule getInstance() {
        return instance;
    }
}


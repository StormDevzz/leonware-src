package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import net.minecraft.class_1309;
import net.minecraft.class_243;
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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/AirStuckModule.class */
@ModuleRegister(name = "Air Stuck", category = Category.MOVEMENT)
public class AirStuckModule extends Module {
    private static final AirStuckModule instance = new AirStuckModule();
    private final ModeSetting mode = new ModeSetting("Режим").values("Обычный", "LonyGrief").value("Обычный");
    private final BooleanSetting freezeSetting = new BooleanSetting("Отменять движение").value((Boolean) false);
    private final BooleanSetting rotateOnAttack = new BooleanSetting("Ротация при ударе").value((Boolean) true);
    private final BooleanSetting onlyCrits = new BooleanSetting("Только криты").value((Boolean) true);
    private class_243 freezepoziciya = class_243.field_1353;
    private boolean freezeezoshka = false;
    private long timemessagge = 0;
    private float savedYaw;
    private float savedPitch;

    @Generated
    public static AirStuckModule getInstance() {
        return instance;
    }

    public AirStuckModule() {
        addSettings(this.mode, this.freezeSetting, this.rotateOnAttack, this.onlyCrits);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onEnable() {
        super.onEnable();
        this.freezeezoshka = false;
        this.timemessagge = 0L;
        if (mc.field_1724 != null) {
            this.savedYaw = mc.field_1724.method_36454();
            this.savedPitch = mc.field_1724.method_36455();
            if (this.mode.is("Обычный")) {
                this.freezepoziciya = mc.field_1724.method_19538();
                this.freezeezoshka = true;
            }
        }
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        super.onDisable();
        this.freezeezoshka = false;
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1724 == null) {
                return;
            }
            if (this.mode.is("LonyGrief") && !this.freezeezoshka) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - this.timemessagge >= 10000) {
                    TextUtil.sendMessage("§c Ждём момента когда начнем падать");
                    this.timemessagge = currentTime;
                }
                if (mc.field_1724.field_6017 > 0.0f && mc.field_1724.method_18798().field_1351 < 0.0d) {
                    this.freezepoziciya = mc.field_1724.method_19538();
                    this.freezeezoshka = true;
                    TextUtil.sendMessage("§a Начали фризиться");
                }
            }
            if (this.freezeezoshka) {
                mc.field_1724.method_18800(0.0d, 0.0d, 0.0d);
                if (this.onlyCrits.getValue().booleanValue()) {
                    mc.field_1724.method_24830(false);
                }
            }
        }));
        EventListener moveEvent = MoveEvent.getInstance().subscribe(new Listener(event2 -> {
            if (this.freezeezoshka) {
                event2.setX(0.0d);
                event2.setY(0.0d);
                event2.setZ(0.0d);
                MoveEvent.getInstance().setCancel(true);
            }
        }));
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener(event3 -> {
            if (this.freezeezoshka && (event3.packet() instanceof class_2828)) {
                if (this.freezeSetting.getValue().booleanValue()) {
                    PacketEvent.getInstance().setCancel(true);
                } else if (this.onlyCrits.getValue().booleanValue()) {
                    float yaw = this.rotateOnAttack.getValue().booleanValue() ? mc.field_1724.method_36454() : this.savedYaw;
                    float pitch = this.rotateOnAttack.getValue().booleanValue() ? mc.field_1724.method_36455() : this.savedPitch;
                    mc.field_1724.field_3944.method_52787(new class_2828.class_2830(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), yaw, pitch, false, mc.field_1724.field_5976));
                    PacketEvent.getInstance().setCancel(true);
                }
            }
        }));
        EventListener disconnectEvent = DisconnectEvent.getInstance().subscribe(new Listener(event4 -> {
            if (isEnabled()) {
                disable();
            }
        }));
        EventListener attackEvent = AttackEvent.getInstance().subscribe(new Listener(event5 -> {
            if (this.freezeezoshka) {
                if (this.onlyCrits.getValue().booleanValue()) {
                    mc.field_1724.field_3944.method_52787(new class_2828.class_5911(false, mc.field_1724.field_5976));
                }
                if (this.rotateOnAttack.getValue().booleanValue()) {
                    class_1309 class_1309VarEntity = event5.entity();
                    if (class_1309VarEntity instanceof class_1309) {
                        class_1309 target = class_1309VarEntity;
                        class_243 dir = target.method_33571().method_1020(mc.field_1724.method_33571());
                        float yaw = (float) (Math.toDegrees(Math.atan2(dir.field_1350, dir.field_1352)) - 90.0d);
                        float pitch = (float) (-Math.toDegrees(Math.atan2(dir.field_1351, Math.sqrt((dir.field_1352 * dir.field_1352) + (dir.field_1350 * dir.field_1350)))));
                        RotationManager.getInstance().addRotation(new Rotation(yaw, pitch), RotationStrategy.TARGET, TaskPriority.HIGH, instance);
                    }
                }
            }
        }));
        addEvents(updateEvent, moveEvent, packetEvent, disconnectEvent, attackEvent);
    }
}

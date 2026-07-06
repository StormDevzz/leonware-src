package sweetie.leonware.client.features.modules.player;

import java.util.Random;
import lombok.Generated;
import net.minecraft.class_1268;
import org.newsclub.net.unix.AFVSOCKSocketAddress;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.math.TimerUtil;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/player/AntiAFKModule.class */
@ModuleRegister(name = "Anti AFK", category = Category.PLAYER)
public class AntiAFKModule extends Module {
    private static final AntiAFKModule instance = new AntiAFKModule();
    private float targetYaw;
    private final SliderSetting delay = new SliderSetting("Задержка").value(Float.valueOf(1000.0f)).range(100.0f, 9000.0f).step(100.0f);
    private final ModeSetting mode = new ModeSetting("Режим").values("Обычный", "Команды").value("Обычный");
    private final BooleanSetting jump = new BooleanSetting("Прыжок").value((Boolean) true);
    private final BooleanSetting swing = new BooleanSetting("Удар рукой").value((Boolean) true);
    private final BooleanSetting rotate = new BooleanSetting("Поворот").value((Boolean) true);
    private final BooleanSetting move = new BooleanSetting("Движение").value((Boolean) true);
    private final TimerUtil timer = new TimerUtil();
    private final Random random = new Random();
    private int moveTicks = 0;
    private int moveDirection = 0;

    @Generated
    public static AntiAFKModule getInstance() {
        return instance;
    }

    public AntiAFKModule() {
        addSettings(this.delay, this.mode, this.jump, this.swing, this.rotate, this.move);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onEnable() {
        this.timer.reset();
        this.moveTicks = 0;
        if (mc.field_1724 != null) {
            this.targetYaw = mc.field_1724.method_36454();
        }
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        this.moveTicks = 0;
        if (mc.field_1724 != null) {
            mc.field_1724.field_3913.field_3905 = 0.0f;
            mc.field_1724.field_3913.field_3907 = 0.0f;
        }
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1724 == null || mc.field_1687 == null) {
                return;
            }
            if (this.timer.finished(this.delay.getValue().longValue())) {
                if (this.mode.is("Команды")) {
                    mc.field_1724.field_3944.method_45730(randomCommand());
                    this.timer.reset();
                    return;
                }
                if (this.jump.getValue().booleanValue() && mc.field_1724.method_24828()) {
                    mc.field_1724.method_6043();
                }
                if (this.swing.getValue().booleanValue()) {
                    mc.field_1724.method_6104(class_1268.field_5808);
                }
                if (this.rotate.getValue().booleanValue()) {
                    mc.field_1724.method_36456(mc.field_1724.method_36454() + ((this.random.nextFloat() - 0.8f) * 2.0f));
                    mc.field_1724.method_36457(mc.field_1724.method_36455() + ((this.random.nextFloat() - 0.8f) * 1.0f));
                }
                if (this.move.getValue().booleanValue() && this.moveTicks <= 0) {
                    this.moveTicks = 10 + this.random.nextInt(10);
                    this.moveDirection = this.random.nextInt(4);
                }
                this.timer.reset();
            }
            if (this.mode.is("Обычный") && this.move.getValue().booleanValue() && this.moveTicks > 0) {
                float forward = 0.0f;
                float sideways = 0.0f;
                switch (this.moveDirection) {
                    case 0:
                        forward = 1.0f;
                        break;
                    case 1:
                        forward = -1.0f;
                        break;
                    case AFVSOCKSocketAddress.VMADDR_CID_HOST /* 2 */:
                        sideways = 1.0f;
                        break;
                    case 3:
                        sideways = -1.0f;
                        break;
                }
                mc.field_1724.field_3913.field_3905 = forward;
                mc.field_1724.field_3913.field_3907 = sideways;
                float yaw = (float) Math.toRadians(mc.field_1724.method_36454());
                double motionX = (((-Math.sin(yaw)) * ((double) forward)) + (Math.cos(yaw) * ((double) sideways))) * ((double) 0.12f);
                double motionZ = ((Math.cos(yaw) * ((double) forward)) + (Math.sin(yaw) * ((double) sideways))) * ((double) 0.12f);
                mc.field_1724.method_18800(motionX, mc.field_1724.method_18798().field_1351, motionZ);
                this.moveTicks--;
                return;
            }
            this.moveTicks = 0;
            mc.field_1724.field_3913.field_3905 = 0.0f;
            mc.field_1724.field_3913.field_3907 = 0.0f;
            if (!this.move.getValue().booleanValue()) {
                mc.field_1724.method_18800(0.0d, mc.field_1724.method_18798().field_1351, 0.0d);
            }
        }));
        addEvents(updateEvent);
    }

    private String randomCommand() {
        int length = 16 + this.random.nextInt(10);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append("abcdefghijklmnopqrstuvwxyz".charAt(this.random.nextInt("abcdefghijklmnopqrstuvwxyz".length())));
        }
        return builder.toString();
    }
}

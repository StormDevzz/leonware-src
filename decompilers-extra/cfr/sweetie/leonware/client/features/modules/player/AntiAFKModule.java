/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1268
 */
package sweetie.leonware.client.features.modules.player;

import java.util.Random;
import lombok.Generated;
import net.minecraft.class_1268;
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

@ModuleRegister(name="Anti AFK", category=Category.PLAYER)
public class AntiAFKModule
extends Module {
    private static final AntiAFKModule instance = new AntiAFKModule();
    private final SliderSetting delay = new SliderSetting("\u0417\u0430\u0434\u0435\u0440\u0436\u043a\u0430").value(Float.valueOf(1000.0f)).range(100.0f, 9000.0f).step(100.0f);
    private final ModeSetting mode = new ModeSetting("\u0420\u0435\u0436\u0438\u043c").values("\u041e\u0431\u044b\u0447\u043d\u044b\u0439", "\u041a\u043e\u043c\u0430\u043d\u0434\u044b").value("\u041e\u0431\u044b\u0447\u043d\u044b\u0439");
    private final BooleanSetting jump = new BooleanSetting("\u041f\u0440\u044b\u0436\u043e\u043a").value(true);
    private final BooleanSetting swing = new BooleanSetting("\u0423\u0434\u0430\u0440 \u0440\u0443\u043a\u043e\u0439").value(true);
    private final BooleanSetting rotate = new BooleanSetting("\u041f\u043e\u0432\u043e\u0440\u043e\u0442").value(true);
    private final BooleanSetting move = new BooleanSetting("\u0414\u0432\u0438\u0436\u0435\u043d\u0438\u0435").value(true);
    private final TimerUtil timer = new TimerUtil();
    private final Random random = new Random();
    private float targetYaw;
    private int moveTicks = 0;
    private int moveDirection = 0;

    public AntiAFKModule() {
        this.addSettings(this.delay, this.mode, this.jump, this.swing, this.rotate, this.move);
    }

    @Override
    public void onEnable() {
        this.timer.reset();
        this.moveTicks = 0;
        if (AntiAFKModule.mc.field_1724 != null) {
            this.targetYaw = AntiAFKModule.mc.field_1724.method_36454();
        }
    }

    @Override
    public void onDisable() {
        this.moveTicks = 0;
        if (AntiAFKModule.mc.field_1724 != null) {
            AntiAFKModule.mc.field_1724.field_3913.field_3905 = 0.0f;
            AntiAFKModule.mc.field_1724.field_3913.field_3907 = 0.0f;
        }
    }

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (AntiAFKModule.mc.field_1724 == null || AntiAFKModule.mc.field_1687 == null) {
                return;
            }
            if (this.timer.finished(((Float)this.delay.getValue()).longValue())) {
                if (this.mode.is("\u041a\u043e\u043c\u0430\u043d\u0434\u044b")) {
                    AntiAFKModule.mc.field_1724.field_3944.method_45730(this.randomCommand());
                    this.timer.reset();
                    return;
                }
                if (((Boolean)this.jump.getValue()).booleanValue() && AntiAFKModule.mc.field_1724.method_24828()) {
                    AntiAFKModule.mc.field_1724.method_6043();
                }
                if (((Boolean)this.swing.getValue()).booleanValue()) {
                    AntiAFKModule.mc.field_1724.method_6104(class_1268.field_5808);
                }
                if (((Boolean)this.rotate.getValue()).booleanValue()) {
                    AntiAFKModule.mc.field_1724.method_36456(AntiAFKModule.mc.field_1724.method_36454() + (this.random.nextFloat() - 0.8f) * 2.0f);
                    AntiAFKModule.mc.field_1724.method_36457(AntiAFKModule.mc.field_1724.method_36455() + (this.random.nextFloat() - 0.8f) * 1.0f);
                }
                if (((Boolean)this.move.getValue()).booleanValue() && this.moveTicks <= 0) {
                    this.moveTicks = 10 + this.random.nextInt(10);
                    this.moveDirection = this.random.nextInt(4);
                }
                this.timer.reset();
            }
            if (this.mode.is("\u041e\u0431\u044b\u0447\u043d\u044b\u0439") && ((Boolean)this.move.getValue()).booleanValue() && this.moveTicks > 0) {
                float speed = 0.12f;
                float forward = 0.0f;
                float sideways = 0.0f;
                switch (this.moveDirection) {
                    case 0: {
                        forward = 1.0f;
                        break;
                    }
                    case 1: {
                        forward = -1.0f;
                        break;
                    }
                    case 2: {
                        sideways = 1.0f;
                        break;
                    }
                    case 3: {
                        sideways = -1.0f;
                    }
                }
                AntiAFKModule.mc.field_1724.field_3913.field_3905 = forward;
                AntiAFKModule.mc.field_1724.field_3913.field_3907 = sideways;
                float yaw = (float)Math.toRadians(AntiAFKModule.mc.field_1724.method_36454());
                double motionX = (-Math.sin(yaw) * (double)forward + Math.cos(yaw) * (double)sideways) * (double)speed;
                double motionZ = (Math.cos(yaw) * (double)forward + Math.sin(yaw) * (double)sideways) * (double)speed;
                AntiAFKModule.mc.field_1724.method_18800(motionX, AntiAFKModule.mc.field_1724.method_18798().field_1351, motionZ);
                --this.moveTicks;
            } else {
                this.moveTicks = 0;
                AntiAFKModule.mc.field_1724.field_3913.field_3905 = 0.0f;
                AntiAFKModule.mc.field_1724.field_3913.field_3907 = 0.0f;
                if (!((Boolean)this.move.getValue()).booleanValue()) {
                    AntiAFKModule.mc.field_1724.method_18800(0.0, AntiAFKModule.mc.field_1724.method_18798().field_1351, 0.0);
                }
            }
        }));
        this.addEvents(updateEvent);
    }

    private String randomCommand() {
        int length = 16 + this.random.nextInt(10);
        String letters = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            builder.append(letters.charAt(this.random.nextInt(letters.length())));
        }
        return builder.toString();
    }

    @Generated
    public static AntiAFKModule getInstance() {
        return instance;
    }
}


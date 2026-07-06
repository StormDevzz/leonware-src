/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1297
 *  net.minecraft.class_2596
 *  net.minecraft.class_2848
 *  net.minecraft.class_2848$class_2849
 */
package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import net.minecraft.class_1297;
import net.minecraft.class_2596;
import net.minecraft.class_2848;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;

@ModuleRegister(name="Sneak", category=Category.MOVEMENT)
public class SneakModule
extends Module {
    private static final SneakModule instance = new SneakModule();
    private final ModeSetting mode = new ModeSetting("\u0420\u0435\u0436\u0438\u043c").values("\u0412\u0430\u043d\u0438\u043b\u044c\u043d\u044b\u0439", "\u041f\u0430\u043a\u0435\u0442\u043d\u044b\u0439").value("\u0412\u0430\u043d\u0438\u043b\u044c\u043d\u044b\u0439");
    private final BooleanSetting twerk = new BooleanSetting("\u0422\u0432\u0435\u0440\u043a").value(false).setVisible(() -> this.mode.is("\u0412\u0430\u043d\u0438\u043b\u044c\u043d\u044b\u0439"));
    private final SliderSetting twerkDelay = new SliderSetting("\u0417\u0430\u0434\u0435\u0440\u0436\u043a\u0430 \u0442\u0432\u0435\u0440\u043a\u0430").value(Float.valueOf(200.0f)).range(50.0f, 1000.0f).step(10.0f).setVisible(() -> this.mode.is("\u0412\u0430\u043d\u0438\u043b\u044c\u043d\u044b\u0439") && (Boolean)this.twerk.getValue() != false);
    private long lastTwerkTime = 0L;
    private boolean twerkState = false;

    public SneakModule() {
        this.addSettings(this.mode, this.twerk, this.twerkDelay);
    }

    @Override
    public void onEnable() {
        this.twerkState = false;
        this.lastTwerkTime = System.currentTimeMillis();
    }

    @Override
    public void onDisable() {
        if (SneakModule.mc.field_1724 == null) {
            return;
        }
        if (this.mode.is("\u0412\u0430\u043d\u0438\u043b\u044c\u043d\u044b\u0439")) {
            SneakModule.mc.field_1724.method_5660(false);
            SneakModule.mc.field_1690.field_1832.method_23481(false);
        } else if (this.mode.is("\u041f\u0430\u043a\u0435\u0442\u043d\u044b\u0439") && mc.method_1562() != null) {
            this.sendPacket((class_2596<?>)new class_2848((class_1297)SneakModule.mc.field_1724, class_2848.class_2849.field_12984));
        }
    }

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (SneakModule.mc.field_1724 == null) {
                return;
            }
            if (this.mode.is("\u0412\u0430\u043d\u0438\u043b\u044c\u043d\u044b\u0439")) {
                this.handleVanilla();
            } else if (this.mode.is("\u041f\u0430\u043a\u0435\u0442\u043d\u044b\u0439")) {
                this.handlePacket();
            }
        }));
        this.addEvents(updateEvent);
    }

    private void handleVanilla() {
        if (((Boolean)this.twerk.getValue()).booleanValue()) {
            long now = System.currentTimeMillis();
            if (now - this.lastTwerkTime >= ((Float)this.twerkDelay.getValue()).longValue()) {
                this.twerkState = !this.twerkState;
                SneakModule.mc.field_1724.method_5660(this.twerkState);
                SneakModule.mc.field_1690.field_1832.method_23481(this.twerkState);
                this.lastTwerkTime = now;
            }
        } else {
            SneakModule.mc.field_1724.method_5660(true);
            SneakModule.mc.field_1690.field_1832.method_23481(true);
        }
    }

    private void handlePacket() {
        this.sendPacket((class_2596<?>)new class_2848((class_1297)SneakModule.mc.field_1724, class_2848.class_2849.field_12979));
    }

    @Generated
    public static SneakModule getInstance() {
        return instance;
    }
}


// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import net.minecraft.class_2596;
import net.minecraft.class_1297;
import net.minecraft.class_2848;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Sneak", category = Category.MOVEMENT)
public class SneakModule extends Module
{
    private static final SneakModule instance;
    private final ModeSetting mode;
    private final BooleanSetting twerk;
    private final SliderSetting twerkDelay;
    private long lastTwerkTime;
    private boolean twerkState;
    
    public SneakModule() {
        this.mode = new ModeSetting("\u0420\u0435\u0436\u0438\u043c").values("\u0412\u0430\u043d\u0438\u043b\u044c\u043d\u044b\u0439", "\u041f\u0430\u043a\u0435\u0442\u043d\u044b\u0439").value("\u0412\u0430\u043d\u0438\u043b\u044c\u043d\u044b\u0439");
        this.twerk = new BooleanSetting("\u0422\u0432\u0435\u0440\u043a").value(false).setVisible(() -> this.mode.is("\u0412\u0430\u043d\u0438\u043b\u044c\u043d\u044b\u0439"));
        this.twerkDelay = new SliderSetting("\u0417\u0430\u0434\u0435\u0440\u0436\u043a\u0430 \u0442\u0432\u0435\u0440\u043a\u0430").value(200.0f).range(50.0f, 1000.0f).step(10.0f).setVisible(() -> this.mode.is("\u0412\u0430\u043d\u0438\u043b\u044c\u043d\u044b\u0439") && this.twerk.getValue());
        this.lastTwerkTime = 0L;
        this.twerkState = false;
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
        }
        else if (this.mode.is("\u041f\u0430\u043a\u0435\u0442\u043d\u044b\u0439") && SneakModule.mc.method_1562() != null) {
            this.sendPacket((class_2596<?>)new class_2848((class_1297)SneakModule.mc.field_1724, class_2848.class_2849.field_12984));
        }
    }
    
    @Override
    public void onEvent() {
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (SneakModule.mc.field_1724 == null) {
                return;
            }
            else {
                if (this.mode.is("\u0412\u0430\u043d\u0438\u043b\u044c\u043d\u044b\u0439")) {
                    this.handleVanilla();
                }
                else if (this.mode.is("\u041f\u0430\u043a\u0435\u0442\u043d\u044b\u0439")) {
                    this.handlePacket();
                }
                return;
            }
        }));
        this.addEvents(updateEvent);
    }
    
    private void handleVanilla() {
        if (this.twerk.getValue()) {
            final long now = System.currentTimeMillis();
            if (now - this.lastTwerkTime >= this.twerkDelay.getValue().longValue()) {
                this.twerkState = !this.twerkState;
                SneakModule.mc.field_1724.method_5660(this.twerkState);
                SneakModule.mc.field_1690.field_1832.method_23481(this.twerkState);
                this.lastTwerkTime = now;
            }
        }
        else {
            SneakModule.mc.field_1724.method_5660(true);
            SneakModule.mc.field_1690.field_1832.method_23481(true);
        }
    }
    
    private void handlePacket() {
        this.sendPacket((class_2596<?>)new class_2848((class_1297)SneakModule.mc.field_1724, class_2848.class_2849.field_12979));
    }
    
    @Generated
    public static SneakModule getInstance() {
        return SneakModule.instance;
    }
    
    static {
        instance = new SneakModule();
    }
}

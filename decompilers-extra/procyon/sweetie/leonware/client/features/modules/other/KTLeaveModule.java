// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.other;

import lombok.Generated;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.events.client.DisconnectEvent;
import sweetie.leonware.api.event.Listener;
import net.minecraft.class_2596;
import net.minecraft.class_2350;
import net.minecraft.class_2338;
import net.minecraft.class_2846;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "KTLeave", category = Category.OTHER)
public class KTLeaveModule extends Module
{
    private static final KTLeaveModule instance;
    public final SliderSetting packets;
    private int ticks;
    
    public KTLeaveModule() {
        this.packets = new SliderSetting("\u041a\u043e\u043b-\u0432\u043e \u043f\u0430\u043a\u0435\u0442\u043e\u0432").value(5.0f).range(1.0f, 7.0f).step(1.0f);
        this.ticks = 0;
        this.addSettings(this.packets);
    }
    
    @Override
    public void onEnable() {
        this.ticks = 0;
    }
    
    @Override
    public void onEvent() {
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (KTLeaveModule.mc.field_1724 == null || KTLeaveModule.mc.field_1687 == null || KTLeaveModule.mc.method_1562() == null) {
                this.disable();
                return;
            }
            else {
                ++this.ticks;
                if (this.ticks >= 1) {
                    this.ticks = 0;
                    for (int count = this.packets.getValue().intValue(), i = 0; i < count; ++i) {
                        KTLeaveModule.mc.method_1562().method_52787((class_2596)new class_2846(class_2846.class_2847.field_12971, class_2338.field_10980, class_2350.field_11033));
                    }
                }
                return;
            }
        }));
        final EventListener disconnectListener = DisconnectEvent.getInstance().subscribe(new Listener<DisconnectEvent.DisconnectData>(event -> this.disable()));
        this.addEvents(updateEvent, disconnectListener);
    }
    
    @Generated
    public static KTLeaveModule getInstance() {
        return KTLeaveModule.instance;
    }
    
    static {
        instance = new KTLeaveModule();
    }
}

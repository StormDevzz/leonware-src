// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import net.minecraft.class_1747;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Delay Helper", category = Category.PLAYER)
public class DelayHelperModule extends Module
{
    private static final DelayHelperModule instance;
    private final BooleanSetting fastUse;
    private final BooleanSetting fastPlace;
    
    public DelayHelperModule() {
        this.fastUse = new BooleanSetting("\u0411\u044b\u0441\u0442\u0440\u043e\u0435 \u0438\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435").value(true);
        this.fastPlace = new BooleanSetting("\u0411\u044b\u0441\u0442\u0440\u0430\u044f \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043a\u0430").value(true);
        this.addSettings(this.fastUse, this.fastPlace);
    }
    
    @Override
    public void onEvent() {
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (DelayHelperModule.mc.field_1724 == null) {
                return;
            }
            else {
                if (this.fastUse.getValue() && !(DelayHelperModule.mc.field_1724.method_6047().method_7909() instanceof class_1747) && !(DelayHelperModule.mc.field_1724.method_6079().method_7909() instanceof class_1747)) {
                    DelayHelperModule.mc.field_1752 = 0;
                }
                if (this.fastPlace.getValue() && (DelayHelperModule.mc.field_1724.method_6047().method_7909() instanceof class_1747 || DelayHelperModule.mc.field_1724.method_6079().method_7909() instanceof class_1747)) {
                    DelayHelperModule.mc.field_1752 = 0;
                }
                return;
            }
        }));
        this.addEvents(updateEvent);
    }
    
    @Generated
    public static DelayHelperModule getInstance() {
        return DelayHelperModule.instance;
    }
    
    static {
        instance = new DelayHelperModule();
    }
}

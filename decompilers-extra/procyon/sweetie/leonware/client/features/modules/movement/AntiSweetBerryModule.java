// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import net.minecraft.class_2338;
import net.minecraft.class_3830;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.utils.player.MoveUtil;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Anti Sweet Berry", category = Category.MOVEMENT)
public class AntiSweetBerryModule extends Module
{
    private static final AntiSweetBerryModule instance;
    private final SliderSetting boostFactor;
    
    public AntiSweetBerryModule() {
        this.boostFactor = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c").value(0.2f).range(0.0f, 1.0f).step(0.01f);
        this.addSettings(this.boostFactor);
    }
    
    @Override
    public void onEvent() {
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (AntiSweetBerryModule.mc.field_1724 == null || AntiSweetBerryModule.mc.field_1687 == null) {
                return;
            }
            else if (!this.isInSweetBerryBush()) {
                return;
            }
            else {
                final double boost = this.boostFactor.getValue();
                final double[] dir = MoveUtil.forward(boost);
                AntiSweetBerryModule.mc.field_1724.method_18800(dir[0], AntiSweetBerryModule.mc.field_1724.method_18798().field_1351, dir[1]);
                return;
            }
        }));
        this.addEvents(updateEvent);
    }
    
    private boolean isInSweetBerryBush() {
        if (AntiSweetBerryModule.mc.field_1687 == null || AntiSweetBerryModule.mc.field_1724 == null) {
            return false;
        }
        final class_2338 pos = AntiSweetBerryModule.mc.field_1724.method_24515();
        return AntiSweetBerryModule.mc.field_1687.method_8320(pos).method_26204() instanceof class_3830 || AntiSweetBerryModule.mc.field_1687.method_8320(pos.method_10074()).method_26204() instanceof class_3830;
    }
    
    @Generated
    public static AntiSweetBerryModule getInstance() {
        return AntiSweetBerryModule.instance;
    }
    
    static {
        instance = new AntiSweetBerryModule();
    }
}

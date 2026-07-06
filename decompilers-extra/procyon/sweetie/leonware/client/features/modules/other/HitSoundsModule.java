// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.other;

import lombok.Generated;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import net.minecraft.class_1657;
import net.minecraft.class_3419;
import sweetie.leonware.api.utils.other.SoundUtil;
import sweetie.leonware.api.event.events.player.world.AttackEvent;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Hit Sounds", category = Category.OTHER)
public class HitSoundsModule extends Module implements QuickImports
{
    private static final HitSoundsModule instance;
    private final SliderSetting volume;
    private final SliderSetting pitch;
    
    public HitSoundsModule() {
        this.volume = new SliderSetting("Volume").value(60.0f).range(1.0f, 100.0f).step(1.0f);
        this.pitch = new SliderSetting("Pitch").value(1.0f).range(0.5f, 2.0f).step(0.05f);
        this.addSettings(this.volume, this.pitch);
    }
    
    @Override
    public void onEvent() {
        final EventListener attackEvent = AttackEvent.getInstance().subscribe(new Listener<AttackEvent.AttackEventData>(e -> {
            if (HitSoundsModule.mc.field_1724 == null || HitSoundsModule.mc.field_1687 == null) {
                return;
            }
            else {
                HitSoundsModule.mc.field_1687.method_8396((class_1657)HitSoundsModule.mc.field_1724, HitSoundsModule.mc.method_1560().method_24515(), SoundUtil.NEVERLOSE_EVENT, class_3419.field_15248, this.volume.getValue() / 100.0f, (float)this.pitch.getValue());
                return;
            }
        }));
        this.addEvents(attackEvent);
    }
    
    @Generated
    public static HitSoundsModule getInstance() {
        return HitSoundsModule.instance;
    }
    
    static {
        instance = new HitSoundsModule();
    }
}

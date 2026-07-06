/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1657
 *  net.minecraft.class_3419
 */
package sweetie.leonware.client.features.modules.other;

import lombok.Generated;
import net.minecraft.class_1657;
import net.minecraft.class_3419;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.world.AttackEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.other.SoundUtil;

@ModuleRegister(name="Hit Sounds", category=Category.OTHER)
public class HitSoundsModule
extends Module
implements QuickImports {
    private static final HitSoundsModule instance = new HitSoundsModule();
    private final SliderSetting volume = new SliderSetting("Volume").value(Float.valueOf(60.0f)).range(1.0f, 100.0f).step(1.0f);
    private final SliderSetting pitch = new SliderSetting("Pitch").value(Float.valueOf(1.0f)).range(0.5f, 2.0f).step(0.05f);

    public HitSoundsModule() {
        this.addSettings(this.volume, this.pitch);
    }

    @Override
    public void onEvent() {
        EventListener attackEvent = AttackEvent.getInstance().subscribe(new Listener<AttackEvent.AttackEventData>(e -> {
            if (HitSoundsModule.mc.field_1724 == null || HitSoundsModule.mc.field_1687 == null) {
                return;
            }
            HitSoundsModule.mc.field_1687.method_8396((class_1657)HitSoundsModule.mc.field_1724, mc.method_1560().method_24515(), SoundUtil.NEVERLOSE_EVENT, class_3419.field_15248, ((Float)this.volume.getValue()).floatValue() / 100.0f, ((Float)this.pitch.getValue()).floatValue());
        }));
        this.addEvents(attackEvent);
    }

    @Generated
    public static HitSoundsModule getInstance() {
        return instance;
    }
}


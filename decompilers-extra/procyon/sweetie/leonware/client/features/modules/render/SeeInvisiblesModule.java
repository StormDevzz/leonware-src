// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.utils.color.ColorUtil;
import java.awt.Color;
import sweetie.leonware.api.event.events.render.EntityColorEvent;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "See Invisibles", category = Category.RENDER)
public class SeeInvisiblesModule extends Module
{
    private static final SeeInvisiblesModule instance;
    private final SliderSetting alpha;
    
    public SeeInvisiblesModule() {
        this.alpha = new SliderSetting("Alpha").value(0.3f).range(0.0f, 1.0f).step(0.1f);
        this.addSettings(this.alpha);
    }
    
    @Override
    public void onEvent() {
        final EventListener entityColorEvent = EntityColorEvent.getInstance().subscribe(new Listener<EntityColorEvent.EntityColorEventData>(event -> {
            final int donichka = (int)(this.alpha.getValue() * 255.0f);
            event.color(ColorUtil.setAlpha(new Color(event.color()), donichka).getRGB());
            EntityColorEvent.getInstance().setCancel(true);
            return;
        }));
        this.addEvents(entityColorEvent);
    }
    
    @Generated
    public static SeeInvisiblesModule getInstance() {
        return SeeInvisiblesModule.instance;
    }
    
    static {
        instance = new SeeInvisiblesModule();
    }
}

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.client.features.modules.render;

import java.awt.Color;
import lombok.Generated;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.render.EntityColorEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.color.ColorUtil;

@ModuleRegister(name="See Invisibles", category=Category.RENDER)
public class SeeInvisiblesModule
extends Module {
    private static final SeeInvisiblesModule instance = new SeeInvisiblesModule();
    private final SliderSetting alpha = new SliderSetting("Alpha").value(Float.valueOf(0.3f)).range(0.0f, 1.0f).step(0.1f);

    public SeeInvisiblesModule() {
        this.addSettings(this.alpha);
    }

    @Override
    public void onEvent() {
        EventListener entityColorEvent = EntityColorEvent.getInstance().subscribe(new Listener<EntityColorEvent.EntityColorEventData>(event -> {
            int donichka = (int)(((Float)this.alpha.getValue()).floatValue() * 255.0f);
            event.color(ColorUtil.setAlpha(new Color(event.color()), donichka).getRGB());
            EntityColorEvent.getInstance().setCancel(true);
        }));
        this.addEvents(entityColorEvent);
    }

    @Generated
    public static SeeInvisiblesModule getInstance() {
        return instance;
    }
}


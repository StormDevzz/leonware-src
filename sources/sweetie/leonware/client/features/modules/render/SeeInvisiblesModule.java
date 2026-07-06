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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/SeeInvisiblesModule.class */
@ModuleRegister(name = "See Invisibles", category = Category.RENDER)
public class SeeInvisiblesModule extends Module {
    private static final SeeInvisiblesModule instance = new SeeInvisiblesModule();
    private final SliderSetting alpha = new SliderSetting("Alpha").value(Float.valueOf(0.3f)).range(0.0f, 1.0f).step(0.1f);

    @Generated
    public static SeeInvisiblesModule getInstance() {
        return instance;
    }

    public SeeInvisiblesModule() {
        addSettings(this.alpha);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener entityColorEvent = EntityColorEvent.getInstance().subscribe(new Listener(event -> {
            int donichka = (int) (this.alpha.getValue().floatValue() * 255.0f);
            event.color(ColorUtil.setAlpha(new Color(event.color()), donichka).getRGB());
            EntityColorEvent.getInstance().setCancel(true);
        }));
        addEvents(entityColorEvent);
    }
}

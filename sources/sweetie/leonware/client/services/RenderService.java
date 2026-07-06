package sweetie.leonware.client.services;

import lombok.Generated;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.other.WindowResizeEvent;
import sweetie.leonware.api.event.events.render.Render2DEvent;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.client.features.modules.render.InterfaceModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/services/RenderService.class */
public class RenderService implements QuickImports {
    private static final RenderService instance = new RenderService();
    private float scale = 1.0f;
    private final Listener<Render2DEvent.Render2DEventData> renderListener = new Listener<>(event -> {
        updateScale();
    });

    @Generated
    public static RenderService getInstance() {
        return instance;
    }

    @Generated
    public float getScale() {
        return this.scale;
    }

    @Generated
    public void setScale(float scale) {
        this.scale = scale;
    }

    @Generated
    public Listener<Render2DEvent.Render2DEventData> getRenderListener() {
        return this.renderListener;
    }

    public void load() {
        WindowResizeEvent.getInstance().subscribe(new Listener(event -> {
            register();
        }));
    }

    private void register() {
        Render2DEvent.getInstance().subscribe((Listener) this.renderListener);
    }

    public float scaled(float value) {
        return value * this.scale;
    }

    public void updateScale() {
        float w = mc.method_22683().method_4486();
        float h = mc.method_22683().method_4502();
        float newScale = Math.max(w / 683.0f, h / 384.0f) * InterfaceModule.getScale();
        if (this.scale == newScale) {
            this.scale = newScale;
            Render2DEvent.getInstance().unsubscribe((Listener) this.renderListener);
        } else {
            this.scale = MathUtil.interpolate(this.scale, newScale, 0.15f);
        }
    }
}

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.client.services;

import lombok.Generated;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.other.WindowResizeEvent;
import sweetie.leonware.api.event.events.render.Render2DEvent;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.client.features.modules.render.InterfaceModule;

public class RenderService
implements QuickImports {
    private static final RenderService instance = new RenderService();
    private float scale = 1.0f;
    private final Listener<Render2DEvent.Render2DEventData> renderListener = new Listener<Render2DEvent.Render2DEventData>(event -> this.updateScale());

    public void load() {
        WindowResizeEvent.getInstance().subscribe(new Listener<WindowResizeEvent>(event -> this.register()));
    }

    private void register() {
        Render2DEvent.getInstance().subscribe(this.renderListener);
    }

    public float scaled(float value) {
        return value * this.scale;
    }

    public void updateScale() {
        float bH;
        float h;
        float bW;
        float w = mc.method_22683().method_4486();
        float newScale = Math.max(w / (bW = 683.0f), (h = (float)mc.method_22683().method_4502()) / (bH = 384.0f)) * InterfaceModule.getScale();
        if (this.scale == newScale) {
            this.scale = newScale;
            Render2DEvent.getInstance().unsubscribe(this.renderListener);
            return;
        }
        this.scale = MathUtil.interpolate(this.scale, newScale, 0.15f);
    }

    @Generated
    public float getScale() {
        return this.scale;
    }

    @Generated
    public Listener<Render2DEvent.Render2DEventData> getRenderListener() {
        return this.renderListener;
    }

    @Generated
    public static RenderService getInstance() {
        return instance;
    }

    @Generated
    public void setScale(float scale) {
        this.scale = scale;
    }
}


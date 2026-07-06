// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.services;

import lombok.Generated;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.client.features.modules.render.InterfaceModule;
import sweetie.leonware.api.event.events.other.WindowResizeEvent;
import sweetie.leonware.api.event.events.render.Render2DEvent;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.system.interfaces.QuickImports;

public class RenderService implements QuickImports
{
    private static final RenderService instance;
    private float scale;
    private final Listener<Render2DEvent.Render2DEventData> renderListener;
    
    public RenderService() {
        this.scale = 1.0f;
        this.renderListener = new Listener<Render2DEvent.Render2DEventData>(event -> this.updateScale());
    }
    
    public void load() {
        WindowResizeEvent.getInstance().subscribe(new Listener<WindowResizeEvent>(event -> this.register()));
    }
    
    private void register() {
        Render2DEvent.getInstance().subscribe(this.renderListener);
    }
    
    public float scaled(final float value) {
        return value * this.scale;
    }
    
    public void updateScale() {
        final float w = (float)RenderService.mc.method_22683().method_4486();
        final float h = (float)RenderService.mc.method_22683().method_4502();
        final float bW = 683.0f;
        final float bH = 384.0f;
        final float newScale = Math.max(w / bW, h / bH) * InterfaceModule.getScale();
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
        return RenderService.instance;
    }
    
    @Generated
    public void setScale(final float scale) {
        this.scale = scale;
    }
    
    static {
        instance = new RenderService();
    }
}

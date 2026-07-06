// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.widget;

import java.util.Objects;
import lombok.Generated;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import sweetie.leonware.api.utils.render.fonts.Font;
import sweetie.leonware.client.services.RenderService;
import sweetie.leonware.api.event.events.render.Render2DEvent;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.client.features.modules.render.InterfaceModule;
import sweetie.leonware.api.system.draggable.DraggableManager;
import sweetie.leonware.api.system.draggable.Draggable;
import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.api.system.interfaces.IRenderer;
import sweetie.leonware.api.system.interfaces.QuickImports;

public abstract class Widget implements QuickImports, IRenderer
{
    private final Easing easing;
    private final long duration = 100L;
    private final Draggable draggable;
    private boolean enabled;
    
    protected Widget(final float x, final float y) {
        this.easing = Easing.SINE_OUT;
        this.draggable = this.create(x, y, this.getName());
    }
    
    public abstract String getName();
    
    private Draggable create(final float x, final float y, final String name) {
        return DraggableManager.getInstance().create(InterfaceModule.getInstance(), name, x, y);
    }
    
    public void render(final Render2DEvent.Render2DEventData event) {
        this.render(event.matrixStack());
    }
    
    public float scaled(final float value) {
        return RenderService.getInstance().scaled(value);
    }
    
    public float getScale() {
        return RenderService.getInstance().getScale();
    }
    
    public float getGap() {
        return this.scaled(3.0f);
    }
    
    public Font getMediumFont() {
        return Fonts.PS_MEDIUM;
    }
    
    public Font getSemiBoldFont() {
        return Fonts.PS_BOLD;
    }
    
    @Generated
    public Easing getEasing() {
        return this.easing;
    }
    
    @Generated
    public long getDuration() {
        Objects.requireNonNull(this);
        return 100L;
    }
    
    @Generated
    public Draggable getDraggable() {
        return this.draggable;
    }
    
    @Generated
    public boolean isEnabled() {
        return this.enabled;
    }
    
    @Generated
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
}

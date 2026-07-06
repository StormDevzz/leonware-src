package sweetie.leonware.client.ui.widget;

import java.util.Objects;
import lombok.Generated;
import sweetie.leonware.api.event.events.render.Render2DEvent;
import sweetie.leonware.api.system.draggable.Draggable;
import sweetie.leonware.api.system.draggable.DraggableManager;
import sweetie.leonware.api.system.interfaces.IRenderer;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.api.utils.render.fonts.Font;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import sweetie.leonware.client.features.modules.render.InterfaceModule;
import sweetie.leonware.client.services.RenderService;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/widget/Widget.class */
public abstract class Widget implements QuickImports, IRenderer {
    private final Easing easing = Easing.SINE_OUT;
    private final long duration = 100;
    private final Draggable draggable;
    private boolean enabled;

    public abstract String getName();

    @Generated
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    protected Widget(float x, float y) {
        this.draggable = create(x, y, getName());
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

    private Draggable create(float x, float y, String name) {
        return DraggableManager.getInstance().create(InterfaceModule.getInstance(), name, x, y);
    }

    public void render(Render2DEvent.Render2DEventData event) {
        render(event.matrixStack());
    }

    public float scaled(float value) {
        return RenderService.getInstance().scaled(value);
    }

    public float getScale() {
        return RenderService.getInstance().getScale();
    }

    public float getGap() {
        return scaled(3.0f);
    }

    public Font getMediumFont() {
        return Fonts.PS_MEDIUM;
    }

    public Font getSemiBoldFont() {
        return Fonts.PS_BOLD;
    }
}

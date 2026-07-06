// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.module;

import sweetie.leonware.client.ui.widget.Widget;
import lombok.Generated;
import sweetie.leonware.client.ui.widget.WidgetManager;
import sweetie.leonware.client.ui.widget.overlay.NotifWidget;
import sweetie.leonware.client.features.modules.other.ToggleSoundsModule;
import sweetie.leonware.client.features.modules.render.ClickGUIModule;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.system.backend.Configurable;

public abstract class Module extends Configurable implements QuickImports
{
    private final String name;
    private final Category category;
    private int bind;
    private boolean enabled;
    
    public Module() {
        final ModuleRegister data = this.getClass().getAnnotation(ModuleRegister.class);
        if (data == null) {
            try {
                throw new Exception("No data for " + this.getClass().getName());
            }
            catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }
        this.name = data.name();
        this.category = data.category();
        this.bind = data.bind();
    }
    
    public boolean hasBind() {
        return this.bind != -999;
    }
    
    public void toggle() {
        this.setEnabled(!this.enabled, false, false);
    }
    
    public void toggle(final boolean fromBind) {
        this.setEnabled(!this.enabled, false, fromBind);
    }
    
    public void setEnabled(final boolean newState) {
        this.setEnabled(newState, false, false);
    }
    
    public void setEnabled(final boolean newState, final boolean config) {
        this.setEnabled(newState, config, false);
    }
    
    public void setEnabled(final boolean newState, final boolean config, final boolean fromBind) {
        if (this.enabled == newState) {
            return;
        }
        this.enabled = newState;
        if (this.enabled) {
            this.onEnable();
            this.onEvent();
        }
        else {
            this.onDisable();
            this.removeAllEvents();
        }
        if (config || this instanceof ClickGUIModule) {
            return;
        }
        ToggleSoundsModule.playToggle(newState);
        if (fromBind) {
            final NotifWidget widget = WidgetManager.getInstance().getWidgets().stream().filter(w -> w instanceof NotifWidget).findFirst().orElse(null);
            if (widget != null && widget.notifTypes.isEnabled("\u0421\u043e\u0441\u0442\u043e\u044f\u043d\u0438\u0435 \u043c\u043e\u0434\u0443\u043b\u0435\u0439")) {
                widget.addNotif(this.name + (newState ? " §a\u0432\u043a\u043b\u044e\u0447\u0435\u043d" : " §c\u0432\u044b\u043a\u043b\u044e\u0447\u0435\u043d"));
            }
        }
    }
    
    public String getDisplayInfo() {
        return null;
    }
    
    @Override
    public abstract void onEvent();
    
    public void disable() {
        this.setEnabled(false);
    }
    
    public void enable() {
        this.setEnabled(true);
    }
    
    public void onEnable() {
    }
    
    public void onDisable() {
    }
    
    @Generated
    public String getName() {
        return this.name;
    }
    
    @Generated
    public Category getCategory() {
        return this.category;
    }
    
    @Generated
    public int getBind() {
        return this.bind;
    }
    
    @Generated
    public boolean isEnabled() {
        return this.enabled;
    }
    
    @Generated
    public void setBind(final int bind) {
        this.bind = bind;
    }
}

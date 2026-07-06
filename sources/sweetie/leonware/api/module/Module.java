package sweetie.leonware.api.module;

import lombok.Generated;
import sweetie.leonware.api.system.backend.Configurable;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.client.features.modules.other.ToggleSoundsModule;
import sweetie.leonware.client.features.modules.render.ClickGUIModule;
import sweetie.leonware.client.ui.widget.WidgetManager;
import sweetie.leonware.client.ui.widget.overlay.NotifWidget;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/module/Module.class */
public abstract class Module extends Configurable implements QuickImports {
    private final String name;
    private final Category category;
    private int bind;
    private boolean enabled;

    @Override // sweetie.leonware.api.system.backend.Configurable
    public abstract void onEvent();

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
    public void setBind(int bind) {
        this.bind = bind;
    }

    @Generated
    public boolean isEnabled() {
        return this.enabled;
    }

    public Module() {
        ModuleRegister data = (ModuleRegister) getClass().getAnnotation(ModuleRegister.class);
        if (data == null) {
            try {
                throw new Exception("No data for " + getClass().getName());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            this.name = data.name();
            this.category = data.category();
            this.bind = data.bind();
        }
    }

    public boolean hasBind() {
        return this.bind != -999;
    }

    public void toggle() {
        setEnabled(!this.enabled, false, false);
    }

    public void toggle(boolean fromBind) {
        setEnabled(!this.enabled, false, fromBind);
    }

    public void setEnabled(boolean newState) {
        setEnabled(newState, false, false);
    }

    public void setEnabled(boolean newState, boolean config) {
        setEnabled(newState, config, false);
    }

    public void setEnabled(boolean newState, boolean config, boolean fromBind) {
        NotifWidget widget;
        if (this.enabled == newState) {
            return;
        }
        this.enabled = newState;
        if (this.enabled) {
            onEnable();
            onEvent();
        } else {
            onDisable();
            removeAllEvents();
        }
        if (config || (this instanceof ClickGUIModule)) {
            return;
        }
        ToggleSoundsModule.playToggle(newState);
        if (fromBind && (widget = (NotifWidget) WidgetManager.getInstance().getWidgets().stream().filter(w -> {
            return w instanceof NotifWidget;
        }).findFirst().orElse(null)) != null && widget.notifTypes.isEnabled("Состояние модулей")) {
            widget.addNotif(this.name + (newState ? " §aвключен" : " §cвыключен"));
        }
    }

    public String getDisplayInfo() {
        return null;
    }

    public void disable() {
        setEnabled(false);
    }

    public void enable() {
        setEnabled(true);
    }

    public void onEnable() {
    }

    public void onDisable() {
    }
}

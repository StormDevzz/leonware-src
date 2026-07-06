/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.module;

import lombok.Generated;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.system.backend.Configurable;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.client.features.modules.other.ToggleSoundsModule;
import sweetie.leonware.client.features.modules.render.ClickGUIModule;
import sweetie.leonware.client.ui.widget.WidgetManager;
import sweetie.leonware.client.ui.widget.overlay.NotifWidget;

public abstract class Module
extends Configurable
implements QuickImports {
    private final String name;
    private final Category category;
    private int bind;
    private boolean enabled;

    public Module() {
        ModuleRegister data = this.getClass().getAnnotation(ModuleRegister.class);
        if (data == null) {
            try {
                throw new Exception("No data for " + this.getClass().getName());
            }
            catch (Exception e) {
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

    public void toggle(boolean fromBind) {
        this.setEnabled(!this.enabled, false, fromBind);
    }

    public void setEnabled(boolean newState) {
        this.setEnabled(newState, false, false);
    }

    public void setEnabled(boolean newState, boolean config) {
        this.setEnabled(newState, config, false);
    }

    public void setEnabled(boolean newState, boolean config, boolean fromBind) {
        NotifWidget widget;
        if (this.enabled == newState) {
            return;
        }
        this.enabled = newState;
        if (this.enabled) {
            this.onEnable();
            this.onEvent();
        } else {
            this.onDisable();
            this.removeAllEvents();
        }
        if (config || this instanceof ClickGUIModule) {
            return;
        }
        ToggleSoundsModule.playToggle(newState);
        if (fromBind && (widget = (NotifWidget)WidgetManager.getInstance().getWidgets().stream().filter(w -> w instanceof NotifWidget).findFirst().orElse(null)) != null && widget.notifTypes.isEnabled("\u0421\u043e\u0441\u0442\u043e\u044f\u043d\u0438\u0435 \u043c\u043e\u0434\u0443\u043b\u0435\u0439")) {
            widget.addNotif(this.name + (newState ? " \u00a7a\u0432\u043a\u043b\u044e\u0447\u0435\u043d" : " \u00a7c\u0432\u044b\u043a\u043b\u044e\u0447\u0435\u043d"));
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
    public void setBind(int bind) {
        this.bind = bind;
    }
}


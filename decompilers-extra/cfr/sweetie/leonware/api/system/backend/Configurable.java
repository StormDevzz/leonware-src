/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.system.backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Generated;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.system.interfaces.QuickImports;

public class Configurable
implements QuickImports {
    private final List<Setting<?>> settings = new ArrayList();
    private final List<EventListener> eventListeners = new ArrayList<EventListener>();

    public void addSettings(Setting<?> ... settings) {
        this.settings.addAll(Arrays.asList(settings));
    }

    public void addSettings(List<Setting<?>> settings) {
        this.settings.addAll(settings);
    }

    public void addEvents(EventListener ... eventListeners) {
        this.eventListeners.addAll(Arrays.asList(eventListeners));
    }

    public void addEvents(List<EventListener> eventListeners) {
        this.eventListeners.addAll(eventListeners);
    }

    public void removeAllEvents() {
        this.eventListeners.forEach(EventListener::unsubscribe);
    }

    public void removeEvents(EventListener ... eventListeners) {
        this.eventListeners.removeAll(Arrays.asList(eventListeners));
    }

    public void removeEvents(List<EventListener> eventListeners) {
        this.eventListeners.removeAll(eventListeners);
    }

    public void onEvent() {
    }

    @Generated
    public List<Setting<?>> getSettings() {
        return this.settings;
    }

    @Generated
    public List<EventListener> getEventListeners() {
        return this.eventListeners;
    }
}


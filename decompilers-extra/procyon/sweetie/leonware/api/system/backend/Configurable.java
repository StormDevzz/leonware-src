// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.system.backend;

import lombok.Generated;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.module.setting.Setting;
import java.util.List;
import sweetie.leonware.api.system.interfaces.QuickImports;

public class Configurable implements QuickImports
{
    private final List<Setting<?>> settings;
    private final List<EventListener> eventListeners;
    
    public Configurable() {
        this.settings = new ArrayList<Setting<?>>();
        this.eventListeners = new ArrayList<EventListener>();
    }
    
    public void addSettings(final Setting<?>... settings) {
        this.settings.addAll(Arrays.asList(settings));
    }
    
    public void addSettings(final List<Setting<?>> settings) {
        this.settings.addAll(settings);
    }
    
    public void addEvents(final EventListener... eventListeners) {
        this.eventListeners.addAll(Arrays.asList(eventListeners));
    }
    
    public void addEvents(final List<EventListener> eventListeners) {
        this.eventListeners.addAll(eventListeners);
    }
    
    public void removeAllEvents() {
        this.eventListeners.forEach(EventListener::unsubscribe);
    }
    
    public void removeEvents(final EventListener... eventListeners) {
        this.eventListeners.removeAll(Arrays.asList(eventListeners));
    }
    
    public void removeEvents(final List<EventListener> eventListeners) {
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

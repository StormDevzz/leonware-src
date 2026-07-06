// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.event.events;

import lombok.Generated;
import sweetie.leonware.api.event.Flora;

public class Event<T> extends Flora<T>
{
    private boolean cancel;
    
    public Event() {
        this.cancel = false;
    }
    
    protected T getSelf() {
        return (T)this;
    }
    
    public boolean call() {
        this.cancel = false;
        this.notify(this.getSelf());
        return this.cancel;
    }
    
    public boolean call(final T any) {
        this.cancel = false;
        this.notify(any);
        return this.cancel;
    }
    
    @Generated
    public boolean isCancel() {
        return this.cancel;
    }
    
    @Generated
    public void setCancel(final boolean cancel) {
        this.cancel = cancel;
    }
}

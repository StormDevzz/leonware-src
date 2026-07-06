// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.event;

record EventListener(Runnable action) {
    public void unsubscribe() {
        this.action.run();
    }
}

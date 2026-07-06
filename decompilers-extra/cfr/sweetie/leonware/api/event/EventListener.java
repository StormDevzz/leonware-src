/*
 * Decompiled with CFR 0.152.
 */
package sweetie.leonware.api.event;

public record EventListener(Runnable action) {
    public void unsubscribe() {
        this.action.run();
    }
}


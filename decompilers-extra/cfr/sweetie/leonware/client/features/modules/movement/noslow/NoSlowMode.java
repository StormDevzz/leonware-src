/*
 * Decompiled with CFR 0.152.
 */
package sweetie.leonware.client.features.modules.movement.noslow;

import sweetie.leonware.api.system.backend.Choice;

public abstract class NoSlowMode
extends Choice {
    public abstract void onUpdate();

    public abstract void onTick();

    public abstract boolean slowingCancel();
}


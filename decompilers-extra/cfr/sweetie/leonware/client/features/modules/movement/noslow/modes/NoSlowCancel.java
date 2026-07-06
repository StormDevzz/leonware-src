/*
 * Decompiled with CFR 0.152.
 */
package sweetie.leonware.client.features.modules.movement.noslow.modes;

import sweetie.leonware.client.features.modules.movement.noslow.NoSlowMode;

public class NoSlowCancel
extends NoSlowMode {
    @Override
    public String getName() {
        return "Cancel";
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onTick() {
    }

    @Override
    public boolean slowingCancel() {
        return true;
    }
}


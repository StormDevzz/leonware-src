/*
 * Decompiled with CFR 0.152.
 */
package sweetie.leonware.api.interfaces;

import java.util.ArrayDeque;
import sweetie.leonware.client.features.modules.combat.BacktrackModule;

public interface IBacktrackable {
    public ArrayDeque<BacktrackModule.Position> leonware$getBackTracks();
}


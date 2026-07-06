// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.interfaces;

import sweetie.leonware.client.features.modules.combat.BacktrackModule;
import java.util.ArrayDeque;

public interface IBacktrackable
{
    ArrayDeque<BacktrackModule.Position> leonware$getBackTracks();
}

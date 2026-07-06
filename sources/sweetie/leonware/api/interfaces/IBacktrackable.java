package sweetie.leonware.api.interfaces;

import java.util.ArrayDeque;
import sweetie.leonware.client.features.modules.combat.BacktrackModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/interfaces/IBacktrackable.class */
public interface IBacktrackable {
    ArrayDeque<BacktrackModule.Position> leonware$getBackTracks();
}

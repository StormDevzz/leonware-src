package sweetie.leonware.client.features.modules.movement.noslow;

import sweetie.leonware.api.system.backend.Choice;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/noslow/NoSlowMode.class */
public abstract class NoSlowMode extends Choice {
    public abstract void onUpdate();

    public abstract void onTick();

    public abstract boolean slowingCancel();
}

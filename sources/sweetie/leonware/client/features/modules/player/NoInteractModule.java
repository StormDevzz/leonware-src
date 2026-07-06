package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/player/NoInteractModule.class */
@ModuleRegister(name = "No Interact Blocks", category = Category.PLAYER)
public class NoInteractModule extends Module {
    private static final NoInteractModule instance = new NoInteractModule();

    @Generated
    public static NoInteractModule getInstance() {
        return instance;
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
    }
}

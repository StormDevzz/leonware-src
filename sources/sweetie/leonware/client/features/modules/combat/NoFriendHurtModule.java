package sweetie.leonware.client.features.modules.combat;

import lombok.Generated;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/NoFriendHurtModule.class */
@ModuleRegister(name = "No Friend Hurt", category = Category.COMBAT)
public class NoFriendHurtModule extends Module {
    private static final NoFriendHurtModule instance = new NoFriendHurtModule();

    @Generated
    public static NoFriendHurtModule getInstance() {
        return instance;
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
    }
}

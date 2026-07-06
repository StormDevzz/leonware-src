package sweetie.leonware.client.features.modules.combat;

import lombok.Generated;
import net.minecraft.class_1297;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.system.configs.FriendManager;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/NoEntityTraceModule.class */
@ModuleRegister(name = "No Entity Trace", category = Category.COMBAT)
public class NoEntityTraceModule extends Module {
    private static final NoEntityTraceModule instance = new NoEntityTraceModule();

    @Generated
    public static NoEntityTraceModule getInstance() {
        return instance;
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
    }

    public boolean shouldCancelResult(class_1297 entity) {
        boolean noFriendHurt = NoFriendHurtModule.getInstance().isEnabled() && entity != null && FriendManager.getInstance().contains(entity.method_5477().getString());
        return noFriendHurt || isEnabled();
    }
}

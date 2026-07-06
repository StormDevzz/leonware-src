// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.combat;

import lombok.Generated;
import sweetie.leonware.api.system.configs.FriendManager;
import net.minecraft.class_1297;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "No Entity Trace", category = Category.COMBAT)
public class NoEntityTraceModule extends Module
{
    private static final NoEntityTraceModule instance;
    
    @Override
    public void onEvent() {
    }
    
    public boolean shouldCancelResult(final class_1297 entity) {
        final boolean noFriendHurt = NoFriendHurtModule.getInstance().isEnabled() && entity != null && FriendManager.getInstance().contains(entity.method_5477().getString());
        return noFriendHurt || this.isEnabled();
    }
    
    @Generated
    public static NoEntityTraceModule getInstance() {
        return NoEntityTraceModule.instance;
    }
    
    static {
        instance = new NoEntityTraceModule();
    }
}

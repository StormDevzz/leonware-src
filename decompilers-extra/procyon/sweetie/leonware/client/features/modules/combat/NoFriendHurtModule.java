// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.combat;

import lombok.Generated;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "No Friend Hurt", category = Category.COMBAT)
public class NoFriendHurtModule extends Module
{
    private static final NoFriendHurtModule instance;
    
    @Override
    public void onEvent() {
    }
    
    @Generated
    public static NoFriendHurtModule getInstance() {
        return NoFriendHurtModule.instance;
    }
    
    static {
        instance = new NoFriendHurtModule();
    }
}

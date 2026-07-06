// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "No Interact Blocks", category = Category.PLAYER)
public class NoInteractModule extends Module
{
    private static final NoInteractModule instance;
    
    @Override
    public void onEvent() {
    }
    
    @Generated
    public static NoInteractModule getInstance() {
        return NoInteractModule.instance;
    }
    
    static {
        instance = new NoInteractModule();
    }
}

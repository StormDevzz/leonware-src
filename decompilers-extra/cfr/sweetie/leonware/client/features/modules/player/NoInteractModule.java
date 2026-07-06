/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;

@ModuleRegister(name="No Interact Blocks", category=Category.PLAYER)
public class NoInteractModule
extends Module {
    private static final NoInteractModule instance = new NoInteractModule();

    @Override
    public void onEvent() {
    }

    @Generated
    public static NoInteractModule getInstance() {
        return instance;
    }
}


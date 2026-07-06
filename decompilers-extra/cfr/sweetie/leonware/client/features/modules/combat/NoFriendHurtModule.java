/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.client.features.modules.combat;

import lombok.Generated;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;

@ModuleRegister(name="No Friend Hurt", category=Category.COMBAT)
public class NoFriendHurtModule
extends Module {
    private static final NoFriendHurtModule instance = new NoFriendHurtModule();

    @Override
    public void onEvent() {
    }

    @Generated
    public static NoFriendHurtModule getInstance() {
        return instance;
    }
}


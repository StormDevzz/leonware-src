/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1297
 */
package sweetie.leonware.client.features.modules.combat;

import lombok.Generated;
import net.minecraft.class_1297;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.system.configs.FriendManager;
import sweetie.leonware.client.features.modules.combat.NoFriendHurtModule;

@ModuleRegister(name="No Entity Trace", category=Category.COMBAT)
public class NoEntityTraceModule
extends Module {
    private static final NoEntityTraceModule instance = new NoEntityTraceModule();

    @Override
    public void onEvent() {
    }

    public boolean shouldCancelResult(class_1297 entity) {
        boolean noFriendHurt = NoFriendHurtModule.getInstance().isEnabled() && entity != null && FriendManager.getInstance().contains(entity.method_5477().getString());
        return noFriendHurt || this.isEnabled();
    }

    @Generated
    public static NoEntityTraceModule getInstance() {
        return instance;
    }
}


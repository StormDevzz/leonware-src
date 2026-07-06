/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 *  net.minecraft.class_1937
 *  net.minecraft.class_243
 *  net.minecraft.class_2684
 *  net.minecraft.class_634
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package sweetie.leonware.inject.entity;

import net.minecraft.class_1297;
import net.minecraft.class_1937;
import net.minecraft.class_243;
import net.minecraft.class_2684;
import net.minecraft.class_634;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sweetie.leonware.api.interfaces.IBacktrackable;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.client.features.modules.combat.BacktrackModule;

@Mixin(value={class_634.class})
public class MixinClientPlayNetworkHandlerBacktrack {
    @Inject(method={"onEntity"}, at={@At(value="TAIL")})
    private void onEntity(class_2684 packet, CallbackInfo ci) {
        BacktrackModule module = BacktrackModule.getInstance();
        if (!module.isEnabled()) {
            return;
        }
        if (QuickImports.mc.field_1687 == null || QuickImports.mc.field_1724 == null) {
            return;
        }
        class_1297 entity = packet.method_11645((class_1937)QuickImports.mc.field_1687);
        if (entity == null || entity == QuickImports.mc.field_1724) {
            return;
        }
        if (!(entity instanceof IBacktrackable)) {
            return;
        }
        IBacktrackable backtrackable = (IBacktrackable)entity;
        double dx = (double)packet.method_36150() / 4096.0;
        double dy = (double)packet.method_36151() / 4096.0;
        double dz = (double)packet.method_36152() / 4096.0;
        class_243 newPos = entity.method_19538().method_1031(dx, dy, dz);
        backtrackable.leonware$getBackTracks().addLast(new BacktrackModule.Position(newPos, System.currentTimeMillis()));
    }
}


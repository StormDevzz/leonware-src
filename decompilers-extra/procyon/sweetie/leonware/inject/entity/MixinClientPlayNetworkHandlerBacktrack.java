// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.entity;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.class_243;
import net.minecraft.class_1297;
import sweetie.leonware.api.interfaces.IBacktrackable;
import net.minecraft.class_1937;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.client.features.modules.combat.BacktrackModule;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.class_2684;
import net.minecraft.class_634;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_634.class })
public class MixinClientPlayNetworkHandlerBacktrack
{
    @Inject(method = { "onEntity" }, at = { @At("TAIL") })
    private void onEntity(final class_2684 packet, final CallbackInfo ci) {
        final BacktrackModule module = BacktrackModule.getInstance();
        if (!module.isEnabled()) {
            return;
        }
        if (QuickImports.mc.field_1687 == null || QuickImports.mc.field_1724 == null) {
            return;
        }
        final class_1297 entity = packet.method_11645((class_1937)QuickImports.mc.field_1687);
        if (entity == null || entity == QuickImports.mc.field_1724) {
            return;
        }
        if (entity instanceof final IBacktrackable backtrackable) {
            final double dx = packet.method_36150() / 4096.0;
            final double dy = packet.method_36151() / 4096.0;
            final double dz = packet.method_36152() / 4096.0;
            final class_243 newPos = entity.method_19538().method_1031(dx, dy, dz);
            backtrackable.leonware$getBackTracks().addLast(new BacktrackModule.Position(newPos, System.currentTimeMillis()));
        }
    }
}

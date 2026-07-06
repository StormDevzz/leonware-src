package sweetie.leonware.inject.entity;

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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/entity/MixinClientPlayNetworkHandlerBacktrack.class */
@Mixin({class_634.class})
public class MixinClientPlayNetworkHandlerBacktrack {
    @Inject(method = {"onEntity"}, at = {@At("TAIL")})
    private void onEntity(class_2684 packet, CallbackInfo ci) {
        IBacktrackable iBacktrackableMethod_11645;
        BacktrackModule module = BacktrackModule.getInstance();
        if (!module.isEnabled() || QuickImports.mc.field_1687 == null || QuickImports.mc.field_1724 == null || (iBacktrackableMethod_11645 = packet.method_11645(QuickImports.mc.field_1687)) == null || iBacktrackableMethod_11645 == QuickImports.mc.field_1724 || !(iBacktrackableMethod_11645 instanceof IBacktrackable)) {
            return;
        }
        IBacktrackable backtrackable = iBacktrackableMethod_11645;
        double dx = ((double) packet.method_36150()) / 4096.0d;
        double dy = ((double) packet.method_36151()) / 4096.0d;
        double dz = ((double) packet.method_36152()) / 4096.0d;
        class_243 newPos = iBacktrackableMethod_11645.method_19538().method_1031(dx, dy, dz);
        backtrackable.leonware$getBackTracks().addLast(new BacktrackModule.Position(newPos, System.currentTimeMillis()));
    }
}

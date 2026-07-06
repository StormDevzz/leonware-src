package sweetie.leonware.inject.entity;

import java.util.ArrayDeque;
import net.minecraft.class_742;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import sweetie.leonware.api.interfaces.IBacktrackable;
import sweetie.leonware.client.features.modules.combat.BacktrackModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/entity/MixinBacktrackablePlayer.class */
@Mixin({class_742.class})
public class MixinBacktrackablePlayer implements IBacktrackable {

    @Unique
    private final ArrayDeque<BacktrackModule.Position> backTracks = new ArrayDeque<>();

    @Override // sweetie.leonware.api.interfaces.IBacktrackable
    public ArrayDeque<BacktrackModule.Position> leonware$getBackTracks() {
        return this.backTracks;
    }
}

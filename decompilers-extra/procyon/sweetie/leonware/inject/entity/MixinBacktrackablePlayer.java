// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.entity;

import org.spongepowered.asm.mixin.Unique;
import sweetie.leonware.client.features.modules.combat.BacktrackModule;
import java.util.ArrayDeque;
import net.minecraft.class_742;
import org.spongepowered.asm.mixin.Mixin;
import sweetie.leonware.api.interfaces.IBacktrackable;

@Mixin({ class_742.class })
public class MixinBacktrackablePlayer implements IBacktrackable
{
    @Unique
    private final ArrayDeque<BacktrackModule.Position> backTracks;
    
    public MixinBacktrackablePlayer() {
        this.backTracks = new ArrayDeque<BacktrackModule.Position>();
    }
    
    @Override
    public ArrayDeque<BacktrackModule.Position> leonware$getBackTracks() {
        return this.backTracks;
    }
}

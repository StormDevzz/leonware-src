/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_742
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 */
package sweetie.leonware.inject.entity;

import java.util.ArrayDeque;
import net.minecraft.class_742;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import sweetie.leonware.api.interfaces.IBacktrackable;
import sweetie.leonware.client.features.modules.combat.BacktrackModule;

@Mixin(value={class_742.class})
public class MixinBacktrackablePlayer
implements IBacktrackable {
    @Unique
    private final ArrayDeque<BacktrackModule.Position> backTracks = new ArrayDeque();

    @Override
    public ArrayDeque<BacktrackModule.Position> leonware$getBackTracks() {
        return this.backTracks;
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1917
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package sweetie.leonware.inject.accessors;

import net.minecraft.class_1917;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_1917.class})
public interface MobSpawnerLogicAccessor {
    @Accessor(value="spawnDelay")
    public int getSpawnDelay();
}


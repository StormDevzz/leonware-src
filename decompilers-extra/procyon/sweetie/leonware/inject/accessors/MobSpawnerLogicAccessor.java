// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.accessors;

import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.class_1917;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_1917.class })
public interface MobSpawnerLogicAccessor
{
    @Accessor("spawnDelay")
    int getSpawnDelay();
}

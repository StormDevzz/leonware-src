package sweetie.leonware.inject.accessors;

import net.minecraft.class_1917;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/accessors/MobSpawnerLogicAccessor.class */
@Mixin({class_1917.class})
public interface MobSpawnerLogicAccessor {
    @Accessor("spawnDelay")
    int getSpawnDelay();
}

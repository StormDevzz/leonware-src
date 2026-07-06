// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.client;

import java.util.List;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.class_270;
import net.minecraft.class_268;
import sweetie.leonware.client.features.modules.render.ExtraTabModule;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.class_2561;
import net.minecraft.class_5250;
import net.minecraft.class_640;
import java.util.Comparator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.class_310;
import net.minecraft.class_355;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_355.class })
public abstract class MixinPlayerListHud
{
    @Shadow
    @Final
    private class_310 field_2155;
    @Shadow
    @Final
    private static Comparator<class_640> field_2156;
    
    @Shadow
    protected abstract class_2561 method_27538(final class_640 p0, final class_5250 p1);
    
    @Inject(method = { "getPlayerName" }, at = { @At("HEAD") }, cancellable = true)
    public void onGetPlayerName(final class_640 entry, final CallbackInfoReturnable<class_2561> cir) {
        final ExtraTabModule mod = ExtraTabModule.getInstance();
        if (mod.isEnabled()) {
            final class_5250 baseName = (entry.method_2971() != null) ? entry.method_2971().method_27661() : class_268.method_1142((class_270)entry.method_2955(), (class_2561)class_2561.method_43470(entry.method_2966().getName()));
            final class_2561 formattedName = this.method_27538(entry, baseName);
            cir.setReturnValue((Object)mod.getModifiedName(formattedName, entry.method_2966().getName()));
        }
    }
    
    @Inject(method = { "collectPlayerEntries" }, at = { @At("HEAD") }, cancellable = true)
    public void onCollectPlayerEntries(final CallbackInfoReturnable<List<class_640>> cir) {
        final ExtraTabModule mod = ExtraTabModule.getInstance();
        if (mod.isEnabled() && this.field_2155.field_1724 != null) {
            final long limitValue = mod.tabSize.getValue().longValue();
            final List<class_640> list = this.field_2155.field_1724.field_3944.method_45732().stream().sorted(MixinPlayerListHud.field_2156).limit(limitValue).toList();
            cir.setReturnValue((Object)list);
        }
    }
}

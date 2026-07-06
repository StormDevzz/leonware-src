package sweetie.leonware.inject.client;

import java.util.Comparator;
import java.util.List;
import net.minecraft.class_2561;
import net.minecraft.class_268;
import net.minecraft.class_310;
import net.minecraft.class_355;
import net.minecraft.class_5250;
import net.minecraft.class_640;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sweetie.leonware.client.features.modules.render.ExtraTabModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/client/MixinPlayerListHud.class */
@Mixin({class_355.class})
public abstract class MixinPlayerListHud {

    @Shadow
    @Final
    private class_310 field_2155;

    @Shadow
    @Final
    private static Comparator<class_640> field_2156;

    @Shadow
    protected abstract class_2561 method_27538(class_640 class_640Var, class_5250 class_5250Var);

    @Inject(method = {"getPlayerName"}, at = {@At("HEAD")}, cancellable = true)
    public void onGetPlayerName(class_640 entry, CallbackInfoReturnable<class_2561> cir) {
        class_5250 class_5250VarMethod_1142;
        ExtraTabModule mod = ExtraTabModule.getInstance();
        if (mod.isEnabled()) {
            if (entry.method_2971() != null) {
                class_5250VarMethod_1142 = entry.method_2971().method_27661();
            } else {
                class_5250VarMethod_1142 = class_268.method_1142(entry.method_2955(), class_2561.method_43470(entry.method_2966().getName()));
            }
            class_5250 baseName = class_5250VarMethod_1142;
            class_2561 formattedName = method_27538(entry, baseName);
            cir.setReturnValue(mod.getModifiedName(formattedName, entry.method_2966().getName()));
        }
    }

    @Inject(method = {"collectPlayerEntries"}, at = {@At("HEAD")}, cancellable = true)
    public void onCollectPlayerEntries(CallbackInfoReturnable<List<class_640>> cir) {
        ExtraTabModule mod = ExtraTabModule.getInstance();
        if (mod.isEnabled() && this.field_2155.field_1724 != null) {
            long limitValue = mod.tabSize.getValue().longValue();
            List<class_640> list = this.field_2155.field_1724.field_3944.method_45732().stream().sorted(field_2156).limit(limitValue).toList();
            cir.setReturnValue(list);
        }
    }
}

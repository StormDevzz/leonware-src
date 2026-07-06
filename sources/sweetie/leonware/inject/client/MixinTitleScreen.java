package sweetie.leonware.inject.client;

import net.minecraft.class_2561;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import net.minecraft.class_442;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sweetie.leonware.LeonWare;
import sweetie.leonware.client.ui.screens.AltManagerScreen;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/client/MixinTitleScreen.class */
@Mixin({class_442.class})
public abstract class MixinTitleScreen extends class_437 {
    protected MixinTitleScreen(class_2561 title) {
        super(title);
    }

    @Inject(method = {"init"}, at = {@At("TAIL")})
    private void onInit(CallbackInfo ci) {
        if (LeonWare.isUnhooked) {
            return;
        }
        int altTextWidth = this.field_22793.method_1727("Alt Manager");
        int altButtonWidth = altTextWidth + (10 * 2);
        method_37063(class_4185.method_46430(class_2561.method_43470("Alt Manager"), btn -> {
            this.field_22787.method_1507(new AltManagerScreen());
        }).method_46434(5, this.field_22790 - 32, altButtonWidth, 20).method_46431());
    }
}

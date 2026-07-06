// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.client;

import sweetie.leonware.client.ui.screens.AltManagerScreen;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.class_364;
import net.minecraft.class_4185;
import sweetie.leonware.LeonWare;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.class_2561;
import net.minecraft.class_442;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.class_437;

@Mixin({ class_442.class })
public abstract class MixinTitleScreen extends class_437
{
    protected MixinTitleScreen(final class_2561 title) {
        super(title);
    }
    
    @Inject(method = { "init" }, at = { @At("TAIL") })
    private void onInit(final CallbackInfo ci) {
        if (LeonWare.isUnhooked) {
            return;
        }
        final int padding = 10;
        final int altTextWidth = this.field_22793.method_1727("Alt Manager");
        final int altButtonWidth = altTextWidth + padding * 2;
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43470("Alt Manager"), btn -> this.field_22787.method_1507((class_437)new AltManagerScreen())).method_46434(5, this.field_22790 - 32, altButtonWidth, 20).method_46431());
    }
}

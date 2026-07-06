/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2561
 *  net.minecraft.class_364
 *  net.minecraft.class_4185
 *  net.minecraft.class_437
 *  net.minecraft.class_442
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package sweetie.leonware.inject.client;

import net.minecraft.class_2561;
import net.minecraft.class_364;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import net.minecraft.class_442;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sweetie.leonware.LeonWare;
import sweetie.leonware.client.ui.screens.AltManagerScreen;

@Mixin(value={class_442.class})
public abstract class MixinTitleScreen
extends class_437 {
    protected MixinTitleScreen(class_2561 title) {
        super(title);
    }

    @Inject(method={"init"}, at={@At(value="TAIL")})
    private void onInit(CallbackInfo ci) {
        if (LeonWare.isUnhooked) {
            return;
        }
        int padding = 10;
        int altTextWidth = this.field_22793.method_1727("Alt Manager");
        int altButtonWidth = altTextWidth + padding * 2;
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43470((String)"Alt Manager"), btn -> this.field_22787.method_1507((class_437)new AltManagerScreen())).method_46434(5, this.field_22790 - 32, altButtonWidth, 20).method_46431());
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2583
 *  net.minecraft.class_2960
 *  net.minecraft.class_5223
 *  net.minecraft.class_5224
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.ModifyArg
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package sweetie.leonware.inject.other;

import net.minecraft.class_2583;
import net.minecraft.class_2960;
import net.minecraft.class_5223;
import net.minecraft.class_5224;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sweetie.leonware.api.system.backend.SharedClass;
import sweetie.leonware.api.utils.other.ReplaceUtil;
import sweetie.leonware.client.features.modules.other.StreamerModule;

@Mixin(value={class_5223.class})
public class MixinTextVisitFactory {
    @Unique
    private static boolean wait = false;

    @ModifyArg(method={"visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z"}, at=@At(value="INVOKE", target="Lnet/minecraft/text/TextVisitFactory;visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z", ordinal=0), index=0)
    private static String visitFormatted(String string) {
        if (!StreamerModule.getInstance().isEnabled() || SharedClass.player() == null) {
            return string;
        }
        return ReplaceUtil.protectedString(string);
    }

    private static String removeColorCodes(String s) {
        StringBuilder out = new StringBuilder(s.length());
        boolean skip = false;
        for (int i = 0; i < s.length(); ++i) {
            char c = s.charAt(i);
            if (skip) {
                skip = false;
                continue;
            }
            if (c == '\u00a7') {
                skip = true;
                continue;
            }
            out.append(c);
        }
        return out.toString();
    }

    @Inject(method={"visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z"}, at={@At(value="HEAD")}, cancellable=true)
    private static void fakeInfo(String text, int startIndex, class_2583 style, class_5224 visitor, CallbackInfoReturnable<Boolean> cir) {
        StreamerModule streamer = StreamerModule.getInstance();
        if (streamer != null && streamer.isEnabled() && !((String)streamer.getFakeDonate().getValue()).equals("Default")) {
            if (text.contains("\u0421\u0442\u0430\u0442\u0443\u0441:") && style.method_27708() != null && style.method_27708().equals((Object)class_2960.method_60654((String)"minecraft:default"))) {
                wait = true;
                class_5223.method_27473((String)text, (int)startIndex, (class_2583)style, (class_2583)style, (class_5224)visitor);
                cir.setReturnValue((Object)true);
                return;
            }
            if (wait && style.method_27708() != null && style.method_27708().equals((Object)class_2960.method_60654((String)"custom:groups/default"))) {
                class_2960 newFont = class_2960.method_60654((String)("custom:groups/" + streamer.getResourceKey()));
                class_2583 newStyle = style.method_27704(newFont);
                class_5223.method_27473((String)text, (int)startIndex, (class_2583)newStyle, (class_2583)newStyle, (class_5224)visitor);
                wait = false;
                cir.setReturnValue((Object)true);
                return;
            }
            if (!text.equals("\u0421\u0442\u0430\u0442\u0443\u0441: ") && wait) {
                wait = false;
            }
        }
    }
}


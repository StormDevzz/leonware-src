// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.other;

import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.class_2960;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.class_5224;
import net.minecraft.class_2583;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import sweetie.leonware.api.utils.other.ReplaceUtil;
import sweetie.leonware.api.system.backend.SharedClass;
import sweetie.leonware.client.features.modules.other.StreamerModule;
import org.spongepowered.asm.mixin.Unique;
import net.minecraft.class_5223;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_5223.class })
public class MixinTextVisitFactory
{
    @Unique
    private static boolean wait;
    
    @ModifyArg(method = { "visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/text/TextVisitFactory;visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z", ordinal = 0), index = 0)
    private static String visitFormatted(final String string) {
        if (!StreamerModule.getInstance().isEnabled() || SharedClass.player() == null) {
            return string;
        }
        return ReplaceUtil.protectedString(string);
    }
    
    private static String removeColorCodes(final String s) {
        final StringBuilder out = new StringBuilder(s.length());
        boolean skip = false;
        for (int i = 0; i < s.length(); ++i) {
            final char c = s.charAt(i);
            if (skip) {
                skip = false;
            }
            else if (c == '§') {
                skip = true;
            }
            else {
                out.append(c);
            }
        }
        return out.toString();
    }
    
    @Inject(method = { "visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z" }, at = { @At("HEAD") }, cancellable = true)
    private static void fakeInfo(final String text, final int startIndex, final class_2583 style, final class_5224 visitor, final CallbackInfoReturnable<Boolean> cir) {
        final StreamerModule streamer = StreamerModule.getInstance();
        if (streamer != null && streamer.isEnabled() && !streamer.getFakeDonate().getValue().equals("Default")) {
            if (text.contains("\u0421\u0442\u0430\u0442\u0443\u0441:") && style.method_27708() != null && style.method_27708().equals((Object)class_2960.method_60654("minecraft:default"))) {
                MixinTextVisitFactory.wait = true;
                class_5223.method_27473(text, startIndex, style, style, visitor);
                cir.setReturnValue((Object)true);
                return;
            }
            if (MixinTextVisitFactory.wait && style.method_27708() != null && style.method_27708().equals((Object)class_2960.method_60654("custom:groups/default"))) {
                final class_2960 newFont = class_2960.method_60654("custom:groups/" + streamer.getResourceKey());
                final class_2583 newStyle = style.method_27704(newFont);
                class_5223.method_27473(text, startIndex, newStyle, newStyle, visitor);
                MixinTextVisitFactory.wait = false;
                cir.setReturnValue((Object)true);
                return;
            }
            if (!text.equals("\u0421\u0442\u0430\u0442\u0443\u0441: ") && MixinTextVisitFactory.wait) {
                MixinTextVisitFactory.wait = false;
            }
        }
    }
    
    static {
        MixinTextVisitFactory.wait = false;
    }
}

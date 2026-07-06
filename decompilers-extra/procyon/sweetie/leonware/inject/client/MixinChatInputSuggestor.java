// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.client;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import sweetie.leonware.api.command.CommandManager;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.brigadier.StringReader;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import net.minecraft.class_2172;
import com.mojang.brigadier.ParseResults;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Final;
import net.minecraft.class_342;
import net.minecraft.class_4717;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_4717.class })
public abstract class MixinChatInputSuggestor
{
    @Final
    @Shadow
    class_342 field_21599;
    @Shadow
    boolean field_21614;
    @Shadow
    private ParseResults<class_2172> field_21610;
    @Shadow
    private CompletableFuture<Suggestions> field_21611;
    @Shadow
    private class_4717.class_464 field_21612;
    
    @Shadow
    protected abstract void method_23937();
    
    @Inject(method = { "refresh" }, at = { @At(value = "INVOKE", target = "Lcom/mojang/brigadier/StringReader;canRead()Z", remap = false) }, cancellable = true)
    public void onRefresh(final CallbackInfo callbackInfo, @Local final StringReader reader) {
        if (reader.canRead(CommandManager.getInstance().getPrefix().length()) && reader.getString().startsWith(CommandManager.getInstance().getPrefix(), reader.getCursor())) {
            reader.setCursor(reader.getCursor() + 1);
            if (this.field_21610 == null) {
                this.field_21610 = (ParseResults<class_2172>)CommandManager.getInstance().getDispatcher().parse(reader, (Object)CommandManager.getInstance().getSource());
            }
            final int cursor = this.field_21599.method_1881();
            if (cursor >= 1 && (this.field_21612 == null || !this.field_21614)) {
                (this.field_21611 = CommandManager.getInstance().getDispatcher().getCompletionSuggestions((ParseResults)this.field_21610, cursor)).thenRun(() -> {
                    if (this.field_21611.isDone()) {
                        this.method_23937();
                    }
                    return;
                });
            }
            callbackInfo.cancel();
        }
    }
}

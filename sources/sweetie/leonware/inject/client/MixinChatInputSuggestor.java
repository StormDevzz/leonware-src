package sweetie.leonware.inject.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import net.minecraft.class_2172;
import net.minecraft.class_342;
import net.minecraft.class_4717;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sweetie.leonware.api.command.CommandManager;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/client/MixinChatInputSuggestor.class */
@Mixin({class_4717.class})
public abstract class MixinChatInputSuggestor {

    @Shadow
    @Final
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

    @Inject(method = {"refresh"}, at = {@At(value = "INVOKE", target = "Lcom/mojang/brigadier/StringReader;canRead()Z", remap = false)}, cancellable = true)
    public void onRefresh(CallbackInfo callbackInfo, @Local StringReader reader) {
        if (reader.canRead(CommandManager.getInstance().getPrefix().length()) && reader.getString().startsWith(CommandManager.getInstance().getPrefix(), reader.getCursor())) {
            reader.setCursor(reader.getCursor() + 1);
            if (this.field_21610 == null) {
                this.field_21610 = CommandManager.getInstance().getDispatcher().parse(reader, CommandManager.getInstance().getSource());
            }
            int cursor = this.field_21599.method_1881();
            if (cursor >= 1 && (this.field_21612 == null || !this.field_21614)) {
                this.field_21611 = CommandManager.getInstance().getDispatcher().getCompletionSuggestions(this.field_21610, cursor);
                this.field_21611.thenRun(() -> {
                    if (this.field_21611.isDone()) {
                        method_23937();
                    }
                });
            }
            callbackInfo.cancel();
        }
    }
}

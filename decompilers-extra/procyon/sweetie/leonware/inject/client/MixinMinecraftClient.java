// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.client;

import sweetie.leonware.api.event.events.client.DisconnectEvent;
import net.minecraft.class_419;
import net.minecraft.class_500;
import net.minecraft.class_442;
import sweetie.leonware.api.system.backend.ClientInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.function.Consumer;
import org.lwjgl.system.MemoryUtil;
import net.minecraft.class_1011;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.system.MemoryStack;
import org.spongepowered.asm.mixin.injection.Redirect;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.class_8518;
import net.minecraft.class_3262;
import net.minecraft.class_542;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.client.features.modules.player.MultiTaskModule;
import sweetie.leonware.api.system.backend.SharedClass;
import sweetie.leonware.api.event.events.client.GameLoopEvent;
import sweetie.leonware.api.utils.framelimiter.IFrameCall;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.lwjgl.glfw.GLFW;
import sweetie.leonware.client.ui.screens.CloseConfirmScreen;
import sweetie.leonware.LeonWare;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.class_437;
import org.spongepowered.asm.mixin.Unique;
import sweetie.leonware.api.utils.framelimiter.FrameLimiter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.class_1041;
import net.minecraft.class_310;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_310.class })
public abstract class MixinMinecraftClient
{
    @Shadow
    @Final
    private class_1041 field_1704;
    @Shadow
    private int field_1752;
    @Unique
    private final FrameLimiter frameLimiter;
    @Shadow
    public class_437 field_1755;
    
    public MixinMinecraftClient() {
        this.frameLimiter = new FrameLimiter(false);
    }
    
    @Shadow
    public abstract void method_1507(final class_437 p0);
    
    @Inject(method = { "scheduleStop" }, at = { @At("HEAD") }, cancellable = true)
    private void onScheduleStop(final CallbackInfo ci) {
        if (LeonWare.isUnhooked) {
            return;
        }
        final class_310 self = (class_310)this;
        if (self.field_1755 instanceof CloseConfirmScreen) {
            return;
        }
        ci.cancel();
        GLFW.glfwSetWindowShouldClose(this.field_1704.method_4490(), false);
        self.execute(() -> self.method_1507((class_437)new CloseConfirmScreen(self.field_1755)));
    }
    
    @Inject(method = { "render" }, at = { @At("HEAD") })
    public void gameLoopHook(final boolean tick, final CallbackInfo ci) {
        this.frameLimiter.execute(60, () -> GameLoopEvent.getInstance().call());
    }
    
    @Inject(method = { "tick" }, at = { @At("HEAD") })
    public void preTickHook(final CallbackInfo ci) {
        if (SharedClass.player() == null) {
            return;
        }
        if (MultiTaskModule.getInstance().isEnabled()) {
            this.field_1752 = 0;
        }
        TickEvent.getInstance().call();
    }
    
    @Inject(method = { "close" }, at = { @At("HEAD") })
    public void closeHook(final CallbackInfo ci) {
        LeonWare.getInstance().onClose();
    }
    
    @Inject(method = { "<init>" }, at = { @At("RETURN") })
    public void initHook(final class_542 args, final CallbackInfo ci) {
        LeonWare.getInstance().postLoad();
    }
    
    @Redirect(method = { "<init>" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Window;setIcon(Lnet/minecraft/resource/ResourcePack;Lnet/minecraft/client/util/Icons;)V"))
    private void onChangeIcon(final class_1041 instance, final class_3262 resourcePack, final class_8518 icons) throws IOException {
        InputStream iconStream = LeonWare.class.getResourceAsStream("/assets/leonware/textures/leon/leon.png");
        if (iconStream == null) {
            iconStream = LeonWare.class.getClassLoader().getResourceAsStream("assets/leonware/textures/leon/leon.png");
        }
        if (iconStream != null) {
            this.setWindowIcon(LeonWare.class.getResourceAsStream("/assets/leonware/textures/leon/leon.png"), LeonWare.class.getResourceAsStream("/assets/leonware/textures/leon/leon.png"));
        }
        else {
            System.out.println("\u043a\u0430\u0440\u0442\u0438\u043d\u043a\u0430 \u043d\u0435 \u0432\u0440\u0443\u0431\u0438\u043b\u0430\u0441\u044c");
            instance.method_4491(resourcePack, icons);
        }
    }
    
    @Unique
    public void setWindowIcon(final InputStream img1, final InputStream img2) {
        if (img1 == null || img2 == null) {
            System.out.println("\u043a\u0430\u0440\u0442\u0438\u043d\u043a\u0430 = null");
            return;
        }
        try (final MemoryStack stack = MemoryStack.stackPush()) {
            final GLFWImage.Buffer buffer = GLFWImage.malloc(2, stack);
            final List<InputStream> imgList = List.of(img1, img2);
            final List<ByteBuffer> byteBuffers = new ArrayList<ByteBuffer>();
            for (int i = 0; i < imgList.size(); ++i) {
                final class_1011 nativeImage = class_1011.method_4309((InputStream)imgList.get(i));
                final int width = nativeImage.method_4307();
                final int height = nativeImage.method_4323();
                final ByteBuffer bytebuffer = MemoryUtil.memAlloc(width * height * 4);
                final int[] pixels = nativeImage.method_61942();
                bytebuffer.asIntBuffer().put(pixels);
                buffer.position(i);
                buffer.width(width);
                buffer.height(height);
                buffer.pixels(bytebuffer);
                byteBuffers.add(bytebuffer);
                nativeImage.close();
            }
            buffer.position(0);
            if (GLFW.glfwGetPlatform() != 393219) {
                GLFW.glfwSetWindowIcon(this.field_1704.method_4490(), buffer);
            }
            byteBuffers.forEach(MemoryUtil::memFree);
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
    }
    
    @Inject(method = { "getWindowTitle" }, at = { @At("HEAD") }, cancellable = true)
    private void onGetWindowTitle(final CallbackInfoReturnable<String> cir) {
        if (LeonWare.isUnhooked) {
            return;
        }
        cir.setReturnValue("LeonWare " + ClientInfo.VERSION);
    }
    
    @Inject(method = { "setScreen" }, at = { @At("HEAD") })
    private void onSetScreen(final class_437 screen, final CallbackInfo ci) {
        if (screen instanceof class_442 || screen instanceof class_500 || screen instanceof class_419) {
            DisconnectEvent.getInstance().call(new DisconnectEvent.DisconnectData());
        }
    }
}

package sweetie.leonware.inject.client;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1011;
import net.minecraft.class_1041;
import net.minecraft.class_310;
import net.minecraft.class_3262;
import net.minecraft.class_419;
import net.minecraft.class_437;
import net.minecraft.class_442;
import net.minecraft.class_500;
import net.minecraft.class_542;
import net.minecraft.class_8518;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sweetie.leonware.LeonWare;
import sweetie.leonware.api.event.events.client.DisconnectEvent;
import sweetie.leonware.api.event.events.client.GameLoopEvent;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.system.backend.ClientInfo;
import sweetie.leonware.api.system.backend.SharedClass;
import sweetie.leonware.api.utils.framelimiter.FrameLimiter;
import sweetie.leonware.client.features.modules.player.MultiTaskModule;
import sweetie.leonware.client.ui.screens.CloseConfirmScreen;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/client/MixinMinecraftClient.class */
@Mixin({class_310.class})
public abstract class MixinMinecraftClient {

    @Shadow
    @Final
    private class_1041 field_1704;

    @Shadow
    private int field_1752;

    @Unique
    private final FrameLimiter frameLimiter = new FrameLimiter(false);

    @Shadow
    public class_437 field_1755;

    @Shadow
    public abstract void method_1507(class_437 class_437Var);

    @Inject(method = {"scheduleStop"}, at = {@At("HEAD")}, cancellable = true)
    private void onScheduleStop(CallbackInfo ci) {
        if (LeonWare.isUnhooked) {
            return;
        }
        class_310 self = (class_310) this;
        if (self.field_1755 instanceof CloseConfirmScreen) {
            return;
        }
        ci.cancel();
        GLFW.glfwSetWindowShouldClose(this.field_1704.method_4490(), false);
        self.execute(() -> {
            self.method_1507(new CloseConfirmScreen(self.field_1755));
        });
    }

    @Inject(method = {"render"}, at = {@At("HEAD")})
    public void gameLoopHook(boolean tick, CallbackInfo ci) {
        this.frameLimiter.execute(60, () -> {
            GameLoopEvent.getInstance().call();
        });
    }

    @Inject(method = {"tick"}, at = {@At("HEAD")})
    public void preTickHook(CallbackInfo ci) {
        if (SharedClass.player() == null) {
            return;
        }
        if (MultiTaskModule.getInstance().isEnabled()) {
            this.field_1752 = 0;
        }
        TickEvent.getInstance().call();
    }

    @Inject(method = {"close"}, at = {@At("HEAD")})
    public void closeHook(CallbackInfo ci) {
        LeonWare.getInstance().onClose();
    }

    @Inject(method = {"<init>"}, at = {@At("RETURN")})
    public void initHook(class_542 args, CallbackInfo ci) {
        LeonWare.getInstance().postLoad();
    }

    @Redirect(method = {"<init>"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Window;setIcon(Lnet/minecraft/resource/ResourcePack;Lnet/minecraft/client/util/Icons;)V"))
    private void onChangeIcon(class_1041 instance, class_3262 resourcePack, class_8518 icons) throws IOException {
        InputStream iconStream = LeonWare.class.getResourceAsStream("/assets/leonware/textures/leon/leon.png");
        if (iconStream == null) {
            iconStream = LeonWare.class.getClassLoader().getResourceAsStream("assets/leonware/textures/leon/leon.png");
        }
        if (iconStream != null) {
            setWindowIcon(LeonWare.class.getResourceAsStream("/assets/leonware/textures/leon/leon.png"), LeonWare.class.getResourceAsStream("/assets/leonware/textures/leon/leon.png"));
        } else {
            System.out.println("картинка не врубилась");
            instance.method_4491(resourcePack, icons);
        }
    }

    @Unique
    public void setWindowIcon(InputStream img1, InputStream img2) {
        if (img1 == null || img2 == null) {
            System.out.println("картинка = null");
            return;
        }
        try {
            MemoryStack stack = MemoryStack.stackPush();
            try {
                GLFWImage.Buffer buffer = GLFWImage.malloc(2, stack);
                List<InputStream> imgList = List.of(img1, img2);
                List<ByteBuffer> byteBuffers = new ArrayList<>();
                for (int i = 0; i < imgList.size(); i++) {
                    class_1011 nativeImage = class_1011.method_4309(imgList.get(i));
                    int width = nativeImage.method_4307();
                    int height = nativeImage.method_4323();
                    ByteBuffer bytebuffer = MemoryUtil.memAlloc(width * height * 4);
                    int[] pixels = nativeImage.method_61942();
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
                byteBuffers.forEach((v0) -> {
                    MemoryUtil.memFree(v0);
                });
                if (stack != null) {
                    stack.close();
                }
            } finally {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Inject(method = {"getWindowTitle"}, at = {@At("HEAD")}, cancellable = true)
    private void onGetWindowTitle(CallbackInfoReturnable<String> cir) {
        if (LeonWare.isUnhooked) {
            return;
        }
        cir.setReturnValue("LeonWare " + ClientInfo.VERSION);
    }

    @Inject(method = {"setScreen"}, at = {@At("HEAD")})
    private void onSetScreen(class_437 screen, CallbackInfo ci) {
        if ((screen instanceof class_442) || (screen instanceof class_500) || (screen instanceof class_419)) {
            DisconnectEvent.getInstance().call(new DisconnectEvent.DisconnectData());
        }
    }
}

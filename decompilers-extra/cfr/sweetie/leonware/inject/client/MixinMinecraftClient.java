/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1011
 *  net.minecraft.class_1041
 *  net.minecraft.class_310
 *  net.minecraft.class_3262
 *  net.minecraft.class_419
 *  net.minecraft.class_437
 *  net.minecraft.class_442
 *  net.minecraft.class_500
 *  net.minecraft.class_542
 *  net.minecraft.class_8518
 *  org.lwjgl.glfw.GLFW
 *  org.lwjgl.glfw.GLFWImage
 *  org.lwjgl.glfw.GLFWImage$Buffer
 *  org.lwjgl.system.MemoryStack
 *  org.lwjgl.system.MemoryUtil
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.Redirect
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
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

@Mixin(value={class_310.class})
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
    public abstract void method_1507(class_437 var1);

    @Inject(method={"scheduleStop"}, at={@At(value="HEAD")}, cancellable=true)
    private void onScheduleStop(CallbackInfo ci) {
        if (LeonWare.isUnhooked) {
            return;
        }
        class_310 self = (class_310)this;
        if (self.field_1755 instanceof CloseConfirmScreen) {
            return;
        }
        ci.cancel();
        GLFW.glfwSetWindowShouldClose((long)this.field_1704.method_4490(), (boolean)false);
        self.execute(() -> self.method_1507((class_437)new CloseConfirmScreen(self.field_1755)));
    }

    @Inject(method={"render"}, at={@At(value="HEAD")})
    public void gameLoopHook(boolean tick, CallbackInfo ci) {
        this.frameLimiter.execute(60, () -> GameLoopEvent.getInstance().call());
    }

    @Inject(method={"tick"}, at={@At(value="HEAD")})
    public void preTickHook(CallbackInfo ci) {
        if (SharedClass.player() == null) {
            return;
        }
        if (MultiTaskModule.getInstance().isEnabled()) {
            this.field_1752 = 0;
        }
        TickEvent.getInstance().call();
    }

    @Inject(method={"close"}, at={@At(value="HEAD")})
    public void closeHook(CallbackInfo ci) {
        LeonWare.getInstance().onClose();
    }

    @Inject(method={"<init>"}, at={@At(value="RETURN")})
    public void initHook(class_542 args, CallbackInfo ci) {
        LeonWare.getInstance().postLoad();
    }

    @Redirect(method={"<init>"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/util/Window;setIcon(Lnet/minecraft/resource/ResourcePack;Lnet/minecraft/client/util/Icons;)V"))
    private void onChangeIcon(class_1041 instance, class_3262 resourcePack, class_8518 icons) throws IOException {
        InputStream iconStream = LeonWare.class.getResourceAsStream("/assets/leonware/textures/leon/leon.png");
        if (iconStream == null) {
            iconStream = LeonWare.class.getClassLoader().getResourceAsStream("assets/leonware/textures/leon/leon.png");
        }
        if (iconStream != null) {
            this.setWindowIcon(LeonWare.class.getResourceAsStream("/assets/leonware/textures/leon/leon.png"), LeonWare.class.getResourceAsStream("/assets/leonware/textures/leon/leon.png"));
        } else {
            System.out.println("\u043a\u0430\u0440\u0442\u0438\u043d\u043a\u0430 \u043d\u0435 \u0432\u0440\u0443\u0431\u0438\u043b\u0430\u0441\u044c");
            instance.method_4491(resourcePack, icons);
        }
    }

    @Unique
    public void setWindowIcon(InputStream img1, InputStream img2) {
        if (img1 == null || img2 == null) {
            System.out.println("\u043a\u0430\u0440\u0442\u0438\u043d\u043a\u0430 = null");
            return;
        }
        try (MemoryStack stack = MemoryStack.stackPush();){
            GLFWImage.Buffer buffer = GLFWImage.malloc((int)2, (MemoryStack)stack);
            List<InputStream> imgList = List.of(img1, img2);
            ArrayList<ByteBuffer> byteBuffers = new ArrayList<ByteBuffer>();
            for (int i = 0; i < imgList.size(); ++i) {
                class_1011 nativeImage = class_1011.method_4309((InputStream)imgList.get(i));
                int width = nativeImage.method_4307();
                int height = nativeImage.method_4323();
                ByteBuffer bytebuffer = MemoryUtil.memAlloc((int)(width * height * 4));
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
                GLFW.glfwSetWindowIcon((long)this.field_1704.method_4490(), (GLFWImage.Buffer)buffer);
            }
            byteBuffers.forEach(MemoryUtil::memFree);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Inject(method={"getWindowTitle"}, at={@At(value="HEAD")}, cancellable=true)
    private void onGetWindowTitle(CallbackInfoReturnable<String> cir) {
        if (LeonWare.isUnhooked) {
            return;
        }
        cir.setReturnValue((Object)("LeonWare " + ClientInfo.VERSION));
    }

    @Inject(method={"setScreen"}, at={@At(value="HEAD")})
    private void onSetScreen(class_437 screen, CallbackInfo ci) {
        if (screen instanceof class_442 || screen instanceof class_500 || screen instanceof class_419) {
            DisconnectEvent.getInstance().call(new DisconnectEvent.DisconnectData());
        }
    }
}


// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.render;

import sweetie.leonware.api.module.setting.Setting;
import java.awt.Color;
import sweetie.leonware.api.utils.render.RenderUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.class_310;
import java.util.Objects;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.class_332;
import net.minecraft.class_490;
import sweetie.leonware.client.features.modules.render.InventoryAnimationModule;
import net.minecraft.class_2561;
import org.spongepowered.asm.mixin.Unique;
import net.minecraft.class_465;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.class_437;
import net.minecraft.class_1703;

@Mixin({ class_465.class })
public abstract class MixinInventoryAnimation<T extends class_1703> extends class_437
{
    @Unique
    private float leonware$progress;
    @Unique
    private boolean leonware$closing;
    @Unique
    private boolean leonware$bypassClose;
    @Unique
    private boolean leonware$scheduledClose;
    @Unique
    private boolean leonware$pushed;
    @Unique
    private boolean leonware$cursorHidden;
    @Unique
    private long leonware$lastFrameTime;
    
    protected MixinInventoryAnimation(final class_2561 title) {
        super(title);
        this.leonware$progress = 0.0f;
        this.leonware$closing = false;
        this.leonware$bypassClose = false;
        this.leonware$scheduledClose = false;
        this.leonware$pushed = false;
        this.leonware$cursorHidden = false;
        this.leonware$lastFrameTime = 0L;
    }
    
    @Unique
    private boolean leonware$isActive() {
        final InventoryAnimationModule module = InventoryAnimationModule.getInstance();
        return module != null && module.isEnabled() && (!Boolean.TRUE.equals(((Setting<Object>)module.inventoryOnly).getValue()) || this instanceof class_490);
    }
    
    @Unique
    private float leonware$deltaTime() {
        final long now = System.nanoTime();
        if (this.leonware$lastFrameTime == 0L) {
            this.leonware$lastFrameTime = now;
            return 0.016f;
        }
        float dt = (now - this.leonware$lastFrameTime) / 1.0E9f;
        this.leonware$lastFrameTime = now;
        if (dt < 0.0f) {
            dt = 0.0f;
        }
        if (dt > 0.1f) {
            dt = 0.1f;
        }
        return dt;
    }
    
    @Unique
    private float leonware$fast(final float end, final float start, final float multiple) {
        float clampedDelta = this.leonware$deltaTime() * multiple;
        if (clampedDelta < 0.0f) {
            clampedDelta = 0.0f;
        }
        if (clampedDelta > 1.0f) {
            clampedDelta = 1.0f;
        }
        return (1.0f - clampedDelta) * end + clampedDelta * start;
    }
    
    @Unique
    private float leonware$easeOutCubic(float t) {
        t = Math.min(1.0f, Math.max(0.0f, t));
        return 1.0f - (float)Math.pow(1.0f - t, 3.0);
    }
    
    @Unique
    private float leonware$easeOutBack(float t) {
        t = Math.min(1.0f, Math.max(0.0f, t));
        final float c1 = 1.70158f;
        final float c2 = c1 + 1.0f;
        return 1.0f + c2 * (float)Math.pow(t - 1.0f, 3.0) + c1 * (float)Math.pow(t - 1.0f, 2.0);
    }
    
    @Unique
    private float leonware$applyEasing(final float raw) {
        if (this.leonware$closing) {
            return this.leonware$easeOutCubic(raw);
        }
        final String s = InventoryAnimationModule.getInstance().animationType.getValue();
        return switch (s) {
            case "Bounce" -> Math.max(0.001f, this.leonware$easeOutBack(raw));
            default -> this.leonware$easeOutCubic(raw);
        };
    }
    
    @Unique
    private void leonware$applyTransform(final class_332 ctx, final float v) {
        final float cx = this.field_22789 / 2.0f;
        final float cy = this.field_22790 / 2.0f;
        final String s = InventoryAnimationModule.getInstance().animationType.getValue();
        switch (s) {
            case "Scale":
            case "Bounce": {
                ctx.method_51448().method_22904((double)cx, (double)cy, 0.0);
                ctx.method_51448().method_22905(Math.max(0.001f, v), Math.max(0.001f, v), 1.0f);
                ctx.method_51448().method_22904((double)(-cx), (double)(-cy), 0.0);
                break;
            }
            case "SlideUp": {
                ctx.method_51448().method_22904(0.0, this.field_22790 * (1.0 - v), 0.0);
                break;
            }
            case "SlideDown": {
                ctx.method_51448().method_22904(0.0, -this.field_22790 * (1.0 - v), 0.0);
                break;
            }
            case "SlideLeft": {
                ctx.method_51448().method_22904(this.field_22789 * (1.0 - v), 0.0, 0.0);
                break;
            }
            case "SlideRight": {
                ctx.method_51448().method_22904(-this.field_22789 * (1.0 - v), 0.0, 0.0);
                break;
            }
            case "Flip": {
                ctx.method_51448().method_22904((double)cx, (double)cy, 0.0);
                ctx.method_51448().method_22905(Math.max(0.001f, v), 1.0f, 1.0f);
                ctx.method_51448().method_22904((double)(-cx), (double)(-cy), 0.0);
                break;
            }
            case "Warp": {
                final float scale = this.leonware$closing ? v : (1.15f - 0.15f * v);
                ctx.method_51448().method_22904((double)cx, (double)cy, 0.0);
                ctx.method_51448().method_22905(Math.max(0.001f, scale), Math.max(0.001f, scale), 1.0f);
                ctx.method_51448().method_22904((double)(-cx), (double)(-cy), 0.0);
                break;
            }
            case "Glitch": {
                final float shake = (1.0f - v) * 35.0f * (float)Math.sin(v * 28.0f);
                final float scale2 = 0.88f + 0.12f * v;
                ctx.method_51448().method_22904((double)shake, 0.0, 0.0);
                ctx.method_51448().method_22904((double)cx, (double)cy, 0.0);
                ctx.method_51448().method_22905(scale2, scale2, 1.0f);
                ctx.method_51448().method_22904((double)(-cx), (double)(-cy), 0.0);
                break;
            }
        }
    }
    
    @Inject(method = { "render" }, at = { @At("HEAD") }, cancellable = true)
    private void leonware$onRenderPre(final class_332 ctx, final int mx, final int my, final float delta, final CallbackInfo ci) {
        this.leonware$pushed = false;
        if (this.leonware$scheduledClose) {
            ci.cancel();
            return;
        }
        if (!this.leonware$isActive()) {
            return;
        }
        final float speed = InventoryAnimationModule.getInstance().speed.getValue();
        if (this.leonware$closing) {
            this.leonware$progress = this.leonware$fast(this.leonware$progress, 0.0f, speed * 3.0f);
            if (this.leonware$progress < 0.05f && !this.leonware$cursorHidden) {
                this.leonware$cursorHidden = true;
                GLFW.glfwSetInputMode(this.field_22787.method_22683().method_4490(), 208897, 212994);
            }
            if (this.leonware$progress < 0.005f) {
                this.leonware$scheduledClose = true;
                this.leonware$bypassClose = true;
                GLFW.glfwSetInputMode(this.field_22787.method_22683().method_4490(), 208897, 212993);
                this.leonware$cursorHidden = false;
                final class_310 field_22787 = this.field_22787;
                final class_437 obj = this;
                Objects.requireNonNull(obj);
                field_22787.execute(obj::method_25419);
                ci.cancel();
                return;
            }
        }
        else {
            this.leonware$progress = Math.min(1.0f, this.leonware$fast(this.leonware$progress, 1.0f, speed));
            if (this.leonware$progress >= 0.999f) {
                return;
            }
        }
        if (InventoryAnimationModule.getInstance().animationType.is("Fade")) {
            return;
        }
        ctx.method_51448().method_22903();
        this.leonware$pushed = true;
        this.leonware$applyTransform(ctx, this.leonware$applyEasing(this.leonware$progress));
    }
    
    @Inject(method = { "render" }, at = { @At("TAIL") })
    private void leonware$onRenderPost(final class_332 ctx, final int mx, final int my, final float delta, final CallbackInfo ci) {
        if (this.leonware$pushed) {
            ctx.method_51448().method_22909();
            this.leonware$pushed = false;
        }
        if (!this.leonware$isActive() || this.leonware$scheduledClose) {
            return;
        }
        if (!InventoryAnimationModule.getInstance().animationType.is("Fade")) {
            return;
        }
        if (this.leonware$progress >= 0.999f && !this.leonware$closing) {
            return;
        }
        final float alpha = 1.0f - this.leonware$applyEasing(this.leonware$progress);
        if (alpha > 0.005f) {
            RenderSystem.disableDepthTest();
            RenderUtil.RECT.draw(ctx.method_51448(), 0.0f, 0.0f, (float)this.field_22789, (float)this.field_22790, 0.0f, new Color(0, 0, 0, Math.min(255, (int)(alpha * 255.0f))));
            RenderSystem.enableDepthTest();
        }
    }
    
    @Inject(method = { "close" }, at = { @At("HEAD") }, cancellable = true)
    private void leonware$onClose(final CallbackInfo ci) {
        if (this.leonware$bypassClose) {
            return;
        }
        if (!this.leonware$isActive()) {
            return;
        }
        if (this.leonware$closing || this.leonware$scheduledClose) {
            ci.cancel();
            return;
        }
        if (this.leonware$progress > 0.05f) {
            this.leonware$closing = true;
            ci.cancel();
        }
    }
}

package sweetie.leonware.inject.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.Objects;
import net.minecraft.class_1703;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_437;
import net.minecraft.class_465;
import net.minecraft.class_490;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.client.features.modules.render.InventoryAnimationModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/render/MixinInventoryAnimation.class */
@Mixin({class_465.class})
public abstract class MixinInventoryAnimation<T extends class_1703> extends class_437 {

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

    protected MixinInventoryAnimation(class_2561 title) {
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
        InventoryAnimationModule module = InventoryAnimationModule.getInstance();
        if (module == null || !module.isEnabled()) {
            return false;
        }
        if (Boolean.TRUE.equals(module.inventoryOnly.getValue())) {
            return this instanceof class_490;
        }
        return true;
    }

    @Unique
    private float leonware$deltaTime() {
        long now = System.nanoTime();
        if (this.leonware$lastFrameTime == 0) {
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
    private float leonware$fast(float end, float start, float multiple) {
        float clampedDelta = leonware$deltaTime() * multiple;
        if (clampedDelta < 0.0f) {
            clampedDelta = 0.0f;
        }
        if (clampedDelta > 1.0f) {
            clampedDelta = 1.0f;
        }
        return ((1.0f - clampedDelta) * end) + (clampedDelta * start);
    }

    @Unique
    private float leonware$easeOutCubic(float t) {
        return 1.0f - ((float) Math.pow(1.0f - Math.min(1.0f, Math.max(0.0f, t)), 3.0d));
    }

    @Unique
    private float leonware$easeOutBack(float t) {
        float t2 = Math.min(1.0f, Math.max(0.0f, t));
        float c3 = 1.70158f + 1.0f;
        return 1.0f + (c3 * ((float) Math.pow(t2 - 1.0f, 3.0d))) + (1.70158f * ((float) Math.pow(t2 - 1.0f, 2.0d)));
    }

    @Unique
    private float leonware$applyEasing(float raw) {
        if (this.leonware$closing) {
            return leonware$easeOutCubic(raw);
        }
        switch (InventoryAnimationModule.getInstance().animationType.getValue()) {
            case "Bounce":
                break;
        }
        return leonware$easeOutCubic(raw);
    }

    @Unique
    private void leonware$applyTransform(class_332 ctx, float v) {
        float cx;
        float cy;
        cx = this.field_22789 / 2.0f;
        cy = this.field_22790 / 2.0f;
        switch (InventoryAnimationModule.getInstance().animationType.getValue()) {
            case "Scale":
            case "Bounce":
                ctx.method_51448().method_22904(cx, cy, 0.0d);
                ctx.method_51448().method_22905(Math.max(0.001f, v), Math.max(0.001f, v), 1.0f);
                ctx.method_51448().method_22904(-cx, -cy, 0.0d);
                break;
            case "SlideUp":
                ctx.method_51448().method_22904(0.0d, ((double) this.field_22790) * (1.0d - ((double) v)), 0.0d);
                break;
            case "SlideDown":
                ctx.method_51448().method_22904(0.0d, ((double) (-this.field_22790)) * (1.0d - ((double) v)), 0.0d);
                break;
            case "SlideLeft":
                ctx.method_51448().method_22904(((double) this.field_22789) * (1.0d - ((double) v)), 0.0d, 0.0d);
                break;
            case "SlideRight":
                ctx.method_51448().method_22904(((double) (-this.field_22789)) * (1.0d - ((double) v)), 0.0d, 0.0d);
                break;
            case "Flip":
                ctx.method_51448().method_22904(cx, cy, 0.0d);
                ctx.method_51448().method_22905(Math.max(0.001f, v), 1.0f, 1.0f);
                ctx.method_51448().method_22904(-cx, -cy, 0.0d);
                break;
            case "Warp":
                float scale = this.leonware$closing ? v : 1.15f - (0.15f * v);
                ctx.method_51448().method_22904(cx, cy, 0.0d);
                ctx.method_51448().method_22905(Math.max(0.001f, scale), Math.max(0.001f, scale), 1.0f);
                ctx.method_51448().method_22904(-cx, -cy, 0.0d);
                break;
            case "Glitch":
                float shake = (1.0f - v) * 35.0f * ((float) Math.sin(v * 28.0f));
                float scale2 = 0.88f + (0.12f * v);
                ctx.method_51448().method_22904(shake, 0.0d, 0.0d);
                ctx.method_51448().method_22904(cx, cy, 0.0d);
                ctx.method_51448().method_22905(scale2, scale2, 1.0f);
                ctx.method_51448().method_22904(-cx, -cy, 0.0d);
                break;
        }
    }

    @Inject(method = {"render"}, at = {@At("HEAD")}, cancellable = true)
    private void leonware$onRenderPre(class_332 ctx, int mx, int my, float delta, CallbackInfo ci) {
        this.leonware$pushed = false;
        if (this.leonware$scheduledClose) {
            ci.cancel();
            return;
        }
        if (leonware$isActive()) {
            float speed = InventoryAnimationModule.getInstance().speed.getValue().floatValue();
            if (this.leonware$closing) {
                this.leonware$progress = leonware$fast(this.leonware$progress, 0.0f, speed * 3.0f);
                if (this.leonware$progress < 0.05f && !this.leonware$cursorHidden) {
                    this.leonware$cursorHidden = true;
                    GLFW.glfwSetInputMode(this.field_22787.method_22683().method_4490(), 208897, 212994);
                }
                if (this.leonware$progress < 0.005f) {
                    this.leonware$scheduledClose = true;
                    this.leonware$bypassClose = true;
                    GLFW.glfwSetInputMode(this.field_22787.method_22683().method_4490(), 208897, 212993);
                    this.leonware$cursorHidden = false;
                    class_310 class_310Var = this.field_22787;
                    MixinInventoryAnimation<T> mixinInventoryAnimation = this;
                    Objects.requireNonNull(mixinInventoryAnimation);
                    class_310Var.execute(mixinInventoryAnimation::method_25419);
                    ci.cancel();
                    return;
                }
            } else {
                this.leonware$progress = Math.min(1.0f, leonware$fast(this.leonware$progress, 1.0f, speed));
                if (this.leonware$progress >= 0.999f) {
                    return;
                }
            }
            if (InventoryAnimationModule.getInstance().animationType.is("Fade")) {
                return;
            }
            ctx.method_51448().method_22903();
            this.leonware$pushed = true;
            leonware$applyTransform(ctx, leonware$applyEasing(this.leonware$progress));
        }
    }

    @Inject(method = {"render"}, at = {@At("TAIL")})
    private void leonware$onRenderPost(class_332 ctx, int mx, int my, float delta, CallbackInfo ci) {
        if (this.leonware$pushed) {
            ctx.method_51448().method_22909();
            this.leonware$pushed = false;
        }
        if (leonware$isActive() && !this.leonware$scheduledClose && InventoryAnimationModule.getInstance().animationType.is("Fade")) {
            if (this.leonware$progress < 0.999f || this.leonware$closing) {
                float alpha = 1.0f - leonware$applyEasing(this.leonware$progress);
                if (alpha > 0.005f) {
                    RenderSystem.disableDepthTest();
                    RenderUtil.RECT.draw(ctx.method_51448(), 0.0f, 0.0f, this.field_22789, this.field_22790, 0.0f, new Color(0, 0, 0, Math.min(255, (int) (alpha * 255.0f))));
                    RenderSystem.enableDepthTest();
                }
            }
        }
    }

    @Inject(method = {"close"}, at = {@At("HEAD")}, cancellable = true)
    private void leonware$onClose(CallbackInfo ci) {
        if (!this.leonware$bypassClose && leonware$isActive()) {
            if (this.leonware$closing || this.leonware$scheduledClose) {
                ci.cancel();
            } else if (this.leonware$progress > 0.05f) {
                this.leonware$closing = true;
                ci.cancel();
            }
        }
    }
}

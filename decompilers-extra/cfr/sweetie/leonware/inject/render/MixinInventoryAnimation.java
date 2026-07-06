/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.class_1703
 *  net.minecraft.class_2561
 *  net.minecraft.class_332
 *  net.minecraft.class_437
 *  net.minecraft.class_465
 *  net.minecraft.class_490
 *  org.lwjgl.glfw.GLFW
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package sweetie.leonware.inject.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.minecraft.class_1703;
import net.minecraft.class_2561;
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

@Mixin(value={class_465.class})
public abstract class MixinInventoryAnimation<T extends class_1703>
extends class_437 {
    @Unique
    private float leonware$progress = 0.0f;
    @Unique
    private boolean leonware$closing = false;
    @Unique
    private boolean leonware$bypassClose = false;
    @Unique
    private boolean leonware$scheduledClose = false;
    @Unique
    private boolean leonware$pushed = false;
    @Unique
    private boolean leonware$cursorHidden = false;
    @Unique
    private long leonware$lastFrameTime = 0L;

    protected MixinInventoryAnimation(class_2561 title) {
        super(title);
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
        if (this.leonware$lastFrameTime == 0L) {
            this.leonware$lastFrameTime = now;
            return 0.016f;
        }
        float dt = (float)(now - this.leonware$lastFrameTime) / 1.0E9f;
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
        float c1 = 1.70158f;
        float c3 = c1 + 1.0f;
        return 1.0f + c3 * (float)Math.pow(t - 1.0f, 3.0) + c1 * (float)Math.pow(t - 1.0f, 2.0);
    }

    @Unique
    private float leonware$applyEasing(float raw) {
        if (this.leonware$closing) {
            return this.leonware$easeOutCubic(raw);
        }
        return switch ((String)InventoryAnimationModule.getInstance().animationType.getValue()) {
            case "Bounce" -> Math.max(0.001f, this.leonware$easeOutBack(raw));
            default -> this.leonware$easeOutCubic(raw);
        };
    }

    @Unique
    private void leonware$applyTransform(class_332 ctx, float v) {
        float cx = (float)this.field_22789 / 2.0f;
        float cy = (float)this.field_22790 / 2.0f;
        switch ((String)InventoryAnimationModule.getInstance().animationType.getValue()) {
            case "Scale": 
            case "Bounce": {
                ctx.method_51448().method_22904((double)cx, (double)cy, 0.0);
                ctx.method_51448().method_22905(Math.max(0.001f, v), Math.max(0.001f, v), 1.0f);
                ctx.method_51448().method_22904((double)(-cx), (double)(-cy), 0.0);
                break;
            }
            case "SlideUp": {
                ctx.method_51448().method_22904(0.0, (double)this.field_22790 * (1.0 - (double)v), 0.0);
                break;
            }
            case "SlideDown": {
                ctx.method_51448().method_22904(0.0, (double)(-this.field_22790) * (1.0 - (double)v), 0.0);
                break;
            }
            case "SlideLeft": {
                ctx.method_51448().method_22904((double)this.field_22789 * (1.0 - (double)v), 0.0, 0.0);
                break;
            }
            case "SlideRight": {
                ctx.method_51448().method_22904((double)(-this.field_22789) * (1.0 - (double)v), 0.0, 0.0);
                break;
            }
            case "Flip": {
                ctx.method_51448().method_22904((double)cx, (double)cy, 0.0);
                ctx.method_51448().method_22905(Math.max(0.001f, v), 1.0f, 1.0f);
                ctx.method_51448().method_22904((double)(-cx), (double)(-cy), 0.0);
                break;
            }
            case "Warp": {
                float scale = this.leonware$closing ? v : 1.15f - 0.15f * v;
                ctx.method_51448().method_22904((double)cx, (double)cy, 0.0);
                ctx.method_51448().method_22905(Math.max(0.001f, scale), Math.max(0.001f, scale), 1.0f);
                ctx.method_51448().method_22904((double)(-cx), (double)(-cy), 0.0);
                break;
            }
            case "Glitch": {
                float shake = (1.0f - v) * 35.0f * (float)Math.sin(v * 28.0f);
                float scale = 0.88f + 0.12f * v;
                ctx.method_51448().method_22904((double)shake, 0.0, 0.0);
                ctx.method_51448().method_22904((double)cx, (double)cy, 0.0);
                ctx.method_51448().method_22905(scale, scale, 1.0f);
                ctx.method_51448().method_22904((double)(-cx), (double)(-cy), 0.0);
                break;
            }
        }
    }

    @Inject(method={"render"}, at={@At(value="HEAD")}, cancellable=true)
    private void leonware$onRenderPre(class_332 ctx, int mx, int my, float delta, CallbackInfo ci) {
        this.leonware$pushed = false;
        if (this.leonware$scheduledClose) {
            ci.cancel();
            return;
        }
        if (!this.leonware$isActive()) {
            return;
        }
        float speed = ((Float)InventoryAnimationModule.getInstance().speed.getValue()).floatValue();
        if (this.leonware$closing) {
            this.leonware$progress = this.leonware$fast(this.leonware$progress, 0.0f, speed * 3.0f);
            if (this.leonware$progress < 0.05f && !this.leonware$cursorHidden) {
                this.leonware$cursorHidden = true;
                GLFW.glfwSetInputMode((long)this.field_22787.method_22683().method_4490(), (int)208897, (int)212994);
            }
            if (this.leonware$progress < 0.005f) {
                this.leonware$scheduledClose = true;
                this.leonware$bypassClose = true;
                GLFW.glfwSetInputMode((long)this.field_22787.method_22683().method_4490(), (int)208897, (int)212993);
                this.leonware$cursorHidden = false;
                this.field_22787.execute(() -> ((class_437)this).method_25419());
                ci.cancel();
                return;
            }
        } else {
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

    @Inject(method={"render"}, at={@At(value="TAIL")})
    private void leonware$onRenderPost(class_332 ctx, int mx, int my, float delta, CallbackInfo ci) {
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
        float alpha = 1.0f - this.leonware$applyEasing(this.leonware$progress);
        if (alpha > 0.005f) {
            RenderSystem.disableDepthTest();
            RenderUtil.RECT.draw(ctx.method_51448(), 0.0f, 0.0f, (float)this.field_22789, (float)this.field_22790, 0.0f, new Color(0, 0, 0, Math.min(255, (int)(alpha * 255.0f))));
            RenderSystem.enableDepthTest();
        }
    }

    @Inject(method={"close"}, at={@At(value="HEAD")}, cancellable=true)
    private void leonware$onClose(CallbackInfo ci) {
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


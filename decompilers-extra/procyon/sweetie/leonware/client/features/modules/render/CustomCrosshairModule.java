// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import net.minecraft.class_1041;
import net.minecraft.class_4587;
import net.minecraft.class_332;
import net.minecraft.class_7833;
import net.minecraft.class_3532;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.events.player.world.AttackEvent;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.render.Render2DEvent;
import sweetie.leonware.api.module.setting.Setting;
import java.awt.Color;
import sweetie.leonware.api.module.setting.ColorSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Custom Crosshair", category = Category.RENDER)
public class CustomCrosshairModule extends Module
{
    private static final CustomCrosshairModule instance;
    public final SliderSetting width;
    public final SliderSetting height;
    public final SliderSetting distance;
    public final BooleanSetting movable;
    public final SliderSetting scatterLimit;
    public final SliderSetting scatterSpeed;
    public final BooleanSetting leftRect;
    public final BooleanSetting rightRect;
    public final BooleanSetting upRect;
    public final BooleanSetting downRect;
    public final BooleanSetting centerRect;
    public final BooleanSetting rainbow;
    public final ColorSetting colorSetting;
    public final SliderSetting rotate;
    public final BooleanSetting spin;
    public final SliderSetting spinSpeed;
    private float currentSpread;
    private float prevSpread;
    private float spinAngle;
    
    public CustomCrosshairModule() {
        this.width = new SliderSetting("Width").value(4.0f).range(0.0f, 50.0f).step(0.5f);
        this.height = new SliderSetting("Height").value(1.0f).range(0.0f, 10.0f).step(0.5f);
        this.distance = new SliderSetting("Distance").value(3.0f).range(0.0f, 30.0f).step(0.5f);
        this.movable = new BooleanSetting("Movable").value(true);
        this.scatterLimit = new SliderSetting("Scatter Limit").value(15.0f).range(6.0f, 30.0f).step(1.0f).setVisible(() -> this.movable.getValue());
        this.scatterSpeed = new SliderSetting("Scatter Speed").value(0.15f).range(0.05f, 0.3f).step(0.01f).setVisible(() -> this.movable.getValue());
        this.leftRect = new BooleanSetting("Left").value(true);
        this.rightRect = new BooleanSetting("Right").value(true);
        this.upRect = new BooleanSetting("Up").value(true);
        this.downRect = new BooleanSetting("Down").value(true);
        this.centerRect = new BooleanSetting("Center").value(true);
        this.rainbow = new BooleanSetting("Rainbow").value(false);
        this.colorSetting = new ColorSetting("Color").value(new Color(0, 255, 0)).setVisible(() -> !this.rainbow.getValue());
        this.rotate = new SliderSetting("Rotate").value(0.0f).range(0.0f, 90.0f).step(1.0f);
        this.spin = new BooleanSetting("Spin").value(false);
        this.spinSpeed = new SliderSetting("Spin Speed").value(2.0f).range(0.5f, 10.0f).step(0.1f).setVisible(() -> this.spin.getValue());
        this.currentSpread = 0.0f;
        this.prevSpread = 0.0f;
        this.spinAngle = 0.0f;
        this.addSettings(this.width, this.height, this.distance, this.movable, this.scatterLimit, this.scatterSpeed, this.leftRect, this.rightRect, this.upRect, this.downRect, this.centerRect, this.rainbow, this.colorSetting, this.rotate, this.spin, this.spinSpeed);
    }
    
    @Override
    public void onEvent() {
        final EventListener renderEvent = Render2DEvent.getInstance().subscribe(new Listener<Render2DEvent.Render2DEventData>(event -> {
            if (CustomCrosshairModule.mc.field_1724 == null || CustomCrosshairModule.mc.field_1687 == null) {
                return;
            }
            else {
                this.renderCrosshair(event);
                return;
            }
        }));
        final EventListener attackEvent = AttackEvent.getInstance().subscribe(new Listener<AttackEvent.AttackEventData>(event -> {
            if (CustomCrosshairModule.mc.field_1724 == null || !this.movable.getValue()) {
                return;
            }
            else {
                this.currentSpread += 6.0f;
                if (this.currentSpread > this.scatterLimit.getValue()) {
                    this.currentSpread = (float)this.scatterLimit.getValue().intValue();
                }
                return;
            }
        }));
        this.addEvents(renderEvent, attackEvent);
    }
    
    private void renderCrosshair(final Render2DEvent.Render2DEventData event) {
        if (!this.movable.getValue()) {
            this.currentSpread = 0.0f;
        }
        final class_332 context = event.context();
        final class_4587 matrices = event.matrixStack();
        final class_1041 window = CustomCrosshairModule.mc.method_22683();
        final int color = this.rainbow.getValue() ? this.getRainbowColor() : this.colorSetting.getValue().getRGB();
        final float centerX = window.method_4486() / 2.0f;
        final float centerY = window.method_4502() / 2.0f;
        final float w = this.width.getValue();
        final float h = this.height.getValue();
        final float dist = this.distance.getValue();
        float angle = this.rotate.getValue();
        if (this.spin.getValue()) {
            this.spinAngle += this.spinSpeed.getValue() * event.partialTicks();
            if (this.spinAngle > 360.0f) {
                this.spinAngle -= 360.0f;
            }
            angle += this.spinAngle;
        }
        final float spread = class_3532.method_16439(event.partialTicks(), this.prevSpread, this.currentSpread);
        matrices.method_22903();
        matrices.method_46416(centerX, centerY, 0.0f);
        if (angle != 0.0f) {
            matrices.method_22907(class_7833.field_40718.rotationDegrees(angle));
        }
        if (this.centerRect.getValue()) {
            context.method_25294((int)(-h), (int)(-h), (int)h, (int)h, color);
        }
        if (this.upRect.getValue()) {
            context.method_25294((int)(-h), (int)(-h - dist - w - spread), (int)h, (int)(-h - dist - spread), color);
        }
        if (this.leftRect.getValue()) {
            context.method_25294((int)(-h - dist - w - spread), (int)(-h), (int)(-h - dist - spread), (int)h, color);
        }
        if (this.downRect.getValue()) {
            context.method_25294((int)(-h), (int)(h + dist + spread), (int)h, (int)(h + dist + w + spread), color);
        }
        if (this.rightRect.getValue()) {
            context.method_25294((int)(h + dist + spread), (int)(-h), (int)(h + dist + w + spread), (int)h, color);
        }
        matrices.method_22909();
        this.prevSpread = this.currentSpread;
        this.currentSpread -= this.scatterSpeed.getValue();
        if (this.currentSpread < 0.0f) {
            this.currentSpread = 0.0f;
        }
    }
    
    private int getRainbowColor() {
        final float hue = System.currentTimeMillis() % 3000L / 3000.0f;
        return Color.HSBtoRGB(hue, 0.8f, 1.0f);
    }
    
    @Generated
    public static CustomCrosshairModule getInstance() {
        return CustomCrosshairModule.instance;
    }
    
    static {
        instance = new CustomCrosshairModule();
    }
}

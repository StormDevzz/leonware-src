/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1041
 *  net.minecraft.class_332
 *  net.minecraft.class_3532
 *  net.minecraft.class_4587
 *  net.minecraft.class_7833
 */
package sweetie.leonware.client.features.modules.render;

import java.awt.Color;
import lombok.Generated;
import net.minecraft.class_1041;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_7833;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.world.AttackEvent;
import sweetie.leonware.api.event.events.render.Render2DEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ColorSetting;
import sweetie.leonware.api.module.setting.SliderSetting;

@ModuleRegister(name="Custom Crosshair", category=Category.RENDER)
public class CustomCrosshairModule
extends Module {
    private static final CustomCrosshairModule instance = new CustomCrosshairModule();
    public final SliderSetting width = new SliderSetting("Width").value(Float.valueOf(4.0f)).range(0.0f, 50.0f).step(0.5f);
    public final SliderSetting height = new SliderSetting("Height").value(Float.valueOf(1.0f)).range(0.0f, 10.0f).step(0.5f);
    public final SliderSetting distance = new SliderSetting("Distance").value(Float.valueOf(3.0f)).range(0.0f, 30.0f).step(0.5f);
    public final BooleanSetting movable = new BooleanSetting("Movable").value(true);
    public final SliderSetting scatterLimit = new SliderSetting("Scatter Limit").value(Float.valueOf(15.0f)).range(6.0f, 30.0f).step(1.0f).setVisible(() -> (Boolean)this.movable.getValue());
    public final SliderSetting scatterSpeed = new SliderSetting("Scatter Speed").value(Float.valueOf(0.15f)).range(0.05f, 0.3f).step(0.01f).setVisible(() -> (Boolean)this.movable.getValue());
    public final BooleanSetting leftRect = new BooleanSetting("Left").value(true);
    public final BooleanSetting rightRect = new BooleanSetting("Right").value(true);
    public final BooleanSetting upRect = new BooleanSetting("Up").value(true);
    public final BooleanSetting downRect = new BooleanSetting("Down").value(true);
    public final BooleanSetting centerRect = new BooleanSetting("Center").value(true);
    public final BooleanSetting rainbow = new BooleanSetting("Rainbow").value(false);
    public final ColorSetting colorSetting = new ColorSetting("Color").value(new Color(0, 255, 0)).setVisible(() -> (Boolean)this.rainbow.getValue() == false);
    public final SliderSetting rotate = new SliderSetting("Rotate").value(Float.valueOf(0.0f)).range(0.0f, 90.0f).step(1.0f);
    public final BooleanSetting spin = new BooleanSetting("Spin").value(false);
    public final SliderSetting spinSpeed = new SliderSetting("Spin Speed").value(Float.valueOf(2.0f)).range(0.5f, 10.0f).step(0.1f).setVisible(() -> (Boolean)this.spin.getValue());
    private float currentSpread = 0.0f;
    private float prevSpread = 0.0f;
    private float spinAngle = 0.0f;

    public CustomCrosshairModule() {
        this.addSettings(this.width, this.height, this.distance, this.movable, this.scatterLimit, this.scatterSpeed, this.leftRect, this.rightRect, this.upRect, this.downRect, this.centerRect, this.rainbow, this.colorSetting, this.rotate, this.spin, this.spinSpeed);
    }

    @Override
    public void onEvent() {
        EventListener renderEvent = Render2DEvent.getInstance().subscribe(new Listener<Render2DEvent.Render2DEventData>(event -> {
            if (CustomCrosshairModule.mc.field_1724 == null || CustomCrosshairModule.mc.field_1687 == null) {
                return;
            }
            this.renderCrosshair((Render2DEvent.Render2DEventData)event);
        }));
        EventListener attackEvent = AttackEvent.getInstance().subscribe(new Listener<AttackEvent.AttackEventData>(event -> {
            if (CustomCrosshairModule.mc.field_1724 == null || !((Boolean)this.movable.getValue()).booleanValue()) {
                return;
            }
            this.currentSpread += 6.0f;
            if (this.currentSpread > ((Float)this.scatterLimit.getValue()).floatValue()) {
                this.currentSpread = ((Float)this.scatterLimit.getValue()).intValue();
            }
        }));
        this.addEvents(renderEvent, attackEvent);
    }

    private void renderCrosshair(Render2DEvent.Render2DEventData event) {
        if (!((Boolean)this.movable.getValue()).booleanValue()) {
            this.currentSpread = 0.0f;
        }
        class_332 context = event.context();
        class_4587 matrices = event.matrixStack();
        class_1041 window = mc.method_22683();
        int color = (Boolean)this.rainbow.getValue() != false ? this.getRainbowColor() : ((Color)this.colorSetting.getValue()).getRGB();
        float centerX = (float)window.method_4486() / 2.0f;
        float centerY = (float)window.method_4502() / 2.0f;
        float w = ((Float)this.width.getValue()).floatValue();
        float h = ((Float)this.height.getValue()).floatValue();
        float dist = ((Float)this.distance.getValue()).floatValue();
        float angle = ((Float)this.rotate.getValue()).floatValue();
        if (((Boolean)this.spin.getValue()).booleanValue()) {
            this.spinAngle += ((Float)this.spinSpeed.getValue()).floatValue() * event.partialTicks();
            if (this.spinAngle > 360.0f) {
                this.spinAngle -= 360.0f;
            }
            angle += this.spinAngle;
        }
        float spread = class_3532.method_16439((float)event.partialTicks(), (float)this.prevSpread, (float)this.currentSpread);
        matrices.method_22903();
        matrices.method_46416(centerX, centerY, 0.0f);
        if (angle != 0.0f) {
            matrices.method_22907(class_7833.field_40718.rotationDegrees(angle));
        }
        if (((Boolean)this.centerRect.getValue()).booleanValue()) {
            context.method_25294((int)(-h), (int)(-h), (int)h, (int)h, color);
        }
        if (((Boolean)this.upRect.getValue()).booleanValue()) {
            context.method_25294((int)(-h), (int)(-h - dist - w - spread), (int)h, (int)(-h - dist - spread), color);
        }
        if (((Boolean)this.leftRect.getValue()).booleanValue()) {
            context.method_25294((int)(-h - dist - w - spread), (int)(-h), (int)(-h - dist - spread), (int)h, color);
        }
        if (((Boolean)this.downRect.getValue()).booleanValue()) {
            context.method_25294((int)(-h), (int)(h + dist + spread), (int)h, (int)(h + dist + w + spread), color);
        }
        if (((Boolean)this.rightRect.getValue()).booleanValue()) {
            context.method_25294((int)(h + dist + spread), (int)(-h), (int)(h + dist + w + spread), (int)h, color);
        }
        matrices.method_22909();
        this.prevSpread = this.currentSpread;
        this.currentSpread -= ((Float)this.scatterSpeed.getValue()).floatValue();
        if (this.currentSpread < 0.0f) {
            this.currentSpread = 0.0f;
        }
    }

    private int getRainbowColor() {
        float hue = (float)(System.currentTimeMillis() % 3000L) / 3000.0f;
        return Color.HSBtoRGB(hue, 0.8f, 1.0f);
    }

    @Generated
    public static CustomCrosshairModule getInstance() {
        return instance;
    }
}


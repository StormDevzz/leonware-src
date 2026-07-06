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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/CustomCrosshairModule.class */
@ModuleRegister(name = "Custom Crosshair", category = Category.RENDER)
public class CustomCrosshairModule extends Module {
    private static final CustomCrosshairModule instance = new CustomCrosshairModule();
    public final SliderSetting width = new SliderSetting("Width").value(Float.valueOf(4.0f)).range(0.0f, 50.0f).step(0.5f);
    public final SliderSetting height = new SliderSetting("Height").value(Float.valueOf(1.0f)).range(0.0f, 10.0f).step(0.5f);
    public final SliderSetting distance = new SliderSetting("Distance").value(Float.valueOf(3.0f)).range(0.0f, 30.0f).step(0.5f);
    public final BooleanSetting movable = new BooleanSetting("Movable").value((Boolean) true);
    public final SliderSetting scatterLimit = new SliderSetting("Scatter Limit").value(Float.valueOf(15.0f)).range(6.0f, 30.0f).step(1.0f).setVisible(() -> {
        return this.movable.getValue();
    });
    public final SliderSetting scatterSpeed = new SliderSetting("Scatter Speed").value(Float.valueOf(0.15f)).range(0.05f, 0.3f).step(0.01f).setVisible(() -> {
        return this.movable.getValue();
    });
    public final BooleanSetting leftRect = new BooleanSetting("Left").value((Boolean) true);
    public final BooleanSetting rightRect = new BooleanSetting("Right").value((Boolean) true);
    public final BooleanSetting upRect = new BooleanSetting("Up").value((Boolean) true);
    public final BooleanSetting downRect = new BooleanSetting("Down").value((Boolean) true);
    public final BooleanSetting centerRect = new BooleanSetting("Center").value((Boolean) true);
    public final BooleanSetting rainbow = new BooleanSetting("Rainbow").value((Boolean) false);
    public final ColorSetting colorSetting = new ColorSetting("Color").value(new Color(0, 255, 0)).setVisible(() -> {
        return Boolean.valueOf(!this.rainbow.getValue().booleanValue());
    });
    public final SliderSetting rotate = new SliderSetting("Rotate").value(Float.valueOf(0.0f)).range(0.0f, 90.0f).step(1.0f);
    public final BooleanSetting spin = new BooleanSetting("Spin").value((Boolean) false);
    public final SliderSetting spinSpeed = new SliderSetting("Spin Speed").value(Float.valueOf(2.0f)).range(0.5f, 10.0f).step(0.1f).setVisible(() -> {
        return this.spin.getValue();
    });
    private float currentSpread = 0.0f;
    private float prevSpread = 0.0f;
    private float spinAngle = 0.0f;

    @Generated
    public static CustomCrosshairModule getInstance() {
        return instance;
    }

    /* JADX WARN: Type inference failed for: r1v18, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v23, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v38, types: [sweetie.leonware.api.module.setting.ColorSetting] */
    /* JADX WARN: Type inference failed for: r1v49, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    public CustomCrosshairModule() {
        addSettings(this.width, this.height, this.distance, this.movable, this.scatterLimit, this.scatterSpeed, this.leftRect, this.rightRect, this.upRect, this.downRect, this.centerRect, this.rainbow, this.colorSetting, this.rotate, this.spin, this.spinSpeed);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener renderEvent = Render2DEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1724 == null || mc.field_1687 == null) {
                return;
            }
            renderCrosshair(event);
        }));
        EventListener attackEvent = AttackEvent.getInstance().subscribe(new Listener(event2 -> {
            if (mc.field_1724 == null || !this.movable.getValue().booleanValue()) {
                return;
            }
            this.currentSpread += 6.0f;
            if (this.currentSpread > this.scatterLimit.getValue().floatValue()) {
                this.currentSpread = this.scatterLimit.getValue().intValue();
            }
        }));
        addEvents(renderEvent, attackEvent);
    }

    private void renderCrosshair(Render2DEvent.Render2DEventData event) {
        if (!this.movable.getValue().booleanValue()) {
            this.currentSpread = 0.0f;
        }
        class_332 context = event.context();
        class_4587 matrices = event.matrixStack();
        class_1041 window = mc.method_22683();
        int color = this.rainbow.getValue().booleanValue() ? getRainbowColor() : this.colorSetting.getValue().getRGB();
        float centerX = window.method_4486() / 2.0f;
        float centerY = window.method_4502() / 2.0f;
        float w = this.width.getValue().floatValue();
        float h = this.height.getValue().floatValue();
        float dist = this.distance.getValue().floatValue();
        float angle = this.rotate.getValue().floatValue();
        if (this.spin.getValue().booleanValue()) {
            this.spinAngle += this.spinSpeed.getValue().floatValue() * event.partialTicks();
            if (this.spinAngle > 360.0f) {
                this.spinAngle -= 360.0f;
            }
            angle += this.spinAngle;
        }
        float spread = class_3532.method_16439(event.partialTicks(), this.prevSpread, this.currentSpread);
        matrices.method_22903();
        matrices.method_46416(centerX, centerY, 0.0f);
        if (angle != 0.0f) {
            matrices.method_22907(class_7833.field_40718.rotationDegrees(angle));
        }
        if (this.centerRect.getValue().booleanValue()) {
            context.method_25294((int) (-h), (int) (-h), (int) h, (int) h, color);
        }
        if (this.upRect.getValue().booleanValue()) {
            context.method_25294((int) (-h), (int) ((((-h) - dist) - w) - spread), (int) h, (int) (((-h) - dist) - spread), color);
        }
        if (this.leftRect.getValue().booleanValue()) {
            context.method_25294((int) ((((-h) - dist) - w) - spread), (int) (-h), (int) (((-h) - dist) - spread), (int) h, color);
        }
        if (this.downRect.getValue().booleanValue()) {
            context.method_25294((int) (-h), (int) (h + dist + spread), (int) h, (int) (h + dist + w + spread), color);
        }
        if (this.rightRect.getValue().booleanValue()) {
            context.method_25294((int) (h + dist + spread), (int) (-h), (int) (h + dist + w + spread), (int) h, color);
        }
        matrices.method_22909();
        this.prevSpread = this.currentSpread;
        this.currentSpread -= this.scatterSpeed.getValue().floatValue();
        if (this.currentSpread < 0.0f) {
            this.currentSpread = 0.0f;
        }
    }

    private int getRainbowColor() {
        float hue = (System.currentTimeMillis() % 3000) / 3000.0f;
        return Color.HSBtoRGB(hue, 0.8f, 1.0f);
    }
}

package sweetie.leonware.client.features.modules.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_243;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_4587;
import net.minecraft.class_7833;
import org.joml.Matrix4f;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.move.JumpEvent;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.system.files.FileUtil;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.api.utils.color.ColorUtil;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.api.utils.render.RenderUtil;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/JumpCircleModule.class */
@ModuleRegister(name = "Jump Circle", category = Category.RENDER)
public class JumpCircleModule extends Module {
    private static final JumpCircleModule instance = new JumpCircleModule();
    private final ModeSetting texture = new ModeSetting("Texture").value("Glow").values("Lean", "Glow");
    private final ModeSetting outAnimation = new ModeSetting("Out animation").value("None").values("In", "None");
    private final SliderSetting size = new SliderSetting("Size").value(Float.valueOf(2.0f)).range(0.1f, 3.0f).step(0.1f);
    private final SliderSetting lifeTime = new SliderSetting("Life time").value(Float.valueOf(10.0f)).range(1.0f, 30.0f).step(1.0f);
    private final SliderSetting spawnDur = new SliderSetting("Spawn duration").value(Float.valueOf(6.0f)).range(1.0f, 30.0f).step(1.0f);
    private final SliderSetting dyingDur = new SliderSetting("Dying duration").value(Float.valueOf(4.0f)).range(1.0f, 30.0f).step(1.0f);
    private final List<Circle> circles = new ArrayList();

    @Generated
    public static JumpCircleModule getInstance() {
        return instance;
    }

    public JumpCircleModule() {
        addSettings(this.texture, this.outAnimation, this.size, this.lifeTime, this.spawnDur, this.dyingDur);
    }

    private String texture() {
        return "circle/" + (this.texture.is("Lean") ? "lean" : "glow_fat");
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            this.circles.removeIf((v0) -> {
                return v0.update();
            });
        }));
        EventListener renderEvent = Render3DEvent.getInstance().subscribe(new Listener(event2 -> {
            class_4587 matrixStack = event2.matrixStack();
            RenderUtil.WORLD.startRender(matrixStack);
            RenderSystem.setShaderTexture(0, FileUtil.getImage(texture()));
            for (Circle circle : this.circles) {
                circle.render(matrixStack);
            }
            RenderUtil.WORLD.endRender(matrixStack);
        }));
        EventListener jumpEvent = JumpEvent.getInstance().subscribe(new Listener(event3 -> {
            this.circles.add(new Circle(mc.field_1724.method_19538().method_1031(0.0d, 0.15d, 0.0d), this.size.getValue().floatValue(), this.circles.size() * 9, this.lifeTime.getValue().intValue() * 50, this.spawnDur.getValue().intValue() * 50, this.dyingDur.getValue().intValue() * 50, this.outAnimation.getValue()));
        }));
        addEvents(updateEvent, renderEvent, jumpEvent);
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/JumpCircleModule$Circle.class */
    private static class Circle {
        private final class_243 position;
        private final float size;
        private final int index;
        private final int lifeTime;
        private final int spawnDur;
        private final int dyingDur;
        private final String animMode;
        private final TimerUtil timerUtil = new TimerUtil();
        private final AnimationUtil animation = new AnimationUtil();
        private final AnimationUtil sizeAnimation = new AnimationUtil();
        private boolean isBack = false;

        @Generated
        public Circle(class_243 position, float size, int index, int lifeTime, int spawnDur, int dyingDur, String animMode) {
            this.position = position;
            this.size = size;
            this.index = index;
            this.lifeTime = lifeTime;
            this.spawnDur = spawnDur;
            this.dyingDur = dyingDur;
            this.animMode = animMode;
        }

        public boolean update() {
            if (this.timerUtil.finished(this.spawnDur + this.lifeTime)) {
                this.isBack = true;
            }
            return this.animation.getValue() <= 0.1d && this.isBack;
        }

        public void render(class_4587 matrixStack) {
            this.animation.update();
            this.sizeAnimation.update();
            AnimationUtil animationUtil = this.sizeAnimation;
            double d = (!this.isBack || this.animMode.contains("None")) ? 1.0d : 0.0d;
            animationUtil.run(d, this.isBack ? this.dyingDur : this.spawnDur, Easing.SINE_OUT);
            this.animation.run(this.isBack ? 0.0d : 1.0d, this.isBack ? this.dyingDur : this.spawnDur, Easing.SINE_OUT);
            float anim = (float) this.animation.getValue();
            int alpha = (int) (anim * 255.0f);
            float scale = (float) (this.sizeAnimation.getValue() * ((double) this.size));
            Color color1 = ColorUtil.setAlpha(UIColors.gradient(this.index), alpha);
            Color color2 = ColorUtil.setAlpha(UIColors.gradient(this.index + 90), alpha);
            Color color3 = ColorUtil.setAlpha(UIColors.gradient(this.index + 180), alpha);
            Color color4 = ColorUtil.setAlpha(UIColors.gradient(this.index + 240), alpha);
            class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
            matrixStack.method_22903();
            matrixStack.method_22904(this.position.field_1352 - QuickImports.mc.method_1561().field_4686.method_19326().method_10216(), this.position.field_1351 - QuickImports.mc.method_1561().field_4686.method_19326().method_10214(), this.position.field_1350 - QuickImports.mc.method_1561().field_4686.method_19326().method_10215());
            matrixStack.method_22907(class_7833.field_40714.rotationDegrees(90.0f));
            matrixStack.method_22907(class_7833.field_40718.rotationDegrees(this.timerUtil.getElapsedTime()));
            Matrix4f matrix = matrixStack.method_23760().method_23761();
            buffer.method_22918(matrix, scale, -scale, 0.0f).method_22913(0.0f, 1.0f).method_39415(color1.getRGB());
            buffer.method_22918(matrix, -scale, -scale, 0.0f).method_22913(1.0f, 1.0f).method_39415(color4.getRGB());
            buffer.method_22918(matrix, -scale, scale, 0.0f).method_22913(1.0f, 0.0f).method_39415(color3.getRGB());
            buffer.method_22918(matrix, scale, scale, 0.0f).method_22913(0.0f, 0.0f).method_39415(color2.getRGB());
            class_286.method_43433(buffer.method_60800());
            matrixStack.method_22909();
        }
    }
}

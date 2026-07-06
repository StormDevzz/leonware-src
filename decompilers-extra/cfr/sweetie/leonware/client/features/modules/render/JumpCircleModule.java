/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  lombok.Generated
 *  net.minecraft.class_243
 *  net.minecraft.class_286
 *  net.minecraft.class_287
 *  net.minecraft.class_289
 *  net.minecraft.class_290
 *  net.minecraft.class_293$class_5596
 *  net.minecraft.class_2960
 *  net.minecraft.class_4587
 *  net.minecraft.class_7833
 *  net.minecraft.class_9801
 *  org.joml.Matrix4f
 */
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
import net.minecraft.class_2960;
import net.minecraft.class_4587;
import net.minecraft.class_7833;
import net.minecraft.class_9801;
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

@ModuleRegister(name="Jump Circle", category=Category.RENDER)
public class JumpCircleModule
extends Module {
    private static final JumpCircleModule instance = new JumpCircleModule();
    private final ModeSetting texture = new ModeSetting("Texture").value("Glow").values("Lean", "Glow");
    private final ModeSetting outAnimation = new ModeSetting("Out animation").value("None").values("In", "None");
    private final SliderSetting size = new SliderSetting("Size").value(Float.valueOf(2.0f)).range(0.1f, 3.0f).step(0.1f);
    private final SliderSetting lifeTime = new SliderSetting("Life time").value(Float.valueOf(10.0f)).range(1.0f, 30.0f).step(1.0f);
    private final SliderSetting spawnDur = new SliderSetting("Spawn duration").value(Float.valueOf(6.0f)).range(1.0f, 30.0f).step(1.0f);
    private final SliderSetting dyingDur = new SliderSetting("Dying duration").value(Float.valueOf(4.0f)).range(1.0f, 30.0f).step(1.0f);
    private final List<Circle> circles = new ArrayList<Circle>();

    public JumpCircleModule() {
        this.addSettings(this.texture, this.outAnimation, this.size, this.lifeTime, this.spawnDur, this.dyingDur);
    }

    private String texture() {
        return "circle/" + (this.texture.is("Lean") ? "lean" : "glow_fat");
    }

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> this.circles.removeIf(Circle::update)));
        EventListener renderEvent = Render3DEvent.getInstance().subscribe(new Listener<Render3DEvent.Render3DEventData>(event -> {
            class_4587 matrixStack = event.matrixStack();
            RenderUtil.WORLD.startRender(matrixStack);
            RenderSystem.setShaderTexture((int)0, (class_2960)FileUtil.getImage(this.texture()));
            for (Circle circle : this.circles) {
                circle.render(matrixStack);
            }
            RenderUtil.WORLD.endRender(matrixStack);
        }));
        EventListener jumpEvent = JumpEvent.getInstance().subscribe(new Listener<JumpEvent>(event -> this.circles.add(new Circle(JumpCircleModule.mc.field_1724.method_19538().method_1031(0.0, 0.15, 0.0), ((Float)this.size.getValue()).floatValue(), this.circles.size() * 9, ((Float)this.lifeTime.getValue()).intValue() * 50, ((Float)this.spawnDur.getValue()).intValue() * 50, ((Float)this.dyingDur.getValue()).intValue() * 50, (String)this.outAnimation.getValue()))));
        this.addEvents(updateEvent, renderEvent, jumpEvent);
    }

    @Generated
    public static JumpCircleModule getInstance() {
        return instance;
    }

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

        public boolean update() {
            if (this.timerUtil.finished(this.spawnDur + this.lifeTime)) {
                this.isBack = true;
            }
            return this.animation.getValue() <= 0.1 && this.isBack;
        }

        public void render(class_4587 matrixStack) {
            this.animation.update();
            this.sizeAnimation.update();
            this.sizeAnimation.run(this.isBack ? (this.animMode.contains("None") ? 1.0 : 0.0) : 1.0, this.isBack ? (long)this.dyingDur : (long)this.spawnDur, Easing.SINE_OUT);
            this.animation.run(this.isBack ? 0.0 : 1.0, this.isBack ? (long)this.dyingDur : (long)this.spawnDur, Easing.SINE_OUT);
            float anim = (float)this.animation.getValue();
            int alpha = (int)(anim * 255.0f);
            float scale = (float)(this.sizeAnimation.getValue() * (double)this.size);
            Color color1 = ColorUtil.setAlpha(UIColors.gradient(this.index), alpha);
            Color color2 = ColorUtil.setAlpha(UIColors.gradient(this.index + 90), alpha);
            Color color3 = ColorUtil.setAlpha(UIColors.gradient(this.index + 180), alpha);
            Color color4 = ColorUtil.setAlpha(UIColors.gradient(this.index + 240), alpha);
            class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
            matrixStack.method_22903();
            matrixStack.method_22904(this.position.field_1352 - QuickImports.mc.method_1561().field_4686.method_19326().method_10216(), this.position.field_1351 - QuickImports.mc.method_1561().field_4686.method_19326().method_10214(), this.position.field_1350 - QuickImports.mc.method_1561().field_4686.method_19326().method_10215());
            matrixStack.method_22907(class_7833.field_40714.rotationDegrees(90.0f));
            matrixStack.method_22907(class_7833.field_40718.rotationDegrees((float)this.timerUtil.getElapsedTime()));
            Matrix4f matrix = matrixStack.method_23760().method_23761();
            buffer.method_22918(matrix, scale, -scale, 0.0f).method_22913(0.0f, 1.0f).method_39415(color1.getRGB());
            buffer.method_22918(matrix, -scale, -scale, 0.0f).method_22913(1.0f, 1.0f).method_39415(color4.getRGB());
            buffer.method_22918(matrix, -scale, scale, 0.0f).method_22913(1.0f, 0.0f).method_39415(color3.getRGB());
            buffer.method_22918(matrix, scale, scale, 0.0f).method_22913(0.0f, 0.0f).method_39415(color2.getRGB());
            class_286.method_43433((class_9801)buffer.method_60800());
            matrixStack.method_22909();
        }

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
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  lombok.Generated
 *  net.minecraft.class_10142
 *  net.minecraft.class_10156
 *  net.minecraft.class_1297
 *  net.minecraft.class_1308
 *  net.minecraft.class_1309
 *  net.minecraft.class_1429
 *  net.minecraft.class_1542
 *  net.minecraft.class_1657
 *  net.minecraft.class_286
 *  net.minecraft.class_287
 *  net.minecraft.class_289
 *  net.minecraft.class_290
 *  net.minecraft.class_293$class_5596
 *  net.minecraft.class_2960
 *  net.minecraft.class_332
 *  net.minecraft.class_437
 *  net.minecraft.class_4587
 *  net.minecraft.class_465
 *  net.minecraft.class_5498
 *  net.minecraft.class_7833
 *  net.minecraft.class_9801
 *  org.joml.Matrix4f
 */
package sweetie.leonware.client.features.modules.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.lang.runtime.SwitchBootstraps;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import lombok.Generated;
import net.minecraft.class_10142;
import net.minecraft.class_10156;
import net.minecraft.class_1297;
import net.minecraft.class_1308;
import net.minecraft.class_1309;
import net.minecraft.class_1429;
import net.minecraft.class_1542;
import net.minecraft.class_1657;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_437;
import net.minecraft.class_4587;
import net.minecraft.class_465;
import net.minecraft.class_5498;
import net.minecraft.class_7833;
import net.minecraft.class_9801;
import org.joml.Matrix4f;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.render.Render2DEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ColorSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.system.configs.FriendManager;
import sweetie.leonware.api.system.files.FileUtil;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.api.utils.color.ColorUtil;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.combat.TargetManager;
import sweetie.leonware.api.utils.math.MathUtil;

@ModuleRegister(name="Pointers", category=Category.RENDER)
public class PointersModule
extends Module {
    private static final PointersModule instance = new PointersModule();
    protected final MultiBooleanSetting targets = new MultiBooleanSetting("Targets").value(new BooleanSetting("Players").value(true), new BooleanSetting("Animals").value(false), new BooleanSetting("Mobs").value(false), new BooleanSetting("Items").value(false));
    private final ModeSetting playerModeC = new ModeSetting("Players mode").value("Client").values("Client", "Custom").setVisible(() -> this.targets.isEnabled("Players"));
    private final ModeSetting animalsModeC = new ModeSetting("Animals mode").value("Client").values("Client", "Custom").setVisible(() -> this.targets.isEnabled("Animals"));
    private final ModeSetting mobModeC = new ModeSetting("Mobs mode").value("Client").values("Client", "Custom").setVisible(() -> this.targets.isEnabled("Mobs"));
    private final ModeSetting friendModeC = new ModeSetting("Friends mode").value("Client").values("Client", "Custom").setVisible(() -> this.targets.isEnabled("Players"));
    private final ModeSetting itemModeC = new ModeSetting("Items mode").value("Client").values("Client", "Custom").setVisible(() -> this.targets.isEnabled("Items"));
    private final ColorSetting playerColor = new ColorSetting("Player color").value(new Color(-1)).setVisible(() -> this.playerModeC.is("Custom") && this.targets.isEnabled("Players"));
    private final ColorSetting animalsColor = new ColorSetting("Animals color").value(new Color(-1)).setVisible(() -> this.animalsModeC.is("Custom") && this.targets.isEnabled("Animals"));
    private final ColorSetting mobColor = new ColorSetting("Mobs color").value(new Color(-1)).setVisible(() -> this.mobModeC.is("Custom") && this.targets.isEnabled("Mobs"));
    private final ColorSetting friendColor = new ColorSetting("Friends color").value(new Color(94, 255, 69)).setVisible(() -> this.friendModeC.is("Custom") && this.targets.isEnabled("Players"));
    private final ColorSetting itemColor = new ColorSetting("Items color").value(new Color(255, 72, 69)).setVisible(() -> this.itemModeC.is("Custom") && this.targets.isEnabled("Items"));
    private final SliderSetting pointerSize = new SliderSetting("Size").value(Float.valueOf(1.0f)).range(0.5f, 2.5f).step(0.1f);
    private final SliderSetting pointerRadius = new SliderSetting("Radius").value(Float.valueOf(40.0f)).range(20.0f, 100.0f).step(1.0f);
    private final ModeSetting animation = new ModeSetting("Animation").value("In").values("Out", "In", "None");
    private final SliderSetting duration = new SliderSetting("Duration").value(Float.valueOf(4.0f)).range(1.0f, 20.0f).step(1.0f);
    private final TargetManager.EntityFilter entityFilter = new TargetManager.EntityFilter(this.targets.getList());
    private final HashSet<class_1297> alive = new HashSet();
    private final HashMap<class_1297, AnimationUtil> animations = new HashMap();
    private final AnimationUtil radiusAnimation = new AnimationUtil();

    public PointersModule() {
        this.addSettings(this.targets, this.playerModeC, this.animalsModeC, this.mobModeC, this.friendModeC, this.itemModeC, this.playerColor, this.animalsColor, this.mobColor, this.friendColor, this.itemColor, this.pointerSize, this.pointerRadius, this.animation, this.duration);
    }

    @Override
    public void onDisable() {
        this.alive.clear();
        this.animations.clear();
        this.radiusAnimation.setValue(0.0);
    }

    @Override
    public void onEvent() {
        EventListener renderEvent = Render2DEvent.getInstance().subscribe(new Listener<Render2DEvent.Render2DEventData>(2, event -> {
            if (PointersModule.mc.field_1687 == null || PointersModule.mc.field_1724 == null) {
                this.alive.clear();
                this.animations.clear();
                this.radiusAnimation.setValue(0.0);
                return;
            }
            this.alive.clear();
            this.radiusAnimation.update();
            this.radiusAnimation.run(this.getContainerSize(), 300L, Easing.EXPO_OUT);
            for (class_1297 entity : PointersModule.mc.field_1687.method_18112()) {
                class_1309 le;
                this.entityFilter.targetSettings = this.targets.getList();
                this.entityFilter.needFriends = true;
                if (PointersModule.mc.field_1724 == entity || (!(entity instanceof class_1542) || !this.targets.isEnabled("Items")) && (!(entity instanceof class_1309) || !this.entityFilter.isValid(le = (class_1309)entity))) continue;
                this.alive.add(entity);
            }
            for (class_1297 entity : this.alive) {
                if (this.animations.containsKey(entity)) continue;
                this.animations.put(entity, new AnimationUtil());
            }
            Iterator<Map.Entry<class_1297, AnimationUtil>> iterator = this.animations.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<class_1297, AnimationUtil> entry = iterator.next();
                class_1297 entity = entry.getKey();
                AnimationUtil anim = entry.getValue();
                boolean isAlive = this.alive.contains(entity);
                anim.update();
                anim.run(isAlive ? 1.0 : 0.0, ((Float)this.duration.getValue()).longValue() * 50L, Easing.SINE_OUT);
                this.drawPointerToEntity((Render2DEvent.Render2DEventData)event, entity, anim);
                if (isAlive || !(anim.getValue() <= 0.0)) continue;
                iterator.remove();
            }
        }));
        this.addEvents(renderEvent);
    }

    private void drawPointerToEntity(Render2DEvent.Render2DEventData event, class_1297 entity, AnimationUtil spawn) {
        float yaw;
        if (PointersModule.mc.field_1724 == null) {
            return;
        }
        class_332 context = event.context();
        class_4587 matrixStack = context.method_51448();
        float centerX = this.getCenterX();
        float centerY = this.getCenterY();
        float spawnAnim = (float)spawn.getValue();
        if ((double)spawnAnim <= 0.0) {
            return;
        }
        float animFactor = 1.0f;
        if (this.animation.is("In")) {
            animFactor = 2.0f - spawnAnim;
        } else if (this.animation.is("Out")) {
            animFactor = 0.3f + 0.7f * spawnAnim;
        }
        float animatedRadius = (float)((double)(((Float)this.pointerRadius.getValue()).floatValue() * animFactor) + this.radiusAnimation.getValue());
        float camYaw = PointersModule.mc.field_1724.method_36454();
        if (PointersModule.mc.field_1690.method_31044() == class_5498.field_26664) {
            camYaw = PointersModule.mc.field_1724.method_5705(event.partialTicks());
        }
        if (Float.isNaN(yaw = this.getEntityYaw(entity) - camYaw) || Float.isInfinite(yaw)) {
            return;
        }
        matrixStack.method_22903();
        matrixStack.method_46416(centerX, centerY, 0.0f);
        matrixStack.method_22907(class_7833.field_40718.rotationDegrees(yaw));
        matrixStack.method_46416(-centerX, -centerY, 0.0f);
        Color color = ColorUtil.setAlpha(this.getEntityColor(entity), (int)(spawnAnim * 255.0f));
        this.drawPointer(context, centerX, centerY - animatedRadius, ((Float)this.pointerSize.getValue()).floatValue() * 20.0f, ColorUtil.setAlpha(color, (int)(255.0f * ((float)color.getAlpha() / 255.0f) * spawnAnim)), false);
        matrixStack.method_22909();
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public void drawPointer(class_332 context, float x, float y, float size, Color color, boolean gps) {
        RenderSystem.setShaderTexture((int)0, (class_2960)FileUtil.getImage("pointers/" + (gps ? "arrow_gps" : "triangle")));
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
        RenderSystem.disableDepthTest();
        Matrix4f matrix = context.method_51448().method_23760().method_23761();
        RenderSystem.setShader((class_10156)class_10142.field_53879);
        class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1585);
        float scaledSize = size + 8.0f;
        buffer.method_22918(matrix, x - scaledSize / 2.0f, y + scaledSize, 0.0f).method_22913(0.0f, 1.0f);
        buffer.method_22918(matrix, x + scaledSize / 2.0f, y + scaledSize, 0.0f).method_22913(1.0f, 1.0f);
        buffer.method_22918(matrix, x + scaledSize / 2.0f, y, 0.0f).method_22913(1.0f, 0.0f);
        buffer.method_22918(matrix, x - scaledSize / 2.0f, y, 0.0f).method_22913(0.0f, 0.0f);
        class_286.method_43433((class_9801)buffer.method_60800());
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderSystem.enableDepthTest();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }

    private float getCenterX() {
        return (float)mc.method_22683().method_4486() / 2.0f;
    }

    private float getCenterY() {
        return (float)mc.method_22683().method_4502() / 2.0f;
    }

    private float getContainerSize() {
        class_437 class_4372 = PointersModule.mc.field_1755;
        if (class_4372 instanceof class_465) {
            class_465 containerScreen = (class_465)class_4372;
            return (float)Math.max(containerScreen.field_22789, containerScreen.field_22790) * 0.05f + (this.pointerRadius.getMax() - ((Float)this.pointerRadius.getValue()).floatValue());
        }
        return 0.0f;
    }

    private float getEntityYaw(class_1297 entity) {
        if (PointersModule.mc.field_1724 == null) {
            return 0.0f;
        }
        double xA = MathUtil.interpolate(PointersModule.mc.field_1724.field_6014, PointersModule.mc.field_1724.method_23317());
        double zA = MathUtil.interpolate(PointersModule.mc.field_1724.field_5969, PointersModule.mc.field_1724.method_23321());
        double x = (double)MathUtil.interpolate(entity.field_6014, entity.method_23317()) - xA;
        double z = (double)MathUtil.interpolate(entity.field_5969, entity.method_23321()) - zA;
        if (Double.isNaN(x) || Double.isNaN(z) || Double.isInfinite(x) || Double.isInfinite(z)) {
            return 0.0f;
        }
        return (float)(-(Math.atan2(x, z) * 57.29577951308232));
    }

    private Color getEntityColor(class_1297 entity) {
        int seed = entity.method_5628() * 13;
        Color gradient = UIColors.gradient(seed);
        class_1297 class_12972 = entity;
        Objects.requireNonNull(class_12972);
        class_1297 class_12973 = class_12972;
        int n = 0;
        return switch (SwitchBootstraps.typeSwitch("typeSwitch", new Object[]{class_1542.class, class_1657.class, class_1429.class, class_1308.class}, (Object)class_12973, n)) {
            case 0 -> {
                class_1542 itemEntity = (class_1542)class_12973;
                if (this.itemModeC.is("Custom")) {
                    yield (Color)this.itemColor.getValue();
                }
                yield gradient;
            }
            case 1 -> {
                class_1657 player = (class_1657)class_12973;
                if (FriendManager.getInstance().contains(player.method_5477().getString())) {
                    if (this.friendModeC.is("Custom")) {
                        yield (Color)this.friendColor.getValue();
                    }
                    yield gradient;
                }
                if (this.playerModeC.is("Custom")) {
                    yield (Color)this.playerColor.getValue();
                }
                yield gradient;
            }
            case 2 -> {
                class_1429 animalEntity = (class_1429)class_12973;
                if (this.animalsModeC.is("Custom")) {
                    yield (Color)this.animalsColor.getValue();
                }
                yield gradient;
            }
            case 3 -> {
                class_1308 mobEntity = (class_1308)class_12973;
                if (this.mobModeC.is("Custom")) {
                    yield (Color)this.mobColor.getValue();
                }
                yield gradient;
            }
            default -> new Color(-1);
        };
    }

    @Generated
    public static PointersModule getInstance() {
        return instance;
    }
}


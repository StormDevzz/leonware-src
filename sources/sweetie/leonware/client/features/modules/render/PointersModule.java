package sweetie.leonware.client.features.modules.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.SwitchBootstraps;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import lombok.Generated;
import net.minecraft.class_10142;
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
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_465;
import net.minecraft.class_5498;
import net.minecraft.class_7833;
import org.joml.Matrix4f;
import org.newsclub.net.unix.AFVSOCKSocketAddress;
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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/PointersModule.class */
@ModuleRegister(name = "Pointers", category = Category.RENDER)
public class PointersModule extends Module {
    private static final PointersModule instance = new PointersModule();
    protected final MultiBooleanSetting targets = new MultiBooleanSetting("Targets").value(new BooleanSetting("Players").value((Boolean) true), new BooleanSetting("Animals").value((Boolean) false), new BooleanSetting("Mobs").value((Boolean) false), new BooleanSetting("Items").value((Boolean) false));
    private final ModeSetting playerModeC = new ModeSetting("Players mode").value("Client").values("Client", "Custom").setVisible(() -> {
        return Boolean.valueOf(this.targets.isEnabled("Players"));
    });
    private final ModeSetting animalsModeC = new ModeSetting("Animals mode").value("Client").values("Client", "Custom").setVisible(() -> {
        return Boolean.valueOf(this.targets.isEnabled("Animals"));
    });
    private final ModeSetting mobModeC = new ModeSetting("Mobs mode").value("Client").values("Client", "Custom").setVisible(() -> {
        return Boolean.valueOf(this.targets.isEnabled("Mobs"));
    });
    private final ModeSetting friendModeC = new ModeSetting("Friends mode").value("Client").values("Client", "Custom").setVisible(() -> {
        return Boolean.valueOf(this.targets.isEnabled("Players"));
    });
    private final ModeSetting itemModeC = new ModeSetting("Items mode").value("Client").values("Client", "Custom").setVisible(() -> {
        return Boolean.valueOf(this.targets.isEnabled("Items"));
    });
    private final ColorSetting playerColor = new ColorSetting("Player color").value(new Color(-1)).setVisible(() -> {
        return Boolean.valueOf(this.playerModeC.is("Custom") && this.targets.isEnabled("Players"));
    });
    private final ColorSetting animalsColor = new ColorSetting("Animals color").value(new Color(-1)).setVisible(() -> {
        return Boolean.valueOf(this.animalsModeC.is("Custom") && this.targets.isEnabled("Animals"));
    });
    private final ColorSetting mobColor = new ColorSetting("Mobs color").value(new Color(-1)).setVisible(() -> {
        return Boolean.valueOf(this.mobModeC.is("Custom") && this.targets.isEnabled("Mobs"));
    });
    private final ColorSetting friendColor = new ColorSetting("Friends color").value(new Color(94, 255, 69)).setVisible(() -> {
        return Boolean.valueOf(this.friendModeC.is("Custom") && this.targets.isEnabled("Players"));
    });
    private final ColorSetting itemColor = new ColorSetting("Items color").value(new Color(255, 72, 69)).setVisible(() -> {
        return Boolean.valueOf(this.itemModeC.is("Custom") && this.targets.isEnabled("Items"));
    });
    private final SliderSetting pointerSize = new SliderSetting("Size").value(Float.valueOf(1.0f)).range(0.5f, 2.5f).step(0.1f);
    private final SliderSetting pointerRadius = new SliderSetting("Radius").value(Float.valueOf(40.0f)).range(20.0f, 100.0f).step(1.0f);
    private final ModeSetting animation = new ModeSetting("Animation").value("In").values("Out", "In", "None");
    private final SliderSetting duration = new SliderSetting("Duration").value(Float.valueOf(4.0f)).range(1.0f, 20.0f).step(1.0f);
    private final TargetManager.EntityFilter entityFilter = new TargetManager.EntityFilter(this.targets.getList());
    private final HashSet<class_1297> alive = new HashSet<>();
    private final HashMap<class_1297, AnimationUtil> animations = new HashMap<>();
    private final AnimationUtil radiusAnimation = new AnimationUtil();

    @Generated
    public static PointersModule getInstance() {
        return instance;
    }

    /* JADX WARN: Type inference failed for: r1v13, types: [sweetie.leonware.api.module.setting.ModeSetting] */
    /* JADX WARN: Type inference failed for: r1v17, types: [sweetie.leonware.api.module.setting.ModeSetting] */
    /* JADX WARN: Type inference failed for: r1v21, types: [sweetie.leonware.api.module.setting.ModeSetting] */
    /* JADX WARN: Type inference failed for: r1v24, types: [sweetie.leonware.api.module.setting.ColorSetting] */
    /* JADX WARN: Type inference failed for: r1v27, types: [sweetie.leonware.api.module.setting.ColorSetting] */
    /* JADX WARN: Type inference failed for: r1v30, types: [sweetie.leonware.api.module.setting.ColorSetting] */
    /* JADX WARN: Type inference failed for: r1v33, types: [sweetie.leonware.api.module.setting.ColorSetting] */
    /* JADX WARN: Type inference failed for: r1v36, types: [sweetie.leonware.api.module.setting.ColorSetting] */
    /* JADX WARN: Type inference failed for: r1v5, types: [sweetie.leonware.api.module.setting.ModeSetting] */
    /* JADX WARN: Type inference failed for: r1v9, types: [sweetie.leonware.api.module.setting.ModeSetting] */
    public PointersModule() {
        addSettings(this.targets, this.playerModeC, this.animalsModeC, this.mobModeC, this.friendModeC, this.itemModeC, this.playerColor, this.animalsColor, this.mobColor, this.friendColor, this.itemColor, this.pointerSize, this.pointerRadius, this.animation, this.duration);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        this.alive.clear();
        this.animations.clear();
        this.radiusAnimation.setValue(0.0d);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener renderEvent = Render2DEvent.getInstance().subscribe(new Listener(2, event -> {
            if (mc.field_1687 == null || mc.field_1724 == null) {
                this.alive.clear();
                this.animations.clear();
                this.radiusAnimation.setValue(0.0d);
                return;
            }
            this.alive.clear();
            this.radiusAnimation.update();
            this.radiusAnimation.run(getContainerSize(), 300L, Easing.EXPO_OUT);
            for (class_1297 entity : mc.field_1687.method_18112()) {
                this.entityFilter.targetSettings = this.targets.getList();
                this.entityFilter.needFriends = true;
                if (mc.field_1724 != entity) {
                    if (!(entity instanceof class_1542) || !this.targets.isEnabled("Items")) {
                        if (entity instanceof class_1309) {
                            class_1309 le = (class_1309) entity;
                            if (this.entityFilter.isValid(le)) {
                            }
                        }
                    }
                    this.alive.add(entity);
                }
            }
            for (class_1297 entity2 : this.alive) {
                if (!this.animations.containsKey(entity2)) {
                    this.animations.put(entity2, new AnimationUtil());
                }
            }
            Iterator<Map.Entry<class_1297, AnimationUtil>> iterator = this.animations.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<class_1297, AnimationUtil> entry = iterator.next();
                class_1297 entity3 = entry.getKey();
                AnimationUtil anim = entry.getValue();
                boolean isAlive = this.alive.contains(entity3);
                anim.update();
                anim.run(isAlive ? 1.0d : 0.0d, this.duration.getValue().longValue() * 50, Easing.SINE_OUT);
                drawPointerToEntity(event, entity3, anim);
                if (!isAlive && anim.getValue() <= 0.0d) {
                    iterator.remove();
                }
            }
        }));
        addEvents(renderEvent);
    }

    private void drawPointerToEntity(Render2DEvent.Render2DEventData event, class_1297 entity, AnimationUtil spawn) {
        if (mc.field_1724 == null) {
            return;
        }
        class_332 context = event.context();
        class_4587 matrixStack = context.method_51448();
        float centerX = getCenterX();
        float centerY = getCenterY();
        float spawnAnim = (float) spawn.getValue();
        if (spawnAnim <= 0.0d) {
            return;
        }
        float animFactor = 1.0f;
        if (this.animation.is("In")) {
            animFactor = 2.0f - spawnAnim;
        } else if (this.animation.is("Out")) {
            animFactor = 0.3f + (0.7f * spawnAnim);
        }
        float animatedRadius = (float) (((double) (this.pointerRadius.getValue().floatValue() * animFactor)) + this.radiusAnimation.getValue());
        float camYaw = mc.field_1724.method_36454();
        if (mc.field_1690.method_31044() == class_5498.field_26664) {
            camYaw = mc.field_1724.method_5705(event.partialTicks());
        }
        float yaw = getEntityYaw(entity) - camYaw;
        if (Float.isNaN(yaw) || Float.isInfinite(yaw)) {
            return;
        }
        matrixStack.method_22903();
        matrixStack.method_46416(centerX, centerY, 0.0f);
        matrixStack.method_22907(class_7833.field_40718.rotationDegrees(yaw));
        matrixStack.method_46416(-centerX, -centerY, 0.0f);
        Color color = ColorUtil.setAlpha(getEntityColor(entity), (int) (spawnAnim * 255.0f));
        drawPointer(context, centerX, centerY - animatedRadius, this.pointerSize.getValue().floatValue() * 20.0f, ColorUtil.setAlpha(color, (int) (255.0f * (color.getAlpha() / 255.0f) * spawnAnim)), false);
        matrixStack.method_22909();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void drawPointer(class_332 context, float x, float y, float size, Color color, boolean gps) {
        RenderSystem.setShaderTexture(0, FileUtil.getImage("pointers/" + (gps ? "arrow_gps" : "triangle")));
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
        RenderSystem.disableDepthTest();
        Matrix4f matrix = context.method_51448().method_23760().method_23761();
        RenderSystem.setShader(class_10142.field_53879);
        class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1585);
        float scaledSize = size + 8.0f;
        buffer.method_22918(matrix, x - (scaledSize / 2.0f), y + scaledSize, 0.0f).method_22913(0.0f, 1.0f);
        buffer.method_22918(matrix, x + (scaledSize / 2.0f), y + scaledSize, 0.0f).method_22913(1.0f, 1.0f);
        buffer.method_22918(matrix, x + (scaledSize / 2.0f), y, 0.0f).method_22913(1.0f, 0.0f);
        buffer.method_22918(matrix, x - (scaledSize / 2.0f), y, 0.0f).method_22913(0.0f, 0.0f);
        class_286.method_43433(buffer.method_60800());
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.enableDepthTest();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }

    private float getCenterX() {
        return mc.method_22683().method_4486() / 2.0f;
    }

    private float getCenterY() {
        return mc.method_22683().method_4502() / 2.0f;
    }

    private float getContainerSize() {
        class_465<?> class_465Var = mc.field_1755;
        if (class_465Var instanceof class_465) {
            class_465<?> containerScreen = class_465Var;
            return (Math.max(containerScreen.field_22789, containerScreen.field_22790) * 0.05f) + (this.pointerRadius.getMax() - this.pointerRadius.getValue().floatValue());
        }
        return 0.0f;
    }

    private float getEntityYaw(class_1297 entity) {
        if (mc.field_1724 == null) {
            return 0.0f;
        }
        double xA = MathUtil.interpolate(mc.field_1724.field_6014, mc.field_1724.method_23317());
        double zA = MathUtil.interpolate(mc.field_1724.field_5969, mc.field_1724.method_23321());
        double x = ((double) MathUtil.interpolate(entity.field_6014, entity.method_23317())) - xA;
        double z = ((double) MathUtil.interpolate(entity.field_5969, entity.method_23321())) - zA;
        if (Double.isNaN(x) || Double.isNaN(z) || Double.isInfinite(x) || Double.isInfinite(z)) {
            return 0.0f;
        }
        return (float) (-(Math.atan2(x, z) * 57.29577951308232d));
    }

    private Color getEntityColor(class_1297 entity) {
        int seed = entity.method_5628() * 13;
        Color gradient = UIColors.gradient(seed);
        Objects.requireNonNull(entity);
        switch ((int) SwitchBootstraps.typeSwitch(MethodHandles.lookup(), "typeSwitch", MethodType.methodType(Integer.TYPE, Object.class, Integer.TYPE), class_1542.class, class_1657.class, class_1429.class, class_1308.class).dynamicInvoker().invoke(entity, 0) /* invoke-custom */) {
            case 0:
                return this.itemModeC.is("Custom") ? this.itemColor.getValue() : gradient;
            case 1:
                class_1657 player = (class_1657) entity;
                return FriendManager.getInstance().contains(player.method_5477().getString()) ? this.friendModeC.is("Custom") ? this.friendColor.getValue() : gradient : this.playerModeC.is("Custom") ? this.playerColor.getValue() : gradient;
            case AFVSOCKSocketAddress.VMADDR_CID_HOST /* 2 */:
                return this.animalsModeC.is("Custom") ? this.animalsColor.getValue() : gradient;
            case 3:
                return this.mobModeC.is("Custom") ? this.mobColor.getValue() : gradient;
            default:
                return new Color(-1);
        }
    }
}

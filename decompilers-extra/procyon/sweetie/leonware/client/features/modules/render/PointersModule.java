// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render;

import java.lang.invoke.CallSite;
import java.lang.reflect.UndeclaredThrowableException;
import java.lang.invoke.MethodHandle;
import java.lang.runtime.SwitchBootstraps;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles;
import lombok.Generated;
import net.minecraft.class_1308;
import net.minecraft.class_1429;
import sweetie.leonware.api.system.configs.FriendManager;
import net.minecraft.class_1657;
import java.util.Objects;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.math.MathUtil;
import net.minecraft.class_437;
import net.minecraft.class_465;
import net.minecraft.class_287;
import org.joml.Matrix4f;
import net.minecraft.class_286;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_289;
import net.minecraft.class_10142;
import sweetie.leonware.api.system.files.FileUtil;
import net.minecraft.class_4587;
import net.minecraft.class_332;
import com.mojang.blaze3d.systems.RenderSystem;
import sweetie.leonware.api.utils.color.ColorUtil;
import net.minecraft.class_7833;
import net.minecraft.class_5498;
import java.util.Iterator;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import java.util.Map;
import net.minecraft.class_1309;
import net.minecraft.class_1542;
import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.api.event.events.render.Render2DEvent;
import sweetie.leonware.api.module.setting.Setting;
import java.awt.Color;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import java.util.HashMap;
import net.minecraft.class_1297;
import java.util.HashSet;
import sweetie.leonware.api.utils.combat.TargetManager;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.ColorSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Pointers", category = Category.RENDER)
public class PointersModule extends Module
{
    private static final PointersModule instance;
    protected final MultiBooleanSetting targets;
    private final ModeSetting playerModeC;
    private final ModeSetting animalsModeC;
    private final ModeSetting mobModeC;
    private final ModeSetting friendModeC;
    private final ModeSetting itemModeC;
    private final ColorSetting playerColor;
    private final ColorSetting animalsColor;
    private final ColorSetting mobColor;
    private final ColorSetting friendColor;
    private final ColorSetting itemColor;
    private final SliderSetting pointerSize;
    private final SliderSetting pointerRadius;
    private final ModeSetting animation;
    private final SliderSetting duration;
    private final TargetManager.EntityFilter entityFilter;
    private final HashSet<class_1297> alive;
    private final HashMap<class_1297, AnimationUtil> animations;
    private final AnimationUtil radiusAnimation;
    
    public PointersModule() {
        this.targets = new MultiBooleanSetting("Targets").value(new BooleanSetting("Players").value(true), new BooleanSetting("Animals").value(false), new BooleanSetting("Mobs").value(false), new BooleanSetting("Items").value(false));
        this.playerModeC = new ModeSetting("Players mode").value("Client").values("Client", "Custom").setVisible(() -> this.targets.isEnabled("Players"));
        this.animalsModeC = new ModeSetting("Animals mode").value("Client").values("Client", "Custom").setVisible(() -> this.targets.isEnabled("Animals"));
        this.mobModeC = new ModeSetting("Mobs mode").value("Client").values("Client", "Custom").setVisible(() -> this.targets.isEnabled("Mobs"));
        this.friendModeC = new ModeSetting("Friends mode").value("Client").values("Client", "Custom").setVisible(() -> this.targets.isEnabled("Players"));
        this.itemModeC = new ModeSetting("Items mode").value("Client").values("Client", "Custom").setVisible(() -> this.targets.isEnabled("Items"));
        this.playerColor = new ColorSetting("Player color").value(new Color(-1)).setVisible(() -> this.playerModeC.is("Custom") && this.targets.isEnabled("Players"));
        this.animalsColor = new ColorSetting("Animals color").value(new Color(-1)).setVisible(() -> this.animalsModeC.is("Custom") && this.targets.isEnabled("Animals"));
        this.mobColor = new ColorSetting("Mobs color").value(new Color(-1)).setVisible(() -> this.mobModeC.is("Custom") && this.targets.isEnabled("Mobs"));
        this.friendColor = new ColorSetting("Friends color").value(new Color(94, 255, 69)).setVisible(() -> this.friendModeC.is("Custom") && this.targets.isEnabled("Players"));
        this.itemColor = new ColorSetting("Items color").value(new Color(255, 72, 69)).setVisible(() -> this.itemModeC.is("Custom") && this.targets.isEnabled("Items"));
        this.pointerSize = new SliderSetting("Size").value(1.0f).range(0.5f, 2.5f).step(0.1f);
        this.pointerRadius = new SliderSetting("Radius").value(40.0f).range(20.0f, 100.0f).step(1.0f);
        this.animation = new ModeSetting("Animation").value("In").values("Out", "In", "None");
        this.duration = new SliderSetting("Duration").value(4.0f).range(1.0f, 20.0f).step(1.0f);
        this.entityFilter = new TargetManager.EntityFilter(this.targets.getList());
        this.alive = new HashSet<class_1297>();
        this.animations = new HashMap<class_1297, AnimationUtil>();
        this.radiusAnimation = new AnimationUtil();
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
        final EventListener renderEvent = Render2DEvent.getInstance().subscribe(new Listener<Render2DEvent.Render2DEventData>(2, event -> {
            if (PointersModule.mc.field_1687 == null || PointersModule.mc.field_1724 == null) {
                this.alive.clear();
                this.animations.clear();
                this.radiusAnimation.setValue(0.0);
                return;
            }
            else {
                this.alive.clear();
                this.radiusAnimation.update();
                this.radiusAnimation.run(this.getContainerSize(), 300L, Easing.EXPO_OUT);
                PointersModule.mc.field_1687.method_18112().iterator();
                final Iterator iterator2;
                while (iterator2.hasNext()) {
                    final class_1297 entity = iterator2.next();
                    this.entityFilter.targetSettings = this.targets.getList();
                    this.entityFilter.needFriends = true;
                    if (PointersModule.mc.field_1724 == entity) {
                        continue;
                    }
                    else {
                        if (!(entity instanceof class_1542) || !this.targets.isEnabled("Items")) {
                            if (entity instanceof final class_1309 le) {
                                if (!this.entityFilter.isValid(le)) {
                                    continue;
                                }
                            }
                            else {
                                continue;
                            }
                        }
                        this.alive.add(entity);
                    }
                }
                this.alive.iterator();
                final Iterator iterator3;
                while (iterator3.hasNext()) {
                    final class_1297 entity2 = iterator3.next();
                    if (!this.animations.containsKey(entity2)) {
                        this.animations.put(entity2, new AnimationUtil());
                    }
                }
                final Iterator<Map.Entry<class_1297, AnimationUtil>> iterator = this.animations.entrySet().iterator();
                while (iterator.hasNext()) {
                    final Map.Entry<class_1297, AnimationUtil> entry = iterator.next();
                    final class_1297 entity3 = entry.getKey();
                    final AnimationUtil anim = entry.getValue();
                    final boolean isAlive = this.alive.contains(entity3);
                    anim.update();
                    anim.run(isAlive ? 1.0 : 0.0, this.duration.getValue().longValue() * 50L, Easing.SINE_OUT);
                    this.drawPointerToEntity(event, entity3, anim);
                    if (!isAlive && anim.getValue() <= 0.0) {
                        iterator.remove();
                    }
                }
                return;
            }
        }));
        this.addEvents(renderEvent);
    }
    
    private void drawPointerToEntity(final Render2DEvent.Render2DEventData event, final class_1297 entity, final AnimationUtil spawn) {
        if (PointersModule.mc.field_1724 == null) {
            return;
        }
        final class_332 context = event.context();
        final class_4587 matrixStack = context.method_51448();
        final float centerX = this.getCenterX();
        final float centerY = this.getCenterY();
        final float spawnAnim = (float)spawn.getValue();
        if (spawnAnim <= 0.0) {
            return;
        }
        float animFactor = 1.0f;
        if (this.animation.is("In")) {
            animFactor = 2.0f - spawnAnim;
        }
        else if (this.animation.is("Out")) {
            animFactor = 0.3f + 0.7f * spawnAnim;
        }
        final float animatedRadius = (float)(this.pointerRadius.getValue() * animFactor + this.radiusAnimation.getValue());
        float camYaw = PointersModule.mc.field_1724.method_36454();
        if (PointersModule.mc.field_1690.method_31044() == class_5498.field_26664) {
            camYaw = PointersModule.mc.field_1724.method_5705(event.partialTicks());
        }
        final float yaw = this.getEntityYaw(entity) - camYaw;
        if (Float.isNaN(yaw) || Float.isInfinite(yaw)) {
            return;
        }
        matrixStack.method_22903();
        matrixStack.method_46416(centerX, centerY, 0.0f);
        matrixStack.method_22907(class_7833.field_40718.rotationDegrees(yaw));
        matrixStack.method_46416(-centerX, -centerY, 0.0f);
        final Color color = ColorUtil.setAlpha(this.getEntityColor(entity), (int)(spawnAnim * 255.0f));
        this.drawPointer(context, centerX, centerY - animatedRadius, this.pointerSize.getValue() * 20.0f, ColorUtil.setAlpha(color, (int)(255.0f * (color.getAlpha() / 255.0f) * spawnAnim)), false);
        matrixStack.method_22909();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public void drawPointer(final class_332 context, final float x, final float y, final float size, final Color color, final boolean gps) {
        RenderSystem.setShaderTexture(0, FileUtil.getImage("pointers/" + (gps ? "arrow_gps" : "triangle")));
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
        RenderSystem.disableDepthTest();
        final Matrix4f matrix = context.method_51448().method_23760().method_23761();
        RenderSystem.setShader(class_10142.field_53879);
        final class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1585);
        final float scaledSize = size + 8.0f;
        buffer.method_22918(matrix, x - scaledSize / 2.0f, y + scaledSize, 0.0f).method_22913(0.0f, 1.0f);
        buffer.method_22918(matrix, x + scaledSize / 2.0f, y + scaledSize, 0.0f).method_22913(1.0f, 1.0f);
        buffer.method_22918(matrix, x + scaledSize / 2.0f, y, 0.0f).method_22913(1.0f, 0.0f);
        buffer.method_22918(matrix, x - scaledSize / 2.0f, y, 0.0f).method_22913(0.0f, 0.0f);
        class_286.method_43433(buffer.method_60800());
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.enableDepthTest();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }
    
    private float getCenterX() {
        return PointersModule.mc.method_22683().method_4486() / 2.0f;
    }
    
    private float getCenterY() {
        return PointersModule.mc.method_22683().method_4502() / 2.0f;
    }
    
    private float getContainerSize() {
        final class_437 field_1755 = PointersModule.mc.field_1755;
        if (field_1755 instanceof class_465) {
            final class_465<?> containerScreen = (class_465<?>)field_1755;
            return Math.max(containerScreen.field_22789, containerScreen.field_22790) * 0.05f + (this.pointerRadius.getMax() - this.pointerRadius.getValue());
        }
        return 0.0f;
    }
    
    private float getEntityYaw(final class_1297 entity) {
        if (PointersModule.mc.field_1724 == null) {
            return 0.0f;
        }
        final double xA = MathUtil.interpolate(PointersModule.mc.field_1724.field_6014, PointersModule.mc.field_1724.method_23317());
        final double zA = MathUtil.interpolate(PointersModule.mc.field_1724.field_5969, PointersModule.mc.field_1724.method_23321());
        final double x = MathUtil.interpolate(entity.field_6014, entity.method_23317()) - xA;
        final double z = MathUtil.interpolate(entity.field_5969, entity.method_23321()) - zA;
        if (Double.isNaN(x) || Double.isNaN(z) || Double.isInfinite(x) || Double.isInfinite(z)) {
            return 0.0f;
        }
        return (float)(-(Math.atan2(x, z) * 57.29577951308232));
    }
    
    private Color getEntityColor(final class_1297 entity) {
        final int seed = entity.method_5628() * 13;
        final Color gradient = UIColors.gradient(seed);
        Objects.requireNonNull(entity);
        return switch (/* invokedynamic(!) */ProcyonInvokeDynamicHelper_3.invoke(entity, false)) {
            case 0 -> {
                final class_1542 itemEntity = (class_1542)entity;
                yield this.itemModeC.is("Custom") ? this.itemColor.getValue() : gradient;
            }
            case 1 -> {
                final class_1657 player = (class_1657)entity;
                yield FriendManager.getInstance().contains(player.method_5477().getString()) ? (this.friendModeC.is("Custom") ? this.friendColor.getValue() : gradient) : (this.playerModeC.is("Custom") ? this.playerColor.getValue() : gradient);
            }
            case 2 -> {
                final class_1429 animalEntity = (class_1429)entity;
                yield this.animalsModeC.is("Custom") ? this.animalsColor.getValue() : gradient;
            }
            case 3 -> {
                final class_1308 mobEntity = (class_1308)entity;
                yield this.mobModeC.is("Custom") ? this.mobColor.getValue() : gradient;
            }
            default -> new Color(-1);
        };
    }
    
    @Generated
    public static PointersModule getInstance() {
        return PointersModule.instance;
    }
    
    static {
        instance = new PointersModule();
    }
    
    // This helper class was generated by Procyon to approximate the behavior of an
    // 'invokedynamic' instruction that it doesn't know how to interpret.
    private static final class ProcyonInvokeDynamicHelper_3
    {
        private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
        private static MethodHandle handle;
        private static volatile int fence;
        
        private static MethodHandle handle() {
            final MethodHandle handle = ProcyonInvokeDynamicHelper_3.handle;
            if (handle != null)
                return handle;
            return ProcyonInvokeDynamicHelper_3.ensureHandle();
        }
        
        private static MethodHandle ensureHandle() {
            ProcyonInvokeDynamicHelper_3.fence = 0;
            MethodHandle handle = ProcyonInvokeDynamicHelper_3.handle;
            if (handle == null) {
                MethodHandles.Lookup lookup = ProcyonInvokeDynamicHelper_3.LOOKUP;
                try {
                    handle = ((CallSite)SwitchBootstraps.typeSwitch(lookup, "typeSwitch", MethodType.methodType(int.class, Object.class, int.class), class_1542.class, class_1657.class, class_1429.class, class_1308.class)).dynamicInvoker();
                }
                catch (Throwable t) {
                    throw new UndeclaredThrowableException(t);
                }
                ProcyonInvokeDynamicHelper_3.fence = 1;
                ProcyonInvokeDynamicHelper_3.handle = handle;
                ProcyonInvokeDynamicHelper_3.fence = 0;
            }
            return handle;
        }
        
        private static int invoke(Object p0, int p1) {
            try {
                return ProcyonInvokeDynamicHelper_3.handle().invokeExact(p0, p1);
            }
            catch (Throwable t) {
                throw new UndeclaredThrowableException(t);
            }
        }
    }
}

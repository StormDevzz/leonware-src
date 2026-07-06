package sweetie.leonware.client.features.modules.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import lombok.Generated;
import net.minecraft.class_10142;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_327;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_746;
import net.minecraft.class_7833;
import org.joml.Matrix4f;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.system.files.FileUtil;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/DamageMarkersModule.class */
@ModuleRegister(name = "Damage Markers", category = Category.RENDER)
public class DamageMarkersModule extends Module {
    private static final DamageMarkersModule instance = new DamageMarkersModule();
    public final SliderSetting markerSize = new SliderSetting("Размер").value(Float.valueOf(0.15f)).range(0.02f, 1.5f).step(0.01f);
    public final SliderSetting baseScale = new SliderSetting("Базовый множитель").value(Float.valueOf(0.25f)).range(0.05f, 1.0f).step(0.01f);
    public final BooleanSetting overshootAnimation = new BooleanSetting("Overshoot анимация").value((Boolean) false);
    public final SliderSetting lifetime = new SliderSetting("Время жизни (мс)").value(Float.valueOf(2000.0f)).range(500.0f, 5000.0f).step(100.0f);
    public final SliderSetting moveSpeed = new SliderSetting("Скорость движения").value(Float.valueOf(0.5f)).range(0.1f, 2.0f).step(0.1f);
    public final BooleanSetting glow = new BooleanSetting("Свечение").value((Boolean) true);
    public final SliderSetting glowSize = new SliderSetting("Размер свечения").value(Float.valueOf(1.0f)).range(0.3f, 3.0f).step(0.1f);
    public final SliderSetting glowAlpha = new SliderSetting("Яркость свечения").value(Float.valueOf(0.65f)).range(0.1f, 1.0f).step(0.05f);
    public final BooleanSetting checkYourself = new BooleanSetting("Показывать себя").value((Boolean) true);
    private final ArrayList<Marker> markers = new ArrayList<>();
    private final HashMap<Integer, Float> healthMap = new HashMap<>();
    private final Random random = new Random();

    @Generated
    public static DamageMarkersModule getInstance() {
        return instance;
    }

    public DamageMarkersModule() {
        addSettings(this.markerSize, this.baseScale, this.overshootAnimation, this.lifetime, this.moveSpeed, this.glow, this.glowSize, this.glowAlpha, this.checkYourself);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        synchronized (this.markers) {
            this.markers.clear();
            this.healthMap.clear();
        }
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1687 == null || mc.field_1724 == null) {
                return;
            }
            synchronized (this.markers) {
                for (class_746 class_746Var : mc.field_1687.method_18112()) {
                    if (class_746Var instanceof class_1309) {
                        class_1309 living = (class_1309) class_746Var;
                        if (!class_746Var.method_31481() && (this.checkYourself.getValue().booleanValue() || class_746Var != mc.field_1724)) {
                            if (mc.field_1724.method_5739(class_746Var) <= 10.0f) {
                                int id = class_746Var.method_5628();
                                float currentHealth = living.method_6032();
                                if (!this.healthMap.containsKey(Integer.valueOf(id))) {
                                    this.healthMap.put(Integer.valueOf(id), Float.valueOf(currentHealth));
                                } else {
                                    float lastHealth = this.healthMap.get(Integer.valueOf(id)).floatValue();
                                    this.healthMap.put(Integer.valueOf(id), Float.valueOf(currentHealth));
                                    if (lastHealth != currentHealth) {
                                        float delta = lastHealth - currentHealth;
                                        if (Math.abs(delta) >= 0.05f) {
                                            boolean isHeal = delta < 0.0f;
                                            String text = BigDecimal.valueOf(Math.abs(delta)).setScale(1, RoundingMode.HALF_UP).toPlainString();
                                            if (!text.equals("0.0")) {
                                                double px = class_746Var.method_23317() + (((double) (this.random.nextInt(5) - 2)) * 0.1d);
                                                double py = class_746Var.method_5829().field_1322 + ((class_746Var.method_5829().field_1325 - class_746Var.method_5829().field_1322) / 2.0d);
                                                double pz = class_746Var.method_23321() + (((double) (this.random.nextInt(5) - 2)) * 0.1d);
                                                this.markers.add(new Marker(class_746Var, text, px, py, pz, isHeal, Math.abs(delta)));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                this.markers.removeIf(m -> {
                    if (m.entity.method_31481()) {
                        return true;
                    }
                    long elapsed = System.currentTimeMillis() - m.createdTime;
                    return elapsed >= this.lifetime.getValue().longValue();
                });
            }
        }));
        EventListener renderEvent = Render3DEvent.getInstance().subscribe(new Listener(event2 -> {
            if (mc.field_1687 == null || mc.field_1724 == null) {
                return;
            }
            class_4184 camera = mc.method_1561().field_4686;
            class_4587 ms = event2.matrixStack();
            class_4597.class_4598 immediate = mc.method_22940().method_23000();
            synchronized (this.markers) {
                for (Marker marker : this.markers) {
                    long elapsed = System.currentTimeMillis() - marker.createdTime;
                    float progress = elapsed / this.lifetime.getValue().floatValue();
                    if (progress <= 1.0f) {
                        float scaleAnim = getScaleAnimation(progress);
                        float alphaAnim = getAlphaAnimation(progress);
                        float yOffset = easeOutQuad(progress) * this.moveSpeed.getValue().floatValue();
                        float size = this.markerSize.getValue().floatValue() * this.baseScale.getValue().floatValue() * scaleAnim;
                        double x = marker.posX - camera.method_19326().field_1352;
                        double y = (marker.posY - camera.method_19326().field_1351) + ((double) yOffset);
                        double z = marker.posZ - camera.method_19326().field_1350;
                        ms.method_22903();
                        ms.method_22904(x, y, z);
                        ms.method_22907(class_7833.field_40716.rotationDegrees(-camera.method_19330()));
                        float pitchSign = mc.field_1690.method_31044().method_31035() ? -1.0f : 1.0f;
                        ms.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329() * pitchSign));
                        ms.method_22905(-size, -size, size);
                        Matrix4f matrix = ms.method_23760().method_23761();
                        float tw = mc.field_1772.method_1727(marker.str) / 2.0f;
                        Objects.requireNonNull(mc.field_1772);
                        int yOff = -(9 - 1);
                        int color = getColor(marker);
                        int alpha = (int) (alphaAnim * 255.0f);
                        int finalColor = (alpha << 24) | (color & 16777215);
                        if (this.glow.getValue().booleanValue()) {
                            renderGlow(matrix, marker.str, yOff, color, alphaAnim);
                        }
                        mc.field_1772.method_27521(marker.str, -tw, yOff, finalColor, true, matrix, immediate, class_327.class_6415.field_33994, 0, 15728880);
                        ms.method_22909();
                    }
                }
            }
            immediate.method_22993();
        }));
        addEvents(updateEvent, renderEvent);
    }

    private void renderGlow(Matrix4f matrix, String text, int yOff, int color, float alphaAnim) {
        float textWidth = mc.field_1772.method_1727(text);
        Objects.requireNonNull(mc.field_1772);
        float centerY = yOff + (9.0f / 2.0f);
        float width = Math.max(textWidth + 28.0f, 36.0f) * this.glowSize.getValue().floatValue();
        float height = Math.max(9.0f + 22.0f, 30.0f) * this.glowSize.getValue().floatValue();
        int r = (color >> 16) & 255;
        int g = (color >> 8) & 255;
        int b = color & 255;
        float alpha = alphaAnim * this.glowAlpha.getValue().floatValue();
        if (alpha <= 0.01f) {
            return;
        }
        RenderSystem.setShader(class_10142.field_53880);
        RenderSystem.setShaderTexture(0, FileUtil.getImage("particles/glow"));
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE, GlStateManager.class_4535.ZERO, GlStateManager.class_4534.ONE);
        RenderSystem.disableCull();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
        putGlowQuad(buffer, matrix, 0.0f, centerY, width * 1.7f, height * 1.7f, r, g, b, (int) (alpha * 55.0f));
        putGlowQuad(buffer, matrix, 0.0f, centerY, width * 1.15f, height * 1.15f, r, g, b, (int) (alpha * 95.0f));
        putGlowQuad(buffer, matrix, 0.0f, centerY, width * 0.75f, height * 0.75f, Math.min(255, (int) (r * 1.25f)), Math.min(255, (int) (g * 1.25f)), Math.min(255, (int) (b * 1.25f)), (int) (alpha * 130.0f));
        class_286.method_43433(buffer.method_60800());
        RenderSystem.setShaderTexture(0, 0);
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
        RenderSystem.blendFunc(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE_MINUS_SRC_ALPHA);
        RenderSystem.enableCull();
    }

    private void putGlowQuad(class_287 buffer, Matrix4f matrix, float cx, float cy, float width, float height, int r, int g, int b, int a) {
        if (a <= 0) {
            return;
        }
        float hw = width / 2.0f;
        float hh = height / 2.0f;
        buffer.method_22918(matrix, cx - hw, cy + hh, 0.0f).method_22913(0.0f, 1.0f).method_1336(r, g, b, a);
        buffer.method_22918(matrix, cx + hw, cy + hh, 0.0f).method_22913(1.0f, 1.0f).method_1336(r, g, b, a);
        buffer.method_22918(matrix, cx + hw, cy - hh, 0.0f).method_22913(1.0f, 0.0f).method_1336(r, g, b, a);
        buffer.method_22918(matrix, cx - hw, cy - hh, 0.0f).method_22913(0.0f, 0.0f).method_1336(r, g, b, a);
    }

    private float getScaleAnimation(float progress) {
        if (progress >= 0.15f) {
            if (progress > 0.85f) {
                return 1.0f - (easeInQuad((progress - 0.85f) / 0.15f) * 0.3f);
            }
            return 1.0f;
        }
        float animProgress = progress / 0.15f;
        if (this.overshootAnimation.getValue().booleanValue()) {
            return easeOutBack(animProgress);
        }
        return easeOutQuad(animProgress);
    }

    private float getAlphaAnimation(float progress) {
        if (progress < 0.1f) {
            return easeOutQuad(progress / 0.1f);
        }
        if (progress > 0.75f) {
            return 1.0f - easeInQuad((progress - 0.75f) / 0.25f);
        }
        return 1.0f;
    }

    private float easeOutBack(float t) {
        float c3 = 1.70158f + 1.0f;
        return 1.0f + (c3 * ((float) Math.pow(t - 1.0f, 3.0d))) + (1.70158f * ((float) Math.pow(t - 1.0f, 2.0d)));
    }

    private float easeOutQuad(float t) {
        return 1.0f - ((1.0f - t) * (1.0f - t));
    }

    private float easeInQuad(float t) {
        return t * t;
    }

    private int getColor(Marker marker) {
        return marker.isHeal ? new Color(100, 255, 100).getRGB() : marker.damage >= 4.0f ? Color.RED.getRGB() : marker.damage >= 3.0f ? Color.YELLOW.getRGB() : Color.GREEN.getRGB();
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/DamageMarkersModule$Marker.class */
    private static class Marker {
        final class_1297 entity;
        final String str;
        final double posX;
        final double posY;
        final double posZ;
        final long createdTime = System.currentTimeMillis();
        final boolean isHeal;
        final float damage;

        Marker(class_1297 entity, String str, double posX, double posY, double posZ, boolean isHeal, float damage) {
            this.entity = entity;
            this.str = str;
            this.posX = posX;
            this.posY = posY;
            this.posZ = posZ;
            this.isHeal = isHeal;
            this.damage = damage;
        }
    }
}

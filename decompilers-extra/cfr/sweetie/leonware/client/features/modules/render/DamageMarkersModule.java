/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.GlStateManager$class_4534
 *  com.mojang.blaze3d.platform.GlStateManager$class_4535
 *  com.mojang.blaze3d.systems.RenderSystem
 *  lombok.Generated
 *  net.minecraft.class_10142
 *  net.minecraft.class_10156
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_286
 *  net.minecraft.class_287
 *  net.minecraft.class_289
 *  net.minecraft.class_290
 *  net.minecraft.class_293$class_5596
 *  net.minecraft.class_2960
 *  net.minecraft.class_327$class_6415
 *  net.minecraft.class_4184
 *  net.minecraft.class_4587
 *  net.minecraft.class_4597
 *  net.minecraft.class_4597$class_4598
 *  net.minecraft.class_7833
 *  net.minecraft.class_9801
 *  org.joml.Matrix4f
 */
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
import net.minecraft.class_10156;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_2960;
import net.minecraft.class_327;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_7833;
import net.minecraft.class_9801;
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

@ModuleRegister(name="Damage Markers", category=Category.RENDER)
public class DamageMarkersModule
extends Module {
    private static final DamageMarkersModule instance = new DamageMarkersModule();
    public final SliderSetting markerSize = new SliderSetting("\u0420\u0430\u0437\u043c\u0435\u0440").value(Float.valueOf(0.15f)).range(0.02f, 1.5f).step(0.01f);
    public final SliderSetting baseScale = new SliderSetting("\u0411\u0430\u0437\u043e\u0432\u044b\u0439 \u043c\u043d\u043e\u0436\u0438\u0442\u0435\u043b\u044c").value(Float.valueOf(0.25f)).range(0.05f, 1.0f).step(0.01f);
    public final BooleanSetting overshootAnimation = new BooleanSetting("Overshoot \u0430\u043d\u0438\u043c\u0430\u0446\u0438\u044f").value(false);
    public final SliderSetting lifetime = new SliderSetting("\u0412\u0440\u0435\u043c\u044f \u0436\u0438\u0437\u043d\u0438 (\u043c\u0441)").value(Float.valueOf(2000.0f)).range(500.0f, 5000.0f).step(100.0f);
    public final SliderSetting moveSpeed = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c \u0434\u0432\u0438\u0436\u0435\u043d\u0438\u044f").value(Float.valueOf(0.5f)).range(0.1f, 2.0f).step(0.1f);
    public final BooleanSetting glow = new BooleanSetting("\u0421\u0432\u0435\u0447\u0435\u043d\u0438\u0435").value(true);
    public final SliderSetting glowSize = new SliderSetting("\u0420\u0430\u0437\u043c\u0435\u0440 \u0441\u0432\u0435\u0447\u0435\u043d\u0438\u044f").value(Float.valueOf(1.0f)).range(0.3f, 3.0f).step(0.1f);
    public final SliderSetting glowAlpha = new SliderSetting("\u042f\u0440\u043a\u043e\u0441\u0442\u044c \u0441\u0432\u0435\u0447\u0435\u043d\u0438\u044f").value(Float.valueOf(0.65f)).range(0.1f, 1.0f).step(0.05f);
    public final BooleanSetting checkYourself = new BooleanSetting("\u041f\u043e\u043a\u0430\u0437\u044b\u0432\u0430\u0442\u044c \u0441\u0435\u0431\u044f").value(true);
    private final ArrayList<Marker> markers = new ArrayList();
    private final HashMap<Integer, Float> healthMap = new HashMap();
    private final Random random = new Random();

    public DamageMarkersModule() {
        this.addSettings(this.markerSize, this.baseScale, this.overshootAnimation, this.lifetime, this.moveSpeed, this.glow, this.glowSize, this.glowAlpha, this.checkYourself);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void onDisable() {
        ArrayList<Marker> arrayList = this.markers;
        synchronized (arrayList) {
            this.markers.clear();
            this.healthMap.clear();
        }
    }

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (DamageMarkersModule.mc.field_1687 == null || DamageMarkersModule.mc.field_1724 == null) {
                return;
            }
            ArrayList<Marker> arrayList = this.markers;
            synchronized (arrayList) {
                for (class_1297 entity : DamageMarkersModule.mc.field_1687.method_18112()) {
                    float delta;
                    if (!(entity instanceof class_1309)) continue;
                    class_1309 living = (class_1309)entity;
                    if (entity.method_31481() || !((Boolean)this.checkYourself.getValue()).booleanValue() && entity == DamageMarkersModule.mc.field_1724 || DamageMarkersModule.mc.field_1724.method_5739(entity) > 10.0f) continue;
                    int id = entity.method_5628();
                    float currentHealth = living.method_6032();
                    if (!this.healthMap.containsKey(id)) {
                        this.healthMap.put(id, Float.valueOf(currentHealth));
                        continue;
                    }
                    float lastHealth = this.healthMap.get(id).floatValue();
                    this.healthMap.put(id, Float.valueOf(currentHealth));
                    if (lastHealth == currentHealth || Math.abs(delta = lastHealth - currentHealth) < 0.05f) continue;
                    boolean isHeal = delta < 0.0f;
                    String text = BigDecimal.valueOf(Math.abs(delta)).setScale(1, RoundingMode.HALF_UP).toPlainString();
                    if (text.equals("0.0")) continue;
                    double px = entity.method_23317() + (double)(this.random.nextInt(5) - 2) * 0.1;
                    double py = entity.method_5829().field_1322 + (entity.method_5829().field_1325 - entity.method_5829().field_1322) / 2.0;
                    double pz = entity.method_23321() + (double)(this.random.nextInt(5) - 2) * 0.1;
                    this.markers.add(new Marker(entity, text, px, py, pz, isHeal, Math.abs(delta)));
                }
                this.markers.removeIf(m -> {
                    if (m.entity.method_31481()) {
                        return true;
                    }
                    long elapsed = System.currentTimeMillis() - m.createdTime;
                    return elapsed >= ((Float)this.lifetime.getValue()).longValue();
                });
            }
        }));
        EventListener renderEvent = Render3DEvent.getInstance().subscribe(new Listener<Render3DEvent.Render3DEventData>(event -> {
            if (DamageMarkersModule.mc.field_1687 == null || DamageMarkersModule.mc.field_1724 == null) {
                return;
            }
            class_4184 camera = DamageMarkersModule.mc.method_1561().field_4686;
            class_4587 ms = event.matrixStack();
            class_4597.class_4598 immediate = mc.method_22940().method_23000();
            ArrayList<Marker> arrayList = this.markers;
            synchronized (arrayList) {
                for (Marker marker : this.markers) {
                    long elapsed = System.currentTimeMillis() - marker.createdTime;
                    float progress = (float)elapsed / ((Float)this.lifetime.getValue()).floatValue();
                    if (progress > 1.0f) continue;
                    float scaleAnim = this.getScaleAnimation(progress);
                    float alphaAnim = this.getAlphaAnimation(progress);
                    float yOffset = this.easeOutQuad(progress) * ((Float)this.moveSpeed.getValue()).floatValue();
                    float size = ((Float)this.markerSize.getValue()).floatValue() * ((Float)this.baseScale.getValue()).floatValue() * scaleAnim;
                    double x = marker.posX - camera.method_19326().field_1352;
                    double y = marker.posY - camera.method_19326().field_1351 + (double)yOffset;
                    double z = marker.posZ - camera.method_19326().field_1350;
                    ms.method_22903();
                    ms.method_22904(x, y, z);
                    ms.method_22907(class_7833.field_40716.rotationDegrees(-camera.method_19330()));
                    float pitchSign = DamageMarkersModule.mc.field_1690.method_31044().method_31035() ? -1.0f : 1.0f;
                    ms.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329() * pitchSign));
                    ms.method_22905(-size, -size, size);
                    Matrix4f matrix = ms.method_23760().method_23761();
                    float tw = (float)DamageMarkersModule.mc.field_1772.method_1727(marker.str) / 2.0f;
                    Objects.requireNonNull(DamageMarkersModule.mc.field_1772);
                    int yOff = -(9 - 1);
                    int color = this.getColor(marker);
                    int alpha = (int)(alphaAnim * 255.0f);
                    int finalColor = alpha << 24 | color & 0xFFFFFF;
                    if (((Boolean)this.glow.getValue()).booleanValue()) {
                        this.renderGlow(matrix, marker.str, yOff, color, alphaAnim);
                    }
                    DamageMarkersModule.mc.field_1772.method_27521(marker.str, -tw, (float)yOff, finalColor, true, matrix, (class_4597)immediate, class_327.class_6415.field_33994, 0, 0xF000F0);
                    ms.method_22909();
                }
            }
            immediate.method_22993();
        }));
        this.addEvents(updateEvent, renderEvent);
    }

    private void renderGlow(Matrix4f matrix, String text, int yOff, int color, float alphaAnim) {
        float textWidth = DamageMarkersModule.mc.field_1772.method_1727(text);
        Objects.requireNonNull(DamageMarkersModule.mc.field_1772);
        float textHeight = 9.0f;
        float centerX = 0.0f;
        float centerY = (float)yOff + textHeight / 2.0f;
        float width = Math.max(textWidth + 28.0f, 36.0f) * ((Float)this.glowSize.getValue()).floatValue();
        float height = Math.max(textHeight + 22.0f, 30.0f) * ((Float)this.glowSize.getValue()).floatValue();
        int r = color >> 16 & 0xFF;
        int g = color >> 8 & 0xFF;
        int b = color & 0xFF;
        float alpha = alphaAnim * ((Float)this.glowAlpha.getValue()).floatValue();
        if (alpha <= 0.01f) {
            return;
        }
        RenderSystem.setShader((class_10156)class_10142.field_53880);
        RenderSystem.setShaderTexture((int)0, (class_2960)FileUtil.getImage("particles/glow"));
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate((GlStateManager.class_4535)GlStateManager.class_4535.SRC_ALPHA, (GlStateManager.class_4534)GlStateManager.class_4534.ONE, (GlStateManager.class_4535)GlStateManager.class_4535.ZERO, (GlStateManager.class_4534)GlStateManager.class_4534.ONE);
        RenderSystem.disableCull();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask((boolean)false);
        class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
        this.putGlowQuad(buffer, matrix, centerX, centerY, width * 1.7f, height * 1.7f, r, g, b, (int)(alpha * 55.0f));
        this.putGlowQuad(buffer, matrix, centerX, centerY, width * 1.15f, height * 1.15f, r, g, b, (int)(alpha * 95.0f));
        this.putGlowQuad(buffer, matrix, centerX, centerY, width * 0.75f, height * 0.75f, Math.min(255, (int)((float)r * 1.25f)), Math.min(255, (int)((float)g * 1.25f)), Math.min(255, (int)((float)b * 1.25f)), (int)(alpha * 130.0f));
        class_286.method_43433((class_9801)buffer.method_60800());
        RenderSystem.setShaderTexture((int)0, (int)0);
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask((boolean)true);
        RenderSystem.disableBlend();
        RenderSystem.blendFunc((GlStateManager.class_4535)GlStateManager.class_4535.SRC_ALPHA, (GlStateManager.class_4534)GlStateManager.class_4534.ONE_MINUS_SRC_ALPHA);
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
        if (progress < 0.15f) {
            float animProgress = progress / 0.15f;
            if (((Boolean)this.overshootAnimation.getValue()).booleanValue()) {
                return this.easeOutBack(animProgress);
            }
            return this.easeOutQuad(animProgress);
        }
        if (progress > 0.85f) {
            return 1.0f - this.easeInQuad((progress - 0.85f) / 0.15f) * 0.3f;
        }
        return 1.0f;
    }

    private float getAlphaAnimation(float progress) {
        if (progress < 0.1f) {
            return this.easeOutQuad(progress / 0.1f);
        }
        if (progress > 0.75f) {
            return 1.0f - this.easeInQuad((progress - 0.75f) / 0.25f);
        }
        return 1.0f;
    }

    private float easeOutBack(float t) {
        float c1 = 1.70158f;
        float c3 = c1 + 1.0f;
        return 1.0f + c3 * (float)Math.pow(t - 1.0f, 3.0) + c1 * (float)Math.pow(t - 1.0f, 2.0);
    }

    private float easeOutQuad(float t) {
        return 1.0f - (1.0f - t) * (1.0f - t);
    }

    private float easeInQuad(float t) {
        return t * t;
    }

    private int getColor(Marker marker) {
        if (marker.isHeal) {
            return new Color(100, 255, 100).getRGB();
        }
        if (marker.damage >= 4.0f) {
            return Color.RED.getRGB();
        }
        if (marker.damage >= 3.0f) {
            return Color.YELLOW.getRGB();
        }
        return Color.GREEN.getRGB();
    }

    @Generated
    public static DamageMarkersModule getInstance() {
        return instance;
    }

    private static class Marker {
        final class_1297 entity;
        final String str;
        final double posX;
        final double posY;
        final double posZ;
        final long createdTime;
        final boolean isHeal;
        final float damage;

        Marker(class_1297 entity, String str, double posX, double posY, double posZ, boolean isHeal, float damage) {
            this.entity = entity;
            this.str = str;
            this.posX = posX;
            this.posY = posY;
            this.posZ = posZ;
            this.isHeal = isHeal;
            this.createdTime = System.currentTimeMillis();
            this.damage = damage;
        }
    }
}


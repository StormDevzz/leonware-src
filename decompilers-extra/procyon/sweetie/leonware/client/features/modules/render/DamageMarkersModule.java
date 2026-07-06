// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import java.awt.Color;
import net.minecraft.class_287;
import net.minecraft.class_286;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_289;
import com.mojang.blaze3d.platform.GlStateManager;
import sweetie.leonware.api.system.files.FileUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_10142;
import org.joml.Matrix4f;
import net.minecraft.class_4587;
import net.minecraft.class_4184;
import java.util.Iterator;
import sweetie.leonware.api.event.EventListener;
import net.minecraft.class_4597;
import net.minecraft.class_327;
import java.util.Objects;
import net.minecraft.class_7833;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.event.Listener;
import java.math.RoundingMode;
import java.math.BigDecimal;
import net.minecraft.class_1309;
import net.minecraft.class_1297;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.setting.Setting;
import java.util.Random;
import java.util.HashMap;
import java.util.ArrayList;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Damage Markers", category = Category.RENDER)
public class DamageMarkersModule extends Module
{
    private static final DamageMarkersModule instance;
    public final SliderSetting markerSize;
    public final SliderSetting baseScale;
    public final BooleanSetting overshootAnimation;
    public final SliderSetting lifetime;
    public final SliderSetting moveSpeed;
    public final BooleanSetting glow;
    public final SliderSetting glowSize;
    public final SliderSetting glowAlpha;
    public final BooleanSetting checkYourself;
    private final ArrayList<Marker> markers;
    private final HashMap<Integer, Float> healthMap;
    private final Random random;
    
    public DamageMarkersModule() {
        this.markerSize = new SliderSetting("\u0420\u0430\u0437\u043c\u0435\u0440").value(0.15f).range(0.02f, 1.5f).step(0.01f);
        this.baseScale = new SliderSetting("\u0411\u0430\u0437\u043e\u0432\u044b\u0439 \u043c\u043d\u043e\u0436\u0438\u0442\u0435\u043b\u044c").value(0.25f).range(0.05f, 1.0f).step(0.01f);
        this.overshootAnimation = new BooleanSetting("Overshoot \u0430\u043d\u0438\u043c\u0430\u0446\u0438\u044f").value(false);
        this.lifetime = new SliderSetting("\u0412\u0440\u0435\u043c\u044f \u0436\u0438\u0437\u043d\u0438 (\u043c\u0441)").value(2000.0f).range(500.0f, 5000.0f).step(100.0f);
        this.moveSpeed = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c \u0434\u0432\u0438\u0436\u0435\u043d\u0438\u044f").value(0.5f).range(0.1f, 2.0f).step(0.1f);
        this.glow = new BooleanSetting("\u0421\u0432\u0435\u0447\u0435\u043d\u0438\u0435").value(true);
        this.glowSize = new SliderSetting("\u0420\u0430\u0437\u043c\u0435\u0440 \u0441\u0432\u0435\u0447\u0435\u043d\u0438\u044f").value(1.0f).range(0.3f, 3.0f).step(0.1f);
        this.glowAlpha = new SliderSetting("\u042f\u0440\u043a\u043e\u0441\u0442\u044c \u0441\u0432\u0435\u0447\u0435\u043d\u0438\u044f").value(0.65f).range(0.1f, 1.0f).step(0.05f);
        this.checkYourself = new BooleanSetting("\u041f\u043e\u043a\u0430\u0437\u044b\u0432\u0430\u0442\u044c \u0441\u0435\u0431\u044f").value(true);
        this.markers = new ArrayList<Marker>();
        this.healthMap = new HashMap<Integer, Float>();
        this.random = new Random();
        this.addSettings(this.markerSize, this.baseScale, this.overshootAnimation, this.lifetime, this.moveSpeed, this.glow, this.glowSize, this.glowAlpha, this.checkYourself);
    }
    
    @Override
    public void onDisable() {
        synchronized (this.markers) {
            this.markers.clear();
            this.healthMap.clear();
        }
    }
    
    @Override
    public void onEvent() {
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (DamageMarkersModule.mc.field_1687 == null || DamageMarkersModule.mc.field_1724 == null) {
                return;
            }
            else {
                synchronized (this.markers) {
                    DamageMarkersModule.mc.field_1687.method_18112().iterator();
                    final Iterator iterator;
                    while (iterator.hasNext()) {
                        final class_1297 entity = iterator.next();
                        if (entity instanceof final class_1309 living) {
                            if (entity.method_31481()) {
                                continue;
                            }
                            else if (!this.checkYourself.getValue() && entity == DamageMarkersModule.mc.field_1724) {
                                continue;
                            }
                            else if (DamageMarkersModule.mc.field_1724.method_5739(entity) > 10.0f) {
                                continue;
                            }
                            else {
                                final int id = entity.method_5628();
                                final float currentHealth = living.method_6032();
                                if (!this.healthMap.containsKey(id)) {
                                    this.healthMap.put(id, currentHealth);
                                }
                                else {
                                    final float lastHealth = this.healthMap.get(id);
                                    this.healthMap.put(id, currentHealth);
                                    if (lastHealth == currentHealth) {
                                        continue;
                                    }
                                    else {
                                        final float delta = lastHealth - currentHealth;
                                        if (Math.abs(delta) < 0.05f) {
                                            continue;
                                        }
                                        else {
                                            final boolean isHeal = delta < 0.0f;
                                            final String text = BigDecimal.valueOf(Math.abs(delta)).setScale(1, RoundingMode.HALF_UP).toPlainString();
                                            if (text.equals("0.0")) {
                                                continue;
                                            }
                                            else {
                                                final double px = entity.method_23317() + (this.random.nextInt(5) - 2) * 0.1;
                                                final double py = entity.method_5829().field_1322 + (entity.method_5829().field_1325 - entity.method_5829().field_1322) / 2.0;
                                                final double pz = entity.method_23321() + (this.random.nextInt(5) - 2) * 0.1;
                                                this.markers.add(new Marker(entity, text, px, py, pz, isHeal, Math.abs(delta)));
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
                        else {
                            final long elapsed = System.currentTimeMillis() - m.createdTime;
                            return elapsed >= this.lifetime.getValue().longValue();
                        }
                    });
                }
                return;
            }
        }));
        final EventListener renderEvent = Render3DEvent.getInstance().subscribe(new Listener<Render3DEvent.Render3DEventData>(event -> {
            if (DamageMarkersModule.mc.field_1687 == null || DamageMarkersModule.mc.field_1724 == null) {
                return;
            }
            else {
                final class_4184 camera = DamageMarkersModule.mc.method_1561().field_4686;
                final class_4587 ms = event.matrixStack();
                final class_4597.class_4598 immediate = DamageMarkersModule.mc.method_22940().method_23000();
                synchronized (this.markers) {
                    this.markers.iterator();
                    final Iterator iterator2;
                    while (iterator2.hasNext()) {
                        final Marker marker = iterator2.next();
                        final long elapsed2 = System.currentTimeMillis() - marker.createdTime;
                        final float progress = elapsed2 / this.lifetime.getValue();
                        if (progress > 1.0f) {
                            continue;
                        }
                        else {
                            final float scaleAnim = this.getScaleAnimation(progress);
                            final float alphaAnim = this.getAlphaAnimation(progress);
                            final float yOffset = this.easeOutQuad(progress) * this.moveSpeed.getValue();
                            final float size = this.markerSize.getValue() * this.baseScale.getValue() * scaleAnim;
                            final double x = marker.posX - camera.method_19326().field_1352;
                            final double y = marker.posY - camera.method_19326().field_1351 + yOffset;
                            final double z = marker.posZ - camera.method_19326().field_1350;
                            ms.method_22903();
                            ms.method_22904(x, y, z);
                            ms.method_22907(class_7833.field_40716.rotationDegrees(-camera.method_19330()));
                            final float pitchSign = DamageMarkersModule.mc.field_1690.method_31044().method_31035() ? -1.0f : 1.0f;
                            ms.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329() * pitchSign));
                            ms.method_22905(-size, -size, size);
                            final Matrix4f matrix = ms.method_23760().method_23761();
                            final float tw = DamageMarkersModule.mc.field_1772.method_1727(marker.str) / 2.0f;
                            Objects.requireNonNull(DamageMarkersModule.mc.field_1772);
                            final int yOff = -(9 - 1);
                            final int color = this.getColor(marker);
                            final int alpha = (int)(alphaAnim * 255.0f);
                            final int finalColor = alpha << 24 | (color & 0xFFFFFF);
                            if (this.glow.getValue()) {
                                this.renderGlow(matrix, marker.str, yOff, color, alphaAnim);
                            }
                            DamageMarkersModule.mc.field_1772.method_27521(marker.str, -tw, (float)yOff, finalColor, true, matrix, (class_4597)immediate, class_327.class_6415.field_33994, 0, 15728880);
                            ms.method_22909();
                        }
                    }
                }
                immediate.method_22993();
                return;
            }
        }));
        this.addEvents(updateEvent, renderEvent);
    }
    
    private void renderGlow(final Matrix4f matrix, final String text, final int yOff, final int color, final float alphaAnim) {
        final float textWidth = (float)DamageMarkersModule.mc.field_1772.method_1727(text);
        Objects.requireNonNull(DamageMarkersModule.mc.field_1772);
        final float textHeight = 9.0f;
        final float centerX = 0.0f;
        final float centerY = yOff + textHeight / 2.0f;
        final float width = Math.max(textWidth + 28.0f, 36.0f) * this.glowSize.getValue();
        final float height = Math.max(textHeight + 22.0f, 30.0f) * this.glowSize.getValue();
        final int r = color >> 16 & 0xFF;
        final int g = color >> 8 & 0xFF;
        final int b = color & 0xFF;
        final float alpha = alphaAnim * this.glowAlpha.getValue();
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
        final class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
        this.putGlowQuad(buffer, matrix, centerX, centerY, width * 1.7f, height * 1.7f, r, g, b, (int)(alpha * 55.0f));
        this.putGlowQuad(buffer, matrix, centerX, centerY, width * 1.15f, height * 1.15f, r, g, b, (int)(alpha * 95.0f));
        this.putGlowQuad(buffer, matrix, centerX, centerY, width * 0.75f, height * 0.75f, Math.min(255, (int)(r * 1.25f)), Math.min(255, (int)(g * 1.25f)), Math.min(255, (int)(b * 1.25f)), (int)(alpha * 130.0f));
        class_286.method_43433(buffer.method_60800());
        RenderSystem.setShaderTexture(0, 0);
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
        RenderSystem.blendFunc(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE_MINUS_SRC_ALPHA);
        RenderSystem.enableCull();
    }
    
    private void putGlowQuad(final class_287 buffer, final Matrix4f matrix, final float cx, final float cy, final float width, final float height, final int r, final int g, final int b, final int a) {
        if (a <= 0) {
            return;
        }
        final float hw = width / 2.0f;
        final float hh = height / 2.0f;
        buffer.method_22918(matrix, cx - hw, cy + hh, 0.0f).method_22913(0.0f, 1.0f).method_1336(r, g, b, a);
        buffer.method_22918(matrix, cx + hw, cy + hh, 0.0f).method_22913(1.0f, 1.0f).method_1336(r, g, b, a);
        buffer.method_22918(matrix, cx + hw, cy - hh, 0.0f).method_22913(1.0f, 0.0f).method_1336(r, g, b, a);
        buffer.method_22918(matrix, cx - hw, cy - hh, 0.0f).method_22913(0.0f, 0.0f).method_1336(r, g, b, a);
    }
    
    private float getScaleAnimation(final float progress) {
        if (progress < 0.15f) {
            final float animProgress = progress / 0.15f;
            if (this.overshootAnimation.getValue()) {
                return this.easeOutBack(animProgress);
            }
            return this.easeOutQuad(animProgress);
        }
        else {
            if (progress > 0.85f) {
                return 1.0f - this.easeInQuad((progress - 0.85f) / 0.15f) * 0.3f;
            }
            return 1.0f;
        }
    }
    
    private float getAlphaAnimation(final float progress) {
        if (progress < 0.1f) {
            return this.easeOutQuad(progress / 0.1f);
        }
        if (progress > 0.75f) {
            return 1.0f - this.easeInQuad((progress - 0.75f) / 0.25f);
        }
        return 1.0f;
    }
    
    private float easeOutBack(final float t) {
        final float c1 = 1.70158f;
        final float c2 = c1 + 1.0f;
        return 1.0f + c2 * (float)Math.pow(t - 1.0f, 3.0) + c1 * (float)Math.pow(t - 1.0f, 2.0);
    }
    
    private float easeOutQuad(final float t) {
        return 1.0f - (1.0f - t) * (1.0f - t);
    }
    
    private float easeInQuad(final float t) {
        return t * t;
    }
    
    private int getColor(final Marker marker) {
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
        return DamageMarkersModule.instance;
    }
    
    static {
        instance = new DamageMarkersModule();
    }
    
    private static class Marker
    {
        final class_1297 entity;
        final String str;
        final double posX;
        final double posY;
        final double posZ;
        final long createdTime;
        final boolean isHeal;
        final float damage;
        
        Marker(final class_1297 entity, final String str, final double posX, final double posY, final double posZ, final boolean isHeal, final float damage) {
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

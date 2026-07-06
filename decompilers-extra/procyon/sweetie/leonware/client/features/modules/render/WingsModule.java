// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import java.awt.Color;
import sweetie.leonware.api.utils.color.UIColors;
import org.lwjgl.opengl.GL11;
import net.minecraft.class_287;
import org.joml.Matrix4f;
import net.minecraft.class_286;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_289;
import net.minecraft.class_7833;
import net.minecraft.class_3532;
import net.minecraft.class_1802;
import net.minecraft.class_1304;
import java.util.Iterator;
import net.minecraft.class_243;
import net.minecraft.class_4587;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_10142;
import com.mojang.blaze3d.systems.RenderSystem;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Wings", category = Category.RENDER)
public class WingsModule extends Module
{
    private static final WingsModule instance;
    private static final float DEFAULT_SPREAD = 8.0f;
    private static final int DEFAULT_ALPHA = 220;
    private static final WingPoint[] SHAPE;
    public final BooleanSetting self;
    public final BooleanSetting players;
    public final BooleanSetting depthTest;
    public final SliderSetting size;
    private float selfBodyYaw;
    private boolean selfBodyYawInitialized;
    
    public WingsModule() {
        this.self = new BooleanSetting("\u041d\u0430 \u0441\u0435\u0431\u044f").value(true);
        this.players = new BooleanSetting("\u041d\u0430 \u0438\u0433\u0440\u043e\u043a\u043e\u0432").value(false);
        this.depthTest = new BooleanSetting("\u041d\u0435 \u0447\u0435\u0440\u0435\u0437 \u0442\u0435\u043b\u043e").value(true);
        this.size = new SliderSetting("\u0420\u0430\u0437\u043c\u0435\u0440").value(1.0f).range(0.75f, 1.35f).step(0.05f);
        this.addSettings(this.self, this.players, this.depthTest, this.size);
    }
    
    @Override
    public void onDisable() {
        this.selfBodyYawInitialized = false;
    }
    
    @Override
    public void onEvent() {
        final EventListener renderEvent = Render3DEvent.getInstance().subscribe(new Listener<Render3DEvent.Render3DEventData>(event -> {
            if (WingsModule.mc.field_1724 == null || WingsModule.mc.field_1687 == null || WingsModule.mc.field_1773 == null) {
                return;
            }
            else {
                final class_4587 stack = event.matrixStack();
                final float tickDelta = WingsModule.mc.method_61966().method_60637(true);
                final class_243 camera = WingsModule.mc.field_1773.method_19418().method_19326();
                stack.method_22903();
                RenderSystem.enableBlend();
                RenderSystem.disableCull();
                if (this.depthTest.getValue()) {
                    RenderSystem.enableDepthTest();
                    RenderSystem.depthMask(false);
                }
                else {
                    RenderSystem.disableDepthTest();
                    RenderSystem.depthMask(false);
                }
                RenderSystem.setShader(class_10142.field_53876);
                if (this.self.getValue() && !WingsModule.mc.field_1690.method_31044().method_31034() && WingsModule.mc.field_1724.method_5805() && !this.hasElytra((class_1657)WingsModule.mc.field_1724)) {
                    try {
                        this.renderWings(stack, (class_1657)WingsModule.mc.field_1724, tickDelta, camera);
                    }
                    catch (final Exception ex) {}
                }
                if (this.players.getValue()) {
                    WingsModule.mc.field_1687.method_18112().iterator();
                    final Iterator iterator;
                    while (iterator.hasNext()) {
                        final class_1297 entity = iterator.next();
                        if (entity instanceof final class_1657 player) {
                            if (player == WingsModule.mc.field_1724) {
                                continue;
                            }
                            else if (player.method_5805()) {
                                if (this.hasElytra(player)) {
                                    continue;
                                }
                                else {
                                    try {
                                        this.renderWings(stack, player, tickDelta, camera);
                                    }
                                    catch (final Exception ex2) {}
                                }
                            }
                            else {
                                continue;
                            }
                        }
                    }
                }
                RenderSystem.depthMask(true);
                RenderSystem.enableDepthTest();
                RenderSystem.enableCull();
                RenderSystem.disableBlend();
                RenderSystem.blendFuncSeparate(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE_MINUS_SRC_ALPHA, GlStateManager.class_4535.ONE, GlStateManager.class_4534.ZERO);
                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                stack.method_22909();
                return;
            }
        }));
        this.addEvents(renderEvent);
    }
    
    private boolean hasElytra(final class_1657 player) {
        return player.method_6118(class_1304.field_6174).method_31574(class_1802.field_8833);
    }
    
    private void renderWings(final class_4587 stack, final class_1657 player, final float tickDelta, final class_243 camera) {
        final double x = class_3532.method_16436((double)tickDelta, player.field_6014, player.method_23317()) - camera.field_1352;
        final double y = class_3532.method_16436((double)tickDelta, player.field_6036, player.method_23318()) - camera.field_1351;
        final double z = class_3532.method_16436((double)tickDelta, player.field_5969, player.method_23321()) - camera.field_1350;
        final float bodyYaw = this.resolveBodyYaw(player, tickDelta);
        final float move = class_3532.method_15363(player.field_42108.method_48570(tickDelta), 0.0f, 1.0f);
        final WingPose pose = this.resolvePose(player, tickDelta);
        if (pose == null) {
            return;
        }
        final float flap = (float)Math.sin((player.field_6012 + tickDelta) * pose.flapSpeed) * pose.flapAmplitude;
        final float open = (8.0f + flap + move * pose.motionSpreadBoost) * pose.openMultiplier;
        final float wingScale = this.size.getValue() * pose.scaleMultiplier;
        final int baseColor = this.resolveBaseColor();
        final int glowColor = this.resolveGlowColor(baseColor);
        final int coreColor = this.resolveCoreColor(baseColor);
        stack.method_22903();
        stack.method_22904(x, y, z);
        stack.method_22907(class_7833.field_40716.rotationDegrees(180.0f - bodyYaw));
        if (pose.preTranslateY != 0.0f || pose.preTranslateZ != 0.0f) {
            stack.method_46416(0.0f, pose.preTranslateY, pose.preTranslateZ);
        }
        if (pose.pitchRotation != 0.0f) {
            stack.method_22907(class_7833.field_40714.rotationDegrees(pose.pitchRotation));
        }
        if (pose.rollRotation != 0.0f) {
            stack.method_22907(class_7833.field_40718.rotationDegrees(pose.rollRotation));
        }
        stack.method_46416(0.0f, pose.anchorY, pose.anchorZ);
        stack.method_22905(wingScale, wingScale, wingScale);
        this.renderWingSide(stack, -1.0f, open, baseColor, glowColor, coreColor, pose);
        this.renderWingSide(stack, 1.0f, open, baseColor, glowColor, coreColor, pose);
        stack.method_22909();
    }
    
    private void renderWingSide(final class_4587 stack, final float side, final float open, final int baseColor, final int glowColor, final int coreColor, final WingPose pose) {
        stack.method_22903();
        stack.method_46416(side * pose.sideOffset, pose.sideYOffset, pose.sideZOffset);
        stack.method_22907(class_7833.field_40716.rotationDegrees(side * open));
        stack.method_22907(class_7833.field_40718.rotationDegrees(side * pose.sideRoll));
        stack.method_22907(class_7833.field_40714.rotationDegrees(pose.sidePitch));
        RenderSystem.blendFunc(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE);
        this.drawWingLayer(stack, side, 1.22f, setAlpha(glowColor, 48), setAlpha(glowColor, 0));
        this.drawWingLayer(stack, side, 0.84f, setAlpha(coreColor, 57), setAlpha(coreColor, 0));
        RenderSystem.blendFunc(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE_MINUS_SRC_ALPHA);
        this.drawWingLayer(stack, side, 1.0f, setAlpha(baseColor, 220), setAlpha(baseColor, 10));
        RenderSystem.blendFunc(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE);
        this.drawWingOutline(stack, side, 1.0f, setAlpha(baseColor, 136));
        this.drawWingRibs(stack, side, 0.96f, setAlpha(glowColor, 44));
        stack.method_22909();
    }
    
    private void drawWingLayer(final class_4587 stack, final float side, final float scale, final int rootColor, final int edgeColor) {
        final Matrix4f matrix = stack.method_23760().method_23761();
        final class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27379, class_290.field_1576);
        for (int i = 0; i < WingsModule.SHAPE.length; ++i) {
            final WingPoint cur = WingsModule.SHAPE[i];
            final WingPoint next = WingsModule.SHAPE[(i + 1) % WingsModule.SHAPE.length];
            this.vertex(buffer, matrix, 0.0f, 0.0f, 0.0f, rootColor);
            this.vertex(buffer, matrix, side * cur.x * scale, cur.y * scale, 0.0f, this.applyPointAlpha(edgeColor, cur.alphaMul));
            this.vertex(buffer, matrix, side * next.x * scale, next.y * scale, 0.0f, this.applyPointAlpha(edgeColor, next.alphaMul));
        }
        class_286.method_43433(buffer.method_60800());
    }
    
    private void drawWingOutline(final class_4587 stack, final float side, final float scale, final int color) {
        final Matrix4f matrix = stack.method_23760().method_23761();
        RenderSystem.lineWidth(1.35f);
        GL11.glEnable(2848);
        final class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_29345, class_290.field_1576);
        for (final WingPoint point : WingsModule.SHAPE) {
            this.vertex(buffer, matrix, side * point.x * scale, point.y * scale, 0.0f, color);
        }
        this.vertex(buffer, matrix, side * WingsModule.SHAPE[0].x * scale, WingsModule.SHAPE[0].y * scale, 0.0f, color);
        class_286.method_43433(buffer.method_60800());
        GL11.glDisable(2848);
    }
    
    private void drawWingRibs(final class_4587 stack, final float side, final float scale, final int color) {
        final Matrix4f matrix = stack.method_23760().method_23761();
        final int[] ribIndices = { 2, 4, 7, 9, 11 };
        RenderSystem.lineWidth(0.9f);
        final class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27377, class_290.field_1576);
        for (final int idx : ribIndices) {
            final WingPoint point = WingsModule.SHAPE[idx];
            this.vertex(buffer, matrix, 0.0f, 0.0f, 0.0f, setAlpha(color, Math.max(8, (int)(alpha(color) * 0.75f))));
            this.vertex(buffer, matrix, side * point.x * scale, point.y * scale, 0.0f, this.applyPointAlpha(color, point.alphaMul));
        }
        class_286.method_43433(buffer.method_60800());
    }
    
    private int applyPointAlpha(final int color, final float multiplier) {
        return setAlpha(color, Math.max(0, Math.min(255, (int)(alpha(color) * multiplier))));
    }
    
    private static int setAlpha(final int color, final int a) {
        return class_3532.method_15340(a, 0, 255) << 24 | (color & 0xFFFFFF);
    }
    
    private static int alpha(final int color) {
        return color >> 24 & 0xFF;
    }
    
    private static int red(final int color) {
        return color >> 16 & 0xFF;
    }
    
    private static int green(final int color) {
        return color >> 8 & 0xFF;
    }
    
    private static int blue(final int color) {
        return color & 0xFF;
    }
    
    private static int getColor(final int r, final int g, final int b, final int a) {
        return a << 24 | r << 16 | g << 8 | b;
    }
    
    private void vertex(final class_287 buffer, final Matrix4f matrix, final float x, final float y, final float z, final int color) {
        buffer.method_22918(matrix, x, y, z).method_22915(red(color) / 255.0f, green(color) / 255.0f, blue(color) / 255.0f, alpha(color) / 255.0f);
    }
    
    private int resolveBaseColor() {
        final Color c = UIColors.gradient(0);
        return getColor(c.getRed(), c.getGreen(), c.getBlue(), 255);
    }
    
    private int resolveGlowColor(final int base) {
        return interpolateColor(base, getColor(255, 255, 255, 255), 0.28f);
    }
    
    private int resolveCoreColor(final int base) {
        return interpolateColor(base, getColor(255, 255, 255, 255), 0.55f);
    }
    
    private static int interpolateColor(final int a, final int b, final float t) {
        final int ar = red(a);
        final int ag = green(a);
        final int ab = blue(a);
        final int aa = alpha(a);
        final int br = red(b);
        final int bg = green(b);
        final int bb = blue(b);
        final int ba = alpha(b);
        return getColor((int)(ar + (br - ar) * t), (int)(ag + (bg - ag) * t), (int)(ab + (bb - ab) * t), (int)(aa + (ba - aa) * t));
    }
    
    private float resolveBodyYaw(final class_1657 player, final float tickDelta) {
        final float target = class_3532.method_17821(tickDelta, player.field_6220, player.field_6283);
        if (player != WingsModule.mc.field_1724) {
            return target;
        }
        if (!this.selfBodyYawInitialized || player.field_6012 < 2) {
            this.selfBodyYaw = target;
            this.selfBodyYawInitialized = true;
            return this.selfBodyYaw;
        }
        return this.selfBodyYaw = approachDegrees(this.selfBodyYaw, target, 14.0f);
    }
    
    private static float approachDegrees(final float current, final float target, final float maxDelta) {
        float delta = class_3532.method_15393(target - current);
        delta = class_3532.method_15363(delta, -maxDelta, maxDelta);
        return current + delta;
    }
    
    private WingPose resolvePose(final class_1657 player, final float tickDelta) {
        final float pitch = class_3532.method_16439(tickDelta, player.field_6004, player.method_36455());
        if (player.method_6128()) {
            final float flightTicks = player.method_6003() + tickDelta;
            final float flightProgress = class_3532.method_15363(flightTicks * flightTicks / 100.0f, 0.0f, 1.0f);
            final float pitchRotation = flightProgress * (-90.0f - pitch);
            return new WingPose(0.34f, 0.46f, 0.0f, 0.0f, pitchRotation, 0.0f, 0.76f, 0.92f, 0.1f, 0.58f, 0.05f, 0.0f, 0.06f, -5.0f, -2.0f, 0.13f);
        }
        if (player.method_5799()) {
            return null;
        }
        if (player.method_5715()) {
            return new WingPose(0.0f, 0.0f, 0.96f, 0.1f, 18.0f, 0.0f, 1.0f, 1.0f, 0.18f, 4.5f, 0.06f, 0.0f, 0.02f, -11.0f, -4.0f, 0.12f);
        }
        return new WingPose(0.0f, 0.0f, 1.38f, 0.1f, 0.0f, 0.0f, 1.0f, 1.0f, 0.18f, 4.5f, 0.06f, 0.0f, 0.02f, -11.0f, -4.0f, 0.12f);
    }
    
    @Generated
    public static WingsModule getInstance() {
        return WingsModule.instance;
    }
    
    static {
        instance = new WingsModule();
        SHAPE = new WingPoint[] { new WingPoint(0.08f, 0.1f, 0.88f), new WingPoint(0.28f, 0.34f, 0.78f), new WingPoint(0.56f, 0.82f, 0.62f), new WingPoint(0.86f, 0.3f, 0.52f), new WingPoint(1.14f, 0.46f, 0.4f), new WingPoint(1.24f, 0.04f, 0.3f), new WingPoint(1.02f, -0.18f, 0.28f), new WingPoint(1.18f, -0.64f, 0.22f), new WingPoint(0.86f, -0.46f, 0.2f), new WingPoint(0.8f, -0.98f, 0.14f), new WingPoint(0.54f, -0.74f, 0.16f), new WingPoint(0.3f, -1.16f, 0.12f), new WingPoint(0.1f, -0.54f, 0.18f) };
    }
    
    private static final class WingPoint
    {
        final float x;
        final float y;
        final float alphaMul;
        
        WingPoint(final float x, final float y, final float alphaMul) {
            this.x = x;
            this.y = y;
            this.alphaMul = alphaMul;
        }
    }
    
    private static final class WingPose
    {
        final float preTranslateY;
        final float preTranslateZ;
        final float anchorY;
        final float anchorZ;
        final float pitchRotation;
        final float rollRotation;
        final float openMultiplier;
        final float scaleMultiplier;
        final float motionSpreadBoost;
        final float flapAmplitude;
        final float sideOffset;
        final float sideYOffset;
        final float sideZOffset;
        final float sideRoll;
        final float sidePitch;
        final float flapSpeed;
        
        WingPose(final float preTranslateY, final float preTranslateZ, final float anchorY, final float anchorZ, final float pitchRotation, final float rollRotation, final float openMultiplier, final float scaleMultiplier, final float motionSpreadBoost, final float flapAmplitude, final float sideOffset, final float sideYOffset, final float sideZOffset, final float sideRoll, final float sidePitch, final float flapSpeed) {
            this.preTranslateY = preTranslateY;
            this.preTranslateZ = preTranslateZ;
            this.anchorY = anchorY;
            this.anchorZ = anchorZ;
            this.pitchRotation = pitchRotation;
            this.rollRotation = rollRotation;
            this.openMultiplier = openMultiplier;
            this.scaleMultiplier = scaleMultiplier;
            this.motionSpreadBoost = motionSpreadBoost;
            this.flapAmplitude = flapAmplitude;
            this.sideOffset = sideOffset;
            this.sideYOffset = sideYOffset;
            this.sideZOffset = sideZOffset;
            this.sideRoll = sideRoll;
            this.sidePitch = sidePitch;
            this.flapSpeed = flapSpeed;
        }
    }
}

package sweetie.leonware.client.features.modules.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import lombok.Generated;
import net.minecraft.class_10142;
import net.minecraft.class_1304;
import net.minecraft.class_1657;
import net.minecraft.class_1802;
import net.minecraft.class_243;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_746;
import net.minecraft.class_7833;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.color.UIColors;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/WingsModule.class */
@ModuleRegister(name = "Wings", category = Category.RENDER)
public class WingsModule extends Module {
    private static final float DEFAULT_SPREAD = 8.0f;
    private static final int DEFAULT_ALPHA = 220;
    public final BooleanSetting self = new BooleanSetting("На себя").value((Boolean) true);
    public final BooleanSetting players = new BooleanSetting("На игроков").value((Boolean) false);
    public final BooleanSetting depthTest = new BooleanSetting("Не через тело").value((Boolean) true);
    public final SliderSetting size = new SliderSetting("Размер").value(Float.valueOf(1.0f)).range(0.75f, 1.35f).step(0.05f);
    private float selfBodyYaw;
    private boolean selfBodyYawInitialized;
    private static final WingsModule instance = new WingsModule();
    private static final WingPoint[] SHAPE = {new WingPoint(0.08f, 0.1f, 0.88f), new WingPoint(0.28f, 0.34f, 0.78f), new WingPoint(0.56f, 0.82f, 0.62f), new WingPoint(0.86f, 0.3f, 0.52f), new WingPoint(1.14f, 0.46f, 0.4f), new WingPoint(1.24f, 0.04f, 0.3f), new WingPoint(1.02f, -0.18f, 0.28f), new WingPoint(1.18f, -0.64f, 0.22f), new WingPoint(0.86f, -0.46f, 0.2f), new WingPoint(0.8f, -0.98f, 0.14f), new WingPoint(0.54f, -0.74f, 0.16f), new WingPoint(0.3f, -1.16f, 0.12f), new WingPoint(0.1f, -0.54f, 0.18f)};

    @Generated
    public static WingsModule getInstance() {
        return instance;
    }

    public WingsModule() {
        addSettings(this.self, this.players, this.depthTest, this.size);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        this.selfBodyYawInitialized = false;
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener renderEvent = Render3DEvent.getInstance().subscribe(new Listener(event -> {
            class_746 class_746Var;
            if (mc.field_1724 == null || mc.field_1687 == null || mc.field_1773 == null) {
                return;
            }
            class_4587 stack = event.matrixStack();
            float tickDelta = mc.method_61966().method_60637(true);
            class_243 camera = mc.field_1773.method_19418().method_19326();
            stack.method_22903();
            RenderSystem.enableBlend();
            RenderSystem.disableCull();
            if (this.depthTest.getValue().booleanValue()) {
                RenderSystem.enableDepthTest();
                RenderSystem.depthMask(false);
            } else {
                RenderSystem.disableDepthTest();
                RenderSystem.depthMask(false);
            }
            RenderSystem.setShader(class_10142.field_53876);
            if (this.self.getValue().booleanValue() && !mc.field_1690.method_31044().method_31034() && mc.field_1724.method_5805() && !hasElytra(mc.field_1724)) {
                try {
                    renderWings(stack, mc.field_1724, tickDelta, camera);
                } catch (Exception e) {
                }
            }
            if (this.players.getValue().booleanValue()) {
                for (class_746 class_746Var2 : mc.field_1687.method_18112()) {
                    if ((class_746Var2 instanceof class_1657) && (class_746Var = (class_1657) class_746Var2) != mc.field_1724 && class_746Var.method_5805() && !hasElytra(class_746Var)) {
                        try {
                            renderWings(stack, class_746Var, tickDelta, camera);
                        } catch (Exception e2) {
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
        }));
        addEvents(renderEvent);
    }

    private boolean hasElytra(class_1657 player) {
        return player.method_6118(class_1304.field_6174).method_31574(class_1802.field_8833);
    }

    private void renderWings(class_4587 stack, class_1657 player, float tickDelta, class_243 camera) {
        double x = class_3532.method_16436(tickDelta, player.field_6014, player.method_23317()) - camera.field_1352;
        double y = class_3532.method_16436(tickDelta, player.field_6036, player.method_23318()) - camera.field_1351;
        double z = class_3532.method_16436(tickDelta, player.field_5969, player.method_23321()) - camera.field_1350;
        float bodyYaw = resolveBodyYaw(player, tickDelta);
        float move = class_3532.method_15363(player.field_42108.method_48570(tickDelta), 0.0f, 1.0f);
        WingPose pose = resolvePose(player, tickDelta);
        if (pose == null) {
            return;
        }
        float flap = ((float) Math.sin((player.field_6012 + tickDelta) * pose.flapSpeed)) * pose.flapAmplitude;
        float open = (DEFAULT_SPREAD + flap + (move * pose.motionSpreadBoost)) * pose.openMultiplier;
        float wingScale = this.size.getValue().floatValue() * pose.scaleMultiplier;
        int baseColor = resolveBaseColor();
        int glowColor = resolveGlowColor(baseColor);
        int coreColor = resolveCoreColor(baseColor);
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
        renderWingSide(stack, -1.0f, open, baseColor, glowColor, coreColor, pose);
        renderWingSide(stack, 1.0f, open, baseColor, glowColor, coreColor, pose);
        stack.method_22909();
    }

    private void renderWingSide(class_4587 stack, float side, float open, int baseColor, int glowColor, int coreColor, WingPose pose) {
        stack.method_22903();
        stack.method_46416(side * pose.sideOffset, pose.sideYOffset, pose.sideZOffset);
        stack.method_22907(class_7833.field_40716.rotationDegrees(side * open));
        stack.method_22907(class_7833.field_40718.rotationDegrees(side * pose.sideRoll));
        stack.method_22907(class_7833.field_40714.rotationDegrees(pose.sidePitch));
        RenderSystem.blendFunc(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE);
        drawWingLayer(stack, side, 1.22f, setAlpha(glowColor, 48), setAlpha(glowColor, 0));
        drawWingLayer(stack, side, 0.84f, setAlpha(coreColor, 57), setAlpha(coreColor, 0));
        RenderSystem.blendFunc(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE_MINUS_SRC_ALPHA);
        drawWingLayer(stack, side, 1.0f, setAlpha(baseColor, DEFAULT_ALPHA), setAlpha(baseColor, 10));
        RenderSystem.blendFunc(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE);
        drawWingOutline(stack, side, 1.0f, setAlpha(baseColor, 136));
        drawWingRibs(stack, side, 0.96f, setAlpha(glowColor, 44));
        stack.method_22909();
    }

    private void drawWingLayer(class_4587 stack, float side, float scale, int rootColor, int edgeColor) {
        Matrix4f matrix = stack.method_23760().method_23761();
        class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27379, class_290.field_1576);
        for (int i = 0; i < SHAPE.length; i++) {
            WingPoint cur = SHAPE[i];
            WingPoint next = SHAPE[(i + 1) % SHAPE.length];
            vertex(buffer, matrix, 0.0f, 0.0f, 0.0f, rootColor);
            vertex(buffer, matrix, side * cur.x * scale, cur.y * scale, 0.0f, applyPointAlpha(edgeColor, cur.alphaMul));
            vertex(buffer, matrix, side * next.x * scale, next.y * scale, 0.0f, applyPointAlpha(edgeColor, next.alphaMul));
        }
        class_286.method_43433(buffer.method_60800());
    }

    private void drawWingOutline(class_4587 stack, float side, float scale, int color) {
        Matrix4f matrix = stack.method_23760().method_23761();
        RenderSystem.lineWidth(1.35f);
        GL11.glEnable(2848);
        class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_29345, class_290.field_1576);
        for (WingPoint point : SHAPE) {
            vertex(buffer, matrix, side * point.x * scale, point.y * scale, 0.0f, color);
        }
        vertex(buffer, matrix, side * SHAPE[0].x * scale, SHAPE[0].y * scale, 0.0f, color);
        class_286.method_43433(buffer.method_60800());
        GL11.glDisable(2848);
    }

    private void drawWingRibs(class_4587 stack, float side, float scale, int color) {
        Matrix4f matrix = stack.method_23760().method_23761();
        int[] ribIndices = {2, 4, 7, 9, 11};
        RenderSystem.lineWidth(0.9f);
        class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27377, class_290.field_1576);
        for (int idx : ribIndices) {
            WingPoint point = SHAPE[idx];
            vertex(buffer, matrix, 0.0f, 0.0f, 0.0f, setAlpha(color, Math.max(8, (int) (alpha(color) * 0.75f))));
            vertex(buffer, matrix, side * point.x * scale, point.y * scale, 0.0f, applyPointAlpha(color, point.alphaMul));
        }
        class_286.method_43433(buffer.method_60800());
    }

    private int applyPointAlpha(int color, float multiplier) {
        return setAlpha(color, Math.max(0, Math.min(255, (int) (alpha(color) * multiplier))));
    }

    private static int setAlpha(int color, int a) {
        return (class_3532.method_15340(a, 0, 255) << 24) | (color & 16777215);
    }

    private static int alpha(int color) {
        return (color >> 24) & 255;
    }

    private static int red(int color) {
        return (color >> 16) & 255;
    }

    private static int green(int color) {
        return (color >> 8) & 255;
    }

    private static int blue(int color) {
        return color & 255;
    }

    private static int getColor(int r, int g, int b, int a) {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    private void vertex(class_287 buffer, Matrix4f matrix, float x, float y, float z, int color) {
        buffer.method_22918(matrix, x, y, z).method_22915(red(color) / 255.0f, green(color) / 255.0f, blue(color) / 255.0f, alpha(color) / 255.0f);
    }

    private int resolveBaseColor() {
        Color c = UIColors.gradient(0);
        return getColor(c.getRed(), c.getGreen(), c.getBlue(), 255);
    }

    private int resolveGlowColor(int base) {
        return interpolateColor(base, getColor(255, 255, 255, 255), 0.28f);
    }

    private int resolveCoreColor(int base) {
        return interpolateColor(base, getColor(255, 255, 255, 255), 0.55f);
    }

    private static int interpolateColor(int a, int b, float t) {
        int ar = red(a);
        int ag = green(a);
        int ab = blue(a);
        int aa = alpha(a);
        int br = red(b);
        int bg = green(b);
        int bb = blue(b);
        int ba = alpha(b);
        return getColor((int) (ar + ((br - ar) * t)), (int) (ag + ((bg - ag) * t)), (int) (ab + ((bb - ab) * t)), (int) (aa + ((ba - aa) * t)));
    }

    private float resolveBodyYaw(class_1657 player, float tickDelta) {
        float target = class_3532.method_17821(tickDelta, player.field_6220, player.field_6283);
        if (player != mc.field_1724) {
            return target;
        }
        if (!this.selfBodyYawInitialized || player.field_6012 < 2) {
            this.selfBodyYaw = target;
            this.selfBodyYawInitialized = true;
            return this.selfBodyYaw;
        }
        this.selfBodyYaw = approachDegrees(this.selfBodyYaw, target, 14.0f);
        return this.selfBodyYaw;
    }

    private static float approachDegrees(float current, float target, float maxDelta) {
        float delta = class_3532.method_15393(target - current);
        return current + class_3532.method_15363(delta, -maxDelta, maxDelta);
    }

    private WingPose resolvePose(class_1657 player, float tickDelta) {
        float pitch = class_3532.method_16439(tickDelta, player.field_6004, player.method_36455());
        if (player.method_6128()) {
            float flightTicks = player.method_6003() + tickDelta;
            float flightProgress = class_3532.method_15363((flightTicks * flightTicks) / 100.0f, 0.0f, 1.0f);
            float pitchRotation = flightProgress * ((-90.0f) - pitch);
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

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/WingsModule$WingPoint.class */
    private static final class WingPoint {
        final float x;
        final float y;
        final float alphaMul;

        WingPoint(float x, float y, float alphaMul) {
            this.x = x;
            this.y = y;
            this.alphaMul = alphaMul;
        }
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/WingsModule$WingPose.class */
    private static final class WingPose {
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

        WingPose(float preTranslateY, float preTranslateZ, float anchorY, float anchorZ, float pitchRotation, float rollRotation, float openMultiplier, float scaleMultiplier, float motionSpreadBoost, float flapAmplitude, float sideOffset, float sideYOffset, float sideZOffset, float sideRoll, float sidePitch, float flapSpeed) {
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

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_241
 *  net.minecraft.class_332
 *  net.minecraft.class_3532
 *  net.minecraft.class_7833
 *  org.joml.Vector2i
 */
package sweetie.leonware.api.system.client;

import lombok.Generated;
import net.minecraft.class_241;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_7833;
import org.joml.Vector2i;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.api.utils.color.ColorUtil;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import sweetie.leonware.client.features.modules.render.PointersModule;
import sweetie.leonware.client.services.RenderService;

public class GpsManager
implements QuickImports {
    private static final GpsManager instance = new GpsManager();
    private Vector2i gpsPosition = null;
    private Vector2i lastGpsPosition = null;
    private String targetLabel = null;
    private final AnimationUtil distanceAnimation = new AnimationUtil();
    private final AnimationUtil switchAnimation = new AnimationUtil();

    public void setGps(Vector2i pos, String label) {
        this.gpsPosition = pos;
        this.targetLabel = label;
    }

    public void update(class_332 context) {
        if (this.gpsPosition != null) {
            this.lastGpsPosition = this.gpsPosition;
        }
        if (context == null) {
            return;
        }
        float scale = RenderService.getInstance().getScale();
        boolean noGps = this.gpsPosition == null;
        boolean noLastGps = this.lastGpsPosition == null;
        float distance = !noLastGps ? this.getDistance(this.lastGpsPosition) : this.getDistance(new Vector2i(0, 0));
        float maxDistance = 10.0f;
        float minDistance = 3.0f;
        this.distanceAnimation.update();
        this.switchAnimation.update();
        this.distanceAnimation.run(distance <= minDistance ? 0.0 : (distance >= maxDistance ? 1.0 : (double)((distance - minDistance) / (maxDistance - minDistance))), 500L, Easing.EXPO_OUT);
        this.switchAnimation.run(noGps ? 0.0 : 1.0, 500L, Easing.EXPO_OUT);
        if (this.distanceAnimation.getValue() < 0.1 || this.switchAnimation.getValue() < 0.1) {
            return;
        }
        float switchAnim = (float)this.switchAnimation.getValue();
        double combinedAnim = this.distanceAnimation.getValue() * (double)switchAnim;
        float x = (float)mc.method_22683().method_4486() / 2.0f;
        float y = (float)mc.method_22683().method_4502() / 6.0f;
        float targetX = noGps ? 0.0f : (float)this.lastGpsPosition.x;
        float targetY = noGps ? 0.0f : (float)this.lastGpsPosition.y;
        float rotation = this.getRotations(new class_241(targetX, targetY)) - GpsManager.mc.field_1724.method_36454();
        float arrowHeight = 12.0f * scale;
        context.method_51448().method_22903();
        context.method_51448().method_46416(x, y, 0.0f);
        context.method_51448().method_22907(class_7833.field_40718.rotationDegrees(rotation));
        context.method_51448().method_46416(-x, -y, 0.0f);
        PointersModule.getInstance().drawPointer(context, x, y - arrowHeight * 1.75f, 30.0f * scale, ColorUtil.setAlpha(UIColors.gradient((int)combinedAnim), (int)(255.0 * combinedAnim)), true);
        RenderUtil.OTHER.scaleStop(context.method_51448());
        float textY = y + arrowHeight * (2.0f - switchAnim);
        Fonts.PS_BOLD.drawCenteredText(context.method_51448(), String.format("%.1f", Float.valueOf(distance)) + "m", x, textY, 8.0f * scale, UIColors.textColor((int)(255.0 * combinedAnim)));
        if (this.targetLabel != null && !noGps) {
            Fonts.PS_BOLD.drawCenteredText(context.method_51448(), this.targetLabel, x, textY + 10.0f * scale, 7.0f * scale, UIColors.textColor((int)(255.0 * combinedAnim)));
        }
    }

    private float getDistance(Vector2i targetVec) {
        double x = GpsManager.mc.field_1724.method_19538().field_1352 - (double)targetVec.x;
        double z = GpsManager.mc.field_1724.method_19538().field_1350 - (double)targetVec.y;
        return class_3532.method_15355((float)((float)(x * x + z * z)));
    }

    private float getRotations(class_241 vec) {
        if (GpsManager.mc.field_1724 == null) {
            return 0.0f;
        }
        double x = (double)vec.field_1343 - GpsManager.mc.field_1724.method_19538().field_1352;
        double z = (double)vec.field_1342 - GpsManager.mc.field_1724.method_19538().field_1350;
        return (float)(-Math.toDegrees(Math.atan2(x, z)));
    }

    @Generated
    public Vector2i getGpsPosition() {
        return this.gpsPosition;
    }

    @Generated
    public Vector2i getLastGpsPosition() {
        return this.lastGpsPosition;
    }

    @Generated
    public String getTargetLabel() {
        return this.targetLabel;
    }

    @Generated
    public AnimationUtil getDistanceAnimation() {
        return this.distanceAnimation;
    }

    @Generated
    public AnimationUtil getSwitchAnimation() {
        return this.switchAnimation;
    }

    @Generated
    public void setGpsPosition(Vector2i gpsPosition) {
        this.gpsPosition = gpsPosition;
    }

    @Generated
    public void setLastGpsPosition(Vector2i lastGpsPosition) {
        this.lastGpsPosition = lastGpsPosition;
    }

    @Generated
    public void setTargetLabel(String targetLabel) {
        this.targetLabel = targetLabel;
    }

    @Generated
    public static GpsManager getInstance() {
        return instance;
    }
}


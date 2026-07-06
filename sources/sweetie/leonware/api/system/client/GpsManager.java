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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/system/client/GpsManager.class */
public class GpsManager implements QuickImports {
    private static final GpsManager instance = new GpsManager();
    private Vector2i gpsPosition = null;
    private Vector2i lastGpsPosition = null;
    private String targetLabel = null;
    private final AnimationUtil distanceAnimation = new AnimationUtil();
    private final AnimationUtil switchAnimation = new AnimationUtil();

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

    public void setGps(Vector2i pos, String label) {
        this.gpsPosition = pos;
        this.targetLabel = label;
    }

    public void update(class_332 context) {
        double d;
        if (this.gpsPosition != null) {
            this.lastGpsPosition = this.gpsPosition;
        }
        if (context == null) {
            return;
        }
        float scale = RenderService.getInstance().getScale();
        boolean noGps = this.gpsPosition == null;
        boolean noLastGps = this.lastGpsPosition == null;
        float distance = !noLastGps ? getDistance(this.lastGpsPosition) : getDistance(new Vector2i(0, 0));
        this.distanceAnimation.update();
        this.switchAnimation.update();
        AnimationUtil animationUtil = this.distanceAnimation;
        if (distance <= 3.0f) {
            d = 0.0d;
        } else {
            d = distance >= 10.0f ? 1.0d : (distance - 3.0f) / (10.0f - 3.0f);
        }
        animationUtil.run(d, 500L, Easing.EXPO_OUT);
        this.switchAnimation.run(noGps ? 0.0d : 1.0d, 500L, Easing.EXPO_OUT);
        if (this.distanceAnimation.getValue() < 0.1d || this.switchAnimation.getValue() < 0.1d) {
            return;
        }
        float switchAnim = (float) this.switchAnimation.getValue();
        double combinedAnim = this.distanceAnimation.getValue() * ((double) switchAnim);
        float x = mc.method_22683().method_4486() / 2.0f;
        float y = mc.method_22683().method_4502() / 6.0f;
        float targetX = noGps ? 0.0f : this.lastGpsPosition.x;
        float targetY = noGps ? 0.0f : this.lastGpsPosition.y;
        float rotation = getRotations(new class_241(targetX, targetY)) - mc.field_1724.method_36454();
        float arrowHeight = 12.0f * scale;
        context.method_51448().method_22903();
        context.method_51448().method_46416(x, y, 0.0f);
        context.method_51448().method_22907(class_7833.field_40718.rotationDegrees(rotation));
        context.method_51448().method_46416(-x, -y, 0.0f);
        PointersModule.getInstance().drawPointer(context, x, y - (arrowHeight * 1.75f), 30.0f * scale, ColorUtil.setAlpha(UIColors.gradient((int) combinedAnim), (int) (255.0d * combinedAnim)), true);
        RenderUtil.OTHER.scaleStop(context.method_51448());
        float textY = y + (arrowHeight * (2.0f - switchAnim));
        Fonts.PS_BOLD.drawCenteredText(context.method_51448(), String.format("%.1f", Float.valueOf(distance)) + "m", x, textY, 8.0f * scale, UIColors.textColor((int) (255.0d * combinedAnim)));
        if (this.targetLabel != null && !noGps) {
            Fonts.PS_BOLD.drawCenteredText(context.method_51448(), this.targetLabel, x, textY + (10.0f * scale), 7.0f * scale, UIColors.textColor((int) (255.0d * combinedAnim)));
        }
    }

    private float getDistance(Vector2i targetVec) {
        double x = mc.field_1724.method_19538().field_1352 - ((double) targetVec.x);
        double z = mc.field_1724.method_19538().field_1350 - ((double) targetVec.y);
        return class_3532.method_15355((float) ((x * x) + (z * z)));
    }

    private float getRotations(class_241 vec) {
        if (mc.field_1724 == null) {
            return 0.0f;
        }
        double x = ((double) vec.field_1343) - mc.field_1724.method_19538().field_1352;
        double z = ((double) vec.field_1342) - mc.field_1724.method_19538().field_1350;
        return (float) (-Math.toDegrees(Math.atan2(x, z)));
    }
}

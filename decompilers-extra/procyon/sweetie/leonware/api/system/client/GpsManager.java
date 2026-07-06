// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.system.client;

import lombok.Generated;
import net.minecraft.class_3532;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.color.ColorUtil;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.client.features.modules.render.PointersModule;
import net.minecraft.class_7833;
import net.minecraft.class_241;
import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.client.services.RenderService;
import net.minecraft.class_332;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import org.joml.Vector2i;
import sweetie.leonware.api.system.interfaces.QuickImports;

public class GpsManager implements QuickImports
{
    private static final GpsManager instance;
    private Vector2i gpsPosition;
    private Vector2i lastGpsPosition;
    private String targetLabel;
    private final AnimationUtil distanceAnimation;
    private final AnimationUtil switchAnimation;
    
    public GpsManager() {
        this.gpsPosition = null;
        this.lastGpsPosition = null;
        this.targetLabel = null;
        this.distanceAnimation = new AnimationUtil();
        this.switchAnimation = new AnimationUtil();
    }
    
    public void setGps(final Vector2i pos, final String label) {
        this.gpsPosition = pos;
        this.targetLabel = label;
    }
    
    public void update(final class_332 context) {
        if (this.gpsPosition != null) {
            this.lastGpsPosition = this.gpsPosition;
        }
        if (context == null) {
            return;
        }
        final float scale = RenderService.getInstance().getScale();
        final boolean noGps = this.gpsPosition == null;
        final boolean noLastGps = this.lastGpsPosition == null;
        final float distance = noLastGps ? this.getDistance(new Vector2i(0, 0)) : this.getDistance(this.lastGpsPosition);
        final float maxDistance = 10.0f;
        final float minDistance = 3.0f;
        this.distanceAnimation.update();
        this.switchAnimation.update();
        this.distanceAnimation.run((distance <= minDistance) ? 0.0 : ((distance >= maxDistance) ? 1.0 : ((distance - minDistance) / (maxDistance - minDistance))), 500L, Easing.EXPO_OUT);
        this.switchAnimation.run(noGps ? 0.0 : 1.0, 500L, Easing.EXPO_OUT);
        if (this.distanceAnimation.getValue() < 0.1 || this.switchAnimation.getValue() < 0.1) {
            return;
        }
        final float switchAnim = (float)this.switchAnimation.getValue();
        final double combinedAnim = this.distanceAnimation.getValue() * switchAnim;
        final float x = GpsManager.mc.method_22683().method_4486() / 2.0f;
        final float y = GpsManager.mc.method_22683().method_4502() / 6.0f;
        final float targetX = noGps ? 0.0f : ((float)this.lastGpsPosition.x);
        final float targetY = noGps ? 0.0f : ((float)this.lastGpsPosition.y);
        final float rotation = this.getRotations(new class_241(targetX, targetY)) - GpsManager.mc.field_1724.method_36454();
        final float arrowHeight = 12.0f * scale;
        context.method_51448().method_22903();
        context.method_51448().method_46416(x, y, 0.0f);
        context.method_51448().method_22907(class_7833.field_40718.rotationDegrees(rotation));
        context.method_51448().method_46416(-x, -y, 0.0f);
        PointersModule.getInstance().drawPointer(context, x, y - arrowHeight * 1.75f, 30.0f * scale, ColorUtil.setAlpha(UIColors.gradient((int)combinedAnim), (int)(255.0 * combinedAnim)), true);
        RenderUtil.OTHER.scaleStop(context.method_51448());
        final float textY = y + arrowHeight * (2.0f - switchAnim);
        Fonts.PS_BOLD.drawCenteredText(context.method_51448(), String.format("%.1f", distance), x, textY, 8.0f * scale, UIColors.textColor((int)(255.0 * combinedAnim)));
        if (this.targetLabel != null && !noGps) {
            Fonts.PS_BOLD.drawCenteredText(context.method_51448(), this.targetLabel, x, textY + 10.0f * scale, 7.0f * scale, UIColors.textColor((int)(255.0 * combinedAnim)));
        }
    }
    
    private float getDistance(final Vector2i targetVec) {
        final double x = GpsManager.mc.field_1724.method_19538().field_1352 - targetVec.x;
        final double z = GpsManager.mc.field_1724.method_19538().field_1350 - targetVec.y;
        return class_3532.method_15355((float)(x * x + z * z));
    }
    
    private float getRotations(final class_241 vec) {
        if (GpsManager.mc.field_1724 == null) {
            return 0.0f;
        }
        final double x = vec.field_1343 - GpsManager.mc.field_1724.method_19538().field_1352;
        final double z = vec.field_1342 - GpsManager.mc.field_1724.method_19538().field_1350;
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
    public void setGpsPosition(final Vector2i gpsPosition) {
        this.gpsPosition = gpsPosition;
    }
    
    @Generated
    public void setLastGpsPosition(final Vector2i lastGpsPosition) {
        this.lastGpsPosition = lastGpsPosition;
    }
    
    @Generated
    public void setTargetLabel(final String targetLabel) {
        this.targetLabel = targetLabel;
    }
    
    @Generated
    public static GpsManager getInstance() {
        return GpsManager.instance;
    }
    
    static {
        instance = new GpsManager();
    }
}

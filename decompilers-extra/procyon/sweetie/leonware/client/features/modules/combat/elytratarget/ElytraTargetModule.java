// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.combat.elytratarget;

import lombok.Generated;
import net.minecraft.class_243;
import net.minecraft.class_1309;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.utils.render.display.BoxRender;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.client.features.modules.combat.AuraModule;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.other.RotationUpdateEvent;
import sweetie.leonware.api.module.setting.Setting;
import java.util.function.Supplier;
import java.util.Objects;
import java.awt.Color;
import sweetie.leonware.api.module.setting.ColorSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Elytra Target", category = Category.COMBAT)
public class ElytraTargetModule extends Module
{
    private static final ElytraTargetModule instance;
    public final ElytraRotationProcessor elytraRotationProcessor;
    private final BooleanSetting renderPrediction;
    private final ColorSetting predictionColor;
    
    public ElytraTargetModule() {
        this.elytraRotationProcessor = new ElytraRotationProcessor(this);
        this.renderPrediction = new BooleanSetting("Render prediction").value(false);
        final ColorSetting value = new ColorSetting("Prediction color").value(new Color(255, 50, 50, 120));
        final BooleanSetting renderPrediction = this.renderPrediction;
        Objects.requireNonNull(renderPrediction);
        this.predictionColor = value.setVisible((Supplier<Boolean>)renderPrediction::getValue);
        this.addSettings(this.elytraRotationProcessor.getSettings());
        this.addSettings(this.renderPrediction, this.predictionColor);
    }
    
    @Override
    public void onEvent() {
        final EventListener rotationUpdateEvent = RotationUpdateEvent.getInstance().subscribe(new Listener<RotationUpdateEvent>(event -> this.elytraRotationProcessor.processRotation()));
        final EventListener renderEvent = Render3DEvent.getInstance().subscribe(new Listener<Render3DEvent.Render3DEventData>(event -> {
            if (!this.renderPrediction.getValue()) {
                return;
            }
            else if (ElytraTargetModule.mc.field_1724 == null || ElytraTargetModule.mc.field_1687 == null) {
                return;
            }
            else if (!ElytraTargetModule.mc.field_1724.method_6128()) {
                return;
            }
            else {
                final class_1309 target = AuraModule.getInstance().target;
                if (target == null) {
                    return;
                }
                else {
                    final class_243 predicted = this.elytraRotationProcessor.getPredictedPos(target);
                    final float halfWidth = target.method_17681() / 2.0f;
                    final float height = target.method_17682();
                    final float x1 = (float)(predicted.field_1352 - halfWidth);
                    final float y1 = (float)predicted.field_1351;
                    final float z1 = (float)(predicted.field_1350 - halfWidth);
                    final float x2 = (float)(predicted.field_1352 + halfWidth);
                    final float y2 = (float)(predicted.field_1351 + height);
                    final float z2 = (float)(predicted.field_1350 + halfWidth);
                    final Color color = this.predictionColor.getValue();
                    final Color fill = new Color(color.getRed(), color.getGreen(), color.getBlue(), 40);
                    RenderUtil.BOX.drawBox(x1, y1, z1, x2, y2, z2, 1.0f, fill, BoxRender.Render.FILL, 0.0f);
                    RenderUtil.BOX.drawBox(x1, y1, z1, x2, y2, z2, 1.5f, color, BoxRender.Render.OUTLINE, 0.0f);
                    return;
                }
            }
        }));
        this.addEvents(rotationUpdateEvent, renderEvent);
    }
    
    @Generated
    public static ElytraTargetModule getInstance() {
        return ElytraTargetModule.instance;
    }
    
    static {
        instance = new ElytraTargetModule();
    }
}

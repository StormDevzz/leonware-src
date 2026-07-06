package sweetie.leonware.client.features.modules.combat.elytratarget;

import java.awt.Color;
import java.util.Objects;
import lombok.Generated;
import net.minecraft.class_1309;
import net.minecraft.class_243;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.other.RotationUpdateEvent;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ColorSetting;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.display.BoxRender;
import sweetie.leonware.client.features.modules.combat.AuraModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/elytratarget/ElytraTargetModule.class */
@ModuleRegister(name = "Elytra Target", category = Category.COMBAT)
public class ElytraTargetModule extends Module {
    private static final ElytraTargetModule instance = new ElytraTargetModule();
    public final ElytraRotationProcessor elytraRotationProcessor = new ElytraRotationProcessor(this);
    private final BooleanSetting renderPrediction = new BooleanSetting("Render prediction").value((Boolean) false);
    private final ColorSetting predictionColor;

    @Generated
    public static ElytraTargetModule getInstance() {
        return instance;
    }

    /* JADX WARN: Type inference failed for: r1v5, types: [sweetie.leonware.api.module.setting.ColorSetting] */
    public ElytraTargetModule() {
        ColorSetting colorSettingValue = new ColorSetting("Prediction color").value(new Color(255, 50, 50, 120));
        BooleanSetting booleanSetting = this.renderPrediction;
        Objects.requireNonNull(booleanSetting);
        this.predictionColor = colorSettingValue.setVisible(booleanSetting::getValue);
        addSettings(this.elytraRotationProcessor.getSettings());
        addSettings(this.renderPrediction, this.predictionColor);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener rotationUpdateEvent = RotationUpdateEvent.getInstance().subscribe(new Listener(event -> {
            this.elytraRotationProcessor.processRotation();
        }));
        EventListener renderEvent = Render3DEvent.getInstance().subscribe(new Listener(event2 -> {
            class_1309 target;
            if (!this.renderPrediction.getValue().booleanValue() || mc.field_1724 == null || mc.field_1687 == null || !mc.field_1724.method_6128() || (target = AuraModule.getInstance().target) == null) {
                return;
            }
            class_243 predicted = this.elytraRotationProcessor.getPredictedPos(target);
            float halfWidth = target.method_17681() / 2.0f;
            float height = target.method_17682();
            float x1 = (float) (predicted.field_1352 - ((double) halfWidth));
            float y1 = (float) predicted.field_1351;
            float z1 = (float) (predicted.field_1350 - ((double) halfWidth));
            float x2 = (float) (predicted.field_1352 + ((double) halfWidth));
            float y2 = (float) (predicted.field_1351 + ((double) height));
            float z2 = (float) (predicted.field_1350 + ((double) halfWidth));
            Color color = this.predictionColor.getValue();
            Color fill = new Color(color.getRed(), color.getGreen(), color.getBlue(), 40);
            RenderUtil.BOX.drawBox(x1, y1, z1, x2, y2, z2, 1.0f, fill, BoxRender.Render.FILL, 0.0f);
            RenderUtil.BOX.drawBox(x1, y1, z1, x2, y2, z2, 1.5f, color, BoxRender.Render.OUTLINE, 0.0f);
        }));
        addEvents(rotationUpdateEvent, renderEvent);
    }
}

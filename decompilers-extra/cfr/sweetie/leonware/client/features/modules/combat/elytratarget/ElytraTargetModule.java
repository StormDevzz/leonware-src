/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1309
 *  net.minecraft.class_243
 */
package sweetie.leonware.client.features.modules.combat.elytratarget;

import java.awt.Color;
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
import sweetie.leonware.client.features.modules.combat.elytratarget.ElytraRotationProcessor;

@ModuleRegister(name="Elytra Target", category=Category.COMBAT)
public class ElytraTargetModule
extends Module {
    private static final ElytraTargetModule instance = new ElytraTargetModule();
    public final ElytraRotationProcessor elytraRotationProcessor = new ElytraRotationProcessor(this);
    private final BooleanSetting renderPrediction = new BooleanSetting("Render prediction").value(false);
    private final ColorSetting predictionColor = new ColorSetting("Prediction color").value(new Color(255, 50, 50, 120)).setVisible(this.renderPrediction::getValue);

    public ElytraTargetModule() {
        this.addSettings(this.elytraRotationProcessor.getSettings());
        this.addSettings(this.renderPrediction, this.predictionColor);
    }

    @Override
    public void onEvent() {
        EventListener rotationUpdateEvent = RotationUpdateEvent.getInstance().subscribe(new Listener<RotationUpdateEvent>(event -> this.elytraRotationProcessor.processRotation()));
        EventListener renderEvent = Render3DEvent.getInstance().subscribe(new Listener<Render3DEvent.Render3DEventData>(event -> {
            if (!((Boolean)this.renderPrediction.getValue()).booleanValue()) {
                return;
            }
            if (ElytraTargetModule.mc.field_1724 == null || ElytraTargetModule.mc.field_1687 == null) {
                return;
            }
            if (!ElytraTargetModule.mc.field_1724.method_6128()) {
                return;
            }
            class_1309 target = AuraModule.getInstance().target;
            if (target == null) {
                return;
            }
            class_243 predicted = this.elytraRotationProcessor.getPredictedPos(target);
            float halfWidth = target.method_17681() / 2.0f;
            float height = target.method_17682();
            float x1 = (float)(predicted.field_1352 - (double)halfWidth);
            float y1 = (float)predicted.field_1351;
            float z1 = (float)(predicted.field_1350 - (double)halfWidth);
            float x2 = (float)(predicted.field_1352 + (double)halfWidth);
            float y2 = (float)(predicted.field_1351 + (double)height);
            float z2 = (float)(predicted.field_1350 + (double)halfWidth);
            Color color = (Color)this.predictionColor.getValue();
            Color fill = new Color(color.getRed(), color.getGreen(), color.getBlue(), 40);
            RenderUtil.BOX.drawBox(x1, y1, z1, x2, y2, z2, 1.0f, fill, BoxRender.Render.FILL, 0.0f);
            RenderUtil.BOX.drawBox(x1, y1, z1, x2, y2, z2, 1.5f, color, BoxRender.Render.OUTLINE, 0.0f);
        }));
        this.addEvents(rotationUpdateEvent, renderEvent);
    }

    @Generated
    public static ElytraTargetModule getInstance() {
        return instance;
    }
}


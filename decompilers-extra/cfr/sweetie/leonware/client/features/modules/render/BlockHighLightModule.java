/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1922
 *  net.minecraft.class_2338
 *  net.minecraft.class_238
 *  net.minecraft.class_239
 *  net.minecraft.class_239$class_240
 *  net.minecraft.class_265
 *  net.minecraft.class_3965
 */
package sweetie.leonware.client.features.modules.render;

import java.awt.Color;
import net.minecraft.class_1922;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_239;
import net.minecraft.class_265;
import net.minecraft.class_3965;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.display.BoxRender;

@ModuleRegister(name="Block HighLight", category=Category.RENDER)
public class BlockHighLightModule
extends Module {
    private static final BlockHighLightModule instance = new BlockHighLightModule();
    private final ModeSetting colorMode = new ModeSetting("\u0426\u0432\u0435\u0442").value("\u0422\u0435\u043c\u0430").values("\u0422\u0435\u043c\u0430");
    private final BooleanSetting filled = new BooleanSetting("\u0417\u0430\u043b\u0438\u0432\u043a\u0430").value(true);

    public BlockHighLightModule() {
        this.addSettings(this.colorMode, this.filled);
    }

    @Override
    public void onEvent() {
        EventListener render3DEvent = Render3DEvent.getInstance().subscribe(new Listener<Render3DEvent.Render3DEventData>(event -> {
            class_238 box;
            class_239 patt0$temp = BlockHighLightModule.mc.field_1765;
            if (!(patt0$temp instanceof class_3965)) {
                return;
            }
            class_3965 result = (class_3965)patt0$temp;
            if (result.method_17783() != class_239.class_240.field_1332) {
                return;
            }
            class_2338 pos = result.method_17777();
            if (pos == null) {
                return;
            }
            Color awtColor = ((String)this.colorMode.getValue()).equals("\u0422\u0435\u043c\u0430") ? UIColors.primary() : Color.BLACK;
            class_265 shape = BlockHighLightModule.mc.field_1687.method_8320(pos).method_26218((class_1922)BlockHighLightModule.mc.field_1687, pos);
            class_238 class_2383 = box = shape.method_1110() ? new class_238(pos) : shape.method_1107().method_996(pos);
            if (((Boolean)this.filled.getValue()).booleanValue()) {
                Color fillColor = new Color(awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue(), 50);
                RenderUtil.BOX.drawBox((float)box.field_1323, (float)box.field_1322, (float)box.field_1321, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324, 1.5f, fillColor, BoxRender.Render.FILL, 0.0f);
                float topEpsilon = 0.002f;
                float topThickness = 0.035f;
                float topMinY = (float)box.field_1325 + topEpsilon;
                float topMaxY = topMinY + topThickness;
                Color topFillColor = new Color(awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue(), 220);
                RenderUtil.BOX.drawBox((float)box.field_1323, topMinY, (float)box.field_1321, (float)box.field_1320, topMaxY, (float)box.field_1324, 1.0f, topFillColor, BoxRender.Render.FILL, 0.0f);
                RenderUtil.BOX.drawBox((float)box.field_1323, (float)box.field_1322, (float)box.field_1321, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324, 1.5f, awtColor, BoxRender.Render.OUTLINE, 0.0f);
            } else {
                RenderUtil.BOX.drawBox((float)box.field_1323, (float)box.field_1322, (float)box.field_1321, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324, 1.5f, awtColor, BoxRender.Render.OUTLINE, 0.0f);
            }
        }));
        this.addEvents(render3DEvent);
    }

    public static BlockHighLightModule getInstance() {
        return instance;
    }
}


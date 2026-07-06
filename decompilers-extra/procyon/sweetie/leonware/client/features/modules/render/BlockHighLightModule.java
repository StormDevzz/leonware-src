// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render;

import net.minecraft.class_265;
import net.minecraft.class_2338;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.utils.render.display.BoxRender;
import sweetie.leonware.api.utils.render.RenderUtil;
import net.minecraft.class_238;
import net.minecraft.class_1922;
import java.awt.Color;
import sweetie.leonware.api.utils.color.UIColors;
import net.minecraft.class_239;
import net.minecraft.class_3965;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Block HighLight", category = Category.RENDER)
public class BlockHighLightModule extends Module
{
    private static final BlockHighLightModule instance;
    private final ModeSetting colorMode;
    private final BooleanSetting filled;
    
    public BlockHighLightModule() {
        this.colorMode = new ModeSetting("\u0426\u0432\u0435\u0442").value("\u0422\u0435\u043c\u0430").values("\u0422\u0435\u043c\u0430");
        this.filled = new BooleanSetting("\u0417\u0430\u043b\u0438\u0432\u043a\u0430").value(true);
        this.addSettings(this.colorMode, this.filled);
    }
    
    @Override
    public void onEvent() {
        final EventListener render3DEvent = Render3DEvent.getInstance().subscribe(new Listener<Render3DEvent.Render3DEventData>(event -> {
            final class_239 patt0$temp = BlockHighLightModule.mc.field_1765;
            if (patt0$temp instanceof final class_3965 result) {
                if (result.method_17783() != class_239.class_240.field_1332) {
                    return;
                }
                else {
                    final class_2338 pos = result.method_17777();
                    if (pos == null) {
                        return;
                    }
                    else {
                        Color awtColor;
                        if (this.colorMode.getValue().equals("\u0422\u0435\u043c\u0430")) {
                            awtColor = UIColors.primary();
                        }
                        else {
                            awtColor = Color.BLACK;
                        }
                        final class_265 shape = BlockHighLightModule.mc.field_1687.method_8320(pos).method_26218((class_1922)BlockHighLightModule.mc.field_1687, pos);
                        class_238 method_996 = null;
                        if (shape.method_1110()) {
                            new(net.minecraft.class_238.class)();
                            new class_238(pos);
                        }
                        else {
                            method_996 = shape.method_1107().method_996(pos);
                        }
                        final class_238 box = method_996;
                        if (this.filled.getValue()) {
                            final Color fillColor = new Color(awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue(), 50);
                            RenderUtil.BOX.drawBox((float)box.field_1323, (float)box.field_1322, (float)box.field_1321, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324, 1.5f, fillColor, BoxRender.Render.FILL, 0.0f);
                            final float topEpsilon = 0.002f;
                            final float topThickness = 0.035f;
                            final float topMinY = (float)box.field_1325 + topEpsilon;
                            final float topMaxY = topMinY + topThickness;
                            final Color topFillColor = new Color(awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue(), 220);
                            RenderUtil.BOX.drawBox((float)box.field_1323, topMinY, (float)box.field_1321, (float)box.field_1320, topMaxY, (float)box.field_1324, 1.0f, topFillColor, BoxRender.Render.FILL, 0.0f);
                            RenderUtil.BOX.drawBox((float)box.field_1323, (float)box.field_1322, (float)box.field_1321, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324, 1.5f, awtColor, BoxRender.Render.OUTLINE, 0.0f);
                        }
                        else {
                            RenderUtil.BOX.drawBox((float)box.field_1323, (float)box.field_1322, (float)box.field_1321, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324, 1.5f, awtColor, BoxRender.Render.OUTLINE, 0.0f);
                        }
                        return;
                    }
                }
            }
            else {
                return;
            }
        }));
        this.addEvents(render3DEvent);
    }
    
    public static BlockHighLightModule getInstance() {
        return BlockHighLightModule.instance;
    }
    
    static {
        instance = new BlockHighLightModule();
    }
}

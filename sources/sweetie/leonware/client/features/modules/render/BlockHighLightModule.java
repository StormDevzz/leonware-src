package sweetie.leonware.client.features.modules.render;

import java.awt.Color;
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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/BlockHighLightModule.class */
@ModuleRegister(name = "Block HighLight", category = Category.RENDER)
public class BlockHighLightModule extends Module {
    private static final BlockHighLightModule instance = new BlockHighLightModule();
    private final ModeSetting colorMode = new ModeSetting("Цвет").value("Тема").values("Тема");
    private final BooleanSetting filled = new BooleanSetting("Заливка").value((Boolean) true);

    public BlockHighLightModule() {
        addSettings(this.colorMode, this.filled);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener render3DEvent = Render3DEvent.getInstance().subscribe(new Listener(event -> {
            class_2338 pos;
            Color awtColor;
            class_3965 class_3965Var = mc.field_1765;
            if (class_3965Var instanceof class_3965) {
                class_3965 result = class_3965Var;
                if (result.method_17783() == class_239.class_240.field_1332 && (pos = result.method_17777()) != null) {
                    if (this.colorMode.getValue().equals("Тема")) {
                        awtColor = UIColors.primary();
                    } else {
                        awtColor = Color.BLACK;
                    }
                    class_265 shape = mc.field_1687.method_8320(pos).method_26218(mc.field_1687, pos);
                    class_238 box = shape.method_1110() ? new class_238(pos) : shape.method_1107().method_996(pos);
                    if (this.filled.getValue().booleanValue()) {
                        Color fillColor = new Color(awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue(), 50);
                        RenderUtil.BOX.drawBox((float) box.field_1323, (float) box.field_1322, (float) box.field_1321, (float) box.field_1320, (float) box.field_1325, (float) box.field_1324, 1.5f, fillColor, BoxRender.Render.FILL, 0.0f);
                        float topMinY = ((float) box.field_1325) + 0.002f;
                        float topMaxY = topMinY + 0.035f;
                        Color topFillColor = new Color(awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue(), 220);
                        RenderUtil.BOX.drawBox((float) box.field_1323, topMinY, (float) box.field_1321, (float) box.field_1320, topMaxY, (float) box.field_1324, 1.0f, topFillColor, BoxRender.Render.FILL, 0.0f);
                        RenderUtil.BOX.drawBox((float) box.field_1323, (float) box.field_1322, (float) box.field_1321, (float) box.field_1320, (float) box.field_1325, (float) box.field_1324, 1.5f, awtColor, BoxRender.Render.OUTLINE, 0.0f);
                        return;
                    }
                    RenderUtil.BOX.drawBox((float) box.field_1323, (float) box.field_1322, (float) box.field_1321, (float) box.field_1320, (float) box.field_1325, (float) box.field_1324, 1.5f, awtColor, BoxRender.Render.OUTLINE, 0.0f);
                }
            }
        }));
        addEvents(render3DEvent);
    }

    public static BlockHighLightModule getInstance() {
        return instance;
    }
}

package sweetie.leonware.client.features.modules.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import lombok.Generated;
import net.minecraft.class_10142;
import net.minecraft.class_243;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_3532;
import org.joml.Matrix4f;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.color.UIColors;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/ChinaHatModule.class */
@ModuleRegister(name = "China Hat", category = Category.RENDER)
public class ChinaHatModule extends Module {
    private static final ChinaHatModule instance = new ChinaHatModule();
    private final SliderSetting height = new SliderSetting("Высота").value(Float.valueOf(0.3f)).range(0.1f, 1.0f).step(0.01f);
    private final SliderSetting radius = new SliderSetting("Радиус").value(Float.valueOf(0.6f)).range(0.1f, 1.5f).step(0.05f);
    private final SliderSetting segments = new SliderSetting("Сегменты").value(Float.valueOf(30.0f)).range(10.0f, 60.0f).step(1.0f);
    private final SliderSetting alpha = new SliderSetting("Прозрачность").value(Float.valueOf(120.0f)).range(0.0f, 255.0f).step(1.0f);
    private final BooleanSetting firstPerson = new BooleanSetting("От 1 лица").value((Boolean) false);
    private final SliderSetting yOffset = new SliderSetting("Y-Смещение").value(Float.valueOf(0.0f)).range(-0.5f, 0.5f).step(0.01f);

    @Generated
    public static ChinaHatModule getInstance() {
        return instance;
    }

    public ChinaHatModule() {
        addSettings(this.height, this.radius, this.segments, this.alpha, this.firstPerson, this.yOffset);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener render3DEvent = Render3DEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1724 == null || mc.field_1687 == null) {
                return;
            }
            if (this.firstPerson.getValue().booleanValue() || !mc.field_1690.method_31044().method_31034()) {
                float partialTicks = event.partialTicks();
                class_243 pos = mc.field_1724.method_30950(partialTicks);
                double viewX = mc.field_1773.method_19418().method_19326().field_1352;
                double viewY = mc.field_1773.method_19418().method_19326().field_1351;
                double viewZ = mc.field_1773.method_19418().method_19326().field_1350;
                double x = pos.field_1352 - viewX;
                double y = (pos.field_1351 - viewY) + (mc.field_1724.method_5715() ? 1.5d : 1.9d) + ((double) this.yOffset.getValue().floatValue());
                double z = pos.field_1350 - viewZ;
                float yaw = class_3532.method_17821(partialTicks, mc.field_1724.field_6220, mc.field_1724.field_6283);
                Matrix4f matrix = event.matrixStack().method_23760().method_23761();
                int a = this.alpha.getValue().intValue();
                int timeOffset = (int) (System.currentTimeMillis() / 10);
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.disableCull();
                RenderSystem.setShader(class_10142.field_53876);
                class_289 tessellator = class_289.method_1348();
                class_287 bufferBuilder = tessellator.method_60827(class_293.class_5596.field_27381, class_290.field_1576);
                Color topCol = UIColors.gradient(timeOffset, a);
                bufferBuilder.method_22918(matrix, (float) x, (float) (y + ((double) this.height.getValue().floatValue())), (float) z).method_1336(topCol.getRed(), topCol.getGreen(), topCol.getBlue(), topCol.getAlpha());
                float r = this.radius.getValue().floatValue();
                int segs = this.segments.getValue().intValue();
                for (int i = 0; i <= segs; i++) {
                    double angle = Math.toRadians(((((double) i) * 360.0d) / ((double) segs)) - ((double) yaw));
                    float dx = (float) (Math.sin(angle) * ((double) r));
                    float dz = (float) (Math.cos(angle) * ((double) r));
                    Color botCol = UIColors.gradient(timeOffset + 20, a);
                    bufferBuilder.method_22918(matrix, (float) (x + ((double) dx)), (float) y, (float) (z + ((double) dz))).method_1336(botCol.getRed(), botCol.getGreen(), botCol.getBlue(), botCol.getAlpha());
                }
                class_286.method_43433(bufferBuilder.method_60800());
                class_287 lineBuffer = tessellator.method_60827(class_293.class_5596.field_29345, class_290.field_1576);
                for (int i2 = 0; i2 <= segs; i2++) {
                    double angle2 = Math.toRadians(((((double) i2) * 360.0d) / ((double) segs)) - ((double) yaw));
                    float dx2 = (float) (Math.sin(angle2) * ((double) r));
                    float dz2 = (float) (Math.cos(angle2) * ((double) r));
                    Color edgeColor = UIColors.gradient(timeOffset + (i2 * 2), 255);
                    lineBuffer.method_22918(matrix, (float) (x + ((double) dx2)), (float) y, (float) (z + ((double) dz2))).method_1336(edgeColor.getRed(), edgeColor.getGreen(), edgeColor.getBlue(), 255);
                }
                class_286.method_43433(lineBuffer.method_60800());
                RenderSystem.enableCull();
                RenderSystem.disableBlend();
            }
        }));
        addEvents(render3DEvent);
    }
}

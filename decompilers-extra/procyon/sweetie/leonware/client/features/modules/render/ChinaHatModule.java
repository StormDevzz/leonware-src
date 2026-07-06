// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import java.awt.Color;
import net.minecraft.class_287;
import org.joml.Matrix4f;
import net.minecraft.class_243;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import net.minecraft.class_286;
import sweetie.leonware.api.utils.color.UIColors;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_289;
import net.minecraft.class_10142;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_3532;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "China Hat", category = Category.RENDER)
public class ChinaHatModule extends Module
{
    private static final ChinaHatModule instance;
    private final SliderSetting height;
    private final SliderSetting radius;
    private final SliderSetting segments;
    private final SliderSetting alpha;
    private final BooleanSetting firstPerson;
    private final SliderSetting yOffset;
    
    public ChinaHatModule() {
        this.height = new SliderSetting("\u0412\u044b\u0441\u043e\u0442\u0430").value(0.3f).range(0.1f, 1.0f).step(0.01f);
        this.radius = new SliderSetting("\u0420\u0430\u0434\u0438\u0443\u0441").value(0.6f).range(0.1f, 1.5f).step(0.05f);
        this.segments = new SliderSetting("\u0421\u0435\u0433\u043c\u0435\u043d\u0442\u044b").value(30.0f).range(10.0f, 60.0f).step(1.0f);
        this.alpha = new SliderSetting("\u041f\u0440\u043e\u0437\u0440\u0430\u0447\u043d\u043e\u0441\u0442\u044c").value(120.0f).range(0.0f, 255.0f).step(1.0f);
        this.firstPerson = new BooleanSetting("\u041e\u0442 1 \u043b\u0438\u0446\u0430").value(false);
        this.yOffset = new SliderSetting("Y-\u0421\u043c\u0435\u0449\u0435\u043d\u0438\u0435").value(0.0f).range(-0.5f, 0.5f).step(0.01f);
        this.addSettings(this.height, this.radius, this.segments, this.alpha, this.firstPerson, this.yOffset);
    }
    
    @Override
    public void onEvent() {
        final EventListener render3DEvent = Render3DEvent.getInstance().subscribe(new Listener<Render3DEvent.Render3DEventData>(event -> {
            if (ChinaHatModule.mc.field_1724 == null || ChinaHatModule.mc.field_1687 == null) {
                return;
            }
            else if (!this.firstPerson.getValue() && ChinaHatModule.mc.field_1690.method_31044().method_31034()) {
                return;
            }
            else {
                final float partialTicks = event.partialTicks();
                final class_243 pos = ChinaHatModule.mc.field_1724.method_30950(partialTicks);
                final double viewX = ChinaHatModule.mc.field_1773.method_19418().method_19326().field_1352;
                final double viewY = ChinaHatModule.mc.field_1773.method_19418().method_19326().field_1351;
                final double viewZ = ChinaHatModule.mc.field_1773.method_19418().method_19326().field_1350;
                final double x = pos.field_1352 - viewX;
                final double y = pos.field_1351 - viewY + (ChinaHatModule.mc.field_1724.method_5715() ? 1.5 : 1.9) + this.yOffset.getValue();
                final double z = pos.field_1350 - viewZ;
                final float yaw = class_3532.method_17821(partialTicks, ChinaHatModule.mc.field_1724.field_6220, ChinaHatModule.mc.field_1724.field_6283);
                final Matrix4f matrix = event.matrixStack().method_23760().method_23761();
                final int a = this.alpha.getValue().intValue();
                final int timeOffset = (int)(System.currentTimeMillis() / 10L);
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.disableCull();
                RenderSystem.setShader(class_10142.field_53876);
                final class_289 tessellator = class_289.method_1348();
                final class_287 bufferBuilder = tessellator.method_60827(class_293.class_5596.field_27381, class_290.field_1576);
                final Color topCol = UIColors.gradient(timeOffset, a);
                bufferBuilder.method_22918(matrix, (float)x, (float)(y + this.height.getValue()), (float)z).method_1336(topCol.getRed(), topCol.getGreen(), topCol.getBlue(), topCol.getAlpha());
                final float r = this.radius.getValue();
                final int segs = this.segments.getValue().intValue();
                for (int i = 0; i <= segs; ++i) {
                    final double angle = Math.toRadians(i * 360.0 / segs - yaw);
                    final float dx = (float)(Math.sin(angle) * r);
                    final float dz = (float)(Math.cos(angle) * r);
                    final Color botCol = UIColors.gradient(timeOffset + 20, a);
                    bufferBuilder.method_22918(matrix, (float)(x + dx), (float)y, (float)(z + dz)).method_1336(botCol.getRed(), botCol.getGreen(), botCol.getBlue(), botCol.getAlpha());
                }
                class_286.method_43433(bufferBuilder.method_60800());
                final class_287 lineBuffer = tessellator.method_60827(class_293.class_5596.field_29345, class_290.field_1576);
                for (int j = 0; j <= segs; ++j) {
                    final double angle2 = Math.toRadians(j * 360.0 / segs - yaw);
                    final float dx2 = (float)(Math.sin(angle2) * r);
                    final float dz2 = (float)(Math.cos(angle2) * r);
                    final Color edgeColor = UIColors.gradient(timeOffset + j * 2, 255);
                    lineBuffer.method_22918(matrix, (float)(x + dx2), (float)y, (float)(z + dz2)).method_1336(edgeColor.getRed(), edgeColor.getGreen(), edgeColor.getBlue(), 255);
                }
                class_286.method_43433(lineBuffer.method_60800());
                RenderSystem.enableCull();
                RenderSystem.disableBlend();
                return;
            }
        }));
        this.addEvents(render3DEvent);
    }
    
    @Generated
    public static ChinaHatModule getInstance() {
        return ChinaHatModule.instance;
    }
    
    static {
        instance = new ChinaHatModule();
    }
}

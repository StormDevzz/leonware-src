package sweetie.leonware.client.features.modules.render;

import java.awt.Color;
import java.util.Objects;
import lombok.Generated;
import net.minecraft.class_1297;
import net.minecraft.class_1694;
import net.minecraft.class_238;
import net.minecraft.class_2531;
import net.minecraft.class_2586;
import net.minecraft.class_2595;
import net.minecraft.class_2601;
import net.minecraft.class_2608;
import net.minecraft.class_2611;
import net.minecraft.class_2614;
import net.minecraft.class_2627;
import net.minecraft.class_265;
import net.minecraft.class_2818;
import net.minecraft.class_3719;
import net.minecraft.class_3720;
import net.minecraft.class_3723;
import net.minecraft.class_3866;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ColorSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.display.BoxRender;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/StorageESPModule.class */
@ModuleRegister(name = "Storage ESP", category = Category.RENDER)
public class StorageESPModule extends Module {
    private static final StorageESPModule instance = new StorageESPModule();
    private final BooleanSetting chestSel = new BooleanSetting("Сундук").value((Boolean) true);
    private final BooleanSetting trappedSel = new BooleanSetting("Сундук-ловушка").value((Boolean) true);
    private final BooleanSetting enderSel = new BooleanSetting("Эндер-сундук").value((Boolean) true);
    private final BooleanSetting shulkerSel = new BooleanSetting("Шалкер").value((Boolean) true);
    private final BooleanSetting barrelSel = new BooleanSetting("Бочка").value((Boolean) true);
    private final BooleanSetting minecartSel = new BooleanSetting("Вагонетка с сундуком").value((Boolean) true);
    private final BooleanSetting furnaceSel = new BooleanSetting("Печь").value((Boolean) false);
    private final BooleanSetting blastSel = new BooleanSetting("Доменная печь").value((Boolean) false);
    private final BooleanSetting smokerSel = new BooleanSetting("Коптильня").value((Boolean) false);
    private final BooleanSetting hopperSel = new BooleanSetting("Воронка").value((Boolean) false);
    private final BooleanSetting dispenserSel = new BooleanSetting("Раздатчик").value((Boolean) false);
    private final BooleanSetting dropperSel = new BooleanSetting("Сбрасыватель").value((Boolean) false);
    private final MultiBooleanSetting storages = new MultiBooleanSetting("Контейнеры").value(this.chestSel, this.trappedSel, this.enderSel, this.shulkerSel, this.barrelSel, this.minecartSel, this.furnaceSel, this.blastSel, this.smokerSel, this.hopperSel, this.dispenserSel, this.dropperSel);
    private final ColorSetting chestColor;
    private final ColorSetting trappedColor;
    private final ColorSetting enderColor;
    private final ColorSetting shulkerColor;
    private final ColorSetting barrelColor;
    private final ColorSetting minecartColor;
    private final ColorSetting furnaceColor;
    private final ColorSetting blastColor;
    private final ColorSetting smokerColor;
    private final ColorSetting hopperColor;
    private final ColorSetting dispenserColor;
    private final ColorSetting dropperColor;
    private final BooleanSetting filled;
    private final BooleanSetting outline;

    @Generated
    public static StorageESPModule getInstance() {
        return instance;
    }

    /* JADX WARN: Type inference failed for: r1v28, types: [sweetie.leonware.api.module.setting.ColorSetting] */
    /* JADX WARN: Type inference failed for: r1v31, types: [sweetie.leonware.api.module.setting.ColorSetting] */
    /* JADX WARN: Type inference failed for: r1v34, types: [sweetie.leonware.api.module.setting.ColorSetting] */
    /* JADX WARN: Type inference failed for: r1v37, types: [sweetie.leonware.api.module.setting.ColorSetting] */
    /* JADX WARN: Type inference failed for: r1v40, types: [sweetie.leonware.api.module.setting.ColorSetting] */
    /* JADX WARN: Type inference failed for: r1v43, types: [sweetie.leonware.api.module.setting.ColorSetting] */
    /* JADX WARN: Type inference failed for: r1v46, types: [sweetie.leonware.api.module.setting.ColorSetting] */
    /* JADX WARN: Type inference failed for: r1v49, types: [sweetie.leonware.api.module.setting.ColorSetting] */
    /* JADX WARN: Type inference failed for: r1v52, types: [sweetie.leonware.api.module.setting.ColorSetting] */
    /* JADX WARN: Type inference failed for: r1v55, types: [sweetie.leonware.api.module.setting.ColorSetting] */
    /* JADX WARN: Type inference failed for: r1v58, types: [sweetie.leonware.api.module.setting.ColorSetting] */
    /* JADX WARN: Type inference failed for: r1v61, types: [sweetie.leonware.api.module.setting.ColorSetting] */
    public StorageESPModule() {
        ColorSetting colorSettingValue = new ColorSetting("Цвет сундука").value(new Color(255, 165, 0, 200));
        BooleanSetting booleanSetting = this.chestSel;
        Objects.requireNonNull(booleanSetting);
        this.chestColor = colorSettingValue.setVisible(booleanSetting::getValue);
        ColorSetting colorSettingValue2 = new ColorSetting("Цвет с-ловушки").value(new Color(255, 60, 60, 200));
        BooleanSetting booleanSetting2 = this.trappedSel;
        Objects.requireNonNull(booleanSetting2);
        this.trappedColor = colorSettingValue2.setVisible(booleanSetting2::getValue);
        ColorSetting colorSettingValue3 = new ColorSetting("Цвет эндер-сундука").value(new Color(110, 0, 200, 200));
        BooleanSetting booleanSetting3 = this.enderSel;
        Objects.requireNonNull(booleanSetting3);
        this.enderColor = colorSettingValue3.setVisible(booleanSetting3::getValue);
        ColorSetting colorSettingValue4 = new ColorSetting("Цвет шалкера").value(new Color(200, 100, 255, 200));
        BooleanSetting booleanSetting4 = this.shulkerSel;
        Objects.requireNonNull(booleanSetting4);
        this.shulkerColor = colorSettingValue4.setVisible(booleanSetting4::getValue);
        ColorSetting colorSettingValue5 = new ColorSetting("Цвет бочки").value(new Color(139, 90, 43, 200));
        BooleanSetting booleanSetting5 = this.barrelSel;
        Objects.requireNonNull(booleanSetting5);
        this.barrelColor = colorSettingValue5.setVisible(booleanSetting5::getValue);
        ColorSetting colorSettingValue6 = new ColorSetting("Цвет вагонетки").value(new Color(255, 230, 100, 200));
        BooleanSetting booleanSetting6 = this.minecartSel;
        Objects.requireNonNull(booleanSetting6);
        this.minecartColor = colorSettingValue6.setVisible(booleanSetting6::getValue);
        ColorSetting colorSettingValue7 = new ColorSetting("Цвет печи").value(new Color(190, 190, 190, 200));
        BooleanSetting booleanSetting7 = this.furnaceSel;
        Objects.requireNonNull(booleanSetting7);
        this.furnaceColor = colorSettingValue7.setVisible(booleanSetting7::getValue);
        ColorSetting colorSettingValue8 = new ColorSetting("Цвет доменной печи").value(new Color(80, 200, 255, 200));
        BooleanSetting booleanSetting8 = this.blastSel;
        Objects.requireNonNull(booleanSetting8);
        this.blastColor = colorSettingValue8.setVisible(booleanSetting8::getValue);
        ColorSetting colorSettingValue9 = new ColorSetting("Цвет коптильни").value(new Color(150, 150, 150, 200));
        BooleanSetting booleanSetting9 = this.smokerSel;
        Objects.requireNonNull(booleanSetting9);
        this.smokerColor = colorSettingValue9.setVisible(booleanSetting9::getValue);
        ColorSetting colorSettingValue10 = new ColorSetting("Цвет воронки").value(new Color(90, 90, 90, 200));
        BooleanSetting booleanSetting10 = this.hopperSel;
        Objects.requireNonNull(booleanSetting10);
        this.hopperColor = colorSettingValue10.setVisible(booleanSetting10::getValue);
        ColorSetting colorSettingValue11 = new ColorSetting("Цвет раздатчика").value(new Color(220, 220, 80, 200));
        BooleanSetting booleanSetting11 = this.dispenserSel;
        Objects.requireNonNull(booleanSetting11);
        this.dispenserColor = colorSettingValue11.setVisible(booleanSetting11::getValue);
        ColorSetting colorSettingValue12 = new ColorSetting("Цвет сбрасывателя").value(new Color(200, 100, 100, 200));
        BooleanSetting booleanSetting12 = this.dropperSel;
        Objects.requireNonNull(booleanSetting12);
        this.dropperColor = colorSettingValue12.setVisible(booleanSetting12::getValue);
        this.filled = new BooleanSetting("Заливка").value((Boolean) true);
        this.outline = new BooleanSetting("Контур").value((Boolean) true);
        addSettings(this.storages, this.chestColor, this.trappedColor, this.enderColor, this.shulkerColor, this.barrelColor, this.minecartColor, this.furnaceColor, this.blastColor, this.smokerColor, this.hopperColor, this.dispenserColor, this.dropperColor, this.filled, this.outline);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener render3DEvent = Render3DEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1687 == null || mc.field_1724 == null) {
                return;
            }
            if (this.minecartSel.getValue().booleanValue()) {
                for (class_1297 entity : mc.field_1687.method_18112()) {
                    if (entity instanceof class_1694) {
                        renderBox(entity.method_5829(), this.minecartColor.getValue());
                    }
                }
            }
            int renderDist = ((Integer) mc.field_1690.method_42503().method_41753()).intValue();
            int cx = mc.field_1724.method_24515().method_10263() >> 4;
            int cz = mc.field_1724.method_24515().method_10260() >> 4;
            for (int dx = -renderDist; dx <= renderDist; dx++) {
                for (int dz = -renderDist; dz <= renderDist; dz++) {
                    class_2818 chunk = mc.field_1687.method_8497(cx + dx, cz + dz);
                    chunk.method_12214().forEach((pos, be) -> {
                        Color color = resolveColor(be);
                        if (color == null) {
                            return;
                        }
                        class_265 shape = mc.field_1687.method_8320(pos).method_26218(mc.field_1687, pos);
                        class_238 box = shape.method_1110() ? new class_238(pos) : shape.method_1107().method_996(pos);
                        renderBox(box, color);
                    });
                }
            }
        }));
        addEvents(render3DEvent);
    }

    private Color resolveColor(class_2586 be) {
        if (be instanceof class_2608) {
            if (this.dropperSel.getValue().booleanValue()) {
                return this.dropperColor.getValue();
            }
            return null;
        }
        if (be instanceof class_2601) {
            if (this.dispenserSel.getValue().booleanValue()) {
                return this.dispenserColor.getValue();
            }
            return null;
        }
        if (be instanceof class_2627) {
            if (this.shulkerSel.getValue().booleanValue()) {
                return this.shulkerColor.getValue();
            }
            return null;
        }
        if (be instanceof class_2611) {
            if (this.enderSel.getValue().booleanValue()) {
                return this.enderColor.getValue();
            }
            return null;
        }
        if (be instanceof class_2595) {
            boolean isTrapped = be.method_11010().method_26204() instanceof class_2531;
            if (isTrapped) {
                if (this.trappedSel.getValue().booleanValue()) {
                    return this.trappedColor.getValue();
                }
                return null;
            }
            if (this.chestSel.getValue().booleanValue()) {
                return this.chestColor.getValue();
            }
            return null;
        }
        if (be instanceof class_3719) {
            if (this.barrelSel.getValue().booleanValue()) {
                return this.barrelColor.getValue();
            }
            return null;
        }
        if (be instanceof class_3720) {
            if (this.blastSel.getValue().booleanValue()) {
                return this.blastColor.getValue();
            }
            return null;
        }
        if (be instanceof class_3723) {
            if (this.smokerSel.getValue().booleanValue()) {
                return this.smokerColor.getValue();
            }
            return null;
        }
        if (be instanceof class_3866) {
            if (this.furnaceSel.getValue().booleanValue()) {
                return this.furnaceColor.getValue();
            }
            return null;
        }
        if ((be instanceof class_2614) && this.hopperSel.getValue().booleanValue()) {
            return this.hopperColor.getValue();
        }
        return null;
    }

    private void renderBox(class_238 box, Color color) {
        float x1 = (float) box.field_1323;
        float y1 = (float) box.field_1322;
        float z1 = (float) box.field_1321;
        float x2 = (float) box.field_1320;
        float y2 = (float) box.field_1325;
        float z2 = (float) box.field_1324;
        if (this.filled.getValue().booleanValue()) {
            Color fill = new Color(color.getRed(), color.getGreen(), color.getBlue(), 45);
            RenderUtil.BOX.drawBox(x1, y1, z1, x2, y2, z2, 1.0f, fill, BoxRender.Render.FILL, 0.0f);
        }
        if (this.outline.getValue().booleanValue()) {
            RenderUtil.BOX.drawBox(x1, y1, z1, x2, y2, z2, 1.5f, color, BoxRender.Render.OUTLINE, 0.0f);
        }
    }
}

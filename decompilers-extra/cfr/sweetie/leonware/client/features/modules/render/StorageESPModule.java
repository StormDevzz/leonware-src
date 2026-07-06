/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1297
 *  net.minecraft.class_1694
 *  net.minecraft.class_1922
 *  net.minecraft.class_238
 *  net.minecraft.class_2531
 *  net.minecraft.class_2586
 *  net.minecraft.class_2595
 *  net.minecraft.class_2601
 *  net.minecraft.class_2608
 *  net.minecraft.class_2611
 *  net.minecraft.class_2614
 *  net.minecraft.class_2627
 *  net.minecraft.class_265
 *  net.minecraft.class_2818
 *  net.minecraft.class_3719
 *  net.minecraft.class_3720
 *  net.minecraft.class_3723
 *  net.minecraft.class_3866
 */
package sweetie.leonware.client.features.modules.render;

import java.awt.Color;
import lombok.Generated;
import net.minecraft.class_1297;
import net.minecraft.class_1694;
import net.minecraft.class_1922;
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

@ModuleRegister(name="Storage ESP", category=Category.RENDER)
public class StorageESPModule
extends Module {
    private static final StorageESPModule instance = new StorageESPModule();
    private final BooleanSetting chestSel = new BooleanSetting("\u0421\u0443\u043d\u0434\u0443\u043a").value(true);
    private final BooleanSetting trappedSel = new BooleanSetting("\u0421\u0443\u043d\u0434\u0443\u043a-\u043b\u043e\u0432\u0443\u0448\u043a\u0430").value(true);
    private final BooleanSetting enderSel = new BooleanSetting("\u042d\u043d\u0434\u0435\u0440-\u0441\u0443\u043d\u0434\u0443\u043a").value(true);
    private final BooleanSetting shulkerSel = new BooleanSetting("\u0428\u0430\u043b\u043a\u0435\u0440").value(true);
    private final BooleanSetting barrelSel = new BooleanSetting("\u0411\u043e\u0447\u043a\u0430").value(true);
    private final BooleanSetting minecartSel = new BooleanSetting("\u0412\u0430\u0433\u043e\u043d\u0435\u0442\u043a\u0430 \u0441 \u0441\u0443\u043d\u0434\u0443\u043a\u043e\u043c").value(true);
    private final BooleanSetting furnaceSel = new BooleanSetting("\u041f\u0435\u0447\u044c").value(false);
    private final BooleanSetting blastSel = new BooleanSetting("\u0414\u043e\u043c\u0435\u043d\u043d\u0430\u044f \u043f\u0435\u0447\u044c").value(false);
    private final BooleanSetting smokerSel = new BooleanSetting("\u041a\u043e\u043f\u0442\u0438\u043b\u044c\u043d\u044f").value(false);
    private final BooleanSetting hopperSel = new BooleanSetting("\u0412\u043e\u0440\u043e\u043d\u043a\u0430").value(false);
    private final BooleanSetting dispenserSel = new BooleanSetting("\u0420\u0430\u0437\u0434\u0430\u0442\u0447\u0438\u043a").value(false);
    private final BooleanSetting dropperSel = new BooleanSetting("\u0421\u0431\u0440\u0430\u0441\u044b\u0432\u0430\u0442\u0435\u043b\u044c").value(false);
    private final MultiBooleanSetting storages = new MultiBooleanSetting("\u041a\u043e\u043d\u0442\u0435\u0439\u043d\u0435\u0440\u044b").value(this.chestSel, this.trappedSel, this.enderSel, this.shulkerSel, this.barrelSel, this.minecartSel, this.furnaceSel, this.blastSel, this.smokerSel, this.hopperSel, this.dispenserSel, this.dropperSel);
    private final ColorSetting chestColor = new ColorSetting("\u0426\u0432\u0435\u0442 \u0441\u0443\u043d\u0434\u0443\u043a\u0430").value(new Color(255, 165, 0, 200)).setVisible(this.chestSel::getValue);
    private final ColorSetting trappedColor = new ColorSetting("\u0426\u0432\u0435\u0442 \u0441-\u043b\u043e\u0432\u0443\u0448\u043a\u0438").value(new Color(255, 60, 60, 200)).setVisible(this.trappedSel::getValue);
    private final ColorSetting enderColor = new ColorSetting("\u0426\u0432\u0435\u0442 \u044d\u043d\u0434\u0435\u0440-\u0441\u0443\u043d\u0434\u0443\u043a\u0430").value(new Color(110, 0, 200, 200)).setVisible(this.enderSel::getValue);
    private final ColorSetting shulkerColor = new ColorSetting("\u0426\u0432\u0435\u0442 \u0448\u0430\u043b\u043a\u0435\u0440\u0430").value(new Color(200, 100, 255, 200)).setVisible(this.shulkerSel::getValue);
    private final ColorSetting barrelColor = new ColorSetting("\u0426\u0432\u0435\u0442 \u0431\u043e\u0447\u043a\u0438").value(new Color(139, 90, 43, 200)).setVisible(this.barrelSel::getValue);
    private final ColorSetting minecartColor = new ColorSetting("\u0426\u0432\u0435\u0442 \u0432\u0430\u0433\u043e\u043d\u0435\u0442\u043a\u0438").value(new Color(255, 230, 100, 200)).setVisible(this.minecartSel::getValue);
    private final ColorSetting furnaceColor = new ColorSetting("\u0426\u0432\u0435\u0442 \u043f\u0435\u0447\u0438").value(new Color(190, 190, 190, 200)).setVisible(this.furnaceSel::getValue);
    private final ColorSetting blastColor = new ColorSetting("\u0426\u0432\u0435\u0442 \u0434\u043e\u043c\u0435\u043d\u043d\u043e\u0439 \u043f\u0435\u0447\u0438").value(new Color(80, 200, 255, 200)).setVisible(this.blastSel::getValue);
    private final ColorSetting smokerColor = new ColorSetting("\u0426\u0432\u0435\u0442 \u043a\u043e\u043f\u0442\u0438\u043b\u044c\u043d\u0438").value(new Color(150, 150, 150, 200)).setVisible(this.smokerSel::getValue);
    private final ColorSetting hopperColor = new ColorSetting("\u0426\u0432\u0435\u0442 \u0432\u043e\u0440\u043e\u043d\u043a\u0438").value(new Color(90, 90, 90, 200)).setVisible(this.hopperSel::getValue);
    private final ColorSetting dispenserColor = new ColorSetting("\u0426\u0432\u0435\u0442 \u0440\u0430\u0437\u0434\u0430\u0442\u0447\u0438\u043a\u0430").value(new Color(220, 220, 80, 200)).setVisible(this.dispenserSel::getValue);
    private final ColorSetting dropperColor = new ColorSetting("\u0426\u0432\u0435\u0442 \u0441\u0431\u0440\u0430\u0441\u044b\u0432\u0430\u0442\u0435\u043b\u044f").value(new Color(200, 100, 100, 200)).setVisible(this.dropperSel::getValue);
    private final BooleanSetting filled = new BooleanSetting("\u0417\u0430\u043b\u0438\u0432\u043a\u0430").value(true);
    private final BooleanSetting outline = new BooleanSetting("\u041a\u043e\u043d\u0442\u0443\u0440").value(true);

    public StorageESPModule() {
        this.addSettings(this.storages, this.chestColor, this.trappedColor, this.enderColor, this.shulkerColor, this.barrelColor, this.minecartColor, this.furnaceColor, this.blastColor, this.smokerColor, this.hopperColor, this.dispenserColor, this.dropperColor, this.filled, this.outline);
    }

    @Override
    public void onEvent() {
        EventListener render3DEvent = Render3DEvent.getInstance().subscribe(new Listener<Render3DEvent.Render3DEventData>(event -> {
            if (StorageESPModule.mc.field_1687 == null || StorageESPModule.mc.field_1724 == null) {
                return;
            }
            if (((Boolean)this.minecartSel.getValue()).booleanValue()) {
                for (class_1297 entity : StorageESPModule.mc.field_1687.method_18112()) {
                    if (!(entity instanceof class_1694)) continue;
                    this.renderBox(entity.method_5829(), (Color)this.minecartColor.getValue());
                }
            }
            int renderDist = (Integer)StorageESPModule.mc.field_1690.method_42503().method_41753();
            int cx = StorageESPModule.mc.field_1724.method_24515().method_10263() >> 4;
            int cz = StorageESPModule.mc.field_1724.method_24515().method_10260() >> 4;
            for (int dx = -renderDist; dx <= renderDist; ++dx) {
                for (int dz = -renderDist; dz <= renderDist; ++dz) {
                    class_2818 chunk = StorageESPModule.mc.field_1687.method_8497(cx + dx, cz + dz);
                    chunk.method_12214().forEach((pos, be) -> {
                        Color color = this.resolveColor((class_2586)be);
                        if (color == null) {
                            return;
                        }
                        class_265 shape = StorageESPModule.mc.field_1687.method_8320(pos).method_26218((class_1922)StorageESPModule.mc.field_1687, pos);
                        class_238 box = shape.method_1110() ? new class_238(pos) : shape.method_1107().method_996(pos);
                        this.renderBox(box, color);
                    });
                }
            }
        }));
        this.addEvents(render3DEvent);
    }

    private Color resolveColor(class_2586 be) {
        if (be instanceof class_2608) {
            return (Boolean)this.dropperSel.getValue() != false ? (Color)this.dropperColor.getValue() : null;
        }
        if (be instanceof class_2601) {
            return (Boolean)this.dispenserSel.getValue() != false ? (Color)this.dispenserColor.getValue() : null;
        }
        if (be instanceof class_2627) {
            return (Boolean)this.shulkerSel.getValue() != false ? (Color)this.shulkerColor.getValue() : null;
        }
        if (be instanceof class_2611) {
            return (Boolean)this.enderSel.getValue() != false ? (Color)this.enderColor.getValue() : null;
        }
        if (be instanceof class_2595) {
            boolean isTrapped = be.method_11010().method_26204() instanceof class_2531;
            if (isTrapped) {
                return (Boolean)this.trappedSel.getValue() != false ? (Color)this.trappedColor.getValue() : null;
            }
            return (Boolean)this.chestSel.getValue() != false ? (Color)this.chestColor.getValue() : null;
        }
        if (be instanceof class_3719) {
            return (Boolean)this.barrelSel.getValue() != false ? (Color)this.barrelColor.getValue() : null;
        }
        if (be instanceof class_3720) {
            return (Boolean)this.blastSel.getValue() != false ? (Color)this.blastColor.getValue() : null;
        }
        if (be instanceof class_3723) {
            return (Boolean)this.smokerSel.getValue() != false ? (Color)this.smokerColor.getValue() : null;
        }
        if (be instanceof class_3866) {
            return (Boolean)this.furnaceSel.getValue() != false ? (Color)this.furnaceColor.getValue() : null;
        }
        if (be instanceof class_2614) {
            return (Boolean)this.hopperSel.getValue() != false ? (Color)this.hopperColor.getValue() : null;
        }
        return null;
    }

    private void renderBox(class_238 box, Color color) {
        float x1 = (float)box.field_1323;
        float y1 = (float)box.field_1322;
        float z1 = (float)box.field_1321;
        float x2 = (float)box.field_1320;
        float y2 = (float)box.field_1325;
        float z2 = (float)box.field_1324;
        if (((Boolean)this.filled.getValue()).booleanValue()) {
            Color fill = new Color(color.getRed(), color.getGreen(), color.getBlue(), 45);
            RenderUtil.BOX.drawBox(x1, y1, z1, x2, y2, z2, 1.0f, fill, BoxRender.Render.FILL, 0.0f);
        }
        if (((Boolean)this.outline.getValue()).booleanValue()) {
            RenderUtil.BOX.drawBox(x1, y1, z1, x2, y2, z2, 1.5f, color, BoxRender.Render.OUTLINE, 0.0f);
        }
    }

    @Generated
    public static StorageESPModule getInstance() {
        return instance;
    }
}


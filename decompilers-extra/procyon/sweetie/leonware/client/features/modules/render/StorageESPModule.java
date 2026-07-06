// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render;

import net.minecraft.class_2338;
import lombok.Generated;
import sweetie.leonware.api.utils.render.display.BoxRender;
import sweetie.leonware.api.utils.render.RenderUtil;
import net.minecraft.class_2614;
import net.minecraft.class_3866;
import net.minecraft.class_3723;
import net.minecraft.class_3720;
import net.minecraft.class_3719;
import net.minecraft.class_2531;
import net.minecraft.class_2595;
import net.minecraft.class_2611;
import net.minecraft.class_2627;
import net.minecraft.class_2601;
import net.minecraft.class_2608;
import net.minecraft.class_2586;
import net.minecraft.class_2818;
import java.util.Iterator;
import net.minecraft.class_265;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import net.minecraft.class_238;
import net.minecraft.class_1922;
import net.minecraft.class_1694;
import net.minecraft.class_1297;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.module.setting.Setting;
import java.util.function.Supplier;
import java.util.Objects;
import java.awt.Color;
import sweetie.leonware.api.module.setting.ColorSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Storage ESP", category = Category.RENDER)
public class StorageESPModule extends Module
{
    private static final StorageESPModule instance;
    private final BooleanSetting chestSel;
    private final BooleanSetting trappedSel;
    private final BooleanSetting enderSel;
    private final BooleanSetting shulkerSel;
    private final BooleanSetting barrelSel;
    private final BooleanSetting minecartSel;
    private final BooleanSetting furnaceSel;
    private final BooleanSetting blastSel;
    private final BooleanSetting smokerSel;
    private final BooleanSetting hopperSel;
    private final BooleanSetting dispenserSel;
    private final BooleanSetting dropperSel;
    private final MultiBooleanSetting storages;
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
    
    public StorageESPModule() {
        this.chestSel = new BooleanSetting("\u0421\u0443\u043d\u0434\u0443\u043a").value(true);
        this.trappedSel = new BooleanSetting("\u0421\u0443\u043d\u0434\u0443\u043a-\u043b\u043e\u0432\u0443\u0448\u043a\u0430").value(true);
        this.enderSel = new BooleanSetting("\u042d\u043d\u0434\u0435\u0440-\u0441\u0443\u043d\u0434\u0443\u043a").value(true);
        this.shulkerSel = new BooleanSetting("\u0428\u0430\u043b\u043a\u0435\u0440").value(true);
        this.barrelSel = new BooleanSetting("\u0411\u043e\u0447\u043a\u0430").value(true);
        this.minecartSel = new BooleanSetting("\u0412\u0430\u0433\u043e\u043d\u0435\u0442\u043a\u0430 \u0441 \u0441\u0443\u043d\u0434\u0443\u043a\u043e\u043c").value(true);
        this.furnaceSel = new BooleanSetting("\u041f\u0435\u0447\u044c").value(false);
        this.blastSel = new BooleanSetting("\u0414\u043e\u043c\u0435\u043d\u043d\u0430\u044f \u043f\u0435\u0447\u044c").value(false);
        this.smokerSel = new BooleanSetting("\u041a\u043e\u043f\u0442\u0438\u043b\u044c\u043d\u044f").value(false);
        this.hopperSel = new BooleanSetting("\u0412\u043e\u0440\u043e\u043d\u043a\u0430").value(false);
        this.dispenserSel = new BooleanSetting("\u0420\u0430\u0437\u0434\u0430\u0442\u0447\u0438\u043a").value(false);
        this.dropperSel = new BooleanSetting("\u0421\u0431\u0440\u0430\u0441\u044b\u0432\u0430\u0442\u0435\u043b\u044c").value(false);
        this.storages = new MultiBooleanSetting("\u041a\u043e\u043d\u0442\u0435\u0439\u043d\u0435\u0440\u044b").value(this.chestSel, this.trappedSel, this.enderSel, this.shulkerSel, this.barrelSel, this.minecartSel, this.furnaceSel, this.blastSel, this.smokerSel, this.hopperSel, this.dispenserSel, this.dropperSel);
        final ColorSetting value = new ColorSetting("\u0426\u0432\u0435\u0442 \u0441\u0443\u043d\u0434\u0443\u043a\u0430").value(new Color(255, 165, 0, 200));
        final BooleanSetting chestSel = this.chestSel;
        Objects.requireNonNull(chestSel);
        this.chestColor = value.setVisible((Supplier<Boolean>)chestSel::getValue);
        final ColorSetting value2 = new ColorSetting("\u0426\u0432\u0435\u0442 \u0441-\u043b\u043e\u0432\u0443\u0448\u043a\u0438").value(new Color(255, 60, 60, 200));
        final BooleanSetting trappedSel = this.trappedSel;
        Objects.requireNonNull(trappedSel);
        this.trappedColor = value2.setVisible((Supplier<Boolean>)trappedSel::getValue);
        final ColorSetting value3 = new ColorSetting("\u0426\u0432\u0435\u0442 \u044d\u043d\u0434\u0435\u0440-\u0441\u0443\u043d\u0434\u0443\u043a\u0430").value(new Color(110, 0, 200, 200));
        final BooleanSetting enderSel = this.enderSel;
        Objects.requireNonNull(enderSel);
        this.enderColor = value3.setVisible((Supplier<Boolean>)enderSel::getValue);
        final ColorSetting value4 = new ColorSetting("\u0426\u0432\u0435\u0442 \u0448\u0430\u043b\u043a\u0435\u0440\u0430").value(new Color(200, 100, 255, 200));
        final BooleanSetting shulkerSel = this.shulkerSel;
        Objects.requireNonNull(shulkerSel);
        this.shulkerColor = value4.setVisible((Supplier<Boolean>)shulkerSel::getValue);
        final ColorSetting value5 = new ColorSetting("\u0426\u0432\u0435\u0442 \u0431\u043e\u0447\u043a\u0438").value(new Color(139, 90, 43, 200));
        final BooleanSetting barrelSel = this.barrelSel;
        Objects.requireNonNull(barrelSel);
        this.barrelColor = value5.setVisible((Supplier<Boolean>)barrelSel::getValue);
        final ColorSetting value6 = new ColorSetting("\u0426\u0432\u0435\u0442 \u0432\u0430\u0433\u043e\u043d\u0435\u0442\u043a\u0438").value(new Color(255, 230, 100, 200));
        final BooleanSetting minecartSel = this.minecartSel;
        Objects.requireNonNull(minecartSel);
        this.minecartColor = value6.setVisible((Supplier<Boolean>)minecartSel::getValue);
        final ColorSetting value7 = new ColorSetting("\u0426\u0432\u0435\u0442 \u043f\u0435\u0447\u0438").value(new Color(190, 190, 190, 200));
        final BooleanSetting furnaceSel = this.furnaceSel;
        Objects.requireNonNull(furnaceSel);
        this.furnaceColor = value7.setVisible((Supplier<Boolean>)furnaceSel::getValue);
        final ColorSetting value8 = new ColorSetting("\u0426\u0432\u0435\u0442 \u0434\u043e\u043c\u0435\u043d\u043d\u043e\u0439 \u043f\u0435\u0447\u0438").value(new Color(80, 200, 255, 200));
        final BooleanSetting blastSel = this.blastSel;
        Objects.requireNonNull(blastSel);
        this.blastColor = value8.setVisible((Supplier<Boolean>)blastSel::getValue);
        final ColorSetting value9 = new ColorSetting("\u0426\u0432\u0435\u0442 \u043a\u043e\u043f\u0442\u0438\u043b\u044c\u043d\u0438").value(new Color(150, 150, 150, 200));
        final BooleanSetting smokerSel = this.smokerSel;
        Objects.requireNonNull(smokerSel);
        this.smokerColor = value9.setVisible((Supplier<Boolean>)smokerSel::getValue);
        final ColorSetting value10 = new ColorSetting("\u0426\u0432\u0435\u0442 \u0432\u043e\u0440\u043e\u043d\u043a\u0438").value(new Color(90, 90, 90, 200));
        final BooleanSetting hopperSel = this.hopperSel;
        Objects.requireNonNull(hopperSel);
        this.hopperColor = value10.setVisible((Supplier<Boolean>)hopperSel::getValue);
        final ColorSetting value11 = new ColorSetting("\u0426\u0432\u0435\u0442 \u0440\u0430\u0437\u0434\u0430\u0442\u0447\u0438\u043a\u0430").value(new Color(220, 220, 80, 200));
        final BooleanSetting dispenserSel = this.dispenserSel;
        Objects.requireNonNull(dispenserSel);
        this.dispenserColor = value11.setVisible((Supplier<Boolean>)dispenserSel::getValue);
        final ColorSetting value12 = new ColorSetting("\u0426\u0432\u0435\u0442 \u0441\u0431\u0440\u0430\u0441\u044b\u0432\u0430\u0442\u0435\u043b\u044f").value(new Color(200, 100, 100, 200));
        final BooleanSetting dropperSel = this.dropperSel;
        Objects.requireNonNull(dropperSel);
        this.dropperColor = value12.setVisible((Supplier<Boolean>)dropperSel::getValue);
        this.filled = new BooleanSetting("\u0417\u0430\u043b\u0438\u0432\u043a\u0430").value(true);
        this.outline = new BooleanSetting("\u041a\u043e\u043d\u0442\u0443\u0440").value(true);
        this.addSettings(this.storages, this.chestColor, this.trappedColor, this.enderColor, this.shulkerColor, this.barrelColor, this.minecartColor, this.furnaceColor, this.blastColor, this.smokerColor, this.hopperColor, this.dispenserColor, this.dropperColor, this.filled, this.outline);
    }
    
    @Override
    public void onEvent() {
        final EventListener render3DEvent = Render3DEvent.getInstance().subscribe(new Listener<Render3DEvent.Render3DEventData>(event -> {
            if (StorageESPModule.mc.field_1687 == null || StorageESPModule.mc.field_1724 == null) {
                return;
            }
            else {
                if (this.minecartSel.getValue()) {
                    StorageESPModule.mc.field_1687.method_18112().iterator();
                    final Iterator iterator;
                    while (iterator.hasNext()) {
                        final class_1297 entity = iterator.next();
                        if (entity instanceof class_1694) {
                            this.renderBox(entity.method_5829(), this.minecartColor.getValue());
                        }
                    }
                }
                final int renderDist = (int)StorageESPModule.mc.field_1690.method_42503().method_41753();
                final int cx = StorageESPModule.mc.field_1724.method_24515().method_10263() >> 4;
                final int cz = StorageESPModule.mc.field_1724.method_24515().method_10260() >> 4;
                for (int dx = -renderDist; dx <= renderDist; ++dx) {
                    for (int dz = -renderDist; dz <= renderDist; ++dz) {
                        final class_2818 chunk = StorageESPModule.mc.field_1687.method_8497(cx + dx, cz + dz);
                        chunk.method_12214().forEach((pos, be) -> {
                            final Color color = this.resolveColor(be);
                            if (color == null) {
                                return;
                            }
                            else {
                                final class_265 shape = StorageESPModule.mc.field_1687.method_8320(pos).method_26218((class_1922)StorageESPModule.mc.field_1687, pos);
                                class_238 method_996 = null;
                                if (shape.method_1110()) {
                                    new(net.minecraft.class_238.class)();
                                    new class_238(pos);
                                }
                                else {
                                    method_996 = shape.method_1107().method_996(pos);
                                }
                                final class_238 box = method_996;
                                this.renderBox(box, color);
                                return;
                            }
                        });
                    }
                }
                return;
            }
        }));
        this.addEvents(render3DEvent);
    }
    
    private Color resolveColor(final class_2586 be) {
        if (be instanceof class_2608) {
            return this.dropperSel.getValue() ? this.dropperColor.getValue() : null;
        }
        if (be instanceof class_2601) {
            return this.dispenserSel.getValue() ? this.dispenserColor.getValue() : null;
        }
        if (be instanceof class_2627) {
            return this.shulkerSel.getValue() ? this.shulkerColor.getValue() : null;
        }
        if (be instanceof class_2611) {
            return this.enderSel.getValue() ? this.enderColor.getValue() : null;
        }
        if (be instanceof class_2595) {
            final boolean isTrapped = be.method_11010().method_26204() instanceof class_2531;
            if (isTrapped) {
                return this.trappedSel.getValue() ? this.trappedColor.getValue() : null;
            }
            return this.chestSel.getValue() ? this.chestColor.getValue() : null;
        }
        else {
            if (be instanceof class_3719) {
                return this.barrelSel.getValue() ? this.barrelColor.getValue() : null;
            }
            if (be instanceof class_3720) {
                return this.blastSel.getValue() ? this.blastColor.getValue() : null;
            }
            if (be instanceof class_3723) {
                return this.smokerSel.getValue() ? this.smokerColor.getValue() : null;
            }
            if (be instanceof class_3866) {
                return this.furnaceSel.getValue() ? this.furnaceColor.getValue() : null;
            }
            if (be instanceof class_2614) {
                return this.hopperSel.getValue() ? this.hopperColor.getValue() : null;
            }
            return null;
        }
    }
    
    private void renderBox(final class_238 box, final Color color) {
        final float x1 = (float)box.field_1323;
        final float y1 = (float)box.field_1322;
        final float z1 = (float)box.field_1321;
        final float x2 = (float)box.field_1320;
        final float y2 = (float)box.field_1325;
        final float z2 = (float)box.field_1324;
        if (this.filled.getValue()) {
            final Color fill = new Color(color.getRed(), color.getGreen(), color.getBlue(), 45);
            RenderUtil.BOX.drawBox(x1, y1, z1, x2, y2, z2, 1.0f, fill, BoxRender.Render.FILL, 0.0f);
        }
        if (this.outline.getValue()) {
            RenderUtil.BOX.drawBox(x1, y1, z1, x2, y2, z2, 1.5f, color, BoxRender.Render.OUTLINE, 0.0f);
        }
    }
    
    @Generated
    public static StorageESPModule getInstance() {
        return StorageESPModule.instance;
    }
    
    static {
        instance = new StorageESPModule();
    }
}

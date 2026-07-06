// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render;

import java.util.function.Supplier;
import sweetie.leonware.client.ui.widget.Widget;
import lombok.Generated;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.client.ui.widget.overlay.PlayerModelWidget;
import sweetie.leonware.client.ui.widget.overlay.ScoreboardWidget;
import sweetie.leonware.client.ui.widget.overlay.NotifWidget;
import sweetie.leonware.client.ui.widget.overlay.StaffsWidget;
import sweetie.leonware.client.ui.widget.overlay.TargetInfoWidget;
import sweetie.leonware.client.ui.widget.overlay.WatermarkWidget;
import sweetie.leonware.api.module.setting.Setting;
import java.util.Objects;
import sweetie.leonware.client.ui.widget.overlay.ArrayListWidget;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.client.ui.widget.WidgetManager;
import sweetie.leonware.api.utils.render.KawaseBlurProgram;
import sweetie.leonware.client.services.RenderService;
import sweetie.leonware.client.ui.theme.ThemeEditor;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.RunSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Interface", category = Category.RENDER)
public class InterfaceModule extends Module
{
    private static final InterfaceModule instance;
    public final MultiBooleanSetting widgets;
    private final RunSetting themes;
    public final SliderSetting scale;
    public final SliderSetting glassy;
    public final SliderSetting passes;
    public final SliderSetting offset;
    
    public InterfaceModule() {
        this.widgets = new MultiBooleanSetting("Widgets");
        this.themes = new RunSetting("Theme editor").value(() -> ThemeEditor.getInstance().setOpen(!ThemeEditor.getInstance().isOpen()));
        this.scale = new SliderSetting("Scale").value(0.7f).range(0.6f, 1.5f).step(0.05f).onAction(() -> RenderService.getInstance().updateScale());
        this.glassy = new SliderSetting("Glassy").value(0.4f).range(0.0f, 1.0f).step(0.1f);
        this.passes = new SliderSetting("Passes").value(3.0f).range(1.0f, 5.0f).step(1.0f).onAction(KawaseBlurProgram::recreate);
        this.offset = new SliderSetting("Offset").value(12.0f).range(5.0f, 25.0f).step(1.0f);
    }
    
    public static float getScale() {
        return getInstance().scale.getValue();
    }
    
    public static float getGlassy() {
        return 1.0f - getInstance().glassy.getValue();
    }
    
    public static int getPasses() {
        return getInstance().passes.getValue().intValue();
    }
    
    public static float getOffset() {
        return getInstance().offset.getValue();
    }
    
    public void init() {
        this.widgets.value(WidgetManager.getInstance().getWidgets().stream().map(widget -> {
            final BooleanSetting setting = new BooleanSetting(widget.getName()).value(widget.isEnabled());
            setting.onAction(() -> widget.setEnabled(setting.getValue()));
            if (widget instanceof final ArrayListWidget arrayList) {
                final ModeSetting mode = arrayList.mode;
                final Setting obj;
                Objects.requireNonNull(obj);
                mode.setVisible(obj::getValue);
                arrayList.simpleRect.setVisible(() -> setting.getValue() && arrayList.mode.is("\u041f\u0440\u043e\u0441\u0442\u043e\u0439"));
                final BooleanSetting hideRender = arrayList.hideRender;
                final Setting obj2;
                Objects.requireNonNull(obj2);
                hideRender.setVisible(obj2::getValue);
                this.addSettings(arrayList.mode, arrayList.simpleRect, arrayList.hideRender);
            }
            if (widget instanceof final WatermarkWidget watermark) {
                final ModeSetting mode2 = watermark.mode;
                final Setting obj3;
                Objects.requireNonNull(obj3);
                mode2.setVisible(obj3::getValue);
                final BooleanSetting crypto = watermark.crypto;
                final Setting obj4;
                Objects.requireNonNull(obj4);
                crypto.setVisible(obj4::getValue);
                watermark.akrienScale.setVisible(() -> setting.getValue() && watermark.style.is("\u0410\u043a\u0440\u0438\u0435\u043d2"));
                watermark.akrienColor.setVisible(() -> setting.getValue() && watermark.style.is("\u0410\u043a\u0440\u0438\u0435\u043d2"));
                watermark.akrienSubtitleOffset.setVisible(() -> setting.getValue() && watermark.style.is("\u0410\u043a\u0440\u0438\u0435\u043d2"));
                this.addSettings(watermark.style, watermark.mode, watermark.crypto, watermark.akrienScale, watermark.akrienColor, watermark.akrienSubtitleOffset);
            }
            if (widget instanceof final TargetInfoWidget targetInfo) {
                final ModeSetting mode3 = targetInfo.mode;
                final Setting obj5;
                Objects.requireNonNull(obj5);
                mode3.setVisible(obj5::getValue);
                this.addSettings(targetInfo.mode);
            }
            if (widget instanceof final StaffsWidget staffsWidget) {
                final ModeSetting mode4 = staffsWidget.mode;
                final Setting obj6;
                Objects.requireNonNull(obj6);
                mode4.setVisible(obj6::getValue);
                this.addSettings(staffsWidget.mode);
            }
            if (widget instanceof final NotifWidget notifWidget) {
                final ModeSetting notifStyle = notifWidget.notifStyle;
                final Setting obj7;
                Objects.requireNonNull(obj7);
                notifStyle.setVisible(obj7::getValue);
                final MultiBooleanSetting notifTypes = notifWidget.notifTypes;
                final Setting obj8;
                Objects.requireNonNull(obj8);
                notifTypes.setVisible(obj8::getValue);
                this.addSettings(notifWidget.notifStyle, notifWidget.notifTypes);
            }
            if (widget instanceof final ScoreboardWidget scoreboard) {
                final ModeSetting renderMode = scoreboard.renderMode;
                final Setting obj9;
                Objects.requireNonNull(obj9);
                renderMode.setVisible(obj9::getValue);
                this.addSettings(scoreboard.renderMode);
            }
            if (widget instanceof final PlayerModelWidget playerModel) {
                final SliderSetting size = playerModel.size;
                final Setting obj10;
                Objects.requireNonNull(obj10);
                size.setVisible(obj10::getValue);
                final BooleanSetting lookAtMouse = playerModel.lookAtMouse;
                final Setting obj11;
                Objects.requireNonNull(obj11);
                lookAtMouse.setVisible(obj11::getValue);
                final BooleanSetting mimic = playerModel.mimic;
                final Setting obj12;
                Objects.requireNonNull(obj12);
                mimic.setVisible(obj12::getValue);
                this.addSettings(playerModel.size, playerModel.lookAtMouse, playerModel.mimic);
            }
            return setting;
        }).toList());
        this.addSettings(this.widgets, this.themes, this.scale, this.glassy, this.passes, this.offset);
    }
    
    @Override
    public void onEvent() {
    }
    
    @Generated
    public static InterfaceModule getInstance() {
        return InterfaceModule.instance;
    }
    
    static {
        instance = new InterfaceModule();
    }
}

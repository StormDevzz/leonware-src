package sweetie.leonware.client.features.modules.render;

import java.util.Objects;
import lombok.Generated;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.setting.RunSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.render.KawaseBlurProgram;
import sweetie.leonware.client.services.RenderService;
import sweetie.leonware.client.ui.theme.ThemeEditor;
import sweetie.leonware.client.ui.widget.WidgetManager;
import sweetie.leonware.client.ui.widget.overlay.ArrayListWidget;
import sweetie.leonware.client.ui.widget.overlay.NotifWidget;
import sweetie.leonware.client.ui.widget.overlay.PlayerModelWidget;
import sweetie.leonware.client.ui.widget.overlay.ScoreboardWidget;
import sweetie.leonware.client.ui.widget.overlay.StaffsWidget;
import sweetie.leonware.client.ui.widget.overlay.TargetInfoWidget;
import sweetie.leonware.client.ui.widget.overlay.WatermarkWidget;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/InterfaceModule.class */
@ModuleRegister(name = "Interface", category = Category.RENDER)
public class InterfaceModule extends Module {
    private static final InterfaceModule instance = new InterfaceModule();
    public final MultiBooleanSetting widgets = new MultiBooleanSetting("Widgets");
    private final RunSetting themes = new RunSetting("Theme editor").value(() -> {
        ThemeEditor.getInstance().setOpen(!ThemeEditor.getInstance().isOpen());
    });
    public final SliderSetting scale = new SliderSetting("Scale").value(Float.valueOf(0.7f)).range(0.6f, 1.5f).step(0.05f).onAction2(() -> {
        RenderService.getInstance().updateScale();
    });
    public final SliderSetting glassy = new SliderSetting("Glassy").value(Float.valueOf(0.4f)).range(0.0f, 1.0f).step(0.1f);
    public final SliderSetting passes = new SliderSetting("Passes").value(Float.valueOf(3.0f)).range(1.0f, 5.0f).step(1.0f).onAction2(KawaseBlurProgram::recreate);
    public final SliderSetting offset = new SliderSetting("Offset").value(Float.valueOf(12.0f)).range(5.0f, 25.0f).step(1.0f);

    @Generated
    public static InterfaceModule getInstance() {
        return instance;
    }

    public static float getScale() {
        return getInstance().scale.getValue().floatValue();
    }

    public static float getGlassy() {
        return 1.0f - getInstance().glassy.getValue().floatValue();
    }

    public static int getPasses() {
        return getInstance().passes.getValue().intValue();
    }

    public static float getOffset() {
        return getInstance().offset.getValue().floatValue();
    }

    public void init() {
        this.widgets.value(WidgetManager.getInstance().getWidgets().stream().map(widget -> {
            BooleanSetting setting = new BooleanSetting(widget.getName()).value(Boolean.valueOf(widget.isEnabled()));
            setting.onAction2(() -> {
                widget.setEnabled(setting.getValue().booleanValue());
            });
            if (widget instanceof ArrayListWidget) {
                ArrayListWidget arrayList = (ArrayListWidget) widget;
                ModeSetting modeSetting = arrayList.mode;
                Objects.requireNonNull(setting);
                modeSetting.setVisible(setting::getValue);
                arrayList.simpleRect.setVisible(() -> {
                    return Boolean.valueOf(setting.getValue().booleanValue() && arrayList.mode.is("Простой"));
                });
                BooleanSetting booleanSetting = arrayList.hideRender;
                Objects.requireNonNull(setting);
                booleanSetting.setVisible(setting::getValue);
                addSettings(arrayList.mode, arrayList.simpleRect, arrayList.hideRender);
            }
            if (widget instanceof WatermarkWidget) {
                WatermarkWidget watermark = (WatermarkWidget) widget;
                ModeSetting modeSetting2 = watermark.mode;
                Objects.requireNonNull(setting);
                modeSetting2.setVisible(setting::getValue);
                BooleanSetting booleanSetting2 = watermark.crypto;
                Objects.requireNonNull(setting);
                booleanSetting2.setVisible(setting::getValue);
                watermark.akrienScale.setVisible(() -> {
                    return Boolean.valueOf(setting.getValue().booleanValue() && watermark.style.is("Акриен2"));
                });
                watermark.akrienColor.setVisible(() -> {
                    return Boolean.valueOf(setting.getValue().booleanValue() && watermark.style.is("Акриен2"));
                });
                watermark.akrienSubtitleOffset.setVisible(() -> {
                    return Boolean.valueOf(setting.getValue().booleanValue() && watermark.style.is("Акриен2"));
                });
                addSettings(watermark.style, watermark.mode, watermark.crypto, watermark.akrienScale, watermark.akrienColor, watermark.akrienSubtitleOffset);
            }
            if (widget instanceof TargetInfoWidget) {
                TargetInfoWidget targetInfo = (TargetInfoWidget) widget;
                ModeSetting modeSetting3 = targetInfo.mode;
                Objects.requireNonNull(setting);
                modeSetting3.setVisible(setting::getValue);
                addSettings(targetInfo.mode);
            }
            if (widget instanceof StaffsWidget) {
                StaffsWidget staffsWidget = (StaffsWidget) widget;
                ModeSetting modeSetting4 = staffsWidget.mode;
                Objects.requireNonNull(setting);
                modeSetting4.setVisible(setting::getValue);
                addSettings(staffsWidget.mode);
            }
            if (widget instanceof NotifWidget) {
                NotifWidget notifWidget = (NotifWidget) widget;
                ModeSetting modeSetting5 = notifWidget.notifStyle;
                Objects.requireNonNull(setting);
                modeSetting5.setVisible(setting::getValue);
                MultiBooleanSetting multiBooleanSetting = notifWidget.notifTypes;
                Objects.requireNonNull(setting);
                multiBooleanSetting.setVisible(setting::getValue);
                addSettings(notifWidget.notifStyle, notifWidget.notifTypes);
            }
            if (widget instanceof ScoreboardWidget) {
                ScoreboardWidget scoreboard = (ScoreboardWidget) widget;
                ModeSetting modeSetting6 = scoreboard.renderMode;
                Objects.requireNonNull(setting);
                modeSetting6.setVisible(setting::getValue);
                addSettings(scoreboard.renderMode);
            }
            if (widget instanceof PlayerModelWidget) {
                PlayerModelWidget playerModel = (PlayerModelWidget) widget;
                SliderSetting sliderSetting = playerModel.size;
                Objects.requireNonNull(setting);
                sliderSetting.setVisible(setting::getValue);
                BooleanSetting booleanSetting3 = playerModel.lookAtMouse;
                Objects.requireNonNull(setting);
                booleanSetting3.setVisible(setting::getValue);
                BooleanSetting booleanSetting4 = playerModel.mimic;
                Objects.requireNonNull(setting);
                booleanSetting4.setVisible(setting::getValue);
                addSettings(playerModel.size, playerModel.lookAtMouse, playerModel.mimic);
            }
            return setting;
        }).toList());
        addSettings(this.widgets, this.themes, this.scale, this.glassy, this.passes, this.offset);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
    }
}

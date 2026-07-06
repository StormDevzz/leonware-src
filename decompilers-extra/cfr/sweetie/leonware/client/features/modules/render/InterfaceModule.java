/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
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

@ModuleRegister(name="Interface", category=Category.RENDER)
public class InterfaceModule
extends Module {
    private static final InterfaceModule instance = new InterfaceModule();
    public final MultiBooleanSetting widgets = new MultiBooleanSetting("Widgets");
    private final RunSetting themes = new RunSetting("Theme editor").value(() -> ThemeEditor.getInstance().setOpen(!ThemeEditor.getInstance().isOpen()));
    public final SliderSetting scale = new SliderSetting("Scale").value(Float.valueOf(0.7f)).range(0.6f, 1.5f).step(0.05f).onAction(() -> RenderService.getInstance().updateScale());
    public final SliderSetting glassy = new SliderSetting("Glassy").value(Float.valueOf(0.4f)).range(0.0f, 1.0f).step(0.1f);
    public final SliderSetting passes = new SliderSetting("Passes").value(Float.valueOf(3.0f)).range(1.0f, 5.0f).step(1.0f).onAction(KawaseBlurProgram::recreate);
    public final SliderSetting offset = new SliderSetting("Offset").value(Float.valueOf(12.0f)).range(5.0f, 25.0f).step(1.0f);

    public static float getScale() {
        return ((Float)InterfaceModule.getInstance().scale.getValue()).floatValue();
    }

    public static float getGlassy() {
        return 1.0f - ((Float)InterfaceModule.getInstance().glassy.getValue()).floatValue();
    }

    public static int getPasses() {
        return ((Float)InterfaceModule.getInstance().passes.getValue()).intValue();
    }

    public static float getOffset() {
        return ((Float)InterfaceModule.getInstance().offset.getValue()).floatValue();
    }

    public void init() {
        this.widgets.value(WidgetManager.getInstance().getWidgets().stream().map(widget -> {
            BooleanSetting setting = new BooleanSetting(widget.getName()).value(widget.isEnabled());
            setting.onAction(() -> widget.setEnabled((Boolean)setting.getValue()));
            if (widget instanceof ArrayListWidget) {
                ArrayListWidget arrayList = (ArrayListWidget)widget;
                arrayList.mode.setVisible(setting::getValue);
                arrayList.simpleRect.setVisible(() -> (Boolean)setting.getValue() != false && arrayList.mode.is("\u041f\u0440\u043e\u0441\u0442\u043e\u0439"));
                arrayList.hideRender.setVisible(setting::getValue);
                this.addSettings(arrayList.mode, arrayList.simpleRect, arrayList.hideRender);
            }
            if (widget instanceof WatermarkWidget) {
                WatermarkWidget watermark = (WatermarkWidget)widget;
                watermark.mode.setVisible(setting::getValue);
                watermark.crypto.setVisible(setting::getValue);
                watermark.akrienScale.setVisible(() -> (Boolean)setting.getValue() != false && watermark.style.is("\u0410\u043a\u0440\u0438\u0435\u043d2"));
                watermark.akrienColor.setVisible(() -> (Boolean)setting.getValue() != false && watermark.style.is("\u0410\u043a\u0440\u0438\u0435\u043d2"));
                watermark.akrienSubtitleOffset.setVisible(() -> (Boolean)setting.getValue() != false && watermark.style.is("\u0410\u043a\u0440\u0438\u0435\u043d2"));
                this.addSettings(watermark.style, watermark.mode, watermark.crypto, watermark.akrienScale, watermark.akrienColor, watermark.akrienSubtitleOffset);
            }
            if (widget instanceof TargetInfoWidget) {
                TargetInfoWidget targetInfo = (TargetInfoWidget)widget;
                targetInfo.mode.setVisible(setting::getValue);
                this.addSettings(targetInfo.mode);
            }
            if (widget instanceof StaffsWidget) {
                StaffsWidget staffsWidget = (StaffsWidget)widget;
                staffsWidget.mode.setVisible(setting::getValue);
                this.addSettings(staffsWidget.mode);
            }
            if (widget instanceof NotifWidget) {
                NotifWidget notifWidget = (NotifWidget)widget;
                notifWidget.notifStyle.setVisible(setting::getValue);
                notifWidget.notifTypes.setVisible(setting::getValue);
                this.addSettings(notifWidget.notifStyle, notifWidget.notifTypes);
            }
            if (widget instanceof ScoreboardWidget) {
                ScoreboardWidget scoreboard = (ScoreboardWidget)widget;
                scoreboard.renderMode.setVisible(setting::getValue);
                this.addSettings(scoreboard.renderMode);
            }
            if (widget instanceof PlayerModelWidget) {
                PlayerModelWidget playerModel = (PlayerModelWidget)widget;
                playerModel.size.setVisible(setting::getValue);
                playerModel.lookAtMouse.setVisible(setting::getValue);
                playerModel.mimic.setVisible(setting::getValue);
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
        return instance;
    }
}


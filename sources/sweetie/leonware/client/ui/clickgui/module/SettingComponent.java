package sweetie.leonware.client.ui.clickgui.module;

import lombok.Generated;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import sweetie.leonware.client.ui.UIComponent;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/clickgui/module/SettingComponent.class */
public abstract class SettingComponent extends UIComponent {
    private final Setting<?> setting;
    private final AnimationUtil visibleAnimation = new AnimationUtil();

    @Generated
    public SettingComponent(Setting<?> setting) {
        this.setting = setting;
    }

    @Generated
    public Setting<?> getSetting() {
        return this.setting;
    }

    @Generated
    public AnimationUtil getVisibleAnimation() {
        return this.visibleAnimation;
    }

    public void updateHeight(float value) {
        setHeight(scaled(value));
    }
}

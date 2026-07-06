/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.client.ui.clickgui.module;

import lombok.Generated;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import sweetie.leonware.client.ui.UIComponent;

public abstract class SettingComponent
extends UIComponent {
    private final Setting<?> setting;
    private final AnimationUtil visibleAnimation = new AnimationUtil();

    public void updateHeight(float value) {
        this.setHeight(this.scaled(value));
    }

    @Generated
    public Setting<?> getSetting() {
        return this.setting;
    }

    @Generated
    public AnimationUtil getVisibleAnimation() {
        return this.visibleAnimation;
    }

    @Generated
    public SettingComponent(Setting<?> setting) {
        this.setting = setting;
    }
}


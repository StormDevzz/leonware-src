// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.clickgui.module;

import lombok.Generated;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.client.ui.UIComponent;

public abstract class SettingComponent extends UIComponent
{
    private final Setting<?> setting;
    private final AnimationUtil visibleAnimation;
    
    public void updateHeight(final float value) {
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
    public SettingComponent(final Setting<?> setting) {
        this.visibleAnimation = new AnimationUtil();
        this.setting = setting;
    }
}

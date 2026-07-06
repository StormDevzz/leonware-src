// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.clickgui.module;

import sweetie.leonware.api.utils.animation.AnimationUtil;
import sweetie.leonware.api.module.setting.Setting;
import lombok.Generated;
import sweetie.leonware.client.ui.UIComponent;

public abstract class ExpandableComponent extends UIComponent
{
    private final ExpandableBase expandableBase;
    
    public ExpandableComponent() {
        this.expandableBase = new ExpandableBase() {};
    }
    
    public void updateOpen() {
        this.expandableBase.updateOpen();
    }
    
    public void toggleOpen() {
        this.expandableBase.toggleOpen();
    }
    
    public boolean isOpen() {
        return this.expandableBase.isOpen();
    }
    
    public float getAnim() {
        return (float)this.expandableBase.getOpenAnimation().getValue();
    }
    
    public boolean isNotOver() {
        return this.getAnim() < 0.8;
    }
    
    @Generated
    public ExpandableBase getExpandableBase() {
        return this.expandableBase;
    }
    
    public abstract static class ExpandableSettingComponent extends SettingComponent
    {
        private final ExpandableBase expandableBase;
        
        public ExpandableSettingComponent(final Setting<?> setting) {
            super(setting);
            this.expandableBase = new ExpandableBase() {};
        }
        
        public void updateOpen() {
            this.expandableBase.updateOpen();
        }
        
        public void toggleOpen() {
            this.expandableBase.toggleOpen();
        }
        
        public boolean isOpen() {
            return this.expandableBase.isOpen();
        }
        
        public void setOpen(final boolean value) {
            this.expandableBase.setOpen(value);
        }
        
        public float getValue() {
            return (float)this.expandableBase.getOpenAnimation().getValue();
        }
        
        public AnimationUtil getAnim() {
            return this.expandableBase.getOpenAnimation();
        }
        
        public boolean isNotOver() {
            return this.getValue() < 0.8;
        }
    }
}

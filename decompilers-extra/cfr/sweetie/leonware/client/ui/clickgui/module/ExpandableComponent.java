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
import sweetie.leonware.client.ui.clickgui.module.ExpandableBase;
import sweetie.leonware.client.ui.clickgui.module.SettingComponent;

public abstract class ExpandableComponent
extends UIComponent {
    private final ExpandableBase expandableBase = new ExpandableBase(this){};

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
        return (double)this.getAnim() < 0.8;
    }

    @Generated
    public ExpandableBase getExpandableBase() {
        return this.expandableBase;
    }

    public static abstract class ExpandableSettingComponent
    extends SettingComponent {
        private final ExpandableBase expandableBase = new ExpandableBase(this){};

        public ExpandableSettingComponent(Setting<?> setting) {
            super(setting);
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

        public void setOpen(boolean value) {
            this.expandableBase.setOpen(value);
        }

        public float getValue() {
            return (float)this.expandableBase.getOpenAnimation().getValue();
        }

        public AnimationUtil getAnim() {
            return this.expandableBase.getOpenAnimation();
        }

        public boolean isNotOver() {
            return (double)this.getValue() < 0.8;
        }
    }
}


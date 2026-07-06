package sweetie.leonware.client.ui.clickgui.module;

import lombok.Generated;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import sweetie.leonware.client.ui.UIComponent;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/clickgui/module/ExpandableComponent.class */
public abstract class ExpandableComponent extends UIComponent {
    private final ExpandableBase expandableBase = new ExpandableBase(this) { // from class: sweetie.leonware.client.ui.clickgui.module.ExpandableComponent.1
    };

    @Generated
    public ExpandableBase getExpandableBase() {
        return this.expandableBase;
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
        return (float) this.expandableBase.getOpenAnimation().getValue();
    }

    public boolean isNotOver() {
        return ((double) getAnim()) < 0.8d;
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/clickgui/module/ExpandableComponent$ExpandableSettingComponent.class */
    public static abstract class ExpandableSettingComponent extends SettingComponent {
        private final ExpandableBase expandableBase;

        public ExpandableSettingComponent(Setting<?> setting) {
            super(setting);
            this.expandableBase = new ExpandableBase(this) { // from class: sweetie.leonware.client.ui.clickgui.module.ExpandableComponent.ExpandableSettingComponent.1
            };
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
            return (float) this.expandableBase.getOpenAnimation().getValue();
        }

        public AnimationUtil getAnim() {
            return this.expandableBase.getOpenAnimation();
        }

        public boolean isNotOver() {
            return ((double) getValue()) < 0.8d;
        }
    }
}

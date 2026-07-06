package sweetie.leonware.client.ui.clickgui.module;

import lombok.Generated;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import sweetie.leonware.api.utils.animation.Easing;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/clickgui/module/ExpandableBase.class */
public abstract class ExpandableBase {
    private final AnimationUtil openAnimation = new AnimationUtil();
    private boolean open = false;

    @Generated
    public AnimationUtil getOpenAnimation() {
        return this.openAnimation;
    }

    @Generated
    public void setOpen(boolean open) {
        this.open = open;
    }

    @Generated
    public boolean isOpen() {
        return this.open;
    }

    public void updateOpen() {
        this.openAnimation.update();
        double target = this.open ? 1.0d : 0.0d;
        long duration = this.open ? 210L : 160L;
        Easing easing = this.open ? Easing.QUINT_OUT : Easing.CUBIC_OUT;
        this.openAnimation.run(target, duration, easing);
    }

    public void toggleOpen() {
        this.open = !this.open;
    }
}

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.client.ui.clickgui.module;

import lombok.Generated;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import sweetie.leonware.api.utils.animation.Easing;

public abstract class ExpandableBase {
    private final AnimationUtil openAnimation = new AnimationUtil();
    private boolean open = false;

    public void updateOpen() {
        this.openAnimation.update();
        double target = this.open ? 1.0 : 0.0;
        long duration = this.open ? 210L : 160L;
        Easing easing = this.open ? Easing.QUINT_OUT : Easing.CUBIC_OUT;
        this.openAnimation.run(target, duration, easing);
    }

    public void toggleOpen() {
        this.open = !this.open;
    }

    @Generated
    public AnimationUtil getOpenAnimation() {
        return this.openAnimation;
    }

    @Generated
    public boolean isOpen() {
        return this.open;
    }

    @Generated
    public void setOpen(boolean open) {
        this.open = open;
    }
}


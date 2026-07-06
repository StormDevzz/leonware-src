// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.clickgui.module;

import lombok.Generated;
import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.api.utils.animation.AnimationUtil;

public abstract class ExpandableBase
{
    private final AnimationUtil openAnimation;
    private boolean open;
    
    public ExpandableBase() {
        this.openAnimation = new AnimationUtil();
        this.open = false;
    }
    
    public void updateOpen() {
        this.openAnimation.update();
        final double target = this.open ? 1.0 : 0.0;
        final long duration = this.open ? 210L : 160L;
        final Easing easing = this.open ? Easing.QUINT_OUT : Easing.CUBIC_OUT;
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
    public void setOpen(final boolean open) {
        this.open = open;
    }
}

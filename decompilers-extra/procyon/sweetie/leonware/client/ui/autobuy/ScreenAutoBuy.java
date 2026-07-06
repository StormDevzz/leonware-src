// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.autobuy;

import lombok.Generated;
import net.minecraft.class_332;
import net.minecraft.class_2561;
import sweetie.leonware.api.system.interfaces.QuickImports;
import net.minecraft.class_437;

public class ScreenAutoBuy extends class_437 implements QuickImports
{
    private static final ScreenAutoBuy instance;
    
    protected ScreenAutoBuy() {
        super(class_2561.method_30163(""));
    }
    
    public void method_25419() {
        super.method_25419();
    }
    
    protected void method_25426() {
        super.method_25426();
    }
    
    public void method_25394(final class_332 context, final int mouseX, final int mouseY, final float delta) {
    }
    
    public boolean method_25404(final int keyCode, final int scanCode, final int modifiers) {
        return super.method_25404(keyCode, scanCode, modifiers);
    }
    
    public boolean method_25402(final double mouseX, final double mouseY, final int button) {
        return super.method_25402(mouseX, mouseY, button);
    }
    
    public boolean method_25406(final double mouseX, final double mouseY, final int button) {
        return super.method_25406(mouseX, mouseY, button);
    }
    
    public boolean method_25401(final double mouseX, final double mouseY, final double horizontalAmount, final double verticalAmount) {
        return super.method_25401(mouseX, mouseY, horizontalAmount, verticalAmount);
    }
    
    public boolean method_25400(final char chr, final int modifiers) {
        return super.method_25400(chr, modifiers);
    }
    
    public void method_48267() {
    }
    
    public boolean method_25422() {
        return true;
    }
    
    public void method_25420(final class_332 context, final int mouseX, final int mouseY, final float delta) {
    }
    
    public boolean method_25421() {
        return false;
    }
    
    protected void method_57734() {
    }
    
    @Generated
    public static ScreenAutoBuy getInstance() {
        return ScreenAutoBuy.instance;
    }
    
    static {
        instance = new ScreenAutoBuy();
    }
}

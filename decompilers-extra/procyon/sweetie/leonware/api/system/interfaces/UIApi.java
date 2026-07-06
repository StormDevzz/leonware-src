// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.system.interfaces;

import net.minecraft.class_332;

public interface UIApi
{
    void render(final class_332 p0, final int p1, final int p2, final float p3);
    
    void keyPressed(final int p0, final int p1, final int p2);
    
    void mouseClicked(final double p0, final double p1, final int p2);
    
    void mouseReleased(final double p0, final double p1, final int p2);
    
    void mouseScrolled(final double p0, final double p1, final double p2, final double p3);
}

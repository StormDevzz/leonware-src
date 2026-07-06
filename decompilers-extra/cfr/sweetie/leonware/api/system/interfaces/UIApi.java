/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_332
 */
package sweetie.leonware.api.system.interfaces;

import net.minecraft.class_332;

public interface UIApi {
    public void render(class_332 var1, int var2, int var3, float var4);

    public void keyPressed(int var1, int var2, int var3);

    public void mouseClicked(double var1, double var3, int var5);

    public void mouseReleased(double var1, double var3, int var5);

    public void mouseScrolled(double var1, double var3, double var5, double var7);
}


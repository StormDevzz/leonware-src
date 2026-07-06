/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2561
 *  net.minecraft.class_8113$class_8123
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package sweetie.leonware.inject.accessors;

import net.minecraft.class_2561;
import net.minecraft.class_8113;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={class_8113.class_8123.class})
public interface ITextDisplayEntity {
    @Invoker(value="getText")
    public class_2561 invokeGetText();
}


// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.module;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleRegister {
    String name();
    
    Category category();
    
    int bind() default -999;
}

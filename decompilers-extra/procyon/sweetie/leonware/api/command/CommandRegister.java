// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.command;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandRegister {
    String name();
}

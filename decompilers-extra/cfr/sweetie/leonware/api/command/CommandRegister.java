/*
 * Decompiled with CFR 0.152.
 */
package sweetie.leonware.api.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value=RetentionPolicy.RUNTIME)
public @interface CommandRegister {
    public String name();
}


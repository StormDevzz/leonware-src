/*
 * Decompiled with CFR 0.152.
 */
package sweetie.leonware.api.module;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import sweetie.leonware.api.module.Category;

@Retention(value=RetentionPolicy.RUNTIME)
public @interface ModuleRegister {
    public String name();

    public Category category();

    public int bind() default -999;
}


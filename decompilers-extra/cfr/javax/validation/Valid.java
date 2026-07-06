/*
 * Decompiled with CFR 0.152.
 */
package javax.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value={ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface Valid {
}


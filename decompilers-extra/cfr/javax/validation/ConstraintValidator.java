/*
 * Decompiled with CFR 0.152.
 */
package javax.validation;

import java.lang.annotation.Annotation;
import javax.validation.ConstraintValidatorContext;

public interface ConstraintValidator<A extends Annotation, T> {
    public void initialize(A var1);

    public boolean isValid(T var1, ConstraintValidatorContext var2);
}


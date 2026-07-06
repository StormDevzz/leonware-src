// 
// Decompiled by Procyon v0.6.0
// 

package javax.validation;

import java.lang.annotation.Annotation;

public interface ConstraintValidator<A extends Annotation, T>
{
    void initialize(final A p0);
    
    boolean isValid(final T p0, final ConstraintValidatorContext p1);
}

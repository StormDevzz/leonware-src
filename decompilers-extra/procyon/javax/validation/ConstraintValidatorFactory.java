// 
// Decompiled by Procyon v0.6.0
// 

package javax.validation;

public interface ConstraintValidatorFactory
{
     <T extends ConstraintValidator<?, ?>> T getInstance(final Class<T> p0);
    
    void releaseInstance(final ConstraintValidator<?, ?> p0);
}

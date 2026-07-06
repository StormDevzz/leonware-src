// 
// Decompiled by Procyon v0.6.0
// 

package javax.validation;

public interface ValidatorContext
{
    ValidatorContext messageInterpolator(final MessageInterpolator p0);
    
    ValidatorContext traversableResolver(final TraversableResolver p0);
    
    ValidatorContext constraintValidatorFactory(final ConstraintValidatorFactory p0);
    
    ValidatorContext parameterNameProvider(final ParameterNameProvider p0);
    
    Validator getValidator();
}

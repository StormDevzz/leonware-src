// 
// Decompiled by Procyon v0.6.0
// 

package javax.validation;

public interface ValidatorFactory
{
    Validator getValidator();
    
    ValidatorContext usingContext();
    
    MessageInterpolator getMessageInterpolator();
    
    TraversableResolver getTraversableResolver();
    
    ConstraintValidatorFactory getConstraintValidatorFactory();
    
    ParameterNameProvider getParameterNameProvider();
    
     <T> T unwrap(final Class<T> p0);
    
    void close();
}

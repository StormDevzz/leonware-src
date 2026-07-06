package javax.validation;

/* JADX INFO: loaded from: leonware-0.0.3.jar:javax/validation/ValidatorContext.class */
public interface ValidatorContext {
    ValidatorContext messageInterpolator(MessageInterpolator messageInterpolator);

    ValidatorContext traversableResolver(TraversableResolver traversableResolver);

    ValidatorContext constraintValidatorFactory(ConstraintValidatorFactory constraintValidatorFactory);

    ValidatorContext parameterNameProvider(ParameterNameProvider parameterNameProvider);

    Validator getValidator();
}

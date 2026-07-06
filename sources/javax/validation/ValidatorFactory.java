package javax.validation;

/* JADX INFO: loaded from: leonware-0.0.3.jar:javax/validation/ValidatorFactory.class */
public interface ValidatorFactory {
    Validator getValidator();

    ValidatorContext usingContext();

    MessageInterpolator getMessageInterpolator();

    TraversableResolver getTraversableResolver();

    ConstraintValidatorFactory getConstraintValidatorFactory();

    ParameterNameProvider getParameterNameProvider();

    <T> T unwrap(Class<T> cls);

    void close();
}

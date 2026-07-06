package javax.validation;

/* JADX INFO: loaded from: leonware-0.0.3.jar:javax/validation/ConstraintValidatorFactory.class */
public interface ConstraintValidatorFactory {
    <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> cls);

    void releaseInstance(ConstraintValidator<?, ?> constraintValidator);
}

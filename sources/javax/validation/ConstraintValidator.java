package javax.validation;

import java.lang.annotation.Annotation;

/* JADX INFO: loaded from: leonware-0.0.3.jar:javax/validation/ConstraintValidator.class */
public interface ConstraintValidator<A extends Annotation, T> {
    void initialize(A a);

    boolean isValid(T t, ConstraintValidatorContext constraintValidatorContext);
}

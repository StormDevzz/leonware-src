package javax.validation.constraintvalidation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* JADX INFO: loaded from: leonware-0.0.3.jar:javax/validation/constraintvalidation/SupportedValidationTarget.class */
@Target({ElementType.TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface SupportedValidationTarget {
    ValidationTarget[] value();
}

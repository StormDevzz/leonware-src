package javax.validation.executable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* JADX INFO: loaded from: leonware-0.0.3.jar:javax/validation/executable/ValidateOnExecution.class */
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE, ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateOnExecution {
    ExecutableType[] type() default {ExecutableType.IMPLICIT};
}

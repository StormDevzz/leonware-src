package javax.validation.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/* JADX INFO: loaded from: leonware-0.0.3.jar:javax/validation/constraints/AssertFalse.class */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Constraint(validatedBy = {})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AssertFalse {

    /* JADX INFO: loaded from: leonware-0.0.3.jar:javax/validation/constraints/AssertFalse$List.class */
    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface List {
        AssertFalse[] value();
    }

    String message() default "{javax.validation.constraints.AssertFalse.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

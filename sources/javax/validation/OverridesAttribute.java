package javax.validation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* JADX INFO: loaded from: leonware-0.0.3.jar:javax/validation/OverridesAttribute.class */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OverridesAttribute {

    /* JADX INFO: loaded from: leonware-0.0.3.jar:javax/validation/OverridesAttribute$List.class */
    @Target({ElementType.METHOD})
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    public @interface List {
        OverridesAttribute[] value();
    }

    Class<? extends Annotation> constraint();

    String name();

    int constraintIndex() default -1;
}

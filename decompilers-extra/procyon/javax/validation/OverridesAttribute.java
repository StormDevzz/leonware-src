// 
// Decompiled by Procyon v0.6.0
// 

package javax.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface OverridesAttribute {
    Class<? extends Annotation> constraint();
    
    String name();
    
    int constraintIndex() default -1;
    
    @Documented
    @Target({ ElementType.METHOD })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface List {
        OverridesAttribute[] value();
    }
}

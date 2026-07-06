package sweetie.leonware.api.module;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/module/ModuleRegister.class */
@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleRegister {
    String name();

    Category category();

    int bind() default -999;
}

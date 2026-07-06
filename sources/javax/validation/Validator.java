package javax.validation;

import java.util.Set;
import javax.validation.executable.ExecutableValidator;
import javax.validation.metadata.BeanDescriptor;

/* JADX INFO: loaded from: leonware-0.0.3.jar:javax/validation/Validator.class */
public interface Validator {
    <T> Set<ConstraintViolation<T>> validate(T t, Class<?>... clsArr);

    <T> Set<ConstraintViolation<T>> validateProperty(T t, String str, Class<?>... clsArr);

    <T> Set<ConstraintViolation<T>> validateValue(Class<T> cls, String str, Object obj, Class<?>... clsArr);

    BeanDescriptor getConstraintsForClass(Class<?> cls);

    <T> T unwrap(Class<T> cls);

    ExecutableValidator forExecutables();
}

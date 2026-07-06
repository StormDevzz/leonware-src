package javax.validation;

import javax.validation.metadata.ConstraintDescriptor;

/* JADX INFO: loaded from: leonware-0.0.3.jar:javax/validation/ConstraintViolation.class */
public interface ConstraintViolation<T> {
    String getMessage();

    String getMessageTemplate();

    T getRootBean();

    Class<T> getRootBeanClass();

    Object getLeafBean();

    Object[] getExecutableParameters();

    Object getExecutableReturnValue();

    Path getPropertyPath();

    Object getInvalidValue();

    ConstraintDescriptor<?> getConstraintDescriptor();

    <U> U unwrap(Class<U> cls);
}

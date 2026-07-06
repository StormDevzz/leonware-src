package javax.validation.metadata;

import java.util.Set;

/* JADX INFO: loaded from: leonware-0.0.3.jar:javax/validation/metadata/BeanDescriptor.class */
public interface BeanDescriptor extends ElementDescriptor {
    boolean isBeanConstrained();

    PropertyDescriptor getConstraintsForProperty(String str);

    Set<PropertyDescriptor> getConstrainedProperties();

    MethodDescriptor getConstraintsForMethod(String str, Class<?>... clsArr);

    Set<MethodDescriptor> getConstrainedMethods(MethodType methodType, MethodType... methodTypeArr);

    ConstructorDescriptor getConstraintsForConstructor(Class<?>... clsArr);

    Set<ConstructorDescriptor> getConstrainedConstructors();
}

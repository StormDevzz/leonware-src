package javax.validation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

/* JADX INFO: loaded from: leonware-0.0.3.jar:javax/validation/ParameterNameProvider.class */
public interface ParameterNameProvider {
    List<String> getParameterNames(Constructor<?> constructor);

    List<String> getParameterNames(Method method);
}

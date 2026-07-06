package javax.validation;

import java.lang.annotation.ElementType;
import javax.validation.Path;

/* JADX INFO: loaded from: leonware-0.0.3.jar:javax/validation/TraversableResolver.class */
public interface TraversableResolver {
    boolean isReachable(Object obj, Path.Node node, Class<?> cls, Path path, ElementType elementType);

    boolean isCascadable(Object obj, Path.Node node, Class<?> cls, Path path, ElementType elementType);
}

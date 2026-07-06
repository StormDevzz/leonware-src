package javax.validation;

import java.util.List;

/* JADX INFO: loaded from: leonware-0.0.3.jar:javax/validation/Path.class */
public interface Path extends Iterable<Node> {

    /* JADX INFO: loaded from: leonware-0.0.3.jar:javax/validation/Path$BeanNode.class */
    public interface BeanNode extends Node {
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:javax/validation/Path$ConstructorNode.class */
    public interface ConstructorNode extends Node {
        List<Class<?>> getParameterTypes();
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:javax/validation/Path$CrossParameterNode.class */
    public interface CrossParameterNode extends Node {
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:javax/validation/Path$MethodNode.class */
    public interface MethodNode extends Node {
        List<Class<?>> getParameterTypes();
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:javax/validation/Path$Node.class */
    public interface Node {
        String getName();

        boolean isInIterable();

        Integer getIndex();

        Object getKey();

        ElementKind getKind();

        <T extends Node> T as(Class<T> cls);
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:javax/validation/Path$ParameterNode.class */
    public interface ParameterNode extends Node {
        int getParameterIndex();
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:javax/validation/Path$PropertyNode.class */
    public interface PropertyNode extends Node {
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:javax/validation/Path$ReturnValueNode.class */
    public interface ReturnValueNode extends Node {
    }
}

package javax.validation.metadata;

import java.util.List;
import java.util.Set;
import javax.validation.metadata.ElementDescriptor;

/* JADX INFO: loaded from: leonware-0.0.3.jar:javax/validation/metadata/ExecutableDescriptor.class */
public interface ExecutableDescriptor extends ElementDescriptor {
    String getName();

    List<ParameterDescriptor> getParameterDescriptors();

    CrossParameterDescriptor getCrossParameterDescriptor();

    ReturnValueDescriptor getReturnValueDescriptor();

    boolean hasConstrainedParameters();

    boolean hasConstrainedReturnValue();

    @Override // javax.validation.metadata.ElementDescriptor
    boolean hasConstraints();

    @Override // javax.validation.metadata.ElementDescriptor
    Set<ConstraintDescriptor<?>> getConstraintDescriptors();

    @Override // javax.validation.metadata.ElementDescriptor
    ElementDescriptor.ConstraintFinder findConstraints();
}

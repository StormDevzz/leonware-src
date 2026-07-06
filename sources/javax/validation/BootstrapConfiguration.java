package javax.validation;

import java.util.Map;
import java.util.Set;
import javax.validation.executable.ExecutableType;

/* JADX INFO: loaded from: leonware-0.0.3.jar:javax/validation/BootstrapConfiguration.class */
public interface BootstrapConfiguration {
    String getDefaultProviderClassName();

    String getConstraintValidatorFactoryClassName();

    String getMessageInterpolatorClassName();

    String getTraversableResolverClassName();

    String getParameterNameProviderClassName();

    Set<String> getConstraintMappingResourcePaths();

    boolean isExecutableValidationEnabled();

    Set<ExecutableType> getDefaultValidatedExecutableTypes();

    Map<String, String> getProperties();
}

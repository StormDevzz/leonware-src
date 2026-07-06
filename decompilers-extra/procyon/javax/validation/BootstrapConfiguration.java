// 
// Decompiled by Procyon v0.6.0
// 

package javax.validation;

import java.util.Map;
import javax.validation.executable.ExecutableType;
import java.util.Set;

public interface BootstrapConfiguration
{
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

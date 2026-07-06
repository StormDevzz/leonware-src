// 
// Decompiled by Procyon v0.6.0
// 

package javax.validation.spi;

import java.util.Map;
import javax.validation.ParameterNameProvider;
import javax.validation.TraversableResolver;
import javax.validation.ConstraintValidatorFactory;
import java.io.InputStream;
import java.util.Set;
import javax.validation.MessageInterpolator;

public interface ConfigurationState
{
    boolean isIgnoreXmlConfiguration();
    
    MessageInterpolator getMessageInterpolator();
    
    Set<InputStream> getMappingStreams();
    
    ConstraintValidatorFactory getConstraintValidatorFactory();
    
    TraversableResolver getTraversableResolver();
    
    ParameterNameProvider getParameterNameProvider();
    
    Map<String, String> getProperties();
}

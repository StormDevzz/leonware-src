/*
 * Decompiled with CFR 0.152.
 */
package javax.validation.spi;

import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.MessageInterpolator;
import javax.validation.ParameterNameProvider;
import javax.validation.TraversableResolver;

public interface ConfigurationState {
    public boolean isIgnoreXmlConfiguration();

    public MessageInterpolator getMessageInterpolator();

    public Set<InputStream> getMappingStreams();

    public ConstraintValidatorFactory getConstraintValidatorFactory();

    public TraversableResolver getTraversableResolver();

    public ParameterNameProvider getParameterNameProvider();

    public Map<String, String> getProperties();
}


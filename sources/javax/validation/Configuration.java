package javax.validation;

import java.io.InputStream;
import javax.validation.Configuration;

/* JADX INFO: loaded from: leonware-0.0.3.jar:javax/validation/Configuration.class */
public interface Configuration<T extends Configuration<T>> {
    T ignoreXmlConfiguration();

    T messageInterpolator(MessageInterpolator messageInterpolator);

    T traversableResolver(TraversableResolver traversableResolver);

    T constraintValidatorFactory(ConstraintValidatorFactory constraintValidatorFactory);

    T parameterNameProvider(ParameterNameProvider parameterNameProvider);

    T addMapping(InputStream inputStream);

    T addProperty(String str, String str2);

    MessageInterpolator getDefaultMessageInterpolator();

    TraversableResolver getDefaultTraversableResolver();

    ConstraintValidatorFactory getDefaultConstraintValidatorFactory();

    ParameterNameProvider getDefaultParameterNameProvider();

    BootstrapConfiguration getBootstrapConfiguration();

    ValidatorFactory buildValidatorFactory();
}

package javax.validation.bootstrap;

import javax.validation.Configuration;
import javax.validation.ValidationProviderResolver;

/* JADX INFO: loaded from: leonware-0.0.3.jar:javax/validation/bootstrap/ProviderSpecificBootstrap.class */
public interface ProviderSpecificBootstrap<T extends Configuration<T>> {
    ProviderSpecificBootstrap<T> providerResolver(ValidationProviderResolver validationProviderResolver);

    T configure();
}

package javax.validation.bootstrap;

import javax.validation.Configuration;
import javax.validation.ValidationProviderResolver;

/* JADX INFO: loaded from: leonware-0.0.3.jar:javax/validation/bootstrap/GenericBootstrap.class */
public interface GenericBootstrap {
    GenericBootstrap providerResolver(ValidationProviderResolver validationProviderResolver);

    Configuration<?> configure();
}

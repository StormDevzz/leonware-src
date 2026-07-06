package javax.validation.spi;

import javax.validation.Configuration;
import javax.validation.ValidatorFactory;

/* JADX INFO: loaded from: leonware-0.0.3.jar:javax/validation/spi/ValidationProvider.class */
public interface ValidationProvider<T extends Configuration<T>> {
    T createSpecializedConfiguration(BootstrapState bootstrapState);

    Configuration<?> createGenericConfiguration(BootstrapState bootstrapState);

    ValidatorFactory buildValidatorFactory(ConfigurationState configurationState);
}

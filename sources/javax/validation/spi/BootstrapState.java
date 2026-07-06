package javax.validation.spi;

import javax.validation.ValidationProviderResolver;

/* JADX INFO: loaded from: leonware-0.0.3.jar:javax/validation/spi/BootstrapState.class */
public interface BootstrapState {
    ValidationProviderResolver getValidationProviderResolver();

    ValidationProviderResolver getDefaultValidationProviderResolver();
}

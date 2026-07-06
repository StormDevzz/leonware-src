package javax.validation;

import java.util.List;
import javax.validation.spi.ValidationProvider;

/* JADX INFO: loaded from: leonware-0.0.3.jar:javax/validation/ValidationProviderResolver.class */
public interface ValidationProviderResolver {
    List<ValidationProvider<?>> getValidationProviders();
}

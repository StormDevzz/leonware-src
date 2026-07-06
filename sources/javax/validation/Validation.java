package javax.validation;

import java.lang.ref.SoftReference;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.WeakHashMap;
import javax.validation.bootstrap.GenericBootstrap;
import javax.validation.bootstrap.ProviderSpecificBootstrap;
import javax.validation.spi.BootstrapState;
import javax.validation.spi.ValidationProvider;

/* JADX INFO: loaded from: leonware-0.0.3.jar:javax/validation/Validation.class */
public class Validation {
    public static ValidatorFactory buildDefaultValidatorFactory() {
        return byDefaultProvider().configure().buildValidatorFactory();
    }

    public static GenericBootstrap byDefaultProvider() {
        return new GenericBootstrapImpl();
    }

    public static <T extends Configuration<T>, U extends ValidationProvider<T>> ProviderSpecificBootstrap<T> byProvider(Class<U> providerType) {
        return new ProviderSpecificBootstrapImpl(providerType);
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:javax/validation/Validation$ProviderSpecificBootstrapImpl.class */
    private static class ProviderSpecificBootstrapImpl<T extends Configuration<T>, U extends ValidationProvider<T>> implements ProviderSpecificBootstrap<T> {
        private final Class<U> validationProviderClass;
        private ValidationProviderResolver resolver;

        public ProviderSpecificBootstrapImpl(Class<U> validationProviderClass) {
            this.validationProviderClass = validationProviderClass;
        }

        @Override // javax.validation.bootstrap.ProviderSpecificBootstrap
        public ProviderSpecificBootstrap<T> providerResolver(ValidationProviderResolver resolver) {
            this.resolver = resolver;
            return this;
        }

        @Override // javax.validation.bootstrap.ProviderSpecificBootstrap
        public T configure() {
            if (this.validationProviderClass == null) {
                throw new ValidationException("builder is mandatory. Use Validation.byDefaultProvider() to use the generic provider discovery mechanism");
            }
            GenericBootstrapImpl genericBootstrapImpl = new GenericBootstrapImpl();
            if (this.resolver == null) {
                this.resolver = genericBootstrapImpl.getDefaultValidationProviderResolver();
            } else {
                genericBootstrapImpl.providerResolver(this.resolver);
            }
            try {
                for (ValidationProvider<?> validationProvider : this.resolver.getValidationProviders()) {
                    if (this.validationProviderClass.isAssignableFrom(validationProvider.getClass())) {
                        return (T) this.validationProviderClass.cast(validationProvider).createSpecializedConfiguration(genericBootstrapImpl);
                    }
                }
                throw new ValidationException("Unable to find provider: " + this.validationProviderClass);
            } catch (RuntimeException e) {
                throw new ValidationException("Unable to get available provider resolvers.", e);
            }
        }
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:javax/validation/Validation$GenericBootstrapImpl.class */
    private static class GenericBootstrapImpl implements GenericBootstrap, BootstrapState {
        private ValidationProviderResolver resolver;
        private ValidationProviderResolver defaultResolver;

        private GenericBootstrapImpl() {
        }

        @Override // javax.validation.bootstrap.GenericBootstrap
        public GenericBootstrap providerResolver(ValidationProviderResolver resolver) {
            this.resolver = resolver;
            return this;
        }

        @Override // javax.validation.spi.BootstrapState
        public ValidationProviderResolver getValidationProviderResolver() {
            return this.resolver;
        }

        @Override // javax.validation.spi.BootstrapState
        public ValidationProviderResolver getDefaultValidationProviderResolver() {
            if (this.defaultResolver == null) {
                this.defaultResolver = new DefaultValidationProviderResolver();
            }
            return this.defaultResolver;
        }

        @Override // javax.validation.bootstrap.GenericBootstrap
        public Configuration<?> configure() {
            ValidationProviderResolver resolver = this.resolver == null ? getDefaultValidationProviderResolver() : this.resolver;
            try {
                List<ValidationProvider<?>> validationProviders = resolver.getValidationProviders();
                if (validationProviders.size() == 0) {
                    throw new ValidationException("Unable to create a Configuration, because no Bean Validation provider could be found. Add a provider like Hibernate Validator (RI) to your classpath.");
                }
                try {
                    Configuration<?> config = resolver.getValidationProviders().get(0).createGenericConfiguration(this);
                    return config;
                } catch (RuntimeException re) {
                    throw new ValidationException("Unable to instantiate Configuration.", re);
                }
            } catch (ValidationException e) {
                throw e;
            } catch (RuntimeException re2) {
                throw new ValidationException("Unable to get available provider resolvers.", re2);
            }
        }
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:javax/validation/Validation$DefaultValidationProviderResolver.class */
    private static class DefaultValidationProviderResolver implements ValidationProviderResolver {
        private DefaultValidationProviderResolver() {
        }

        @Override // javax.validation.ValidationProviderResolver
        public List<ValidationProvider<?>> getValidationProviders() {
            return GetValidationProviderListAction.getValidationProviderList();
        }
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:javax/validation/Validation$GetValidationProviderListAction.class */
    private static class GetValidationProviderListAction implements PrivilegedAction<List<ValidationProvider<?>>> {
        private static final WeakHashMap<ClassLoader, SoftReference<List<ValidationProvider<?>>>> providersPerClassloader = new WeakHashMap<>();

        private GetValidationProviderListAction() {
        }

        public static List<ValidationProvider<?>> getValidationProviderList() {
            GetValidationProviderListAction action = new GetValidationProviderListAction();
            if (System.getSecurityManager() != null) {
                return (List) AccessController.doPrivileged(action);
            }
            return action.run();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        public List<ValidationProvider<?>> run() {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            List<ValidationProvider<?>> cachedContextClassLoaderProviderList = getCachedValidationProviders(classloader);
            if (cachedContextClassLoaderProviderList != null) {
                return cachedContextClassLoaderProviderList;
            }
            List<ValidationProvider<?>> validationProviderList = loadProviders(classloader);
            if (validationProviderList.isEmpty()) {
                classloader = DefaultValidationProviderResolver.class.getClassLoader();
                List<ValidationProvider<?>> cachedCurrentClassLoaderProviderList = getCachedValidationProviders(classloader);
                if (cachedCurrentClassLoaderProviderList != null) {
                    return cachedCurrentClassLoaderProviderList;
                }
                validationProviderList = loadProviders(classloader);
            }
            cacheValidationProviders(classloader, validationProviderList);
            return validationProviderList;
        }

        private List<ValidationProvider<?>> loadProviders(ClassLoader classloader) {
            ServiceLoader<ValidationProvider> loader = ServiceLoader.load(ValidationProvider.class, classloader);
            Iterator<ValidationProvider> providerIterator = loader.iterator();
            List<ValidationProvider<?>> validationProviderList = new ArrayList<>();
            while (providerIterator.hasNext()) {
                try {
                    validationProviderList.add(providerIterator.next());
                } catch (ServiceConfigurationError e) {
                }
            }
            return validationProviderList;
        }

        private synchronized List<ValidationProvider<?>> getCachedValidationProviders(ClassLoader classLoader) {
            SoftReference<List<ValidationProvider<?>>> ref = providersPerClassloader.get(classLoader);
            if (ref != null) {
                return ref.get();
            }
            return null;
        }

        private synchronized void cacheValidationProviders(ClassLoader classLoader, List<ValidationProvider<?>> providers) {
            providersPerClassloader.put(classLoader, new SoftReference<>(providers));
        }
    }
}

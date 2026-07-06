// 
// Decompiled by Procyon v0.6.0
// 

package javax.validation;

import java.util.ServiceConfigurationError;
import java.util.ArrayList;
import java.util.ServiceLoader;
import java.security.AccessController;
import java.lang.ref.SoftReference;
import java.util.WeakHashMap;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.List;
import javax.validation.spi.BootstrapState;
import javax.validation.bootstrap.ProviderSpecificBootstrap;
import javax.validation.spi.ValidationProvider;
import javax.validation.bootstrap.GenericBootstrap;

public class Validation
{
    public static ValidatorFactory buildDefaultValidatorFactory() {
        return byDefaultProvider().configure().buildValidatorFactory();
    }
    
    public static GenericBootstrap byDefaultProvider() {
        return new GenericBootstrapImpl();
    }
    
    public static <T extends Configuration<T>, U extends ValidationProvider<T>> ProviderSpecificBootstrap<T> byProvider(final Class<U> providerType) {
        return new ProviderSpecificBootstrapImpl<T, Object>(providerType);
    }
    
    private static class ProviderSpecificBootstrapImpl<T extends Configuration<T>, U extends ValidationProvider<T>> implements ProviderSpecificBootstrap<T>
    {
        private final Class<U> validationProviderClass;
        private ValidationProviderResolver resolver;
        
        public ProviderSpecificBootstrapImpl(final Class<U> validationProviderClass) {
            this.validationProviderClass = validationProviderClass;
        }
        
        @Override
        public ProviderSpecificBootstrap<T> providerResolver(final ValidationProviderResolver resolver) {
            this.resolver = resolver;
            return this;
        }
        
        @Override
        public T configure() {
            if (this.validationProviderClass == null) {
                throw new ValidationException("builder is mandatory. Use Validation.byDefaultProvider() to use the generic provider discovery mechanism");
            }
            final GenericBootstrapImpl state = new GenericBootstrapImpl();
            if (this.resolver == null) {
                this.resolver = state.getDefaultValidationProviderResolver();
            }
            else {
                state.providerResolver(this.resolver);
            }
            List<ValidationProvider<?>> resolvers;
            try {
                resolvers = this.resolver.getValidationProviders();
            }
            catch (final RuntimeException re) {
                throw new ValidationException("Unable to get available provider resolvers.", re);
            }
            for (final ValidationProvider provider : resolvers) {
                if (this.validationProviderClass.isAssignableFrom(provider.getClass())) {
                    final ValidationProvider<T> specificProvider = this.validationProviderClass.cast(provider);
                    return specificProvider.createSpecializedConfiguration(state);
                }
            }
            throw new ValidationException("Unable to find provider: " + this.validationProviderClass);
        }
    }
    
    private static class GenericBootstrapImpl implements GenericBootstrap, BootstrapState
    {
        private ValidationProviderResolver resolver;
        private ValidationProviderResolver defaultResolver;
        
        @Override
        public GenericBootstrap providerResolver(final ValidationProviderResolver resolver) {
            this.resolver = resolver;
            return this;
        }
        
        @Override
        public ValidationProviderResolver getValidationProviderResolver() {
            return this.resolver;
        }
        
        @Override
        public ValidationProviderResolver getDefaultValidationProviderResolver() {
            if (this.defaultResolver == null) {
                this.defaultResolver = new DefaultValidationProviderResolver();
            }
            return this.defaultResolver;
        }
        
        @Override
        public Configuration<?> configure() {
            final ValidationProviderResolver resolver = (this.resolver == null) ? this.getDefaultValidationProviderResolver() : this.resolver;
            List<ValidationProvider<?>> validationProviders;
            try {
                validationProviders = resolver.getValidationProviders();
            }
            catch (final ValidationException e) {
                throw e;
            }
            catch (final RuntimeException re) {
                throw new ValidationException("Unable to get available provider resolvers.", re);
            }
            if (validationProviders.size() == 0) {
                final String msg = "Unable to create a Configuration, because no Bean Validation provider could be found. Add a provider like Hibernate Validator (RI) to your classpath.";
                throw new ValidationException(msg);
            }
            Configuration<?> config;
            try {
                config = resolver.getValidationProviders().get(0).createGenericConfiguration(this);
            }
            catch (final RuntimeException re2) {
                throw new ValidationException("Unable to instantiate Configuration.", re2);
            }
            return config;
        }
    }
    
    private static class DefaultValidationProviderResolver implements ValidationProviderResolver
    {
        @Override
        public List<ValidationProvider<?>> getValidationProviders() {
            return GetValidationProviderListAction.getValidationProviderList();
        }
    }
    
    private static class GetValidationProviderListAction implements PrivilegedAction<List<ValidationProvider<?>>>
    {
        private static final WeakHashMap<ClassLoader, SoftReference<List<ValidationProvider<?>>>> providersPerClassloader;
        
        public static List<ValidationProvider<?>> getValidationProviderList() {
            final GetValidationProviderListAction action = new GetValidationProviderListAction();
            if (System.getSecurityManager() != null) {
                return AccessController.doPrivileged((PrivilegedAction<List<ValidationProvider<?>>>)action);
            }
            return action.run();
        }
        
        @Override
        public List<ValidationProvider<?>> run() {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            final List<ValidationProvider<?>> cachedContextClassLoaderProviderList = this.getCachedValidationProviders(classloader);
            if (cachedContextClassLoaderProviderList != null) {
                return cachedContextClassLoaderProviderList;
            }
            List<ValidationProvider<?>> validationProviderList = this.loadProviders(classloader);
            if (validationProviderList.isEmpty()) {
                classloader = DefaultValidationProviderResolver.class.getClassLoader();
                final List<ValidationProvider<?>> cachedCurrentClassLoaderProviderList = this.getCachedValidationProviders(classloader);
                if (cachedCurrentClassLoaderProviderList != null) {
                    return cachedCurrentClassLoaderProviderList;
                }
                validationProviderList = this.loadProviders(classloader);
            }
            this.cacheValidationProviders(classloader, validationProviderList);
            return validationProviderList;
        }
        
        private List<ValidationProvider<?>> loadProviders(final ClassLoader classloader) {
            final ServiceLoader<ValidationProvider> loader = (ServiceLoader<ValidationProvider>)ServiceLoader.load(ValidationProvider.class, classloader);
            final Iterator<ValidationProvider> providerIterator = (Iterator<ValidationProvider>)loader.iterator();
            final List<ValidationProvider<?>> validationProviderList = new ArrayList<ValidationProvider<?>>();
            while (providerIterator.hasNext()) {
                try {
                    validationProviderList.add(providerIterator.next());
                }
                catch (final ServiceConfigurationError e) {}
            }
            return validationProviderList;
        }
        
        private synchronized List<ValidationProvider<?>> getCachedValidationProviders(final ClassLoader classLoader) {
            final SoftReference<List<ValidationProvider<?>>> ref = GetValidationProviderListAction.providersPerClassloader.get(classLoader);
            return (ref != null) ? ref.get() : null;
        }
        
        private synchronized void cacheValidationProviders(final ClassLoader classLoader, final List<ValidationProvider<?>> providers) {
            GetValidationProviderListAction.providersPerClassloader.put(classLoader, new SoftReference<List<ValidationProvider<?>>>(providers));
        }
        
        static {
            providersPerClassloader = new WeakHashMap<ClassLoader, SoftReference<List<ValidationProvider<?>>>>();
        }
    }
}

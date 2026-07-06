package javax.validation;

import java.util.Locale;
import javax.validation.metadata.ConstraintDescriptor;

/* JADX INFO: loaded from: leonware-0.0.3.jar:javax/validation/MessageInterpolator.class */
public interface MessageInterpolator {

    /* JADX INFO: loaded from: leonware-0.0.3.jar:javax/validation/MessageInterpolator$Context.class */
    public interface Context {
        ConstraintDescriptor<?> getConstraintDescriptor();

        Object getValidatedValue();

        <T> T unwrap(Class<T> cls);
    }

    String interpolate(String str, Context context);

    String interpolate(String str, Context context, Locale locale);
}

package javax.validation.metadata;

import java.util.Set;

/* JADX INFO: loaded from: leonware-0.0.3.jar:javax/validation/metadata/CascadableDescriptor.class */
public interface CascadableDescriptor {
    boolean isCascaded();

    Set<GroupConversionDescriptor> getGroupConversions();
}

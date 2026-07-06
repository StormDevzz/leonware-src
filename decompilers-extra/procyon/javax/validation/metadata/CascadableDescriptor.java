// 
// Decompiled by Procyon v0.6.0
// 

package javax.validation.metadata;

import java.util.Set;

public interface CascadableDescriptor
{
    boolean isCascaded();
    
    Set<GroupConversionDescriptor> getGroupConversions();
}

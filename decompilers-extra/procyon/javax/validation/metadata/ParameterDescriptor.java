// 
// Decompiled by Procyon v0.6.0
// 

package javax.validation.metadata;

public interface ParameterDescriptor extends ElementDescriptor, CascadableDescriptor
{
    int getIndex();
    
    String getName();
}

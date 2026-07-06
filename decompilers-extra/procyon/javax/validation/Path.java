// 
// Decompiled by Procyon v0.6.0
// 

package javax.validation;

import java.util.List;

public interface Path extends Iterable<Node>
{
    public interface PropertyNode extends Node
    {
    }
    
    public interface Node
    {
        String getName();
        
        boolean isInIterable();
        
        Integer getIndex();
        
        Object getKey();
        
        ElementKind getKind();
        
         <T extends Node> T as(final Class<T> p0);
    }
    
    public interface BeanNode extends Node
    {
    }
    
    public interface CrossParameterNode extends Node
    {
    }
    
    public interface ParameterNode extends Node
    {
        int getParameterIndex();
    }
    
    public interface ReturnValueNode extends Node
    {
    }
    
    public interface ConstructorNode extends Node
    {
        List<Class<?>> getParameterTypes();
    }
    
    public interface MethodNode extends Node
    {
        List<Class<?>> getParameterTypes();
    }
}

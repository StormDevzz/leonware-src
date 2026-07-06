// 
// Decompiled by Procyon v0.6.0
// 

package javax.validation;

public interface ConstraintValidatorContext
{
    void disableDefaultConstraintViolation();
    
    String getDefaultConstraintMessageTemplate();
    
    ConstraintViolationBuilder buildConstraintViolationWithTemplate(final String p0);
    
     <T> T unwrap(final Class<T> p0);
    
    public interface ConstraintViolationBuilder
    {
        @Deprecated
        NodeBuilderDefinedContext addNode(final String p0);
        
        NodeBuilderCustomizableContext addPropertyNode(final String p0);
        
        LeafNodeBuilderCustomizableContext addBeanNode();
        
        NodeBuilderDefinedContext addParameterNode(final int p0);
        
        ConstraintValidatorContext addConstraintViolation();
        
        public interface NodeContextBuilder
        {
            NodeBuilderDefinedContext atKey(final Object p0);
            
            NodeBuilderDefinedContext atIndex(final Integer p0);
            
            @Deprecated
            NodeBuilderCustomizableContext addNode(final String p0);
            
            NodeBuilderCustomizableContext addPropertyNode(final String p0);
            
            LeafNodeBuilderCustomizableContext addBeanNode();
            
            ConstraintValidatorContext addConstraintViolation();
        }
        
        public interface NodeBuilderDefinedContext
        {
            @Deprecated
            NodeBuilderCustomizableContext addNode(final String p0);
            
            NodeBuilderCustomizableContext addPropertyNode(final String p0);
            
            LeafNodeBuilderCustomizableContext addBeanNode();
            
            ConstraintValidatorContext addConstraintViolation();
        }
        
        public interface LeafNodeBuilderCustomizableContext
        {
            LeafNodeContextBuilder inIterable();
            
            ConstraintValidatorContext addConstraintViolation();
        }
        
        public interface LeafNodeContextBuilder
        {
            LeafNodeBuilderDefinedContext atKey(final Object p0);
            
            LeafNodeBuilderDefinedContext atIndex(final Integer p0);
            
            ConstraintValidatorContext addConstraintViolation();
        }
        
        public interface LeafNodeBuilderDefinedContext
        {
            ConstraintValidatorContext addConstraintViolation();
        }
        
        public interface NodeBuilderCustomizableContext
        {
            NodeContextBuilder inIterable();
            
            @Deprecated
            NodeBuilderCustomizableContext addNode(final String p0);
            
            NodeBuilderCustomizableContext addPropertyNode(final String p0);
            
            LeafNodeBuilderCustomizableContext addBeanNode();
            
            ConstraintValidatorContext addConstraintViolation();
        }
    }
}

/*
 * Decompiled with CFR 0.152.
 */
package javax.validation;

public interface ConstraintValidatorContext {
    public void disableDefaultConstraintViolation();

    public String getDefaultConstraintMessageTemplate();

    public ConstraintViolationBuilder buildConstraintViolationWithTemplate(String var1);

    public <T> T unwrap(Class<T> var1);

    public static interface ConstraintViolationBuilder {
        public NodeBuilderDefinedContext addNode(String var1);

        public NodeBuilderCustomizableContext addPropertyNode(String var1);

        public LeafNodeBuilderCustomizableContext addBeanNode();

        public NodeBuilderDefinedContext addParameterNode(int var1);

        public ConstraintValidatorContext addConstraintViolation();

        public static interface NodeContextBuilder {
            public NodeBuilderDefinedContext atKey(Object var1);

            public NodeBuilderDefinedContext atIndex(Integer var1);

            public NodeBuilderCustomizableContext addNode(String var1);

            public NodeBuilderCustomizableContext addPropertyNode(String var1);

            public LeafNodeBuilderCustomizableContext addBeanNode();

            public ConstraintValidatorContext addConstraintViolation();
        }

        public static interface NodeBuilderCustomizableContext {
            public NodeContextBuilder inIterable();

            public NodeBuilderCustomizableContext addNode(String var1);

            public NodeBuilderCustomizableContext addPropertyNode(String var1);

            public LeafNodeBuilderCustomizableContext addBeanNode();

            public ConstraintValidatorContext addConstraintViolation();
        }

        public static interface NodeBuilderDefinedContext {
            public NodeBuilderCustomizableContext addNode(String var1);

            public NodeBuilderCustomizableContext addPropertyNode(String var1);

            public LeafNodeBuilderCustomizableContext addBeanNode();

            public ConstraintValidatorContext addConstraintViolation();
        }

        public static interface LeafNodeContextBuilder {
            public LeafNodeBuilderDefinedContext atKey(Object var1);

            public LeafNodeBuilderDefinedContext atIndex(Integer var1);

            public ConstraintValidatorContext addConstraintViolation();
        }

        public static interface LeafNodeBuilderCustomizableContext {
            public LeafNodeContextBuilder inIterable();

            public ConstraintValidatorContext addConstraintViolation();
        }

        public static interface LeafNodeBuilderDefinedContext {
            public ConstraintValidatorContext addConstraintViolation();
        }
    }
}


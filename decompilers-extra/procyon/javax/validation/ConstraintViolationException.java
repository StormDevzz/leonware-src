// 
// Decompiled by Procyon v0.6.0
// 

package javax.validation;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ConstraintViolationException extends ValidationException
{
    private final Set<ConstraintViolation<?>> constraintViolations;
    
    public ConstraintViolationException(final String message, final Set<? extends ConstraintViolation<?>> constraintViolations) {
        super(message);
        if (constraintViolations == null) {
            this.constraintViolations = null;
        }
        else {
            this.constraintViolations = new HashSet<ConstraintViolation<?>>(constraintViolations);
        }
    }
    
    public ConstraintViolationException(final Set<? extends ConstraintViolation<?>> constraintViolations) {
        this(null, constraintViolations);
    }
    
    public Set<ConstraintViolation<?>> getConstraintViolations() {
        return this.constraintViolations;
    }
}

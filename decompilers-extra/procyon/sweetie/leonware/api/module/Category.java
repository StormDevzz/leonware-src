// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.module;

import lombok.Generated;

public enum Category
{
    COMBAT("Combat"), 
    MOVEMENT("Movement"), 
    RENDER("Render"), 
    PLAYER("Player"), 
    OTHER("Other");
    
    private final String label;
    
    @Generated
    public String getLabel() {
        return this.label;
    }
    
    @Generated
    private Category(final String label) {
        this.label = label;
    }
}

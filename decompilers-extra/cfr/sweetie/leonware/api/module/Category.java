/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.module;

import lombok.Generated;

public enum Category {
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
    private Category(String label) {
        this.label = label;
    }
}


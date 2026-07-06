package sweetie.leonware.api.module;

import lombok.Generated;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/module/Category.class */
public enum Category {
    COMBAT("Combat"),
    MOVEMENT("Movement"),
    RENDER("Render"),
    PLAYER("Player"),
    OTHER("Other");

    private final String label;

    @Generated
    Category(final String label) {
        this.label = label;
    }

    @Generated
    public String getLabel() {
        return this.label;
    }
}

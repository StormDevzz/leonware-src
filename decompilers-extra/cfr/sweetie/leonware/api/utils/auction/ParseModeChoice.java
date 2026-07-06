/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.utils.auction;

import lombok.Generated;
import sweetie.leonware.api.module.setting.ModeSetting;

public enum ParseModeChoice implements ModeSetting.NamedChoice
{
    FUN_TIME("Fun Time"),
    SPOOKY_TIME("Spooky Time"),
    HOLY_WORLD("Holy World"),
    REALLY_WORLD("Really World"),
    ARES_MINE("Ares Mine");

    private final String name;

    @Override
    public String getName() {
        return this.name;
    }

    @Generated
    private ParseModeChoice(String name) {
        this.name = name;
    }
}


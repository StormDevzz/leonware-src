package sweetie.leonware.api.utils.auction;

import lombok.Generated;
import sweetie.leonware.api.module.setting.ModeSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/auction/ParseModeChoice.class */
public enum ParseModeChoice implements ModeSetting.NamedChoice {
    FUN_TIME("Fun Time"),
    SPOOKY_TIME("Spooky Time"),
    HOLY_WORLD("Holy World"),
    REALLY_WORLD("Really World"),
    ARES_MINE("Ares Mine");

    private final String name;

    @Generated
    ParseModeChoice(final String name) {
        this.name = name;
    }

    @Override // sweetie.leonware.api.module.setting.ModeSetting.NamedChoice
    public String getName() {
        return this.name;
    }
}

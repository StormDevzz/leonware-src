package sweetie.leonware.client.features.modules.other;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_1703;
import net.minecraft.class_1707;
import net.minecraft.class_1735;
import net.minecraft.class_1799;
import net.minecraft.class_332;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.auction.ParseModeChoice;
import sweetie.leonware.api.utils.auction.PriceParser;
import sweetie.leonware.api.utils.render.RenderUtil;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/other/AuctionHelperModule.class */
@ModuleRegister(name = "Auction Helper", category = Category.OTHER)
public class AuctionHelperModule extends Module {
    private static final AuctionHelperModule instance = new AuctionHelperModule();
    private final PriceParser priceParser = new PriceParser();
    private final ModeSetting mode = new ModeSetting("Mode").value((Enum<?>) this.priceParser.currentMode).values(ParseModeChoice.values()).onAction2(() -> {
        ParseModeChoice parseModeChoice;
        PriceParser priceParser = this.priceParser;
        switch (getMode().getValue()) {
            case "Spooky Time":
                parseModeChoice = ParseModeChoice.SPOOKY_TIME;
                break;
            case "Holy World":
                parseModeChoice = ParseModeChoice.HOLY_WORLD;
                break;
            case "Really World":
                parseModeChoice = ParseModeChoice.REALLY_WORLD;
                break;
            case "Ares Mine":
                parseModeChoice = ParseModeChoice.ARES_MINE;
                break;
            default:
                parseModeChoice = ParseModeChoice.FUN_TIME;
                break;
        }
        priceParser.currentMode = parseModeChoice;
    });
    private final SliderSetting slots = new SliderSetting("Slots").value(Float.valueOf(3.0f)).range(1.0f, 6.0f).step(1.0f);
    private final List<class_1735> minPriceSlots = new ArrayList();

    @Generated
    public static AuctionHelperModule getInstance() {
        return instance;
    }

    @Generated
    public ModeSetting getMode() {
        return this.mode;
    }

    /* JADX WARN: Type inference failed for: r1v4, types: [sweetie.leonware.api.module.setting.ModeSetting] */
    public AuctionHelperModule() {
        addSettings(this.mode, this.slots);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            handleUpdateEvent();
        }));
        addEvents(updateEvent);
    }

    public void handleUpdateEvent() {
        class_1703 class_1703Var = mc.field_1724.field_7512;
        if (class_1703Var instanceof class_1707) {
            class_1707 chest = (class_1707) class_1703Var;
            String title = mc.field_1755.method_25440().getString();
            boolean isAuction = title.contains("0B1L2o3v") || title.contains("Аукцион") || title.contains("Поиск") || title.contains("Маркет") || title.contains("ꈁꀀꈂꌲꈂꀁ") || title.contains("[☃] Аукционы");
            if (isAuction) {
                this.minPriceSlots.clear();
                this.minPriceSlots.addAll(getMinPriceSlots(chest));
            }
        }
    }

    private List<class_1735> getMinPriceSlots(class_1707 chest) {
        return chest.field_7761.stream().filter(s -> {
            return (s.field_7874 > 44 || s.method_7677().method_7960() || getPrice(s.method_7677()) == -1) ? false : true;
        }).sorted((s1, s2) -> {
            return Integer.compare(getPrice(s1.method_7677()), getPrice(s2.method_7677()));
        }).limit(this.slots.getValue().intValue()).toList();
    }

    private int getPrice(class_1799 stack) {
        return this.priceParser.getPrice(stack);
    }

    public void onRenderChest(class_332 context, class_1735 slot) {
        if (this.minPriceSlots.contains(slot)) {
            int alpha = (int) (1.0d + (110.0d * Math.abs(Math.sin(System.currentTimeMillis() * 0.005d))));
            RenderUtil.RECT.draw(context.method_51448(), slot.field_7873, slot.field_7872, 16.0f, 16.0f, 0.0f, new Color(0, 255, 0, alpha));
        }
    }
}

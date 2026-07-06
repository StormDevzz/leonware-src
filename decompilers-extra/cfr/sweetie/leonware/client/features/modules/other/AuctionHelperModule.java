/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1703
 *  net.minecraft.class_1707
 *  net.minecraft.class_1735
 *  net.minecraft.class_1799
 *  net.minecraft.class_332
 */
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

@ModuleRegister(name="Auction Helper", category=Category.OTHER)
public class AuctionHelperModule
extends Module {
    private static final AuctionHelperModule instance = new AuctionHelperModule();
    private final PriceParser priceParser = new PriceParser();
    private final ModeSetting mode;
    private final SliderSetting slots;
    private final List<class_1735> minPriceSlots;

    public AuctionHelperModule() {
        this.mode = new ModeSetting("Mode").value(this.priceParser.currentMode).values(ParseModeChoice.values()).onAction(() -> {
            this.priceParser.currentMode = switch ((String)this.getMode().getValue()) {
                case "Spooky Time" -> ParseModeChoice.SPOOKY_TIME;
                case "Holy World" -> ParseModeChoice.HOLY_WORLD;
                case "Really World" -> ParseModeChoice.REALLY_WORLD;
                case "Ares Mine" -> ParseModeChoice.ARES_MINE;
                default -> ParseModeChoice.FUN_TIME;
            };
        });
        this.slots = new SliderSetting("Slots").value(Float.valueOf(3.0f)).range(1.0f, 6.0f).step(1.0f);
        this.minPriceSlots = new ArrayList<class_1735>();
        this.addSettings(this.mode, this.slots);
    }

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> this.handleUpdateEvent()));
        this.addEvents(updateEvent);
    }

    public void handleUpdateEvent() {
        boolean isAuction;
        class_1703 class_17032 = AuctionHelperModule.mc.field_1724.field_7512;
        if (!(class_17032 instanceof class_1707)) {
            return;
        }
        class_1707 chest = (class_1707)class_17032;
        String title = AuctionHelperModule.mc.field_1755.method_25440().getString();
        boolean bl = isAuction = title.contains("0B1L2o3v") || title.contains("\u0410\u0443\u043a\u0446\u0438\u043e\u043d") || title.contains("\u041f\u043e\u0438\u0441\u043a") || title.contains("\u041c\u0430\u0440\u043a\u0435\u0442") || title.contains("\ua201\ua000\ua202\ua332\ua202\ua001") || title.contains("[\u2603] \u0410\u0443\u043a\u0446\u0438\u043e\u043d\u044b");
        if (!isAuction) {
            return;
        }
        this.minPriceSlots.clear();
        this.minPriceSlots.addAll(this.getMinPriceSlots(chest));
    }

    private List<class_1735> getMinPriceSlots(class_1707 chest) {
        return chest.field_7761.stream().filter(s -> s.field_7874 <= 44 && !s.method_7677().method_7960() && this.getPrice(s.method_7677()) != -1).sorted((s1, s2) -> Integer.compare(this.getPrice(s1.method_7677()), this.getPrice(s2.method_7677()))).limit(((Float)this.slots.getValue()).intValue()).toList();
    }

    private int getPrice(class_1799 stack) {
        return this.priceParser.getPrice(stack);
    }

    public void onRenderChest(class_332 context, class_1735 slot) {
        if (this.minPriceSlots.contains(slot)) {
            int alpha = (int)(1.0 + 110.0 * Math.abs(Math.sin((double)System.currentTimeMillis() * 0.005)));
            RenderUtil.RECT.draw(context.method_51448(), (float)slot.field_7873, (float)slot.field_7872, 16.0f, 16.0f, 0.0f, new Color(0, 255, 0, alpha));
        }
    }

    @Generated
    public static AuctionHelperModule getInstance() {
        return instance;
    }

    @Generated
    public ModeSetting getMode() {
        return this.mode;
    }
}


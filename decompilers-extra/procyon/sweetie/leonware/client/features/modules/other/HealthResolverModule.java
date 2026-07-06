// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.other;

import lombok.Generated;
import net.minecraft.class_5250;
import net.minecraft.class_266;
import java.util.Iterator;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import net.minecraft.class_9013;
import net.minecraft.class_9022;
import net.minecraft.class_9025;
import net.minecraft.class_9015;
import net.minecraft.class_8646;
import sweetie.leonware.api.utils.player.PlayerUtil;
import net.minecraft.class_1657;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Health Resolver", category = Category.OTHER)
public class HealthResolverModule extends Module
{
    private static final HealthResolverModule instance;
    private final ModeSetting mode;
    
    public HealthResolverModule() {
        this.mode = new ModeSetting("Mode").value("Fun Time").values("Really World", "Fun Time");
        this.addSettings(this.mode);
    }
    
    public boolean isRW() {
        return this.mode.is("Really World") && this.isEnabled();
    }
    
    public boolean isFT() {
        return this.mode.is("Fun Time") && this.isEnabled();
    }
    
    @Override
    public void onEvent() {
        final EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener<TickEvent>(event -> {
            if (!this.isFT()) {
                return;
            }
            else if (HealthResolverModule.mc.method_1562() == null && HealthResolverModule.mc.method_1562().method_45734() == null) {
                return;
            }
            else {
                HealthResolverModule.mc.field_1687.method_18456().iterator();
                final Iterator iterator;
                while (iterator.hasNext()) {
                    final class_1657 player = iterator.next();
                    if (player == HealthResolverModule.mc.field_1724) {
                        continue;
                    }
                    else if (!PlayerUtil.isValidName(player.method_5477().getString())) {
                        continue;
                    }
                    else {
                        final class_266 scoreboard = null;
                        String parsedHealth = "";
                        if (player.method_7327().method_1189(class_8646.field_45158) != null) {
                            final class_266 scoreboard2 = player.method_7327().method_1189(class_8646.field_45158);
                            if (scoreboard2 != null) {
                                final class_9013 readableScoreboardScore = player.method_7327().method_55430((class_9015)player, scoreboard2);
                                final class_5250 mutableText = class_9013.method_55398(readableScoreboardScore, scoreboard2.method_55380((class_9022)class_9025.field_47566));
                                parsedHealth = mutableText.getString();
                            }
                        }
                        float resolvedHealth = 0.0f;
                        try {
                            resolvedHealth = Float.parseFloat(parsedHealth);
                        }
                        catch (final NumberFormatException ex) {}
                        if (!parsedHealth.isEmpty() && !parsedHealth.equals("0")) {
                            player.method_6033(resolvedHealth);
                        }
                        else {
                            continue;
                        }
                    }
                }
                return;
            }
        }));
        this.addEvents(tickEvent);
    }
    
    @Generated
    public static HealthResolverModule getInstance() {
        return HealthResolverModule.instance;
    }
    
    static {
        instance = new HealthResolverModule();
    }
}

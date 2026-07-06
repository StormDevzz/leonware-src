// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import net.minecraft.class_1799;
import net.minecraft.class_1703;
import net.minecraft.class_437;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import net.minecraft.class_1657;
import net.minecraft.class_1713;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import net.minecraft.class_1722;
import net.minecraft.class_488;
import net.minecraft.class_1733;
import net.minecraft.class_495;
import net.minecraft.class_1707;
import net.minecraft.class_476;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.setting.Setting;
import java.util.Random;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Chest Stealer", category = Category.PLAYER)
public class ChestStealerModule extends Module
{
    private static final ChestStealerModule instance;
    private final SliderSetting delay;
    private final BooleanSetting randomize;
    private final Random random;
    private long lastClickTime;
    
    public ChestStealerModule() {
        this.delay = new SliderSetting("\u0417\u0430\u0434\u0435\u0440\u0436\u043a\u0430").value(50.0f).range(0.0f, 1000.0f).step(10.0f);
        this.randomize = new BooleanSetting("\u0420\u0430\u043d\u0434\u043e\u043c\u0438\u0437\u0430\u0446\u0438\u044f").value(false);
        this.random = new Random();
        this.lastClickTime = 0L;
        this.addSettings(this.delay, this.randomize);
    }
    
    @Override
    public void onEnable() {
        this.lastClickTime = 0L;
    }
    
    @Override
    public void onEvent() {
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (ChestStealerModule.mc.field_1724 == null || ChestStealerModule.mc.field_1687 == null || ChestStealerModule.mc.field_1761 == null) {
                return;
            }
            else if (ChestStealerModule.mc.field_1755 == null) {
                return;
            }
            else {
                final class_437 patt0$temp = ChestStealerModule.mc.field_1755;
                class_1703 handler;
                int chestSize;
                if (patt0$temp instanceof final class_476 screen) {
                    final class_1707 h = (class_1707)(handler = screen.method_17577());
                    chestSize = h.method_17388() * 9;
                }
                else {
                    final class_437 patt1$temp = ChestStealerModule.mc.field_1755;
                    if (patt1$temp instanceof final class_495 screen2) {
                        final class_1733 h2 = (class_1733)(handler = screen2.method_17577());
                        chestSize = 27;
                    }
                    else {
                        final class_437 patt2$temp = ChestStealerModule.mc.field_1755;
                        if (patt2$temp instanceof final class_488 screen3) {
                            final class_1722 h3 = (class_1722)(handler = screen3.method_17577());
                            chestSize = 5;
                        }
                        else {
                            return;
                        }
                    }
                }
                final long currentTime = System.currentTimeMillis();
                if (currentTime - this.lastClickTime < this.delay.getValue().longValue()) {
                    return;
                }
                else {
                    final ArrayList indexes = new ArrayList<Integer>(chestSize);
                    for (int i = 0; i < chestSize; ++i) {
                        final class_1799 stack = handler.method_7611(i).method_7677();
                        if (!stack.method_7960()) {
                            indexes.add(i);
                        }
                    }
                    if (indexes.isEmpty()) {
                        return;
                    }
                    else {
                        if (this.randomize.getValue()) {
                            Collections.shuffle(indexes, this.random);
                        }
                        final int slot = (int)indexes.get(0);
                        ChestStealerModule.mc.field_1761.method_2906(handler.field_7763, slot, 0, class_1713.field_7794, (class_1657)ChestStealerModule.mc.field_1724);
                        this.lastClickTime = currentTime;
                        return;
                    }
                }
            }
        }));
        this.addEvents(updateEvent);
    }
    
    @Generated
    public static ChestStealerModule getInstance() {
        return ChestStealerModule.instance;
    }
    
    static {
        instance = new ChestStealerModule();
    }
}

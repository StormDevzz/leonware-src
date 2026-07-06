// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import net.minecraft.class_8875;
import net.minecraft.class_2873;
import net.minecraft.class_2811;
import net.minecraft.class_2813;
import net.minecraft.class_3532;
import net.minecraft.class_3675;
import sweetie.leonware.api.utils.other.SlownessManager;
import java.util.Iterator;
import sweetie.leonware.api.system.client.ThreadManager;
import net.minecraft.class_304;
import net.minecraft.class_437;
import net.minecraft.class_2815;
import java.util.Collection;
import sweetie.leonware.api.utils.player.MoveUtil;
import net.minecraft.class_490;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.CloseScreenEvent;
import net.minecraft.class_497;
import net.minecraft.class_463;
import net.minecraft.class_471;
import net.minecraft.class_498;
import net.minecraft.class_408;
import java.util.function.Supplier;
import java.util.Objects;
import sweetie.leonware.api.module.setting.Setting;
import net.minecraft.class_2596;
import java.util.LinkedList;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Inventory Move", category = Category.MOVEMENT)
public class InventoryMoveModule extends Module
{
    private static final InventoryMoveModule instance;
    public final ModeSetting swapMode;
    public final BooleanSetting multiActionsC;
    public final BooleanSetting allowSneak;
    public final BooleanSetting rotateInArrow;
    public final SliderSetting rotationSpeed;
    private final LinkedList<class_2596<?>> packet;
    private volatile boolean processingClick;
    
    public InventoryMoveModule() {
        this.swapMode = new ModeSetting("Swap mode").value("Grim").values("Basic", "Grim", "Legit", "AresMine");
        this.multiActionsC = new BooleanSetting("MultiActionsC").value(false);
        this.allowSneak = new BooleanSetting("\u0412\u043e\u0437\u043c\u043e\u0436\u043d\u043e\u0441\u0442\u044c \u0441\u0438\u0434\u0435\u0442\u044c").value(false);
        this.rotateInArrow = new BooleanSetting("\u041f\u043e\u0432\u043e\u0440\u0430\u0447\u0438\u0432\u0430\u0442\u044c\u0441\u044f \u043f\u043e \u0441\u0442\u0440\u0435\u043b\u043e\u0447\u043a\u0430\u043c").value(false);
        this.rotationSpeed = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c \u043f\u043e\u0432\u043e\u0440\u043e\u0442\u0430").value(2.0f).range(0.1f, 10.0f).step(0.1f);
        this.packet = new LinkedList<class_2596<?>>();
        this.processingClick = false;
        this.multiActionsC.setVisible(this::isGrim);
        this.addSettings(this.swapMode, this.multiActionsC, this.allowSneak, this.rotateInArrow, this.rotationSpeed);
        final SliderSetting rotationSpeed = this.rotationSpeed;
        final BooleanSetting rotateInArrow = this.rotateInArrow;
        Objects.requireNonNull(rotateInArrow);
        rotationSpeed.setVisible((Supplier<Boolean>)rotateInArrow::getValue);
    }
    
    public boolean isLegit() {
        return this.swapMode.is("Legit");
    }
    
    public boolean isGrim() {
        return this.swapMode.is("Grim");
    }
    
    public boolean isBasic() {
        return this.swapMode.is("Basic");
    }
    
    public boolean isAresMine() {
        return this.swapMode.is("AresMine");
    }
    
    public boolean isMovementAllowedInScreen() {
        return InventoryMoveModule.mc.field_1755 != null && !(InventoryMoveModule.mc.field_1755 instanceof class_408) && !(InventoryMoveModule.mc.field_1755 instanceof class_498) && !(InventoryMoveModule.mc.field_1755 instanceof class_471) && !(InventoryMoveModule.mc.field_1755 instanceof class_463) && !(InventoryMoveModule.mc.field_1755 instanceof class_497);
    }
    
    @Override
    public void onEvent() {
        final EventListener closeScreenEvent = CloseScreenEvent.getInstance().subscribe(new Listener<CloseScreenEvent>(event -> this.closeScreenEvent()));
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> this.updateEvent()));
        final EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(event -> this.packetEvent(event)));
        this.addEvents(closeScreenEvent, updateEvent, packetEvent);
    }
    
    private void closeScreenEvent() {
        if (!this.isLegit() && !this.isAresMine()) {
            return;
        }
        if (!(InventoryMoveModule.mc.field_1755 instanceof class_490)) {
            return;
        }
        boolean[] keysSnapshot = null;
        if (!this.isAresMine()) {
            if (!this.packet.isEmpty()) {
                if (!MoveUtil.isMoving() && !InventoryMoveModule.mc.field_1690.field_1903.method_1434()) {
                    while (!this.packet.isEmpty()) {
                        this.sendPacket(this.packet.removeFirst());
                    }
                    return;
                }
                CloseScreenEvent.getInstance().setCancel(true);
                keysSnapshot = new boolean[] { InventoryMoveModule.mc.field_1690.field_1894.method_1434(), InventoryMoveModule.mc.field_1690.field_1881.method_1434(), InventoryMoveModule.mc.field_1690.field_1913.method_1434(), InventoryMoveModule.mc.field_1690.field_1849.method_1434(), InventoryMoveModule.mc.field_1690.field_1903.method_1434(), InventoryMoveModule.mc.field_1690.field_1867.method_1434() };
                final LinkedList<class_2596<?>> packetsCopy = new LinkedList<class_2596<?>>(this.packet);
                this.packet.clear();
                ThreadManager.run(() -> {
                    MoveUtil.getMovementKeys();
                    final class_304[] array;
                    int i = 0;
                    for (int length = array.length; i < length; ++i) {
                        final class_304 key = array[i];
                        key.method_23481(false);
                    }
                    try {
                        Thread.sleep(60L);
                    }
                    catch (final InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    packetsCopy.iterator();
                    final Iterator iterator;
                    while (iterator.hasNext()) {
                        final class_2596<?> pkt = (class_2596<?>)iterator.next();
                        this.sendPacket(pkt);
                        try {
                            Thread.sleep(8L);
                        }
                        catch (final InterruptedException ex2) {
                            throw new RuntimeException(ex2);
                        }
                    }
                    try {
                        Thread.sleep(20L);
                    }
                    catch (final InterruptedException ex3) {
                        throw new RuntimeException(ex3);
                    }
                    this.sendPacket((class_2596<?>)new class_2815(0));
                    InventoryMoveModule.mc.execute(() -> {
                        InventoryMoveModule.mc.method_1507((class_437)null);
                        final class_304[] keys = { InventoryMoveModule.mc.field_1690.field_1894, InventoryMoveModule.mc.field_1690.field_1881, InventoryMoveModule.mc.field_1690.field_1913, InventoryMoveModule.mc.field_1690.field_1849, InventoryMoveModule.mc.field_1690.field_1903, InventoryMoveModule.mc.field_1690.field_1867 };
                        for (int j = 0; j < keys.length; ++j) {
                            keys[j].method_23481(keysSnapshot[j]);
                        }
                    });
                });
            }
            return;
        }
        final boolean isMoving = MoveUtil.isMoving() || InventoryMoveModule.mc.field_1690.field_1903.method_1434() || InventoryMoveModule.mc.field_1724.method_5624();
        if (!isMoving) {
            return;
        }
        CloseScreenEvent.getInstance().setCancel(true);
        final boolean[] keysSnapshot2 = { InventoryMoveModule.mc.field_1690.field_1894.method_1434(), InventoryMoveModule.mc.field_1690.field_1881.method_1434(), InventoryMoveModule.mc.field_1690.field_1913.method_1434(), InventoryMoveModule.mc.field_1690.field_1849.method_1434(), InventoryMoveModule.mc.field_1690.field_1903.method_1434(), InventoryMoveModule.mc.field_1690.field_1867.method_1434() };
        this.processingClick = true;
        ThreadManager.run(() -> {
            MoveUtil.getMovementKeys();
            final class_304[] array2;
            int k = 0;
            for (int length2 = array2.length; k < length2; ++k) {
                final class_304 key2 = array2[k];
                key2.method_23481(false);
            }
            InventoryMoveModule.mc.field_1724.method_5728(false);
            InventoryMoveModule.mc.field_1690.field_1894.method_23481(false);
            try {
                Thread.sleep(50L);
                InventoryMoveModule.mc.execute(() -> {
                    InventoryMoveModule.mc.method_1507((class_437)null);
                    InventoryMoveModule.mc.field_1724.field_3944.method_52787((class_2596)new class_2815(0));
                    return;
                });
                Thread.sleep(40L);
                InventoryMoveModule.mc.execute(() -> {
                    final class_304[] keys2 = { InventoryMoveModule.mc.field_1690.field_1894, InventoryMoveModule.mc.field_1690.field_1881, InventoryMoveModule.mc.field_1690.field_1913, InventoryMoveModule.mc.field_1690.field_1849, InventoryMoveModule.mc.field_1690.field_1903, InventoryMoveModule.mc.field_1690.field_1867 };
                    for (int l = 0; l < keys2.length; ++l) {
                        keys2[l].method_23481(keysSnapshot[l]);
                    }
                    return;
                });
                Thread.sleep(300L);
                InventoryMoveModule.mc.execute(() -> {
                    this.syncKeysWithPhysicalState();
                    this.processingClick = false;
                });
            }
            catch (final InterruptedException ex4) {
                this.processingClick = false;
                throw new RuntimeException(ex4);
            }
        });
    }
    
    private void updateEvent() {
        if (!this.isMovementAllowedInScreen()) {
            return;
        }
        if (this.isAresMine() && this.processingClick) {
            return;
        }
        if (!SlownessManager.slowed) {
            MoveUtil.updateMovementKeys();
        }
        if (this.allowSneak.getValue() && InventoryMoveModule.mc.field_1755 != null) {
            InventoryMoveModule.mc.field_1690.field_1832.method_23481(class_3675.method_15987(InventoryMoveModule.mc.method_22683().method_4490(), class_3675.method_15981(InventoryMoveModule.mc.field_1690.field_1832.method_1428()).method_1444()));
        }
        if (this.rotateInArrow.getValue() && InventoryMoveModule.mc.field_1755 != null) {
            final float speed = this.rotationSpeed.getValue();
            float yaw = InventoryMoveModule.mc.field_1724.method_36454();
            float pitch = InventoryMoveModule.mc.field_1724.method_36455();
            final long handle = InventoryMoveModule.mc.method_22683().method_4490();
            if (class_3675.method_15987(handle, 263)) {
                yaw -= speed;
            }
            if (class_3675.method_15987(handle, 262)) {
                yaw += speed;
            }
            if (class_3675.method_15987(handle, 265)) {
                pitch -= speed;
            }
            if (class_3675.method_15987(handle, 264)) {
                pitch += speed;
            }
            InventoryMoveModule.mc.field_1724.method_36456(yaw);
            InventoryMoveModule.mc.field_1724.method_36457(class_3532.method_15363(pitch, -90.0f, 90.0f));
        }
    }
    
    public boolean isProcessing() {
        return this.processingClick;
    }
    
    private void packetEvent(final PacketEvent.PacketEventData event) {
        if (event.isReceive()) {
            return;
        }
        if (this.isAresMine()) {
            this.handleAresMinePacket(event);
            return;
        }
        if (!this.isLegit() || !event.isSend()) {
            return;
        }
        if (!MoveUtil.isMoving() && !InventoryMoveModule.mc.field_1690.field_1903.method_1434()) {
            return;
        }
        final class_2596<?> pacl = event.packet();
        if (InventoryMoveModule.mc.field_1755 instanceof class_490 && (pacl instanceof class_2813 || pacl instanceof class_2811 || pacl instanceof class_2873 || pacl instanceof class_8875)) {
            this.packet.add(pacl);
            PacketEvent.getInstance().setCancel(true);
        }
    }
    
    private void handleAresMinePacket(final PacketEvent.PacketEventData event) {
        final class_2596<?> pkt = event.packet();
        if (!this.isMovementAllowedInScreen()) {
            return;
        }
        final boolean isClickPacket = pkt instanceof class_2813 || pkt instanceof class_2811 || pkt instanceof class_2873 || pkt instanceof class_8875;
        if (!isClickPacket) {
            return;
        }
        final boolean isMoving = MoveUtil.isMoving() || InventoryMoveModule.mc.field_1690.field_1903.method_1434() || InventoryMoveModule.mc.field_1724.method_5624();
        if (!isMoving) {
            return;
        }
        PacketEvent.getInstance().setCancel(true);
        final boolean[] keysSnapshot = { InventoryMoveModule.mc.field_1690.field_1894.method_1434(), InventoryMoveModule.mc.field_1690.field_1881.method_1434(), InventoryMoveModule.mc.field_1690.field_1913.method_1434(), InventoryMoveModule.mc.field_1690.field_1849.method_1434(), InventoryMoveModule.mc.field_1690.field_1903.method_1434(), InventoryMoveModule.mc.field_1690.field_1867.method_1434() };
        this.processingClick = true;
        InventoryMoveModule.mc.execute(() -> {
            MoveUtil.getMovementKeys();
            final class_304[] array;
            int i = 0;
            for (int length = array.length; i < length; ++i) {
                final class_304 key = array[i];
                key.method_23481(false);
            }
            InventoryMoveModule.mc.field_1724.method_5728(false);
            return;
        });
        ThreadManager.run(() -> {
            try {
                Thread.sleep(80L);
                this.sendPacket((class_2596<?>)pkt);
                Thread.sleep(40L);
                InventoryMoveModule.mc.execute(() -> {
                    final class_304[] keys = { InventoryMoveModule.mc.field_1690.field_1894, InventoryMoveModule.mc.field_1690.field_1881, InventoryMoveModule.mc.field_1690.field_1913, InventoryMoveModule.mc.field_1690.field_1849, InventoryMoveModule.mc.field_1690.field_1903, InventoryMoveModule.mc.field_1690.field_1867 };
                    for (int j = 0; j < keys.length; ++j) {
                        keys[j].method_23481(keysSnapshot[j]);
                    }
                    return;
                });
                Thread.sleep(300L);
                InventoryMoveModule.mc.execute(() -> {
                    this.syncKeysWithPhysicalState();
                    this.processingClick = false;
                });
            }
            catch (final InterruptedException ex) {
                this.processingClick = false;
                throw new RuntimeException(ex);
            }
        });
    }
    
    private void syncKeysWithPhysicalState() {
        if (InventoryMoveModule.mc.field_1724 == null || InventoryMoveModule.mc.method_22683() == null) {
            return;
        }
        final long handle = InventoryMoveModule.mc.method_22683().method_4490();
        final class_304[] array;
        final class_304[] movementKeys = array = new class_304[] { InventoryMoveModule.mc.field_1690.field_1894, InventoryMoveModule.mc.field_1690.field_1881, InventoryMoveModule.mc.field_1690.field_1913, InventoryMoveModule.mc.field_1690.field_1849, InventoryMoveModule.mc.field_1690.field_1903, InventoryMoveModule.mc.field_1690.field_1867, InventoryMoveModule.mc.field_1690.field_1832 };
        for (final class_304 key : array) {
            final boolean physicallyPressed = class_3675.method_15987(handle, key.method_1429().method_1444());
            if (key.method_1434() && !physicallyPressed) {
                key.method_23481(false);
            }
        }
    }
    
    @Generated
    public static InventoryMoveModule getInstance() {
        return InventoryMoveModule.instance;
    }
    
    static {
        instance = new InventoryMoveModule();
    }
}

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_2596
 *  net.minecraft.class_2811
 *  net.minecraft.class_2813
 *  net.minecraft.class_2815
 *  net.minecraft.class_2873
 *  net.minecraft.class_304
 *  net.minecraft.class_3532
 *  net.minecraft.class_3675
 *  net.minecraft.class_408
 *  net.minecraft.class_463
 *  net.minecraft.class_471
 *  net.minecraft.class_490
 *  net.minecraft.class_497
 *  net.minecraft.class_498
 *  net.minecraft.class_8875
 */
package sweetie.leonware.client.features.modules.movement;

import java.util.LinkedList;
import lombok.Generated;
import net.minecraft.class_2596;
import net.minecraft.class_2811;
import net.minecraft.class_2813;
import net.minecraft.class_2815;
import net.minecraft.class_2873;
import net.minecraft.class_304;
import net.minecraft.class_3532;
import net.minecraft.class_3675;
import net.minecraft.class_408;
import net.minecraft.class_463;
import net.minecraft.class_471;
import net.minecraft.class_490;
import net.minecraft.class_497;
import net.minecraft.class_498;
import net.minecraft.class_8875;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.events.player.other.CloseScreenEvent;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.system.client.ThreadManager;
import sweetie.leonware.api.utils.other.SlownessManager;
import sweetie.leonware.api.utils.player.MoveUtil;

@ModuleRegister(name="Inventory Move", category=Category.MOVEMENT)
public class InventoryMoveModule
extends Module {
    private static final InventoryMoveModule instance = new InventoryMoveModule();
    public final ModeSetting swapMode = new ModeSetting("Swap mode").value("Grim").values("Basic", "Grim", "Legit", "AresMine");
    public final BooleanSetting multiActionsC = new BooleanSetting("MultiActionsC").value(false);
    public final BooleanSetting allowSneak = new BooleanSetting("\u0412\u043e\u0437\u043c\u043e\u0436\u043d\u043e\u0441\u0442\u044c \u0441\u0438\u0434\u0435\u0442\u044c").value(false);
    public final BooleanSetting rotateInArrow = new BooleanSetting("\u041f\u043e\u0432\u043e\u0440\u0430\u0447\u0438\u0432\u0430\u0442\u044c\u0441\u044f \u043f\u043e \u0441\u0442\u0440\u0435\u043b\u043e\u0447\u043a\u0430\u043c").value(false);
    public final SliderSetting rotationSpeed = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c \u043f\u043e\u0432\u043e\u0440\u043e\u0442\u0430").value(Float.valueOf(2.0f)).range(0.1f, 10.0f).step(0.1f);
    private final LinkedList<class_2596<?>> packet = new LinkedList();
    private volatile boolean processingClick = false;

    public InventoryMoveModule() {
        this.multiActionsC.setVisible(this::isGrim);
        this.addSettings(this.swapMode, this.multiActionsC, this.allowSneak, this.rotateInArrow, this.rotationSpeed);
        this.rotationSpeed.setVisible(this.rotateInArrow::getValue);
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
        if (InventoryMoveModule.mc.field_1755 == null) {
            return false;
        }
        return !(InventoryMoveModule.mc.field_1755 instanceof class_408) && !(InventoryMoveModule.mc.field_1755 instanceof class_498) && !(InventoryMoveModule.mc.field_1755 instanceof class_471) && !(InventoryMoveModule.mc.field_1755 instanceof class_463) && !(InventoryMoveModule.mc.field_1755 instanceof class_497);
    }

    @Override
    public void onEvent() {
        EventListener closeScreenEvent = CloseScreenEvent.getInstance().subscribe(new Listener<CloseScreenEvent>(event -> this.closeScreenEvent()));
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> this.updateEvent()));
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(event -> this.packetEvent((PacketEvent.PacketEventData)event)));
        this.addEvents(closeScreenEvent, updateEvent, packetEvent);
    }

    private void closeScreenEvent() {
        if (!this.isLegit() && !this.isAresMine()) {
            return;
        }
        if (!(InventoryMoveModule.mc.field_1755 instanceof class_490)) {
            return;
        }
        if (this.isAresMine()) {
            boolean isMoving;
            boolean bl = isMoving = MoveUtil.isMoving() || InventoryMoveModule.mc.field_1690.field_1903.method_1434() || InventoryMoveModule.mc.field_1724.method_5624();
            if (!isMoving) {
                return;
            }
            CloseScreenEvent.getInstance().setCancel(true);
            boolean[] keysSnapshot = new boolean[]{InventoryMoveModule.mc.field_1690.field_1894.method_1434(), InventoryMoveModule.mc.field_1690.field_1881.method_1434(), InventoryMoveModule.mc.field_1690.field_1913.method_1434(), InventoryMoveModule.mc.field_1690.field_1849.method_1434(), InventoryMoveModule.mc.field_1690.field_1903.method_1434(), InventoryMoveModule.mc.field_1690.field_1867.method_1434()};
            this.processingClick = true;
            ThreadManager.run(() -> {
                for (class_304 key : MoveUtil.getMovementKeys()) {
                    key.method_23481(false);
                }
                InventoryMoveModule.mc.field_1724.method_5728(false);
                InventoryMoveModule.mc.field_1690.field_1894.method_23481(false);
                try {
                    Thread.sleep(50L);
                    mc.execute(() -> {
                        mc.method_1507(null);
                        InventoryMoveModule.mc.field_1724.field_3944.method_52787((class_2596)new class_2815(0));
                    });
                    Thread.sleep(40L);
                    mc.execute(() -> {
                        class_304[] keys = new class_304[]{InventoryMoveModule.mc.field_1690.field_1894, InventoryMoveModule.mc.field_1690.field_1881, InventoryMoveModule.mc.field_1690.field_1913, InventoryMoveModule.mc.field_1690.field_1849, InventoryMoveModule.mc.field_1690.field_1903, InventoryMoveModule.mc.field_1690.field_1867};
                        for (int i = 0; i < keys.length; ++i) {
                            keys[i].method_23481(keysSnapshot[i]);
                        }
                    });
                    Thread.sleep(300L);
                    mc.execute(() -> {
                        this.syncKeysWithPhysicalState();
                        this.processingClick = false;
                    });
                }
                catch (InterruptedException ex) {
                    this.processingClick = false;
                    throw new RuntimeException(ex);
                }
            });
            return;
        }
        if (!this.packet.isEmpty()) {
            if (!MoveUtil.isMoving() && !InventoryMoveModule.mc.field_1690.field_1903.method_1434()) {
                while (!this.packet.isEmpty()) {
                    this.sendPacket(this.packet.removeFirst());
                }
                return;
            }
            CloseScreenEvent.getInstance().setCancel(true);
            boolean[] keysSnapshot = new boolean[]{InventoryMoveModule.mc.field_1690.field_1894.method_1434(), InventoryMoveModule.mc.field_1690.field_1881.method_1434(), InventoryMoveModule.mc.field_1690.field_1913.method_1434(), InventoryMoveModule.mc.field_1690.field_1849.method_1434(), InventoryMoveModule.mc.field_1690.field_1903.method_1434(), InventoryMoveModule.mc.field_1690.field_1867.method_1434()};
            LinkedList packetsCopy = new LinkedList(this.packet);
            this.packet.clear();
            ThreadManager.run(() -> {
                for (class_304 key : MoveUtil.getMovementKeys()) {
                    key.method_23481(false);
                }
                try {
                    Thread.sleep(60L);
                }
                catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                for (class_2596 pkt : packetsCopy) {
                    this.sendPacket(pkt);
                    try {
                        Thread.sleep(8L);
                    }
                    catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                try {
                    Thread.sleep(20L);
                }
                catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                this.sendPacket((class_2596<?>)new class_2815(0));
                mc.execute(() -> {
                    mc.method_1507(null);
                    class_304[] keys = new class_304[]{InventoryMoveModule.mc.field_1690.field_1894, InventoryMoveModule.mc.field_1690.field_1881, InventoryMoveModule.mc.field_1690.field_1913, InventoryMoveModule.mc.field_1690.field_1849, InventoryMoveModule.mc.field_1690.field_1903, InventoryMoveModule.mc.field_1690.field_1867};
                    for (int i = 0; i < keys.length; ++i) {
                        keys[i].method_23481(keysSnapshot[i]);
                    }
                });
            });
        }
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
        if (((Boolean)this.allowSneak.getValue()).booleanValue() && InventoryMoveModule.mc.field_1755 != null) {
            InventoryMoveModule.mc.field_1690.field_1832.method_23481(class_3675.method_15987((long)mc.method_22683().method_4490(), (int)class_3675.method_15981((String)InventoryMoveModule.mc.field_1690.field_1832.method_1428()).method_1444()));
        }
        if (((Boolean)this.rotateInArrow.getValue()).booleanValue() && InventoryMoveModule.mc.field_1755 != null) {
            float speed = ((Float)this.rotationSpeed.getValue()).floatValue();
            float yaw = InventoryMoveModule.mc.field_1724.method_36454();
            float pitch = InventoryMoveModule.mc.field_1724.method_36455();
            long handle = mc.method_22683().method_4490();
            if (class_3675.method_15987((long)handle, (int)263)) {
                yaw -= speed;
            }
            if (class_3675.method_15987((long)handle, (int)262)) {
                yaw += speed;
            }
            if (class_3675.method_15987((long)handle, (int)265)) {
                pitch -= speed;
            }
            if (class_3675.method_15987((long)handle, (int)264)) {
                pitch += speed;
            }
            InventoryMoveModule.mc.field_1724.method_36456(yaw);
            InventoryMoveModule.mc.field_1724.method_36457(class_3532.method_15363((float)pitch, (float)-90.0f, (float)90.0f));
        }
    }

    public boolean isProcessing() {
        return this.processingClick;
    }

    private void packetEvent(PacketEvent.PacketEventData event) {
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
        class_2596<?> pacl = event.packet();
        if (InventoryMoveModule.mc.field_1755 instanceof class_490 && (pacl instanceof class_2813 || pacl instanceof class_2811 || pacl instanceof class_2873 || pacl instanceof class_8875)) {
            this.packet.add(pacl);
            PacketEvent.getInstance().setCancel(true);
        }
    }

    private void handleAresMinePacket(PacketEvent.PacketEventData event) {
        boolean isMoving;
        boolean isClickPacket;
        class_2596<?> pkt = event.packet();
        if (!this.isMovementAllowedInScreen()) {
            return;
        }
        boolean bl = isClickPacket = pkt instanceof class_2813 || pkt instanceof class_2811 || pkt instanceof class_2873 || pkt instanceof class_8875;
        if (!isClickPacket) {
            return;
        }
        boolean bl2 = isMoving = MoveUtil.isMoving() || InventoryMoveModule.mc.field_1690.field_1903.method_1434() || InventoryMoveModule.mc.field_1724.method_5624();
        if (!isMoving) {
            return;
        }
        PacketEvent.getInstance().setCancel(true);
        boolean[] keysSnapshot = new boolean[]{InventoryMoveModule.mc.field_1690.field_1894.method_1434(), InventoryMoveModule.mc.field_1690.field_1881.method_1434(), InventoryMoveModule.mc.field_1690.field_1913.method_1434(), InventoryMoveModule.mc.field_1690.field_1849.method_1434(), InventoryMoveModule.mc.field_1690.field_1903.method_1434(), InventoryMoveModule.mc.field_1690.field_1867.method_1434()};
        this.processingClick = true;
        mc.execute(() -> {
            for (class_304 key : MoveUtil.getMovementKeys()) {
                key.method_23481(false);
            }
            InventoryMoveModule.mc.field_1724.method_5728(false);
        });
        ThreadManager.run(() -> {
            try {
                Thread.sleep(80L);
                this.sendPacket(pkt);
                Thread.sleep(40L);
                mc.execute(() -> {
                    class_304[] keys = new class_304[]{InventoryMoveModule.mc.field_1690.field_1894, InventoryMoveModule.mc.field_1690.field_1881, InventoryMoveModule.mc.field_1690.field_1913, InventoryMoveModule.mc.field_1690.field_1849, InventoryMoveModule.mc.field_1690.field_1903, InventoryMoveModule.mc.field_1690.field_1867};
                    for (int i = 0; i < keys.length; ++i) {
                        keys[i].method_23481(keysSnapshot[i]);
                    }
                });
                Thread.sleep(300L);
                mc.execute(() -> {
                    this.syncKeysWithPhysicalState();
                    this.processingClick = false;
                });
            }
            catch (InterruptedException ex) {
                this.processingClick = false;
                throw new RuntimeException(ex);
            }
        });
    }

    private void syncKeysWithPhysicalState() {
        class_304[] movementKeys;
        if (InventoryMoveModule.mc.field_1724 == null || mc.method_22683() == null) {
            return;
        }
        long handle = mc.method_22683().method_4490();
        for (class_304 key : movementKeys = new class_304[]{InventoryMoveModule.mc.field_1690.field_1894, InventoryMoveModule.mc.field_1690.field_1881, InventoryMoveModule.mc.field_1690.field_1913, InventoryMoveModule.mc.field_1690.field_1849, InventoryMoveModule.mc.field_1690.field_1903, InventoryMoveModule.mc.field_1690.field_1867, InventoryMoveModule.mc.field_1690.field_1832}) {
            boolean physicallyPressed = class_3675.method_15987((long)handle, (int)key.method_1429().method_1444());
            if (!key.method_1434() || physicallyPressed) continue;
            key.method_23481(false);
        }
    }

    @Generated
    public static InventoryMoveModule getInstance() {
        return instance;
    }
}


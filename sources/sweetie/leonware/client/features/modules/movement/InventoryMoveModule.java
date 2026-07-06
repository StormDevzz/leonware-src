package sweetie.leonware.client.features.modules.movement;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
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
import net.minecraft.class_437;
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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/InventoryMoveModule.class */
@ModuleRegister(name = "Inventory Move", category = Category.MOVEMENT)
public class InventoryMoveModule extends Module {
    private static final InventoryMoveModule instance = new InventoryMoveModule();
    public final ModeSetting swapMode = new ModeSetting("Swap mode").value("Grim").values("Basic", "Grim", "Legit", "AresMine");
    public final BooleanSetting multiActionsC = new BooleanSetting("MultiActionsC").value((Boolean) false);
    public final BooleanSetting allowSneak = new BooleanSetting("Возможность сидеть").value((Boolean) false);
    public final BooleanSetting rotateInArrow = new BooleanSetting("Поворачиваться по стрелочкам").value((Boolean) false);
    public final SliderSetting rotationSpeed = new SliderSetting("Скорость поворота").value(Float.valueOf(2.0f)).range(0.1f, 10.0f).step(0.1f);
    private final LinkedList<class_2596<?>> packet = new LinkedList<>();
    private volatile boolean processingClick = false;

    @Generated
    public static InventoryMoveModule getInstance() {
        return instance;
    }

    public InventoryMoveModule() {
        this.multiActionsC.setVisible(this::isGrim);
        addSettings(this.swapMode, this.multiActionsC, this.allowSneak, this.rotateInArrow, this.rotationSpeed);
        SliderSetting sliderSetting = this.rotationSpeed;
        BooleanSetting booleanSetting = this.rotateInArrow;
        Objects.requireNonNull(booleanSetting);
        sliderSetting.setVisible(booleanSetting::getValue);
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
        return (mc.field_1755 == null || (mc.field_1755 instanceof class_408) || (mc.field_1755 instanceof class_498) || (mc.field_1755 instanceof class_471) || (mc.field_1755 instanceof class_463) || (mc.field_1755 instanceof class_497)) ? false : true;
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener closeScreenEvent = CloseScreenEvent.getInstance().subscribe(new Listener(event -> {
            closeScreenEvent();
        }));
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event2 -> {
            updateEvent();
        }));
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener(event3 -> {
            packetEvent(event3);
        }));
        addEvents(closeScreenEvent, updateEvent, packetEvent);
    }

    private void closeScreenEvent() {
        if ((isLegit() || isAresMine()) && (mc.field_1755 instanceof class_490)) {
            if (isAresMine()) {
                boolean isMoving = MoveUtil.isMoving() || mc.field_1690.field_1903.method_1434() || mc.field_1724.method_5624();
                if (isMoving) {
                    CloseScreenEvent.getInstance().setCancel(true);
                    boolean[] keysSnapshot = {mc.field_1690.field_1894.method_1434(), mc.field_1690.field_1881.method_1434(), mc.field_1690.field_1913.method_1434(), mc.field_1690.field_1849.method_1434(), mc.field_1690.field_1903.method_1434(), mc.field_1690.field_1867.method_1434()};
                    this.processingClick = true;
                    ThreadManager.run(() -> {
                        for (class_304 key : MoveUtil.getMovementKeys()) {
                            key.method_23481(false);
                        }
                        mc.field_1724.method_5728(false);
                        mc.field_1690.field_1894.method_23481(false);
                        try {
                            Thread.sleep(50L);
                            mc.execute(() -> {
                                mc.method_1507((class_437) null);
                                mc.field_1724.field_3944.method_52787(new class_2815(0));
                            });
                            Thread.sleep(40L);
                            mc.execute(() -> {
                                class_304[] keys = {mc.field_1690.field_1894, mc.field_1690.field_1881, mc.field_1690.field_1913, mc.field_1690.field_1849, mc.field_1690.field_1903, mc.field_1690.field_1867};
                                for (int i = 0; i < keys.length; i++) {
                                    keys[i].method_23481(keysSnapshot[i]);
                                }
                            });
                            Thread.sleep(300L);
                            mc.execute(() -> {
                                syncKeysWithPhysicalState();
                                this.processingClick = false;
                            });
                        } catch (InterruptedException ex) {
                            this.processingClick = false;
                            throw new RuntimeException(ex);
                        }
                    });
                    return;
                }
                return;
            }
            if (!this.packet.isEmpty()) {
                if (!MoveUtil.isMoving() && !mc.field_1690.field_1903.method_1434()) {
                    while (!this.packet.isEmpty()) {
                        sendPacket(this.packet.removeFirst());
                    }
                    return;
                }
                CloseScreenEvent.getInstance().setCancel(true);
                boolean[] keysSnapshot2 = {mc.field_1690.field_1894.method_1434(), mc.field_1690.field_1881.method_1434(), mc.field_1690.field_1913.method_1434(), mc.field_1690.field_1849.method_1434(), mc.field_1690.field_1903.method_1434(), mc.field_1690.field_1867.method_1434()};
                LinkedList<class_2596<?>> packetsCopy = new LinkedList<>(this.packet);
                this.packet.clear();
                ThreadManager.run(() -> {
                    for (class_304 key : MoveUtil.getMovementKeys()) {
                        key.method_23481(false);
                    }
                    try {
                        Thread.sleep(60L);
                        Iterator it = packetsCopy.iterator();
                        while (it.hasNext()) {
                            class_2596<?> pkt = (class_2596) it.next();
                            sendPacket(pkt);
                            try {
                                Thread.sleep(8L);
                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                        try {
                            Thread.sleep(20L);
                            sendPacket((class_2596<?>) new class_2815(0));
                            mc.execute(() -> {
                                mc.method_1507((class_437) null);
                                class_304[] keys = {mc.field_1690.field_1894, mc.field_1690.field_1881, mc.field_1690.field_1913, mc.field_1690.field_1849, mc.field_1690.field_1903, mc.field_1690.field_1867};
                                for (int i = 0; i < keys.length; i++) {
                                    keys[i].method_23481(keysSnapshot2[i]);
                                }
                            });
                        } catch (InterruptedException ex2) {
                            throw new RuntimeException(ex2);
                        }
                    } catch (InterruptedException ex3) {
                        throw new RuntimeException(ex3);
                    }
                });
            }
        }
    }

    private void updateEvent() {
        if (isMovementAllowedInScreen()) {
            if (isAresMine() && this.processingClick) {
                return;
            }
            if (!SlownessManager.slowed) {
                MoveUtil.updateMovementKeys();
            }
            if (this.allowSneak.getValue().booleanValue() && mc.field_1755 != null) {
                mc.field_1690.field_1832.method_23481(class_3675.method_15987(mc.method_22683().method_4490(), class_3675.method_15981(mc.field_1690.field_1832.method_1428()).method_1444()));
            }
            if (this.rotateInArrow.getValue().booleanValue() && mc.field_1755 != null) {
                float speed = this.rotationSpeed.getValue().floatValue();
                float yaw = mc.field_1724.method_36454();
                float pitch = mc.field_1724.method_36455();
                long handle = mc.method_22683().method_4490();
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
                mc.field_1724.method_36456(yaw);
                mc.field_1724.method_36457(class_3532.method_15363(pitch, -90.0f, 90.0f));
            }
        }
    }

    public boolean isProcessing() {
        return this.processingClick;
    }

    private void packetEvent(PacketEvent.PacketEventData event) {
        if (event.isReceive()) {
            return;
        }
        if (isAresMine()) {
            handleAresMinePacket(event);
            return;
        }
        if (isLegit() && event.isSend()) {
            if (MoveUtil.isMoving() || mc.field_1690.field_1903.method_1434()) {
                class_2596<?> pacl = event.packet();
                if (mc.field_1755 instanceof class_490) {
                    if ((pacl instanceof class_2813) || (pacl instanceof class_2811) || (pacl instanceof class_2873) || (pacl instanceof class_8875)) {
                        this.packet.add(pacl);
                        PacketEvent.getInstance().setCancel(true);
                    }
                }
            }
        }
    }

    private void handleAresMinePacket(PacketEvent.PacketEventData event) {
        class_2596<?> pkt = event.packet();
        if (isMovementAllowedInScreen()) {
            boolean isClickPacket = (pkt instanceof class_2813) || (pkt instanceof class_2811) || (pkt instanceof class_2873) || (pkt instanceof class_8875);
            if (isClickPacket) {
                boolean isMoving = MoveUtil.isMoving() || mc.field_1690.field_1903.method_1434() || mc.field_1724.method_5624();
                if (isMoving) {
                    PacketEvent.getInstance().setCancel(true);
                    boolean[] keysSnapshot = {mc.field_1690.field_1894.method_1434(), mc.field_1690.field_1881.method_1434(), mc.field_1690.field_1913.method_1434(), mc.field_1690.field_1849.method_1434(), mc.field_1690.field_1903.method_1434(), mc.field_1690.field_1867.method_1434()};
                    this.processingClick = true;
                    mc.execute(() -> {
                        for (class_304 key : MoveUtil.getMovementKeys()) {
                            key.method_23481(false);
                        }
                        mc.field_1724.method_5728(false);
                    });
                    ThreadManager.run(() -> {
                        try {
                            Thread.sleep(80L);
                            sendPacket((class_2596<?>) pkt);
                            Thread.sleep(40L);
                            mc.execute(() -> {
                                class_304[] keys = {mc.field_1690.field_1894, mc.field_1690.field_1881, mc.field_1690.field_1913, mc.field_1690.field_1849, mc.field_1690.field_1903, mc.field_1690.field_1867};
                                for (int i = 0; i < keys.length; i++) {
                                    keys[i].method_23481(keysSnapshot[i]);
                                }
                            });
                            Thread.sleep(300L);
                            mc.execute(() -> {
                                syncKeysWithPhysicalState();
                                this.processingClick = false;
                            });
                        } catch (InterruptedException ex) {
                            this.processingClick = false;
                            throw new RuntimeException(ex);
                        }
                    });
                }
            }
        }
    }

    private void syncKeysWithPhysicalState() {
        if (mc.field_1724 == null || mc.method_22683() == null) {
            return;
        }
        long handle = mc.method_22683().method_4490();
        class_304[] movementKeys = {mc.field_1690.field_1894, mc.field_1690.field_1881, mc.field_1690.field_1913, mc.field_1690.field_1849, mc.field_1690.field_1903, mc.field_1690.field_1867, mc.field_1690.field_1832};
        for (class_304 key : movementKeys) {
            boolean physicallyPressed = class_3675.method_15987(handle, key.method_1429().method_1444());
            if (key.method_1434() && !physicallyPressed) {
                key.method_23481(false);
            }
        }
    }
}

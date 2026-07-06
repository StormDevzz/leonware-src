package sweetie.leonware.api.utils.combat;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.Generated;
import net.minecraft.class_1297;
import net.minecraft.class_1304;
import net.minecraft.class_1308;
import net.minecraft.class_1309;
import net.minecraft.class_1429;
import net.minecraft.class_1531;
import net.minecraft.class_1657;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_243;
import net.minecraft.class_9282;
import net.minecraft.class_9334;
import sweetie.leonware.api.system.configs.FriendManager;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import sweetie.leonware.client.features.modules.combat.AntiBotModule;
import sweetie.leonware.client.ui.widget.Widget;
import sweetie.leonware.client.ui.widget.WidgetManager;
import sweetie.leonware.client.ui.widget.overlay.StaffsWidget;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/combat/TargetManager.class */
public class TargetManager implements QuickImports {
    private class_1309 currentTarget;
    private Stream<class_1309> potentialTargets;

    @Generated
    public class_1309 getCurrentTarget() {
        return this.currentTarget;
    }

    public void lockTarget(class_1309 target) {
        if (this.currentTarget == null) {
            this.currentTarget = target;
        }
    }

    public void releaseTarget() {
        this.currentTarget = null;
    }

    public void validateTarget(Predicate<class_1309> predicate) {
        findFirstMatch(predicate).ifPresent(this::lockTarget);
        if (this.currentTarget != null && !predicate.test(this.currentTarget)) {
            releaseTarget();
        }
    }

    public void searchTargets(Iterable<class_1297> entities, float maxDistance) {
        if (isTargetOutOfRange(maxDistance)) {
            releaseTarget();
        }
        this.potentialTargets = createStreamFromEntities(entities, maxDistance);
    }

    private boolean isTargetOutOfRange(float maxDistance) {
        return this.currentTarget != null && RotationUtil.getSpot(this.currentTarget).method_1022(mc.field_1724.method_33571()) > ((double) maxDistance);
    }

    private Stream<class_1309> createStreamFromEntities(Iterable<class_1297> entities, float maxDistance) {
        Stream stream = StreamSupport.stream(entities.spliterator(), false);
        Class<class_1309> cls = class_1309.class;
        Objects.requireNonNull(class_1309.class);
        Stream streamFilter = stream.filter((v1) -> {
            return r1.isInstance(v1);
        });
        Class<class_1309> cls2 = class_1309.class;
        Objects.requireNonNull(class_1309.class);
        return streamFilter.map((v1) -> {
            return r1.cast(v1);
        }).filter(it -> {
            class_243 spot = RotationUtil.getSpot(it);
            return mc.field_1724.method_33571().method_1022(spot) <= ((double) maxDistance);
        }).sorted(Comparator.comparingDouble(it2 -> {
            class_243 spot = RotationUtil.getSpot(it2);
            return mc.field_1724.method_33571().method_1022(spot);
        }));
    }

    private Optional<class_1309> findFirstMatch(Predicate<class_1309> predicate) {
        return this.potentialTargets != null ? this.potentialTargets.filter(predicate).findFirst() : Optional.empty();
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/combat/TargetManager$EntityFilter.class */
    public static class EntityFilter implements QuickImports {
        public List<String> targetSettings;
        public boolean needFriends = false;

        public EntityFilter(List<String> targetSettings) {
            this.targetSettings = targetSettings;
        }

        public boolean isValid(class_1309 entity) {
            if ((AntiBotModule.getInstance().isEnabled() && AntiBotModule.getInstance().getMode().is("Zamorozka") && !AntiBotModule.validatedNonBots.contains(Integer.valueOf(entity.method_5628()))) || isLocalPlayer(entity) || isInvalidHealth(entity) || isBotPlayer(entity) || isIgnoredStaff(entity) || !isValidEntityType(entity)) {
                return false;
            }
            if (entity instanceof class_1657) {
                class_1657 player = (class_1657) entity;
                if (isTeammate(player)) {
                    return false;
                }
                if (this.targetSettings.contains("Ignore Naked") && isNaked(player)) {
                    return false;
                }
                return (this.targetSettings.contains("Ignore Simple Armor") && isSimpleArmor(player)) ? false : true;
            }
            return true;
        }

        private boolean isTeammate(class_1657 player) {
            if (!this.targetSettings.contains("Ignore Teams") || mc.field_1724 == null) {
                return false;
            }
            class_1799 myHelmet = mc.field_1724.method_6118(class_1304.field_6169);
            class_1799 myChestplate = mc.field_1724.method_6118(class_1304.field_6174);
            if (myHelmet.method_31574(class_1802.field_8267) && myChestplate.method_31574(class_1802.field_8577)) {
                class_1799 targetHelmet = player.method_6118(class_1304.field_6169);
                class_1799 targetChestplate = player.method_6118(class_1304.field_6174);
                if (targetHelmet.method_31574(class_1802.field_8267) && targetChestplate.method_31574(class_1802.field_8577)) {
                    int myHelmetColor = getArmorColor(myHelmet);
                    int myChestColor = getArmorColor(myChestplate);
                    int targetHelmetColor = getArmorColor(targetHelmet);
                    int targetChestColor = getArmorColor(targetChestplate);
                    return myHelmetColor == targetHelmetColor && myChestColor == targetChestColor;
                }
                return false;
            }
            return false;
        }

        private int getArmorColor(class_1799 stack) {
            class_9282 colorComponent = (class_9282) stack.method_57824(class_9334.field_49644);
            if (colorComponent != null) {
                return colorComponent.comp_2384();
            }
            return 10511680;
        }

        private boolean isNaked(class_1657 player) {
            return player.method_6118(class_1304.field_6169).method_7960() && player.method_6118(class_1304.field_6174).method_7960() && player.method_6118(class_1304.field_6172).method_7960() && player.method_6118(class_1304.field_6166).method_7960();
        }

        private boolean isSimpleArmor(class_1657 player) {
            int diamondCount = 0;
            int netheriteCount = 0;
            for (class_1304 slot : new class_1304[]{class_1304.field_6169, class_1304.field_6174, class_1304.field_6172, class_1304.field_6166}) {
                class_1799 stack = player.method_6118(slot);
                if (!stack.method_7960()) {
                    class_1792 item = stack.method_7909();
                    if (item == class_1802.field_8805 || item == class_1802.field_8058 || item == class_1802.field_8348 || item == class_1802.field_8285) {
                        diamondCount++;
                    } else if (item == class_1802.field_22027 || item == class_1802.field_22028 || item == class_1802.field_22029 || item == class_1802.field_22030) {
                        netheriteCount++;
                    }
                }
            }
            return diamondCount < 3 && netheriteCount < 2;
        }

        private boolean isIgnoredStaff(class_1309 entity) {
            if (!this.targetSettings.contains("Ignore Staff")) {
                return false;
            }
            StaffsWidget staffsWidget = null;
            Iterator<Widget> it = WidgetManager.getInstance().getWidgets().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Widget w = it.next();
                if (w instanceof StaffsWidget) {
                    StaffsWidget sw = (StaffsWidget) w;
                    staffsWidget = sw;
                    break;
                }
            }
            if (staffsWidget == null) {
                return false;
            }
            String name = entity.method_5477().getString();
            return staffsWidget.getStaffList().stream().anyMatch(s -> {
                return s.name().split(":")[0].equalsIgnoreCase(name);
            });
        }

        private boolean isLocalPlayer(class_1309 entity) {
            return entity == mc.field_1724;
        }

        private boolean isInvalidHealth(class_1309 entity) {
            return !entity.method_5805() || entity.method_6032() <= 0.0f;
        }

        private boolean isBotPlayer(class_1309 entity) {
            if (entity instanceof class_1657) {
                class_1657 player = (class_1657) entity;
                return AntiBotModule.checkBot(player);
            }
            return false;
        }

        private boolean isValidEntityType(class_1309 entity) {
            if (entity instanceof class_1657) {
                class_1657 player = (class_1657) entity;
                if (FriendManager.getInstance().contains(player.method_5477().getString()) && !this.needFriends) {
                    return false;
                }
                return this.targetSettings.contains("Players");
            }
            if (entity instanceof class_1429) {
                return this.targetSettings.contains("Animals");
            }
            if (entity instanceof class_1308) {
                return this.targetSettings.contains("Mobs");
            }
            return !(entity instanceof class_1531);
        }
    }
}

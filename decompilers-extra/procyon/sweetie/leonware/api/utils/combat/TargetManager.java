// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.combat;

import net.minecraft.class_1531;
import net.minecraft.class_1308;
import net.minecraft.class_1429;
import sweetie.leonware.api.system.configs.FriendManager;
import java.util.Iterator;
import sweetie.leonware.client.ui.widget.overlay.StaffsWidget;
import sweetie.leonware.client.ui.widget.Widget;
import sweetie.leonware.client.ui.widget.WidgetManager;
import net.minecraft.class_1792;
import net.minecraft.class_9334;
import net.minecraft.class_9282;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1304;
import net.minecraft.class_1657;
import sweetie.leonware.client.features.modules.combat.AntiBotModule;
import java.util.List;
import lombok.Generated;
import java.util.Optional;
import net.minecraft.class_243;
import java.util.Comparator;
import java.util.function.Function;
import java.util.Objects;
import java.util.stream.StreamSupport;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import net.minecraft.class_1297;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.class_1309;
import sweetie.leonware.api.system.interfaces.QuickImports;

public class TargetManager implements QuickImports
{
    private class_1309 currentTarget;
    private Stream<class_1309> potentialTargets;
    
    public void lockTarget(final class_1309 target) {
        if (this.currentTarget == null) {
            this.currentTarget = target;
        }
    }
    
    public void releaseTarget() {
        this.currentTarget = null;
    }
    
    public void validateTarget(final Predicate<class_1309> predicate) {
        this.findFirstMatch(predicate).ifPresent(this::lockTarget);
        if (this.currentTarget != null && !predicate.test(this.currentTarget)) {
            this.releaseTarget();
        }
    }
    
    public void searchTargets(final Iterable<class_1297> entities, final float maxDistance) {
        if (this.isTargetOutOfRange(maxDistance)) {
            this.releaseTarget();
        }
        this.potentialTargets = this.createStreamFromEntities(entities, maxDistance);
    }
    
    private boolean isTargetOutOfRange(final float maxDistance) {
        return this.currentTarget != null && RotationUtil.getSpot((class_1297)this.currentTarget).method_1022(TargetManager.mc.field_1724.method_33571()) > maxDistance;
    }
    
    private Stream<class_1309> createStreamFromEntities(final Iterable<class_1297> entities, final float maxDistance) {
        final Stream<class_1297> stream = StreamSupport.stream(entities.spliterator(), false);
        final Class<class_1309> obj = class_1309.class;
        Objects.requireNonNull(obj);
        final Stream<class_1297> filter = stream.filter(obj::isInstance);
        final Class<class_1309> obj2 = class_1309.class;
        Objects.requireNonNull(obj2);
        return filter.map((Function<? super class_1297, ?>)obj2::cast).filter(it -> {
            final class_243 spot = RotationUtil.getSpot((class_1297)it);
            return TargetManager.mc.field_1724.method_33571().method_1022(spot) <= maxDistance;
        }).sorted(Comparator.comparingDouble(it -> {
            final class_243 spot2 = RotationUtil.getSpot((class_1297)it);
            return TargetManager.mc.field_1724.method_33571().method_1022(spot2);
        }));
    }
    
    private Optional<class_1309> findFirstMatch(final Predicate<class_1309> predicate) {
        return (this.potentialTargets != null) ? this.potentialTargets.filter(predicate).findFirst() : Optional.empty();
    }
    
    @Generated
    public class_1309 getCurrentTarget() {
        return this.currentTarget;
    }
    
    public static class EntityFilter implements QuickImports
    {
        public List<String> targetSettings;
        public boolean needFriends;
        
        public EntityFilter(final List<String> targetSettings) {
            this.needFriends = false;
            this.targetSettings = targetSettings;
        }
        
        public boolean isValid(final class_1309 entity) {
            if (AntiBotModule.getInstance().isEnabled() && AntiBotModule.getInstance().getMode().is("Zamorozka") && !AntiBotModule.validatedNonBots.contains(entity.method_5628())) {
                return false;
            }
            if (this.isLocalPlayer(entity)) {
                return false;
            }
            if (this.isInvalidHealth(entity)) {
                return false;
            }
            if (this.isBotPlayer(entity)) {
                return false;
            }
            if (this.isIgnoredStaff(entity)) {
                return false;
            }
            if (!this.isValidEntityType(entity)) {
                return false;
            }
            if (entity instanceof final class_1657 player) {
                if (this.isTeammate(player)) {
                    return false;
                }
                if (this.targetSettings.contains("Ignore Naked") && this.isNaked(player)) {
                    return false;
                }
                if (this.targetSettings.contains("Ignore Simple Armor") && this.isSimpleArmor(player)) {
                    return false;
                }
            }
            return true;
        }
        
        private boolean isTeammate(final class_1657 player) {
            if (!this.targetSettings.contains("Ignore Teams")) {
                return false;
            }
            if (EntityFilter.mc.field_1724 == null) {
                return false;
            }
            final class_1799 myHelmet = EntityFilter.mc.field_1724.method_6118(class_1304.field_6169);
            final class_1799 myChestplate = EntityFilter.mc.field_1724.method_6118(class_1304.field_6174);
            if (myHelmet.method_31574(class_1802.field_8267) && myChestplate.method_31574(class_1802.field_8577)) {
                final class_1799 targetHelmet = player.method_6118(class_1304.field_6169);
                final class_1799 targetChestplate = player.method_6118(class_1304.field_6174);
                if (targetHelmet.method_31574(class_1802.field_8267) && targetChestplate.method_31574(class_1802.field_8577)) {
                    final int myHelmetColor = this.getArmorColor(myHelmet);
                    final int myChestColor = this.getArmorColor(myChestplate);
                    final int targetHelmetColor = this.getArmorColor(targetHelmet);
                    final int targetChestColor = this.getArmorColor(targetChestplate);
                    return myHelmetColor == targetHelmetColor && myChestColor == targetChestColor;
                }
            }
            return false;
        }
        
        private int getArmorColor(final class_1799 stack) {
            final class_9282 colorComponent = (class_9282)stack.method_57824(class_9334.field_49644);
            return (colorComponent != null) ? colorComponent.comp_2384() : 10511680;
        }
        
        private boolean isNaked(final class_1657 player) {
            return player.method_6118(class_1304.field_6169).method_7960() && player.method_6118(class_1304.field_6174).method_7960() && player.method_6118(class_1304.field_6172).method_7960() && player.method_6118(class_1304.field_6166).method_7960();
        }
        
        private boolean isSimpleArmor(final class_1657 player) {
            int diamondCount = 0;
            int netheriteCount = 0;
            for (final class_1304 slot : new class_1304[] { class_1304.field_6169, class_1304.field_6174, class_1304.field_6172, class_1304.field_6166 }) {
                final class_1799 stack = player.method_6118(slot);
                if (!stack.method_7960()) {
                    final class_1792 item = stack.method_7909();
                    if (item == class_1802.field_8805 || item == class_1802.field_8058 || item == class_1802.field_8348 || item == class_1802.field_8285) {
                        ++diamondCount;
                    }
                    else if (item == class_1802.field_22027 || item == class_1802.field_22028 || item == class_1802.field_22029 || item == class_1802.field_22030) {
                        ++netheriteCount;
                    }
                }
            }
            return diamondCount < 3 && netheriteCount < 2;
        }
        
        private boolean isIgnoredStaff(final class_1309 entity) {
            if (!this.targetSettings.contains("Ignore Staff")) {
                return false;
            }
            StaffsWidget staffsWidget = null;
            for (final Widget w : WidgetManager.getInstance().getWidgets()) {
                if (w instanceof final StaffsWidget staffsWidget2) {
                    final StaffsWidget sw = staffsWidget = staffsWidget2;
                    break;
                }
            }
            if (staffsWidget == null) {
                return false;
            }
            final String name = entity.method_5477().getString();
            return staffsWidget.getStaffList().stream().anyMatch(s -> s.name().split(":")[0].equalsIgnoreCase(name));
        }
        
        private boolean isLocalPlayer(final class_1309 entity) {
            return entity == EntityFilter.mc.field_1724;
        }
        
        private boolean isInvalidHealth(final class_1309 entity) {
            return !entity.method_5805() || entity.method_6032() <= 0.0f;
        }
        
        private boolean isBotPlayer(final class_1309 entity) {
            if (entity instanceof final class_1657 player) {
                return AntiBotModule.checkBot(player);
            }
            return false;
        }
        
        private boolean isValidEntityType(final class_1309 entity) {
            if (entity instanceof final class_1657 player) {
                return (!FriendManager.getInstance().contains(player.method_5477().getString()) || this.needFriends) && this.targetSettings.contains("Players");
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

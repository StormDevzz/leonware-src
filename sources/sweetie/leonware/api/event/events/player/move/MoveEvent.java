package sweetie.leonware.api.event.events.player.move;

import lombok.Generated;
import net.minecraft.class_243;
import sweetie.leonware.api.event.events.Event;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/event/events/player/move/MoveEvent.class */
public class MoveEvent extends Event<MoveEventData> {
    private static final MoveEvent instance = new MoveEvent();

    @Generated
    public static MoveEvent getInstance() {
        return instance;
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/event/events/player/move/MoveEvent$MoveEventData.class */
    public static class MoveEventData {
        private double x;
        private double y;
        private double z;

        @Generated
        public void setX(double x) {
            this.x = x;
        }

        @Generated
        public void setY(double y) {
            this.y = y;
        }

        @Generated
        public void setZ(double z) {
            this.z = z;
        }

        @Generated
        public MoveEventData(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Generated
        public double getX() {
            return this.x;
        }

        @Generated
        public double getY() {
            return this.y;
        }

        @Generated
        public double getZ() {
            return this.z;
        }

        public void set(class_243 vec3d) {
            this.x = vec3d.method_10216();
            this.y = vec3d.method_10214();
            this.z = vec3d.method_10215();
        }
    }
}

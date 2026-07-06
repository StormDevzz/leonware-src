// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.event.events.player.move;

import net.minecraft.class_243;
import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

public class MoveEvent extends Event<MoveEventData>
{
    private static final MoveEvent instance;
    
    @Generated
    public static MoveEvent getInstance() {
        return MoveEvent.instance;
    }
    
    static {
        instance = new MoveEvent();
    }
    
    public static class MoveEventData
    {
        private double x;
        private double y;
        private double z;
        
        public void set(final class_243 vec3d) {
            this.x = vec3d.method_10216();
            this.y = vec3d.method_10214();
            this.z = vec3d.method_10215();
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
        
        @Generated
        public void setX(final double x) {
            this.x = x;
        }
        
        @Generated
        public void setY(final double y) {
            this.y = y;
        }
        
        @Generated
        public void setZ(final double z) {
            this.z = z;
        }
        
        @Generated
        public MoveEventData(final double x, final double y, final double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}

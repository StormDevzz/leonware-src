// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.system.draggable;

import lombok.Generated;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.other.WindowResizeEvent;
import sweetie.leonware.api.module.Module;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;
import sweetie.leonware.api.system.interfaces.QuickImports;

public class Draggable implements QuickImports
{
    @Expose
    @SerializedName("x")
    private float x;
    @Expose
    @SerializedName("y")
    private float y;
    public float initialXVal;
    public float initialYVal;
    private float startX;
    private float startY;
    private boolean dragging;
    private float width;
    private float height;
    @Expose
    @SerializedName("name")
    private final String name;
    private final Module module;
    
    public Draggable(final Module module, final String name, final float initialXVal, final float initialYVal) {
        this.width = 0.0f;
        this.height = 0.0f;
        this.module = module;
        this.name = name;
        this.x = this.roundToHalf(initialXVal);
        this.y = this.roundToHalf(initialYVal);
        this.initialXVal = initialXVal;
        this.initialYVal = initialYVal;
        WindowResizeEvent.getInstance().subscribe(new Listener<WindowResizeEvent>(-1, event -> {
            if (this.dragging) {
                this.clampToScreen();
            }
        }));
    }
    
    public final void onDraw() {
        if (this.dragging) {
            this.x = this.roundToHalf(MathUtil.interpolate(this.x, this.normaliseX() - this.startX, 0.15f));
            this.y = this.roundToHalf(MathUtil.interpolate(this.y, this.normaliseY() - this.startY, 0.15f));
            this.clampToScreen();
        }
    }
    
    public final void onClick(final int button) {
        if (button == 0 && this.isHovering()) {
            final boolean anotherDragging = DraggableManager.getInstance().getDraggables().values().stream().anyMatch(Draggable::isDragging);
            if (!anotherDragging) {
                this.dragging = true;
                this.startX = (float)(int)(this.normaliseX() - this.x);
                this.startY = (float)(int)(this.normaliseY() - this.y);
            }
        }
    }
    
    public final void onRelease(final int button) {
        if (button == 0) {
            this.dragging = false;
        }
    }
    
    public boolean isHovering() {
        return this.normaliseX() > Math.min(this.x, this.x + this.width) && this.normaliseX() < Math.max(this.x, this.x + this.width) && this.normaliseY() > Math.min(this.y, this.y + this.height) && this.normaliseY() < Math.max(this.y, this.y + this.height);
    }
    
    public int normaliseX() {
        return (int)(Draggable.mc.field_1729.method_1603() / Draggable.mc.method_22683().method_4495());
    }
    
    public int normaliseY() {
        return (int)(Draggable.mc.field_1729.method_1604() / Draggable.mc.method_22683().method_4495());
    }
    
    private float roundToHalf(final float value) {
        return Math.round(value * 2.0f) / 2.0f;
    }
    
    private void clampToScreen() {
        final float margin = 3.0f;
        final float screenWidth = (float)Draggable.mc.method_22683().method_4486();
        final float screenHeight = (float)Draggable.mc.method_22683().method_4502();
        if (this.x < margin) {
            this.x = margin;
        }
        if (this.y < margin) {
            this.y = margin;
        }
        if (this.x + this.width > screenWidth - margin) {
            this.x = screenWidth - this.width - margin;
        }
        if (this.y + this.height > screenHeight - margin) {
            this.y = screenHeight - this.height - margin;
        }
    }
    
    @Generated
    public float getX() {
        return this.x;
    }
    
    @Generated
    public float getY() {
        return this.y;
    }
    
    @Generated
    public float getInitialXVal() {
        return this.initialXVal;
    }
    
    @Generated
    public float getInitialYVal() {
        return this.initialYVal;
    }
    
    @Generated
    public float getStartX() {
        return this.startX;
    }
    
    @Generated
    public float getStartY() {
        return this.startY;
    }
    
    @Generated
    public boolean isDragging() {
        return this.dragging;
    }
    
    @Generated
    public float getWidth() {
        return this.width;
    }
    
    @Generated
    public float getHeight() {
        return this.height;
    }
    
    @Generated
    public String getName() {
        return this.name;
    }
    
    @Generated
    public Module getModule() {
        return this.module;
    }
    
    @Generated
    public void setX(final float x) {
        this.x = x;
    }
    
    @Generated
    public void setY(final float y) {
        this.y = y;
    }
    
    @Generated
    public void setInitialXVal(final float initialXVal) {
        this.initialXVal = initialXVal;
    }
    
    @Generated
    public void setInitialYVal(final float initialYVal) {
        this.initialYVal = initialYVal;
    }
    
    @Generated
    public void setStartX(final float startX) {
        this.startX = startX;
    }
    
    @Generated
    public void setStartY(final float startY) {
        this.startY = startY;
    }
    
    @Generated
    public void setDragging(final boolean dragging) {
        this.dragging = dragging;
    }
    
    @Generated
    public void setWidth(final float width) {
        this.width = width;
    }
    
    @Generated
    public void setHeight(final float height) {
        this.height = height;
    }
}

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.system.draggable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Generated;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.other.WindowResizeEvent;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.system.draggable.DraggableManager;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.math.MathUtil;

public class Draggable
implements QuickImports {
    @Expose
    @SerializedName(value="x")
    private float x;
    @Expose
    @SerializedName(value="y")
    private float y;
    public float initialXVal;
    public float initialYVal;
    private float startX;
    private float startY;
    private boolean dragging;
    private float width = 0.0f;
    private float height = 0.0f;
    @Expose
    @SerializedName(value="name")
    private final String name;
    private final Module module;

    public Draggable(Module module, String name, float initialXVal, float initialYVal) {
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
            this.x = this.roundToHalf(MathUtil.interpolate(this.x, (float)this.normaliseX() - this.startX, 0.15f));
            this.y = this.roundToHalf(MathUtil.interpolate(this.y, (float)this.normaliseY() - this.startY, 0.15f));
            this.clampToScreen();
        }
    }

    public final void onClick(int button) {
        boolean anotherDragging;
        if (button == 0 && this.isHovering() && !(anotherDragging = DraggableManager.getInstance().getDraggables().values().stream().anyMatch(Draggable::isDragging))) {
            this.dragging = true;
            this.startX = (int)((float)this.normaliseX() - this.x);
            this.startY = (int)((float)this.normaliseY() - this.y);
        }
    }

    public final void onRelease(int button) {
        if (button == 0) {
            this.dragging = false;
        }
    }

    public boolean isHovering() {
        return (float)this.normaliseX() > Math.min(this.x, this.x + this.width) && (float)this.normaliseX() < Math.max(this.x, this.x + this.width) && (float)this.normaliseY() > Math.min(this.y, this.y + this.height) && (float)this.normaliseY() < Math.max(this.y, this.y + this.height);
    }

    public int normaliseX() {
        return (int)(Draggable.mc.field_1729.method_1603() / mc.method_22683().method_4495());
    }

    public int normaliseY() {
        return (int)(Draggable.mc.field_1729.method_1604() / mc.method_22683().method_4495());
    }

    private float roundToHalf(float value) {
        return (float)Math.round(value * 2.0f) / 2.0f;
    }

    private void clampToScreen() {
        float margin = 3.0f;
        float screenWidth = mc.method_22683().method_4486();
        float screenHeight = mc.method_22683().method_4502();
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
    public void setX(float x) {
        this.x = x;
    }

    @Generated
    public void setY(float y) {
        this.y = y;
    }

    @Generated
    public void setInitialXVal(float initialXVal) {
        this.initialXVal = initialXVal;
    }

    @Generated
    public void setInitialYVal(float initialYVal) {
        this.initialYVal = initialYVal;
    }

    @Generated
    public void setStartX(float startX) {
        this.startX = startX;
    }

    @Generated
    public void setStartY(float startY) {
        this.startY = startY;
    }

    @Generated
    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    @Generated
    public void setWidth(float width) {
        this.width = width;
    }

    @Generated
    public void setHeight(float height) {
        this.height = height;
    }
}


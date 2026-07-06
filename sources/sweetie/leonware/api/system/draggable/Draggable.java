package sweetie.leonware.api.system.draggable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Generated;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.other.WindowResizeEvent;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.math.MathUtil;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/system/draggable/Draggable.class */
public class Draggable implements QuickImports {

    @SerializedName("x")
    @Expose
    private float x;

    @SerializedName("y")
    @Expose
    private float y;
    public float initialXVal;
    public float initialYVal;
    private float startX;
    private float startY;
    private boolean dragging;
    private float width = 0.0f;
    private float height = 0.0f;

    @SerializedName("name")
    @Expose
    private final String name;
    private final Module module;

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

    public Draggable(Module module, String name, float initialXVal, float initialYVal) {
        this.module = module;
        this.name = name;
        this.x = roundToHalf(initialXVal);
        this.y = roundToHalf(initialYVal);
        this.initialXVal = initialXVal;
        this.initialYVal = initialYVal;
        WindowResizeEvent.getInstance().subscribe(new Listener(-1, event -> {
            if (this.dragging) {
                clampToScreen();
            }
        }));
    }

    public final void onDraw() {
        if (this.dragging) {
            this.x = roundToHalf(MathUtil.interpolate(this.x, normaliseX() - this.startX, 0.15f));
            this.y = roundToHalf(MathUtil.interpolate(this.y, normaliseY() - this.startY, 0.15f));
            clampToScreen();
        }
    }

    public final void onClick(int button) {
        if (button == 0 && isHovering()) {
            boolean anotherDragging = DraggableManager.getInstance().getDraggables().values().stream().anyMatch((v0) -> {
                return v0.isDragging();
            });
            if (!anotherDragging) {
                this.dragging = true;
                this.startX = (int) (normaliseX() - this.x);
                this.startY = (int) (normaliseY() - this.y);
            }
        }
    }

    public final void onRelease(int button) {
        if (button == 0) {
            this.dragging = false;
        }
    }

    public boolean isHovering() {
        return ((float) normaliseX()) > Math.min(this.x, this.x + this.width) && ((float) normaliseX()) < Math.max(this.x, this.x + this.width) && ((float) normaliseY()) > Math.min(this.y, this.y + this.height) && ((float) normaliseY()) < Math.max(this.y, this.y + this.height);
    }

    public int normaliseX() {
        return (int) (mc.field_1729.method_1603() / mc.method_22683().method_4495());
    }

    public int normaliseY() {
        return (int) (mc.field_1729.method_1604() / mc.method_22683().method_4495());
    }

    private float roundToHalf(float value) {
        return Math.round(value * 2.0f) / 2.0f;
    }

    private void clampToScreen() {
        float screenWidth = mc.method_22683().method_4486();
        float screenHeight = mc.method_22683().method_4502();
        if (this.x < 3.0f) {
            this.x = 3.0f;
        }
        if (this.y < 3.0f) {
            this.y = 3.0f;
        }
        if (this.x + this.width > screenWidth - 3.0f) {
            this.x = (screenWidth - this.width) - 3.0f;
        }
        if (this.y + this.height > screenHeight - 3.0f) {
            this.y = (screenHeight - this.height) - 3.0f;
        }
    }
}

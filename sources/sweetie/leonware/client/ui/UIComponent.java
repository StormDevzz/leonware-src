package sweetie.leonware.client.ui;

import lombok.Generated;
import sweetie.leonware.api.system.interfaces.UIApi;
import sweetie.leonware.client.services.RenderService;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/UIComponent.class */
public abstract class UIComponent implements UIApi {
    private float x;
    private float y;
    private float width;
    private float height;
    private float alpha;

    @Generated
    public void setX(float x) {
        this.x = x;
    }

    @Generated
    public void setY(float y) {
        this.y = y;
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
    public void setAlpha(float alpha) {
        this.alpha = alpha;
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
    public float getWidth() {
        return this.width;
    }

    @Generated
    public float getHeight() {
        return this.height;
    }

    @Generated
    public float getAlpha() {
        return this.alpha;
    }

    public float gap() {
        return scaled(2.0f);
    }

    public float offset() {
        return gap() * 1.5f;
    }

    public float scaled(float value) {
        return RenderService.getInstance().scaled(value);
    }

    public float getScale() {
        return RenderService.getInstance().getScale();
    }
}

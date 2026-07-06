// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui;

import lombok.Generated;
import sweetie.leonware.client.services.RenderService;
import sweetie.leonware.api.system.interfaces.UIApi;

public abstract class UIComponent implements UIApi
{
    private float x;
    private float y;
    private float width;
    private float height;
    private float alpha;
    
    public float gap() {
        return this.scaled(2.0f);
    }
    
    public float offset() {
        return this.gap() * 1.5f;
    }
    
    public float scaled(final float value) {
        return RenderService.getInstance().scaled(value);
    }
    
    public float getScale() {
        return RenderService.getInstance().getScale();
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
    
    @Generated
    public void setX(final float x) {
        this.x = x;
    }
    
    @Generated
    public void setY(final float y) {
        this.y = y;
    }
    
    @Generated
    public void setWidth(final float width) {
        this.width = width;
    }
    
    @Generated
    public void setHeight(final float height) {
        this.height = height;
    }
    
    @Generated
    public void setAlpha(final float alpha) {
        this.alpha = alpha;
    }
}

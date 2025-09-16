package ghost.wraith.features.gui.items;

import java.awt.Color;
import ghost.wraith.features.Feature;
import net.minecraft.class_332;

public class Item extends Feature {
    public static class_332 context;
    protected float x;
    protected float y;
    protected int width;
    protected int height;
    private boolean hidden;

    public Item(String name) {
        super(name);
    }

    // ===== GUI Placement =====
    public void setLocation(float x, float y) {
        this.x = x;
        this.y = y;
    }

    // ===== GUI Lifecycle =====
    public void drawScreen(class_332 context, int mouseX, int mouseY, float partialTicks) {
        Item.context = context; // keep context in sync
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {}
    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {}
    public void update() {}
    public void onKeyTyped(char typedChar, int keyCode) {}
    public void onKeyPressed(int key) {}

    // ===== Getters / Setters =====
    public float getX() { return this.x; }
    public float getY() { return this.y; }
    public int getWidth() { return this.width; }
    public void setWidth(int width) { this.width = width; }
    public int getHeight() { return this.height; }
    public void setHeight(int height) { this.height = height; }

    public boolean isHidden() { return this.hidden; }
    public boolean setHidden(boolean hidden) {
        this.hidden = hidden;
        return this.hidden;
    }

    // ===== Text Drawing =====
    protected void drawString(String text, double x, double y, Color color) {
        this.drawString(text, x, y, color.getRGB()); // fixed from .hashCode()
    }

    protected void drawString(String text, double x, double y, int color) {
        if (context != null) {
            context.method_25303(mc.field_1772, text, (int) x, (int) y, color);
        }
    }
}

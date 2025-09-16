package ghost.wraith.util;

import java.awt.Color;
import ghost.wraith.features.modules.client.ClickGui;

public class ColorUtil {

    // ====== THEME COLORS ======
    public static final Color DISCORD_PRIMARY = new Color(88, 101, 242);   // blurple
    public static final Color DISCORD_DARK = new Color(54, 57, 63);        // dark background
    public static final Color DISCORD_LIGHT = new Color(185, 187, 190);    // light gray
    public static final Color WRAITH_ACCENT = new Color(120, 80, 200);     // purple accent
    public static final Color WRAITH_GLOW = new Color(0, 255, 200);        // neon teal

    // ====== BASIC CONVERSIONS ======
    public static int toARGB(int r, int g, int b, int a) {
        return (new Color(r, g, b, a)).getRGB();
    }

    public static int toRGBA(int r, int g, int b) {
        return toRGBA(r, g, b, 255);
    }

    public static int toRGBA(int r, int g, int b, int a) {
        return (r << 16) + (g << 8) + b + (a << 24);
    }

    public static int toRGBA(float r, float g, float b, float a) {
        return toRGBA((int)(r * 255.0F), (int)(g * 255.0F), (int)(b * 255.0F), (int)(a * 255.0F));
    }

    public static int toRGBA(Color color) {
        return toRGBA(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    // ====== MODERN COLOR EFFECTS ======

    /** Smooth muted rainbow (Discord style, not oversaturated) */
    public static Color modernRainbow(int delay) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0F);
        float hue = (float)((rainbowState % 360.0F) / 360.0F);
        float sat = (Float)ClickGui.getInstance().rainbowSaturation.getValue() / 255.0F;
        float bri = (Float)ClickGui.getInstance().rainbowBrightness.getValue() / 255.0F;

        // Slightly desaturate for modern look
        return Color.getHSBColor(hue, sat * 0.75f, bri * 0.9f);
    }

    /** Gradient blend between two colors */
    public static Color gradient(Color c1, Color c2, float progress) {
        progress = Math.min(1f, Math.max(0f, progress)); // clamp
        int r = (int)(c1.getRed()   + (c2.getRed()   - c1.getRed()) * progress);
        int g = (int)(c1.getGreen() + (c2.getGreen() - c1.getGreen()) * progress);
        int b = (int)(c1.getBlue()  + (c2.getBlue()  - c1.getBlue()) * progress);
        int a = (int)(c1.getAlpha() + (c2.getAlpha() - c1.getAlpha()) * progress);
        return new Color(r, g, b, a);
    }

    /** Wraith glow effect: cycling between accent and neon teal */
    public static Color wraithGlow(int delay) {
        float progress = (float)((System.currentTimeMillis() + delay) % 2000L) / 2000f;
        if (progress > 0.5f) progress = 1f - (progress - 0.5f) * 2f; // make it pulse
        return gradient(WRAITH_ACCENT, WRAITH_GLOW, progress);
    }
}

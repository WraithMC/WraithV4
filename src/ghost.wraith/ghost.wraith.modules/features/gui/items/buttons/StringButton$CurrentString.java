//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package ghost.wraith.features.gui.items.buttons;

import ghost.wraith.Wraith;
import ghost.wraith.features.gui.Gui;
import ghost.wraith.features.modules.client.ClickGui;
import ghost.wraith.features.settings.Setting;
import ghost.wraith.util.RenderUtil;
import ghost.wraith.util.models.Timer;
import net.minecraft.class_1109;
import net.minecraft.class_124;
import net.minecraft.class_332;
import net.minecraft.class_3417;

public class StringButton extends Button {
    private static final Timer idleTimer = new Timer();
    private static boolean idle;
    private final Setting<String> setting;
    public boolean isListening;
    private CurrentString currentString = new CurrentString("");

    public StringButton(Setting<String> setting) {
        super(setting.getName());
        this.setting = setting;
        this.width = 15;
    }

    // Remove last character from string
    public static String removeLastChar(String str) {
        if (str != null && !str.isEmpty()) {
            return str.substring(0, str.length() - 1);
        }
        return "";
    }

    @Override
    public void drawScreen(class_332 context, int mouseX, int mouseY, float partialTicks) {
        // Modern, Discord-like colors (like in your Tampermonkey GUI)
        int bgColor = this.getState()
                ? (!this.isHovering(mouseX, mouseY)
                ? Wraith.colorManager.getColorWithAlpha((Integer)((ClickGui)Wraith.moduleManager.getModuleByClass(ClickGui.class)).hoverAlpha.getValue())
                : Wraith.colorManager.getColorWithAlpha((Integer)((ClickGui)Wraith.moduleManager.getModuleByClass(ClickGui.class)).alpha.getValue()))
                : (!this.isHovering(mouseX, mouseY) ? 0x11515151 : 0x88202020);

        RenderUtil.rect(context.method_51448(), this.x, this.y,
                this.x + (float)this.width + 7.4F,
                this.y + (float)this.height - 0.5F,
                bgColor);

        if (this.isListening) {
            // Show typed text with underscore cursor
            this.drawString(this.currentString.string() + getIdleSign(),
                    this.x + 2.3F,
                    this.y - 1.7F - (float)Gui.getClickGui().getTextOffset(),
                    this.getState() ? -1 : 0xFFAAAAAA);
        } else {
            String prefix = "";
            if (this.setting.getName().equals("Buttons")) {
                prefix = "Buttons ";
            } else if (this.setting.getName().equals("Prefix")) {
                prefix = "Prefix  " + class_124.field_1080;
            }

            this.drawString(prefix + this.setting.getValue(),
                    this.x + 2.3F,
                    this.y - 1.7F - (float)Gui.getClickGui().getTextOffset(),
                    this.getState() ? -1 : 0xFFAAAAAA);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            mc.method_1483().method_4873(class_1109.method_47978(class_3417.field_15015, 1.0F));
        }
    }

    @Override
    public void onKeyTyped(char typedChar, int keyCode) {
        if (this.isListening) {
            this.setString(this.currentString.string() + typedChar);
        }
    }

    @Override
    public void onKeyPressed(int key) {
        if (this.isListening) {
            switch (key) {
                case 1: // ESC
                    return;
                case 28: // ENTER
                    this.enterString();
                    break;
                case 14: // BACKSPACE
                    this.setString(removeLastChar(this.currentString.string()));
                    break;
            }
        }
    }

    @Override
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }

    private void enterString() {
        if (this.currentString.string().isEmpty()) {
            this.setting.setValue(this.setting.getDefaultValue());
        } else {
            this.setting.setValue(this.currentString.string());
        }

        this.setString("");
        this.onMouseClick();
    }

    @Override
    public int getHeight() {
        return 14;
    }

    @Override
    public void toggle() {
        this.isListening = !this.isListening;
    }

    @Override
    public boolean getState() {
        return !this.isListening;
    }

    public void setString(String newString) {
        this.currentString = new CurrentString(newString);
    }

    // Idle blinking underscore (cursor style)
    public static String getIdleSign() {
        if (idleTimer.passedMs(500L)) {
            idle = !idle;
            idleTimer.reset();
        }
        return idle ? "_" : "";
    }

    public static record CurrentString(String string) {}
}

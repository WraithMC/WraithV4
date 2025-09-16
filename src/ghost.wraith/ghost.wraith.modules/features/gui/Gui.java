package ghost.wraith.features.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;

import ghost.wraith.Gardenia;
import ghost.wraith.features.Feature;
import ghost.wraith.features.gui.items.Item;
import ghost.wraith.features.gui.items.buttons.ModuleButton;
import ghost.wraith.features.modules.Module;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_437;


public class Gui extends class_437 {
    private static Gui INSTANCE = new Gui();
    private final ArrayList<Component> components = new ArrayList<>();

    private float animationAlpha = 0f; // for fade-in animation

    public Gui() {
        super(class_2561.method_43470("WraithV4"));
        setInstance();
        load();
    }

    public static Gui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Gui();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    private void load() {
        int x = -106;

        for (final Module.Category category : Gardenia.moduleManager.getCategories()) {
            x += 110;
            components.add(new Component(category.getName(), x, 20, true) {
                @Override
                public void setupItems() {
                    Gardenia.moduleManager.getModulesByCategory(category).forEach((module) -> {
                        if (!module.hidden) {
                            this.addButton(new ModuleButton(module));
                        }
                    });
                }
            });
        }

        components.forEach(c -> c.getItems().sort(Comparator.comparing(Feature::getName)));
    }

    @Override
    public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
        Item.context = context;

        if (animationAlpha < 1f) {
            animationAlpha += delta * 0.08f;
        }
        float alpha = Math.min(animationAlpha, 1f);

        int screenW = context.method_51421();
        int screenH = context.method_51443();
ound
        int overlayColor = new Color(15, 15, 15, (int) (180 * alpha)).getRGB();
        context.method_25294(0, 0, screenW, screenH, overlayColor);

        components.forEach((c) -> c.drawScreen(context, mouseX, mouseY, delta));
    }

    @Override
    public boolean method_25402(double mouseX, double mouseY, int clickedButton) {
        components.forEach((c) -> c.mouseClicked((int) mouseX, (int) mouseY, clickedButton));
        return super.method_25402(mouseX, mouseY, clickedButton);
    }

    @Override
    public boolean method_25406(double mouseX, double mouseY, int releaseButton) {
        components.forEach((c) -> c.mouseReleased((int) mouseX, (int) mouseY, releaseButton));
        return super.method_25406(mouseX, mouseY, releaseButton);
    }

    @Override
    public boolean method_25401(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (verticalAmount != 0) {
            int offset = verticalAmount < 0 ? -10 : 10;
            components.forEach((c) -> c.setY(c.getY() + offset));
        }
        return super.method_25401(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean method_25404(int keyCode, int scanCode, int modifiers) {
        components.forEach((c) -> c.onKeyPressed(keyCode));
        return super.method_25404(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean method_25400(char chr, int modifiers) {
        components.forEach((c) -> c.onKeyTyped(chr, modifiers));
        return super.method_25400(chr, modifiers);
    }

    @Override
    public boolean method_25421() {
        return false;
    }

    public ArrayList<Component> getComponents() {
        return components;
    }

    public Component getComponentByName(String name) {
        for (Component c : components) {
            if (c.getName().equalsIgnoreCase(name)) {
                return c;
            }
        }
        return null;
    }
}

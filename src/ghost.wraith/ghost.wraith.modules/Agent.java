//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package ghost.wraith;

import java.lang.instrument.Instrumentation;
import net.fabricmc.loader.api.FabricLoader;
import org.spongepowered.asm.mixin.Mixins;

public class Agent {
    public static boolean isAnInjection = false;

    public static void agentmain(String args, Instrumentation instrumentation) throws Exception {
        for (Class<?> loadedClass : instrumentation.getAllLoadedClasses()) {
            if (loadedClass.getName().equals("net.minecraft.client.MinecraftClient")) {
                isAnInjection = true;
                FabricLoader.getInstance().getModContainer("ghost").ifPresent((modContainer) -> {
                    Mixins.addConfiguration("mixins.ghost.json");

                    try {
                        Class<?> ghostClass = Class.forName(Ghost.class.getName());
                        ghostClass.getDeclaredConstructor().newInstance();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                });
                System.out.println("Injected successfully!");
                break;
            }
        }

        if (!isAnInjection) {
            System.err.println("Minecraft client not found, injection failed.");
        }

    }
}
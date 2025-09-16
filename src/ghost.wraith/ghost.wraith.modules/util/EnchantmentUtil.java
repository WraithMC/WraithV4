package ghost.wraith.util;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import ghost.wraith.util.traits.Util;
import net.minecraft.class_1304;
import net.minecraft.class_1309;
import net.minecraft.class_1799;
import net.minecraft.class_1887;
import net.minecraft.class_5321;
import net.minecraft.class_6880;

public final class EnchantmentUtil implements Util {
    private EnchantmentUtil() {
        throw new IllegalArgumentException("пошел нахуй");
    }

    public static int getLevel(class_5321<class_1887> key, class_1799 stack) {
        if (stack.method_7960()) {
            return 0;
        } else {
            for (Object2IntMap.Entry<class_6880<class_1887>> enchantment : stack.method_58657().method_57539()) {
                if (((class_6880) enchantment.getKey()).method_40225(key)) {
                    return enchantment.getIntValue();
                }
            }
            return 0;
        }
    }

    public static int getLevel(class_5321<class_1887> key, class_1304 slot, class_1309 entity) {
        return getLevel(key, entity.method_6118(slot));
    }

    public static int getLevel(class_5321<class_1887> key, class_1304 slot) {
        return getLevel(key, slot, mc.field_1724);
    }

    public static boolean has(class_5321<class_1887> key, class_1799 stack) {
        return getLevel(key, stack) > 0;
    }

    public static boolean has(class_5321<class_1887> key, class_1304 slot, class_1309 entity) {
        return getLevel(key, slot, entity) > 0;
    }

    public static boolean has(class_5321<class_1887> key, class_1304 slot) {
        return getLevel(key, slot) > 0;
    }
}

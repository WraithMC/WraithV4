package ghost.wraith.util;

import java.util.ArrayList;
import net.minecraft.class_1297;
import net.minecraft.class_1542;
import net.minecraft.class_1713;
import net.minecraft.class_1723;
import net.minecraft.class_1792;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_310;
import net.minecraft.class_437;
import net.minecraft.class_490;

public class InventoryUtil {
    public static InventoryUtil INSTANCE = new InventoryUtil();

    public static ArrayList<Integer> findItemSlot(class_1792 item, boolean bl) {
        ArrayList<Integer> arrayList = new ArrayList();

        for (int i = bl ? 0 : 9; i < 36; ++i) {
            if (getMc().field_1724.method_31548().method_5438(i).method_7909() == item) {
                arrayList.add(i);
            }
        }

        return arrayList;
    }

    public static void setCurrentSlot(int n) {
        getMc().field_1724.method_31548().field_7545 = n;
    }

    public static boolean isCollidesEntity(class_2338 blockPos) {
        for (class_1297 entity : getMc().field_1687.method_18112()) {
            if (!(entity instanceof class_1542) &&
                    (new class_238(
                            (double) blockPos.method_10263(),
                            (double) blockPos.method_10264(),
                            (double) blockPos.method_10260(),
                            (double) (blockPos.method_10263() + 1),
                            (double) (blockPos.method_10264() + 1),
                            (double) (blockPos.method_10260() + 1)
                    )).method_994(entity.method_5829())) {
                return true;
            }
        }

        return false;
    }

    public static boolean isHoldingItem(class_1792 item) {
        return getMc().field_1724.method_6047().method_7909() == item;
    }

    public static Integer findBlockSlot(class_2248 block) {
        for (int i = 0; i < 9; ++i) {
            if (getMc().field_1724.method_31548().method_5438(i).method_7909() == block.method_8389()) {
                return i;
            }
        }

        return null;
    }

    public static boolean performSlotAction(int n, int n2, class_1713 slotActionType) {
        class_437 var4 = getMc().field_1755;
        if (var4 instanceof class_490 screen) {
            getMc().field_1761.method_2906(((class_1723) screen.method_17577()).field_7763, n, n2, slotActionType, getMc().field_1724);
            return true;
        } else {
            return true;
        }
    }

    public static boolean isHoldingOffhandItem(class_1792 item) {
        return getMc().field_1724.method_6079().method_7909() == item;
    }

    public static Integer findItemInHotbar(class_1792 item) {
        for (int i = 0; i < 9; ++i) {
            if (getMc().field_1724.method_31548().method_5438(i).method_7909() == item) {
                return i;
            }
        }

        return 0;
    }

    public static class_310 getMc() {
        return class_310.method_1551();
    }
}
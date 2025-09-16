package ghost.wraith.util;

import java.util.ArrayList;
import java.util.List;

import ghost.wraith.util.traits.Util;
import net.minecraft.class_1297;
import net.minecraft.class_1542;
import net.minecraft.class_1657;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_2680;

public class BlockUtil {

    public static List<class_2338> getBlockPositionsWithinRadius(int radius) {
        List<class_2338> blockPositions = new ArrayList<>();
        class_2338 playerPos = Util.mc.field_1724.method_24515();

        int r2 = radius * radius;
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x * x + y * y + z * z <= r2) {
                        class_2338 pos = playerPos.method_10069(x, y, z);
                        if (isValidBlock(pos)) {
                            blockPositions.add(pos);
                        }
                    }
                }
            }
        }
        return blockPositions;
    }

    private static boolean isValidBlock(class_2338 blockPos) {
        class_2680 state = Util.mc.field_1687.method_8320(blockPos);
        return !state.method_26215() // not air
                && state.method_26204() // solid
                && !state.method_45474(); // not replaceable
    }

    public static boolean isCollidesEntity(class_2338 blockPos) {
        class_238 box = new class_238(
                blockPos.method_10263(), blockPos.method_10264(), blockPos.method_10260(),
                blockPos.method_10263() + 1, blockPos.method_10264() + 1, blockPos.method_10260() + 1
        );

        for (class_1297 entity : Util.mc.field_1687.method_8335(null, box)) {
            if (entity instanceof class_1542) continue;
            if (entity instanceof class_1657 && entity.method_7325()) continue;

            if (entity.method_5829().method_994(box)) {
                return true;
            }
        }
        return false;
    }
}

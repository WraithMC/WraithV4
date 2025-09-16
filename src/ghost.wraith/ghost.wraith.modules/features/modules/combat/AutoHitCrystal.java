package kambing.gardenia.features.modules.combat;

import com.google.common.eventbus.Subscribe;
import java.awt.Robot;
import kambing.gardenia.event.impl.MoveEvent;
import kambing.gardenia.features.modules.Module;
import kambing.gardenia.features.modules.Module.Category;
import kambing.gardenia.features.modules.misc.MCP;
import kambing.gardenia.features.settings.Setting;
import kambing.gardenia.util.BlockUtil;
import kambing.gardenia.util.InventoryUtil;
import kambing.gardenia.util.models.Timer;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1511;
import net.minecraft.class_1657;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_3965;
import net.minecraft.class_3966;
import net.minecraft.class_239.class_240;

public class AutoHitCrystal extends Module {
    public Setting<Float> delay = this.register(new Setting("Delay", 30.0F, 0.0F, 200.0F));
    public Setting<MODE> mode;
    public Setting<Boolean> silent;
    public Setting<Boolean> perfectTiming;
    public Setting<Boolean> pauseOnKill;
    private final Timer timer;
    private class_1657 target;
    private int progress;
    private Robot robot;

    public AutoHitCrystal() {
        super("AutoHitCrystal", "Automatically hit crystals and attempts to d-tap them by syncing with enemy damage ticks", Category.COMBAT, true, false, false);
        this.mode = this.register(new Setting("Mode", AutoHitCrystal.MODE.SingleTap));
        this.silent = this.register(new Setting("Silent", false));
        this.perfectTiming = this.register(new Setting("Perfect Timing", false));
        this.pauseOnKill = this.register(new Setting("Pause On Kill", false));
        this.timer = new Timer();

        try {
            this.robot = new Robot();
        } catch (Exception e) {
            this.robot = null; // Robot may not be available — handle gracefully
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        this.target = null;
        this.progress = 0;
        this.timer.reset();
    }

    @Subscribe
    public void onMove(MoveEvent event) {
        // Basic safety checks
        if (mc == null || mc.field_1724 == null || mc.field_1687 == null) return;
        if (mc.field_1755 != null) return; // ignore when a screen is open
        if (!mc.method_1569()) return;     // not in-game
        if (!this.timer.passedMs(((Float)this.delay.getValue()).longValue())) return;
        if ((Boolean)this.pauseOnKill.getValue() && mc.field_1724 == null) return;

        class_1657 playerEntity = this.getPlayer();
        if (playerEntity != null) {
            this.target = playerEntity;
        }

        // Progress stages
        switch (this.progress) {
            case 0 -> this.handleObsidianPlacement();
            case 1 -> this.handleBlockHit();
            case 2 -> this.handleCrystalPlacement();
            case 3 -> this.handleBlockHit();
            case 4 -> this.handleEntityHit();
            case 5 -> this.handleBlockHit();
            case 6 -> this.handleEntityHit();
            default -> this.progress = 0;
        }
    }

    private void handleObsidianPlacement() {
        int slot = findHotbarItemSlot(class_1802.field_8281); // obsidian
        if (slot != -1) {
            InventoryUtil.setCurrentSlot(slot);
            setProgress();
        }
    }

    private void handleBlockHit() {
        if (mc.field_1765 instanceof class_3965) {
            class_3965 blockHitResult = (class_3965) mc.field_1765;
            class_2338 hitPos = blockHitResult.method_17777();

            // if block is not air and placement/hit is valid
            if (!mc.field_1687.method_8320(hitPos).method_26215()) {
                class_2338 blockPos = hitPos.method_10069(blockHitResult.method_17780().method_10148(),
                        blockHitResult.method_17780().method_10164(),
                        blockHitResult.method_17780().method_10165());

                if (!BlockUtil.isCollidesEntity(blockPos)) {
                    // if target block is already the expected block (e.g., obsidian), advance
                    if (mc.field_1687.method_8320(hitPos).method_26204() == class_2246.field_10540) {
                        setProgress();
                        return;
                    }

                    // simulate right-click to place block (if Robot available)
                    if (this.robot != null) {
                        MCP.simulateRightClick(this.robot);
                    }
                    setProgress();
                }
            }
        }
    }

    private void handleCrystalPlacement() {
        int slot = findHotbarItemSlot(class_1802.field_8301); // end crystal
        if (slot != -1) {
            InventoryUtil.setCurrentSlot(slot);
            setProgress();
        }
    }

    private void handleEntityHit() {
        if (this.mode.getValue() == AutoHitCrystal.MODE.None) {
            this.toggle();
            return;
        }

        // If perfectTiming is enabled, wait until target's hurtTime is 0 (i.e., don't hit mid-tick)
        if ((Boolean)this.perfectTiming.getValue() && this.target != null && this.target.field_6235 > 0) {
            return;
        }

        class_239 hitResult = mc.field_1765;
        if (hitResult != null && hitResult.method_17783() == class_240.field_1331) {
            if (hitResult instanceof class_3966) {
                class_3966 entityHitResult = (class_3966) hitResult;
                if (entityHitResult.method_17782() instanceof class_1511) {
                    mc.field_1761.method_2918(mc.field_1724, entityHitResult.method_17782());
                    mc.field_1724.method_6104(class_1268.field_5808);
                    if (this.mode.getValue() == AutoHitCrystal.MODE.DoubleTap) {
                        setProgress();
                    } else {
                        // finish cycle
                        resetProgress();
                    }
                }
            }
        } else if (!(Boolean)this.silent.getValue()) {
            class_2338 blockPos = null;
            if (hitResult != null && hitResult.method_17783() == class_240.field_1332) {
                blockPos = ((class_3965) hitResult).method_17777();
            }

            if (blockPos != null && isBlockAtPosition(blockPos, class_2246.field_10540)) {
                for (class_1297 entity : mc.field_1687.method_18112()) {
                    if (entity instanceof class_1511 &&
                            entity.method_19538().method_1022(blockPos.method_10084().method_46558()) < (double)1.0F &&
                            mc.field_1724.method_5739(entity) <= 4.5F) {

                        float[] rotations = this.calculateAngle(mc.field_1724.method_19538(), entity.method_19538());
                        mc.field_1724.method_36457(rotations[1] + 2.5F);
                        mc.field_1761.method_2918(mc.field_1724, entity);
                        mc.field_1724.method_6104(class_1268.field_5808);

                        if (this.mode.getValue() == AutoHitCrystal.MODE.DoubleTap) {
                            setProgress();
                        } else {
                            resetProgress();
                        }
                    }
                }
            }
        }
    }

    private void setProgress() {
        ++this.progress;
        if (this.progress > 6) {
            resetProgress();
        } else {
            this.timer.reset();
        }
    }

    private void resetProgress() {
        this.progress = 0;
        this.timer.reset();
    }

    /**
     * Robust hotbar search (returns slot 0-8 or -1 if not found)
     * Avoids relying on other utilities with inconsistent return conventions.
     */
    private int findHotbarItemSlot(class_1802 item) {
        if (mc == null || mc.field_1724 == null) return -1;
        try {
            for (int i = 0; i < 9; ++i) {
                if (mc.field_1724.method_31548().method_5438(i).method_7909() == item) {
                    return i;
                }
            }
        } catch (Exception ignored) {}
        return -1;
    }

    private class_1657 getPlayer() {
        class_1657 playerEntity = null;
        for (class_1657 player : mc.field_1687.method_18456()) {
            if (player.method_5805() && !player.equals(mc.field_1724)) {
                playerEntity = player;
            }
        }
        return playerEntity;
    }

    private float[] calculateAngle(class_243 playerPos, class_243 targetPos) {
        double dx = targetPos.field_1352 - playerPos.field_1352;
        double difY = (targetPos.field_1351 + (double)1.0F) - (playerPos.field_1351 + (double)mc.field_1724.method_18381(mc.field_1724.method_18376()));
        double dz = targetPos.field_1350 - playerPos.field_1350;
        double dist = Math.sqrt(dx * dx + dz * dz);
        return new float[]{
                (float) (Math.toDegrees(Math.atan2(dz, dx)) - (double)90.0F),
                (float) ( -Math.toDegrees(Math.atan2(difY, dist)) )
        };
    }

    public static boolean isBlockAtPosition(class_2338 blockPos, class_2248 block) {
        return mc.field_1687.method_8320(blockPos).method_26204() == block;
    }

    static enum MODE {
        None,
        SingleTap,
        DoubleTap;
    }
}
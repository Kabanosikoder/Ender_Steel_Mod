package net.panther.endersteel.advancement;

import net.minecraft.advancement.criterion.Criteria;
import net.panther.endersteel.advancement.criterion.EnderSteelTeleportCriterion;
import net.panther.endersteel.advancement.criterion.StareAtBlockCriterion;

public class ModCriteria {
    public static final StareAtBlockCriterion STARE_AT_BLOCK = StareAtBlockCriterion.INSTANCE;
    public static final EnderSteelTeleportCriterion ENDER_STEEL_TELEPORT = EnderSteelTeleportCriterion.INSTANCE;

    public static void register() {
        // Register criteria
        Criteria.register(STARE_AT_BLOCK);
        Criteria.register(ENDER_STEEL_TELEPORT);
    }
}

package me.eccentric_nz.tardischunkgenerator.disguise;

import net.minecraft.server.v1_15_R1.EntityMushroomCow;
import org.bukkit.entity.MushroomCow;

public enum MUSHROOM_COW {

    BROWN(EntityMushroomCow.Type.BROWN),
    RED(EntityMushroomCow.Type.RED);

    private final EntityMushroomCow.Type nmsType;

    MUSHROOM_COW(EntityMushroomCow.Type nmsType) {
        this.nmsType = nmsType;
    }

    public static MUSHROOM_COW getFromMushroomCowType(MushroomCow.Variant variant) {
        return MUSHROOM_COW.valueOf(variant.toString());
    }

    public EntityMushroomCow.Type getNmsType() {
        return nmsType;
    }
}

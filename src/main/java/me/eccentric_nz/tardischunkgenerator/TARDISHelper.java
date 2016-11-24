package me.eccentric_nz.tardischunkgenerator;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.v1_11_R1.AttributeInstance;
import net.minecraft.server.v1_11_R1.BlockPosition;
import net.minecraft.server.v1_11_R1.Entity;
import net.minecraft.server.v1_11_R1.EntityInsentient;
import net.minecraft.server.v1_11_R1.EntityLiving;
import net.minecraft.server.v1_11_R1.EntityVillager;
import net.minecraft.server.v1_11_R1.GenericAttributes;
import net.minecraft.server.v1_11_R1.NBTTagCompound;
import net.minecraft.server.v1_11_R1.TileEntity;
import net.minecraft.server.v1_11_R1.TileEntityFurnace;
import net.minecraft.server.v1_11_R1.WorldServer;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftVillager;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class TARDISHelper extends JavaPlugin implements TARDISHelperAPI {

    public String pluginName;

    private TARDISHelper tardisHelper;

    @Override
    public void onEnable() {
        tardisHelper = this;
    }

    public TARDISHelper getTardisHelper() {
        return tardisHelper;
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new TARDISChunkGenerator();
    }

    @Override
    public Double getHorseSpeed(AbstractHorse h) {
        AttributeInstance attributes = ((EntityInsentient) ((CraftLivingEntity) h).getHandle()).getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
        return attributes.getValue();
    }

    @Override
    public void setHorseSpeed(AbstractHorse h, double speed) {
        // use about  2.25 for normalish speed
        AttributeInstance attributes = ((EntityInsentient) ((CraftLivingEntity) h).getHandle()).getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
        attributes.setValue(speed);
    }

    @Override
    public void nameFurnaceGUI(Block block, String name) {
        WorldServer ws = ((CraftWorld) block.getWorld()).getHandle();
        BlockPosition bp = new BlockPosition(block.getX(), block.getY(), block.getZ());
        TileEntity tile = ws.getTileEntity(bp);
        if (tile == null || !(tile instanceof TileEntityFurnace)) {
            return;
        }
        TileEntityFurnace furnace = (TileEntityFurnace) tile;
        furnace.a(name);
    }

    @Override
    public int getVillagerCareer(Villager v) {
        try {
            EntityVillager villager = ((CraftVillager) v).getHandle();
            Field careerField = EntityVillager.class.getDeclaredField("bJ");
            careerField.setAccessible(true);
            return careerField.getInt(villager);
        } catch (IllegalArgumentException ex) {
            System.err.println("[TARDISHelper] Failed to get villager career: " + ex.getMessage());
            return 0;
        } catch (IllegalAccessException ex) {
            System.err.println("[TARDISHelper] Failed to get villager career: " + ex.getMessage());
            return 0;
        } catch (NoSuchFieldException ex) {
            System.err.println("[TARDISHelper] Failed to get villager career: " + ex.getMessage());
            return 0;
        } catch (SecurityException ex) {
            System.err.println("[TARDISHelper] Failed to get villager career: " + ex.getMessage());
            return 0;
        }
    }

    @Override
    public void setVillagerCareer(Villager v, int c) {
        try {
            EntityVillager villager = ((CraftVillager) v).getHandle();
            Field careerField = EntityVillager.class.getDeclaredField("bJ");
            careerField.setAccessible(true);
            careerField.set(villager, c);
        } catch (NoSuchFieldException ex) {
            System.err.println("[TARDISHelper] Failed to set villager career: " + ex.getMessage());
        } catch (SecurityException ex) {
            System.err.println("[TARDISHelper] Failed to set villager career: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            System.err.println("[TARDISHelper] Failed to set villager career: " + ex.getMessage());
        } catch (IllegalAccessException ex) {
            System.err.println("[TARDISHelper] Failed to set villager career: " + ex.getMessage());
        }
    }

    @Override
    public int getVillagerCareerLevel(Villager v) {
        try {
            EntityVillager villager = ((CraftVillager) v).getHandle();
            Field careerLevelField = EntityVillager.class.getDeclaredField("bK");
            careerLevelField.setAccessible(true);
            return careerLevelField.getInt(villager);
        } catch (IllegalArgumentException ex) {
            System.err.println("[TARDISHelper] Failed to get villager career level: " + ex.getMessage());
            return 0;
        } catch (IllegalAccessException ex) {
            System.err.println("[TARDISHelper] Failed to get villager career level: " + ex.getMessage());
            return 0;
        } catch (NoSuchFieldException ex) {
            System.err.println("[TARDISHelper] Failed to get villager career level: " + ex.getMessage());
            return 0;
        } catch (SecurityException ex) {
            System.err.println("[TARDISHelper] Failed to get villager career level: " + ex.getMessage());
            return 0;
        }
    }

    @Override
    public void setVillagerCareerLevel(Villager v, int l) {
        try {
            EntityVillager villager = ((CraftVillager) v).getHandle();
            Field careerField = EntityVillager.class.getDeclaredField("bK");
            careerField.setAccessible(true);
            careerField.set(villager, l);
        } catch (NoSuchFieldException ex) {
            System.err.println("[TARDISHelper] Failed to set villager career level: " + ex.getMessage());
        } catch (SecurityException ex) {
            System.err.println("[TARDISHelper] Failed to set villager career level: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            System.err.println("[TARDISHelper] Failed to set villager career level: " + ex.getMessage());
        } catch (IllegalAccessException ex) {
            System.err.println("[TARDISHelper] Failed to set villager career level: " + ex.getMessage());
        }
    }

    @Override
    public boolean getVillagerWilling(Villager v) {
        try {
            EntityVillager villager = ((CraftVillager) v).getHandle();
            Field willingField = EntityVillager.class.getDeclaredField("bG");
            willingField.setAccessible(true);
            return willingField.getBoolean(villager);
        } catch (IllegalArgumentException ex) {
            System.err.println("[TARDISHelper] Failed to get villager willingness: " + ex.getMessage());
            return false;
        } catch (IllegalAccessException ex) {
            System.err.println("[TARDISHelper] Failed to get villager willingness: " + ex.getMessage());
            return false;
        } catch (NoSuchFieldException ex) {
            System.err.println("[TARDISHelper] Failed to get villager willingness: " + ex.getMessage());
            return false;
        } catch (SecurityException ex) {
            System.err.println("[TARDISHelper] Failed to get villager willingness: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public void setVillagerWilling(Villager v, boolean w) {
        try {
            EntityVillager villager = ((CraftVillager) v).getHandle();
            Field willingField = EntityVillager.class.getDeclaredField("bG");
            willingField.setAccessible(true);
            willingField.set(villager, w);
        } catch (NoSuchFieldException ex) {
            System.err.println("[TARDISHelper] Failed to set villager willingness: " + ex.getMessage());
        } catch (SecurityException ex) {
            System.err.println("[TARDISHelper] Failed to set villager willingness: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            System.err.println("[TARDISHelper] Failed to set villager willingness: " + ex.getMessage());
        } catch (IllegalAccessException ex) {
            System.err.println("[TARDISHelper] Failed to set villager willingness: " + ex.getMessage());
        }
    }

    @Override
    public void setCookTimeTotal(Block f, int c) {
        try {
            TileEntityFurnace furnace = (TileEntityFurnace) ((CraftWorld) f.getWorld()).getTileEntityAt(f.getX(), f.getY(), f.getZ());
            Field cttField = TileEntityFurnace.class.getDeclaredField("cookTimeTotal");
            cttField.setAccessible(true);
            cttField.set(furnace, c);
        } catch (NoSuchFieldException ex) {
            Logger.getLogger(TARDISHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(TARDISHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(TARDISHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(TARDISHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void refreshChunk(Chunk c) {
        TARDISPacketMapChunk.refreshChunk(c);
    }

    @Override
    public ItemStack setSpawnEggType(ItemStack is, EntityType et) {
        net.minecraft.server.v1_11_R1.ItemStack stack = CraftItemStack.asNMSCopy(is);
        NBTTagCompound tagCompound = stack.getTag();
        if (tagCompound == null) {
            tagCompound = new NBTTagCompound();
        }
        NBTTagCompound id = new NBTTagCompound();
        String name;
        switch (et) {
            case EVOKER:
                name = "minecraft:evocation_illager";
                break;
            case IRON_GOLEM:
                name = "minecraft:villager_golem";
                break;
            case MUSHROOM_COW:
                name = "minecraft:mooshroom";
                break;
            case PIG_ZOMBIE:
                name = "minecraft:zombie_pigman";
                break;
            case VINDICATOR:
                name = "minecraft:vindication_illager";
                break;
            default:
                name = "minecraft:" + et.toString().toLowerCase();
        }
        id.setString("id", name);
        tagCompound.set("EntityTag", id);
        stack.setTag(tagCompound);
        return CraftItemStack.asBukkitCopy(stack);
    }

    @Override
    public void setFallFlyingTag(org.bukkit.entity.Entity e) {
        Entity nmsEntity = ((CraftEntity) e).getHandle();
        NBTTagCompound tag = new NBTTagCompound();
        // writes the entity's NBT data to the `tag` object
        nmsEntity.c(tag);
        tag.setBoolean("FallFlying", true);
        // sets the entity's tag to the altered `tag`
        ((EntityLiving) nmsEntity).a(tag);
    }
}

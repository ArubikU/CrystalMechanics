package dev.arubiku.libs.blocker;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;

import dev.arubiku.libs.database.CrystalBlock;

public interface FeaturedBlock {
  Location getLocation();

  public static enum PluginType {
    VANILLA,
    NEXO,
    ITEMSADDER,
    CRUCIBLE
  }

  PluginType getType();

  public static enum BlockType {
    FURNITURE,
    BLOCK
  }

  BlockType getBlockType();

  Material getMaterial();

  String getNamespacedKey();

  Entity getEntity();

  public static FeaturedBlock getBlockByLocation(Location loc) {

    return VanillaBlock.getBlockByLocation(loc);
  }

  public static FeaturedBlock getBlockByEntity(Entity entity) {
    return VanillaBlock.getBlockByEntity(entity);
  }

  public static FeaturedBlock getBlockByCrystal(CrystalBlock block) {

    final FeaturedBlock[] fblock = new FeaturedBlock[1];
    Location loc = new Location(Bukkit.getWorld(block.getWorld()), block.getX(), block.getY(), block.getZ());
    if (block.getType() == BlockType.BLOCK) {
      fblock[0] = FeaturedBlock.getBlockByLocation(
          loc);
    } else {
      loc.getNearbyEntities(1, 1, 1).forEach(entity -> {
        if (FeaturedBlock.isValidEntity(entity)) {
          fblock[0] = FeaturedBlock.getBlockByEntity(entity);
        }
      });
    }

    return fblock[0];
  }

  public static Boolean isValidEntity(Entity entity) {
    return false;
  }
}

package dev.arubiku.libs.blocker;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;

public class NexoBlock implements FeaturedBlock {

  private final Location block;
  private final String key;
  private boolean isEntity;

  public NexoBlock(Location block, String key, boolean isEntity) {
    this.block = block;
    this.key = key;
    this.isEntity = isEntity;
  }

  @Override
  public Location getLocation() {
    return block;
  }

  @Override
  public PluginType getType() {
    return PluginType.NEXO;
  }

  @Override
  public BlockType getBlockType() {
    return isEntity ? BlockType.FURNITURE : BlockType.BLOCK;
  }

  @Override
  public Material getMaterial() {
    return block.getWorld().getBlockAt(block).getType();
  }

  @Override
  public String getNamespacedKey() {
    return key;
  }

  @Override
  public Entity getEntity() {
    // Vanilla blocks don't have entities by default
    return null;
  }

  public static FeaturedBlockI getBlockByLocation(Location loc) {
    if(com.nexomc.nexo.api.NexoBlocks.isCustomBlock(loc.getWorld().getBlockAt(loc))){
        return
    }
  }

  public static FeaturedBlockI getBlockByEntity(Entity entity) {
    Location loc = entity.getLocation().getBlock().getLocation();
    return getBlockByLocation(loc);
  }
}

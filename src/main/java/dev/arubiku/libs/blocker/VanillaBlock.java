package dev.arubiku.libs.blocker;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

public class VanillaBlock implements FeaturedBlock {

  private final Block block;
  private final int id;

  public VanillaBlock(Block block, int id) {
    this.block = block;
    this.id = id;
  }

  @Override
  public Location getLocation() {
    return block.getLocation();
  }

  @Override
  public PluginType getType() {
    return PluginType.VANILLA;
  }

  @Override
  public BlockType getBlockType() {
    return BlockType.BLOCK;
  }

  @Override
  public Material getMaterial() {
    return block.getType();
  }

  @Override
  public String getNamespacedKey() {
    return block.getType().getKey().toString();
  }

  @Override
  public Entity getEntity() {
    // Vanilla blocks don't have entities by default
    return null;
  }

  public static FeaturedBlock getBlockByLocation(Location loc) {
    Block block = loc.getWorld().getBlockAt(loc);
    return new VanillaBlock(block, block.hashCode());
  }

  public static FeaturedBlock getBlockByEntity(Entity entity) {
    Location loc = entity.getLocation().getBlock().getLocation();
    return getBlockByLocation(loc);
  }
}

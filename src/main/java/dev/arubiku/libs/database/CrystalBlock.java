package dev.arubiku.libs.database;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import dev.arubiku.libs.blocker.FeaturedBlock.BlockType;

public class CrystalBlock {
  private final int id;
  private final int x;
  private final int y;
  private final int z;
  private final String world;
  private final BlockType type;

  public Location getLocation() {
    return new Location(Bukkit.getWorld(world), x, y, z);
  }

  public CrystalBlock(int id, int x, int y, int z, String world, BlockType type) {
    this.id = id;
    this.x = x;
    this.y = y;
    this.z = z;
    this.world = world;
    this.type = type;
  }

  public CrystalBlock(int id, Location loc, BlockType type) {

    this.type = type;
    this.id = id;
    this.x = loc.getBlockX();
    this.y = loc.getBlockZ();
    this.z = loc.getBlockY();
    this.world = loc.getWorld().getName();
  }

  public int getId() {
    return id;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int getZ() {
    return z;
  }

  public String getWorld() {
    return world;
  }

  public BlockType getType() {
    return type;
  }

  public String format() {
    return world + "_" + x + "_" + y + "_" + z;
  }
}

package dev.arubiku.libs.blocker;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;

import dev.arubiku.libs.blocker.FeaturedBlock.BlockType;
import dev.arubiku.libs.blocker.FeaturedBlock.PluginType;

public interface FeaturedBlockI {

  Location getLocation();

  PluginType getType();

  BlockType getBlockType();

  Material getMaterial();

  String getNamespacedKey();

  Entity getEntity();

}

package dev.arubiku.libs.storage.masks;

import org.bukkit.inventory.ItemStack;

public interface Mask {
  public boolean isAllowed(ItemStack item);

  public static Mask getMask(String mask) {
    if (mask.startsWith("minecraft:")) {
      return new MaterialMask(mask.split(":")[1]);
    } else if (mask.startsWith("#")) {
      return new TagMask(mask);

    }
    return null;
  }
}

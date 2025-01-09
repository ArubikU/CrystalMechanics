package dev.arubiku.libs.storage.masks;

import org.bukkit.inventory.ItemStack;

public class MaterialMask implements Mask {
  private String mask;

  public MaterialMask(String mask) {
    this.mask = mask;
  }

  @Override
  public boolean isAllowed(ItemStack item) {
    return item.getType().name().equalsIgnoreCase(mask);
  }

}

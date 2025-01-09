package dev.arubiku.libs.storage.masks;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;

public class TagMask implements Mask {

  public static Map<String, Tag<Material>> tags;

  static {
    tags = TagLoader.loadTags();
  }

  public Tag<Material> tag;

  public TagMask(String mask) {
    // #minecraft:
    mask = mask.replaceFirst("#", "");
    if (mask.contains(":")) {
      mask = mask.split(":")[1];
    }
    mask = mask.toUpperCase();
    if (tags.containsKey(mask)) {
      tag = tags.get(mask);
    }
    tag = null;

  }

  @Override
  public boolean isAllowed(ItemStack item) {
    if (tag != null) {
      return tag.isTagged(item.getType());
    }
    return false;
  }

}

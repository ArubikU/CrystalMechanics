package dev.arubiku.libs.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;

import dev.arubiku.crystal.Mechanics;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class CrystalItem {

  // Obtiene un ItemStack desde una ConfigurationSection
  public static ItemStack getFromSection(ConfigurationSection section) {
    if (section == null)
      return null;

    // Material y cantidad
    String materialName = section.getString("material");
    Material material = Material.getMaterial(materialName);
    if (material == null)
      throw new IllegalArgumentException("Material inválido: " + materialName);

    int amount = section.getInt("amount", 1);
    ItemStack item = new ItemStack(material, amount);

    // Nombre usando MiniMessage
    if (section.contains("name")) {
      item.editMeta(meta -> {

        meta.displayName(MiniMessage.miniMessage().deserialize(section.getString("name")));
      });
    }

    // Descripción usando MiniMessage
    if (section.contains("lore")) {
      item.editMeta(meta -> {

        List<Component> lore = new ArrayList<>();
        for (String line : section.getStringList("lore")) {
          lore.add(MiniMessage.miniMessage().deserialize(line));
        }
        meta.lore(lore);
      });

    }
    if (section.contains("flags")) {

      if (section.getBoolean("flags.hideAll", false)) {
        item.editMeta(meta -> {
          for (ItemFlag flag : ItemFlag.values()) {
            meta.addItemFlags(flag);
          }
        });
      } else {
        List<String> flags = section.getStringList("flags.list");
        item.editMeta(meta -> {
          for (String flag : flags) {
            try {
              meta.addItemFlags(ItemFlag.valueOf(flag.toUpperCase()));
            } catch (IllegalArgumentException e) {
              throw new IllegalArgumentException("Flag inválida: " + flag);
            }
          }
        });
      }
    }

    // Encantamientos
    if (section.contains("enchants")) {
      ConfigurationSection enchantsSection = section.getConfigurationSection("enchants");
      item.editMeta(meta -> {
        for (String enchantName : enchantsSection.getKeys(false)) {
          Enchantment enchantment = Enchantment.getByKey(org.bukkit.NamespacedKey.minecraft(enchantName));
          if (enchantment != null) {
            int level = enchantsSection.getInt(enchantName, 1);
            meta.addEnchant(enchantment, level, true);
          } else {
            throw new IllegalArgumentException("Encantamiento inválido: " + enchantName);
          }
        }
      });
    }

    // Custom model data (viejo)
    if (section.contains("customModelData")) {
      int customModelData = section.getInt("customModelData");
      item.editMeta(meta -> {
        meta.setCustomModelData(customModelData);
      });
    }

    // Manejo especial para LeatherArmorMeta
    if (item.getItemMeta() instanceof LeatherArmorMeta && section.contains("color")) {
      item.editMeta(LeatherArmorMeta.class, meta -> {
        Color color = section.getColor("color");
        meta.setColor(color);
      });
    }

    // Manejo especial para PotionMeta
    if (item.getItemMeta() instanceof PotionMeta && section.contains("potionType")) {
      String potionType = section.getString("potionType");
      item.editMeta(meta -> {

        ((PotionMeta) meta).setBasePotionData(new org.bukkit.potion.PotionData(
            org.bukkit.potion.PotionType.valueOf(potionType.toUpperCase())));
      });
    }
    return item;
  }

  private static final ItemStack STONE = new ItemStack(Material.STONE);

  public static ItemStack getByMaterial(String material) {
    if (Bukkit.getPluginManager().isPluginEnabled("Nexo")) {
      if (com.nexomc.nexo.api.NexoItems.exists(material)) {
        return com.nexomc.nexo.api.NexoItems.itemFromId(material).build();
      }

    }
    if (Bukkit.getPluginManager().isPluginEnabled("ItemsAdder")) {
    }
    try {
      return ItemStack.of(Material.valueOf(material));
    } catch (Throwable e) {
      Mechanics.getPlugin().getLogger().log(Level.WARNING, "Invalid material: " + material);
    }
    return STONE;
  }
}

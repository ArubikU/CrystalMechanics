package dev.arubiku.crystal.addons;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import com.google.gson.JsonObject;

import dev.arubiku.crystal.Mechanics;
import dev.arubiku.libs.adapter.BukkitSerializer;
import dev.arubiku.libs.database.adapters.JsonDataImplementation;
import dev.arubiku.libs.storage.masks.Mask;

public class StorageAddonimplements extends Addon implements Listener {

  public StorageAddonimplements(Mechanics plugin, int id) {
    super(plugin, id);
  }

  public static final ItemStack BARRIER = new ItemStack(Material.BARRIER);
  public static JsonDataImplementation<ItemStack[]> STORAGE = new JsonDataImplementation<>((ItemStack[].class),
      "storage", t -> {
        try {
          return BukkitSerializer.itemStackArrayFromBase64(t);
        } catch (IOException e) {
          e.printStackTrace();
          return new ItemStack[0];
        }
      }, BukkitSerializer::itemStackArrayToBase64);

  ItemStack[] inventory;
  Set<Integer> storageSlots;
  Map<Integer, Mask> masks = new HashMap<>();
  Set<Integer> notStorageSlots;

  private static List<Integer> openedGuis = new ArrayList<>();

  public void openGui(Player player) {
    if (openedGuis.contains(id))
      return;

  }

  public boolean inUse() {
    return openedGuis.contains(id);
  }

  @Override
  public void save() {

  }

  @Override
  public void setup() {
    super.setup();

    ConfigurationSection storageSection = plugin.getSection(id).getConfigurationSection("Addons.storage");

    if (storageSection.contains("storageSlots")) {
      storageSlots = new HashSet<>(storageSection.getIntegerList("storageSlots"));
    }

    JsonObject meta = plugin.getData().getData().getMetadata(id).getData();
    if (STORAGE.present(meta)) {
      inventory = STORAGE.getValue(meta);
    } else {
      inventory = new ItemStack[storageSlots.toArray().length];
      plugin.getData().updateMeta(id, STORAGE.updater(inventory));
    }
    if (storageSection.contains("mask")) {
      ConfigurationSection imasks = storageSection.getConfigurationSection("mask");
      for (String key : imasks.getKeys(false)) {
        if (Double.isNaN(Double.valueOf(key)))
          continue;
        Integer slot = Integer.valueOf(key);
        Mask mask = Mask.getMask(storageSection.getString("mask." + key));
        if (mask != null) {
          masks.put(slot, mask);
        }
      }
    }

    this.notStorageSlots = notStorage();
  }

  private Set<Integer> notStorage() {
    // El tamaño máximo de slots en el inventario
    int maxSlots = inventory.length;

    // Crear un conjunto con todos los slots disponibles en el inventario
    Set<Integer> slots = new HashSet<>();
    for (int i = 0; i < maxSlots; i++) {
      slots.add(i);
    }

    // Remover los slots de almacenamiento
    slots.removeAll(storageSlots);
    slots.removeAll(masks.keySet());

    return slots;
  }

  public Set<Integer> getStorageSlots() {
    return storageSlots;
  }

  public void setStorageSlots(Set<Integer> storageSlots) {
    this.storageSlots = storageSlots;
  }

  public Map<Integer, Mask> getMasks() {
    return masks;
  }

  public void setMasks(Map<Integer, Mask> masks) {
    this.masks = masks;
  }

  public Collection<ItemStack> addAll(ItemStack... ostacks) {
    List<ItemStack> stacks = Arrays.asList(ostacks);
    Inventory fallback = createFallbackInventory();

    for (int i : masks.keySet()) {
      processMaskSlot(fallback, stacks, i);
    }
    notStorageSlots.forEach(s -> fallback.setItem(s, BARRIER));
    Collection<ItemStack> outs = fallback.addItem((@NotNull ItemStack[]) stacks.toArray()).values();
    updateInventory(fallback);
    return outs;
  }

  private Inventory createFallbackInventory() {
    Inventory fallback = Bukkit.createInventory(null, (inventory.length + 1) / 9);
    for (int i : storageSlots) {
      if (inventory[i] == null)
        continue;
      fallback.setItem(i, inventory[i]);
    }
    return fallback;
  }

  private void processMaskSlot(Inventory fallback, List<ItemStack> stacks, int slotIndex) {
    ItemStack maskSlot = fallback.getItem(slotIndex);
    boolean empty = (maskSlot == null || maskSlot.getType() == Material.AIR);

    if (!empty) {
      Mask mask = masks.get(slotIndex);
      for (ItemStack stack : stacks) {
        if (mask.isAllowed(stack)) {
          fallback.setItem(slotIndex, maskSlot);
          stacks.remove(slotIndex);
          break;
        }
      }
    }
  }

  private void updateInventory(Inventory fallback) {
    for (int i : storageSlots) {
      if (fallback.getItem(i) == null)
        continue;
      inventory[i] = fallback.getItem(i);
    }
  }

  public Map<Integer, ItemStack> getOutputContents() {
    Map<Integer, ItemStack> contents = new HashMap<>();
    for (int i : storageSlots) {
      if (masks.containsKey(i))
        continue;
      if (inventory[i] == null)
        continue;
      contents.put(i, inventory[i]);
    }
    return contents;
  }

  public Map<Integer, ItemStack> getContents() {
    Map<Integer, ItemStack> contents = new HashMap<>();
    for (int i : storageSlots) {
      if (inventory[i] == null)
        continue;
      contents.put(i, inventory[i]);
    }
    return contents;
  }
}

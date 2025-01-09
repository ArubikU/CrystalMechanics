package dev.arubiku.libs.storage;

import java.util.Map;
import java.util.Set;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import dev.arubiku.libs.storage.masks.Mask;

public class CrystalHolder implements InventoryHolder {

  Inventory inventory;
  Map<Integer, CrystalButton> buttons;
  Set<Integer> storageSlots;
  Map<Integer, Mask> masks;
  Set<Integer> buttonSlots;

  public Map<Integer, CrystalButton> getButtons() {
    return buttons;
  }

  public void setButtons(Map<Integer, CrystalButton> buttons) {
    this.buttons = buttons;
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

  public Set<Integer> getButtonSlots() {
    return buttonSlots;
  }

  public void setButtonSlots(Set<Integer> buttonSlots) {
    this.buttonSlots = buttonSlots;
  }

  @Override
  public @NotNull Inventory getInventory() {
    return inventory;
  }

  public void setInventory(Inventory inventory) {
    this.inventory = inventory;
  }

  public boolean isItemAllowedInSlot(int slot, @NotNull ItemStack item) {
    Mask mask = masks.get(slot);
    if (mask == null)
      return true; // Si no hay máscara, el ítem es válido.
    return mask.isAllowed(item); // Verifica la validez del ítem según la máscara.
  }

}

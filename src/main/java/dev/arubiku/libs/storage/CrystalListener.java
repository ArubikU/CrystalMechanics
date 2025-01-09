package dev.arubiku.libs.storage;

import java.util.EventListener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import dev.arubiku.libs.components.contexts.InventoryContext;

public class CrystalListener implements EventListener {
  @EventHandler
  public void onCrystalClick(InventoryClickEvent event) {

    if (event.getInventory().getHolder() instanceof CrystalHolder) {
      int slot = event.getSlot();
      CrystalHolder holder = (CrystalHolder) event.getInventory().getHolder();
      if (holder.getStorageSlots().contains(slot)) {
        handleStorageSlot(event, holder, slot);
        return;
      }
      if (holder.getButtonSlots().contains(slot)) {
        handleButtonSlot(event, holder, slot);
      }
    }
  }

  @EventHandler
  public void onCloseInventory(InventoryCloseEvent event) {
    if (event.getInventory().getHolder() instanceof CrystalHolder holder) {
      for (int i : holder.getMasks().keySet()) {
        // TODO
      }

    }
  }

  private void handleStorageSlot(InventoryClickEvent event, CrystalHolder holder, int slot) {
    if (holder.getMasks().containsKey(slot)) {
      if (!holder.getMasks().get(slot).isAllowed(event.getCurrentItem())) {
        event.setCancelled(true);
      }
    }
  }

  private void handleButtonSlot(InventoryClickEvent event, CrystalHolder holder, int slot) {
    InventoryContext context = new InventoryContext((Player) event.getWhoClicked(), slot, event, event.getInventory());
    switch (event.getClick()) {
      case DOUBLE_CLICK:
        holder.getButtons().get(slot).onDoubleClick(context);
        break;
      case CONTROL_DROP:
        holder.getButtons().get(slot).onCtrlDrop(context);
        break;
      case CREATIVE:
        break;
      case DROP:
        holder.getButtons().get(slot).onDrop(context);
        break;
      case LEFT:
        holder.getButtons().get(slot).onLeftClick(context);
        break;
      case MIDDLE:
        holder.getButtons().get(slot).onMiddleClick(context);
        break;
      case NUMBER_KEY:
        context.setDestinationFeatures(event.getHotbarButton(), event.getWhoClicked().getInventory());
        holder.getButtons().get(slot).onMoveToHotbar(context);
        break;
      case RIGHT:
        holder.getButtons().get(slot).onRightClick(context);
        break;
      case SHIFT_LEFT:
        holder.getButtons().get(slot).onShiftLeftClick(context);
        break;
      case SHIFT_RIGHT:
        holder.getButtons().get(slot).onShiftRightClick(context);
        break;
      case SWAP_OFFHAND:
        break;
      case UNKNOWN:
        break;
      case WINDOW_BORDER_LEFT:
        break;
      case WINDOW_BORDER_RIGHT:
        break;
      default:
        break;
    }
  }
}

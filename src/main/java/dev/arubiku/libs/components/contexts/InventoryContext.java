package dev.arubiku.libs.components.contexts;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;

import dev.arubiku.libs.components.InvokerContext;

public class InventoryContext implements InvokerContext {
  private final Player player;
  private final int slot;
  private final Event event;
  private Inventory inventory;
  private int destinationSlot;
  private Inventory destinationInventory;

  public InventoryContext(Player player, int slot, Event event, Inventory inventory) {
    this.player = player;
    this.slot = slot;
    this.event = event;
    this.inventory = inventory;
    this.destinationSlot = -1;
    this.destinationInventory = inventory;
  }

  public void setDestinationFeatures(int destinationSlot, Inventory destinationInventory) {
    this.destinationSlot = destinationSlot;
    this.destinationInventory = destinationInventory;
  }

  public int getDestinationSlot() {
    return destinationSlot;
  }

  public Inventory getDestinationInventory() {
    return destinationInventory;
  }

  public boolean isDestinationSet() {
    return destinationSlot != -1;
  }

  public Player getPlayer() {
    return player;
  }

  public int getSlot() {
    return slot;
  }

  @Override
  public Event getEvent() {
    return event;
  }

  @Override
  public Location getLocation() {
    return player.getLocation();
  }

  @Override
  public Block getBlock() {
    return player.getLocation().getBlock();
  }

  @Override
  public Entity getEntity() {
    return player;
  }

  public Inventory getInventory() {
    return inventory;
  }

  @Override
  public Map<String, String> dumpVariables(Map<String, String> vars) {
    if (vars == null) {
      vars = new HashMap<>();
    }
    vars.put("slot", String.valueOf(slot));
    vars.put("inventory_type", inventory.getType().name());
    vars.put("destination_slot", String.valueOf(destinationSlot));
    vars.put("destination_inventory_type", destinationInventory.getType().name());
    vars.put("location", formatLocation(player.getLocation()));

    return vars;
  }

  private String formatLocation(Location loc) {
    return String.format("World: %s, X: %.2f, Y: %.2f, Z: %.2f",
        loc.getWorld().getName(),
        loc.getX(),
        loc.getY(),
        loc.getZ());
  }
}

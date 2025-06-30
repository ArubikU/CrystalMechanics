package dev.arubiku.crystal.implementations;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class Nexo implements Listener {

  @EventHandler
  public void onNexoFurniturePlace(com.nexomc.nexo.api.events.furniture.NexoFurniturePlaceEvent event) {
    ItemStack item = event.getItemInHand();
    Block block = event.getBlock();
  }
}

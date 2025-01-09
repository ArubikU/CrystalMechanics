package dev.arubiku.libs.components;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

public interface InvokerContext {
  Location getLocation();

  Block getBlock();

  Entity getEntity();

  Event getEvent();

  Map<String, String> dumpVariables(Map<String, String> vars);
}

package dev.arubiku.libs.components.contexts;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

import dev.arubiku.libs.blocker.FeaturedBlock;
import dev.arubiku.libs.components.InvokerContext;

public class BlockContext implements InvokerContext {
  FeaturedBlock block;
  Block blockD = null;

  public BlockContext(FeaturedBlock block, Block blockD) {
    this.block = block;
    this.blockD = blockD;
  }

  public BlockContext(FeaturedBlock block) {
    this.block = block;
    this.blockD = block.getLocation().getBlock();
  }

  public void setBlock(Block blockD) {
    this.blockD = blockD;
  }

  @Override
  public Location getLocation() {
    return block.getLocation();
  }

  @Override
  public Block getBlock() {
    return block.getLocation().getBlock();
  }

  Event event = null;

  public void setEvent(Event event) {
    this.event = event;
  }

  @Override
  public Event getEvent() {
    return event;
  }

  @Override
  public Map<String, String> dumpVariables(Map<String, String> vars) {
    if (vars == null) {
      vars = new java.util.HashMap<>();
    }

    Location loc = block.getLocation();

    vars.put("block_type", block.getBlockType().name());
    vars.put("furniture_type", block.getNamespacedKey());
    vars.put("block_location.x", loc.getBlockX() + "");
    vars.put("block_location.y", loc.getBlockY() + "");
    vars.put("block_location.z", loc.getBlockZ() + "");
    if (event != null) {
      vars.put("event", event.getEventName());
    }

    return vars;
  }

  @Override
  public Entity getEntity() {
    return null;
  }
}

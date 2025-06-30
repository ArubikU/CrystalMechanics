package dev.arubiku.libs.components.contexts;

import java.util.Map;

import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

import dev.arubiku.libs.blocker.FeaturedBlock.BlockType;
import dev.arubiku.libs.blocker.FeaturedBlockI;
import dev.arubiku.libs.components.InvokerContext;

public class FurnitureContext implements InvokerContext {
  FeaturedBlockI block;
  Entity entity = null;

  public FurnitureContext(FeaturedBlockI block, Entity entity) {
    this.block = block;
    this.entity = entity;
  }

  public FurnitureContext(FeaturedBlockI block) {
    this.block = block;
    this.entity = block.getEntity();
  }

  public void setEntity(Entity entity) {
    this.entity = entity;
  }

  @Override
  public Location getLocation() {
    return block.getLocation();
  }

  @Override
  @Nullable
  public Entity getEntity() {
    if (block.getBlockType() == BlockType.FURNITURE) {
      return block.getEntity();
    }
    return entity;
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
    vars.put("block_location.x", loc.getBlockX() + "");
    vars.put("block_location.y", loc.getBlockY() + "");
    vars.put("block_location.z", loc.getBlockZ() + "");
    vars.put("entity", entity != null ? entity.getName() : "None");

    if (event != null) {
      vars.put("event", event.getEventName());
    }

    return vars;
  }
}

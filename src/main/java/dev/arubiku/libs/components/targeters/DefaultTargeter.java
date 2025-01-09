package dev.arubiku.libs.components.targeters;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import dev.arubiku.libs.components.Targeter;

public class DefaultTargeter implements Targeter {

  private Entity defentity = null;

  @Override
  public Entity[] getTargets(Location location) {
    if (defentity != null) {
      return new Entity[] { defentity };
    }
    Entity entity = location.getWorld().getNearbyEntities(location, 1, 1, 1).stream()
        .filter(e -> e instanceof Player)
        .findFirst()
        .orElse(null);
    return entity != null ? new Entity[] { entity } : new Entity[0];
  }

  @Override
  public void setArgumentsRaw(Map<String, String> arguments) {
  }

  @Override
  public void setupArguments() {
  }

  public void setEntity(Entity entity) {
    this.defentity = entity;
  }
}

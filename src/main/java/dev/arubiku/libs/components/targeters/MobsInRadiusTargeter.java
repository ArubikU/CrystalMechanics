package dev.arubiku.libs.components.targeters;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import dev.arubiku.libs.components.Argument;
import dev.arubiku.libs.components.GenericArgument;
import dev.arubiku.libs.components.Targeter;

public class MobsInRadiusTargeter implements Targeter {
  @SuppressWarnings("unchecked")
  private Argument<Double> radius = GenericArgument.RADIUS.getArgument().build();
  @SuppressWarnings("unchecked")
  private Argument<Double> yoffset = GenericArgument.Y_OFFSET.getArgument().build();
  @SuppressWarnings("unchecked")
  private Argument<Double> xoffset = GenericArgument.X_OFFSET.getArgument().build();
  @SuppressWarnings("unchecked")
  private Argument<Double> zoffset = GenericArgument.Z_OFFSET.getArgument().build();

  @Override
  public Entity[] getTargets(Location location) {
    double radius = this.radius.getValue();
    return location.getWorld()
        .getNearbyEntities(location.add(xoffset.getValue(), yoffset.getValue(), zoffset.getValue()), radius, radius,
            radius)
        .stream()
        .filter(entity -> entity instanceof LivingEntity && !(entity instanceof Player))
        .toArray(Entity[]::new);
  }

  Map<String, String> arguments = new HashMap<>();

  @Override
  public void setArgumentsRaw(Map<String, String> arguments) {
    this.arguments = arguments;
  }

  @Override
  public void setupArguments() {
    for (String key : arguments.keySet()) {
      if (radius.matchesAlias(key)) {
        radius.setValue(arguments.get(key));
      }
      if (yoffset.matchesAlias(key)) {
        yoffset.setValue(arguments.get(key));
      }
      if (xoffset.matchesAlias(key)) {
        xoffset.setValue(arguments.get(key));
      }
      if (zoffset.matchesAlias(key)) {
        zoffset.setValue(arguments.get(key));
      }
    }
  }
}

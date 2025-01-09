package dev.arubiku.libs.components;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public interface Targeter {
  Entity[] getTargets(Location location);

  void setArgumentsRaw(Map<String, String> arguments);

  void setupArguments();
}

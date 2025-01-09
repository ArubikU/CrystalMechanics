package dev.arubiku.crystal.addons;

import org.bukkit.event.Listener;

import dev.arubiku.crystal.Mechanics;
import dev.arubiku.libs.database.adapters.JsonDataImplementation;

public class ExperienceAddon extends Addon implements Listener {

  public ExperienceAddon(Mechanics plugin, int id) {
    super(plugin, id);
  }

  public static JsonDataImplementation<Integer> EXPERIENCE = new JsonDataImplementation<>(Integer.class, "experience",
      Integer::valueOf, i -> Integer.toString(i));
  public static JsonDataImplementation<Integer> MAX_EXPERIENCE = new JsonDataImplementation<>(Integer.class,
      "max_experience",
      Integer::valueOf, i -> Integer.toString(i));

}

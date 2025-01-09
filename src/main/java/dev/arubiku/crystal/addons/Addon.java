package dev.arubiku.crystal.addons;

import org.bukkit.plugin.Plugin;

import dev.arubiku.crystal.Mechanics;

public abstract class Addon {
  public final Mechanics plugin;
  public int id;

  public Addon(Plugin plugin, int id) {
    this.plugin = (Mechanics) plugin;
    this.id = id;
  }

  public void setup() {
  }

  public void unsetup() {

  }

  public void save() {

  }
}

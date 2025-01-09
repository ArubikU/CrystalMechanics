package dev.arubiku.crystal.addons;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

import org.bukkit.configuration.ConfigurationSection;

import dev.arubiku.crystal.Mechanics;
import dev.arubiku.libs.database.CrystalBlock;

public class AddonProvider<T extends Addon> {
  public Function<ConfigurationSection, Boolean> isValid;
  public Mechanics plugin;
  public Class<T> clazz;

  public AddonProvider(Function<ConfigurationSection, Boolean> isValid, Mechanics plugin) {
    this.isValid = isValid;
    this.plugin = plugin;
  }

  public Boolean isValidB(ConfigurationSection sec) {
    return this.isValid.apply(sec);
  }

  public T setup(CrystalBlock block) {
    try {
      return (T) clazz.getConstructors()[0].newInstance(plugin, block.getId());
    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
        | SecurityException e) {
      return null;
    }
  }
}

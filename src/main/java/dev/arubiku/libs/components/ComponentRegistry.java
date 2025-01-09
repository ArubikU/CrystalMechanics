package dev.arubiku.libs.components;

import java.util.HashMap;
import java.util.Map;

public class ComponentRegistry {
  private static final Map<String, Class<? extends Component>> Components = new HashMap<>();

  public static void registerComponent(String name, Class<? extends Component> component) {
    Components.put(name.toUpperCase(), component);
  }

  public static Class<? extends Component> getComponent(String name) {
    return Components.get(name.toUpperCase());
  }

  public static Boolean hasComponent(String name) {
    return Components.containsKey(name.toUpperCase());
  }

  private static final Map<String, Class<? extends Targeter>> Targeters = new HashMap<>();

  public static void registerTargeter(String name, Class<? extends Targeter> targeter) {
    Targeters.put(name.toUpperCase(), targeter);
  }

  public static Class<? extends Targeter> getTargeter(String name) {
    return Targeters.get(name.toUpperCase());
  }

  public static Boolean hasTargeter(String name) {
    return Targeters.containsKey(name.toUpperCase());
  }
}

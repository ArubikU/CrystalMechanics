package dev.arubiku.libs.components;

import java.util.Arrays;

public class Trigger {

  private final String name;
  private final String[] aliases;

  public Trigger(String name, String... aliases) {
    this.name = name;
    this.aliases = aliases;
  }

  public String getName() {
    return name;
  }

  public boolean matchesAlias(String input) {
    String alias = input.toLowerCase().split(":")[0];
    alias = alias.replaceFirst("on", "");
    return name.equals(alias) || Arrays.asList(aliases).contains(alias);
  }

  public int getArgument(String input) {
    String[] split = input.split(":");
    if (split.length == 1) {
      return 0;
    }
    return Integer.parseInt(split[1]);
  }

}

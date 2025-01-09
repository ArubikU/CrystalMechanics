package dev.arubiku.libs.components;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.annotation.Nullable;

public class Argument<T> {
  private final String name;
  private final String[] aliases;
  private final Predicate<T> validator;
  private final String errorMessage;
  private final Function<String, T> parser;
  private T value;

  public Argument(String name, String[] aliases, Predicate<T> validator, String errorMessage,
      @Nullable Function<String, T> parser) {
    this.name = name;
    this.aliases = aliases;
    this.validator = validator;
    this.errorMessage = errorMessage;
    this.parser = parser;
  }

  public boolean setValue(String strValue) {
    if (parser == null) {
      this.value = (T) strValue;
      return true;
    }
    try {
      T parsedValue = parser.apply(strValue);
      if (validator.test(parsedValue)) {
        this.value = parsedValue;
        return true;
      }
    } catch (Exception e) {
      System.err.println("Error parsing value for " + name + ": " + e.getMessage());
    }
    System.err.println(errorMessage);
    return false;
  }

  public T getValue() {
    return value;
  }

  public String getName() {
    return name;
  }

  public boolean matchesAlias(String alias) {
    return name.equals(alias) || Arrays.asList(aliases).contains(alias);
  }
}

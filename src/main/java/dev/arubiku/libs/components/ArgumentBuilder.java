
package dev.arubiku.libs.components;

import java.util.function.Function;
import java.util.function.Predicate;

public class ArgumentBuilder<T> {
  private String name;
  private String[] aliases = new String[0];
  private Predicate<T> validator = value -> true; // Validador por defecto: siempre true
  private String errorMessage = "Invalid argument.";
  private Function<String, T> parser = null;

  public static <T> ArgumentBuilder<T> create(Class<T> type) {
    return new ArgumentBuilder<>();
  }

  public static <T> ArgumentBuilder<T> create() {
    return new ArgumentBuilder<>();
  }

  public ArgumentBuilder<T> withName(String name) {
    this.name = name;
    return this;
  }

  public ArgumentBuilder<T> withAliases(String... aliases) {
    this.aliases = aliases;
    return this;
  }

  public ArgumentBuilder<T> withValidator(Predicate<T> validator) {
    this.validator = validator;
    return this;
  }

  public ArgumentBuilder<T> withErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
    return this;
  }

  public ArgumentBuilder<T> withParser(Function<String, T> parser) {
    this.parser = parser;
    return this;
  }

  public Argument<T> build() {
    if (name == null) {
      throw new IllegalStateException("Name must be provided before building the Argument.");
    }
    return new Argument<>(name, aliases, validator, errorMessage, parser);
  }
}

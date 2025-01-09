package dev.arubiku.libs.components;

public enum GenericArgument {
  Y_OFFSET(ArgumentBuilder.create(Double.class)
      .withName("yoffset")
      .withAliases("yoff")
      .withErrorMessage("Y Offset must be a valid number")
      .withValidator(d -> d != null && !Double.isNaN(d))
      .withParser(Double::valueOf)),

  X_OFFSET(ArgumentBuilder.create(Double.class)
      .withName("xoffset")
      .withAliases("xoff")
      .withErrorMessage("X Offset must be a valid number")
      .withValidator(d -> d != null && !Double.isNaN(d))
      .withParser(Double::valueOf)),

  Z_OFFSET(ArgumentBuilder.create(Double.class)
      .withName("zoffset")
      .withAliases("zoff")
      .withErrorMessage("Z Offset must be a valid number")
      .withValidator(d -> d != null && !Double.isNaN(d))
      .withParser(Double::valueOf)),

  RADIUS(ArgumentBuilder.create(Double.class)
      .withName("radius")
      .withAliases("r")
      .withErrorMessage("Radius must be a positive number")
      .withValidator(d -> d != null && d > 0)
      .withParser(Double::valueOf)),

  TICKS(ArgumentBuilder.create(Long.class)
      .withName("ticks")
      .withAliases("t")
      .withErrorMessage("Ticks must be a non-negative integer")
      .withValidator(l -> l != null && l >= 0)
      .withParser(Long::valueOf)),

  DAMAGE(ArgumentBuilder.create(Double.class)
      .withName("damage")
      .withAliases("dmg")
      .withErrorMessage("Damage must be a positive number")
      .withValidator(d -> d != null && d > 0)
      .withParser(Double::valueOf)),

  COMMAND(ArgumentBuilder.create(String.class)
      .withName("command")
      .withAliases("cmd"))

  ;

  private final ArgumentBuilder<?> argument;

  private GenericArgument(ArgumentBuilder<?> argument) {
    this.argument = argument;
  }

  public ArgumentBuilder getArgument() {
    return argument;
  }
}

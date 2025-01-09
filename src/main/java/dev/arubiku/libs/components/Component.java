package dev.arubiku.libs.components;

import java.util.HashMap;
import java.util.Map;

public abstract class Component {

  // Mapa para almacenar los argumentos
  protected Map<String, String> arguments = new HashMap<>();

  // Método abstracto para configurar argumentos
  public abstract void setupArguments();

  // Método abstracto para ejecutar la lógica principal del componente
  public abstract void execute(InvokerContext context, Targeter targeter);

  // Método para establecer los argumentos en bruto
  public void setArgumentsRaw(Map<String, String> arguments) {
    this.arguments = arguments;
  }
}

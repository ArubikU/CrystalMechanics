package dev.arubiku.libs.components;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev.arubiku.libs.components.targeters.DefaultTargeter;

public class ConfigurationReader {

  public Optional<PackagedComponent> parseComponentConfig(String componentConfig) {
    String[] parts = componentConfig.split(" ", 2);
    if (parts.length != 2) {
      System.err.println("Invalid component configuration: " + componentConfig);
      return Optional.empty();
    }

    String componentPart = parts[0];
    String triggerPart = parts[1];

    String componentName = componentPart.split("\\[")[0];
    Map<String, String> args = parseArguments(componentPart);
    String trigger = parseTrigger(triggerPart);
    String targeter = parseTargeter(triggerPart);

    Component component = createComponent(componentName, args);
    Targeter targeterInstance = createTargeter(targeter, args);
    PackagedComponent comp = new PackagedComponent(component, trigger, targeterInstance);
    return Optional.of(comp);
  }

  private Map<String, String> parseArguments(String componentPart) {
    Map<String, String> args = new HashMap<>();
    Pattern pattern = Pattern.compile("\\[(.*?)\\]");
    Matcher matcher = pattern.matcher(componentPart);
    if (matcher.find()) {
      String[] argPairs = matcher.group(1).split(";");
      for (String argPair : argPairs) {
        String[] keyValue = argPair.split("=");
        if (keyValue.length == 2) {
          args.put(keyValue[0].trim(), keyValue[1].trim());
        }
      }
    }
    return args;
  }

  private String parseTrigger(String triggerPart) {
    if (triggerPart.startsWith("~")) {
      String[] triggerInfo = triggerPart.substring(1).split(":");
      return triggerInfo[0];
    }
    return "";
  }

  private String parseTargeter(String triggerPart) {
    if (triggerPart.startsWith("@")) {
      return triggerPart.split(" ")[0].substring(1);
    }
    return "DefaultTargeter";
  }

  private Component createComponent(String componentName, Map<String, String> args) {
    if (ComponentRegistry.hasComponent(componentName)) {
      Class<? extends Component> componentClass = ComponentRegistry.getComponent(componentName);
      try {
        Component component = componentClass.newInstance();
        component.setArgumentsRaw(args);

        return component;
      } catch (InstantiationException | IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  private Targeter createTargeter(String targeterName, Map<String, String> args) {
    if (ComponentRegistry.hasTargeter(targeterName)) {
      Class<? extends Targeter> targeterClass = ComponentRegistry.getTargeter(targeterName);
      try {
        Targeter targeter = targeterClass.newInstance();
        targeter.setArgumentsRaw(args);
        return targeter;
      } catch (InstantiationException | IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    return new DefaultTargeter();
  }
}

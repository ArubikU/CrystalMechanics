package dev.arubiku.libs.components;

public class PackagedComponent {
  private Component component;
  private String trigger;
  private Targeter targeter;

  public PackagedComponent(Component component, String trigger, Targeter targeter) {
    this.component = component;
    this.trigger = trigger;
    this.targeter = targeter;
  }

  public void Invoke(InvokerContext context) {
    component.execute(context, targeter);
  }

  public Component getComponent() {
    return component;
  }

  public String getTrigger() {
    return trigger;
  }

  public Targeter getTargeter() {
    return targeter;
  }
}

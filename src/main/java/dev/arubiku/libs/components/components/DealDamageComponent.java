package dev.arubiku.libs.components.components;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import dev.arubiku.libs.components.Argument;
import dev.arubiku.libs.components.Component;
import dev.arubiku.libs.components.GenericArgument;
import dev.arubiku.libs.components.InvokerContext;
import dev.arubiku.libs.components.Targeter;
import dev.arubiku.libs.components.annotations.ComponentInfo;

@ComponentInfo(name = "DealDamage", description = "Deals damage to entities what are targeted")
public class DealDamageComponent extends Component {

  @SuppressWarnings("unchecked")
  private Argument<Double> damage = GenericArgument.DAMAGE.getArgument().build();

  public DealDamageComponent() {
    damage.setValue("1.0");
  }

  @Override
  public void execute(InvokerContext context, Targeter targeter) {
    Entity[] targets = targeter.getTargets(context.getLocation());
    for (Entity target : targets) {
      if (target instanceof LivingEntity) {
        ((LivingEntity) target).damage(damage.getValue());
      }
    }
  }

  @Override
  public void setupArguments() {
    for (String key : arguments.keySet()) {
      if (damage.matchesAlias(key)) {
        damage.setValue(arguments.get(key));
      }
    }
  }
}

package dev.arubiku.libs.components.components;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import dev.arubiku.libs.components.Argument;
import dev.arubiku.libs.components.Component;
import dev.arubiku.libs.components.GenericArgument;
import dev.arubiku.libs.components.InvokerContext;
import dev.arubiku.libs.components.Targeter;
import dev.arubiku.libs.components.annotations.ComponentInfo;

@ComponentInfo(name = "ApplyFire", description = "Applies fire to entities what are targeted")
public class ApplyFireComponent extends Component {

  @SuppressWarnings("unchecked")
  private Argument<Long> time = GenericArgument.DAMAGE.getArgument().build();

  public ApplyFireComponent() {
    time.setValue("40");
  }

  @Override
  public void execute(InvokerContext context, Targeter targeter) {
    Entity[] targets = targeter.getTargets(context.getLocation());
    for (Entity target : targets) {
      if (target instanceof LivingEntity) {
        ((LivingEntity) target).setFireTicks(time.getValue().intValue());
      }
    }
  }

  @Override
  public void setupArguments() {
    for (String key : arguments.keySet()) {
      if (time.matchesAlias(key)) {
        time.setValue(arguments.get(key));
      }
    }
  }
}

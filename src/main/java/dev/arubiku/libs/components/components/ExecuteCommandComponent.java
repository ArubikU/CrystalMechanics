package dev.arubiku.libs.components.components;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import dev.arubiku.libs.components.Argument;
import dev.arubiku.libs.components.Component;
import dev.arubiku.libs.components.GenericArgument;
import dev.arubiku.libs.components.InvokerContext;
import dev.arubiku.libs.components.Targeter;
import dev.arubiku.libs.components.annotations.ComponentInfo;
import dev.arubiku.libs.components.contexts.InventoryContext;
import dev.arubiku.libs.components.targeters.DefaultTargeter;

@ComponentInfo(name = "ExecuteCommandComponent")
public class ExecuteCommandComponent extends Component {

  @Override
  public void setupArguments() {
    for (String key : arguments.keySet()) {
      if (command.matchesAlias(key)) {
        command.setValue(arguments.get(key));
      }
    }
  }

  @SuppressWarnings("unchecked")
  private Argument<String> command = GenericArgument.COMMAND.getArgument().build();

  @Override
  public void execute(InvokerContext context, Targeter targeter) {

    Player player = null;
    Map<String, String> variables = new HashMap<String, String>();
    String icommand = command.getValue();
    if (context instanceof InventoryContext cont) {
      player = cont.getPlayer();
    }
    if (player == null) {
      if (targeter instanceof DefaultTargeter target) {
        for (Entity entity : target.getTargets(context.getLocation())) {
          if (entity instanceof Player) {
            player = (Player) player;
            break;
          }
        }
      }
    }

    if (player != null)
      variables.put("player", player.getName());
    variables = context.dumpVariables(variables);

  }

}

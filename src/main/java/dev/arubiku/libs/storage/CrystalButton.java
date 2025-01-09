package dev.arubiku.libs.storage;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.PlayerInventory;

import dev.arubiku.libs.components.InvokerContext;
import dev.arubiku.libs.components.PackagedComponent;
import dev.arubiku.libs.components.contexts.InventoryContext;

public class CrystalButton {

  private List<PackagedComponent> components = new ArrayList<>();

  public void addComponent(PackagedComponent component) {
    components.add(component);
  }

  public void onRightClick(InvokerContext context) {
    components.forEach(component -> {
      if (CrystalInventoryTriggers.RIGHT_CLICK_TRIGGER.matchesAlias(component.getTrigger())) {
        component.getComponent().execute(context, component.getTargeter());
      }
    });
  }

  public void onLeftClick(InvokerContext context) {
    components.forEach(component -> {
      if (CrystalInventoryTriggers.LEFT_CLICK_TRIGGER.matchesAlias(component.getTrigger())) {
        component.getComponent().execute(context, component.getTargeter());
      }
    });
  }

  public void onShiftLeftClick(InvokerContext context) {
    components.forEach(component -> {
      if (CrystalInventoryTriggers.SHIFT_LEFT_CLICK_TRIGGER.matchesAlias(component.getTrigger())) {
        component.getComponent().execute(context, component.getTargeter());
      }
    });
  }

  public void onShiftRightClick(InvokerContext context) {
    components.forEach(component -> {
      if (CrystalInventoryTriggers.SHIFT_RIGHT_CLICK_TRIGGER.matchesAlias(component.getTrigger())) {
        component.getComponent().execute(context, component.getTargeter());
      }
    });
  }

  public void onDrop(InvokerContext context) {
    components.forEach(component -> {
      if (CrystalInventoryTriggers.DROP_TRIGGER.matchesAlias(component.getTrigger())) {
        component.getComponent().execute(context, component.getTargeter());
      }
    });
  }

  public void onShiftDrop(InvokerContext context) {
    components.forEach(component -> {
      if (CrystalInventoryTriggers.SHIFT_DROP_TRIGGER.matchesAlias(component.getTrigger())) {
        component.getComponent().execute(context, component.getTargeter());
      }
    });
  }

  public void onCtrlDrop(InvokerContext context) {
    components.forEach(component -> {
      if (CrystalInventoryTriggers.CTRL_DROP_TRIGGER.matchesAlias(component.getTrigger())) {
        component.getComponent().execute(context, component.getTargeter());
      }
    });
  }

  public void onDoubleClick(InvokerContext context) {
    components.forEach(component -> {
      if (CrystalInventoryTriggers.DOUBLE_CLICK_TRIGGER.matchesAlias(component.getTrigger())) {
        component.getComponent().execute(context, component.getTargeter());
      }
    });
  }

  public void onMiddleClick(InvokerContext context) {
    components.forEach(component -> {
      if (CrystalInventoryTriggers.MIDDLE_CLICK_TRIGGER.matchesAlias(component.getTrigger())) {
        component.getComponent().execute(context, component.getTargeter());
      }
    });
  }

  public void onMoveToHotbar(InvokerContext originaContext) {
    if (!(originaContext instanceof InventoryContext context)) {
      return;
    }

    if (!context.isDestinationSet() || !(context.getDestinationInventory() instanceof PlayerInventory)) {
      return;
    }

    for (PackagedComponent component : components) {
      if (CrystalInventoryTriggers.MOVE_TO_HOTBAR_TRIGGER.matchesAlias(component.getTrigger()) &&
          context.getDestinationSlot() == CrystalInventoryTriggers.MOVE_TO_HOTBAR_TRIGGER
              .getArgument(component.getTrigger())) {
        component.getComponent().execute(context, component.getTargeter());
      }
    }
  }

}

package dev.arubiku.libs.storage;

import dev.arubiku.libs.components.Trigger;

public class CrystalInventoryTriggers {

  public static Trigger MIDDLE_CLICK_TRIGGER = new Trigger("middle_click", "mc");
  public static Trigger RIGHT_CLICK_TRIGGER = new Trigger("right_click", "rc");
  public static Trigger LEFT_CLICK_TRIGGER = new Trigger("left_click", "lc");
  public static Trigger SHIFT_RIGHT_CLICK_TRIGGER = new Trigger("shift_right_click", "src");
  public static Trigger SHIFT_LEFT_CLICK_TRIGGER = new Trigger("shift_left_click", "slc");
  public static Trigger DROP_TRIGGER = new Trigger("drop", "d");
  public static Trigger SHIFT_DROP_TRIGGER = new Trigger("shift_drop", "sd");
  public static Trigger MOVE_TO_HOTBAR_TRIGGER = new Trigger("move_to_hotbar", "mth");
  public static Trigger CTRL_DROP_TRIGGER = new Trigger("ctrl_drop", "cd");
  public static Trigger DOUBLE_CLICK_TRIGGER = new Trigger("double_click", "dc");
  public static Trigger SWAP_OFFHAND_TRIGGER = new Trigger("swap_offhand", "spoffh");

  public static Trigger ON_CLOSE_GUI_TRIGGER = new Trigger("close_gui", "clgui");
}

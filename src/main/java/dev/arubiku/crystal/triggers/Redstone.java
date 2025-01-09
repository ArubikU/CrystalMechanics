package dev.arubiku.crystal.triggers;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.FaceAttachable;
import org.bukkit.block.data.FaceAttachable.AttachedFace;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.type.RedstoneWallTorch;
import org.bukkit.block.data.type.RedstoneWire;
import org.bukkit.block.data.type.RedstoneWire.Connection;

import com.destroystokyo.paper.MaterialTags;
import com.mojang.datafixers.util.Pair;

import dev.arubiku.crystal.Mechanics;
import dev.arubiku.crystal.addons.Addon;
import dev.arubiku.crystal.addons.RedstoneAddon;
import dev.arubiku.libs.blocker.FeaturedBlock;
import dev.arubiku.libs.blocker.FeaturedBlock.BlockType;
import dev.arubiku.libs.components.InvokerContext;
import dev.arubiku.libs.components.Trigger;
import dev.arubiku.libs.components.contexts.BlockContext;
import dev.arubiku.libs.components.contexts.FurnitureContext;
import dev.arubiku.libs.database.CrystalBlock;

public class Redstone {
  public static Trigger RECIVE_REDSTONE_TRIGGER = new Trigger("recieve_redstone", "signal", "recieveredstone");
  public static Trigger STOP_RECIEVING_REDSTONE_TRIGGER = new Trigger("off_redstone", "signaloff", "offredstone");

  public Mechanics plugin;

  public void onTick() {
    for (int id : plugin.blockaddons.keySet()) {
      boolean redstoneModule = false;
      for (Addon addon : plugin.blockaddons.get(id)) {
        if (addon instanceof RedstoneAddon) {
          redstoneModule = true;
          break;
        }
      }
      if (!redstoneModule)
        continue;

      CrystalBlock block = plugin.getData().getData().getBlock(id);

      InvokerContext context;
      if (block.getType() == BlockType.BLOCK) {
        context = new BlockContext(FeaturedBlock.getBlockByCrystal(block));
      } else {
        context = new FurnitureContext(FeaturedBlock.getBlockByCrystal(block));
      }

      Pair<Integer, Integer> pair = onTickBlock(block.getLocation().getBlock());
      if (pair.getFirst() == pair.getSecond())
        return;

      if (pair.getFirst() == 0) {
        plugin.blocks.get(id).forEach(compi -> {
          if (RECIVE_REDSTONE_TRIGGER.matchesAlias(compi.getTrigger())) {
            compi.Invoke(context);
          }
        });
      }
      if (pair.getSecond() == 0) {

        plugin.blocks.get(id).forEach(compi -> {
          if (STOP_RECIEVING_REDSTONE_TRIGGER.matchesAlias(compi.getTrigger())) {
            compi.Invoke(context);
          }
        });
      }
    }
  }

  private static BlockFace[] faces = new BlockFace[] { BlockFace.EAST, BlockFace.NORTH, BlockFace.EAST,
      BlockFace.WEST };

  static HashMap<String, Integer> levels = new HashMap<>();

  public static Pair<Integer, Integer> onTickBlock(Block block) {

    int level = levels.getOrDefault(block.getLocation().toString(), 0);
    int redstonefeed = 0;
    Block upBlock = block.getRelative(BlockFace.UP);
    if ((upBlock.getType() == Material.REDSTONE_BLOCK)
        || (upBlock.getType() == Material.LEVER && upBlock.isBlockPowered()
            && ((FaceAttachable) upBlock.getBlockData()).getAttachedFace() == AttachedFace.FLOOR)
        || (upBlock.getType() == Material.REDSTONE_WALL_TORCH && ((RedstoneWallTorch) upBlock.getBlockData()).isLit())
        || (upBlock.getType() == Material.REDSTONE_TORCH && ((Lightable) upBlock.getBlockData()).isLit())
        || ((MaterialTags.PRESSURE_PLATES.isTagged(upBlock.getType()))
            && (block.isBlockPowered()))
        || ((Tag.BUTTONS.isTagged(upBlock.getType())
            && ((FaceAttachable) upBlock.getBlockData()).getAttachedFace() == AttachedFace.FLOOR))) {
      levels.put(block.getLocation().toString(), 14);
      return Pair.of(level, 14);
    }
    if (upBlock.getType() == Material.REDSTONE_WIRE) {
      redstonefeed = ((RedstoneWire) upBlock.getBlockData()).getPower() - 1;
    }
    upBlock = null;

    Block downBlock = block.getRelative(BlockFace.DOWN);
    if (downBlock.getType() == Material.REDSTONE_BLOCK
        || (downBlock.getType() == Material.LEVER && downBlock.isBlockPowered()
            && ((FaceAttachable) downBlock.getBlockData()).getAttachedFace() == AttachedFace.CEILING)
        || (Tag.BUTTONS.isTagged(downBlock.getType()) && downBlock.isBlockPowered()
            && ((FaceAttachable) downBlock.getBlockData()).getAttachedFace() == AttachedFace.CEILING)) {
      levels.put(block.getLocation().toString(), 14);
      return Pair.of(level, 14);
    }
    downBlock = null;
    for (BlockFace side : faces) {
      Block refBlock = block.getRelative(side);
      if (refBlock.getType() == Material.REDSTONE_BLOCK
          || (refBlock.getType() == Material.LEVER && refBlock.isBlockPowered()
              && ((Directional) refBlock.getBlockData()).getFacing() == side)
          || (refBlock.getType() == Material.REPEATER && refBlock.isBlockPowered()
              && ((Directional) refBlock.getBlockData()).getFacing() == side)
          || (refBlock.getType() == Material.REDSTONE_WALL_TORCH
              && ((RedstoneWallTorch) refBlock.getBlockData()).isLit())
          || (refBlock.getType() == Material.REDSTONE_TORCH && ((Lightable) refBlock.getBlockData()).isLit())

          || ((Tag.BUTTONS.isTagged(refBlock.getType())
              && ((Directional) refBlock.getBlockData()).getFacing() == side))
          || ((Tag.PRESSURE_PLATES.isTagged(refBlock.getType())) && refBlock.isBlockPowered())) {
        levels.put(block.getLocation().toString(), 14);
        return Pair.of(level, 14);
      }

      if (refBlock.getType() == Material.REDSTONE_WIRE) {
        RedstoneWire wire = (RedstoneWire) refBlock.getBlockData();
        if (wire.getFace(side) == Connection.SIDE) {
          if (wire.getPower() > redstonefeed) {
            redstonefeed = wire.getPower() - 1;
          }
        }
      }
    }

    levels.put(block.getLocation().toString(), redstonefeed);
    return Pair.of(level, redstonefeed);
  }

}

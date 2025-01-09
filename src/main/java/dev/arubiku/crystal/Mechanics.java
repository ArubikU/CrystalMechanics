package dev.arubiku.crystal;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import dev.arubiku.crystal.addons.Addon;
import dev.arubiku.crystal.addons.AddonProvider;
import dev.arubiku.crystal.triggers.Basic;
import dev.arubiku.libs.blocker.FeaturedBlock;
import dev.arubiku.libs.blocker.FeaturedBlock.BlockType;
import dev.arubiku.libs.components.ConfigurationReader;
import dev.arubiku.libs.components.InvokerContext;
import dev.arubiku.libs.components.PackagedComponent;
import dev.arubiku.libs.database.CrystalBlock;
import dev.arubiku.libs.database.CrystalDataManager;
import dev.arubiku.libs.database.YamlCrystalData;
import lombok.Getter;

public class Mechanics extends JavaPlugin {

  public Map<Integer, List<PackagedComponent>> blocks = new HashMap<>();
  public Map<Integer, List<Addon>> blockaddons = new HashMap<>();
  public Map<Integer, String> blocksconfigs = new HashMap<>();
  public Map<String, YamlConfiguration> configurationFiles = new HashMap<>();
  private List<AddonProvider<?>> addons = new ArrayList<>();
  private ConfigurationReader reader = null;
  @Getter
  private CrystalDataManager data = null;

  @Override
  public void onEnable() {
    this.data = new CrystalDataManager(new YamlCrystalData(this));
    this.reader = new ConfigurationReader();

    loadBlocks();
  }

  public ConfigurationSection getSection(int id) {
    String[] path = blocksconfigs.get(id).split(":");
    return configurationFiles.get(path[0]).getConfigurationSection(path[1]);
  }

  public void setupFiles() {
    File configFolder = this.getDataPath().resolve("configs").toFile();
    for (File file : getFiles(configFolder)) {
      String path = file.getAbsolutePath();
      if (path.endsWith(".yaml")
          || path.endsWith(".yml")) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        configurationFiles.put(path, config);
      }
    }
  }

  public List<File> getFiles(File file) {
    List<File> files = new ArrayList<>();
    for (File subfile : file.listFiles()) {
      if (subfile.isDirectory()) {
        files.addAll(getFiles(subfile));

      } else {
        files.add(subfile);
      }

    }
    return files;
  }

  public void loadBlocks() {
    data.getData().getAllBlocks().forEach(block -> {
      configurationFiles.forEach((path, config) -> {
        for (String sectionKey : config.getKeys(false)) {
          ConfigurationSection section = config.getConfigurationSection(sectionKey);
          if (!section.contains("Type"))
            continue;

          final FeaturedBlock[] fblock = new FeaturedBlock[1];
          Location loc = new Location(Bukkit.getWorld(block.getWorld()), block.getX(), block.getY(), block.getZ());
          if (block.getType() == BlockType.BLOCK) {
            fblock[0] = FeaturedBlock.getBlockByLocation(
                loc);
          } else {
            loc.getNearbyEntities(1, 1, 1).forEach(entity -> {
              if (FeaturedBlock.isValidEntity(entity)) {
                fblock[0] = FeaturedBlock.getBlockByEntity(entity);
              }
            });
          }

          if (section.getString("Type").equalsIgnoreCase(fblock[0].getNamespacedKey())) {
            blocksconfigs.put(block.getId(), path + ":" + sectionKey);
            break;
          }

          addons.forEach(addon -> {
            if (addon.isValidB(section)) {
              List<Addon> addons = new ArrayList<>();
              if (blockaddons.containsKey(block.getId())) {
                addons = blockaddons.get(block.getId());
              }
              Addon add = addon.setup(block);
              add.setup();
              addons.add(add);

              blockaddons.put(block.getId(), addons);
            }
          });
          if (section.contains("Components")) {
            List<String> components = section.getStringList("Components");
            List<PackagedComponent> comps = new ArrayList<>();
            components.forEach(compi -> {
              reader.parseComponentConfig(compi).ifPresent(a -> {
                comps.add(a);
              });
            });
            blocks.put(block.getId(), comps);

          }
        }
      });
    });
  }

  public void removeBlock(Location loc, InvokerContext context) {
    int id = data.getData().getIdWhereCordinates(loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(),
        loc.getBlockZ());
    if (id == 0)
      return;
    data.getData().removeKey(id);
    if (blocks.containsKey(id)) {
      blocks.forEach((ide, actions) -> {
        actions.forEach(comp -> {
          if (Basic.REMOVE.matchesAlias(comp.getTrigger())) {
            comp.getComponent().execute(context, comp.getTargeter());
          }
        });
      });
      blocks.remove(id);
    }
    if (blockaddons.containsKey(id)) {

      blockaddons.get(id).forEach(addon -> {
        addon.unsetup();
      });
      blockaddons.remove(id);
    }
    if (blocksconfigs.containsKey(id)) {
      blocksconfigs.remove(id);
    }
  }

  public void createBlock(FeaturedBlock block, InvokerContext context) {
    CrystalBlock cblock = data.addCrystalBlock(block);

    configurationFiles.forEach((path, config) -> {
      for (String sectionKey : config.getKeys(false)) {
        ConfigurationSection section = config.getConfigurationSection(sectionKey);
        if (!section.contains("Type"))
          continue;
        if (section.getString("Type").equalsIgnoreCase(block.getNamespacedKey())) {
          blocksconfigs.put(cblock.getId(), path + ":" + sectionKey);
          addons.forEach(addon -> {
            if (addon.isValidB(section)) {
              List<Addon> addons = new ArrayList<>();
              if (blockaddons.containsKey(cblock.getId())) {
                addons = blockaddons.get(cblock.getId());
              }
              Addon add = addon.setup(cblock);
              add.setup();
              addons.add(add);

              blockaddons.put(cblock.getId(), addons);
            }
          });
          if (section.contains("Components")) {
            List<String> components = section.getStringList("Components");
            List<PackagedComponent> comps = new ArrayList<>();
            components.forEach(compi -> {
              reader.parseComponentConfig(compi).ifPresent(a -> {
                if (Basic.PLACE.matchesAlias(a.getTrigger())) {
                  a.getComponent().execute(context, a.getTargeter());
                }
                comps.add(a);
              });
            });
            blocks.put(cblock.getId(), comps);

          }
          return;
        }
      }
    });
  }

}

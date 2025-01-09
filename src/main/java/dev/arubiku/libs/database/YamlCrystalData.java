package dev.arubiku.libs.database;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import dev.arubiku.libs.blocker.FeaturedBlock.BlockType;

public class YamlCrystalData extends CrystalData {
  private final JavaPlugin plugin;
  private final File blocksFile;
  private final File metadataFile;
  private FileConfiguration blocksConfig;
  private FileConfiguration metadataConfig;

  public YamlCrystalData(JavaPlugin plugin) {
    this.plugin = plugin;
    this.blocksFile = new File(plugin.getDataFolder(), "blocks.yml");
    this.metadataFile = new File(plugin.getDataFolder(), "metadata.yml");
    loadConfigurations();
  }

  private void loadConfigurations() {
    if (!blocksFile.exists()) {
      plugin.saveResource("blocks.yml", false);
    }
    if (!metadataFile.exists()) {
      plugin.saveResource("metadata.yml", false);
    }
    blocksConfig = YamlConfiguration.loadConfiguration(blocksFile);
    metadataConfig = YamlConfiguration.loadConfiguration(metadataFile);
  }

  @Override
  public CrystalBlock fetchBlockFromDatabase(int id) {
    String path = "blocks." + id;
    if (blocksConfig.contains(path)) {
      int x = blocksConfig.getInt("blocks." + id + ".x");
      int y = blocksConfig.getInt("blocks." + id + ".y");
      int z = blocksConfig.getInt("blocks." + id + ".z");
      String world = blocksConfig.getString("blocks." + id + ".world");
      String type = blocksConfig.getString("blocks." + id + ".type");
      return new CrystalBlock(id, x, y, z, world, BlockType.valueOf(type));
    }
    return null;
  }

  @Override
  public CrystalMetadata fetchMetadataFromDatabase(int id) {
    String path = "metadata." + id;
    if (metadataConfig.contains(path)) {
      ConfigurationSection section = metadataConfig.getConfigurationSection(path);
      JsonObject jsonObject = new JsonObject();
      for (String key : section.getKeys(false)) {
        jsonObject.add(key, new JsonPrimitive(section.getString(key)));
      }
      return new CrystalMetadata(id, jsonObject);
    }
    return null;
  }

  @Override
  public List<CrystalBlock> fetchAllBlocksFromDatabase() {
    List<CrystalBlock> blocks = new ArrayList<>();
    for (String key : blocksConfig.getConfigurationSection("blocks").getKeys(false)) {
      int id = Integer.parseInt(key);
      int x = blocksConfig.getInt("blocks." + key + ".x");
      int y = blocksConfig.getInt("blocks." + key + ".y");
      int z = blocksConfig.getInt("blocks." + key + ".z");
      String world = blocksConfig.getString("blocks." + key + ".world");
      String type = blocksConfig.getString("blocks." + key + ".type");
      blocks.add(new CrystalBlock(id, x, y, z, world, BlockType.valueOf(type)));
    }
    return blocks;
  }

  @Override
  public List<CrystalMetadata> fetchAllMetadataFromDatabase() {
    List<CrystalMetadata> metadataList = new ArrayList<>();
    for (String key : metadataConfig.getConfigurationSection("metadata").getKeys(false)) {
      int id = Integer.parseInt(key);
      ConfigurationSection section = metadataConfig.getConfigurationSection("metadata." + id);
      JsonObject jsonObject = new JsonObject();
      for (String keytwo : section.getKeys(false)) {
        jsonObject.add(keytwo, new JsonPrimitive(section.getString(key)));
      }
      metadataList.add(new CrystalMetadata(Integer.valueOf(key), jsonObject));
    }
    return metadataList;
  }

  @Override
  public void saveBlockToDatabase(CrystalBlock block) {
    String path = "blocks." + block.getId();
    blocksConfig.set(path + ".name", block.getType());
    blocksConfig.set(path + ".x", block.getX());
    blocksConfig.set(path + ".y", block.getY());
    blocksConfig.set(path + ".z", block.getZ());
    blocksConfig.set(path + ".world", block.getWorld());
    // save x y z and world
    try {
      blocksConfig.save(blocksFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void saveMetadataToDatabase(CrystalMetadata metadata) {
    String path = "metadata." + metadata.getId();
    metadataConfig.set(path, metadata.getData());
    try {
      metadataConfig.save(metadataFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public boolean keyExists(int id) {
    return blocksConfig.contains("blocks." + id);
  }

  @Override
  public void removeKey(int id) {
    blocksConfig.set("blocks." + id, null);
    metadataConfig.set("metadata." + id, null);
  }

  @Override
  public int getIdWhereCordinates(String world, int x, int y, int z) {
    for (String id : blocksConfig.getKeys(false)) {
      String blockWorld = blocksConfig.getString("blocks." + id + ".world");
      if (!blockWorld.equals(world))
        continue;
      int blockX = Integer.valueOf(blocksConfig.getString("blocks." + id + ".x"));
      if (!(blockX == x))
        continue;
      int blockZ = Integer.valueOf(blocksConfig.getString("blocks." + id + ".z"));
      if (!(blockZ == z))
        continue;
      int blockY = Integer.valueOf(blocksConfig.getString("blocks." + id + ".y"));
      if (!(blockY == y))
        continue;
      return Integer.valueOf(id);
    }

    return 0;
  }
}

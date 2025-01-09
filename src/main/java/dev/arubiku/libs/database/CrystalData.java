package dev.arubiku.libs.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.block.BlockFace;

import com.google.gson.Gson;

public abstract class CrystalData {
  public abstract boolean keyExists(int id);

  protected static final Gson gson = new Gson();

  public boolean keyInCache(int id) {
    return blockCache.containsKey(id);
  }

  public abstract void removeKey(int id);

  // Cache for Blocks and CrystalMetadata
  private final Map<String, Integer> blockCordinates = new HashMap<>();
  private final Map<Integer, CrystalBlock> blockCache = new HashMap<>();
  private final Map<Integer, CrystalMetadata> metadataCache = new HashMap<>();

  // Abstract methods to be implemented for actual data fetching and storage
  public abstract CrystalBlock fetchBlockFromDatabase(int id);

  public abstract CrystalMetadata fetchMetadataFromDatabase(int id);

  public abstract List<CrystalBlock> fetchAllBlocksFromDatabase();

  public abstract List<CrystalMetadata> fetchAllMetadataFromDatabase();

  public abstract int getIdWhereCordinates(String world, int x, int y, int z);

  public int getIdWhereCords(String world, int x, int y, int z) {
    String key = world + "_" + x + "_" + y + "_" + z;
    if (blockCordinates.containsKey(key)) {
      return blockCordinates.get(key);
    }
    return 0;
  }

  public abstract void saveBlockToDatabase(CrystalBlock block);

  public abstract void saveMetadataToDatabase(CrystalMetadata metadata);

  // Public method to get a CrystalBlock by ID with caching
  public CrystalBlock getBlock(int id) {
    return blockCache.computeIfAbsent(id, this::fetchBlockFromDatabase);
  }

  // Public method to get CrystalMetadata by ID with caching
  public CrystalMetadata getMetadata(int id) {
    return metadataCache.computeIfAbsent(id, this::fetchMetadataFromDatabase);
  }

  // Method to get all Blocks with caching
  public List<CrystalBlock> getAllBlocks() {
    if (blockCache.isEmpty()) {
      List<CrystalBlock> blocks = fetchAllBlocksFromDatabase();
      for (CrystalBlock block : blocks) {
        blockCache.put(block.getId(), block);
      }
    }
    return new ArrayList<>(blockCache.values());
  }

  // Method to get all CrystalMetadata with caching
  public List<CrystalMetadata> getAllMetadata() {
    if (metadataCache.isEmpty()) {
      List<CrystalMetadata> metadataList = fetchAllMetadataFromDatabase();
      for (CrystalMetadata metadata : metadataList) {
        metadataCache.put(metadata.getId(), metadata);
      }
    }
    return new ArrayList<>(metadataCache.values());
  }

  // Method to update a CrystalBlock and cache it
  public void updateBlock(CrystalBlock block) {
    blockCache.put(block.getId(), block);
    saveBlockToDatabase(block);
  }

  // Method to update CrystalMetadata and cache it
  public void updateMetadata(CrystalMetadata metadata) {
    metadataCache.put(metadata.getId(), metadata);
    saveMetadataToDatabase(metadata);
  }

  // Optional: Clear the cache if needed
  public void clearCache() {
    blockCache.clear();
    metadataCache.clear();
  }

  public List<CrystalBlock> moveBlocks(BlockFace direction, CrystalBlock... blocks) {
    List<CrystalBlock> movedBlocks = new ArrayList<>();
    for (CrystalBlock block : blocks) {
      int x = block.getX();
      int y = block.getY();
      int z = block.getZ();
      switch (direction) {
        case DOWN:
          y--;
          break;
        case UP:
          y++;
          break;
        case NORTH:
          z--;
          break;
        case SOUTH:
          z++;
          break;
        case WEST:
          x--;
          break;
        case EAST:
          x++;
          break;
        default:
          // If an invalid direction is provided, skip this block
          continue;
      }

      // Check if the new position is already occupied
      int existingBlockId = getIdWhereCordinates(block.getWorld(), x, y, z);
      if (existingBlockId != 0) {
        // If the position is occupied, skip this block
        continue;
      }

      // Create a new block with updated coordinates
      CrystalBlock movedBlock = new CrystalBlock(block.getId(), x, y, z, block.getWorld(), block.getType());

      // Update the block in the database and cache
      updateBlock(movedBlock);
      movedBlocks.add(movedBlock);
    }
    return movedBlocks;
  }
}

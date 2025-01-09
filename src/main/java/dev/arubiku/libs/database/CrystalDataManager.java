package dev.arubiku.libs.database;

import java.util.Random;
import java.util.function.Function;

import com.google.gson.JsonObject;

import dev.arubiku.libs.blocker.FeaturedBlock;
import lombok.Getter;

public class CrystalDataManager {
  @Getter
  public CrystalData data;
  public static final Random random = new Random(786734384);

  public CrystalDataManager(CrystalData data) {
    this.data = data;
  }

  public int generateId() {
    int id = random.nextInt();
    while (data.keyInCache(id)) {
      id = random.nextInt();
    }
    return id;

  }

  public CrystalBlock addCrystalBlock(FeaturedBlock block) {
    CrystalBlock metablock = new CrystalBlock(generateId(), block.getLocation(), block.getBlockType());
    data.updateBlock(metablock);
    return metablock;
  }

  public void updateMeta(int id, MetaUpdater... updates) {
    if (!data.keyInCache(id))
      return;
    CrystalMetadata da = data.getMetadata(id);
    JsonObject object = da.getData();
    for (MetaUpdater update : updates) {
      object = update.executor.apply(object);
    }
    da.setData(object);
    data.updateMetadata(da);
  }

  public static class MetaUpdater {
    public Function<JsonObject, JsonObject> executor;

    public MetaUpdater(Function<JsonObject, JsonObject> executor) {
      this.executor = executor;
    }

  }

}

package dev.arubiku.libs.database;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class CrystalMetadata {
  private int id;
  private JsonObject data;
  private static final Gson gson = new Gson();

  public CrystalMetadata(int id, JsonObject data) {
    this.id = id;
    this.data = data;
  }

  public int getId() {
    return id;
  }

  public JsonObject getData() {
    return data;
  }

  public void setData(JsonObject data) {
    this.data = data;
  }

  public String getDataAsString() {
    return gson.toJson(data);
  }

  public static CrystalMetadata fromString(int id, String jsonString) {
    JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
    return new CrystalMetadata(id, jsonObject);
  }
}

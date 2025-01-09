package dev.arubiku.libs.database.adapters;

import java.util.function.Function;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import dev.arubiku.libs.database.CrystalDataManager.MetaUpdater;

public class JsonDataImplementation<T> {
  protected Function<String, T> evaluator;
  protected Function<T, String> coder;
  protected String key;

  public JsonDataImplementation(Class<T> clazz, String key, Function<String, T> evaluator, Function<T, String> coder) {
    this.key = key;
    this.evaluator = evaluator;
    this.coder = coder;
  }

  public boolean present(JsonObject object) {
    return object.has(key);
  }

  public T getValue(JsonObject object) {
    return evaluator.apply(object.get(key).getAsString());
  }

  public JsonObject encode(T object, JsonObject object2) {
    if (object2.has(key))
      object2.remove(key);
    object2.add(key, new JsonPrimitive(this.coder.apply(object)));
    return object2;
  }

  public MetaUpdater updater(T object) {
    // public MetaUpdater(Function<JsonObject, Void> executor)
    return new MetaUpdater((JsonObject object2) -> {
      if (object2.has(key))
        object2.remove(key);
      object2.add(key, new JsonPrimitive(this.coder.apply(object)));
      return object2;
    });
  }
}

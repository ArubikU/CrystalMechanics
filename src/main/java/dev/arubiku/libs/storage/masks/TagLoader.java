package dev.arubiku.libs.storage.masks;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.Tag;

public class TagLoader {

  public static Map<String, Tag<Material>> loadTags() {
    Map<String, Tag<Material>> tagMap = new HashMap<>();

    // Obtener todas las variables estáticas de la clase Tag
    Field[] fields = Tag.class.getDeclaredFields();

    for (Field field : fields) {
      // Comprobar si el tipo del campo es Tag<Material>
      if (Tag.class.isAssignableFrom(field.getType())) {
        try {
          // Hacer accesible el campo si es privado
          field.setAccessible(true);

          @SuppressWarnings("unchecked")
          Tag<Material> tag = (Tag<Material>) field.get(null); // Obtener el valor estático
          tagMap.put(field.getName().toUpperCase(), tag); // Guardar en el mapa con el nombre del campo
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
      }
    }

    return tagMap;
  }
}

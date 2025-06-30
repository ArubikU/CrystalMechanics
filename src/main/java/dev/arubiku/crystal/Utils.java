package dev.arubiku.crystal;

import dev.arubiku.crystal.addons.AddonProvider;
import dev.arubiku.crystal.addons.StorageAddonimplements;

public class Utils {

  public static void setupData(Mechanics plugin) {
    AddonProvider<StorageAddonimplements> storageProvider = new AddonProvider<StorageAddonimplements>((conf) -> {
      return conf.contains("Addons.storage");
    }, plugin);

    plugin.addons.add(storageProvider);
  }

}

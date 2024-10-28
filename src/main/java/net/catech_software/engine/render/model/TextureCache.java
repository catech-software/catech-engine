package net.catech_software.engine.render.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TextureCache {
  HashMap<String, Texture> cache = new HashMap<>();

  public Texture get(String path) {
    if (!this.cache.containsKey(path)) this.cache.put(path, new Texture(path));
    return this.cache.get(path);
  }

  public void free(String path) {
    if (this.cache.containsKey(path)) {
      this.cache.get(path).free();
      this.cache.remove(path);
    }
  }

  public void free() {
    List<String> paths = new ArrayList<>(this.cache.keySet());

    for (int i = 0; i < paths.size(); i++) this.free(paths.get(i));
    this.cache = null;
  }
}

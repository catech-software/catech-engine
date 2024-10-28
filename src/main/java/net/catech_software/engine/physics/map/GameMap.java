package net.catech_software.engine.physics.map;

import net.catech_software.engine.render.model.Model;
import org.joml.Matrix4f;

public abstract class GameMap {
  protected Model model;

  public GameMap(Model model) {
    this.model = model;
  }

  public GameMap() {
    this(null);
  }

  public void draw(int model) {
    if (this.model != null) this.model.draw(model, new Matrix4f());
  }

  public void free() {
    this.model = null;
  }
}

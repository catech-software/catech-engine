package net.catech_software.engine.physics.map;

import org.joml.*;

import net.catech_software.engine.physics.object.GameObject;
import net.catech_software.engine.render.model.Model;

public abstract class GameMap {
  public Vector3f minBoundingBox = new Vector3f();
  public Vector3f maxBoundingBox = new Vector3f();
  protected Model model;

  public GameMap(Model model) {
    this.model = model;
  }

  public GameMap() {
    this(null);
  }

  public boolean collision(GameObject object) {
    Vector3f min = new Vector3f(object.minBoundingBox).add(object.position);
    Vector3f max = new Vector3f(object.maxBoundingBox).add(object.position);

    return this.minBoundingBox.x() <= max.x() &&
           this.maxBoundingBox.x() >= min.x() &&
           this.minBoundingBox.y() <= max.y() &&
           this.maxBoundingBox.y() >= min.y() &&
           this.minBoundingBox.z() <= max.z() &&
           this.maxBoundingBox.z() >= min.z();
  }

  public void draw(int model) {
    if (this.model != null) this.model.draw(model, new Matrix4f());
  }

  public void free() {
    this.model = null;
  }
}

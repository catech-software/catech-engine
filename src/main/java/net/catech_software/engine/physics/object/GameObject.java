package net.catech_software.engine.physics.object;

import org.joml.*;

import net.catech_software.engine.render.model.Model;

public abstract class GameObject {
  public Vector3f position = new Vector3f();
  public Vector3f prevPosition = new Vector3f();
  public Quaternionf rotation = new Quaternionf();
  public Quaternionf prevRotation = new Quaternionf();
  protected Model model;

  public GameObject(Model model) {
    this.model = model;
  }

  public GameObject() {
    this(null);
  }

  public void draw(int model, double alpha) {
    if (this.model != null) {
      Matrix4f prevModel, currModel;

      prevModel = new Matrix4f().rotate(this.prevRotation).translate(this.prevPosition);
      currModel = new Matrix4f().rotate(this.rotation).translate(this.position);

      this.model.draw(model, prevModel.lerp(currModel, (float) alpha));
    }
  }

  public void free() {
    this.model = null;
  }
}

package net.catech_software.engine.physics.object;

import org.joml.*;

import net.catech_software.engine.render.model.Model;

public abstract class GameObject {
  public Vector3f position = new Vector3f();
  public Vector3f prevPosition = new Vector3f();
  public Vector3f velocity = new Vector3f();
  public Vector3f force = new Vector3f();
  public float mass = 0f;
  public Quaternionf rotation = new Quaternionf();
  public Quaternionf prevRotation = new Quaternionf();
  public Vector3f minBoundingBox = new Vector3f();
  public Vector3f maxBoundingBox = new Vector3f();
  protected Model model;

  public GameObject(Model model) {
    this.model = model;
  }

  public GameObject() {
    this(null);
  }

  public void applyForces(double delta) {
    this.velocity.add(new Vector3f(this.force).div(this.mass).mul((float) delta));
    this.position.add(new Vector3f(this.velocity).mul((float) delta));
    this.force = new Vector3f();
  }

  public boolean collision(GameObject object) {
    Vector3f thisMin = new Vector3f(this.minBoundingBox).add(this.position);
    Vector3f thisMax = new Vector3f(this.maxBoundingBox).add(this.position);
    Vector3f objectMin = new Vector3f(object.minBoundingBox).add(object.position);
    Vector3f objectMax = new Vector3f(object.maxBoundingBox).add(object.position);

    return thisMin.x() <= objectMax.x() &&
           thisMax.x() >= objectMin.x() &&
           thisMin.y() <= objectMax.y() &&
           thisMax.y() >= objectMin.y() &&
           thisMin.z() <= objectMax.z() &&
           thisMax.z() >= objectMin.z();
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

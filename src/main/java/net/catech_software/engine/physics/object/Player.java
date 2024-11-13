package net.catech_software.engine.physics.object;

import org.joml.*;

public class Player extends GameObject {
  public Quaternionf camera = new Quaternionf();
  public Quaternionf prevCamera = new Quaternionf();

  public Player() {
    this.mass = 70f;
    this.minBoundingBox = new Vector3f(-0.1945f, 0f, -0.1945f);
    this.maxBoundingBox = new Vector3f(0.1945f, 1.695f, 0.1945f);
  }
}

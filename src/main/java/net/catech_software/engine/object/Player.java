package net.catech_software.engine.object;

import org.joml.*;

public class Player {
  public Vector3f position = new Vector3f();
  public Vector3f prevPosition = new Vector3f();
  public Quaternionf rotation = new Quaternionf();
  public Quaternionf prevRotation = new Quaternionf();
  public Quaternionf camera = new Quaternionf();
  public Quaternionf prevCamera = new Quaternionf();
}

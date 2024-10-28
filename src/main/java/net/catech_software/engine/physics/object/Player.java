package net.catech_software.engine.physics.object;

import org.joml.*;

public class Player extends GameObject {
  public Quaternionf camera = new Quaternionf();
  public Quaternionf prevCamera = new Quaternionf();
}

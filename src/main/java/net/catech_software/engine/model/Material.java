package net.catech_software.engine.model;

import java.nio.IntBuffer;

import org.lwjgl.assimp.*;

public class Material {
  private AIMaterial material;

  public Material(AIMaterial material) {
    this.material = material;

    try (AIString path = AIString.create()) {
      Assimp.aiGetMaterialTexture(material, Assimp.aiTextureType_BASE_COLOR, 0, path, (IntBuffer) null, null, null, null, null, null);
      System.out.printf("%s\n", path.dataString());
    }
  }

  public void free() {
    this.material = null;
  }

  public AIMaterial getMaterial() {
    return material;
  }
}

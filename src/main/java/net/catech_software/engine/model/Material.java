package net.catech_software.engine.model;

import java.nio.IntBuffer;

import org.lwjgl.assimp.*;

public class Material {
  private AIMaterial material;
  private Texture baseColorTex;

  public Material(AIMaterial material) {
    this.material = material;

    try (AIString path = AIString.create()) {
      Assimp.aiGetMaterialTexture(this.material, Assimp.aiTextureType_BASE_COLOR, 0, path, (IntBuffer) null, null, null, null, null, null);
      this.baseColorTex = !path.dataString().isEmpty() ? new Texture(path.dataString()) : null;
    }
  }

  public void free() {
    if (this.baseColorTex != null) this.baseColorTex.free();
    this.baseColorTex = null;
    this.material = null;
  }

  public Texture getBaseColorTex() {
    return this.baseColorTex;
  }
}

package net.catech_software.engine.render.model;

import java.nio.IntBuffer;

import org.lwjgl.assimp.*;

public class Material {
  private AIMaterial material;
  private Texture baseColorTex;
  private Texture emissiveTex;
  private Texture normalTex;
  private Texture occlusionRoughnessMetallicTex;

  public Material(AIMaterial material, TextureCache cache) {
    this.material = material;

    try (AIString path = AIString.create()) {
      Assimp.aiGetMaterialTexture(this.material, Assimp.aiTextureType_BASE_COLOR, 0, path, (IntBuffer) null, null, null, null, null, null);
      this.baseColorTex = !path.dataString().isEmpty() ? cache.get(path.dataString()) : Texture.blank;

      Assimp.aiGetMaterialTexture(this.material, Assimp.aiTextureType_EMISSIVE, 0, path, (IntBuffer) null, null, null, null, null, null);
      this.emissiveTex = !path.dataString().isEmpty() ? cache.get(path.dataString()) : Texture.blank;

      Assimp.aiGetMaterialTexture(this.material, Assimp.aiTextureType_NORMALS, 0, path, (IntBuffer) null, null, null, null, null, null);
      this.normalTex = !path.dataString().isEmpty() ? cache.get(path.dataString()) : Texture.blank;

      Assimp.aiGetMaterialTexture(this.material, Assimp.aiTextureType_UNKNOWN, 0, path, (IntBuffer) null, null, null, null, null, null);
      this.occlusionRoughnessMetallicTex = !path.dataString().isEmpty() ? cache.get(path.dataString()) : Texture.blank;
    }
  }

  public void free() {
    if (this.baseColorTex != null) this.baseColorTex.free();
    if (this.emissiveTex != null) this.emissiveTex.free();
    if (this.normalTex != null) this.normalTex.free();
    if (this.occlusionRoughnessMetallicTex != null) this.occlusionRoughnessMetallicTex.free();
    this.baseColorTex = null;
    this.emissiveTex = null;
    this.normalTex = null;
    this.occlusionRoughnessMetallicTex = null;
    this.material = null;
  }

  public Texture getBaseColorTex() {
    return this.baseColorTex;
  }

  public Texture getEmissiveTex() {
    return this.emissiveTex;
  }

  public Texture getNormalTex() {
    return this.normalTex;
  }

  public Texture getOcclusionRoughnessMetallicTex() {
    return this.occlusionRoughnessMetallicTex;
  }
}

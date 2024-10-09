package net.catech_software.engine.model;

import java.util.ArrayList;

import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;

public class Model {
  private AIScene scene;
  private ArrayList<Material> materials;
  private ArrayList<Mesh> meshes;

  public Model(AIScene scene) {
    PointerBuffer buffer;

    this.scene = scene;

    buffer = this.scene.mMaterials();
    if (buffer == null) throw new RuntimeException("Could not load materials");
    this.materials = new ArrayList<>();
    for (int i = 0; i < this.scene.mNumMaterials(); i++) this.materials.add(new Material(AIMaterial.create(buffer.get(i))));

    buffer = this.scene.mMeshes();
    if (buffer == null) throw new RuntimeException("Could not load meshes");
    this.meshes = new ArrayList<>();
    for (int i = 0; i < this.scene.mNumMeshes(); i++) this.meshes.add(new Mesh(AIMesh.create(buffer.get(i)), this.materials));
  }

  public void draw() {
    for (int i = 0; i < this.meshes.size(); i++) this.meshes.get(i).draw();
  }

  public void free() {
    for (int i = 0; i < this.meshes.size(); i++) this.meshes.get(i).free();
    for (int i = 0; i < this.materials.size(); i++) this.materials.get(i).free();
    Assimp.aiReleaseImport(this.scene);
    this.meshes = null;
    this.materials = null;
    this.scene = null;
  }
}

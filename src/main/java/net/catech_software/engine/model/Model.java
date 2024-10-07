package net.catech_software.engine.model;

import java.util.*;

import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;

public class Model {
  private AIScene scene;
  private List<Mesh> meshes;

  public Model(AIScene scene) {
    int count;
    PointerBuffer buffer;

    this.scene = scene;

    count = scene.mNumMeshes();
    buffer = scene.mMeshes();
    if (buffer == null) throw new RuntimeException("Could not load meshes");
    this.meshes = new ArrayList<>();
    for (int i = 0; i < count; i++) this.meshes.add(new Mesh(AIMesh.create(buffer.get(i))));
  }

  public void draw() {
    for (int i = 0; i < this.meshes.size(); i++) this.meshes.get(i).draw();
  }

  public void free() {
    for (int i = 0; i < this.meshes.size(); i++) this.meshes.get(i).free();
    Assimp.aiReleaseImport(this.scene);
    this.scene = null;
    this.meshes = null;
  }

  public AIScene getScene() {
    return scene;
  }

  public Mesh getMesh(int index) {
    return meshes.get(index);
  }
}

package net.catech_software.engine.render.model;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.lwjgl.assimp.*;

public class Model {
  private AIScene scene;
  private ArrayList<Material> materials;
  private ArrayList<Mesh> meshes;
  private Node rootNode;

  public Model(AIScene scene, TextureCache cache) {
    this.scene = scene;

    this.materials = new ArrayList<>();
    for (int i = 0; i < this.scene.mNumMaterials(); i++) this.materials.add(new Material(AIMaterial.create(this.scene.mMaterials().get(i)), cache));

    this.meshes = new ArrayList<>();
    for (int i = 0; i < this.scene.mNumMeshes(); i++) this.meshes.add(new Mesh(AIMesh.create(this.scene.mMeshes().get(i)), this.materials));

    this.rootNode = new Node(this.scene.mRootNode(), new Matrix4f(), this.meshes);
  }

  public void draw(int model, Matrix4f transformation) {
    this.rootNode.draw(model, transformation);
  }

  public void free() {
    this.rootNode.free();
    for (int i = 0; i < this.meshes.size(); i++) this.meshes.get(i).free();
    for (int i = 0; i < this.materials.size(); i++) this.materials.get(i).free();
    Assimp.aiReleaseImport(this.scene);
    this.rootNode = null;
    this.meshes = null;
    this.materials = null;
    this.scene = null;
  }
}

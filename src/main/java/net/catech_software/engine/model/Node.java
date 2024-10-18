package net.catech_software.engine.model;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.lwjgl.assimp.*;

public class Node {
  private AINode node;
  private Matrix4f localTransform;
  private Matrix4f globalTransform = new Matrix4f();
  private ArrayList<Mesh> meshes;
  private ArrayList<Node> children;

  public Node(AINode node, Matrix4f transformation, ArrayList<Mesh> meshes) {
    this.node = node;
    this.localTransform = aiMatrixToJOML(this.node.mTransformation());
    transformation.mul(this.localTransform, this.globalTransform);

    this.meshes = new ArrayList<>();
    for (int i = 0; i < this.node.mNumMeshes(); i++) this.meshes.add(meshes.get(this.node.mMeshes().get(i)));

    this.children = new ArrayList<>();
    for (int i = 0; i < this.node.mNumChildren(); i++) this.children.add(new Node(AINode.create(this.node.mChildren().get(i)), this.globalTransform, meshes));
  }

  private static Matrix4f aiMatrixToJOML(AIMatrix4x4 matrix) {
    return new Matrix4f(matrix.a1(), matrix.b1(), matrix.c1(), matrix.d1(),
                        matrix.a2(), matrix.b2(), matrix.c2(), matrix.d2(),
                        matrix.a3(), matrix.b3(), matrix.c3(), matrix.d3(),
                        matrix.a4(), matrix.b4(), matrix.c4(), matrix.d4());
  }

  public void draw(int model, Matrix4f transformation) {
    for (int i = 0; i < this.meshes.size(); i++) this.meshes.get(i).draw(model, new Matrix4f(transformation).mul(this.globalTransform));
    for (int i = 0; i < this.children.size(); i++) this.children.get(i).draw(model, transformation);
  }

  public void free() {
    for (int i = 0; i < this.children.size(); i++) this.children.get(i).free();
    for (int i = 0; i < this.meshes.size(); i++) this.meshes.get(i).free();
    this.children = null;
    this.meshes = null;
    this.globalTransform = null;
    this.localTransform = null;
    this.node = null;
  }
}
